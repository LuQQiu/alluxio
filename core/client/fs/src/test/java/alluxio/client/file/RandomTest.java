package alluxio.client.file;

import alluxio.conf.Configuration;
import alluxio.conf.PropertyKey;
import alluxio.util.CommonUtils;
import alluxio.util.io.PathUtils;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.UUID;

public class RandomTest {
  File mLocalCacheFile = new File(PathUtils.concatPath(
      CommonUtils.getTmpDir(Configuration.getList(PropertyKey.TMP_DIRS)), UUID.randomUUID()));
  
  @Test
  public void readWriteTest() throws IOException, InterruptedException {
    OutputStream mLocalOutputStream = new BufferedOutputStream(new FileOutputStream(mLocalCacheFile));
    RandomAccessFile mLocalCacheFileReader = new RandomAccessFile(mLocalCacheFile, "r");
    Thread.sleep(10);
  }
}
