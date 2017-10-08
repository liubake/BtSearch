package com.erola.btsearch.util.mongodb;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * MongoDB 配置类
 * Created by Erola on 2017/9/10.
 */
public class MongoDBConfig {
    /**
     * 端口号
     */
    private int port = 0;
    /**
     * 连接数量
     */
    private int poolSize=0;
    /**
     *等待队列长度
     */
    private int blockSize=0;
    /**
     * 队列等待时间
     */
    private int blockWaittime=0;
    /**
     * 连接超时时间
     */
    private int connectTimeout=0;
    /**
     * 连接空闲时间
     */
    private int connectIdletime=0;
    /**
     *数据库名称
     */
    private String dbName="";
    /**
     *服务器地址
     */
    private String serverAddress = "";

    /**
     * MongoDB 配置实例
     */
    private static MongoDBConfig mongoDBConfigInstance;

    /**
     * 获取MongoDB 配置实例
     * @return
     */
    private static MongoDBConfig getMongoDBConfigInstance(){
        if(mongoDBConfigInstance!=null)
            return mongoDBConfigInstance;
        else
            throw new NullPointerException("MongoDBConfig uninitialized!");
    }

    /**
     * 获取端口号
     * @return
     */
    public static int getPort(){
        return getMongoDBConfigInstance().port;
    }

    /**
     * 获取连接池大小
     * @return
     */
    public static int getPoolSize(){
        return getMongoDBConfigInstance().poolSize;
    }

    /**
     * 获取队列大小
     * @return
     */
    public static int getBlockSize(){
        return getMongoDBConfigInstance().blockSize;
    }

    /**
     * 获取队列等待时间
     * @return
     */
    public static int getBlockWaittime(){
        return getMongoDBConfigInstance().blockWaittime;
    }

    /**
     * 获取连接超时时间
     * @return
     */
    public static int getConnectTimeout(){
        return getMongoDBConfigInstance().connectTimeout;
    }

    /**
     * 获取连接空闲时间
     * @return
     */
    public static int getConnectIdletime(){
        return getMongoDBConfigInstance().connectIdletime;
    }

    /**
     * 获取数据库名称
     * @return
     */
    public static String getDbName(){
        return getMongoDBConfigInstance().dbName;
    }

    /**
     * 获取服务器地址
     * @return
     */
    public static String getServerAddress(){
        return getMongoDBConfigInstance().serverAddress;
    }

    /**
     * 初始化MongoDB 配置实例
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
     * 初始化MongoDB 配置实例
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
        if(mongoDBConfigInstance==null) {
            mongoDBConfigInstance = new MongoDBConfig();
        }
        mongoDBConfigInstance.port = Integer.valueOf(propertiesConfig.get("Port").toString().trim());
        mongoDBConfigInstance.poolSize = Integer.valueOf(propertiesConfig.get("PoolSize").toString().trim());
        mongoDBConfigInstance.blockSize = Integer.valueOf(propertiesConfig.get("BlockSize").toString().trim());
        mongoDBConfigInstance.blockWaittime = Integer.valueOf(propertiesConfig.get("BlockWaittime").toString().trim());
        mongoDBConfigInstance.connectTimeout = Integer.valueOf(propertiesConfig.get("ConnectTimeout").toString().trim());
        mongoDBConfigInstance.connectIdletime = Integer.valueOf(propertiesConfig.get("ConnectIdletime").toString().trim());
        mongoDBConfigInstance.dbName =propertiesConfig.get("DbName").toString().trim();
        mongoDBConfigInstance.serverAddress =propertiesConfig.get("ServerAddress").toString().trim();
    }
}