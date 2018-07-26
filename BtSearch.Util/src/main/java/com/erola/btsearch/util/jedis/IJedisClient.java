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
 * jedis客户端操作接口
 * Created by Erola on 2018/7/24.
 */
public interface IJedisClient {
    /**
     * 判断指定的key是否存在
     * @param key
     * @return
     */
    Boolean exists(String key);

    /**
     * 删除指定的key
     * @param key
     * @return
     */
    Long del(String key);

    /**
     * 删除多个指定的key
     * @param keys
     * @return
     */
    Long del(String... keys);

    /**
     * 查询指定key的值
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 设置key对应的值
     * @param key
     * @param value
     * @return
     */
    String set(String key, String value);

    /**
     * 设置key对应的值并设置过期时间
     * @param key
     * @param value
     * @param expireSeconds
     * @return
     */
    String setex(String key, String value, int expireSeconds);

    /**
     * 查询指定的key，并用json反序列化为指定的对象
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T get(String key, Class<T> clazz) throws IOException;

    /**
     * 设置key对应的，对象json序列化为字符串之后的值
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    <T> String set(String key, T value) throws JsonProcessingException;

    /**
     * 设置key对应的，对象json序列化为字符串之后的值并设置过期时间
     * @param key
     * @param value
     * @param expireSeconds
     * @param <T>
     * @return
     */
    <T> String setex(String key, T value, int expireSeconds) throws JsonProcessingException;
}
