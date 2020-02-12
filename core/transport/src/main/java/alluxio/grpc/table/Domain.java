// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: grpc/table/table_master.proto

package alluxio.grpc.table;

/**
 * Protobuf type {@code alluxio.grpc.table.Domain}
 */
public  final class Domain extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:alluxio.grpc.table.Domain)
    DomainOrBuilder {
private static final long serialVersionUID = 0L;
  // Use Domain.newBuilder() to construct.
  private Domain(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private Domain() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new Domain();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private Domain(
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
            alluxio.grpc.table.RangeSet.Builder subBuilder = null;
            if (valueSetCase_ == 1) {
              subBuilder = ((alluxio.grpc.table.RangeSet) valueSet_).toBuilder();
            }
            valueSet_ =
                input.readMessage(alluxio.grpc.table.RangeSet.PARSER, extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom((alluxio.grpc.table.RangeSet) valueSet_);
              valueSet_ = subBuilder.buildPartial();
            }
            valueSetCase_ = 1;
            break;
          }
          case 18: {
            alluxio.grpc.table.EquatableValueSet.Builder subBuilder = null;
            if (valueSetCase_ == 2) {
              subBuilder = ((alluxio.grpc.table.EquatableValueSet) valueSet_).toBuilder();
            }
            valueSet_ =
                input.readMessage(alluxio.grpc.table.EquatableValueSet.PARSER, extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom((alluxio.grpc.table.EquatableValueSet) valueSet_);
              valueSet_ = subBuilder.buildPartial();
            }
            valueSetCase_ = 2;
            break;
          }
          case 26: {
            alluxio.grpc.table.AllOrNoneSet.Builder subBuilder = null;
            if (valueSetCase_ == 3) {
              subBuilder = ((alluxio.grpc.table.AllOrNoneSet) valueSet_).toBuilder();
            }
            valueSet_ =
                input.readMessage(alluxio.grpc.table.AllOrNoneSet.PARSER, extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom((alluxio.grpc.table.AllOrNoneSet) valueSet_);
              valueSet_ = subBuilder.buildPartial();
            }
            valueSetCase_ = 3;
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
    return alluxio.grpc.table.TableMasterProto.internal_static_alluxio_grpc_table_Domain_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return alluxio.grpc.table.TableMasterProto.internal_static_alluxio_grpc_table_Domain_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            alluxio.grpc.table.Domain.class, alluxio.grpc.table.Domain.Builder.class);
  }

  private int bitField0_;
  private int valueSetCase_ = 0;
  private java.lang.Object valueSet_;
  public enum ValueSetCase
      implements com.google.protobuf.Internal.EnumLite,
          com.google.protobuf.AbstractMessage.InternalOneOfEnum {
    RANGE(1),
    EQUATABLE(2),
    ALL_OR_NONE(3),
    VALUESET_NOT_SET(0);
    private final int value;
    private ValueSetCase(int value) {
      this.value = value;
    }
    /**
     * @param value The number of the enum to look for.
     * @return The enum associated with the given number.
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static ValueSetCase valueOf(int value) {
      return forNumber(value);
    }

    public static ValueSetCase forNumber(int value) {
      switch (value) {
        case 1: return RANGE;
        case 2: return EQUATABLE;
        case 3: return ALL_OR_NONE;
        case 0: return VALUESET_NOT_SET;
        default: return null;
      }
    }
    public int getNumber() {
      return this.value;
    }
  };

  public ValueSetCase
  getValueSetCase() {
    return ValueSetCase.forNumber(
        valueSetCase_);
  }

  public static final int RANGE_FIELD_NUMBER = 1;
  /**
   * <code>optional .alluxio.grpc.table.RangeSet range = 1;</code>
   * @return Whether the range field is set.
   */
  public boolean hasRange() {
    return valueSetCase_ == 1;
  }
  /**
   * <code>optional .alluxio.grpc.table.RangeSet range = 1;</code>
   * @return The range.
   */
  public alluxio.grpc.table.RangeSet getRange() {
    if (valueSetCase_ == 1) {
       return (alluxio.grpc.table.RangeSet) valueSet_;
    }
    return alluxio.grpc.table.RangeSet.getDefaultInstance();
  }
  /**
   * <code>optional .alluxio.grpc.table.RangeSet range = 1;</code>
   */
  public alluxio.grpc.table.RangeSetOrBuilder getRangeOrBuilder() {
    if (valueSetCase_ == 1) {
       return (alluxio.grpc.table.RangeSet) valueSet_;
    }
    return alluxio.grpc.table.RangeSet.getDefaultInstance();
  }

  public static final int EQUATABLE_FIELD_NUMBER = 2;
  /**
   * <code>optional .alluxio.grpc.table.EquatableValueSet equatable = 2;</code>
   * @return Whether the equatable field is set.
   */
  public boolean hasEquatable() {
    return valueSetCase_ == 2;
  }
  /**
   * <code>optional .alluxio.grpc.table.EquatableValueSet equatable = 2;</code>
   * @return The equatable.
   */
  public alluxio.grpc.table.EquatableValueSet getEquatable() {
    if (valueSetCase_ == 2) {
       return (alluxio.grpc.table.EquatableValueSet) valueSet_;
    }
    return alluxio.grpc.table.EquatableValueSet.getDefaultInstance();
  }
  /**
   * <code>optional .alluxio.grpc.table.EquatableValueSet equatable = 2;</code>
   */
  public alluxio.grpc.table.EquatableValueSetOrBuilder getEquatableOrBuilder() {
    if (valueSetCase_ == 2) {
       return (alluxio.grpc.table.EquatableValueSet) valueSet_;
    }
    return alluxio.grpc.table.EquatableValueSet.getDefaultInstance();
  }

  public static final int ALL_OR_NONE_FIELD_NUMBER = 3;
  /**
   * <code>optional .alluxio.grpc.table.AllOrNoneSet all_or_none = 3;</code>
   * @return Whether the allOrNone field is set.
   */
  public boolean hasAllOrNone() {
    return valueSetCase_ == 3;
  }
  /**
   * <code>optional .alluxio.grpc.table.AllOrNoneSet all_or_none = 3;</code>
   * @return The allOrNone.
   */
  public alluxio.grpc.table.AllOrNoneSet getAllOrNone() {
    if (valueSetCase_ == 3) {
       return (alluxio.grpc.table.AllOrNoneSet) valueSet_;
    }
    return alluxio.grpc.table.AllOrNoneSet.getDefaultInstance();
  }
  /**
   * <code>optional .alluxio.grpc.table.AllOrNoneSet all_or_none = 3;</code>
   */
  public alluxio.grpc.table.AllOrNoneSetOrBuilder getAllOrNoneOrBuilder() {
    if (valueSetCase_ == 3) {
       return (alluxio.grpc.table.AllOrNoneSet) valueSet_;
    }
    return alluxio.grpc.table.AllOrNoneSet.getDefaultInstance();
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
    if (valueSetCase_ == 1) {
      output.writeMessage(1, (alluxio.grpc.table.RangeSet) valueSet_);
    }
    if (valueSetCase_ == 2) {
      output.writeMessage(2, (alluxio.grpc.table.EquatableValueSet) valueSet_);
    }
    if (valueSetCase_ == 3) {
      output.writeMessage(3, (alluxio.grpc.table.AllOrNoneSet) valueSet_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (valueSetCase_ == 1) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, (alluxio.grpc.table.RangeSet) valueSet_);
    }
    if (valueSetCase_ == 2) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, (alluxio.grpc.table.EquatableValueSet) valueSet_);
    }
    if (valueSetCase_ == 3) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, (alluxio.grpc.table.AllOrNoneSet) valueSet_);
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
    if (!(obj instanceof alluxio.grpc.table.Domain)) {
      return super.equals(obj);
    }
    alluxio.grpc.table.Domain other = (alluxio.grpc.table.Domain) obj;

    if (!getValueSetCase().equals(other.getValueSetCase())) return false;
    switch (valueSetCase_) {
      case 1:
        if (!getRange()
            .equals(other.getRange())) return false;
        break;
      case 2:
        if (!getEquatable()
            .equals(other.getEquatable())) return false;
        break;
      case 3:
        if (!getAllOrNone()
            .equals(other.getAllOrNone())) return false;
        break;
      case 0:
      default:
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
    switch (valueSetCase_) {
      case 1:
        hash = (37 * hash) + RANGE_FIELD_NUMBER;
        hash = (53 * hash) + getRange().hashCode();
        break;
      case 2:
        hash = (37 * hash) + EQUATABLE_FIELD_NUMBER;
        hash = (53 * hash) + getEquatable().hashCode();
        break;
      case 3:
        hash = (37 * hash) + ALL_OR_NONE_FIELD_NUMBER;
        hash = (53 * hash) + getAllOrNone().hashCode();
        break;
      case 0:
      default:
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static alluxio.grpc.table.Domain parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static alluxio.grpc.table.Domain parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static alluxio.grpc.table.Domain parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static alluxio.grpc.table.Domain parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static alluxio.grpc.table.Domain parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static alluxio.grpc.table.Domain parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static alluxio.grpc.table.Domain parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static alluxio.grpc.table.Domain parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static alluxio.grpc.table.Domain parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static alluxio.grpc.table.Domain parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static alluxio.grpc.table.Domain parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static alluxio.grpc.table.Domain parseFrom(
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
  public static Builder newBuilder(alluxio.grpc.table.Domain prototype) {
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
   * Protobuf type {@code alluxio.grpc.table.Domain}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:alluxio.grpc.table.Domain)
      alluxio.grpc.table.DomainOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return alluxio.grpc.table.TableMasterProto.internal_static_alluxio_grpc_table_Domain_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return alluxio.grpc.table.TableMasterProto.internal_static_alluxio_grpc_table_Domain_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              alluxio.grpc.table.Domain.class, alluxio.grpc.table.Domain.Builder.class);
    }

    // Construct using alluxio.grpc.table.Domain.newBuilder()
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
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      valueSetCase_ = 0;
      valueSet_ = null;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return alluxio.grpc.table.TableMasterProto.internal_static_alluxio_grpc_table_Domain_descriptor;
    }

    @java.lang.Override
    public alluxio.grpc.table.Domain getDefaultInstanceForType() {
      return alluxio.grpc.table.Domain.getDefaultInstance();
    }

    @java.lang.Override
    public alluxio.grpc.table.Domain build() {
      alluxio.grpc.table.Domain result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public alluxio.grpc.table.Domain buildPartial() {
      alluxio.grpc.table.Domain result = new alluxio.grpc.table.Domain(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (valueSetCase_ == 1) {
        if (rangeBuilder_ == null) {
          result.valueSet_ = valueSet_;
        } else {
          result.valueSet_ = rangeBuilder_.build();
        }
      }
      if (valueSetCase_ == 2) {
        if (equatableBuilder_ == null) {
          result.valueSet_ = valueSet_;
        } else {
          result.valueSet_ = equatableBuilder_.build();
        }
      }
      if (valueSetCase_ == 3) {
        if (allOrNoneBuilder_ == null) {
          result.valueSet_ = valueSet_;
        } else {
          result.valueSet_ = allOrNoneBuilder_.build();
        }
      }
      result.bitField0_ = to_bitField0_;
      result.valueSetCase_ = valueSetCase_;
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
      if (other instanceof alluxio.grpc.table.Domain) {
        return mergeFrom((alluxio.grpc.table.Domain)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(alluxio.grpc.table.Domain other) {
      if (other == alluxio.grpc.table.Domain.getDefaultInstance()) return this;
      switch (other.getValueSetCase()) {
        case RANGE: {
          mergeRange(other.getRange());
          break;
        }
        case EQUATABLE: {
          mergeEquatable(other.getEquatable());
          break;
        }
        case ALL_OR_NONE: {
          mergeAllOrNone(other.getAllOrNone());
          break;
        }
        case VALUESET_NOT_SET: {
          break;
        }
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
      alluxio.grpc.table.Domain parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (alluxio.grpc.table.Domain) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int valueSetCase_ = 0;
    private java.lang.Object valueSet_;
    public ValueSetCase
        getValueSetCase() {
      return ValueSetCase.forNumber(
          valueSetCase_);
    }

    public Builder clearValueSet() {
      valueSetCase_ = 0;
      valueSet_ = null;
      onChanged();
      return this;
    }

    private int bitField0_;

    private com.google.protobuf.SingleFieldBuilderV3<
        alluxio.grpc.table.RangeSet, alluxio.grpc.table.RangeSet.Builder, alluxio.grpc.table.RangeSetOrBuilder> rangeBuilder_;
    /**
     * <code>optional .alluxio.grpc.table.RangeSet range = 1;</code>
     * @return Whether the range field is set.
     */
    public boolean hasRange() {
      return valueSetCase_ == 1;
    }
    /**
     * <code>optional .alluxio.grpc.table.RangeSet range = 1;</code>
     * @return The range.
     */
    public alluxio.grpc.table.RangeSet getRange() {
      if (rangeBuilder_ == null) {
        if (valueSetCase_ == 1) {
          return (alluxio.grpc.table.RangeSet) valueSet_;
        }
        return alluxio.grpc.table.RangeSet.getDefaultInstance();
      } else {
        if (valueSetCase_ == 1) {
          return rangeBuilder_.getMessage();
        }
        return alluxio.grpc.table.RangeSet.getDefaultInstance();
      }
    }
    /**
     * <code>optional .alluxio.grpc.table.RangeSet range = 1;</code>
     */
    public Builder setRange(alluxio.grpc.table.RangeSet value) {
      if (rangeBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        valueSet_ = value;
        onChanged();
      } else {
        rangeBuilder_.setMessage(value);
      }
      valueSetCase_ = 1;
      return this;
    }
    /**
     * <code>optional .alluxio.grpc.table.RangeSet range = 1;</code>
     */
    public Builder setRange(
        alluxio.grpc.table.RangeSet.Builder builderForValue) {
      if (rangeBuilder_ == null) {
        valueSet_ = builderForValue.build();
        onChanged();
      } else {
        rangeBuilder_.setMessage(builderForValue.build());
      }
      valueSetCase_ = 1;
      return this;
    }
    /**
     * <code>optional .alluxio.grpc.table.RangeSet range = 1;</code>
     */
    public Builder mergeRange(alluxio.grpc.table.RangeSet value) {
      if (rangeBuilder_ == null) {
        if (valueSetCase_ == 1 &&
            valueSet_ != alluxio.grpc.table.RangeSet.getDefaultInstance()) {
          valueSet_ = alluxio.grpc.table.RangeSet.newBuilder((alluxio.grpc.table.RangeSet) valueSet_)
              .mergeFrom(value).buildPartial();
        } else {
          valueSet_ = value;
        }
        onChanged();
      } else {
        if (valueSetCase_ == 1) {
          rangeBuilder_.mergeFrom(value);
        }
        rangeBuilder_.setMessage(value);
      }
      valueSetCase_ = 1;
      return this;
    }
    /**
     * <code>optional .alluxio.grpc.table.RangeSet range = 1;</code>
     */
    public Builder clearRange() {
      if (rangeBuilder_ == null) {
        if (valueSetCase_ == 1) {
          valueSetCase_ = 0;
          valueSet_ = null;
          onChanged();
        }
      } else {
        if (valueSetCase_ == 1) {
          valueSetCase_ = 0;
          valueSet_ = null;
        }
        rangeBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>optional .alluxio.grpc.table.RangeSet range = 1;</code>
     */
    public alluxio.grpc.table.RangeSet.Builder getRangeBuilder() {
      return getRangeFieldBuilder().getBuilder();
    }
    /**
     * <code>optional .alluxio.grpc.table.RangeSet range = 1;</code>
     */
    public alluxio.grpc.table.RangeSetOrBuilder getRangeOrBuilder() {
      if ((valueSetCase_ == 1) && (rangeBuilder_ != null)) {
        return rangeBuilder_.getMessageOrBuilder();
      } else {
        if (valueSetCase_ == 1) {
          return (alluxio.grpc.table.RangeSet) valueSet_;
        }
        return alluxio.grpc.table.RangeSet.getDefaultInstance();
      }
    }
    /**
     * <code>optional .alluxio.grpc.table.RangeSet range = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        alluxio.grpc.table.RangeSet, alluxio.grpc.table.RangeSet.Builder, alluxio.grpc.table.RangeSetOrBuilder> 
        getRangeFieldBuilder() {
      if (rangeBuilder_ == null) {
        if (!(valueSetCase_ == 1)) {
          valueSet_ = alluxio.grpc.table.RangeSet.getDefaultInstance();
        }
        rangeBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            alluxio.grpc.table.RangeSet, alluxio.grpc.table.RangeSet.Builder, alluxio.grpc.table.RangeSetOrBuilder>(
                (alluxio.grpc.table.RangeSet) valueSet_,
                getParentForChildren(),
                isClean());
        valueSet_ = null;
      }
      valueSetCase_ = 1;
      onChanged();;
      return rangeBuilder_;
    }

    private com.google.protobuf.SingleFieldBuilderV3<
        alluxio.grpc.table.EquatableValueSet, alluxio.grpc.table.EquatableValueSet.Builder, alluxio.grpc.table.EquatableValueSetOrBuilder> equatableBuilder_;
    /**
     * <code>optional .alluxio.grpc.table.EquatableValueSet equatable = 2;</code>
     * @return Whether the equatable field is set.
     */
    public boolean hasEquatable() {
      return valueSetCase_ == 2;
    }
    /**
     * <code>optional .alluxio.grpc.table.EquatableValueSet equatable = 2;</code>
     * @return The equatable.
     */
    public alluxio.grpc.table.EquatableValueSet getEquatable() {
      if (equatableBuilder_ == null) {
        if (valueSetCase_ == 2) {
          return (alluxio.grpc.table.EquatableValueSet) valueSet_;
        }
        return alluxio.grpc.table.EquatableValueSet.getDefaultInstance();
      } else {
        if (valueSetCase_ == 2) {
          return equatableBuilder_.getMessage();
        }
        return alluxio.grpc.table.EquatableValueSet.getDefaultInstance();
      }
    }
    /**
     * <code>optional .alluxio.grpc.table.EquatableValueSet equatable = 2;</code>
     */
    public Builder setEquatable(alluxio.grpc.table.EquatableValueSet value) {
      if (equatableBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        valueSet_ = value;
        onChanged();
      } else {
        equatableBuilder_.setMessage(value);
      }
      valueSetCase_ = 2;
      return this;
    }
    /**
     * <code>optional .alluxio.grpc.table.EquatableValueSet equatable = 2;</code>
     */
    public Builder setEquatable(
        alluxio.grpc.table.EquatableValueSet.Builder builderForValue) {
      if (equatableBuilder_ == null) {
        valueSet_ = builderForValue.build();
        onChanged();
      } else {
        equatableBuilder_.setMessage(builderForValue.build());
      }
      valueSetCase_ = 2;
      return this;
    }
    /**
     * <code>optional .alluxio.grpc.table.EquatableValueSet equatable = 2;</code>
     */
    public Builder mergeEquatable(alluxio.grpc.table.EquatableValueSet value) {
      if (equatableBuilder_ == null) {
        if (valueSetCase_ == 2 &&
            valueSet_ != alluxio.grpc.table.EquatableValueSet.getDefaultInstance()) {
          valueSet_ = alluxio.grpc.table.EquatableValueSet.newBuilder((alluxio.grpc.table.EquatableValueSet) valueSet_)
              .mergeFrom(value).buildPartial();
        } else {
          valueSet_ = value;
        }
        onChanged();
      } else {
        if (valueSetCase_ == 2) {
          equatableBuilder_.mergeFrom(value);
        }
        equatableBuilder_.setMessage(value);
      }
      valueSetCase_ = 2;
      return this;
    }
    /**
     * <code>optional .alluxio.grpc.table.EquatableValueSet equatable = 2;</code>
     */
    public Builder clearEquatable() {
      if (equatableBuilder_ == null) {
        if (valueSetCase_ == 2) {
          valueSetCase_ = 0;
          valueSet_ = null;
          onChanged();
        }
      } else {
        if (valueSetCase_ == 2) {
          valueSetCase_ = 0;
          valueSet_ = null;
        }
        equatableBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>optional .alluxio.grpc.table.EquatableValueSet equatable = 2;</code>
     */
    public alluxio.grpc.table.EquatableValueSet.Builder getEquatableBuilder() {
      return getEquatableFieldBuilder().getBuilder();
    }
    /**
     * <code>optional .alluxio.grpc.table.EquatableValueSet equatable = 2;</code>
     */
    public alluxio.grpc.table.EquatableValueSetOrBuilder getEquatableOrBuilder() {
      if ((valueSetCase_ == 2) && (equatableBuilder_ != null)) {
        return equatableBuilder_.getMessageOrBuilder();
      } else {
        if (valueSetCase_ == 2) {
          return (alluxio.grpc.table.EquatableValueSet) valueSet_;
        }
        return alluxio.grpc.table.EquatableValueSet.getDefaultInstance();
      }
    }
    /**
     * <code>optional .alluxio.grpc.table.EquatableValueSet equatable = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        alluxio.grpc.table.EquatableValueSet, alluxio.grpc.table.EquatableValueSet.Builder, alluxio.grpc.table.EquatableValueSetOrBuilder> 
        getEquatableFieldBuilder() {
      if (equatableBuilder_ == null) {
        if (!(valueSetCase_ == 2)) {
          valueSet_ = alluxio.grpc.table.EquatableValueSet.getDefaultInstance();
        }
        equatableBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            alluxio.grpc.table.EquatableValueSet, alluxio.grpc.table.EquatableValueSet.Builder, alluxio.grpc.table.EquatableValueSetOrBuilder>(
                (alluxio.grpc.table.EquatableValueSet) valueSet_,
                getParentForChildren(),
                isClean());
        valueSet_ = null;
      }
      valueSetCase_ = 2;
      onChanged();;
      return equatableBuilder_;
    }

    private com.google.protobuf.SingleFieldBuilderV3<
        alluxio.grpc.table.AllOrNoneSet, alluxio.grpc.table.AllOrNoneSet.Builder, alluxio.grpc.table.AllOrNoneSetOrBuilder> allOrNoneBuilder_;
    /**
     * <code>optional .alluxio.grpc.table.AllOrNoneSet all_or_none = 3;</code>
     * @return Whether the allOrNone field is set.
     */
    public boolean hasAllOrNone() {
      return valueSetCase_ == 3;
    }
    /**
     * <code>optional .alluxio.grpc.table.AllOrNoneSet all_or_none = 3;</code>
     * @return The allOrNone.
     */
    public alluxio.grpc.table.AllOrNoneSet getAllOrNone() {
      if (allOrNoneBuilder_ == null) {
        if (valueSetCase_ == 3) {
          return (alluxio.grpc.table.AllOrNoneSet) valueSet_;
        }
        return alluxio.grpc.table.AllOrNoneSet.getDefaultInstance();
      } else {
        if (valueSetCase_ == 3) {
          return allOrNoneBuilder_.getMessage();
        }
        return alluxio.grpc.table.AllOrNoneSet.getDefaultInstance();
      }
    }
    /**
     * <code>optional .alluxio.grpc.table.AllOrNoneSet all_or_none = 3;</code>
     */
    public Builder setAllOrNone(alluxio.grpc.table.AllOrNoneSet value) {
      if (allOrNoneBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        valueSet_ = value;
        onChanged();
      } else {
        allOrNoneBuilder_.setMessage(value);
      }
      valueSetCase_ = 3;
      return this;
    }
    /**
     * <code>optional .alluxio.grpc.table.AllOrNoneSet all_or_none = 3;</code>
     */
    public Builder setAllOrNone(
        alluxio.grpc.table.AllOrNoneSet.Builder builderForValue) {
      if (allOrNoneBuilder_ == null) {
        valueSet_ = builderForValue.build();
        onChanged();
      } else {
        allOrNoneBuilder_.setMessage(builderForValue.build());
      }
      valueSetCase_ = 3;
      return this;
    }
    /**
     * <code>optional .alluxio.grpc.table.AllOrNoneSet all_or_none = 3;</code>
     */
    public Builder mergeAllOrNone(alluxio.grpc.table.AllOrNoneSet value) {
      if (allOrNoneBuilder_ == null) {
        if (valueSetCase_ == 3 &&
            valueSet_ != alluxio.grpc.table.AllOrNoneSet.getDefaultInstance()) {
          valueSet_ = alluxio.grpc.table.AllOrNoneSet.newBuilder((alluxio.grpc.table.AllOrNoneSet) valueSet_)
              .mergeFrom(value).buildPartial();
        } else {
          valueSet_ = value;
        }
        onChanged();
      } else {
        if (valueSetCase_ == 3) {
          allOrNoneBuilder_.mergeFrom(value);
        }
        allOrNoneBuilder_.setMessage(value);
      }
      valueSetCase_ = 3;
      return this;
    }
    /**
     * <code>optional .alluxio.grpc.table.AllOrNoneSet all_or_none = 3;</code>
     */
    public Builder clearAllOrNone() {
      if (allOrNoneBuilder_ == null) {
        if (valueSetCase_ == 3) {
          valueSetCase_ = 0;
          valueSet_ = null;
          onChanged();
        }
      } else {
        if (valueSetCase_ == 3) {
          valueSetCase_ = 0;
          valueSet_ = null;
        }
        allOrNoneBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>optional .alluxio.grpc.table.AllOrNoneSet all_or_none = 3;</code>
     */
    public alluxio.grpc.table.AllOrNoneSet.Builder getAllOrNoneBuilder() {
      return getAllOrNoneFieldBuilder().getBuilder();
    }
    /**
     * <code>optional .alluxio.grpc.table.AllOrNoneSet all_or_none = 3;</code>
     */
    public alluxio.grpc.table.AllOrNoneSetOrBuilder getAllOrNoneOrBuilder() {
      if ((valueSetCase_ == 3) && (allOrNoneBuilder_ != null)) {
        return allOrNoneBuilder_.getMessageOrBuilder();
      } else {
        if (valueSetCase_ == 3) {
          return (alluxio.grpc.table.AllOrNoneSet) valueSet_;
        }
        return alluxio.grpc.table.AllOrNoneSet.getDefaultInstance();
      }
    }
    /**
     * <code>optional .alluxio.grpc.table.AllOrNoneSet all_or_none = 3;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        alluxio.grpc.table.AllOrNoneSet, alluxio.grpc.table.AllOrNoneSet.Builder, alluxio.grpc.table.AllOrNoneSetOrBuilder> 
        getAllOrNoneFieldBuilder() {
      if (allOrNoneBuilder_ == null) {
        if (!(valueSetCase_ == 3)) {
          valueSet_ = alluxio.grpc.table.AllOrNoneSet.getDefaultInstance();
        }
        allOrNoneBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            alluxio.grpc.table.AllOrNoneSet, alluxio.grpc.table.AllOrNoneSet.Builder, alluxio.grpc.table.AllOrNoneSetOrBuilder>(
                (alluxio.grpc.table.AllOrNoneSet) valueSet_,
                getParentForChildren(),
                isClean());
        valueSet_ = null;
      }
      valueSetCase_ = 3;
      onChanged();;
      return allOrNoneBuilder_;
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


    // @@protoc_insertion_point(builder_scope:alluxio.grpc.table.Domain)
  }

  // @@protoc_insertion_point(class_scope:alluxio.grpc.table.Domain)
  private static final alluxio.grpc.table.Domain DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new alluxio.grpc.table.Domain();
  }

  public static alluxio.grpc.table.Domain getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  @java.lang.Deprecated public static final com.google.protobuf.Parser<Domain>
      PARSER = new com.google.protobuf.AbstractParser<Domain>() {
    @java.lang.Override
    public Domain parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new Domain(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<Domain> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Domain> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public alluxio.grpc.table.Domain getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

