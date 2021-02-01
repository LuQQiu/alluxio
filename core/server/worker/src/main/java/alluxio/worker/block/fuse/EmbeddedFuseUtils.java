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

import alluxio.fuse.FuseMountInfo;
import alluxio.grpc.MountEmbeddedFuseRequest;

public class EmbeddedFuseUtils {
  // TODO(lu) deal with the not set case
  public static FuseMountInfo fromProto(alluxio.grpc.FuseMountInfo info) {
    return new FuseMountInfo(info.getMountPoint(), info.getAlluxioPath(), info.getDebug(), info.getFuseOptsList());
  }

  public static alluxio.grpc.FuseMountInfo toProto(FuseMountInfo info) {
    return alluxio.grpc.FuseMountInfo.newBuilder().setMountPoint(info.getMountPoint())
        .setAlluxioPath(info.getAlluxioPath()).setDebug(info.isDebug())
        .addAllFuseOpts(info.getFuseOpts()).build();
  }
}
