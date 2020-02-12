// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: grpc/file_system_master.proto

package alluxio.grpc;

public interface UnmountPRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:alluxio.grpc.file.UnmountPRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   ** the path of the alluxio mount point 
   * </pre>
   *
   * <code>optional string alluxioPath = 1;</code>
   * @return Whether the alluxioPath field is set.
   */
  boolean hasAlluxioPath();
  /**
   * <pre>
   ** the path of the alluxio mount point 
   * </pre>
   *
   * <code>optional string alluxioPath = 1;</code>
   * @return The alluxioPath.
   */
  java.lang.String getAlluxioPath();
  /**
   * <pre>
   ** the path of the alluxio mount point 
   * </pre>
   *
   * <code>optional string alluxioPath = 1;</code>
   * @return The bytes for alluxioPath.
   */
  com.google.protobuf.ByteString
      getAlluxioPathBytes();

  /**
   * <code>optional .alluxio.grpc.file.UnmountPOptions options = 2;</code>
   * @return Whether the options field is set.
   */
  boolean hasOptions();
  /**
   * <code>optional .alluxio.grpc.file.UnmountPOptions options = 2;</code>
   * @return The options.
   */
  alluxio.grpc.UnmountPOptions getOptions();
  /**
   * <code>optional .alluxio.grpc.file.UnmountPOptions options = 2;</code>
   */
  alluxio.grpc.UnmountPOptionsOrBuilder getOptionsOrBuilder();
}
