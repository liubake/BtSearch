package com.erola.btsearch.util.redis;

import com.erola.btsearch.util.json.JsonHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Redis 操作辅助类
 * Created by Erola on 2017/9/10.
 */
public class RedisHelper {
    /**
     * Redis 连接池初始化类
     */
    private static class RedisPoolHelper {
        /**
         * Redis 写连接池实例
         */
        private static final JedisPool redisWritePool = initRedisWritePool();
        /**
         * Redis 读连接池实例
         */
        private static final List<JedisPool> redisReadPoolList = initRedisReadPoolList();

        /**
         * 初始化 Redis写入池
         * @return
         */
        private static JedisPool initRedisWritePool() {
            JedisPool ret = null;
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(RedisConfig.getWritePoolSize());
            config.setMaxIdle(RedisConfig.getWritePoolSize() / 10);
            config.setTestOnBorrow(true);
            String password = null;
            String writeServerIp = null;
            Integer writeServerPort = null;
            String redisWriteServer = RedisConfig.getWriteServer();
            if (redisWriteServer.indexOf('@') < 0) {
                String[] tempArray = redisWriteServer.split(":");
                writeServerIp = tempArray[0];
                writeServerPort = Integer.parseInt(tempArray[1]);
            } else {
                String[] tempArray1 = redisWriteServer.split("@");
                String[] tempArray2 = tempArray1[1].split(":");
                password = tempArray1[0];
                writeServerIp = tempArray2[0];
                writeServerPort = Integer.parseInt(tempArray2[1]);
            }
            if (password == null)
                ret = new JedisPool(config, writeServerIp, writeServerPort, 2000);
            else
                ret = new JedisPool(config, writeServerIp, writeServerPort, 2000, password);
            return ret;
        }

        /**
         * 初始化Redis读取池，支持配置多个读服务器
         * @return
         */
        private static List<JedisPool> initRedisReadPoolList() {
            List<JedisPool> ret = new ArrayList<JedisPool>();
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(RedisConfig.getReadPoolSize());
            config.setMaxIdle(RedisConfig.getReadPoolSize() / 10);
            config.setTestOnBorrow(true);
            String redisReadServerList = RedisConfig.getReadServerList();
            String[] redisReadServerArray = redisReadServerList.split(";");
            for (String redisReadServer : redisReadServerArray) {
                String password = null;
                String readServerIp = null;
                Integer readServerPort = null;
                if (redisReadServer.indexOf('@') < 0) {
                    String[] tempArray = redisReadServer.split(":");
                    readServerIp = tempArray[0];
                    readServerPort = Integer.parseInt(tempArray[1]);
                } else {
                    String[] tempArray1 = redisReadServer.split("@");
                    String[] tempArray2 = tempArray1[1].split(":");
                    password = tempArray1[0];
                    readServerIp = tempArray2[0];
                    readServerPort = Integer.parseInt(tempArray2[1]);
                }
                if (password == null)
                    ret.add(new JedisPool(config, readServerIp, readServerPort, 2000));
                else
                    ret.add(new JedisPool(config, readServerIp, readServerPort, 2000, password));
            }
            return ret;
        }

        /**
         * 获取Redis写入池
         * @return
         */
        private static JedisPool getRedisWritePool() {
            return redisWritePool;
        }

        /**
         * 获取Redis读取池
         * @return
         */
        private static JedisPool getRedisReadPool() {
            if (redisReadPoolList != null && !redisReadPoolList.isEmpty()) {
                int poolListSize = redisReadPoolList.size();
                if (poolListSize > 1)
                    return redisReadPoolList.get((int) (System.currentTimeMillis() % poolListSize));
                else
                    return redisReadPoolList.get(0);
            } else
                return null;
        }
    }

    /**
     * 判断对应的key是否存在
     * @param key
     * @return
     */
    public static Boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = RedisPoolHelper.getRedisReadPool().getResource();
            return jedis.exists(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 从Redis中删除
     * @param key
     * @return
     */
    public static Long del(String key) {
        Jedis jedis = null;
        try {
            jedis = RedisPoolHelper.getRedisWritePool().getResource();
            return jedis.del(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 从Redis中删除多个
     * @param keys
     * @return
     */
    public static Long del(String... keys) {
        Jedis jedis = null;
        try {
            jedis = RedisPoolHelper.getRedisWritePool().getResource();
            return jedis.del(keys);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 从Redis中查询数据
     * @param key
     * @return
     */
    public static String get(String key) {
        Jedis jedis = null;
        try {
            jedis = RedisPoolHelper.getRedisReadPool().getResource();
            return jedis.get(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 向Redis中写入数据
     * @param key
     * @param value
     * @return
     */
    public static String set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = RedisPoolHelper.getRedisWritePool().getResource();
            return jedis.set(key, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 向Redis中写入数据
     * @param key
     * @param value
     * @param expseconds
     * @return
     */
    public static String setex(String key, String value, int expseconds) {
        Jedis jedis = null;
        try {
            jedis = RedisPoolHelper.getRedisWritePool().getResource();
            return jedis.setex(key, expseconds, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T get(String key, Class<T> clazz) throws IOException {
        return JsonHelper.jsonToObject(get(key), clazz);
    }

    /**
     *
     * @param key
     * @param t
     * @param <T>
     * @return
     * @throws JsonProcessingException
     */
    public static <T> String set(String key, T t) throws JsonProcessingException {
        return set(key, JsonHelper.objectToJson(t));
    }

    /**
     *
     * @param key
     * @param t
     * @param expseconds
     * @param <T>
     * @return
     * @throws JsonProcessingException
     */
    public static <T> String setex(String key, T t, int expseconds) throws JsonProcessingException {
        return setex(key, JsonHelper.objectToJson(t), expseconds);
    }
}