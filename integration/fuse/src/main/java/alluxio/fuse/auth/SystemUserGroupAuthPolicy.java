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

package alluxio.fuse.auth;

import alluxio.AlluxioURI;
import alluxio.client.file.FileSystem;
import alluxio.exception.AlluxioException;
import alluxio.fuse.AlluxioFuseFileSystemOpts;
import alluxio.fuse.AlluxioFuseUtils;
import alluxio.grpc.SetAttributePOptions;
import alluxio.jnifuse.AbstractFuseFileSystem;
import alluxio.jnifuse.struct.FuseContext;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Default Fuse Auth Policy.
 */
public final class SystemUserGroupAuthPolicy implements AuthPolicy {
  private static final Logger LOG = LoggerFactory.getLogger(
      SystemUserGroupAuthPolicy.class);

  private final LoadingCache<Long, String> mUsernameCache = CacheBuilder.newBuilder()
      .maximumSize(100)
      .build(new CacheLoader<Long, String>() {
        @Override
        public String load(Long uid) {
          return AlluxioFuseUtils.getUserName(uid);
        }
      });
  private final LoadingCache<Long, String> mGroupnameCache = CacheBuilder.newBuilder()
      .maximumSize(100)
      .build(new CacheLoader<Long, String>() {
        @Override
        public String load(Long gid) {
          return AlluxioFuseUtils.getGroupName(gid);
        }
      });
  private final FileSystem mFileSystem;
  private final AbstractFuseFileSystem mFuseFileSystem;

  /**
   * @param fileSystem     the Alluxio file system
   * @param fuseFsOpts     the options for AlluxioFuse filesystem
   * @param fuseFileSystem AbstractFuseFileSystem
   */
  public SystemUserGroupAuthPolicy(FileSystem fileSystem, AlluxioFuseFileSystemOpts fuseFsOpts,
      AbstractFuseFileSystem fuseFileSystem) {
    mFileSystem = fileSystem;
    mFuseFileSystem = fuseFileSystem;
  }

  @Override
  public void setUserGroupIfNeeded(AlluxioURI uri) {
    FuseContext fc = mFuseFileSystem.getContext();
    long uid = fc.uid.get();
    long gid = fc.gid.get();
    if (uid == AlluxioFuseUtils.ID_NOT_SET_VALUE
        || uid == AlluxioFuseUtils.ID_NOT_SET_VALUE_UNSIGNED
        || gid == AlluxioFuseUtils.ID_NOT_SET_VALUE
        || gid == AlluxioFuseUtils.ID_NOT_SET_VALUE_UNSIGNED) {
      // cannot get valid uid or gid
      return;
    }
    if (uid == AlluxioFuseUtils.DEFAULT_UID && gid == AlluxioFuseUtils.DEFAULT_GID) {
      // no need to set attribute
      return;
    }
    try {
      String userName = mUsernameCache.get(uid);
      String groupName = mGroupnameCache.get(gid);
      SetAttributePOptions attributeOptions = SetAttributePOptions.newBuilder()
          .setGroup(groupName)
          .setOwner(userName)
          .build();
      mFileSystem.setAttribute(uri, attributeOptions);
      LOG.debug("Set attributes of path {} to {}", uri, attributeOptions);
    } catch (IOException | ExecutionException | AlluxioException e) {
      throw new RuntimeException(e);
    }
  }
}
