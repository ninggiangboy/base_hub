package dev.ngb.base_hub.common.api.lock;

public interface LockService {
    boolean tryLock(String key, long waitTime, long leaseTime);
    void unlock(String key);
}
