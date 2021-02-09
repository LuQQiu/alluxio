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

package alluxio.worker.block.fuse;

import alluxio.block.BlockReadRequest;
import alluxio.proto.dataserver.Protocol;
import alluxio.util.IdUtils;
import alluxio.worker.block.BlockWorker;
import alluxio.worker.block.LocalBlockWorker;
import alluxio.worker.block.io.BlockReader;

import java.io.IOException;

// The main thing is to extract common information from BlockWorkerImpl. Grpc and internal client all directly call and create those operations.
public class LocalBlockWorkerImpl implements LocalBlockWorker {
  private final BlockWorker mBlockWorker;
  /**
   * Constructs a new {@link EmbeddedFuseManager}.
   */
  public LocalBlockWorkerImpl(BlockWorker blockWorker) {
    mBlockWorker = blockWorker;
  }

  // TODO(lu) where to use this two methods, and how to change to inputstream
  public BlockReader getBlockReader(BlockReadRequest request) throws Exception{
    // GrpcBlockReadHandler -> general BlockReadHandler
    // GrpcShortCircuitReadHandler -> general ShortCircuitBlockReadHandler
    // LocalBlockWorkerImpl -> directly create BlockReadHandler, ShortCircuitBlockReadHandler without problem
    return mBlockWorker.getBlockReader(request);
  }

  public void  closeBlockReader(BlockReader reader, long sessionId, long blockId) throws Exception {
    mBlockWorker.closeBlockReader(reader, sessionId, blockId);
  }

  private void asyncCache() throws Exception {
    // TODO(lu) AsyncCacheReuqestManager is strongly bound to Grpc AsyncCacheRequest and proto
    // TODO(lu) BlockWorkerImpl creates the AsyncCacheRequestManager??? this should be able to access by all kinds of clients, and better to move to DefaultBlockWorker
    // DefaultBlockWorker.asyncCache(general input) -> pass to AsyncCacheRequestManager
    // DefaultBlockWorker contains asyncCacheRequestManager, and
    // should i add the corresponding wire class in alluxio-core-base
    // Create alluxio.wire.AsyncCacheBlock for AsyncCacheRequest
    // Create alluxio.wire.OpenUfsBlock
  }

  private void removeBlock(long blockId) throws Exception {
    long sessionId = IdUtils.createSessionId();
    mBlockWorker.removeBlock(sessionId, blockId);
  }

  private void moveBlock(long blockId, String mediumType) throws Exception {
    long sessionId = IdUtils.createSessionId();
    // TODO(lu) exception handler
    mBlockWorker.moveBlockToMedium(sessionId, blockId, mediumType);
  }
}
