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

package alluxio.worker.block.meta;

import com.google.common.base.Preconditions;

/**
 * This class is a wrapper of {@link StorageDir} to provide more limited access for allocators.
 */
public class StorageDirAllocatorView extends StorageDirView {
  /** The {@link StorageTierAllocatorView} this view under. */
  private final StorageTierAllocatorView mTierView;
  /**
   * Creates a {@link StorageDirAllocatorView} using the actual {@link StorageDir}.
   *
   * @param dir which the dirView is constructed from
   * @param tierView which the dirView is under
   */
  public StorageDirAllocatorView(StorageDir dir, StorageTierAllocatorView tierView) {
    super(dir);
    mTierView = Preconditions.checkNotNull(tierView, "tierView");
  }

  @Override
  public StorageTierAllocatorView getParentTierView() {
    return mTierView;
  }

  @Override
  public long getAvailableBytes() {
    return mDir.getAvailableBytes();
  }
}
