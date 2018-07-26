/**
                                                                                     +
                lbk                                                                @@
                                                                                  `@@@@@
                                                                       +@'  .:+@@@;@@@@@@
                                                                    @@@@@@@@@@+:      `#@@:
                                                               ,'@@@@@@@@@,`.`;``.; ```` @@@@
                                                           @#@@@@@@@@@@@``.`````;:@+,`````': ``
                                                             :@@@@@@@@ ``````` :,@@@ ;`` ````` ,
                                                                @@@@@@@ .````. .@@@..`. `````: `
                                                               ;````` `.`````.    '``````` ,,@.`
                                                              .`..`, `..`````````````````.@@@;`;
                        ,;:,..,:.::::::;::,`                   , .......``````````````.`:,@@,`:
                @`````````````````````````````````` ``     `.,;,,;..:,..```````,````,``'.::`'
                  :,;@ ````````````````````````````````````````````....`.`````````````````.+
                   `:;,`````````````````````````````````````````````....`,:`````````````` +
                  .`.```.....`':.````````````..................```````......`.,;;.``,;;`
                                                        + `...``````````........`
                                                   @@@+#;..``````````````````` ``,
                                      ` :.,:,.+. @@+`  '#.```````````````````  ``
                                    .,'''' ``..`@@@@@'+++'```````````````` ; ``
                      `,:,,::,,;,.'''#++'`......@@@@@@'++;',````````````;.````;
                `::::.......`''+++''''''++`.``...;@@@@@@+  `` `.``` ,:`..``` .
           `::,::,......````.+++''+''+++'''+#'##+``:@@@@@@@@@+;:;,.....`````
        `,:::,,.........``.```#''+++''++''+++''+++``` ,,:      ;`....```` '
    .,,,::,,:::,,::..,:...`..,;,````.+''++++'''+'''',`'      ```....````;
                `` ``.;:,.....`````````..,.,                   ``..``` `
                     ```.,:::,,,,,,,,,::`                       ;;.```'
                                                                  ,```



 */
package com.erola.btsearch.util.jedis;

import com.fasterxml.jackson.core.JsonProcessingException;
import redis.clients.jedis.*;
import java.io.IOException;

/**
 * Jedis 静态操作辅助类，可以脱离spring使用
 * 需要在程序入口处初始化对应的配置
 * 支持根据配置连接redis单机或者集群
 * Created by Erola on 2017/9/10.
 */
public class JedisStaticHelper {
    /**
     * Jedis 客户端初始化静态类
     */
    private static class JedisClientHelper {
        /**
         * Jedis 客户端实例
         */
        private static final IJedisClient instance = initJedisClient();

        /**
         * 初始化 Jedis客户端
         * @return
         */
        private static IJedisClient initJedisClient() {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            if(JedisStaticConfig.getMaxIdle()!=null){
                jedisPoolConfig.setMaxIdle(JedisStaticConfig.getMaxIdle());
            }
            if(JedisStaticConfig.getMaxTotal()!=null){
                jedisPoolConfig.setMaxTotal(JedisStaticConfig.getMaxTotal());
            }
            if(JedisStaticConfig.getMaxWaitMillis()!=null){
                jedisPoolConfig.setMaxWaitMillis(JedisStaticConfig.getMaxWaitMillis());
            }
            if(JedisStaticConfig.getTestOnBorrow()!=null){
                jedisPoolConfig.setTestOnBorrow(JedisStaticConfig.getTestOnBorrow());
            }
            if(JedisStaticConfig.getTestWhileIdle()!=null){
                jedisPoolConfig.setTestWhileIdle(JedisStaticConfig.getTestWhileIdle());
            }
            if(JedisStaticConfig.getBlockWhenExhausted()!=null){
                jedisPoolConfig.setBlockWhenExhausted(JedisStaticConfig.getBlockWhenExhausted());
            }
            if(JedisStaticConfig.getNumTestsPerEvictionRun()!=null){
                jedisPoolConfig.setNumTestsPerEvictionRun(JedisStaticConfig.getNumTestsPerEvictionRun());
            }
            if(JedisStaticConfig.getMinEvictableIdleTimeMillis()!=null){
                jedisPoolConfig.setMinEvictableIdleTimeMillis(JedisStaticConfig.getMinEvictableIdleTimeMillis());
            }
            if(JedisStaticConfig.getTimeBetweenEvictionRunsMillis()!=null){
                jedisPoolConfig.setTimeBetweenEvictionRunsMillis(JedisStaticConfig.getTimeBetweenEvictionRunsMillis());
            }
            if(JedisStaticConfig.getSoftMinEvictableIdleTimeMillis()!=null){
                jedisPoolConfig.setSoftMinEvictableIdleTimeMillis(JedisStaticConfig.getSoftMinEvictableIdleTimeMillis());
            }
            if(JedisStaticConfig.getClientType()==null || JedisStaticConfig.getClientType().trim().isEmpty() || !JedisStaticConfig.getClientType().trim().equalsIgnoreCase("Cluster")){
                HostAndPort firstHostAndPort = JedisStaticConfig.getHostPortSet().iterator().next();
                JedisPool jedisPool = new JedisPool(jedisPoolConfig, firstHostAndPort.getHost(), firstHostAndPort.getPort(), JedisStaticConfig.getTimeout(), JedisStaticConfig.getSoTimeout(), JedisStaticConfig.getPassWord(), 0, null, false, null, null, null);
                return new JedisSingleClient(jedisPool);
            }else{

                JedisCluster jedisCluster = new JedisCluster(JedisStaticConfig.getHostPortSet(), JedisStaticConfig.getTimeout(), JedisStaticConfig.getSoTimeout(), 5, JedisStaticConfig.getPassWord(), jedisPoolConfig);
                return new JedisClusterClient(jedisCluster);
            }
        }
    }

    /**
     * 判断指定的key是否存在
     * @param key
     * @return
     */
    public static Boolean exists(String key) {
        return JedisClientHelper.instance.exists(key);
    }

    /**
     * 删除指定的key
     * @param key
     * @return
     */
    public static Long del(String key) {
        return JedisClientHelper.instance.del(key);
    }

    /**
     * 删除多个指定的key
     * @param keys
     * @return
     */
    public static Long del(String... keys) {
        return JedisClientHelper.instance.del(keys);
    }

    /**
     * 从Redis中查询数据
     * @param key
     * @return
     */
    public static String get(String key) {
        return JedisClientHelper.instance.get(key);
    }

    /**
     * 向Redis中写入数据
     * @param key
     * @param value
     * @return
     */
    public static String set(String key, String value) {
        return JedisClientHelper.instance.set(key, value);
    }

    /**
     * 向Redis中写入数据
     * @param key
     * @param value
     * @param expireSeconds
     * @return
     */
    public static String setex(String key, String value, int expireSeconds) {
        return JedisClientHelper.instance.setex(key, value, expireSeconds);
    }

    /**
     * 查询指定的key，并用json反序列化为指定的对象
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T get(String key, Class<T> clazz) throws IOException {
        return JedisClientHelper.instance.get(key, clazz);
    }

    /**
     * 设置key对应的，对象json序列化为字符串之后的值
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public static <T> String set(String key, T value) throws JsonProcessingException {
        return JedisClientHelper.instance.set(key, value);
    }

    /**
     * 设置key对应的，对象json序列化为字符串之后的值并设置过期时间
     * @param key
     * @param value
     * @param expireSeconds
     * @param <T>
     * @return
     */
    public static <T> String setex(String key, T value, int expireSeconds) throws JsonProcessingException {
        return JedisClientHelper.instance.setex(key, value, expireSeconds);
    }
}