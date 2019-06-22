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

package alluxio.underfs.gcs;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;

public class GCSRandomTest {
  String mBucketName = "lutestbucket";
  String mFileName = "1GB.zip/";
  String mLocalFile = "/Users/alluxio/alluxioFolder/alluxio/ddFile";

  @Test
  public void test() throws IOException {
    GoogleCredentials credentials = GoogleCredentials
        .fromStream(new FileInputStream("/Users/alluxio/downloads/lutestproject-894ee0d470c2.json"))
        .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));

    Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    Bucket bucket = storage.get(mBucketName);
    mFileName = mFileName.replaceAll("/$", "");
    System.out.println(mFileName);
    BlobId info = BlobId.of(mBucketName, mFileName);
    Blob blob = storage.get(info);
    if (blob == null) {
      System.out.println("Cannot get key");
    }
  }

  @Test
  public void test2() throws IOException {
    GoogleCredentials credentials = GoogleCredentials
        .fromStream(new FileInputStream("/Users/alluxio/downloads/lutestproject-894ee0d470c2.json"))
        .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));

    Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    String uploadFileName = "myNewFile2";
    BlobId id = BlobId.of (mBucketName, uploadFileName);
    BlobInfo info  = BlobInfo.newBuilder(id).build();
    File newFile = new File(mLocalFile);
    storage.create(info, Files.readAllBytes(newFile.toPath()));
    ReadChannel channel = storage.reader(id);
    ByteBuffer byteBuffer = ByteBuffer.allocate(10);
    int read = -1;
    while (read == -1) {
      read = channel.read(byteBuffer);
      System.out.println(read);
    }
  }

  @Test
  public void test3() throws IOException {
    GoogleCredentials credentials = GoogleCredentials
        .fromStream(new FileInputStream("/Users/alluxio/downloads/lutestproject-894ee0d470c2.json"))
        .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));

    Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    Bucket bucket = storage.get(mBucketName);
    BlobInfo info  = BlobInfo.newBuilder(BlobId.of(mBucketName, "fileName"))
        .build();
    storage.create(info, new byte[0]);
    BlobInfo info2  = BlobInfo.newBuilder(BlobId.of(mBucketName, "fileName/"))
        .build();
    storage.create(info2, new byte[0]);
    BlobInfo info3  = BlobInfo.newBuilder(BlobId.of(mBucketName, "fileName/myFile"))
        .build();
    storage.create(info3, new byte[0]);
  }
}
