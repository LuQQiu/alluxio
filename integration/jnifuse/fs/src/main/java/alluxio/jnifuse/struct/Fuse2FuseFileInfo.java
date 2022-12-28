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

package alluxio.jnifuse.struct;

import jnr.ffi.NativeType;
import jnr.ffi.Runtime;

import java.nio.ByteBuffer;

/**
 * Maps to struct fuse_file_info in /usr/include/fuse/fuse_common.h
 * Supports Libfuse version bigger or equal to 2.6 but smaller than 3.0.
 */
public class Fuse2FuseFileInfo extends FuseFileInfo {

  // unused fields are omitted

  /**
   * Creates a FuseFileInfo class matching the struct fuse_file_info in libfuse2.
   *
   * This struct is not meant to be used directly.
   * You should use {@link alluxio.jnifuse.struct.FuseFileInfo#of(ByteBuffer)}
   * to create a FuseFileIfo that matches currently used libfuse.
   *
   * @param runtime the JNR runtime
   * @param buffer the ByteBuffer containing struct fuse_file_info from JNR
   */
  public Fuse2FuseFileInfo(Runtime runtime, ByteBuffer buffer) {
    super(runtime, buffer);
    // total 40 bytes
    this.flags = new Signed32(); // 0
    this.fh_old = new UnsignedLong(); // fh_old // 8
    this.writepage = new Signed32(); // write_page // 16
    this.direct_io = new Unsigned32(); // 20
    this.fh = new u_int64_t(); // 24
    this.lock_owner = new u_int64_t(); // 32
  }
}
