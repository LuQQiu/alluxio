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
 * Method options for deleting a file in UnderFileSystem.
 */
@PublicApi
@NotThreadSafe
public final class DeleteFileOptions {
  // Ensure consistency. When true, eventual consistency issues
  // in workloads like delete-then-create will be taken care of
  private boolean mEnsureConsistency;

  /**
   * @return the default {@link DeleteFileOptions}
   */
  public static DeleteFileOptions defaults() {
    return new DeleteFileOptions();
  }

  /**
   * @return true, if consistency is guaranteed
   */
  public boolean isEnsureConsistency() {
    return mEnsureConsistency;
  }

  /**
   * Sets consistency guarantees. When true, eventual consistency issues
   * in workloads like create-them-delete will be taken care of.
   *
   * @param ensureConsistency whether to ensure the data consistency
   * @return the updated object
   */
  public DeleteFileOptions setEnsureConsistency(boolean ensureConsistency) {
    mEnsureConsistency = ensureConsistency;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DeleteFileOptions)) {
      return false;
    }
    DeleteFileOptions that = (DeleteFileOptions) o;
    return mEnsureConsistency == that.mEnsureConsistency;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(mEnsureConsistency);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("ensureConsistency", mEnsureConsistency)
        .toString();
  }
}
