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
import alluxio.conf.AlluxioConfiguration;
import alluxio.conf.Configuration;
import alluxio.conf.PropertyKey;
import alluxio.PositionReader;
import alluxio.AlluxioURI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Main entry point to Alluxio-FUSE.
 */
public final class TestMain {
  private static final int BUFFER_SIZE = 10240;
  private static final CountDownLatch startSignal = new CountDownLatch(1);
  private static CountDownLatch doneSignal;
  private static int numThreads;
  private static int numFiles;
  private static String testFolder;
  private static String testFileFormat;
  private static volatile boolean stopReading;
  private static List<Long> threadBytesRead;
  private static volatile long stopReadingTime;

  public static void main(String[] args) throws InterruptedException {
    parseArgs(args);
    threadBytesRead = Collections.synchronizedList(new ArrayList<Long>(numThreads));
    AlluxioConfiguration conf = Configuration.global();
    FileSystem fileSystem = FileSystem.Factory
        .create(conf);
    ExecutorService executor = Executors.newFixedThreadPool(numThreads);
    for (int i = 0; i < numThreads; i++) {
      executor.execute(new FileReaderTask(startSignal, doneSignal, i, numFiles, fileSystem));
    }
    Thread.sleep(10);
    long startTime = System.currentTimeMillis();
    startSignal.countDown(); // start all threads at the same time
    doneSignal.await(); // wait for all threads to finish
    printStats(startTime, stopReadingTime, threadBytesRead);
    executor.shutdown();
  }

  private static void parseArgs(String[] args) {
    for (String arg : args) {
      if (arg.startsWith("--num_threads=")) {
        numThreads = Integer.parseInt(arg.substring("--num_threads=".length()));
        doneSignal = new CountDownLatch(numThreads);
      } else if (arg.startsWith("--num_files=")) {
        numFiles = Integer.parseInt(arg.substring("--num_files=".length()));
      } else if (arg.startsWith("--test_folder=")) {
        testFolder = arg.substring("--test_folder=".length());
        testFileFormat = testFolder + "/%s/file.0.%d";
      }
    }

    if (numThreads == 0 || numFiles == 0 || testFolder == null || testFolder.isEmpty()) {
      System.err.println("Usage: java TestMain --num_threads=<num_threads> --num_files=<num_files> --test_folder=<folder_test_file_exist>");
      System.exit(1);
    }
  }

  private static void printStats(long startTime, long endTime, List<Long> threadTimes) {
    long totalTime = endTime - startTime;
    long totalBytesRead = threadTimes.stream().mapToLong(t -> t).sum();
    double throughput = (double) totalBytesRead / totalTime * 1000;
    System.out.printf("Total time: %dms\n", totalTime);
    System.out.printf("Total bytes read: %d\n", totalBytesRead);
    System.out.printf("Throughput: %.2f bytes/second\n", throughput);
  }

  static class FileReaderTask implements Runnable {
    private final CountDownLatch startSignal;
    private final CountDownLatch doneSignal;
    private int threadId;
    private int numFiles;
    private FileSystem fileSystem;

    public FileReaderTask(CountDownLatch startSignal, CountDownLatch doneSignal, int threadId, int numFiles, FileSystem fileSystem) {
      this.startSignal = startSignal;
      this.doneSignal = doneSignal;
      this.threadId = threadId;
      this.numFiles = numFiles;
      this.fileSystem = fileSystem;
    }

    @Override
    public void run() {
      try {
        startSignal.await(); // wait for all threads to start
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
      long totalBytesRead = 0;
      byte[] buffer = new byte[BUFFER_SIZE];
      long startTime = System.currentTimeMillis();
      try {
        for (int i = 0; i < numFiles; i++) {
          if (stopReading) {
            break;
          }
          AlluxioURI uri = new AlluxioURI(String.format(testFileFormat, threadId, i));
          try (PositionReader reader = fileSystem.openPositionRead(uri)) {
            int bytesRead = reader.read(0, buffer, 0, BUFFER_SIZE);
            if (bytesRead < 0) {
              System.out.printf("error reading from file %s", uri.toString());
            }
            if (bytesRead > 0) {
              totalBytesRead += bytesRead;
            }
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      long endTime = System.currentTimeMillis();
      threadBytesRead.add(totalBytesRead);
      if (threadBytesRead.size() > 1) {
        synchronized (this) {
          if (!stopReading) {
            stopReading = true;
            stopReadingTime = System.currentTimeMillis();
          }
        }
      }
      System.out.printf("Thread %d read %d bytes in %dms\n", threadId, totalBytesRead, endTime - startTime);
      doneSignal.countDown(); // signal that this thread has finished
    }
  }
}
