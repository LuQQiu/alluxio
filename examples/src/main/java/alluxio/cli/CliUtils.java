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

import alluxio.Constants;

import alluxio.underfs.UfsFileStatus;
import alluxio.underfs.UfsStatus;
import alluxio.underfs.UnderFileSystem;
import alluxio.underfs.options.DeleteOptions;
import alluxio.util.io.PathUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Utilities to run the examples.
 */
public final class CliUtils {
  private static final Logger LOG = LoggerFactory.getLogger(CliUtils.class);

  private CliUtils() {} // prevent instantiation

  /**
   * Prints information of the test result.
   *
   * @param pass the test result
   */
  public static void printPassInfo(boolean pass) {
    if (pass) {
      System.out.println(Constants.ANSI_GREEN + "Passed the test!" + Constants.ANSI_RESET);
    } else {
      System.out.println(Constants.ANSI_RED + "Failed the test!" + Constants.ANSI_RESET);
    }
  }

  /**
   * Runs an example.
   *
   * @param example the example to run
   * @return whether the example completes
   */
  public static boolean runExample(final Callable<Boolean> example) {
    boolean result;
    try {
      result = example.call();
    } catch (Exception e) {
      LOG.error("Exception running test: " + example, e);
      result = false;
    }
    CliUtils.printPassInfo(result);
    return result;
  }

  /**
   * Cleans all the files or sub directories inside the given directory
   * in the under filesystem.
   *
   * @param ufs the under filesystem
   * @param directory the directory to clean
   */
  public static void cleanup(UnderFileSystem ufs, String directory) throws IOException {
    UfsStatus[] statuses = ufs.listStatus(directory);
    for (UfsStatus status : statuses) {
      if (status instanceof UfsFileStatus) {
        ufs.deleteFile(PathUtils.concatPath(directory, status.getName()));
      } else {
        ufs.deleteDirectory(PathUtils.concatPath(directory, status.getName()),
            DeleteOptions.defaults().setRecursive(true));
      }
    }
  }
}
