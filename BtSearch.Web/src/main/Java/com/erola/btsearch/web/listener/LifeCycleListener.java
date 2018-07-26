package com.erola.btsearch.web.listener;

import com.erola.btsearch.util.jedis.JedisStaticConfig;
import com.erola.btsearch.util.log4j.Log4jConfig;
import com.erola.btsearch.util.mongodb.MongoDBConfig;
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
        /**----------------------初始化Log4j配置----------------------**/
        String log4jConfigPath = servletContextEvent.getServletContext().getInitParameter("log4jConfigLocation");
        if(log4jConfigPath!=null && !log4jConfigPath.isEmpty()){
            Log4jConfig.initializeConfig(servletContextEvent.getServletContext().getResourceAsStream(log4jConfigPath));
        }
        /**----------------------初始化Log4j配置----------------------**/

        /**----------------------初始化Jedis配置----------------------**/
        String jedisConfigPath = servletContextEvent.getServletContext().getInitParameter("jedisConfigLocation");
        if(jedisConfigPath!=null && !jedisConfigPath.isEmpty()){
            JedisStaticConfig.initializeConfig(servletContextEvent.getServletContext().getResourceAsStream(jedisConfigPath));
        }
        /**----------------------初始化Jedis配置----------------------**/

        /**-----------------------初始化Mongo配置---------------------**/
        String mongoConfigPath = servletContextEvent.getServletContext().getInitParameter("mongoConfigLocation");
        if(mongoConfigPath!=null && !mongoConfigPath.isEmpty()){
            MongoDBConfig.initializeConfig(servletContextEvent.getServletContext().getResourceAsStream(mongoConfigPath));
        }
        /**-----------------------初始化Mongo配置---------------------**/
    }

    /**
     * 在服务销毁时执行
     * @param servletContextEvent
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

}
