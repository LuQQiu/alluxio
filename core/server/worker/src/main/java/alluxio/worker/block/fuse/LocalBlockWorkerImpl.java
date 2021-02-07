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

import alluxio.util.IdUtils;
import alluxio.worker.block.BlockWorker;
import alluxio.worker.block.DefaultBlockWorker;
import alluxio.worker.block.LocalBlockWorker;
import alluxio.worker.grpc.ShortCircuitBlockReadHandler;

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

  /**
   *
   * @return the local block path
   */
  private void readBlock() throws Exception{
    // GrpcBlockReadHandler -> general BlockReadHandler
    // GrpcShortCircuitReadHandler -> general ShortCircuitBlockReadHandler
    // LocalBlockWorkerImpl -> directly create BlockReadHandler, ShortCircuitBlockReadHandler without problem
  }

  /**
   *
   * @return the local block path
   */
  private String openLocalBlock() throws Exception{
    // TODO(lu) shortCircuitBlockReadHandler is also grpc, try to remove it

    // do the same logic as ShortCircuitBlockReadHandler.onNext (checkBlockId, block promote, mWorker.readBlock
    // TODO(lu) I should put all those logics around DefaultBlockWorker operations in DefaultBlockWorker, or have another layer somewhere ? and where?

    // this include two step: openLocalBlock(): return path, lockId and other information, closeLocalBlock (deal with onError, ExceptionCaught, onComplete, need lockId, session id, )
    // Have another layer before ShortCircuitBlockReadHandler to deal with the general cases! and allow other operation to openLocalBlock and closeLocalBlock
    // workerLocalBlockOperations.openLocalBlock, closeLocalBlock

    // all the operations we can take the GRPC logics out
    // where should i put those operations? may be still put in DefaultBlockWorker for now? if it's not that long and complex, if not, we can have another blockreadwriteManager
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
