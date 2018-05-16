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

package alluxio.wire;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import java.io.Serializable;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * The address of a node.
 */
@NotThreadSafe
public final class Address implements Serializable {
  private static final long serialVersionUID = 1645922245406539186L;

  private String mHost = "";
  private int mRpcPort;

  /**
   * Creates a new instance of {@link Address}.
   */
  public Address() {}

  /**
   * Creates a new instance of {@link Address}.
   *
   * @param hostname the hostname to set
   * @param rpcPort the rpc port to set
   */
  public Address(String hostname, int rpcPort) {
    mHost = hostname;
    mRpcPort = rpcPort;
  }

  /**
   * Creates a new instance of {@link Address} from thrift representation.
   *
   * @param address the thrift address
   */
  protected Address(alluxio.thrift.Address address) {
    mHost = address.getHost();
    mRpcPort = address.getRpcPort();
  }

  /**
   * @return the host of this node
   */
  public String getHost() {
    return mHost;
  }

  /**
   * @return the RPC port of this node
   */
  public int getRpcPort() {
    return mRpcPort;
  }

  /**
   * @param host the host to use
   * @return the address
   */
  public Address setHost(String host) {
    mHost = Preconditions.checkNotNull(host, "host");
    return this;
  }

  /**
   * @param rpcPort the rpc port to use
   * @return the address
   */
  public Address setRpcPort(int rpcPort) {
    mRpcPort = rpcPort;
    return this;
  }

  /**
   * @return a address of thrift construct
   */
  protected alluxio.thrift.Address toThrift() {
    return new alluxio.thrift.Address()
        .setHost(mHost).setRpcPort(mRpcPort);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Address)) {
      return false;
    }
    Address that = (Address) o;
    return mHost.equals(that.mHost)
        && mRpcPort == that.mRpcPort;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(mHost, mRpcPort);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("host", mHost)
        .add("rpcPort", mRpcPort)
        .toString();
  }
}
