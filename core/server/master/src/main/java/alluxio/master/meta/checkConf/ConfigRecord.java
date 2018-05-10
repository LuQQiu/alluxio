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

package alluxio.master.meta.checkConf;

import alluxio.PropertyKey;

/**
 * The Alluxio configuration record.
 */
public final class ConfigRecord {
  private PropertyKey mKey;
  private String mSource;
  private String mValue;

  /**
   * Creates a new instance of {@link ConfigRecord}.
   */
  public ConfigRecord() {}
  
  /**
   * Creates a new instance of {@link ConfigRecord}
   */
  public ConfigRecord(PropertyKey key, String source, String value) {
    mKey = key;
    mSource = source;
    mValue = value;
  }

  /**
   * @return the configuration property
   */
  public PropertyKey getKey() {
    return mKey;
  }

  /**
   * @return the source of this configuration property
   */
  public String getSource() {
    return mSource;
  }

  /**
   * @return the value of this configuration property
   */
  public String getValue() {
    return mValue;
  }

  /**
   * @param key the configuration property key
   * @return the configuration record
   */
  public ConfigRecord setKey(PropertyKey key) {
    mKey = key;
    return this;
  }

  /**
   * @param source the configuration source to use
   * @return the configuration property
   */
  public ConfigRecord setSource(String source) {
    mSource = source;
    return this;
  }

  /**
   * @param value the configuration value to use
   * @return the configuration property
   */
  public ConfigRecord setValue(String value) {
    mValue = value;
    return this;
  }
}

