package com.erola.btsearch.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by Erola on 2018/7/25.
 */
public class LifeCycleListener implements ServletContextListener {

    /**
     * 在服务启动时执行
     * @param servletContextEvent
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
//        /**----------------------初始化Log4j配置----------------------**/
//        String log4jConfigPath = servletContextEvent.getServletContext().getInitParameter("log4jConfigLocation");
//        if(log4jConfigPath!=null && !log4jConfigPath.isEmpty()){
//            Log4jConfig.initializeLog4jConfig(servletContextEvent.getServletContext().getResourceAsStream(log4jConfigPath));
//        }
//        /**----------------------初始化Log4j配置----------------------**/
//
//        /**-------------------初始化NettyClient配置-------------------**/
//        String nettyClientConfigPath = servletContextEvent.getServletContext().getInitParameter("nettyClientConfigLocation");
//        if(nettyClientConfigPath!=null && !nettyClientConfigPath.isEmpty()){
//            NettyClientConfig.initializeRedisConfig(servletContextEvent.getServletContext().getResourceAsStream(nettyClientConfigPath));
//        }
//        /**-------------------初始化NettyClient配置-------------------**/
//
//        /**-----------------------初始化站点配置-----------------------**/
//        String siteConfigPath = servletContextEvent.getServletContext().getInitParameter("siteConfigLocation");
//        if(siteConfigPath!=null && !siteConfigPath.isEmpty()){
//            SiteConfig.initializeSiteConfig(servletContextEvent.getServletContext().getResourceAsStream(siteConfigPath));
//        }
//        /**-----------------------初始化站点配置-----------------------**/
    }

    /**
     * 在服务销毁时执行
     * @param servletContextEvent
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

}
