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

package alluxio.client.file.dora;

import alluxio.client.block.stream.LocalCachedNettyDataReader;
import alluxio.client.file.FileInStream;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Implementation of {@link FileInStream} that reads from a dora cache if possible.
 */
public class PositionReadDoraFileInStream extends FileInStream {

  private final LocalCachedNettyDataReader mReader;
  private boolean mClosed;

  /**
   * Constructor.
   * @param reader
   */
  public PositionReadDoraFileInStream(LocalCachedNettyDataReader reader) {
    mReader = reader;
  }

  @Override
  public long remaining() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public int read(ByteBuffer byteBuffer, int off, int len) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public int positionedRead(long position, byte[] buffer, int offset, int length)
      throws IOException {
    return mReader.positionedRead(position, buffer, offset, length);
  }

  @Override
  public long getPos() throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void seek(long pos) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public long skip(long n) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void close() throws IOException {
    if (mClosed) {
      return;
    }
    mReader.close();
    mClosed = true;
  }
}
