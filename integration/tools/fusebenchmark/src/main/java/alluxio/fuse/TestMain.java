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

import alluxio.client.file.FileSystem;
import alluxio.client.file.FileSystemContext;
import alluxio.conf.AlluxioConfiguration;
import alluxio.conf.Configuration;
import alluxio.conf.PropertyKey;
import alluxio.fuse.options.FuseOptions;
import alluxio.jnifuse.LibFuse;
import alluxio.jnifuse.struct.FuseFileInfo;
import alluxio.network.protocol.databuffer.NioDirectBufferPool;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.io.FileReader;
import java.util.List;

import com.google.common.base.Preconditions;
import com.opencsv.CSVReader;
import jnr.constants.platform.OpenFlags;


/**
 * Main entry point to Alluxio-FUSE.
 */
public final class TestMain {
  AlluxioJniFuseFileSystem mFuseFileSystem;
  private final int mConcurrency;
  private final String mTarget;
  String mMountPoint;
  String mUfs;
  
  public TestMain(int concurrency, String target){
    Preconditions.checkArgument(concurrency == 1 || concurrency == 4);
    Preconditions.checkArgument(target.equals("train") || target.equals("dev") || target.equals("test"));
    mConcurrency = concurrency;
    mTarget = target;
  }
  
  public void run() throws Exception {
    setup();
    if (mConcurrency == 1) {
      testConcurrent1();
    } else {
      testConcurrent4();
    }
  }
  
  public void setup() throws IOException {
    AlluxioConfiguration conf = Configuration.global();
    mUfs = conf.getString(PropertyKey.DORA_CLIENT_UFS_ROOT);
    mMountPoint = conf.getString(PropertyKey.FUSE_MOUNT_POINT);
    LibFuse.loadLibrary(AlluxioFuseUtils.getLibfuseVersion(conf));
    FileSystemContext context = FileSystemContext.create(conf);
    FuseOptions fuseOptions = FuseOptions.create(conf);
    FileSystem fileSystem = FileSystem.Factory.create(context, fuseOptions.getFileSystemOptions());
    mFuseFileSystem
        = new AlluxioJniFuseFileSystem(context, fileSystem, fuseOptions);
    mFuseFileSystem.mount(false, false, new HashSet<>());
  }
  
  public void testConcurrent1() throws IOException {
    String readPatternFile = mTarget.equals("train") ? "train-arrow-36GB.csv" : 
        mTarget.equals("dev") ? "dev-arrow-9GB.csv" : "test-arrow-11GB.csv";
    String testData = Paths.get(mTarget, "dataset.arrow").toString();
    String fullUfsPatternPath = Paths.get(mUfs, "mock_test_read_pattern/con1", readPatternFile).toString();
    readFile(fullUfsPatternPath, testData);
  }
  
  private void readFile(String readPatternFile, String testData) throws IOException {
    CSVReader patternReader = new CSVReader(new FileReader(readPatternFile));
    String[] readOp;
    try (AlluxioFuseUtils.CloseableFuseFileInfo info = new AlluxioFuseUtils.CloseableFuseFileInfo()) {
      FuseFileInfo fuseFileInfo = info.get();

      // cannot open non-existing file for read
      fuseFileInfo.flags.set(OpenFlags.O_RDONLY.intValue());
      int ret = mFuseFileSystem.open(testData, fuseFileInfo);
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
        ret = mFuseFileSystem.read(testData, NioDirectBufferPool.acquire(size), size, offset, info.get());
        if (ret < -1) {
          throw new IOException("Read failed with return code " + ret);
        }
      }
      System.out.println("Total time cost (in seconds): " + ((System.currentTimeMillis() - start)/1000));
    }
  }
  
  public void testConcurrent4() throws InterruptedException {
    String ufsPatternFolder =  Paths.get(mUfs, "mock_test_read_pattern/con4").toString();
    String readPatternFileFormat = mTarget.equals("train") ? ufsPatternFolder + "/train-arrow-36GB-%d.csv" :
        mTarget.equals("dev") ? ufsPatternFolder + "/dev-arrow-9GB-%d.csv" : ufsPatternFolder + "/test-arrow-11GB-%d.csv";
    String testData = Paths.get(mTarget, "dataset.arrow").toString();
    List<Thread> threads = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      String readPatternFile = String.format(readPatternFileFormat, i);
      Thread t = new Thread(() -> {
        try {
          readFile(readPatternFile, testData);
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
  public static void main(String[] args) throws Exception {
    int concurrency = Integer.parseInt(args[0]);
    String testTarget = args[1];
    TestMain main = new TestMain(concurrency, testTarget);
    main.run();
  }
}
