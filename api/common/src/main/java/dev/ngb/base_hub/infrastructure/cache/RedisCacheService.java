package dev.ngb.base_hub.infrastructure.cache;

import dev.ngb.base_hub.application.spi.cache.CacheService;
import dev.ngb.base_hub.application.spi.lock.LockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.redisson.api.options.KeysScanOptions;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisCacheService implements CacheService {

    private final RedissonClient redissonClient;
    private final LockService lockService;

    // Cache to prevent stampede
    private final ConcurrentMap<String, CompletableFuture<?>> futureCache = new ConcurrentHashMap<>();

    // Default lock timings
    private static final long LOCK_WAIT_TIME_MS = 2000;
    private static final long LOCK_LEASE_TIME_MS = 3000;
    private static final long FUTURE_TIMEOUT_MS = 5000;

    // prefix for all cache
    private static final String CACHE_PREFIX = "cache:";

    private String buildCacheKey(String key) {
        return CACHE_PREFIX + key;
    }

    @Override
    public <V> void put(String key, V value) {
        put(key, value, null);
    }

    @Override
    public <V> void put(String key, V value, Duration timeout) {
        String realKey = buildCacheKey(key);
        RBucket<V> bucket = redissonClient.getBucket(realKey);

        if (timeout == null) {
            bucket.set(value);
        } else {
            bucket.set(value, timeout);
        }
    }

    @Override
    public <V> V get(String key) {
        return get(key, null, null);
    }

    @Override
    public <V> V get(String key, Supplier<V> callbackIfNull) {
        return get(key, callbackIfNull, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> V get(String key, Supplier<V> callbackIfNull, Duration timeout) {
        String realKey = buildCacheKey(key);
        RBucket<V> bucket = redissonClient.getBucket(realKey);
        V value = bucket.get();
        if (value != null) return value;

        if (callbackIfNull == null) return null;

        String lockKey = "lock:" + realKey;

        CompletableFuture<V> future = (CompletableFuture<V>) futureCache.computeIfAbsent(realKey, k ->
                CompletableFuture.supplyAsync(() -> {
                    boolean locked = lockService.tryLock(lockKey, LOCK_WAIT_TIME_MS, LOCK_LEASE_TIME_MS);
                    try {
                        V result = bucket.get();
                        if (result == null && locked) {
                            log.info("Cache miss for key {}, loading from callback...", realKey);
                            result = callbackIfNull.get();
                            if (result != null) {
                                put(key, result, timeout);
                            }
                        }
                        return result;
                    } finally {
                        if (locked) lockService.unlock(lockKey);
                        futureCache.remove(realKey);
                    }
                })
        );

        try {
            value = future.get(FUTURE_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread interrupted while loading cache for key {}", realKey, e);
        } catch (ExecutionException e) {
            log.error("Execution error while loading cache for key {}", realKey, e);
        } catch (TimeoutException e) {
            log.warn("Timeout while loading cache for key {}, returning stale value if exists", realKey);
            value = bucket.get(); // fallback
        }

        return value;
    }

    @Override
    public void evict(String key) {
        redissonClient.getBucket(buildCacheKey(key)).delete();
    }

    @Override
    public void evictAll(String prefix) {
        String realPrefix = CACHE_PREFIX + prefix;
        RKeys rKeys = redissonClient.getKeys();
        KeysScanOptions options = KeysScanOptions.defaults()
                .limit(1000)
                .pattern(realPrefix + "*");

        try {
            List<String> keys = new ArrayList<>();
            rKeys.getKeys(options).forEach(keys::add);

            if (!keys.isEmpty()) {
                rKeys.delete(keys.toArray(new String[0]));
            }
        } catch (Exception e) {
            log.error("Failed to evict cache with prefix {}", realPrefix, e);
        }
    }
}