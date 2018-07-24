package com.erola.btsearch.util.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by Erola on 2018/7/24.
 */
public class JedisSingleClient implements IJedisClient {
    /**
     * jedis 连接池
     */
    private JedisPool jedisPool;

    public JedisSingleClient(JedisPool jedisPool){
        this.jedisPool = jedisPool;
    }

    @Override
    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.get(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public String set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.set(key, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public String setex(String key, String value, int expireSeconds) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.setex(key, expireSeconds, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
