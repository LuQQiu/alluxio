// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: grpc/block_master.proto

package alluxio.grpc;

/**
 * Protobuf type {@code alluxio.grpc.block.GetWorkerIdPRequest}
 */
public  final class GetWorkerIdPRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:alluxio.grpc.block.GetWorkerIdPRequest)
    GetWorkerIdPRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use GetWorkerIdPRequest.newBuilder() to construct.
  private GetWorkerIdPRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private GetWorkerIdPRequest() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new GetWorkerIdPRequest();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private GetWorkerIdPRequest(
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
          case 10: {
            alluxio.grpc.WorkerNetAddress.Builder subBuilder = null;
            if (((bitField0_ & 0x00000001) != 0)) {
              subBuilder = workerNetAddress_.toBuilder();
            }
            workerNetAddress_ = input.readMessage(alluxio.grpc.WorkerNetAddress.PARSER, extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(workerNetAddress_);
              workerNetAddress_ = subBuilder.buildPartial();
            }
            bitField0_ |= 0x00000001;
            break;
          }
          case 18: {
            alluxio.grpc.GetWorkerIdPOptions.Builder subBuilder = null;
            if (((bitField0_ & 0x00000002) != 0)) {
              subBuilder = options_.toBuilder();
            }
            options_ = input.readMessage(alluxio.grpc.GetWorkerIdPOptions.PARSER, extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(options_);
              options_ = subBuilder.buildPartial();
            }
            bitField0_ |= 0x00000002;
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
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
    return alluxio.grpc.BlockMasterProto.internal_static_alluxio_grpc_block_GetWorkerIdPRequest_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return alluxio.grpc.BlockMasterProto.internal_static_alluxio_grpc_block_GetWorkerIdPRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            alluxio.grpc.GetWorkerIdPRequest.class, alluxio.grpc.GetWorkerIdPRequest.Builder.class);
  }

  private int bitField0_;
  public static final int WORKERNETADDRESS_FIELD_NUMBER = 1;
  private alluxio.grpc.WorkerNetAddress workerNetAddress_;
  /**
   * <pre>
   ** the worker network address 
   * </pre>
   *
   * <code>optional .alluxio.grpc.WorkerNetAddress workerNetAddress = 1;</code>
   * @return Whether the workerNetAddress field is set.
   */
  public boolean hasWorkerNetAddress() {
    return ((bitField0_ & 0x00000001) != 0);
  }
  /**
   * <pre>
   ** the worker network address 
   * </pre>
   *
   * <code>optional .alluxio.grpc.WorkerNetAddress workerNetAddress = 1;</code>
   * @return The workerNetAddress.
   */
  public alluxio.grpc.WorkerNetAddress getWorkerNetAddress() {
    return workerNetAddress_ == null ? alluxio.grpc.WorkerNetAddress.getDefaultInstance() : workerNetAddress_;
  }
  /**
   * <pre>
   ** the worker network address 
   * </pre>
   *
   * <code>optional .alluxio.grpc.WorkerNetAddress workerNetAddress = 1;</code>
   */
  public alluxio.grpc.WorkerNetAddressOrBuilder getWorkerNetAddressOrBuilder() {
    return workerNetAddress_ == null ? alluxio.grpc.WorkerNetAddress.getDefaultInstance() : workerNetAddress_;
  }

  public static final int OPTIONS_FIELD_NUMBER = 2;
  private alluxio.grpc.GetWorkerIdPOptions options_;
  /**
   * <code>optional .alluxio.grpc.block.GetWorkerIdPOptions options = 2;</code>
   * @return Whether the options field is set.
   */
  public boolean hasOptions() {
    return ((bitField0_ & 0x00000002) != 0);
  }
  /**
   * <code>optional .alluxio.grpc.block.GetWorkerIdPOptions options = 2;</code>
   * @return The options.
   */
  public alluxio.grpc.GetWorkerIdPOptions getOptions() {
    return options_ == null ? alluxio.grpc.GetWorkerIdPOptions.getDefaultInstance() : options_;
  }
  /**
   * <code>optional .alluxio.grpc.block.GetWorkerIdPOptions options = 2;</code>
   */
  public alluxio.grpc.GetWorkerIdPOptionsOrBuilder getOptionsOrBuilder() {
    return options_ == null ? alluxio.grpc.GetWorkerIdPOptions.getDefaultInstance() : options_;
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (((bitField0_ & 0x00000001) != 0)) {
      output.writeMessage(1, getWorkerNetAddress());
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      output.writeMessage(2, getOptions());
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (((bitField0_ & 0x00000001) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getWorkerNetAddress());
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, getOptions());
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
    if (!(obj instanceof alluxio.grpc.GetWorkerIdPRequest)) {
      return super.equals(obj);
    }
    alluxio.grpc.GetWorkerIdPRequest other = (alluxio.grpc.GetWorkerIdPRequest) obj;

    if (hasWorkerNetAddress() != other.hasWorkerNetAddress()) return false;
    if (hasWorkerNetAddress()) {
      if (!getWorkerNetAddress()
          .equals(other.getWorkerNetAddress())) return false;
    }
    if (hasOptions() != other.hasOptions()) return false;
    if (hasOptions()) {
      if (!getOptions()
          .equals(other.getOptions())) return false;
    }
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (hasWorkerNetAddress()) {
      hash = (37 * hash) + WORKERNETADDRESS_FIELD_NUMBER;
      hash = (53 * hash) + getWorkerNetAddress().hashCode();
    }
    if (hasOptions()) {
      hash = (37 * hash) + OPTIONS_FIELD_NUMBER;
      hash = (53 * hash) + getOptions().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static alluxio.grpc.GetWorkerIdPRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static alluxio.grpc.GetWorkerIdPRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static alluxio.grpc.GetWorkerIdPRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static alluxio.grpc.GetWorkerIdPRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static alluxio.grpc.GetWorkerIdPRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static alluxio.grpc.GetWorkerIdPRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static alluxio.grpc.GetWorkerIdPRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static alluxio.grpc.GetWorkerIdPRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static alluxio.grpc.GetWorkerIdPRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static alluxio.grpc.GetWorkerIdPRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static alluxio.grpc.GetWorkerIdPRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static alluxio.grpc.GetWorkerIdPRequest parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(alluxio.grpc.GetWorkerIdPRequest prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
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
   * Protobuf type {@code alluxio.grpc.block.GetWorkerIdPRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:alluxio.grpc.block.GetWorkerIdPRequest)
      alluxio.grpc.GetWorkerIdPRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return alluxio.grpc.BlockMasterProto.internal_static_alluxio_grpc_block_GetWorkerIdPRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return alluxio.grpc.BlockMasterProto.internal_static_alluxio_grpc_block_GetWorkerIdPRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              alluxio.grpc.GetWorkerIdPRequest.class, alluxio.grpc.GetWorkerIdPRequest.Builder.class);
    }

    // Construct using alluxio.grpc.GetWorkerIdPRequest.newBuilder()
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
        getWorkerNetAddressFieldBuilder();
        getOptionsFieldBuilder();
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      if (workerNetAddressBuilder_ == null) {
        workerNetAddress_ = null;
      } else {
        workerNetAddressBuilder_.clear();
      }
      bitField0_ = (bitField0_ & ~0x00000001);
      if (optionsBuilder_ == null) {
        options_ = null;
      } else {
        optionsBuilder_.clear();
      }
      bitField0_ = (bitField0_ & ~0x00000002);
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return alluxio.grpc.BlockMasterProto.internal_static_alluxio_grpc_block_GetWorkerIdPRequest_descriptor;
    }

    @java.lang.Override
    public alluxio.grpc.GetWorkerIdPRequest getDefaultInstanceForType() {
      return alluxio.grpc.GetWorkerIdPRequest.getDefaultInstance();
    }

    @java.lang.Override
    public alluxio.grpc.GetWorkerIdPRequest build() {
      alluxio.grpc.GetWorkerIdPRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public alluxio.grpc.GetWorkerIdPRequest buildPartial() {
      alluxio.grpc.GetWorkerIdPRequest result = new alluxio.grpc.GetWorkerIdPRequest(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        if (workerNetAddressBuilder_ == null) {
          result.workerNetAddress_ = workerNetAddress_;
        } else {
          result.workerNetAddress_ = workerNetAddressBuilder_.build();
        }
        to_bitField0_ |= 0x00000001;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        if (optionsBuilder_ == null) {
          result.options_ = options_;
        } else {
          result.options_ = optionsBuilder_.build();
        }
        to_bitField0_ |= 0x00000002;
      }
      result.bitField0_ = to_bitField0_;
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof alluxio.grpc.GetWorkerIdPRequest) {
        return mergeFrom((alluxio.grpc.GetWorkerIdPRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(alluxio.grpc.GetWorkerIdPRequest other) {
      if (other == alluxio.grpc.GetWorkerIdPRequest.getDefaultInstance()) return this;
      if (other.hasWorkerNetAddress()) {
        mergeWorkerNetAddress(other.getWorkerNetAddress());
      }
      if (other.hasOptions()) {
        mergeOptions(other.getOptions());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      alluxio.grpc.GetWorkerIdPRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (alluxio.grpc.GetWorkerIdPRequest) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private alluxio.grpc.WorkerNetAddress workerNetAddress_;
    private com.google.protobuf.SingleFieldBuilderV3<
        alluxio.grpc.WorkerNetAddress, alluxio.grpc.WorkerNetAddress.Builder, alluxio.grpc.WorkerNetAddressOrBuilder> workerNetAddressBuilder_;
    /**
     * <pre>
     ** the worker network address 
     * </pre>
     *
     * <code>optional .alluxio.grpc.WorkerNetAddress workerNetAddress = 1;</code>
     * @return Whether the workerNetAddress field is set.
     */
    public boolean hasWorkerNetAddress() {
      return ((bitField0_ & 0x00000001) != 0);
    }
    /**
     * <pre>
     ** the worker network address 
     * </pre>
     *
     * <code>optional .alluxio.grpc.WorkerNetAddress workerNetAddress = 1;</code>
     * @return The workerNetAddress.
     */
    public alluxio.grpc.WorkerNetAddress getWorkerNetAddress() {
      if (workerNetAddressBuilder_ == null) {
        return workerNetAddress_ == null ? alluxio.grpc.WorkerNetAddress.getDefaultInstance() : workerNetAddress_;
      } else {
        return workerNetAddressBuilder_.getMessage();
      }
    }
    /**
     * <pre>
     ** the worker network address 
     * </pre>
     *
     * <code>optional .alluxio.grpc.WorkerNetAddress workerNetAddress = 1;</code>
     */
    public Builder setWorkerNetAddress(alluxio.grpc.WorkerNetAddress value) {
      if (workerNetAddressBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        workerNetAddress_ = value;
        onChanged();
      } else {
        workerNetAddressBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000001;
      return this;
    }
    /**
     * <pre>
     ** the worker network address 
     * </pre>
     *
     * <code>optional .alluxio.grpc.WorkerNetAddress workerNetAddress = 1;</code>
     */
    public Builder setWorkerNetAddress(
        alluxio.grpc.WorkerNetAddress.Builder builderForValue) {
      if (workerNetAddressBuilder_ == null) {
        workerNetAddress_ = builderForValue.build();
        onChanged();
      } else {
        workerNetAddressBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000001;
      return this;
    }
    /**
     * <pre>
     ** the worker network address 
     * </pre>
     *
     * <code>optional .alluxio.grpc.WorkerNetAddress workerNetAddress = 1;</code>
     */
    public Builder mergeWorkerNetAddress(alluxio.grpc.WorkerNetAddress value) {
      if (workerNetAddressBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0) &&
            workerNetAddress_ != null &&
            workerNetAddress_ != alluxio.grpc.WorkerNetAddress.getDefaultInstance()) {
          workerNetAddress_ =
            alluxio.grpc.WorkerNetAddress.newBuilder(workerNetAddress_).mergeFrom(value).buildPartial();
        } else {
          workerNetAddress_ = value;
        }
        onChanged();
      } else {
        workerNetAddressBuilder_.mergeFrom(value);
      }
      bitField0_ |= 0x00000001;
      return this;
    }
    /**
     * <pre>
     ** the worker network address 
     * </pre>
     *
     * <code>optional .alluxio.grpc.WorkerNetAddress workerNetAddress = 1;</code>
     */
    public Builder clearWorkerNetAddress() {
      if (workerNetAddressBuilder_ == null) {
        workerNetAddress_ = null;
        onChanged();
      } else {
        workerNetAddressBuilder_.clear();
      }
      bitField0_ = (bitField0_ & ~0x00000001);
      return this;
    }
    /**
     * <pre>
     ** the worker network address 
     * </pre>
     *
     * <code>optional .alluxio.grpc.WorkerNetAddress workerNetAddress = 1;</code>
     */
    public alluxio.grpc.WorkerNetAddress.Builder getWorkerNetAddressBuilder() {
      bitField0_ |= 0x00000001;
      onChanged();
      return getWorkerNetAddressFieldBuilder().getBuilder();
    }
    /**
     * <pre>
     ** the worker network address 
     * </pre>
     *
     * <code>optional .alluxio.grpc.WorkerNetAddress workerNetAddress = 1;</code>
     */
    public alluxio.grpc.WorkerNetAddressOrBuilder getWorkerNetAddressOrBuilder() {
      if (workerNetAddressBuilder_ != null) {
        return workerNetAddressBuilder_.getMessageOrBuilder();
      } else {
        return workerNetAddress_ == null ?
            alluxio.grpc.WorkerNetAddress.getDefaultInstance() : workerNetAddress_;
      }
    }
    /**
     * <pre>
     ** the worker network address 
     * </pre>
     *
     * <code>optional .alluxio.grpc.WorkerNetAddress workerNetAddress = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        alluxio.grpc.WorkerNetAddress, alluxio.grpc.WorkerNetAddress.Builder, alluxio.grpc.WorkerNetAddressOrBuilder> 
        getWorkerNetAddressFieldBuilder() {
      if (workerNetAddressBuilder_ == null) {
        workerNetAddressBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            alluxio.grpc.WorkerNetAddress, alluxio.grpc.WorkerNetAddress.Builder, alluxio.grpc.WorkerNetAddressOrBuilder>(
                getWorkerNetAddress(),
                getParentForChildren(),
                isClean());
        workerNetAddress_ = null;
      }
      return workerNetAddressBuilder_;
    }

    private alluxio.grpc.GetWorkerIdPOptions options_;
    private com.google.protobuf.SingleFieldBuilderV3<
        alluxio.grpc.GetWorkerIdPOptions, alluxio.grpc.GetWorkerIdPOptions.Builder, alluxio.grpc.GetWorkerIdPOptionsOrBuilder> optionsBuilder_;
    /**
     * <code>optional .alluxio.grpc.block.GetWorkerIdPOptions options = 2;</code>
     * @return Whether the options field is set.
     */
    public boolean hasOptions() {
      return ((bitField0_ & 0x00000002) != 0);
    }
    /**
     * <code>optional .alluxio.grpc.block.GetWorkerIdPOptions options = 2;</code>
     * @return The options.
     */
    public alluxio.grpc.GetWorkerIdPOptions getOptions() {
      if (optionsBuilder_ == null) {
        return options_ == null ? alluxio.grpc.GetWorkerIdPOptions.getDefaultInstance() : options_;
      } else {
        return optionsBuilder_.getMessage();
      }
    }
    /**
     * <code>optional .alluxio.grpc.block.GetWorkerIdPOptions options = 2;</code>
     */
    public Builder setOptions(alluxio.grpc.GetWorkerIdPOptions value) {
      if (optionsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        options_ = value;
        onChanged();
      } else {
        optionsBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000002;
      return this;
    }
    /**
     * <code>optional .alluxio.grpc.block.GetWorkerIdPOptions options = 2;</code>
     */
    public Builder setOptions(
        alluxio.grpc.GetWorkerIdPOptions.Builder builderForValue) {
      if (optionsBuilder_ == null) {
        options_ = builderForValue.build();
        onChanged();
      } else {
        optionsBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000002;
      return this;
    }
    /**
     * <code>optional .alluxio.grpc.block.GetWorkerIdPOptions options = 2;</code>
     */
    public Builder mergeOptions(alluxio.grpc.GetWorkerIdPOptions value) {
      if (optionsBuilder_ == null) {
        if (((bitField0_ & 0x00000002) != 0) &&
            options_ != null &&
            options_ != alluxio.grpc.GetWorkerIdPOptions.getDefaultInstance()) {
          options_ =
            alluxio.grpc.GetWorkerIdPOptions.newBuilder(options_).mergeFrom(value).buildPartial();
        } else {
          options_ = value;
        }
        onChanged();
      } else {
        optionsBuilder_.mergeFrom(value);
      }
      bitField0_ |= 0x00000002;
      return this;
    }
    /**
     * <code>optional .alluxio.grpc.block.GetWorkerIdPOptions options = 2;</code>
     */
    public Builder clearOptions() {
      if (optionsBuilder_ == null) {
        options_ = null;
        onChanged();
      } else {
        optionsBuilder_.clear();
      }
      bitField0_ = (bitField0_ & ~0x00000002);
      return this;
    }
    /**
     * <code>optional .alluxio.grpc.block.GetWorkerIdPOptions options = 2;</code>
     */
    public alluxio.grpc.GetWorkerIdPOptions.Builder getOptionsBuilder() {
      bitField0_ |= 0x00000002;
      onChanged();
      return getOptionsFieldBuilder().getBuilder();
    }
    /**
     * <code>optional .alluxio.grpc.block.GetWorkerIdPOptions options = 2;</code>
     */
    public alluxio.grpc.GetWorkerIdPOptionsOrBuilder getOptionsOrBuilder() {
      if (optionsBuilder_ != null) {
        return optionsBuilder_.getMessageOrBuilder();
      } else {
        return options_ == null ?
            alluxio.grpc.GetWorkerIdPOptions.getDefaultInstance() : options_;
      }
    }
    /**
     * <code>optional .alluxio.grpc.block.GetWorkerIdPOptions options = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        alluxio.grpc.GetWorkerIdPOptions, alluxio.grpc.GetWorkerIdPOptions.Builder, alluxio.grpc.GetWorkerIdPOptionsOrBuilder> 
        getOptionsFieldBuilder() {
      if (optionsBuilder_ == null) {
        optionsBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            alluxio.grpc.GetWorkerIdPOptions, alluxio.grpc.GetWorkerIdPOptions.Builder, alluxio.grpc.GetWorkerIdPOptionsOrBuilder>(
                getOptions(),
                getParentForChildren(),
                isClean());
        options_ = null;
      }
      return optionsBuilder_;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:alluxio.grpc.block.GetWorkerIdPRequest)
  }

  // @@protoc_insertion_point(class_scope:alluxio.grpc.block.GetWorkerIdPRequest)
  private static final alluxio.grpc.GetWorkerIdPRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new alluxio.grpc.GetWorkerIdPRequest();
  }

  public static alluxio.grpc.GetWorkerIdPRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  @java.lang.Deprecated public static final com.google.protobuf.Parser<GetWorkerIdPRequest>
      PARSER = new com.google.protobuf.AbstractParser<GetWorkerIdPRequest>() {
    @java.lang.Override
    public GetWorkerIdPRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new GetWorkerIdPRequest(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<GetWorkerIdPRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<GetWorkerIdPRequest> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public alluxio.grpc.GetWorkerIdPRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

