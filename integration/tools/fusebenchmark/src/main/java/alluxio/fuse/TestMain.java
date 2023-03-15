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

package alluxio.fuse;

import alluxio.client.block.stream.SharedGrpcDataReader;
import alluxio.client.file.FileSystem;
import alluxio.client.file.FileSystemContext;
import alluxio.conf.AlluxioConfiguration;
import alluxio.conf.Configuration;
import alluxio.conf.PropertyKey;
import alluxio.fuse.options.FuseOptions;
import alluxio.grpc.ReadRequest;
import alluxio.jnifuse.LibFuse;
import alluxio.jnifuse.struct.FuseFileInfo;
import alluxio.network.protocol.databuffer.NioDirectBufferPool;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.io.FileReader;
import java.util.List;
import java.util.Random;

import com.google.common.base.Throwables;
import com.opencsv.CSVReader;
import jnr.constants.platform.OpenFlags;


/**
 * Main entry point to Alluxio-FUSE.
 */
public final class TestMain {
  AlluxioJniFuseFileSystem mFuseFileSystem;
  
  public TestMain() {
    
  }
  
  public void setup() {
    // SET UP
    Configuration.set(PropertyKey.MASTER_HOSTNAME, "FuseBasic-worker-0");
    Configuration.set(PropertyKey.MASTER_MOUNT_TABLE_ROOT_UFS, "/local2/");

    Configuration.set(PropertyKey.USER_NETTY_DATA_TRANSMISSION_ENABLED, true);
    Configuration.set(PropertyKey.WORKER_NETWORK_NETTY_CHANNEL, "epoll");
    Configuration.set(PropertyKey.WORKER_NETWORK_NETTY_FILE_TRANSFER_TYPE, "TRANSFER");
    Configuration.set(PropertyKey.WORKER_NETWORK_NETTY_BACKLOG, 100);

    Configuration.set(PropertyKey.USER_SHORT_CIRCUIT_ENABLED, false);
    Configuration.set(PropertyKey.DORA_CLIENT_READ_LOCATION_POLICY_ENABLED, true);
    Configuration.set(PropertyKey.DORA_CLIENT_UFS_ROOT, "/local2/");
    Configuration.set(PropertyKey.MASTER_WORKER_REGISTER_LEASE_ENABLED, false);
    
    Configuration.set(PropertyKey.FUSE_JNIFUSE_LIBFUSE_VERSION, 3);
    Configuration.set(PropertyKey.FUSE_MOUNT_POINT, "/mnt/alluxio-fuse");
    
    AlluxioConfiguration conf = Configuration.global();
    LibFuse.loadLibrary(AlluxioFuseUtils.getLibfuseVersion(conf));
    FileSystemContext context = FileSystemContext.create(conf);
    FuseOptions fuseOptions = FuseOptions.create(conf);
    FileSystem fileSystem = FileSystem.Factory.create(context, fuseOptions.getFileSystemOptions());
    mFuseFileSystem
        = new AlluxioJniFuseFileSystem(context, fileSystem, fuseOptions);
    mFuseFileSystem.mount(false, false, new HashSet<>());
  }
  
  public void testConcurrent1() throws IOException {
    String readPatternFile = "/local2/mock_test_read_pattern/con1/dev-arrow-9GB.csv";
    String testFile = "/datasets/dev/dataset.arrow";
    readFile(readPatternFile, testFile);
  }
  
  private void readFile(String readPatternFile, String testFile) throws IOException {
    CSVReader patternReader = new CSVReader(new FileReader(readPatternFile));
    String[] readOp;
    try (AlluxioFuseUtils.CloseableFuseFileInfo info = new AlluxioFuseUtils.CloseableFuseFileInfo()) {
      FuseFileInfo fuseFileInfo = info.get();

      // cannot open non-existing file for read
      fuseFileInfo.flags.set(OpenFlags.O_RDONLY.intValue());
      int ret = mFuseFileSystem.open(testFile, fuseFileInfo);
      if (ret != 0) {
        throw new IOException("Open failed with return code " + ret);
      }
      int size;
      long offset;
      patternReader.readNext(); // skip headers
      long start = System.currentTimeMillis();
      while ((readOp = patternReader.readNext()) != null) {
        size = Integer.parseInt(readOp[0]);
        offset = Long.parseLong(readOp[1]);
        ret = mFuseFileSystem.read(testFile, NioDirectBufferPool.acquire(size), size, offset, info.get());
        if (ret < -1) {
          throw new IOException("Read failed with return code " + ret);
        }
      }
      System.out.println("Total time cost (in seconds): " + ((System.currentTimeMillis() - start)/1000));
    }
  }
  
  public void testConcurrent4() throws InterruptedException {
    String readPatternFileFormat = "/local2/mock_test_read_pattern/con4/dev-arrow-9GB-%d.csv";
    String testFile = "/datasets/dev/dataset.arrow";
    List<Thread> threads = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      String readPatternFile = String.format(readPatternFileFormat, i);
      Thread t = new Thread(() -> {
        try {
          readFile(readPatternFile, testFile);
        } catch (IOException e) {
          System.out.println(e);
        }
      });
      threads.add(t);
    }
    Collections.shuffle(threads);
    for (Thread t : threads) {
      t.start();
    }
    for (Thread t : threads) {
      t.join();
    }
  }

  /**
   * Running this class will mount the file system according to the options passed to this function.
   * The user-space fuse application will stay on the foreground and keep the file system mounted.
   * The user can unmount the file system by gracefully killing (SIGINT) the process.
   *
   * @param args arguments to run the command line
   */
  public static void main(String[] args) throws ParseException, IOException, InterruptedException {
    TestMain main = new TestMain();
    main.setup();
    if (args.length != 0 && Integer.parseInt(args[0]) == 4) {
      main.testConcurrent4();
    } else {
      main.testConcurrent1();
    }
  }
}
