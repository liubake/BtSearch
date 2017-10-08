package com.erola.btsearch.util.log4j;

import org.apache.log4j.Logger;

/**
 * Log4j 日志记录辅助类
 * Created by Erola on 2017/10/3.
 */
public class Log4jHelper {
    /**
     * 记录 Debug 类型日志
     * @param callClass
     * @param message
     */
    public static void logDebug(Class callClass, String message){
        Log4jConfig.confirmInitialized();
        Logger logger  =  Logger.getLogger(callClass);
        logger.debug(message);
    }

    /**
     * 记录 Info 类型日志
     * @param callClass
     * @param message
     */
    public static void logInfo(Class callClass, String message){
        Log4jConfig.confirmInitialized();
        Logger logger  =  Logger.getLogger(callClass);
        logger.info(message);
    }

    /**
     * 记录 Error 类型日志
     * @param callClass
     * @param message
     */
    public static void logError(Class callClass, String message){
        Log4jConfig.confirmInitialized();
        Logger logger  =  Logger.getLogger(callClass);
        logger.error(message);
    }

    /**
     * 记录 Error 类型日志，带异常信息
     * @param callClass
     * @param message
     * @param error
     */
    public static void logError(Class callClass, String message, Throwable error){
        Log4jConfig.confirmInitialized();
        Logger logger  =  Logger.getLogger(callClass);
        logger.error(message,error);
    }
}