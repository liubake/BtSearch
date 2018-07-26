package com.erola.btsearch.util.log4j;

import org.apache.log4j.PropertyConfigurator;
import java.io.InputStream;

/**
 * Log4j 配置类
 * Created by Erola on 2017/10/3.
 */
public class Log4jConfig {
    /**
     *
     */
    private static boolean hasInitialized=false;

    /**
     * 检测初始化状态
     */
    public static void confirmInitialized(){
        if(!hasInitialized){
            throw new NullPointerException("Log4jConfig uninitialized!");
        }
    }

    /**
     * 初始化配置
     * @param propertiesFilePath
     */
    public static void initializeConfig(String propertiesFilePath) {
        PropertyConfigurator.configure(propertiesFilePath);
        hasInitialized=true;
    }

    /**
     * 初始化配置
     * @param propertiesStream
     */
    public static void initializeConfig(InputStream propertiesStream) {
        PropertyConfigurator.configure(propertiesStream);
        hasInitialized=true;
        //不用捕获，初始化异常直接抛出
    }
}