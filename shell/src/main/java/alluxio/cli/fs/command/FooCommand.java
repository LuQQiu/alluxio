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

package alluxio.cli.fs.command;

import alluxio.AlluxioURI;
import alluxio.Constants;
import alluxio.cli.CommandUtils;
import alluxio.client.file.FileInStream;
import alluxio.client.file.FileSystemContext;
import alluxio.client.file.URIStatus;
import alluxio.exception.AlluxioException;
import alluxio.exception.status.InvalidArgumentException;

import alluxio.grpc.OpenFilePOptions;
import com.google.common.io.Closer;
import org.apache.commons.cli.CommandLine;

import javax.annotation.concurrent.ThreadSafe;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Displays the number of folders and files matching the specified prefix in args.
 */
@ThreadSafe
public final class FooCommand extends AbstractFileSystemCommand {

  /**
   * @param fsContext the filesystem of Alluxio
   */
  public FooCommand(FileSystemContext fsContext) {
    super(fsContext);
  }

  @Override
  public String getCommandName() {
    return "foo";
  }

  @Override
  public void validateArgs(CommandLine cl) throws InvalidArgumentException {
    CommandUtils.checkNumOfArgsEquals(this, cl, 2);
  }

  @Override
  public int run(CommandLine cl) throws AlluxioException, IOException {
    String[] args = cl.getArgs();
    AlluxioURI inputPath = new AlluxioURI(args[0]);
    int code = Integer.parseInt(args[1]);

    if (code == 1) {
      for (int i = 0; i < 20; i++) {
        readWhole(inputPath);
      }
    } else {
      for (int i = 0; i < 30; i++) {
        readPart(inputPath);
      }
    }
    return 0;
  }

  private void readWhole(AlluxioURI inputPath) throws AlluxioException, IOException {
    try (Closer closer = Closer.create()) {
      OpenFilePOptions options = OpenFilePOptions.getDefaultInstance();
      URIStatus uriStatus = mFileSystem.getStatus(inputPath);
      FileInStream is = closer.register(mFileSystem.openFile(inputPath, options));
      byte[] buf = new byte[(int) uriStatus.getLength()];
      long start = System.currentTimeMillis();
      int t = is.read(buf);
      while (t != -1) {
        t = is.read(buf);
      }
      System.out.println("readWhole takes " + (System.currentTimeMillis() - start));
    }
  }

  private void readPart(AlluxioURI inputPath) throws AlluxioException, IOException {
    try (Closer closer = Closer.create()) {
      OpenFilePOptions options = OpenFilePOptions.getDefaultInstance();
      URIStatus uriStatus = mFileSystem.getStatus(inputPath);
      FileInStream is = closer.register(mFileSystem.openFile(inputPath, options));
      int totalSize = (int) uriStatus.getLength();
      int size = 128 * Constants.KB;
      int number = (int) Math.ceil(totalSize / size);
      for (int i = 0; i < number; i++) {
        int offset = i * size;
        long start = System.currentTimeMillis();
        byte[] dest = new byte[size];
        int rd = 0;
        int nread = 0;
        is.seek(offset);
        while (rd >= 0 && nread < size) {
          rd = is.read(dest, nread, size - nread);
          if (rd >= 0) {
            nread += rd;
          }
        }
        System.out.printf("read {} part takes {}", i, (System.currentTimeMillis() - start));
      }
    }
  }

  @Override
  public String getUsage() {
    return "foo <path>";
  }

  @Override
  public String getDescription() {
    return "Foo command.";
  }
}
