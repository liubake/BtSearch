package com.erola.btsearch.util.redis;

import redis.clients.jedis.JedisCluster;

/**
 * Created by Erola on 2018/7/24.
 */
public class JedisClusterClient implements IJedisClient {
    /**
     * jedis集群
     */
    private JedisCluster jedisCluster;

    public JedisClusterClient(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    @Override
    public String get(String key) {
        return jedisCluster.get(key);
    }

    @Override
    public String set(String key, String value) {
        return jedisCluster.set(key, value);
    }

    @Override
    public String setex(String key, String value, int expireSeconds) {
        return jedisCluster.setex(key,expireSeconds,value);
    }
}
