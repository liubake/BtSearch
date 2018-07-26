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
package com.erola.btsearch.util.jedis;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;

/**
 * 使用spring管理的jedis 客户端方法模板
 * 支持根据配置注入redis单机或者集群
 * Created by Erola on 2018/7/24.
 */
public class JedisClientTemplate implements IJedisClient {
    /**
     * JedisClient 根据配置文件中注入的类型返回单机或集群的 IJedisClient
     */
    private IJedisClient jedisClient;

    public void setJedisClient(IJedisClient jedisClient) {
        this.jedisClient = jedisClient;
    }


    @Override
    public Boolean exists(String key) {
        return jedisClient.exists(key);
    }

    @Override
    public Long del(String key) {
        return jedisClient.del(key);
    }

    @Override
    public Long del(String... keys) {
        return jedisClient.del(keys);
    }

    @Override
    public String get(String key) {
        return jedisClient.get(key);
    }

    @Override
    public String set(String key, String value) {
        return jedisClient.set(key, value);
    }

    @Override
    public String setex(String key, String value, int expireSeconds) {
        return jedisClient.setex(key, value, expireSeconds);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) throws IOException {
        return jedisClient.get(key, clazz);
    }

    @Override
    public <T> String set(String key, T value) throws JsonProcessingException {
        return jedisClient.set(key, value);
    }

    @Override
    public <T> String setex(String key, T value, int expireSeconds) throws JsonProcessingException {
        return jedisClient.setex(key, value, expireSeconds);
    }
}