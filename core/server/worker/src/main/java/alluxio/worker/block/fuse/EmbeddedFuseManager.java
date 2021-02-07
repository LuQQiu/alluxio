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

package alluxio.worker.block.fuse;

import alluxio.client.file.FileSystemContext;
import alluxio.conf.ServerConfiguration;
import alluxio.fuse.AlluxioFuse;
import alluxio.fuse.FuseMountInfo;
import alluxio.fuse.FuseUmountable;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class manages the embedded fuse mounts.
 */
public class EmbeddedFuseManager {
  // TODO(lu) support user providing configuration via API
  private final Supplier<FileSystemContext> mFsContextSupplier;

  private final Map<String, FuseUmountable> mFuseUmountableMap;
  private final Map<String, FuseMountInfo> mFuseMountTable;

  /**
   * Constructs a new {@link EmbeddedFuseManager}.
   */
  public EmbeddedFuseManager() {
    mFsContextSupplier = Suppliers.memoize(()
        -> FileSystemContext.create(ServerConfiguration.global()));
    mFuseUmountableMap = new HashMap<>();
    mFuseMountTable = new HashMap<>();
  }

  public synchronized void mount(FuseMountInfo info) throws IOException {
    if (mFuseUmountableMap.containsKey(info.getMountPoint())) {
      throw new IOException(String.format("the given mount point %s is mounted already", info.getMountPoint()));
    }
    FuseUmountable fuse = AlluxioFuse.launchFuse(mFsContextSupplier.get(), info, false);
    mFuseUmountableMap.put(info.getMountPoint(), fuse);
    mFuseMountTable.put(info.getMountPoint(), info);
  }

  public synchronized void unmount(String mountPoint) throws IOException {
    if (!mFuseUmountableMap.containsKey(mountPoint)) {
      throw new IOException(String.format("Cannot unmount %s which is not mounted", mountPoint));
    }
    mFuseUmountableMap.get(mountPoint).umount();
    mFuseMountTable.remove(mountPoint);
  }

  // TODO(lu) get mount table
  public synchronized Map<String, FuseMountInfo> getMountTable() {
    return Collections.unmodifiableMap(mFuseMountTable);
  }
}
