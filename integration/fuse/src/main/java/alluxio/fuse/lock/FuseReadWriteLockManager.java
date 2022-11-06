package alluxio.fuse.lock;

import alluxio.concurrent.ClientRWLock;
import alluxio.concurrent.LockMode;
import alluxio.exception.runtime.CancelledRuntimeException;
import alluxio.exception.runtime.DeadlineExceededRuntimeException;
import alluxio.resource.ResourcePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class FuseReadWriteLockManager {
  private static final Logger LOG = LoggerFactory.getLogger(FuseReadWriteLockManager.class);

  /** A pool of read write locks. */
  private final ResourcePool<ClientRWLock> mLockPool;

  /** A map from block id to the read write lock used to guard that block. */
  private final ConcurrentHashMap<String, ClientRWLock> mLocks;

  /**
   * Constructs a new {@link FuseReadWriteLockManager}.
   */
  public FuseReadWriteLockManager(int initialCapacity, int concurrencyLevel, int maxCapacity) {
    mLockPool = new ResourcePool<ClientRWLock>(
        maxCapacity) {
      @Override
      public void close() {}

      @Override
      public ClientRWLock createNewResource() {
        return new ClientRWLock();
      }
    };
    mLocks = new ConcurrentHashMap<>(initialCapacity, 0.75f, concurrencyLevel);
  }

  public void tryLock(String path, LockMode mode) {
    long time = 2;
    TimeUnit unit = TimeUnit.MINUTES;
    ClientRWLock pathLock = getPathLock(path);
    Lock lock = mode == LockMode.READ ? pathLock.readLock() : pathLock.writeLock();
    try {
      if (!lock.tryLock(time, unit)) {
        throw new DeadlineExceededRuntimeException(String.format(
            "Failed to acquire lock for path %s after %s %s. "
                + "LockMode: %s, lock reference count = %s",
            path, time, unit, mode, pathLock.getReferenceCount()));
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new CancelledRuntimeException(String.format(
          "Failed to acquire lock for path %s after %s %s. "
              + "LockMode: %s, lock reference count = %s",
          path, time, unit, mode, pathLock.getReferenceCount()));
    }
  }
  
  public void unlock(String path, LockMode mode) {
    ClientRWLock pathRWLock = mLocks.get(path);
    Lock pathLock = mode == LockMode.READ ? pathRWLock.readLock() : pathRWLock.writeLock();
    pathLock.unlock();
    mLocks.computeIfPresent(
        path,
        (blkid, lock) -> {
          // If we were the last thread with a reference to the lock, clean it up.
          if (lock.dropReference() == 0) {
            mLockPool.release(lock);
            return null;
          }
          return lock;
        }
    );
  }
  
  private ClientRWLock getPathLock(String path) {
    // Loop until we either find the block lock in the mLocks map, or successfully acquire a new
    // block lock from the lock pool.
    while (true) {
      // Check whether a lock has already been allocated for the block id.
      ClientRWLock reuseExistingLock = mLocks.computeIfPresent(
          path,
          (blkid, lock) -> {
            lock.addReference();
            return lock;
          }
      );
      if (reuseExistingLock != null) {
        return reuseExistingLock;
      }
      // Since a block lock hasn't already been allocated, try to acquire a new one from the pool.
      // We shouldn't wait indefinitely in acquire because the another lock for this block could be
      // allocated to another thread, in which case we could just use that lock.
      ClientRWLock newlyAcquiredLock = mLockPool.acquire(1, TimeUnit.SECONDS);
      if (newlyAcquiredLock != null) {
        int referenceCount = newlyAcquiredLock.getReferenceCount();
        if (referenceCount != 0) {
          LOG.error("A block lock was not cleanly released as newly acquired locks should have 0 "
              + "references, but got {}", referenceCount);
        }
        ClientRWLock computed = mLocks.compute(path, (id, lock) -> {
          // Check if someone else acquired a block lock for blockId while we were acquiring one.
          if (lock != null) {
            // reuse someone else's lock and release newlyAcquiredLock later
            // Instead of releasing it immediately here, we release it outside mLock.compute
            // as ResourcePool.release has an internal lock, and may block due to concurrent calls
            // to ResourcePool.acquire, thus blocking other access to mLocks if called in
            // mLock.compute.
            lock.addReference();
            return lock;
          } else {
            newlyAcquiredLock.addReference();
            return newlyAcquiredLock;
          }
        });
        if (computed != newlyAcquiredLock) {
          // reuse someone else's lock and release the unused lock
          mLockPool.release(newlyAcquiredLock);
        }
        return computed;
      }
    }
  }
}
