import com.erola.btsearch.model.TorrentInfo;
import com.erola.btsearch.service.interfaces.ITorrentInfoService;
import com.erola.btsearch.spider.config.SpiderConfig;
import com.erola.btsearch.spider.dht.server.DHTServer;
import com.erola.btsearch.util.jedis.JedisStaticConfig;
import com.erola.btsearch.util.log4j.Log4jConfig;
import com.erola.btsearch.util.mongodb.MongoDBConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import java.io.File;
import java.net.URLDecoder;

/**
 * Created by Erola on 2017/9/8.
 */
public class Spider {
    /**
     *
     * @param args
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        File currentFile = new File(Spider.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String rootDirectory = URLDecoder.decode(currentFile.getParentFile().getCanonicalPath(), "utf-8");

        /**
         * 初始化 Log4j 配置
         */
        String log4jConfig = rootDirectory + File.separator + "log4jconfig.properties";
        Log4jConfig.initializeConfig(log4jConfig);

        /**
         * 初始化 Jedis 配置
         */
        String jedisConfig = rootDirectory + File.separator + "jedisconfig.properties";
        JedisStaticConfig.initializeConfig(jedisConfig);

        /**
         * 初始化 Mongo 配置
         */
        String mongoConfig = rootDirectory + File.separator + "mongoconfig.properties";
        MongoDBConfig.initializeConfig(mongoConfig);

        /**
         * 初始化爬虫配置
         */
        String spiderConfig = rootDirectory + File.separator + "spiderconfig.properties";
        SpiderConfig.initializeSpiderConfig(spiderConfig);


        /*Log4jHelper.logDebug(Spider.class, "debug");
        Log4jHelper.logInfo(Spider.class, "info");
        Log4jHelper.logError(Spider.class, "error");
        Log4jHelper.logError(Spider.class, "error", new NullPointerException("error test"));
        Log4jHelper.logError(Spider.class, "error", new NullPointerException("error test111"));*/


        String contextConfig = rootDirectory + File.separator + "appcontext.xml";
        ApplicationContext applicationContext = new FileSystemXmlApplicationContext(contextConfig);
        ITorrentInfoService torrentInfoService = applicationContext.getBean("TorrentInfoService", ITorrentInfoService.class);
        /*//使用 @resource @autowired 自动注入需要该对象是由 spring 管理的
        JedisClientTemplate jedisClientTemplate = applicationContext.getBean("JedisClientTemplate", JedisClientTemplate.class);*/
        Thread dhtThread = new Thread(new DHTServer((TorrentInfo torrentInfo)->torrentInfoService.saveOrUpdate(torrentInfo)));
        dhtThread.run();
    }
}