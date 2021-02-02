/*
 * The Alluxio Open Foundation licenses this work under the Apache License, version 2.0
 * (the "License"). You may not use this work except in compliance with the License, which is
 * available at www.apache.org/licenses/LICENSE-2.0
 *
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied, as more fully set forth in the License.
 *
 * See the NOTICE file distributed with this work for information regarding copyright ownership.
 */

package alluxio.cli.fsadmin.embeddedfuse;

import alluxio.cli.CommandUtils;
import alluxio.cli.fs.FileSystemShellUtils;
import alluxio.cli.fsadmin.command.AbstractFsAdminCommand;
import alluxio.cli.fsadmin.command.Context;
import alluxio.client.block.BlockWorkerInfo;
import alluxio.client.block.stream.BlockWorkerClient;
import alluxio.client.file.FileSystemContext;
import alluxio.conf.AlluxioConfiguration;
import alluxio.exception.status.InvalidArgumentException;

import alluxio.grpc.FuseMountInfo;
import alluxio.grpc.MountEmbeddedFuseRequest;
import alluxio.resource.CloseableResource;
import alluxio.util.ThreadFactoryUtils;
import alluxio.wire.WorkerNetAddress;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Clear the leading master (and workers) metrics.
 */
public final class MountCommand extends AbstractFsAdminCommand {
  private static final String WORKERS_OPTION_NAME = "workers";
  private static final String PARALLELISM_OPTION_NAME = "parallelism";
  private static final int DEFAULT_PARALLELISM = 8;

  private static final Option WORKERS_OPTION =
      Option.builder()
          .longOpt(WORKERS_OPTION_NAME)
          .required(false)
          .hasArg(true)
          .desc("Clear metrics of specified workers. "
              + "Pass in the worker hostnames separated by comma")
          .build();
  private static final Option PARALLELISM_OPTION =
      Option.builder()
          .longOpt(PARALLELISM_OPTION_NAME)
          .required(false)
          .hasArg(true)
          .argName("# concurrent operations")
          .desc("Number of concurrent worker metrics clear operations, "
              + "default: " + DEFAULT_PARALLELISM)
          .build();

  private final AlluxioConfiguration mAlluxioConf;

  /**
   * @param context fsadmin command context
   * @param alluxioConf Alluxio configuration
   */
  public MountCommand(Context context, AlluxioConfiguration alluxioConf) {
    super(context);
    mAlluxioConf = alluxioConf;
  }

  @Override
  public String getCommandName() {
    return "mount";
  }

  @Override
  public Options getOptions() {
    return new Options().addOption(WORKERS_OPTION)
        .addOption(PARALLELISM_OPTION);
  }

  @Override
  public void validateArgs(CommandLine cl) throws InvalidArgumentException {
    CommandUtils.checkNumOfArgsNoMoreThan(this, cl, 3);
    CommandUtils.checkNumOfArgsNoLessThan(this, cl, 1);
  }

  @Override
  public int run(CommandLine cl) throws IOException {
    String[] args = cl.getArgs();
    String mountPoint = args[0];
    String alluxioPath = args.length > 1 ? args[1] : "/";

    int globalParallelism = FileSystemShellUtils
        .getIntArg(cl, PARALLELISM_OPTION, DEFAULT_PARALLELISM);

    MountEmbeddedFuseRequest mountRequest = MountEmbeddedFuseRequest.newBuilder()
        .setFuseMountInfo(FuseMountInfo.newBuilder().setMountPoint(mountPoint).setAlluxioPath(alluxioPath).build()).build();
    try (FileSystemContext context = FileSystemContext.create(mAlluxioConf)) {
      List<WorkerNetAddress> addressList = context.getCachedWorkers().stream()
          .map(BlockWorkerInfo::getNetAddress).collect(Collectors.toList());

      if (cl.hasOption(WORKERS_OPTION_NAME)) {
        String workersValue = cl.getOptionValue(WORKERS_OPTION_NAME);
        Set<String> workersRequired = new HashSet<>(Arrays.asList(workersValue.split(",")));
        List<WorkerNetAddress> workersToMount = new ArrayList<>();
        for (WorkerNetAddress worker : addressList) {
          if (workersRequired.contains(worker.getHost())) {
            workersToMount.add(worker);
            workersRequired.remove(worker.getHost());
          }
        }
        if (workersRequired.size() != 0) {
          System.out.printf("Cannot find workers of hostnames %s%n",
              String.join(",", workersRequired));
          System.out.printf("Valid workers include %s%n", addressListToString(addressList));
          return -1;
        }
        if (!mountFuseInWorkers(workersToMount, context, mountRequest, globalParallelism)) {
          System.out.printf("Failed to mount fuse in workers %s%n",
              addressListToString(workersToMount));
          return -1;
        }
      } else {
        if (!mountFuseInWorkers(addressList, context, mountRequest, globalParallelism)) {
          System.out.printf("Failed to mount fuse in workers %s%n",
              addressListToString(addressList));
          return -1;
        }
      }
    }
    return 0;
  }

  private boolean mountFuseInWorkers(List<WorkerNetAddress> workers, FileSystemContext context, MountEmbeddedFuseRequest request, int globalParallelism) throws IOException {
    int workerNum = workers.size();
    if (workerNum == 0) {
      System.out.println("No worker metrics to clear.");
      return true;
    } else if (workerNum == 1) {
      mountFuseInOneWorker(workers.get(0), context, request);
    } else {
      List<Future<Void>> futures = new ArrayList<>();
      int parallelism = Math.min(workerNum, globalParallelism);
      ExecutorService service = Executors.newFixedThreadPool(parallelism,
          ThreadFactoryUtils.build("metrics-clear-cli-%d", true));
      for (WorkerNetAddress worker : workers) {
        futures.add(service.submit(new MountFuseCallable(worker, context, request)));
      }
      try {
        for (Future<Void> future : futures) {
          future.get();
        }
      } catch (ExecutionException e) {
        System.out.println("Fatal error: " + e);
        return false;
      } catch (InterruptedException e) {
        System.out.println("Metrics clearance interrupted, exiting.");
        return false;
      } finally {
        service.shutdownNow();
      }
    }
    return true;
  }

  /**
   * Thread that clears the metrics of a specific worker.
   */
  private class MountFuseCallable implements Callable<Void> {
    private final WorkerNetAddress mWorker;
    private final FileSystemContext mContext;
    private final MountEmbeddedFuseRequest mRequest;

    MountFuseCallable(WorkerNetAddress worker, FileSystemContext context, MountEmbeddedFuseRequest request) {
      mWorker = worker;
      mContext = context;
      mRequest = request;
    }

    @Override
    public Void call() throws Exception {
      mountFuseInOneWorker(mWorker, mContext, mRequest);
      return null;
    }
  }

  /**
   * Clears the worker metrics.
   *
   * @param worker the worker to clear metrics of
   * @param context the file system context
   */
  private void mountFuseInOneWorker(WorkerNetAddress worker,
      FileSystemContext context, MountEmbeddedFuseRequest request) throws IOException {
    try (CloseableResource<BlockWorkerClient> blockWorkerClient =
             context.acquireBlockWorkerClient(worker)) {
      blockWorkerClient.get().mountEmbeddedFuse(request);
    }
    System.out.printf("Successfully mounted fuse of worker %s.%n", worker.getHost());
  }

  /**
   * Get string value of worker address list.
   *
   * @param addressList the addressList to transform
   * @return string value of worker address list
   */
  private String addressListToString(List<WorkerNetAddress> addressList) {
    return Arrays.toString(addressList.stream()
        .map(WorkerNetAddress::getHost).toArray(String[]::new));
  }

  @Override
  public String getUsage() {
    return "mount [--workers <worker_hostnames>] <mount_point> [<alluxio_path>]";
  }

  /**
   * @return command's description
   */
  @VisibleForTesting
  public static String description() {
    return "";
  }

  @Override
  public String getDescription() {
    return description();
  }
}
