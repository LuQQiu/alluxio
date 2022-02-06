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

package alluxio.client.fuse;

import alluxio.client.file.FileSystem;
import alluxio.conf.PropertyKey;
import alluxio.conf.ServerConfiguration;
import alluxio.fuse.AlluxioJniFuseFileSystem;
import alluxio.fuse.FuseMountOptions;
import alluxio.jnifuse.struct.FuseFileInfo;
import alluxio.shell.CommandReturn;
import alluxio.util.ShellUtils;
import alluxio.util.io.BufferUtils;

import jnr.constants.platform.OpenFlags;
import org.junit.Assert;
import org.junit.Test;

import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Integration tests for JNR-FUSE based {@link AlluxioJniFuseFileSystem}.
 */
public class JNIFuseIntegrationTest extends AbstractFuseIntegrationTest {
  private AlluxioJniFuseFileSystem mFuseFileSystem;

  @Override
  public void configure() {
    ServerConfiguration.set(PropertyKey.FUSE_JNIFUSE_ENABLED, true);
  }

  @Override
  public void mountFuse(FileSystem fileSystem, String mountPoint, String alluxioRoot) {
    FuseMountOptions options =
        new FuseMountOptions(mountPoint, alluxioRoot, false, new ArrayList<>());
    mFuseFileSystem =
        new AlluxioJniFuseFileSystem(fileSystem, options, ServerConfiguration.global());
    mFuseFileSystem.mount(false, false, new String[] {});
  }

  @Override
  public void umountFuse(String mountPath) throws Exception {
    mFuseFileSystem.umount(true);
  }

  /**
   * Tests creating a file for writing
   * and opening a file for O_RDONLY read-only open flag.
   */
  @Test
  public void createWriteOpenRead() throws Exception {
    String testFile = "/createWriteOpenReadTestFile";
    int fileLen = 128;
    try (CloseableFuseFileInfo info = new CloseableFuseFileInfo()) {
      FuseFileInfo fuseFileInfo = info.getFuseFileInfo();
      fuseFileInfo.flags.set(OpenFlags.O_RDONLY.intValue());
      // cannot open non-existing file for read
      Assert.assertNotEquals(0, mFuseFileSystem.open(testFile, fuseFileInfo));
      // open existing file for read
      createTestFile(testFile, fuseFileInfo, fileLen);
      readAndValidateTestFile(testFile, fuseFileInfo, fileLen);
    }
  }

  /**
   * Tests opening a file for O_WRONLY write-only open flag.
   */
  @Test
  public void openWrite() throws Exception {
    String testFile = "/openWriteTestFile";
    int fileLen = 128;
    try (CloseableFuseFileInfo closeableFuseFileInfo = new CloseableFuseFileInfo()) {
      FuseFileInfo info = closeableFuseFileInfo.getFuseFileInfo();
      info.flags.set(OpenFlags.O_WRONLY.intValue());

      // O_WRONLY non-existing file will write to file
      Assert.assertEquals(0, mFuseFileSystem.open(testFile, info));
      ByteBuffer buffer = BufferUtils.getIncreasingByteBuffer(fileLen);
      try {
        Assert.assertEquals(fileLen, mFuseFileSystem.write(testFile, buffer, fileLen, 0, info));
      } finally {
        Assert.assertEquals(0, mFuseFileSystem.release(testFile, info));
      }
      readAndValidateTestFile(testFile, info, fileLen);

      // O_WRONLY existing file without O_TRUNC or fuse.truncate(size=0) will error out
      info.flags.set(OpenFlags.O_WRONLY.intValue());
      try {
        Assert.assertEquals(0, mFuseFileSystem.open(testFile, info));
        Assert.assertTrue(mFuseFileSystem.write(testFile, buffer, fileLen, 0, info) < 0);
      } finally {
        mFuseFileSystem.release(testFile, info);
      }

      // O_WRONLY with O_TRUNC will successfully overwrite file
      info.flags.set(OpenFlags.O_WRONLY.intValue() | OpenFlags.O_TRUNC.intValue());
      fileLen = 256; // create a bigger file
      Assert.assertEquals(0, mFuseFileSystem.open(testFile, info));
      buffer = BufferUtils.getIncreasingByteBuffer(fileLen);
      try {
        Assert.assertEquals(fileLen, mFuseFileSystem.write(testFile, buffer, fileLen, 0, info));
      } finally {
        Assert.assertEquals(0, mFuseFileSystem.release(testFile, info));
      }
      readAndValidateTestFile(testFile, info, fileLen);

      // O_WRONLY with truncate(size=0) will successfully overwrite file
      info.flags.set(OpenFlags.O_WRONLY.intValue());
      fileLen = 512; // create a bigger file
      Assert.assertEquals(0, mFuseFileSystem.open(testFile, info));
      buffer = BufferUtils.getIncreasingByteBuffer(fileLen);
      try {
        Assert.assertEquals(0, mFuseFileSystem.truncate(testFile, 0));
        Assert.assertEquals(fileLen, mFuseFileSystem.write(testFile, buffer, fileLen, 0, info));
      } finally {
        Assert.assertEquals(0, mFuseFileSystem.release(testFile, info));
      }
      readAndValidateTestFile(testFile, info, fileLen);
    }
  }

  /**
   * Tests opening a file for O_RDWR reading and writing open flag.
   */
  @Test
  public void openReadWrite() throws Exception {
    String testFile = "/openReadWriteTestFile";
    int fileLen = 128;
    // if file not exist, open read write will perform write-only, any read will error out
    try (CloseableFuseFileInfo closeableFuseFileInfo = new CloseableFuseFileInfo()) {
      FuseFileInfo info = closeableFuseFileInfo.getFuseFileInfo();
      info.flags.set(OpenFlags.O_RDWR.intValue());

      // O_RDWR with non-existing file -> write-only, read will error out
      Assert.assertEquals(0, mFuseFileSystem.open(testFile, info));
      ByteBuffer buffer = BufferUtils.getIncreasingByteBuffer(fileLen);
      try {
        Assert.assertEquals(fileLen, mFuseFileSystem.write(testFile, buffer, fileLen, 0, info));
        buffer.clear();
        Assert.assertTrue(mFuseFileSystem.read(testFile, buffer, fileLen, 0, info) < 0);
      } finally {
        Assert.assertEquals(0, mFuseFileSystem.release(testFile, info));
      }

      // O_RDWR with existing file -> read-only, write will error out
      info.flags.set(OpenFlags.O_RDWR.intValue());
      Assert.assertEquals(0, mFuseFileSystem.open(testFile, info));
      ByteBuffer directBuffer = ByteBuffer.allocateDirect(fileLen);
      try {
        Assert.assertEquals(fileLen,
            mFuseFileSystem.read(testFile, directBuffer, fileLen, 0, info));
        directBuffer.rewind();
        byte[] array = new byte[fileLen];
        directBuffer.get(array, 0, fileLen);
        Assert.assertTrue(BufferUtils.equalIncreasingByteArray(fileLen, array));
        directBuffer.clear();
        Assert.assertTrue(mFuseFileSystem.write(testFile, buffer, fileLen, 0, info) < 0);
      } finally {
        BufferUtils.cleanDirectBuffer(directBuffer);
        Assert.assertEquals(0, mFuseFileSystem.release(testFile, info));
      }

      // O_RDWR with existing file & O_TRUNC -> overwrite and write-only, read will error out
      info.flags.set(OpenFlags.O_RDWR.intValue() | OpenFlags.O_TRUNC.intValue());
      fileLen = 256; // create a bigger file
      Assert.assertEquals(0, mFuseFileSystem.open(testFile, info));
      buffer = BufferUtils.getIncreasingByteBuffer(fileLen);
      try {
        Assert.assertEquals(fileLen, mFuseFileSystem.write(testFile, buffer, fileLen, 0, info));
        buffer.clear();
        Assert.assertTrue(mFuseFileSystem.read(testFile, buffer, fileLen, 0, info) < 0);
      } finally {
        Assert.assertEquals(0, mFuseFileSystem.release(testFile, info));
      }
      readAndValidateTestFile(testFile, info, fileLen);

      // O_RDWR with existing file -> treat as read-only first
      // fuse.truncate(size=0) -> become write-only
      info.flags.set(OpenFlags.O_RDWR.intValue());
      int newFileLen = 512; // create a bigger file
      Assert.assertEquals(0, mFuseFileSystem.open(testFile, info));
      buffer = BufferUtils.getIncreasingByteBuffer(newFileLen);
      try {
        Assert.assertEquals(fileLen, mFuseFileSystem.read(testFile, buffer, fileLen, 0, info));
        Assert.assertEquals(0, mFuseFileSystem.truncate(testFile, 0));
        buffer.clear();
        Assert.assertEquals(newFileLen,
            mFuseFileSystem.write(testFile, buffer, newFileLen, 0, info));
        buffer.clear();
        Assert.assertTrue(mFuseFileSystem.read(testFile, buffer, newFileLen, 0, info) < 0);
      } finally {
        Assert.assertEquals(0, mFuseFileSystem.release(testFile, info));
      }
      readAndValidateTestFile(testFile, info, newFileLen);
    }
  }

  @Test
  public void overwrite() throws Exception {
    String testFile = mMountPoint + "/overwriteTestFile";
    String content = "old content";
    int length = content.length();
    try (FileWriter writer = new FileWriter(testFile)) {
      writer.write(content);
    }
    File file = new File(testFile);
    Assert.assertTrue(file.exists());
    try (FileReader reader = new FileReader(testFile)) {
      char[] res = new char[length];
      Assert.assertEquals(length, reader.read(res));
      assertEquals(content, new String(res));
    }
    content = "new content";
    length = content.length();
    try (FileWriter writer = new FileWriter(testFile)) {
      writer.write(content);
    }
    file = new File(testFile);
    Assert.assertTrue(file.exists());
    try (FileReader reader = new FileReader(testFile)) {
      char[] res = new char[length];
      Assert.assertEquals(length, reader.read(res));
      assertEquals(content, new String(res));
    }
  }

  private void createTestFile(String testFile, FuseFileInfo info, int fileLen) {
    info.flags.set(OpenFlags.O_WRONLY.intValue());
    Assert.assertEquals(0, mFuseFileSystem.create(testFile, 777, info));
    ByteBuffer buffer = BufferUtils.getIncreasingByteBuffer(fileLen);
    try {
      Assert.assertEquals(fileLen, mFuseFileSystem.write(testFile, buffer, fileLen, 0, info));
    } finally {
      Assert.assertEquals(0, mFuseFileSystem.release(testFile, info));
    }
  }

  private void readAndValidateTestFile(String testFile, FuseFileInfo info, int fileLen) {
    info.flags.set(OpenFlags.O_RDONLY.intValue());
    Assert.assertEquals(0, mFuseFileSystem.open(testFile, info));
    ByteBuffer buffer = ByteBuffer.allocateDirect(fileLen);
    try {
      Assert.assertEquals(fileLen, mFuseFileSystem.read(testFile, buffer, fileLen, 0, info));
      buffer.rewind();
      byte[] array = new byte[fileLen];
      buffer.get(array, 0, fileLen);
      Assert.assertTrue(BufferUtils.equalIncreasingByteArray(fileLen, array));
    } finally {
      BufferUtils.cleanDirectBuffer(buffer);
      Assert.assertEquals(0, mFuseFileSystem.release(testFile, info));
    }
  }

  static class CloseableFuseFileInfo implements Closeable {
    private final FuseFileInfo mInfo;
    private final ByteBuffer mBuffer;

    public CloseableFuseFileInfo() {
      mBuffer = ByteBuffer.allocateDirect(36);
      mBuffer.clear();
      mInfo =  FuseFileInfo.of(mBuffer);
    }

    public FuseFileInfo getFuseFileInfo() {
      return mInfo;
    }

    @Override
    public void close() throws IOException {
      BufferUtils.cleanDirectBuffer(mBuffer);
    }
  }
}
