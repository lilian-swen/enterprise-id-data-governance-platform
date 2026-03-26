package com.idgovern.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.idgovern.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import jakarta.annotation.Resource;
import java.util.List;

/**
 * High-level service for Redis operations, wrapping the low-level RedisPool.
 * Handles automatic resource management and JSON serialization.
 */
@Service
@Slf4j
public class RedisService {

    @Resource(name = "redisPool")
    private RedisPool redisPool;

    /**
     * Sets a value with an expiration time.
     * Uses safeClose to ensure the connection returns to the pool.
     */
    public void setEx(String key, String value, long timeoutSeconds) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = redisPool.instance();
            shardedJedis.setex(key, (int) timeoutSeconds, value);
        } catch (Exception e) {
            log.error("Redis setEx error, key: {}, value: {}", key, value, e);
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    /**
     * Retrieves a raw String value from Redis.
     */
    public String get(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = redisPool.instance();
            return shardedJedis.get(key);
        } catch (Exception e) {
            log.error("Redis get error, key: {}", key, e);
            return null;
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    /**
     * Retrieves and deserializes a List of objects.
     * Integrates with your getUserAclListWithCache method.
     */
    public <T> List<T> getList(String key, Class<T> clazz) {
        String value = get(key);
        if (value == null) {
            return null;
        }
        // Uses your project's JsonMapper for conversion
        return JsonMapper.string2Obj(value, new TypeReference<List<T>>() {});
    }

    /**
     * Removes a key from Redis. Critical for cache invalidation when permissions change.
     */
    public void delete(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = redisPool.instance();
            shardedJedis.del(key);
        } catch (Exception e) {
            log.error("Redis delete error, key: {}", key, e);
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }
}