package com.erola.btsearch.util.redis;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Erola on 2018/7/24.
 */
public class JedisClientFactory implements FactoryBean<IJedisClient>, InitializingBean {

    /**
     * redis 类型 Cluster：集群，其它：单机
     */
    private String clientType;

    /**
     * redis 访问密码
     */
    private String passWord;

    /**
     * 连接超时
     */
    private String timeout;

    /**
     * 响应超时
     */
    private String soTimeout;

    /**
     * redis 节点配置，单机类型只取第一个节点
     */
    private Set<String> redisNodes;

    /**
     * jedis 连接池配置
     */
    private JedisPoolConfig jedisPoolConfig;

    /**
     * 具体的 JedisClient
     */
    private IJedisClient jedisClient;

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public void setSoTimeout(String soTimeout) {
        this.soTimeout = soTimeout;
    }

    public void setRedisNodes(Set<String> redisNodes) {
        this.redisNodes = redisNodes;
    }

    public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public IJedisClient getObject() throws Exception {
        return jedisClient;
    }

    @Override
    public Class<?> getObjectType() {
        return (this.jedisClient != null ? this.jedisClient.getClass() : IJedisClient.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(redisNodes==null || redisNodes.isEmpty()){
            throw new IllegalArgumentException("redisNodes 配置不能为空");
        }
        int timeoutValue = 2000;
        int soTimeoutValue = 2000;
        String passWordValue = null;
        List<String> redisNodeList = new ArrayList<>(redisNodes);
        if(timeout!=null && !timeout.isEmpty()){
            timeoutValue = Integer.parseInt(timeout);
        }
        if(soTimeout!=null && !soTimeout.isEmpty()){
            soTimeoutValue = Integer.parseInt(soTimeout);
        }
        if(passWord!=null && !passWord.isEmpty()){
            passWordValue = passWord;
        }
        if(clientType==null || clientType.isEmpty() || !clientType.equalsIgnoreCase("Cluster")){
            String[] firstNodeArray = redisNodeList.get(0).split(":");
            if(firstNodeArray.length != 2){
                throw new ParseException("node address error!", 0);
            }
            JedisPool jedisPool = new JedisPool(jedisPoolConfig, firstNodeArray[0], Integer.parseInt(firstNodeArray[1]), timeoutValue, soTimeoutValue, passWordValue, 0, null, false, null, null, null);
            jedisClient = new JedisSingleClient(jedisPool);
        }else{
            int index = 0;
            Set<HostAndPort> hostPortSet = new HashSet<>();
            for(String node:redisNodeList){
                String[] nodeArray = node.split(":");
                if(nodeArray.length != 2){
                    throw new ParseException("node address error!", index);
                }
                hostPortSet.add(new HostAndPort(nodeArray[0],Integer.valueOf(nodeArray[1])));
                index++;
            }
            JedisCluster jedisCluster = new JedisCluster(hostPortSet, timeoutValue, soTimeoutValue, 5, passWordValue, jedisPoolConfig);
            jedisClient = new JedisClusterClient(jedisCluster);
        }
    }
}
