package com.idgovern.service;

import com.google.common.base.Joiner;
import com.idgovern.beans.CacheKeyConstants;
import com.idgovern.util.JsonMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;


/**
 * Service class for managing Redis-based caching operations.
 *
 * <p>
 * Provides abstraction for storing and retrieving cached data
 * using Redis. Handles cache key generation, expiration control,
 * and exception-safe resource management.
 * </p>
 *
 * <p>
 * Business Rules:
 * <ul>
 *     <li>All cache keys must be generated using predefined prefixes.</li>
 *     <li>Cache entries must define an expiration time (TTL).</li>
 *     <li>Null values are not cached.</li>
 *     <li>Redis resources must always be safely released.</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author     | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-03-01 | Lilian S.  | Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Service
@Slf4j
public class SysCacheService {

    /**
     * Redis connection pool wrapper.
     */
    @Resource(name = "redisPool")
    private RedisPool redisPool;


    /**
     * Save value to cache using prefix only.
     *
     * @param toSavedValue   value to be cached
     * @param timeoutSeconds expiration time in seconds
     * @param prefix         cache key prefix
     */
    public void saveCache(String toSavedValue, int timeoutSeconds, CacheKeyConstants prefix) {
        saveCache(toSavedValue, timeoutSeconds, prefix, null);
    }


    /**
     * Save value to cache with dynamic key components.
     *
     * <p>
     * Cache key format:
     * <pre>
     *     PREFIX_key1_key2_key3
     * </pre>
     * </p>
     *
     * @param toSavedValue   value to be cached
     * @param timeoutSeconds expiration time in seconds
     * @param prefix         cache key prefix
     * @param keys           optional dynamic key segments
     */
    public void saveCache(String toSavedValue, int timeoutSeconds, CacheKeyConstants prefix, String... keys) {

        if (toSavedValue == null) {
            return;
        }

        ShardedJedis shardedJedis = null;

        try {
            String cacheKey = generateCacheKey(prefix, keys);
            shardedJedis = redisPool.instance();
            shardedJedis.setex(cacheKey, timeoutSeconds, toSavedValue);
        } catch (Exception e) {
            log.error("save cache exception, prefix:{}, keys:{}", prefix.name(), JsonMapper.obj2String(keys), e);
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }


    /**
     * Retrieve value from cache.
     *
     * @param prefix cache key prefix
     * @param keys   optional dynamic key segments
     * @return cached value, or null if not found or error occurs
     */
    public String getFromCache(CacheKeyConstants prefix, String... keys) {

        ShardedJedis shardedJedis = null;
        String cacheKey = generateCacheKey(prefix, keys);

        try {
            shardedJedis = redisPool.instance();
            String value = shardedJedis.get(cacheKey);
            return value;
        } catch (Exception e) {
            log.error("get from cache exception, prefix:{}, keys:{}", prefix.name(), JsonMapper.obj2String(keys), e);
            return null;
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }


    /**
     * Generate standardized cache key.
     *
     * <p>
     * Key generation strategy:
     * <ul>
     *     <li>Base key = prefix name</li>
     *     <li>Optional keys appended using underscore separator</li>
     * </ul>
     * </p>
     *
     * Example:
     * <pre>
     *     USER_1001_PROFILE
     * </pre>
     *
     * @param prefix cache key prefix enum
     * @param keys   dynamic key parts
     * @return formatted cache key
     */
    private String generateCacheKey(CacheKeyConstants prefix, String... keys) {
        String key = prefix.name();
        if (keys != null && keys.length > 0) {
            key += "_" + Joiner.on("_").join(keys);
        }
        return key;
    }
}
