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

package alluxio.metrics;

import alluxio.grpc.MetricType;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A metric of a given instance. The instance can be master, worker, or client.
 */
public final class Metric implements Serializable {
  private static final Logger LOG = LoggerFactory.getLogger(Metric.class);
  private static final long serialVersionUID = -2236393414222298333L;

  private static final String ID_SEPARATOR = "-id:";
  public static final String TAG_SEPARATOR = ":";
  private static final ConcurrentHashMap<UserMetricKey, String> CACHED_METRICS =
      new ConcurrentHashMap<>();

  private final MetricIdentifier mMetricIdentifier;
  private final MetricType mMetricType;
  private String mInstanceId;

  private Double mValue;

  /**
   * Constructs a {@link Metric} instance.
   *
   * @param instanceType the instance type
   * @param hostname the hostname
   * @param metricType the type of the metric
   * @param name the metric name
   * @param value the value
   */
  public Metric(MetricsSystem.InstanceType instanceType, String hostname,
      MetricType metricType, String name, Double value) {
    this(instanceType, hostname, null, metricType, name, value);
  }

  /**
   * Constructs a {@link Metric} instance.
   *
   * @param instanceType the instance type
   * @param hostname the hostname
   * @param id the instance id
   * @param metricType the type of the metric
   * @param name the metric name
   * @param value the value
   */
  public Metric(MetricsSystem.InstanceType instanceType, String hostname, String id,
      MetricType metricType, String name, Double value) {
    Preconditions.checkNotNull(name, "name");
    mMetricIdentifier = new MetricIdentifier(instanceType, hostname, name);
    mInstanceId = id;
    mMetricType = metricType;
    mValue = value;
  }

  /**
   * Add metric value delta to the existing value.
   * This method should only be used by {@link MetricsStore}
   *
   * @param value value to add
   */
  public synchronized void addValue(double value) {
    mValue += value;
  }

  /**
   * Set the metric value.
   * This method should only be used by {@link MetricsStore}
   *
   * @param value value to set
   */
  public synchronized void setValue(double value) {
    mValue = value;
  }

  /**
   * @return the metric value
   */
  public synchronized double getValue() {
    return mValue;
  }

  /**
   * @return the metric identifier
   */
  public MetricIdentifier getMetricIdentifier() {
    return mMetricIdentifier;
  }

  /**
   * @return the instance type
   */
  public MetricsSystem.InstanceType getInstanceType() {
    return mMetricIdentifier.getInstanceType();
  }

  /**
   * Adds a new tag. If the tag name already exists, it will be replaced.
   *
   * @param name the tag name
   * @param value the tag value
   */
  public void addTag(String name, String value) {
    mMetricIdentifier.addTag(name, value);
  }

  /**
   * @return the hostname
   */
  public String getHostname() {
    return mMetricIdentifier.getHostname();
  }

  /**
   * @return the hostname
   */
  public MetricType getMetricType() {
    return mMetricType;
  }

  /**
   * @return the metric name
   */
  public String getName() {
    return mMetricIdentifier.getName();
  }

  /**
   * @return the instance id
   */
  public String getInstanceId() {
    return mInstanceId;
  }

  /**
   * @return the tags
   */
  public Map<String, String> getTags() {
    return mMetricIdentifier.getTags();
  }

  /**
   * Sets the instance id.
   *
   * @param instanceId the instance id;
   */
  public void setInstanceId(String instanceId) {
    mInstanceId = instanceId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Metric)) {
      return false;
    }
    Metric  that = (Metric) o;
    return Objects.equal(mMetricIdentifier, that.mMetricIdentifier)
        && Objects.equal(mInstanceId, that.mInstanceId)
        && Objects.equal(mMetricType, that.mMetricType)
        && Objects.equal(getValue(), that.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(mMetricIdentifier, mMetricType, mInstanceId, getValue());
  }

  /**
   * @return the proto object it converts to. Note the value must be either integer or long
   */
  public alluxio.grpc.Metric toProto() {
    alluxio.grpc.Metric.Builder metric = alluxio.grpc.Metric.newBuilder();
    metric.setInstance(getInstanceType().toString())
        .setHostname(getHostname()).setMetricType(mMetricType)
        .setName(getName()).setValue(getValue()).putAllTags(getTags());

    if (mInstanceId != null && !mInstanceId.isEmpty()) {
      metric.setInstanceId(mInstanceId);
    }

    return metric.build();
  }

  /**
   * Gets the metric name with the appendix of tags. The returned name is of the pattern
   * name[.tagName:tagValue]*.
   *
   * @param name the metric name
   * @param tags the tag name and tag value pairs
   * @return the name with the tags appended
   */
  public static String getMetricNameWithTags(String name, String... tags) {
    Preconditions.checkArgument(tags.length % 2 == 0,
        "The tag arguments should be tag name and tag value pairs");
    StringBuilder sb = new StringBuilder();
    sb.append(name);
    for (int i = 0; i < tags.length; i += 2) {
      sb.append('.').append(tags[i]).append(TAG_SEPARATOR).append(tags[i + 1]);
    }
    return sb.toString();
  }

  /**
   * Gets a metric name with a specific user tag.
   *
   * @param metricName the name of the metric
   * @param userName the user
   * @return a metric name with the user tagged
   */
  public static String getMetricNameWithUserTag(String metricName, String userName) {
    UserMetricKey k = new UserMetricKey(metricName, userName);
    String result = CACHED_METRICS.get(k);
    if (result != null) {
      return result;
    }
    return CACHED_METRICS.computeIfAbsent(k, key -> metricName + "." + CommonMetrics.TAG_USER
        + TAG_SEPARATOR + userName);
  }

  /**
   * Creates the metric from the full name and the value.
   *
   * @param fullName the full name
   * @param value the value
   * @param metricType the type of metric that is being created
   * @return the created metric
   */
  public static Metric from(String fullName, double value, MetricType metricType) {
    String[] pieces = fullName.split("\\.");
    Preconditions.checkArgument(pieces.length > 1, "Incorrect metrics name: %s.", fullName);

    String hostname = null;
    String id = null;
    String name = null;
    int tagStartIdx = 0;
    // Master or cluster metrics don't have hostname included.
    if (pieces[0].equals(MetricsSystem.InstanceType.MASTER.toString())
        || pieces[0].equals(MetricsSystem.CLUSTER)) {
      name = pieces[1];
      tagStartIdx = 2;
    } else {
      if (pieces[1].contains(ID_SEPARATOR)) {
        String[] ids = pieces[1].split(ID_SEPARATOR);
        hostname = ids[0];
        id = ids[1];
      } else {
        hostname = pieces[1];
      }
      name = pieces[2];
      tagStartIdx = 3;
    }
    MetricsSystem.InstanceType instance = MetricsSystem.InstanceType.fromString(pieces[0]);
    Metric metric = new Metric(instance, hostname, id, metricType, name, value);

    // parse tags
    for (int i = tagStartIdx; i < pieces.length; i++) {
      String tagStr = pieces[i];
      if (!tagStr.contains(TAG_SEPARATOR)) {
        // Unknown tag
        continue;
      }
      int tagSeparatorIdx = tagStr.indexOf(TAG_SEPARATOR);
      metric.addTag(tagStr.substring(0, tagSeparatorIdx), tagStr.substring(tagSeparatorIdx + 1));
    }
    return metric;
  }

  /**
   * Constructs the metric object from the proto format.
   *
   * @param metric the metric in proto format
   * @return the constructed metric
   */
  public static Metric fromProto(alluxio.grpc.Metric metric) {
    Metric created = new Metric(
        MetricsSystem.InstanceType.fromString(metric.getInstance()),
        metric.getHostname(),
        metric.hasInstanceId() ? metric.getInstanceId() : null,
        metric.getMetricType(),
        metric.getName(),
        metric.getValue());
    for (Entry<String, String> entry : metric.getTagsMap().entrySet()) {
      created.addTag(entry.getKey(), entry.getValue());
    }
    return created;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("metricIdentifier", mMetricIdentifier)
        .add("instanceId", mInstanceId)
        .add("metricType", mMetricType)
        .add("value", getValue())
        .toString();
  }

  /**
   * Data structure representing a metric name and user name.
   */
  private static class UserMetricKey {
    private String mMetric;
    private String mUser;

    private UserMetricKey(String metricName, String userName) {
      mMetric = metricName;
      mUser = userName;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof UserMetricKey)) {
        return false;
      }
      UserMetricKey that = (UserMetricKey) o;
      return Objects.equal(mMetric, that.mMetric)
          && Objects.equal(mUser, that.mUser);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(mMetric, mUser);
    }
  }
}
