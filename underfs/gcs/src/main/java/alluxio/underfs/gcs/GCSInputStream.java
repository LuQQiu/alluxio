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

package alluxio.underfs.gcs;

import alluxio.retry.RetryPolicy;

import com.google.cloud.ReadChannel;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * A stream for reading a file from GCS. The main purpose is to provide a faster skip method, as
 * the underlying implementation will read and discard bytes until the number to skip has been
 * reached. This input stream returns 0 when calling read with an empty buffer.
 */
@NotThreadSafe
public final class GCSInputStream extends InputStream {
  private static final Logger LOG = LoggerFactory.getLogger(GCSInputStream.class);

  /** Bucket name of the Alluxio GCS bucket. */
  private final String mBucketName;

  /** Key of the file in GCS to read. */
  private final String mKey;

  /** The Google cloud storage client. */
  private final Storage mClient;

  /** The underlying input stream. */
  private ReadChannel mReadChannel;

  /** Position of the stream. */
  private long mPos;

  private BlobId mBlobId;
  /**
   * Policy determining the retry behavior in case the key does not exist. The key may not exist
   * because of eventual consistency.
   */
  private final RetryPolicy mRetryPolicy;

  /**
   * Creates a new instance of {@link GCSInputStream}.
   *
   * @param bucketName the name of the bucket
   * @param key the key of the file
   * @param client the client for GCS
   * @param retryPolicy retry policy in case the key does not exist
   */
  GCSInputStream(String bucketName, String key, Storage client,
      RetryPolicy retryPolicy) {
    this(bucketName, key, client, 0L, retryPolicy);
  }

  /**
   * Creates a new instance of {@link GCSInputStream}, at a specific position.
   *
   * @param bucketName the name of the bucket
   * @param key the key of the file
   * @param client the client for GCS
   * @param pos the position to start
   * @param retryPolicy retry policy in case the key does not exist
   */
  GCSInputStream(String bucketName, String key, Storage client,
      long pos, RetryPolicy retryPolicy) {
    mBucketName = bucketName;
    mKey = key;
    mClient = client;
    mPos = pos;
    mRetryPolicy = retryPolicy;
    mBlobId = BlobId.of(mBucketName, mKey);
  }

  @Override
  public void close() throws IOException {
    mReadChannel.close();
  }

  @Override
  public int read() throws IOException {
    if (mReadChannel == null) {
      openStream();
    }
    ByteBuffer bytes = ByteBuffer.allocate(1);
    int num = mReadChannel.read(bytes);
    if (num != -1) { // valid data read
      mPos++;
    }
    bytes.position(0);
    return bytes.get() & 0xff;
  }


  @Override
  public int read(byte[] b) throws IOException {
    return read(b, 0, b.length);
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    if (len == 0) {
      return 0;
    }
    if (mReadChannel == null) {
      openStream();
    }
    ByteBuffer bytes = ByteBuffer.allocate(b.length);
    int ret = mReadChannel.read(bytes);
    if (ret != -1) {
      mPos += ret;
    }
    bytes.position(0);
    bytes.get(b, off, len);
    return ret;
  }

  /**
   * This method leverages the ability to open a stream from GCS from a given offset. When the
   * underlying stream has fewer bytes buffered than the skip request, the stream is closed, and
   * a new stream is opened starting at the requested offset.
   *
   * @param n number of bytes to skip
   * @return the number of bytes skipped
   */
  @Override
  public long skip(long n) throws IOException {
    if (n <= 0) {
      return 0;
    }
    mReadChannel.seek(mPos + n);
    mPos += n;
    return n;
  }

  /**
   * Opens a new stream at mPos if the wrapped stream mInputStream is null.
   */
  private void openStream() throws IOException {
    mReadChannel = mClient.reader(mBlobId);
    if (mPos > 0) {
      mReadChannel.seek(mPos);
    }
  }
}
