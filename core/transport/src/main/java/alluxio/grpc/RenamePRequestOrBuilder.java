// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: grpc/file_system_master.proto

package alluxio.grpc;

public interface RenamePRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:alluxio.grpc.file.RenamePRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   ** the source path of the file or directory 
   * </pre>
   *
   * <code>optional string path = 1;</code>
   * @return Whether the path field is set.
   */
  boolean hasPath();
  /**
   * <pre>
   ** the source path of the file or directory 
   * </pre>
   *
   * <code>optional string path = 1;</code>
   * @return The path.
   */
  java.lang.String getPath();
  /**
   * <pre>
   ** the source path of the file or directory 
   * </pre>
   *
   * <code>optional string path = 1;</code>
   * @return The bytes for path.
   */
  com.google.protobuf.ByteString
      getPathBytes();

  /**
   * <pre>
   ** the destination path 
   * </pre>
   *
   * <code>optional string dstPath = 2;</code>
   * @return Whether the dstPath field is set.
   */
  boolean hasDstPath();
  /**
   * <pre>
   ** the destination path 
   * </pre>
   *
   * <code>optional string dstPath = 2;</code>
   * @return The dstPath.
   */
  java.lang.String getDstPath();
  /**
   * <pre>
   ** the destination path 
   * </pre>
   *
   * <code>optional string dstPath = 2;</code>
   * @return The bytes for dstPath.
   */
  com.google.protobuf.ByteString
      getDstPathBytes();

  /**
   * <code>optional .alluxio.grpc.file.RenamePOptions options = 3;</code>
   * @return Whether the options field is set.
   */
  boolean hasOptions();
  /**
   * <code>optional .alluxio.grpc.file.RenamePOptions options = 3;</code>
   * @return The options.
   */
  alluxio.grpc.RenamePOptions getOptions();
  /**
   * <code>optional .alluxio.grpc.file.RenamePOptions options = 3;</code>
   */
  alluxio.grpc.RenamePOptionsOrBuilder getOptionsOrBuilder();
}
