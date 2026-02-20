package com.idgovern.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;


/**
 * Service class for managing Redis connection pooling.
 *
 * <p>
 * Provides simplified access to Redis resources using a
 * {@link redis.clients.jedis.ShardedJedisPool}. Encapsulates
 * connection acquisition and safe resource release logic
 * to ensure proper lifecycle management.
 * </p>
 *
 * <p>
 * Business Rules:
 * <ul>
 *     <li>Redis connections must be obtained from the pool.</li>
 *     <li>All acquired connections must be returned to the pool.</li>
 *     <li>Resources must be safely closed to prevent connection leaks.</li>
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
@Service("redisPool")
@Slf4j
public class RedisPool {

    /**
     * Injected Redis sharded connection pool.
     * Managed by the Spring container.
     */
    @Resource(name = "shardedJedisPool")
    private ShardedJedisPool shardedJedisPool;

    /**
     * Obtain a Redis resource from the connection pool.
     *
     * @return ShardedJedis instance
     */
    public ShardedJedis instance() {
        return shardedJedisPool.getResource();
    }

    /**
     * Safely close (return) the Redis resource back to the pool.
     *
     * @param shardedJedis Redis connection instance
     */
    public void safeClose(ShardedJedis shardedJedis) {
        try {
           if (shardedJedis != null) {
               shardedJedis.close();
           }
        } catch (Exception e) {
            log.error("return redis resource exception", e);
        }
    }
}
