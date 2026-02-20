package com.idgovern.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Redis Sharded Connection Pool Configuration
 */
@Configuration
public class RedisConfig {

    @Value("${redis.shard1.host:127.0.0.1}")
    private String host;

    @Value("${redis.shard1.port:6379}")
    private int port;

    @Value("${redis.pool.max-total:20}")
    private int maxTotal;

    /**
     * Configures a Redis Sharded Connection Pool.
     *
     * Responsibilities:
     * - Supports distributed Redis shards for scalability
     * - Provides a thread-safe connection pool
     * - Ensures high availability for caching and session management
     *
     * @return ShardedJedisPool configured for local Redis shard
     */
    @Bean
    public ShardedJedisPool shardedJedisPool() {
        // 1. Configure the connection pool settings
        GenericObjectPoolConfig<redis.clients.jedis.ShardedJedis> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(2);

        // 2. Define your shards (nodes)
        List<JedisShardInfo> shards = new ArrayList<>();
        JedisShardInfo shard1 = new JedisShardInfo(host, port);
        // If your Redis has a password, use: shard1.setPassword("your_password");
        shards.add(shard1);

        // 3. Create the pool
        return new ShardedJedisPool(poolConfig, shards);
    }
}