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

package alluxio.underfs.options;

import alluxio.annotation.PublicApi;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * Method options for deleting a directory in UnderFileSystem.
 */
@PublicApi
@NotThreadSafe
public final class DeleteDirectoryOptions {
  // Whether to delete a directory with children
  private boolean mRecursive;
  // Ensure consistency. When true, eventual consistency issues
  // in workloads like delete-then-create will be taken care of
  private boolean mEnsureConsistency;

  /**
   * @return the default {@link DeleteDirectoryOptions}
   */
  public static DeleteDirectoryOptions defaults() {
    return new DeleteDirectoryOptions();
  }

  /**
   * Constructs a default {@link DeleteDirectoryOptions}.
   */
  private DeleteDirectoryOptions() {
    mRecursive = false;
  }

  /**
   * @return whether to delete a non-empty directory
   */
  public boolean isRecursive() {
    return mRecursive;
  }

  /**
   * Sets recursive delete.
   *
   * @param recursive whether to delete recursively
   * @return the updated option object
   */
  public DeleteDirectoryOptions setRecursive(boolean recursive) {
    mRecursive = recursive;
    return this;
  }

  /**
   * Sets consistency guarantees. When true, eventual consistency issues
   * in workloads like create-them-delete will be taken care of.
   *
   * @param ensureConsistency whether to ensure the data consistency
   * @return the updated object
   */
  public DeleteDirectoryOptions setEnsureConsistency(boolean ensureConsistency) {
    mEnsureConsistency = ensureConsistency;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DeleteDirectoryOptions)) {
      return false;
    }
    DeleteDirectoryOptions that = (DeleteDirectoryOptions) o;
    return Objects.equal(mRecursive, that.mRecursive)
        && (mEnsureConsistency == that.mEnsureConsistency);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(mRecursive);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("recursive", mRecursive)
        .add("ensureConsistency", mEnsureConsistency)
        .toString();
  }
}
