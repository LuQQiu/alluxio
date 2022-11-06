package alluxio.fuse.lock;

import alluxio.Constants;
import alluxio.concurrent.ClientRWLock;
import alluxio.concurrent.LockMode;
import alluxio.exception.runtime.CancelledRuntimeException;
import alluxio.exception.runtime.DeadlineExceededRuntimeException;
import alluxio.resource.LockResource;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class FuseReadWriteLockManager {
  private static final long TRY_LOCK_TIMEOUT = 20 * Constants.SECOND_MS;

  private final LoadingCache<String, ClientRWLock> mLockCache = CacheBuilder.newBuilder().weakValues()
      .build(new CacheLoader<String, ClientRWLock>() {
    @Override
    public ClientRWLock load(String key) {
      return new ClientRWLock();
    }
  });
  
  /**
   * Constructs a new {@link FuseReadWriteLockManager}.
   */
  public FuseReadWriteLockManager() {}

  public LockResource tryLock(String path, LockMode mode) {
    ClientRWLock pathLock = mLockCache.getUnchecked(path);
    Lock lock = mode == LockMode.READ ? pathLock.readLock() : pathLock.writeLock();
    try {
      if (!lock.tryLock(TRY_LOCK_TIMEOUT, TimeUnit.MILLISECONDS)) {
        throw new DeadlineExceededRuntimeException(String.format(
            "Failed to acquire lock for path %s after %s ms. "
                + "LockMode: %s, lock reference count = %s",
            path, TRY_LOCK_TIMEOUT, mode, pathLock.getReferenceCount()));
      }
      return new LockResource(lock);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new CancelledRuntimeException(String.format(
          "Failed to acquire lock for path %s after %s ms. "
              + "LockMode: %s, lock reference count = %s",
          path, TRY_LOCK_TIMEOUT, mode, pathLock.getReferenceCount()));
    }
  }
}
