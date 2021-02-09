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

package alluxio.client.block.stream;

import alluxio.block.BlockReadRequest;
import alluxio.conf.AlluxioConfiguration;
import alluxio.client.ReadType;
import alluxio.client.file.FileSystemContext;
import alluxio.client.file.options.InStreamOptions;
import alluxio.conf.PropertyKey;
import alluxio.grpc.OpenLocalBlockRequest;
import alluxio.grpc.OpenLocalBlockResponse;
import alluxio.grpc.ReadRequest;
import alluxio.metrics.MetricKey;
import alluxio.metrics.MetricsSystem;
import alluxio.network.protocol.databuffer.DataBuffer;
import alluxio.network.protocol.databuffer.NioDataBuffer;
import alluxio.resource.CloseableResource;
import alluxio.wire.WorkerNetAddress;
import alluxio.worker.block.LocalBlockWorker;
import alluxio.worker.block.io.BlockReader;
import alluxio.worker.block.io.LocalFileBlockReader;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Paths;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * A data reader that simply reads packets from a local file.
 */
@NotThreadSafe
public final class EmbeddedClientDataReader implements DataReader {
  /** The file reader to read a local block. */
  private final BlockReader mReader;
  private final long mEnd;
  private final long mChunkSize;
  private long mPos;
  private boolean mClosed;

  /**
   * Creates an instance of {@link LocalFileDataReader}.
   *
   * @param reader the file reader to the block path
   * @param offset the offset
   * @param len the length to read
   * @param chunkSize the chunk size
   */
  private EmbeddedClientDataReader(BlockReader reader, long offset, long len, long chunkSize) {
    mReader = reader;
    Preconditions.checkArgument(chunkSize > 0);
    mPos = offset;
    mEnd = Math.min(mReader.getLength(), offset + len);
    mChunkSize = chunkSize;
  }

  @Override
  public DataBuffer readChunk() throws IOException {
    if (mPos >= mEnd) {
      return null;
    }
    ByteBuffer buffer = mReader.read(mPos, Math.min(mChunkSize, mEnd - mPos));
    DataBuffer dataBuffer = new NioDataBuffer(buffer, buffer.remaining());
    mPos += dataBuffer.getLength();
    MetricsSystem.counter(MetricKey.CLIENT_BYTES_READ_LOCAL.getName()).inc(dataBuffer.getLength());
    MetricsSystem.meter(MetricKey.CLIENT_BYTES_READ_LOCAL_THROUGHPUT.getName())
        .mark(dataBuffer.getLength());
    return dataBuffer;
  }

  @Override
  public long pos() {
    return mPos;
  }

  @Override
  public void close() throws IOException {
    if (mClosed) {
      return;
    }
    mClosed = true;
  }

  /**
   * Factory class to create {@link LocalFileDataReader}s.
   */
  @NotThreadSafe
  public static class Factory implements DataReader.Factory {
    private final LocalBlockWorker mLocalBlockWorker;
    private final long mLocalReaderChunkSize;

    private BlockReader mReader;
    private boolean mClosed;
    private ReadRequest mReadRequestPartial;
    private BlockReadRequest mBlockReadRequest;

    /**
     * Creates an instance of {@link Factory}.
     *
     * @param context the file system context
     * @param blockId the block ID
     * @param localReaderChunkSize chunk size in bytes for local reads
     * @param options the instream options
     */
    public Factory(FileSystemContext context, long blockId,
        long localReaderChunkSize, InStreamOptions options)  {
      mLocalReaderChunkSize = localReaderChunkSize;
      mClosed = false;
      boolean isPromote = ReadType.fromProto(options.getOptions().getReadType()).isPromote();
      mReadRequestPartial = ReadRequest.newBuilder().setBlockId(blockId).setPromote(isPromote).build();
      mLocalBlockWorker = context.acquireLocalBlockWorkerClient();
    }

    @Override
    public DataReader create(long offset, long len) throws IOException {
      // TODO(lu) how to use BlockReader directly
      mReadRequestPartial = mReadRequestPartial.toBuilder().setOffset(offset).setLength(len).build();
      mBlockReadRequest = new BlockReadRequest(mReadRequestPartial);
      try {
        mReader = mLocalBlockWorker.getBlockReader(mBlockReadRequest);
        return new EmbeddedClientDataReader(mReader, offset, len, mLocalReaderChunkSize);
      } catch (Exception e) {
        throw new IOException(e);
      }
    }

    @Override
    public boolean isShortCircuit() {
      return false;
    }

    @Override
    public void close() throws IOException {
      if (mClosed) {
        return;
      }
      try {
        mLocalBlockWorker.closeBlockReader(mReader, mBlockReadRequest.getSessionId(), mBlockReadRequest.getId());
      } catch (Exception e) {
        throw new IOException(e);
      }
    }
  }
}

