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

package alluxio.cli;

import alluxio.AlluxioURI;
import alluxio.client.file.FileOutStream;
import alluxio.client.file.FileSystem;
import alluxio.conf.AlluxioConfiguration;
import alluxio.conf.InstancedConfiguration;
import alluxio.exception.AlluxioException;
import alluxio.grpc.CreateFilePOptions;
import alluxio.util.ConfigurationUtils;
import alluxio.util.io.PathUtils;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class for running an operation multiple times.
 */
public class RunOperation {
  private static final Logger LOG = LoggerFactory.getLogger(RunOperation.class);
  private static final String BASE_DIRECTORY = "/RunOperationDir";

  enum Operation {
    CreateFile,
    CreateEmptyFile,
    CreateAndDeleteEmptyFile,
    ListStatus,
    GetStatus
  }

  @Parameter(names = {"-op", "-operation"},
      description = "the operation to perform. Options are [CreateEmptyFile, "
          + "CreateAndDeleteEmptyFile, CreateFile, ListStatus]",
      required = true)
  private Operation mOperation;
  @Parameter(names = {"-n", "-num"},
      description = "the number of times to perform the operation (total for all threads)")
  private int mTimes = 1;
  @Parameter(names = {"-t", "-threads"}, description = "the number of threads to use")
  private int mThreads = 1;
  @Parameter(names = {"-d", "-dir"}, description = "The directory to perform operations in")
  private String mDir = BASE_DIRECTORY;
  @Parameter(names = {"-s", "-size"},
      description = "The size of a file to create")
  private int mSize = 4096;

  private byte[] mFiledata;

  private final FileSystem mFileSystem;

  /** Remaining number of times that the operation should be performed. */
  private AtomicInteger mRemainingOps;

  /**
   * Tool for running an operation multiple times.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    System.exit(new RunOperation(new InstancedConfiguration(ConfigurationUtils.defaults()))
        .run(args));
  }

  /**
   * Constructs a new {@link RunOperation} object.
   *
   * @param alluxioConf Alluxio configuration
   */
  public RunOperation(AlluxioConfiguration alluxioConf) {
    mFileSystem = FileSystem.Factory.create(alluxioConf);
  }

  /**
   * @param args command line arguments
   * @return the exit status
   */
  public int run(String[] args) {
    JCommander jc = new JCommander(this);
    jc.setProgramName("runOperation");
    try {
      jc.parse(args);
    } catch (Exception e) {
      System.out.println(e.toString());
      System.out.println();
      jc.usage();
      return -1;
    }
    mRemainingOps = new AtomicInteger(mTimes);
    mFiledata = new byte[mSize];
    Arrays.fill(mFiledata, (byte) 0x7A);

    List<Thread> threads = new ArrayList<>();
    for (int i = 0; i < mThreads; i++) {
      threads.add(new OperationThread());
    }
    long start = System.currentTimeMillis();
    for (Thread thread : threads) {
      thread.start();
    }
    for (Thread thread : threads) {
      try {
        thread.join();
      } catch (InterruptedException e) {
        return -1;
      }
    }
    System.out.println("Completed in " + (System.currentTimeMillis() - start) + "ms");
    return 0;
  }

  private final class OperationThread extends Thread {
    private OperationThread() {}

    @Override
    public void run() {
      while (mRemainingOps.decrementAndGet() >= 0) {
        try {
          applyOperation();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }

    private void applyOperation() throws IOException, AlluxioException {
      AlluxioURI uri = new AlluxioURI(PathUtils.concatPath(mDir, UUID.randomUUID()));
      long start = System.nanoTime();
      switch (mOperation) {
        case CreateEmptyFile:
          mFileSystem.createFile(uri).close();
          LOG.info("Create file takes {}", System.nanoTime() - start);
          break;
        case CreateAndDeleteEmptyFile:
          mFileSystem.createFile(uri).close();
          mFileSystem.delete(uri);
          LOG.info("Create and delete empty file takes {}", System.nanoTime() - start);
          break;
        case CreateFile:
          try (FileOutStream file =
              mFileSystem.createFile(uri,
                  CreateFilePOptions.newBuilder().setRecursive(true).build())) {
            file.write(mFiledata);
          }
          LOG.info("Create file takes {}", System.nanoTime() - start);
          break;
        case ListStatus:
          mFileSystem.listStatus(new AlluxioURI(mDir));
          LOG.info("list status takes {}", System.nanoTime() - start);
          break;
        case GetStatus:
          mFileSystem.getStatus(new AlluxioURI(mDir));
          LOG.info("get status takes {}", System.nanoTime() - start);
          break;
        default:
          throw new IllegalStateException("Unknown operation: " + mOperation);
      }
    }
  }
}
