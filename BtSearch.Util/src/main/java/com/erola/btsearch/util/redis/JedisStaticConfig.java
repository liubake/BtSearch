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

import redis.clients.jedis.HostAndPort;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

/**
 * 使用静态方法调用的Redis 配置类
 * Created by Erola on 2017/9/10.
 */
public class JedisStaticConfig {
    /**
     * redis 类型 Cluster：集群，其它：单机
     */
    private String clientType;

    /**
     * 访问密码
     */
    private String passWord;

    /**
     * 连接超时，不配置默认2000毫秒
     */
    private int timeout = 2000;

    /**
     * 响应超时，不配置默认2000毫秒
     */
    private int soTimeout = 2000;

    /**
     * redis 节点配置，单机类型只取第一个节点
     */
    private Set<HostAndPort> hostPortSet;

    /**
     * 最大空闲连接数
     */
    private Integer maxIdle;

    /**
     * 最大连接数
     */
    private Integer maxTotal;

    /**
     * 获取连接时的最大等待毫秒数, 默认-1
     */
    private Integer maxWaitMillis;

    /**
     * 在获取连接的时候检查有效性, 默认false
     */
    private Boolean testOnBorrow;

    /**
     * 在空闲时检查有效性, 默认false
     */
    private Boolean testWhileIdle;

    /**
     * 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
     */
    private Boolean blockWhenExhausted;

    /**
     * 每次释放连接的最大数目
     */
    private Integer numTestsPerEvictionRun;

    /**
     * 连接空闲的最小时间，达到此值后空闲连接将可能会被移除，负值(-1)表示不移除
     */
    private Integer minEvictableIdleTimeMillis;

    /**
     * 空闲链接”检测线程，检测的周期，毫秒数。如果为负值，表示不运行“检测线程”。默认为-1
     */
    private Integer timeBetweenEvictionRunsMillis;

    /**
     * 连接空闲的最小时间，达到此值后空闲链接将会被移除，且保留“minIdle”个空闲连接数。默认为-1
     */
    private Integer softMinEvictableIdleTimeMillis;


    /**
     * Jedis 静态配置实例
     */
    private static JedisStaticConfig jedisStaticConfigInstance;

    /**
     * 获取Redis 配置实例
     * @return
     */
    private static JedisStaticConfig getJedisStaticConfigInstance(){
        if(jedisStaticConfigInstance!=null)
            return jedisStaticConfigInstance;
        else
            throw new NullPointerException("JedisStaticConfig uninitialized!");
    }

    public static String getClientType() {
        return getJedisStaticConfigInstance().clientType;
    }

    public static String getPassWord() {
        return getJedisStaticConfigInstance().passWord;
    }

    public static int getTimeout() {
        return getJedisStaticConfigInstance().timeout;
    }

    public static int getSoTimeout() {
        return getJedisStaticConfigInstance().soTimeout;
    }

    public static Set<HostAndPort> getHostPortSet() {
        return getJedisStaticConfigInstance().hostPortSet;
    }

    public static Integer getMaxIdle() {
        return getJedisStaticConfigInstance().maxIdle;
    }

    public static Integer getMaxTotal() {
        return getJedisStaticConfigInstance().maxTotal;
    }

    public static Integer getMaxWaitMillis() {
        return getJedisStaticConfigInstance().maxWaitMillis;
    }

    public static Boolean getTestOnBorrow() {
        return getJedisStaticConfigInstance().testOnBorrow;
    }

    public static Boolean getTestWhileIdle() {
        return getJedisStaticConfigInstance().testWhileIdle;
    }

    public static Boolean getBlockWhenExhausted() {
        return getJedisStaticConfigInstance().blockWhenExhausted;
    }

    public static Integer getNumTestsPerEvictionRun() {
        return getJedisStaticConfigInstance().numTestsPerEvictionRun;
    }

    public static Integer getMinEvictableIdleTimeMillis() {
        return getJedisStaticConfigInstance().minEvictableIdleTimeMillis;
    }

    public static Integer getTimeBetweenEvictionRunsMillis() {
        return getJedisStaticConfigInstance().timeBetweenEvictionRunsMillis;
    }

    public static Integer getSoftMinEvictableIdleTimeMillis() {
        return getJedisStaticConfigInstance().softMinEvictableIdleTimeMillis;
    }

    /**
     * 初始化Redis 配置实例
     * @param propertiesFilePath
     */
    public static void initializeRedisConfig(String propertiesFilePath) {
        try {
            FileInputStream propertiesStream = new FileInputStream(propertiesFilePath);
            initializeRedisConfig(propertiesStream);
        } catch (FileNotFoundException e) {
            //这种配置初始化错误直接抛出去
            throw new RuntimeException(e);
        }
    }

    /**
     * 初始化Redis 配置实例
     * @param propertiesStream
     */
    public static void initializeRedisConfig(InputStream propertiesStream){
        try{
            Properties propertiesConfig =new Properties();
            propertiesConfig.load(propertiesStream);
            setPropertiesConfig(propertiesConfig);
        } catch (Exception e) {
            //这种配置初始化错误直接抛出去
            throw new RuntimeException(e);
        } finally {
            if(propertiesStream!=null) {
                try {
                    propertiesStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 设置配置文件值
     * @param propertiesConfig
     */
    private static void setPropertiesConfig(Properties propertiesConfig) throws ParseException {
        if(jedisStaticConfigInstance == null) {
            jedisStaticConfigInstance = new JedisStaticConfig();
        }

        String redisNodesValue = propertiesConfig.getProperty("jedis.client.redisNodes");
        if(redisNodesValue==null || redisNodesValue.trim().isEmpty()){
            throw new IllegalArgumentException("redisNodes 配置不能为空");
        }
        jedisStaticConfigInstance.hostPortSet = new LinkedHashSet<>();
        String[] redisNodesArray = redisNodesValue.trim().split(";");
        int index = 0;
        for(String node : redisNodesArray){
            String[] nodeArray = node.trim().split(":");
            if(nodeArray.length != 2){
                throw new ParseException("node address error", index);
            }
            jedisStaticConfigInstance.hostPortSet.add(new HostAndPort(nodeArray[0],Integer.valueOf(nodeArray[1])));
            index++;
        }
        String clientTypeValue = propertiesConfig.getProperty("jedis.clientType");
        if(clientTypeValue!=null && !clientTypeValue.trim().isEmpty()){
            jedisStaticConfigInstance.clientType = clientTypeValue.trim();
        }
        String passWordValue = propertiesConfig.getProperty("jedis.client.passWord");
        if(passWordValue!=null && !passWordValue.trim().isEmpty()){
            jedisStaticConfigInstance.passWord = passWordValue.trim();
        }
        String timeoutValue = propertiesConfig.getProperty("jedis.client.timeout");
        if(timeoutValue!=null && !timeoutValue.trim().isEmpty()){
            jedisStaticConfigInstance.timeout = Integer.parseInt(timeoutValue.trim());
        }
        String soTimeoutValue = propertiesConfig.getProperty("jedis.client.soTimeout");
        if(soTimeoutValue!=null && !timeoutValue.trim().isEmpty()){
            jedisStaticConfigInstance.soTimeout = Integer.parseInt(soTimeoutValue.trim());
        }
        String maxIdleValue = propertiesConfig.getProperty("jedis.pool.maxIdle");
        if(maxIdleValue!=null && !maxIdleValue.trim().isEmpty()){
            jedisStaticConfigInstance.maxIdle = Integer.parseInt(maxIdleValue.trim());
        }
        String maxTotalValue = propertiesConfig.getProperty("jedis.pool.maxTotal");
        if(maxTotalValue!=null && !maxTotalValue.trim().isEmpty()){
            jedisStaticConfigInstance.maxTotal = Integer.parseInt(maxTotalValue.trim());
        }
        String maxWaitMillisValue = propertiesConfig.getProperty("jedis.pool.maxWaitMillis");
        if(maxWaitMillisValue!=null && !maxWaitMillisValue.trim().isEmpty()){
            jedisStaticConfigInstance.maxWaitMillis = Integer.parseInt(maxWaitMillisValue.trim());
        }
        String testOnBorrowValue = propertiesConfig.getProperty("jedis.pool.testOnBorrow");
        if(testOnBorrowValue!=null && !testOnBorrowValue.trim().isEmpty()){
            jedisStaticConfigInstance.testOnBorrow = Boolean.parseBoolean(testOnBorrowValue.trim());
        }
        String testWhileIdleValue = propertiesConfig.getProperty("jedis.pool.testWhileIdle");
        if(testWhileIdleValue!=null && !testWhileIdleValue.trim().isEmpty()){
            jedisStaticConfigInstance.testWhileIdle = Boolean.parseBoolean(testWhileIdleValue.trim());
        }
        String blockWhenExhaustedValue = propertiesConfig.getProperty("jedis.pool.blockWhenExhausted");
        if(blockWhenExhaustedValue!=null && !blockWhenExhaustedValue.trim().isEmpty()){
            jedisStaticConfigInstance.blockWhenExhausted = Boolean.parseBoolean(blockWhenExhaustedValue.trim());
        }
        String numTestsPerEvictionRunValue = propertiesConfig.getProperty("jedis.pool.numTestsPerEvictionRun");
        if(numTestsPerEvictionRunValue!=null && !numTestsPerEvictionRunValue.trim().isEmpty()){
            jedisStaticConfigInstance.numTestsPerEvictionRun = Integer.parseInt(numTestsPerEvictionRunValue.trim());
        }
        String minEvictableIdleTimeMillisValue = propertiesConfig.getProperty("jedis.pool.minEvictableIdleTimeMillis");
        if(minEvictableIdleTimeMillisValue!=null && !minEvictableIdleTimeMillisValue.trim().isEmpty()){
            jedisStaticConfigInstance.minEvictableIdleTimeMillis = Integer.parseInt(minEvictableIdleTimeMillisValue.trim());
        }
        String timeBetweenEvictionRunsMillisValue = propertiesConfig.getProperty("jedis.pool.timeBetweenEvictionRunsMillis");
        if(timeBetweenEvictionRunsMillisValue!=null && !timeBetweenEvictionRunsMillisValue.trim().isEmpty()){
            jedisStaticConfigInstance.timeBetweenEvictionRunsMillis = Integer.parseInt(timeBetweenEvictionRunsMillisValue.trim());
        }
        String softMinEvictableIdleTimeMillisValue = propertiesConfig.getProperty("jedis.pool.softMinEvictableIdleTimeMillis");
        if(softMinEvictableIdleTimeMillisValue!=null && !softMinEvictableIdleTimeMillisValue.trim().isEmpty()){
            jedisStaticConfigInstance.softMinEvictableIdleTimeMillis = Integer.parseInt(softMinEvictableIdleTimeMillisValue.trim());
        }
    }
}