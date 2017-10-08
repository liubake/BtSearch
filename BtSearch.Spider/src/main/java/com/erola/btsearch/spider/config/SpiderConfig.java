package com.erola.btsearch.spider.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Erola on 2017/9/6.
 */
public class SpiderConfig {

    private int port;

    private String hostName;

    private int maxNodeQueueSize;

    private int downloadPeerQueueSize;

    private int downloadPeerThreadCount;

    private int nodeConnectTimeOut;

    private int nodeReadWriteTimeOut;

    private List<InetSocketAddress> dhtRootNodeList;

    private static SpiderConfig spiderConfigInstance;

    /**
     * 获取配置实例
     */
    private static SpiderConfig getSpiderConfigInstance(){
        if(spiderConfigInstance!=null)
            return spiderConfigInstance;
        else
            throw new NullPointerException("SpiderConfig uninitialized!");
    }

    /**
     *
     */
    public static int getPort(){
        return getSpiderConfigInstance().port;
    }

    /**
     *
     */
    public static String getHostName(){
        return getSpiderConfigInstance().hostName;
    }

    /**
     *
     */
    public static int getMaxNodeQueueSize(){
        return getSpiderConfigInstance().maxNodeQueueSize;
    }

    /**
     *
     */
    public static int getDownloadPeerQueueSize(){
        return getSpiderConfigInstance().downloadPeerQueueSize;
    }

    /**
     *
     */
    public static int getDownloadPeerThreadCount(){
        return getSpiderConfigInstance().downloadPeerThreadCount;
    }

    /**
     *
     * @return
     */
    public static int getNodeConnectTimeOut(){
        return getSpiderConfigInstance().nodeConnectTimeOut;
    }

    /**
     *
     * @return
     */
    public static int getNodeReadWriteTimeOut(){
        return getSpiderConfigInstance().nodeReadWriteTimeOut;
    }

    /**
     *
     */
    public static List<InetSocketAddress> getDhtRootNodeList(){
        return getSpiderConfigInstance().dhtRootNodeList;
    }

    /**
     * 初始化配置实例
     * @param propertiesFilePath
     */
    public static void initializeSpiderConfig(String propertiesFilePath) {
        try {
            FileInputStream propertiesStream = new FileInputStream(propertiesFilePath);
            initializeSpiderConfig(propertiesStream);
        } catch (FileNotFoundException e) {
            //这种配置初始化错误直接抛出去
            throw new RuntimeException(e);
        }
    }

    /**
     * 初始化配置实例
     * @param propertiesStream
     */
    public static void initializeSpiderConfig(InputStream propertiesStream){
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
     * 设置配置
     */
    private static void setPropertiesConfig(Properties propertiesConfig){
        if(spiderConfigInstance==null) {
            spiderConfigInstance = new SpiderConfig();
        }
        spiderConfigInstance.port = Integer.valueOf(propertiesConfig.get("Port").toString().trim());
        spiderConfigInstance.hostName = propertiesConfig.get("HostName").toString().trim();
        spiderConfigInstance.maxNodeQueueSize = Integer.valueOf(propertiesConfig.get("MaxNodeQueueSize").toString().trim());
        spiderConfigInstance.downloadPeerQueueSize = Integer.valueOf(propertiesConfig.get("DownloadPeerQueueSize").toString().trim());
        spiderConfigInstance.downloadPeerThreadCount = Integer.valueOf(propertiesConfig.get("DownloadPeerThreadCount").toString().trim());
        spiderConfigInstance.nodeConnectTimeOut = Integer.valueOf(propertiesConfig.get("NodeConnectTimeOut").toString().trim());
        spiderConfigInstance.nodeReadWriteTimeOut = Integer.valueOf(propertiesConfig.get("NodeReadWriteTimeOut").toString().trim());
        spiderConfigInstance.dhtRootNodeList = new ArrayList<>();
        String[] dhtRootNodeArray = propertiesConfig.get("DhtRootNodeList").toString().trim().split(";");
        for (String dhtRootNodeItem : dhtRootNodeArray) {
            String[] dhtRootNodeItemArray = dhtRootNodeItem.split(":");
            spiderConfigInstance.dhtRootNodeList.add(new InetSocketAddress(dhtRootNodeItemArray[0], Integer.valueOf(dhtRootNodeItemArray[1])));
        }
    }
}