package dev.ngb.base_hub.infrastructure.cache;

import dev.ngb.base_hub.application.spi.cache.CacheService;
import dev.ngb.base_hub.application.spi.lock.LockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.redisson.api.options.KeysScanOptions;
import org.redisson.api.RTopic;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Slf4j
public class HybridCacheService implements CacheService {

    private final RedissonClient redissonClient;
    private final LockService lockService;

    // L1 Caffeine cache
    private final Cache<String, Object> localCache = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(Duration.ofMinutes(10))
            .build();

    // Cache to prevent stampede
    private final ConcurrentMap<String, CompletableFuture<?>> futureCache = new ConcurrentHashMap<>();

    private static final long LOCK_WAIT_TIME_MS = 2000;
    private static final long LOCK_LEASE_TIME_MS = 3000;
    private static final long FUTURE_TIMEOUT_MS = 5000;
    private static final String CACHE_INVALIDATE_TOPIC = "cache:invalidate";
    private static final String CACHE_INVALIDATE_ALL_TOPIC = "cache:invalidate-all";

    private static final String CACHE_PREFIX = "cache:";

    private String buildCacheKey(String key) {
        return CACHE_PREFIX + key;
    }

    @PostConstruct
    public void subscribeInvalidation() {
        RTopic t1 = redissonClient.getTopic(CACHE_INVALIDATE_TOPIC);
        t1.addListener(String.class, (_, key) -> {
            localCache.invalidate(key);
            log.info("[L1 INVALIDATE] key={}", key);
        });

        RTopic t2 = redissonClient.getTopic(CACHE_INVALIDATE_ALL_TOPIC);
        t2.addListener(String.class, (_, prefix) -> {
            localCache.asMap().keySet().removeIf(k -> k.startsWith(prefix));
            log.info("[L1 INVALIDATE ALL] prefix={}", prefix);
        });
    }

    @Override
    public <V> void put(String key, V value) {
        put(key, value, null);
    }

    @Override
    public <V> void put(String key, V value, Duration timeout) {
        localCache.put(key, value);

        String realKey = buildCacheKey(key);
        RBucket<V> bucket = redissonClient.getBucket(realKey);

        if (timeout == null) bucket.set(value);
        else bucket.set(value, timeout);

        redissonClient.getTopic(CACHE_INVALIDATE_TOPIC).publish(key);
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

        Object l1 = localCache.getIfPresent(key);
        if (l1 != null) return (V) l1;

        String realKey = buildCacheKey(key);
        RBucket<V> bucket = redissonClient.getBucket(realKey);
        V value = bucket.get();
        if (value != null) {
            localCache.put(key, value);
            return value;
        }

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
            value = bucket.get();
        }

        if (value != null) localCache.put(key, value);
        return value;
    }

    @Override
    public void evict(String key) {
        localCache.invalidate(key);
        redissonClient.getBucket(buildCacheKey(key)).delete();
        redissonClient.getTopic(CACHE_INVALIDATE_TOPIC).publish(key);
    }

    @Override
    public void evictAll(String prefix) {
        localCache.asMap().keySet().removeIf(k -> k.startsWith(prefix));

        String realPrefix = CACHE_PREFIX + prefix;
        RKeys rKeys = redissonClient.getKeys();
        KeysScanOptions options = KeysScanOptions.defaults()
                .limit(1000)
                .pattern(realPrefix + "*");

        try {
            List<String> keys = new ArrayList<>();
            rKeys.getKeys(options).forEach(keys::add);

            if (!keys.isEmpty()) rKeys.delete(keys.toArray(new String[0]));

            redissonClient.getTopic(CACHE_INVALIDATE_ALL_TOPIC).publish(prefix);

        } catch (Exception e) {
            log.error("Failed to evict cache with prefix {}", realPrefix, e);
        }
    }
}
