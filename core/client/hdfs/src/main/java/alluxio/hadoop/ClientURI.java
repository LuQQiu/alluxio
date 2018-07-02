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

package alluxio.hadoop;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.net.URI;

/**
 * This class represents a client connection URI to connect to the Alluxio cluster.
 *
 * {@link ClientURI} supports more than just strict {@link URI}. Some examples:
 *   * Alluxio scheme with master hostname and port
 *     * alluxio://host1:port1/path
 *   * Alluxio on zookeeper high availability mode
 *     * alluxio://zk://host1:port1,host2:port2,../path
 *     * alluxio://zk:/host1:port1,host2:port2,.../path
 *
 * Does not support fragment in the URI.
 */
public final class ClientURI implements Serializable {
  private static final long serialVersionUID = 7091616565184279202L;

  public enum UriType {
    NORMAL, // alluxio://host1:port1/path
    ZOOKEEPER, // Uri begins with alluxio://zk:// or alluxio://zk:/
  }

  private UriType mUriType;

  private URI mUri;

  private String mZookeeperAddresses;

  private String mPath;

  /**
   * Constructs a {@link ClientURI} from a uri passed in by computation frameworks.
   * Elements in this uri may be wrong due to our flexible uri format.
   *
   * @param uri uri path to construct the {@link ClientURI} from
   */
  public ClientURI(URI uri) {
    if (uri.toString().contains("zk:")) {
      mUriType = UriType.ZOOKEEPER;
      processZookeeperUri(uri.toString());
    } else {
      mUriType = UriType.NORMAL;
      mUri = uri;
      mPath = mUri.getPath();
    }
  }

  /**
   * Gets the zookeeper addresses and path from input uri.
   * The input string is of format alluxio://zk://host1:port1,host2:port2,../path or
   * alluxio://zk:/host1:port1,host2:port2,.../path.
   *
   * @param uri
   */
  private void processZookeeperUri(String uri) {
    int trimIndex = uri.indexOf("zk:") + 3;
    String addressesAndPath = uri.substring(trimIndex).replaceAll("^/+", "");
    int sepIndex = addressesAndPath.indexOf('/');
    mZookeeperAddresses = addressesAndPath.substring(0, sepIndex);
    mPath = addressesAndPath.substring(sepIndex + 1);
  }

  public boolean isZookeeperEnabled() {
    return mUriType == UriType.ZOOKEEPER;
  }

  @Nullable
  public String getZookeeperAddresses() {
    return mZookeeperAddresses;
  }

  @Nullable
  public String getHost() {
    if (mUriType == UriType.NORMAL) {
      return mUri.getHost();
    }
    return null;
  }

  public int getPort() {
    if (mUriType == UriType.NORMAL) {
      return mUri.getPort();
    }
    return -1;
  }

  public String getPath() {
    return mPath;
  }

  public UriType getUriType() {
    return mUriType;
  }

  @Nullable
  public URI getUri() {
    return mUri;
  }
}
