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

package alluxio.underfs.s3a;

import alluxio.Configuration;
import alluxio.Constants;
import alluxio.PropertyKey;
import alluxio.util.CommonUtils;
import alluxio.util.io.PathUtils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.internal.Mimetypes;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.util.Base64;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * A stream for writing a file into S3. The data will be persisted to a temporary directory on the
 * local disk and copied as a complete file when the {@link #close()} method is called. The data
 * transfer is done using a {@link TransferManager} which manages the upload threads and handles
 * multipart upload.
 */
@NotThreadSafe
public class S3AOutputStream extends OutputStream {
  private static final Logger LOG = LoggerFactory.getLogger(S3AOutputStream.class);
  private static final long PARTITION_SIZE = 50 * Constants.MB;
  private static final long UPLOAD_THRESHOLD = 5 * Constants.MB;
  private static final boolean SSE_ENABLED =
      Configuration.getBoolean(PropertyKey.UNDERFS_S3A_SERVER_SIDE_ENCRYPTION_ENABLED);

  /** Bucket name of the Alluxio S3 bucket. */
  private final String mBucketName;

  /** Key of the file when it is uploaded to S3. */
  private final String mKey;

  private final AmazonS3 mClient;

  private final ExecutorService mExecutor;

  /** The upload id of this multipart upload. */
  private final String mUploadId;

  /** Flag to indicate the this stream has been closed, to ensure close is only done once. */
  private boolean mClosed = false;

  /** The MD5 hash of the file. */
  private MessageDigest mHash;

  /** The local file that will be uploaded when the stream is closed. */
  private File mFile;

  /** The current File is closed or not. */
  private boolean mFileClosed = true;

  /** The output stream to a local file where the file will be buffered until closed. */
  private OutputStream mLocalOutputStream;

  private Map<Future<PartETag>, File> mFutureTagsAndFile = new HashMap<>();

  private List<PartETag> mTags = new ArrayList<>();

  private AtomicInteger mPartNumber;

  private long mOffset;

  /**
   * Constructs a new stream for writing a file.
   *
   * @param bucketName the name of the bucket
   * @param key the key of the file
   * @param s3Client the Amazon S3 client to upload the file with
   * @param executor the executor to submit upload jobs to
   */
  public S3AOutputStream(String bucketName, String key, AmazonS3 s3Client,
      ExecutorService executor) throws IOException {
    Preconditions.checkArgument(bucketName != null && !bucketName.isEmpty(), "Bucket name must "
        + "not be null or empty.");
    mBucketName = bucketName;
    mKey = key;
    try {
      mHash = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      LOG.warn("Algorithm not available for MD5 hash.", e);
      mHash = null;
    }
    initNewFile();

    mClient = s3Client;
    mExecutor = executor;

    LOG.info("BucketName {}, key {}", mBucketName, mKey);

    // Generate the object metadata by setting server side encryption, md5 checksum, the file
    // length, and encoding as octet stream since no assumptions are made about the file type
    ObjectMetadata meta = new ObjectMetadata();
    if (SSE_ENABLED) {
      meta.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
    }
    if (mHash != null) {
      meta.setContentMD5(new String(Base64.encode(mHash.digest())));
    }
    meta.setContentType(Mimetypes.MIMETYPE_OCTET_STREAM);

    InitiateMultipartUploadRequest initRequest =
        new InitiateMultipartUploadRequest(mBucketName, mKey).withObjectMetadata(meta);
    mUploadId = s3Client.initiateMultipartUpload(initRequest).getUploadId();
    mPartNumber = new AtomicInteger(1);
    LOG.info("Start multipart upload bucket name {}, key {}, meta {}", mBucketName, mKey, meta);
    LOG.info("Upload id is " + mUploadId);
  }

  @Override
  public void write(int b) throws IOException {
    if (mFileClosed) {
      initNewFile();
    }
    mLocalOutputStream.write(b);
    mOffset++;
    if (mOffset >= PARTITION_SIZE) {
      uploadPart();
    }
  }

  @Override
  public void write(byte[] b) throws IOException {
    write(b, 0, b.length);
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    if (mFileClosed) {
      initNewFile();
    }
    mLocalOutputStream.write(b, off, len);
    mOffset += len;
    if (mOffset >= PARTITION_SIZE) {
      uploadPart();
    }
  }

  @Override
  public void flush() throws IOException {
    LOG.info("flush now .....");
    // We try to minimize the time use to close()
    // because Fuse release() method which calls close() is async.
    // In flush(), we upload the current writing file if it is bigger than 5 MB,
    // and wait for all current upload to complete.
    if (mOffset > UPLOAD_THRESHOLD) {
      uploadPart();
    }
    waitForUploadResults();
    LOG.info("flush() : get tags {}", mTags.size());
    mFutureTagsAndFile = new HashMap<>();
  }

  @Override
  public void close() throws IOException {
    if (mClosed) {
      return;
    }
    try {
      long start = System.currentTimeMillis();
      LOG.info("start close() function");

      if (!mFileClosed) {
        mLocalOutputStream.close();
        int partNumber = mPartNumber.getAndIncrement();
        final UploadPartRequest uploadRequest = new UploadPartRequest()
            .withBucketName(mBucketName)
            .withKey(mKey)
            .withUploadId(mUploadId)
            .withPartNumber(partNumber)
            .withFile(mFile)
            .withPartSize(mFile.length());
        uploadRequest.setLastPart(true);
        Future<PartETag> futureETag = mExecutor.submit(()
            -> mClient.uploadPart(uploadRequest).getPartETag());
        mFutureTagsAndFile.put(futureETag, mFile);
        LOG.info("successfully upload last part {}, takes {}",
            partNumber, (System.currentTimeMillis() - start));
      }

      waitForUploadResults();
      long beginComplete = System.currentTimeMillis();
      CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(mBucketName,
          mKey, mUploadId, mTags);
      mClient.completeMultipartUpload(compRequest);

      LOG.info("Completed upload with {} tags, we have {} parts, the whole close takes {}, the complete takes {}",
          mTags.size(), mPartNumber.get() - 1, (System.currentTimeMillis() - start), (System.currentTimeMillis() - beginComplete));
    } catch (Exception e) {
      LOG.error("Failed to upload {}: {}", mKey, e.toString());
      throw new IOException(e);
    }

    // Set the closed flag, close can be retried until mFile.delete is called successfully
    mClosed = true;
  }

  /**
   * @return the path in S3 to upload the file to
   */
  protected String getUploadPath() {
    return mKey;
  }

  /**
   * Stops writing to the mFile and uploads it.
   */
  private void uploadPart() throws IOException {
    if (mFileClosed) {
      return;
    }
    mLocalOutputStream.close();
    int partNumber = mPartNumber.getAndIncrement();
    final UploadPartRequest uploadRequest = new UploadPartRequest()
        .withBucketName(mBucketName)
        .withKey(mKey)
        .withUploadId(mUploadId)
        .withPartNumber(partNumber)
        .withFile(mFile)
        .withPartSize(mFile.length());
    Future<PartETag> futureETag = mExecutor.submit(()
        -> mClient.uploadPart(uploadRequest).getPartETag());
    mFutureTagsAndFile.put(futureETag, new File(mFile.getPath()));
    LOG.info("submit upload part {} with File {} and size {}",
        partNumber, mFile.toString(), mFile.length());
    mFileClosed = true;
    mLocalOutputStream = null;
    mFile = null;
  }

  /**
   * Creates new file and the future write will write in this file.
   */
  private void initNewFile() throws IOException {
    mFile = new File(PathUtils.concatPath(CommonUtils.getTmpDir(), UUID.randomUUID()));
    if (mHash != null) {
      mLocalOutputStream =
          new BufferedOutputStream(new DigestOutputStream(new FileOutputStream(mFile), mHash));
    } else {
      mLocalOutputStream = new BufferedOutputStream(new FileOutputStream(mFile));
    }
    mOffset = 0;
    mFileClosed = false;
    LOG.info("init new temp file {}", mFile);
  }

  /**
   * Waits for the submitted upload to finish and delete all the tmp files.
   */
  private void waitForUploadResults() throws IOException {
    long start = System.currentTimeMillis();
    try {
      for (Map.Entry<Future<PartETag>, File> entry : mFutureTagsAndFile.entrySet()) {
        mTags.add(entry.getKey().get());
        if (!entry.getValue().delete()) {
          LOG.error("Failed to delete temporary file @ {}", mFile.getPath());
        }
      }
    } catch (Exception e) {
      LOG.error("Failed to upload {}: {}", getUploadPath(), e.toString());
      throw new IOException(e);
    }
    LOG.info("wait for upload results takes {}", (System.currentTimeMillis() - start));
  }
}
