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
import alluxio.conf.AlluxioConfiguration;
import alluxio.jnifuse.AbstractFuseFileSystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A default Fuse authentication policy that does nothing.
 * Alluxio Fuse does the authentication based on the user and group that launches the
 * process whether Fuse applications runs inside.
 */
public class DefaultAuthPolicy implements AuthPolicy {
  private static final Logger LOG =
      LoggerFactory.getLogger(CustomAuthPolicy.class);

  /**
   * @param fileSystem     the Alluxio file system
   * @param conf           alluxio configuration
   * @param fuseFileSystem the FuseFileSystem
   */
  public DefaultAuthPolicy(FileSystem fileSystem, AlluxioConfiguration conf,
      AbstractFuseFileSystem fuseFileSystem) {
    // log the UID and GID
  }

  @Override
  public void setUserGroupIfNeeded(AlluxioURI uri) {}
}
