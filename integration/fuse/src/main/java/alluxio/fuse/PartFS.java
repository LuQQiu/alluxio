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

package alluxio.fuse;

import alluxio.jnifuse.AbstractFuseFileSystem;
import alluxio.jnifuse.ErrorCodes;
import alluxio.jnifuse.FuseFillDir;
import alluxio.jnifuse.struct.FileStat;
import alluxio.jnifuse.struct.FuseFileInfo;
import alluxio.util.io.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.Optional;
import java.util.Set;

/**
 * Stack FS implements the FUSE callbacks defined by jni-fuse
 * without interactions with Alluxio clients/servers.
 * <p>
 * Stack FS mounts a local filesystem path to another local filesystem path.
 * All the operations target the Stack FS mount point will be directly
 * trigger on the local filesystem mounted path without complex added logics.
 * </p>
 * <p>
 * This class is mainly added for testing purposes to understand the
 * performance overhead introduced by jni-fuse and provides an upper-bound
 * performance data for Alluxio jni-fuse implementations.
 * </p>
 */
public class PartFS extends AbstractFuseFileSystem {
  private static final Logger LOG = LoggerFactory.getLogger(StackFS.class);
  private static final long ID_NOT_SET_VALUE = -1;
  private static final long ID_NOT_SET_VALUE_UNSIGNED = 4294967295L;

  private final Path mRoot;

  /**
   * @param root root
   * @param mountPoint mount point
   */
  public PartFS(Path root, Path mountPoint) {
    super(mountPoint);
    mRoot = root;
  }

  private String transformPath(String path) {
    return mRoot + path;
  }

  private int getMode(Path path) throws IOException {
    Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(path);
    int mode = FileUtils.translatePosixPermissionToMode(permissions);
    if (Files.isDirectory(path)) {
      mode |= FileStat.S_IFDIR;
    } else {
      mode |= FileStat.S_IFREG;
    }
    return mode;
  }

  @Override
  public int getattr(String path, FileStat stat) {
    return AlluxioFuseUtils.call(
        LOG, () -> getattrInternal(path, stat), "Stackfs.Getattr", "path=%s", path);
  }

  private int getattrInternal(String path, FileStat stat) {
    stat.st_size.set(0);
    stat.st_blksize.set(0);

    stat.st_ctim.tv_sec.set(-1);
    stat.st_ctim.tv_nsec.set(-1);
    stat.st_mtim.tv_sec.set(-1);
    stat.st_mtim.tv_nsec.set(-1);

    stat.st_uid.set(1100);
    stat.st_gid.set(1100);

    stat.st_mode.set(511);
    return 0;
  }

  @Override
  public int readdir(String path, long buff, long filter, long offset,
      FuseFileInfo fi) {
    return AlluxioFuseUtils.call(LOG, () -> readdirInternal(path, buff, filter, offset, fi),
        "Stackfs.Readdir", "path=%s,buf=%s", path, buff);
  }

  private int readdirInternal(String path, long buff, long filter, long offset,
      FuseFileInfo fi) {
    path = transformPath(path);
    File dir = new File(path);
    FuseFillDir.apply(filter, buff, ".", null, 0);
    FuseFillDir.apply(filter, buff, "..", null, 0);
    File[] subfiles = dir.listFiles();
    if (subfiles != null) {
      for (File subfile : subfiles) {
        FuseFillDir.apply(filter, buff, subfile.getName(), null, 0);
      }
    }
    return 0;
  }

  @Override
  public int open(String path, FuseFileInfo fi) {
    return AlluxioFuseUtils.call(LOG, () -> openInternal(path, fi),
        "Stackfs.Open", "path=%s", path);
  }

  private int openInternal(String path, FuseFileInfo fi) {
    return 0;
  }

  @Override
  public int read(String path, ByteBuffer buf, long size, long offset, FuseFileInfo fi) {
    return AlluxioFuseUtils.call(LOG, () -> readInternal(path, buf, size, offset, fi),
        "Stackfs.Read", "path=%s,buf=%s,size=%d,offset=%d", path, buf, size, offset);
  }

  private int readInternal(String path, ByteBuffer buf, long size, long offset, FuseFileInfo fi) {
    return 0;
  }

  @Override
  public int create(String path, long mode, FuseFileInfo fi) {
    return AlluxioFuseUtils.call(LOG, () -> createInternal(path, mode, fi),
        "Stackfs.Create", "path=%s,mode=%o", path, mode);
  }

  private int createInternal(String path, long mode, FuseFileInfo fi) {
    return 0;
  }

  @Override
  public int write(String path, ByteBuffer buf, long size, long offset, FuseFileInfo fi) {
    return AlluxioFuseUtils.call(LOG, () -> writeInternal(path, buf, size, offset, fi),
        "Stackfs.Write", "path=%s,buf=%s,size=%d,offset=%d", path, buf, size, offset);
  }

  private int writeInternal(String path, ByteBuffer buf, long size, long offset, FuseFileInfo fi) {
    return 0;
  }

  @Override
  public int mkdir(String path, long mode) {
    return AlluxioFuseUtils.call(LOG, () -> mkdirInternal(path, mode),
        "Stackfs.Mkdir", "path=%s,mode=%o,", path, mode);
  }

  private int mkdirInternal(String path, long mode) {
    path = transformPath(path);
    Path dirPath = Paths.get(path);
    if (Files.exists(dirPath)) {
      LOG.error("Dir {} already exist", path);
      return -ErrorCodes.EEXIST();
    }
    try {
      Files.createDirectory(dirPath);
      return 0;
    } catch (IOException e) {
      LOG.error("Failed to mkdir {}", path, e);
      return -ErrorCodes.EIO();
    }
  }

  @Override
  public int unlink(String path) {
    return AlluxioFuseUtils.call(LOG, () -> unlinkInternal(path),
        "Stackfs.Unlink", "path=%s", path);
  }

  private int unlinkInternal(String path) {
    return 0;
  }

  @Override
  public int rename(String oldPath, String newPath, int flags) {
    return AlluxioFuseUtils.call(LOG, () -> renameInternal(oldPath, newPath),
        "Stackfs.Rename", "oldPath=%s,newPath=%s,", oldPath, newPath);
  }

  private int renameInternal(String oldPath, String newPath) {
    oldPath = transformPath(oldPath);
    newPath = transformPath(newPath);
    Path oldFilePath = Paths.get(oldPath);
    Path newFilePath = Paths.get(newPath);
    if (!Files.exists(oldFilePath)) {
      LOG.error("Old path {} does not exist", oldPath);
      return -ErrorCodes.ENOENT();
    }
    if (Files.exists(newFilePath)) {
      LOG.error("New path {} does not exist", newPath);
      return -ErrorCodes.ENOENT();
    }
    try {
      Files.move(oldFilePath, newFilePath);
      return 0;
    } catch (IOException e) {
      LOG.error("Failed to move {} to {}", oldFilePath, newFilePath, e);
      return -ErrorCodes.EIO();
    }
  }

  @Override
  public int chmod(String path, long mode) {
    return AlluxioFuseUtils.call(LOG, () -> chmodInternal(path, mode),
        "Stackfs.Chmod", "path=%s,mode=%o", path, mode);
  }

  private int chmodInternal(String path, long mode) {
    path = transformPath(path);
    Path filePath = Paths.get(path);
    if (!Files.exists(filePath)) {
      return -ErrorCodes.ENOENT();
    }
    try {
      Files.setPosixFilePermissions(filePath,
          FileUtils.translateModeToPosixPermissions((int) mode));
      return 0;
    } catch (IOException e) {
      LOG.error("Failed to chmod {}", path, e);
      return -ErrorCodes.EIO();
    }
  }

  @Override
  public int chown(String path, long uid, long gid) {
    return AlluxioFuseUtils.call(LOG, () -> chownInternal(path, uid, gid),
        "Stackfs.Chown", "path=%s,uid=%d,gid=%d", path, uid, gid);
  }

  private int chownInternal(String path, long uid, long gid) {
    path = transformPath(path);
    Path filePath = Paths.get(path);
    if (!Files.exists(filePath)) {
      return -ErrorCodes.ENOENT();
    }
    try {
      UserPrincipalLookupService lookupService =
          FileSystems.getDefault().getUserPrincipalLookupService();
      PosixFileAttributeView view = Files.getFileAttributeView(filePath,
          PosixFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
      Optional<String> userName = AlluxioFuseUtils.getUserName(uid);
      if (userName.isPresent()) {
        view.setOwner(lookupService.lookupPrincipalByName(userName.get()));
      }
      Optional<String> groupName = AlluxioFuseUtils.getGroupName(gid);
      if (groupName.isPresent()) {
        view.setGroup(lookupService.lookupPrincipalByGroupName(groupName.get()));
      }
      return 0;
    } catch (IOException e) {
      LOG.error("Failed to chown {}", path, e);
      return -ErrorCodes.EIO();
    }
  }

  @Override
  public int flush(String path, FuseFileInfo fi) {
    return 0;
  }

  @Override
  public int release(String path, FuseFileInfo fi) {
    return 0;
  }

  @Override
  public String getFileSystemName() {
    return "jnifuse-stackfs";
  }
}
