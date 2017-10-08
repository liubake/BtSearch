package com.erola.btsearch.util.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erola on 2017/9/5.
 */
public class JsonHelper {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //设置可用单引号
        objectMapper.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        //设置字段可以不用双引号包括
        objectMapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        //序列化时设置时间格式
        //objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        //反序列化时设置实体无属性和json串属性对应时不会出错
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    }

    /**
     *
     * @param t
     * @param <T>
     * @return
     * @throws JsonProcessingException
     */
    public  static <T>  String objectToJson(T t) throws JsonProcessingException {
        if(t != null)
            return objectMapper.writeValueAsString(t);
        else
            return null;
    }

    /**
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T jsonToObject(String json, Class<T> clazz) throws IOException {
        if(json != null && !json.isEmpty())
            return objectMapper.readValue(json, clazz);
        else
            return null;
    }

    /**
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> List<T> jsonToObjectArrayList(String json, Class<T> clazz) throws IOException {
        if(json != null && !json.isEmpty())
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructParametricType(ArrayList.class, clazz));
        else
            return null;
    }
}