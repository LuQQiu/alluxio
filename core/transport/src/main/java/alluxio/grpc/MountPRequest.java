// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: grpc/file_system_master.proto

package alluxio.grpc;

/**
 * Protobuf type {@code alluxio.grpc.file.MountPRequest}
 */
public  final class MountPRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:alluxio.grpc.file.MountPRequest)
    MountPRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use MountPRequest.newBuilder() to construct.
  private MountPRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private MountPRequest() {
    alluxioPath_ = "";
    ufsPath_ = "";
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private MountPRequest(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
          case 10: {
            com.google.protobuf.ByteString bs = input.readBytes();
            bitField0_ |= 0x00000001;
            alluxioPath_ = bs;
            break;
          }
          case 18: {
            com.google.protobuf.ByteString bs = input.readBytes();
            bitField0_ |= 0x00000002;
            ufsPath_ = bs;
            break;
          }
          case 26: {
            alluxio.grpc.MountPOptions.Builder subBuilder = null;
            if (((bitField0_ & 0x00000004) == 0x00000004)) {
              subBuilder = options_.toBuilder();
            }
            options_ = input.readMessage(alluxio.grpc.MountPOptions.PARSER, extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(options_);
              options_ = subBuilder.buildPartial();
            }
            bitField0_ |= 0x00000004;
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return alluxio.grpc.FileSystemMasterProto.internal_static_alluxio_grpc_file_MountPRequest_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return alluxio.grpc.FileSystemMasterProto.internal_static_alluxio_grpc_file_MountPRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            alluxio.grpc.MountPRequest.class, alluxio.grpc.MountPRequest.Builder.class);
  }

  private int bitField0_;
  public static final int ALLUXIOPATH_FIELD_NUMBER = 1;
  private volatile java.lang.Object alluxioPath_;
  /**
   * <pre>
   ** the path of alluxio mount point 
   * </pre>
   *
   * <code>optional string alluxioPath = 1;</code>
   */
  public boolean hasAlluxioPath() {
    return ((bitField0_ & 0x00000001) == 0x00000001);
  }
  /**
   * <pre>
   ** the path of alluxio mount point 
   * </pre>
   *
   * <code>optional string alluxioPath = 1;</code>
   */
  public java.lang.String getAlluxioPath() {
    java.lang.Object ref = alluxioPath_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      if (bs.isValidUtf8()) {
        alluxioPath_ = s;
      }
      return s;
    }
  }
  /**
   * <pre>
   ** the path of alluxio mount point 
   * </pre>
   *
   * <code>optional string alluxioPath = 1;</code>
   */
  public com.google.protobuf.ByteString
      getAlluxioPathBytes() {
    java.lang.Object ref = alluxioPath_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      alluxioPath_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int UFSPATH_FIELD_NUMBER = 2;
  private volatile java.lang.Object ufsPath_;
  /**
   * <pre>
   ** the path of the under file system 
   * </pre>
   *
   * <code>optional string ufsPath = 2;</code>
   */
  public boolean hasUfsPath() {
    return ((bitField0_ & 0x00000002) == 0x00000002);
  }
  /**
   * <pre>
   ** the path of the under file system 
   * </pre>
   *
   * <code>optional string ufsPath = 2;</code>
   */
  public java.lang.String getUfsPath() {
    java.lang.Object ref = ufsPath_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      if (bs.isValidUtf8()) {
        ufsPath_ = s;
      }
      return s;
    }
  }
  /**
   * <pre>
   ** the path of the under file system 
   * </pre>
   *
   * <code>optional string ufsPath = 2;</code>
   */
  public com.google.protobuf.ByteString
      getUfsPathBytes() {
    java.lang.Object ref = ufsPath_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      ufsPath_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int OPTIONS_FIELD_NUMBER = 3;
  private alluxio.grpc.MountPOptions options_;
  /**
   * <code>optional .alluxio.grpc.file.MountPOptions options = 3;</code>
   */
  public boolean hasOptions() {
    return ((bitField0_ & 0x00000004) == 0x00000004);
  }
  /**
   * <code>optional .alluxio.grpc.file.MountPOptions options = 3;</code>
   */
  public alluxio.grpc.MountPOptions getOptions() {
    return options_ == null ? alluxio.grpc.MountPOptions.getDefaultInstance() : options_;
  }
  /**
   * <code>optional .alluxio.grpc.file.MountPOptions options = 3;</code>
   */
  public alluxio.grpc.MountPOptionsOrBuilder getOptionsOrBuilder() {
    return options_ == null ? alluxio.grpc.MountPOptions.getDefaultInstance() : options_;
  }

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (((bitField0_ & 0x00000001) == 0x00000001)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, alluxioPath_);
    }
    if (((bitField0_ & 0x00000002) == 0x00000002)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, ufsPath_);
    }
    if (((bitField0_ & 0x00000004) == 0x00000004)) {
      output.writeMessage(3, getOptions());
    }
    unknownFields.writeTo(output);
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (((bitField0_ & 0x00000001) == 0x00000001)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, alluxioPath_);
    }
    if (((bitField0_ & 0x00000002) == 0x00000002)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, ufsPath_);
    }
    if (((bitField0_ & 0x00000004) == 0x00000004)) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, getOptions());
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof alluxio.grpc.MountPRequest)) {
      return super.equals(obj);
    }
    alluxio.grpc.MountPRequest other = (alluxio.grpc.MountPRequest) obj;

    boolean result = true;
    result = result && (hasAlluxioPath() == other.hasAlluxioPath());
    if (hasAlluxioPath()) {
      result = result && getAlluxioPath()
          .equals(other.getAlluxioPath());
    }
    result = result && (hasUfsPath() == other.hasUfsPath());
    if (hasUfsPath()) {
      result = result && getUfsPath()
          .equals(other.getUfsPath());
    }
    result = result && (hasOptions() == other.hasOptions());
    if (hasOptions()) {
      result = result && getOptions()
          .equals(other.getOptions());
    }
    result = result && unknownFields.equals(other.unknownFields);
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (hasAlluxioPath()) {
      hash = (37 * hash) + ALLUXIOPATH_FIELD_NUMBER;
      hash = (53 * hash) + getAlluxioPath().hashCode();
    }
    if (hasUfsPath()) {
      hash = (37 * hash) + UFSPATH_FIELD_NUMBER;
      hash = (53 * hash) + getUfsPath().hashCode();
    }
    if (hasOptions()) {
      hash = (37 * hash) + OPTIONS_FIELD_NUMBER;
      hash = (53 * hash) + getOptions().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static alluxio.grpc.MountPRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static alluxio.grpc.MountPRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static alluxio.grpc.MountPRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static alluxio.grpc.MountPRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static alluxio.grpc.MountPRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static alluxio.grpc.MountPRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static alluxio.grpc.MountPRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static alluxio.grpc.MountPRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static alluxio.grpc.MountPRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static alluxio.grpc.MountPRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static alluxio.grpc.MountPRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static alluxio.grpc.MountPRequest parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(alluxio.grpc.MountPRequest prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code alluxio.grpc.file.MountPRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:alluxio.grpc.file.MountPRequest)
      alluxio.grpc.MountPRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return alluxio.grpc.FileSystemMasterProto.internal_static_alluxio_grpc_file_MountPRequest_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return alluxio.grpc.FileSystemMasterProto.internal_static_alluxio_grpc_file_MountPRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              alluxio.grpc.MountPRequest.class, alluxio.grpc.MountPRequest.Builder.class);
    }

    // Construct using alluxio.grpc.MountPRequest.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
        getOptionsFieldBuilder();
      }
    }
    public Builder clear() {
      super.clear();
      alluxioPath_ = "";
      bitField0_ = (bitField0_ & ~0x00000001);
      ufsPath_ = "";
      bitField0_ = (bitField0_ & ~0x00000002);
      if (optionsBuilder_ == null) {
        options_ = null;
      } else {
        optionsBuilder_.clear();
      }
      bitField0_ = (bitField0_ & ~0x00000004);
      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return alluxio.grpc.FileSystemMasterProto.internal_static_alluxio_grpc_file_MountPRequest_descriptor;
    }

    public alluxio.grpc.MountPRequest getDefaultInstanceForType() {
      return alluxio.grpc.MountPRequest.getDefaultInstance();
    }

    public alluxio.grpc.MountPRequest build() {
      alluxio.grpc.MountPRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public alluxio.grpc.MountPRequest buildPartial() {
      alluxio.grpc.MountPRequest result = new alluxio.grpc.MountPRequest(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
        to_bitField0_ |= 0x00000001;
      }
      result.alluxioPath_ = alluxioPath_;
      if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
        to_bitField0_ |= 0x00000002;
      }
      result.ufsPath_ = ufsPath_;
      if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
        to_bitField0_ |= 0x00000004;
      }
      if (optionsBuilder_ == null) {
        result.options_ = options_;
      } else {
        result.options_ = optionsBuilder_.build();
      }
      result.bitField0_ = to_bitField0_;
      onBuilt();
      return result;
    }

    public Builder clone() {
      return (Builder) super.clone();
    }
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.setField(field, value);
    }
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof alluxio.grpc.MountPRequest) {
        return mergeFrom((alluxio.grpc.MountPRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(alluxio.grpc.MountPRequest other) {
      if (other == alluxio.grpc.MountPRequest.getDefaultInstance()) return this;
      if (other.hasAlluxioPath()) {
        bitField0_ |= 0x00000001;
        alluxioPath_ = other.alluxioPath_;
        onChanged();
      }
      if (other.hasUfsPath()) {
        bitField0_ |= 0x00000002;
        ufsPath_ = other.ufsPath_;
        onChanged();
      }
      if (other.hasOptions()) {
        mergeOptions(other.getOptions());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      alluxio.grpc.MountPRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (alluxio.grpc.MountPRequest) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.lang.Object alluxioPath_ = "";
    /**
     * <pre>
     ** the path of alluxio mount point 
     * </pre>
     *
     * <code>optional string alluxioPath = 1;</code>
     */
    public boolean hasAlluxioPath() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <pre>
     ** the path of alluxio mount point 
     * </pre>
     *
     * <code>optional string alluxioPath = 1;</code>
     */
    public java.lang.String getAlluxioPath() {
      java.lang.Object ref = alluxioPath_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          alluxioPath_ = s;
        }
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <pre>
     ** the path of alluxio mount point 
     * </pre>
     *
     * <code>optional string alluxioPath = 1;</code>
     */
    public com.google.protobuf.ByteString
        getAlluxioPathBytes() {
      java.lang.Object ref = alluxioPath_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        alluxioPath_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     ** the path of alluxio mount point 
     * </pre>
     *
     * <code>optional string alluxioPath = 1;</code>
     */
    public Builder setAlluxioPath(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
      alluxioPath_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     ** the path of alluxio mount point 
     * </pre>
     *
     * <code>optional string alluxioPath = 1;</code>
     */
    public Builder clearAlluxioPath() {
      bitField0_ = (bitField0_ & ~0x00000001);
      alluxioPath_ = getDefaultInstance().getAlluxioPath();
      onChanged();
      return this;
    }
    /**
     * <pre>
     ** the path of alluxio mount point 
     * </pre>
     *
     * <code>optional string alluxioPath = 1;</code>
     */
    public Builder setAlluxioPathBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
      alluxioPath_ = value;
      onChanged();
      return this;
    }

    private java.lang.Object ufsPath_ = "";
    /**
     * <pre>
     ** the path of the under file system 
     * </pre>
     *
     * <code>optional string ufsPath = 2;</code>
     */
    public boolean hasUfsPath() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <pre>
     ** the path of the under file system 
     * </pre>
     *
     * <code>optional string ufsPath = 2;</code>
     */
    public java.lang.String getUfsPath() {
      java.lang.Object ref = ufsPath_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          ufsPath_ = s;
        }
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <pre>
     ** the path of the under file system 
     * </pre>
     *
     * <code>optional string ufsPath = 2;</code>
     */
    public com.google.protobuf.ByteString
        getUfsPathBytes() {
      java.lang.Object ref = ufsPath_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        ufsPath_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     ** the path of the under file system 
     * </pre>
     *
     * <code>optional string ufsPath = 2;</code>
     */
    public Builder setUfsPath(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
      ufsPath_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     ** the path of the under file system 
     * </pre>
     *
     * <code>optional string ufsPath = 2;</code>
     */
    public Builder clearUfsPath() {
      bitField0_ = (bitField0_ & ~0x00000002);
      ufsPath_ = getDefaultInstance().getUfsPath();
      onChanged();
      return this;
    }
    /**
     * <pre>
     ** the path of the under file system 
     * </pre>
     *
     * <code>optional string ufsPath = 2;</code>
     */
    public Builder setUfsPathBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
      ufsPath_ = value;
      onChanged();
      return this;
    }

    private alluxio.grpc.MountPOptions options_ = null;
    private com.google.protobuf.SingleFieldBuilderV3<
        alluxio.grpc.MountPOptions, alluxio.grpc.MountPOptions.Builder, alluxio.grpc.MountPOptionsOrBuilder> optionsBuilder_;
    /**
     * <code>optional .alluxio.grpc.file.MountPOptions options = 3;</code>
     */
    public boolean hasOptions() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional .alluxio.grpc.file.MountPOptions options = 3;</code>
     */
    public alluxio.grpc.MountPOptions getOptions() {
      if (optionsBuilder_ == null) {
        return options_ == null ? alluxio.grpc.MountPOptions.getDefaultInstance() : options_;
      } else {
        return optionsBuilder_.getMessage();
      }
    }
    /**
     * <code>optional .alluxio.grpc.file.MountPOptions options = 3;</code>
     */
    public Builder setOptions(alluxio.grpc.MountPOptions value) {
      if (optionsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        options_ = value;
        onChanged();
      } else {
        optionsBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000004;
      return this;
    }
    /**
     * <code>optional .alluxio.grpc.file.MountPOptions options = 3;</code>
     */
    public Builder setOptions(
        alluxio.grpc.MountPOptions.Builder builderForValue) {
      if (optionsBuilder_ == null) {
        options_ = builderForValue.build();
        onChanged();
      } else {
        optionsBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000004;
      return this;
    }
    /**
     * <code>optional .alluxio.grpc.file.MountPOptions options = 3;</code>
     */
    public Builder mergeOptions(alluxio.grpc.MountPOptions value) {
      if (optionsBuilder_ == null) {
        if (((bitField0_ & 0x00000004) == 0x00000004) &&
            options_ != null &&
            options_ != alluxio.grpc.MountPOptions.getDefaultInstance()) {
          options_ =
            alluxio.grpc.MountPOptions.newBuilder(options_).mergeFrom(value).buildPartial();
        } else {
          options_ = value;
        }
        onChanged();
      } else {
        optionsBuilder_.mergeFrom(value);
      }
      bitField0_ |= 0x00000004;
      return this;
    }
    /**
     * <code>optional .alluxio.grpc.file.MountPOptions options = 3;</code>
     */
    public Builder clearOptions() {
      if (optionsBuilder_ == null) {
        options_ = null;
        onChanged();
      } else {
        optionsBuilder_.clear();
      }
      bitField0_ = (bitField0_ & ~0x00000004);
      return this;
    }
    /**
     * <code>optional .alluxio.grpc.file.MountPOptions options = 3;</code>
     */
    public alluxio.grpc.MountPOptions.Builder getOptionsBuilder() {
      bitField0_ |= 0x00000004;
      onChanged();
      return getOptionsFieldBuilder().getBuilder();
    }
    /**
     * <code>optional .alluxio.grpc.file.MountPOptions options = 3;</code>
     */
    public alluxio.grpc.MountPOptionsOrBuilder getOptionsOrBuilder() {
      if (optionsBuilder_ != null) {
        return optionsBuilder_.getMessageOrBuilder();
      } else {
        return options_ == null ?
            alluxio.grpc.MountPOptions.getDefaultInstance() : options_;
      }
    }
    /**
     * <code>optional .alluxio.grpc.file.MountPOptions options = 3;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        alluxio.grpc.MountPOptions, alluxio.grpc.MountPOptions.Builder, alluxio.grpc.MountPOptionsOrBuilder> 
        getOptionsFieldBuilder() {
      if (optionsBuilder_ == null) {
        optionsBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            alluxio.grpc.MountPOptions, alluxio.grpc.MountPOptions.Builder, alluxio.grpc.MountPOptionsOrBuilder>(
                getOptions(),
                getParentForChildren(),
                isClean());
        options_ = null;
      }
      return optionsBuilder_;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:alluxio.grpc.file.MountPRequest)
  }

  // @@protoc_insertion_point(class_scope:alluxio.grpc.file.MountPRequest)
  private static final alluxio.grpc.MountPRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new alluxio.grpc.MountPRequest();
  }

  public static alluxio.grpc.MountPRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  @java.lang.Deprecated public static final com.google.protobuf.Parser<MountPRequest>
      PARSER = new com.google.protobuf.AbstractParser<MountPRequest>() {
    public MountPRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new MountPRequest(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<MountPRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<MountPRequest> getParserForType() {
    return PARSER;
  }

  public alluxio.grpc.MountPRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

