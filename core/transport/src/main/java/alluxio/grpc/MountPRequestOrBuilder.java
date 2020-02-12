// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: grpc/file_system_master.proto

package alluxio.grpc;

public interface MountPRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:alluxio.grpc.file.MountPRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   ** the path of alluxio mount point 
   * </pre>
   *
   * <code>optional string alluxioPath = 1;</code>
   * @return Whether the alluxioPath field is set.
   */
  boolean hasAlluxioPath();
  /**
   * <pre>
   ** the path of alluxio mount point 
   * </pre>
   *
   * <code>optional string alluxioPath = 1;</code>
   * @return The alluxioPath.
   */
  java.lang.String getAlluxioPath();
  /**
   * <pre>
   ** the path of alluxio mount point 
   * </pre>
   *
   * <code>optional string alluxioPath = 1;</code>
   * @return The bytes for alluxioPath.
   */
  com.google.protobuf.ByteString
      getAlluxioPathBytes();

  /**
   * <pre>
   ** the path of the under file system 
   * </pre>
   *
   * <code>optional string ufsPath = 2;</code>
   * @return Whether the ufsPath field is set.
   */
  boolean hasUfsPath();
  /**
   * <pre>
   ** the path of the under file system 
   * </pre>
   *
   * <code>optional string ufsPath = 2;</code>
   * @return The ufsPath.
   */
  java.lang.String getUfsPath();
  /**
   * <pre>
   ** the path of the under file system 
   * </pre>
   *
   * <code>optional string ufsPath = 2;</code>
   * @return The bytes for ufsPath.
   */
  com.google.protobuf.ByteString
      getUfsPathBytes();

  /**
   * <code>optional .alluxio.grpc.file.MountPOptions options = 3;</code>
   * @return Whether the options field is set.
   */
  boolean hasOptions();
  /**
   * <code>optional .alluxio.grpc.file.MountPOptions options = 3;</code>
   * @return The options.
   */
  alluxio.grpc.MountPOptions getOptions();
  /**
   * <code>optional .alluxio.grpc.file.MountPOptions options = 3;</code>
   */
  alluxio.grpc.MountPOptionsOrBuilder getOptionsOrBuilder();
}
