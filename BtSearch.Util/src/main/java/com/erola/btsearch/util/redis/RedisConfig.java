package com.erola.btsearch.util.redis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Redis 配置类
 * Created by Erola on 2017/9/10.
 */
public class RedisConfig {
    /**
     * 写连接池数量
     */
    private int writePoolSize = 0;
    /**
     * 写服务器地址
     */
    private String writeServer = "";
    /**
     * 读连接池数量
     */
    private int readPoolSize = 0;
    /**
     * 读服务器地址
     */
    private String readServerList = "";

    /**
     * Redis 配置实例
     */
    private static RedisConfig redisConfigInstance;

    /**
     * 获取Redis 配置实例
     * @return
     */
    private static RedisConfig getRedisConfigInstance(){
        if(redisConfigInstance!=null)
            return redisConfigInstance;
        else
            throw new NullPointerException("RedisConfig uninitialized!");
    }

    /**
     * 获取写连接池配置
     * @return
     */
    public static int getWritePoolSize()
    {
        return getRedisConfigInstance().writePoolSize;
    }

    /**
     * 获取写服务器地址
     * @return
     */
    public static String getWriteServer()
    {
        return getRedisConfigInstance().writeServer;
    }

    /**
     * 获取读连接池配置
     * @return
     */
    public static int getReadPoolSize()
    {
        return getRedisConfigInstance().readPoolSize;
    }

    /**
     *获取读服务器地址
     * @return
     */
    public static String getReadServerList()
    {
        return getRedisConfigInstance().readServerList;
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
        } catch (IOException e) {
            //这种配置初始化错误直接抛出去
            throw new RuntimeException(e);
        } finally {
            if(propertiesStream!=null) {
                try {
                    propertiesStream.close();
                    propertiesStream = null;
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
    private static void setPropertiesConfig(Properties propertiesConfig){
        if(redisConfigInstance==null) {
            redisConfigInstance = new RedisConfig();
        }
        redisConfigInstance.writePoolSize = Integer.valueOf(propertiesConfig.get("WritePoolSize").toString().trim());
        redisConfigInstance.writeServer =propertiesConfig.get("WriteServer").toString().trim();
        redisConfigInstance.readPoolSize = Integer.valueOf(propertiesConfig.get("ReadPoolSize").toString().trim());
        redisConfigInstance.readServerList =propertiesConfig.get("ReadServerList").toString().trim();
    }
}