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

package alluxio.worker.block.evictor;

import alluxio.worker.block.BlockMetadataEvictorView;
import alluxio.worker.block.BlockStoreLocation;
import alluxio.worker.block.meta.StorageDirEvictorView;
import alluxio.worker.block.meta.StorageDirView;
import alluxio.worker.block.meta.StorageTierEvictorView;

import javax.annotation.concurrent.ThreadSafe;
import javax.annotation.Nullable;

/**
 * Utility functions for the evictor package.
 */
@ThreadSafe
// TODO(calvin): This could be moved into AbstractEvictor.
public final class EvictorUtils {

  /**
   * Gets {@link StorageDirView} with max free space.
   *
   * @param bytesToBeAvailable space size to be requested
   * @param location location that the space will be allocated in
   * @param metadataView a view of block metadata information
   * @return the {@link StorageDirView} selected
   */
  public static StorageDirEvictorView getDirWithMaxFreeSpace(long bytesToBeAvailable,
      BlockStoreLocation location, BlockMetadataEvictorView metadataView) {
    long maxFreeSize = -1;
    StorageDirEvictorView selectedDirView = null;

    if (location.equals(BlockStoreLocation.anyTier())) {
      for (StorageTierEvictorView tierView : metadataView.getTierViews()) {
        for (StorageDirEvictorView dirView : tierView.getDirViews()) {
          if (dirView.getCommittedBytes() + dirView.getAvailableBytes() >= bytesToBeAvailable
              && dirView.getAvailableBytes() > maxFreeSize) {
            selectedDirView = dirView;
            maxFreeSize = dirView.getAvailableBytes();
          }
        }
      }
    } else {
      String tierAlias = location.tierAlias();
      StorageTierEvictorView tierView = metadataView.getTierView(tierAlias);
      if (location.equals(BlockStoreLocation.anyDirInTier(tierAlias))) {
        for (StorageDirEvictorView dirView : tierView.getDirViews()) {
          if (dirView.getCommittedBytes() + dirView.getAvailableBytes() >= bytesToBeAvailable
              && dirView.getAvailableBytes() > maxFreeSize) {
            selectedDirView = dirView;
            maxFreeSize = dirView.getAvailableBytes();
          }
        }
      } else {
        int dirIndex = location.dir();
        StorageDirEvictorView dirView = tierView.getDirView(dirIndex);
        if (dirView.getCommittedBytes() + dirView.getAvailableBytes() >= bytesToBeAvailable
            && dirView.getAvailableBytes() > maxFreeSize) {
          selectedDirView = dirView;
        }
      }
    }
    return selectedDirView;
  }

  /**
   * Finds a directory in the given location range with capacity upwards of the given bound.
   *
   * @param bytesToBeAvailable the capacity bound
   * @param location the location range
   * @param mManagerView the storage manager view
   * @return a {@link StorageDirView} in the range of location that already
   *         has availableBytes larger than bytesToBeAvailable, otherwise null
   */
  @Nullable
  public static StorageDirEvictorView selectDirWithRequestedSpace(long bytesToBeAvailable,
      BlockStoreLocation location, BlockMetadataEvictorView mManagerView) {
    if (location.equals(BlockStoreLocation.anyTier())) {
      for (StorageTierEvictorView tierView : mManagerView.getTierViews()) {
        for (StorageDirEvictorView dirView : tierView.getDirViews()) {
          if (dirView.getAvailableBytes() >= bytesToBeAvailable) {
            return dirView;
          }
        }
      }
      return null;
    }

    String tierAlias = location.tierAlias();
    StorageTierEvictorView tierView = mManagerView.getTierView(tierAlias);
    if (location.equals(BlockStoreLocation.anyDirInTier(tierAlias))) {
      for (StorageDirEvictorView dirView : tierView.getDirViews()) {
        if (dirView.getAvailableBytes() >= bytesToBeAvailable) {
          return dirView;
        }
      }
      return null;
    }

    StorageDirEvictorView dirView = tierView.getDirView(location.dir());
    return (dirView.getAvailableBytes() >= bytesToBeAvailable) ? dirView : null;
  }

  private EvictorUtils() {} // prevent instantiation
}
