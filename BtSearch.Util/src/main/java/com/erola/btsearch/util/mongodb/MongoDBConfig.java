/**
                                                                                    +
                lbk                                                                @@
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
    private int port = 27017;
    /**
     *服务器地址
     */
    private String address = "127.0.0.1";
    /**
     *数据库名称
     */
    private String databaseName="";
    /**
     * 连接数量
     */
    private int poolSize=10;
    /**
     *等待队列长度
     */
    private int blockSize=10;
    /**
     * 队列等待时间
     */
    private int blockWaittime=2000;
    /**
     * 连接超时时间
     */
    private int connectTimeout=2000;
    /**
     * 连接空闲时间
     */
    private int connectIdletime=2000;

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
     * 获取服务器地址
     * @return
     */
    public static String getAddress(){
        return getMongoDBConfigInstance().address;
    }

    /**
     * 获取数据库名称
     * @return
     */
    public static String getDatabaseName(){
        return getMongoDBConfigInstance().databaseName;
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
     * 初始化MongoDB 配置实例
     * @param propertiesFilePath
     */
    public static void initializeConfig(String propertiesFilePath) {
        try {
            FileInputStream propertiesStream = new FileInputStream(propertiesFilePath);
            initializeConfig(propertiesStream);
        } catch (FileNotFoundException e) {
            //这种配置初始化错误直接抛出去
            throw new RuntimeException(e);
        }
    }

    /**
     * 初始化MongoDB 配置实例
     * @param propertiesStream
     */
    public static void initializeConfig(InputStream propertiesStream){
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

        String databaseNameValue = propertiesConfig.getProperty("mongo.db.databaseName");
        if(databaseNameValue==null || databaseNameValue.trim().isEmpty()){
            throw new IllegalArgumentException("databaseName 配置不能为空");
        }
        mongoDBConfigInstance.databaseName = databaseNameValue.trim();
        String portValue = propertiesConfig.getProperty("mongo.db.port");
        if(portValue!=null && !portValue.trim().isEmpty()){
            mongoDBConfigInstance.port = Integer.parseInt(portValue.trim());
        }
        String addressValue = propertiesConfig.getProperty("mongo.db.address");
        if(addressValue!=null && !addressValue.trim().isEmpty()){
            mongoDBConfigInstance.address = addressValue.trim();
        }
        String poolSizeValue = propertiesConfig.getProperty("mongo.client.poolSize");
        if(poolSizeValue!=null && !poolSizeValue.trim().isEmpty()){
            mongoDBConfigInstance.poolSize = Integer.parseInt(poolSizeValue.trim());
        }
        String blockSizeValue = propertiesConfig.getProperty("mongo.client.blockSize");
        if(blockSizeValue!=null && !blockSizeValue.trim().isEmpty()){
            mongoDBConfigInstance.blockSize = Integer.parseInt(blockSizeValue.trim());
        }
        String blockWaittimeValue = propertiesConfig.getProperty("mongo.client.blockWaittime");
        if(blockWaittimeValue!=null && !blockWaittimeValue.trim().isEmpty()){
            mongoDBConfigInstance.blockWaittime = Integer.parseInt(blockWaittimeValue.trim());
        }
        String connectTimeoutValue = propertiesConfig.getProperty("mongo.client.connectTimeout");
        if(connectTimeoutValue!=null && !connectTimeoutValue.trim().isEmpty()){
            mongoDBConfigInstance.connectTimeout = Integer.parseInt(connectTimeoutValue.trim());
        }
        String connectIdletimeValue = propertiesConfig.getProperty("mongo.client.connectIdletime");
        if(connectIdletimeValue!=null && !connectIdletimeValue.trim().isEmpty()){
            mongoDBConfigInstance.connectIdletime = Integer.parseInt(connectIdletimeValue.trim());
        }
    }
}