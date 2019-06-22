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

import alluxio.AlluxioURI;
import alluxio.Constants;
import alluxio.conf.PropertyKey;
import alluxio.retry.RetryPolicy;
import alluxio.underfs.ObjectUnderFileSystem;
import alluxio.underfs.UfsDirectoryStatus;
import alluxio.underfs.UnderFileSystem;
import alluxio.underfs.UnderFileSystemConfiguration;
import alluxio.underfs.options.OpenOptions;
import alluxio.util.UnderFileSystemUtils;
import alluxio.util.io.PathUtils;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.jets3t.service.ServiceException;
import org.jets3t.service.StorageObjectsChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.concurrent.ThreadSafe;

/**
 * GCS FS {@link UnderFileSystem} implementation based on the jets3t library.
 */
@ThreadSafe
public class GCSUnderFileSystem extends ObjectUnderFileSystem {
  private static final Logger LOG = LoggerFactory.getLogger(GCSUnderFileSystem.class);

  /** Google cloud storage client. */
  private final Storage mStorageClient;

  /** Suffix for an empty file to flag it as a directory. */
  private static final String FOLDER_SUFFIX = "/";

  /** Google cloud storage bucket client. */
  private final Bucket mBucketClient;

  /** Bucket name of user's configured Alluxio bucket. */
  private final String mBucketName;

  /**
   * Constructs a new instance of {@link GCSUnderFileSystem}.
   *
   * @param uri the {@link AlluxioURI} for this UFS
   * @param conf the configuration for this UFS
   * @return the created {@link GCSUnderFileSystem} instance
   * @throws ServiceException when a connection to GCS could not be created
   */
  public static GCSUnderFileSystem createInstance(AlluxioURI uri, UnderFileSystemConfiguration conf)
      throws IOException {
    String bucketName = UnderFileSystemUtils.getBucketName(uri);
    Preconditions.checkArgument(conf.isSet(PropertyKey.GOOGLE_APPLICATION_CREDENTIALS),
            "Property " + PropertyKey.GOOGLE_APPLICATION_CREDENTIALS + " is required to connect to GCS");

    GoogleCredentials credentials = GoogleCredentials
        .fromStream(new FileInputStream(conf.get(PropertyKey.GOOGLE_APPLICATION_CREDENTIALS)))
        .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));

    Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    Bucket bucket = storage.get(bucketName);
    return new GCSUnderFileSystem(uri, storage, bucket, bucketName,
        conf);
  }

  /**
   * Constructor for {@link GCSUnderFileSystem}.
   *
   * @param uri the {@link AlluxioURI} for this UFS
   * @param storage the Google cloud storage client
   * @param bucketName bucket name of user's configured Alluxio bucket
   * @param conf configuration for this UFS
   */
  protected GCSUnderFileSystem(AlluxioURI uri, Storage storage,Bucket bucket,
      String bucketName, UnderFileSystemConfiguration conf) {
    super(uri, conf);
    mStorageClient = storage;
    mBucketClient = bucket;
    mBucketName = bucketName;
  }

  @Override
  public String getUnderFSType() {
    return "gcs";
  }

  // Setting GCS owner via Alluxio is not supported yet. This is a no-op.
  @Override
  public void setOwner(String path, String user, String group) {}

  // Setting GCS mode via Alluxio is not supported yet. This is a no-op.
  @Override
  public void setMode(String path, short mode) throws IOException {}

  @Override
  public UfsDirectoryStatus getExistingDirectoryStatus(String path) throws IOException {
    return getDirectoryStatus(path);
  }

  // GCS provides strong global consistency for read-after-write and read-after-metadata-update
  @Override
  public InputStream openExistingFile(String path) throws IOException {
    return open(path);
  }

  // GCS provides strong global consistency for read-after-write and read-after-metadata-update
  @Override
  public InputStream openExistingFile(String path, OpenOptions options) throws IOException {
    return open(path, options);
  }

  @Override
  protected boolean copyObject(String src, String dst) {
    LOG.debug("Copying {} to {}", src, dst);
    // TODO(lu) how GOOGLE CLOUD API throws exceptions???
    // Retry copy for a few times, in case some Jets3t or GCS internal errors happened during copy.
    Storage.CopyRequest request = Storage.CopyRequest.newBuilder()
        .setSource(BlobId.of(mBucketName, src))
        .setTarget(BlobId.of(mBucketName, dst))
        .build();
    mStorageClient.copy(request).getResult();
    return true;
  }

  @Override
  public boolean createEmptyObject(String key) {
    BlobId blobId = BlobId.of(mBucketName, key);
    // TODO(lu) see what other content need to be included
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
    mStorageClient.create(blobInfo);
    return true;
  }

  @Override
  protected OutputStream createObject(String key) throws IOException {
    return new GCSOutputStream(mBucketName, key, mStorageClient,
        mUfsConf.getList(PropertyKey.TMP_DIRS, ","));
  }

  @Override
  protected boolean deleteObject(String key) {
    BlobId blobId = BlobId.of(mBucketName, key);
    mStorageClient.delete(blobId);
    return true;
  }

  @Override
  protected String getFolderSuffix() {
    return FOLDER_SUFFIX;
  }

  @Override
  protected ObjectListingChunk getObjectListingChunk(String key, boolean recursive)
      throws IOException {
    key = PathUtils.normalizePath(key, PATH_SEPARATOR);
    // In case key is root (empty string) do not normalize prefix
    key = key.equals(PATH_SEPARATOR) ? "" : key;
    Page<Blob> blobPages= getObjectListingChunk(key);
    if (blobPages != null && blobPages.getValues().iterator().hasNext()) {
      return new GCSObjectListingChunk(blobPages, Pattern.compile("^" + key + "([^/]+)/?$"), recursive);
    }
    return null;
  }

  // Get next chunk of listing result.
  private Page<Blob> getObjectListingChunk(String key) {
    return mBucketClient.list(Storage.BlobListOption.prefix(key));
  }

  /**
   * Wrapper over GCS {@link StorageObjectsChunk}.
   */
  private final class GCSObjectListingChunk implements ObjectListingChunk {
    final Page<Blob> mBlobs;
    final Pattern mPattern;
    final boolean mRecursive;

    GCSObjectListingChunk(Page<Blob> blobs, Pattern pattern, boolean recursive) {
      mBlobs = blobs;
      mPattern = pattern;
      mRecursive = recursive;
    }

    @Override
    public ObjectStatus[] getObjectStatuses() {
      Iterator<Blob> blobs = mBlobs.getValues().iterator();
      List<Blob> blobList = new ArrayList<>();
      while (blobs.hasNext()) {
        Blob blob = blobs.next();
        if (mRecursive || mPattern.matcher(blob.getName()).matches()) {
          blobList.add(blob);
        }
      }
      ObjectStatus[] res = new ObjectStatus[blobList.size()];
      for (int i = 0; i < blobList.size(); i++) {
        Blob blob = blobList.get(i);
        // TODO(Lu) investigate MD5
        res[i] = new ObjectStatus(blob.getName(), blob.getMd5(), blob.getSize(), blob.getUpdateTime());
      }
      return res;
    }

    @Override
    public String[] getCommonPrefixes() {
      return new String[0];
    }

    @Override
    public ObjectListingChunk getNextChunk() {
      if (mBlobs.hasNextPage()) {
        return new GCSObjectListingChunk(mBlobs.getNextPage(), mPattern, mRecursive);
      }
      return null;
    }
  }

  @Override
  protected ObjectStatus getObjectStatus(String key) throws IOException {
    BlobId info = BlobId.of(mBucketName, key);
    Blob blob = mStorageClient.get(info);
    if (blob == null) {
      return null;
    }
    return new ObjectStatus(key, blob.getMd5ToHexString(), blob.getSize(),
        blob.getUpdateTime());
  }

  @Override
  protected ObjectPermissions getPermissions() {
    return new ObjectPermissions("", "", Constants.DEFAULT_FILE_SYSTEM_MODE);
  }

  @Override
  protected String getRootKey() {
    return Constants.HEADER_GCS + mBucketName;
  }

  @Override
  protected InputStream openObject(String key, OpenOptions options, RetryPolicy retryPolicy) {
    return new GCSInputStream(mBucketName, key, mStorageClient, options.getOffset(), retryPolicy);
  }
}
