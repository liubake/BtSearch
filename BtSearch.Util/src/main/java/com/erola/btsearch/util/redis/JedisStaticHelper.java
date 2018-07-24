/**
                                                                                     +
                Atom                                                                @@
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
package com.erola.btsearch.util.redis;

import redis.clients.jedis.*;

/**
 * Jedis 静态操作辅助类
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
}