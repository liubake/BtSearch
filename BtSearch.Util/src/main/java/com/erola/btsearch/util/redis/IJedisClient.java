package com.erola.btsearch.util.redis;

/**
 * Created by Erola on 2018/7/24.
 */
public interface IJedisClient {


    /**
     * 查询指定key的值
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 设置key对应的值
     * @param key
     * @param value
     * @return
     */
    String set(String key, String value);

    /**
     * 设置key对应的值并加上过期时间
     * @param key
     * @param value
     * @param expireSeconds
     * @return
     */
    String setex(String key, String value, int expireSeconds);


}
