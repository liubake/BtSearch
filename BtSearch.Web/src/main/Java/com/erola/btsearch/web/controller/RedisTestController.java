package com.erola.btsearch.web.controller;

import com.erola.btsearch.util.jedis.JedisStaticHelper;
import com.erola.btsearch.web.model.AjaxResponseModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by Erola on 2017/9/30.
 */
@Controller
@RequestMapping(value = "/redis")
public class RedisTestController extends BaseController {
    /**
     * redis测试页面
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    @RequestMapping(value="/index", method = {RequestMethod.GET})
    public ModelAndView index() throws URISyntaxException, IOException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redis/index");
        return modelAndView;
    }

    /**
     * 从redis查询对应key的值
     * @param key
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/get", method = {RequestMethod.GET})
    public AjaxResponseModel<String> get(String key){
        if(key==null || key.trim().isEmpty())
            throw new IllegalArgumentException("key 不能为空");
        return AjaxResponseModel.getSuccessResponse(JedisStaticHelper.get(key));
    }

    /**
     * 删除redis中对应key的值
     * @param key
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/del", method = {RequestMethod.POST})
    public AjaxResponseModel<Long> del(String key){
        if(key==null || key.trim().isEmpty())
            throw new IllegalArgumentException("key 不能为空");
        return AjaxResponseModel.getSuccessResponse(JedisStaticHelper.del(key));
    }

    /**
     * 把对应key的值保存在redis中
     * @param key
     * @param value
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/add", method = {RequestMethod.POST})
    public AjaxResponseModel<String> add(String key, String value){
        if(key==null || key.trim().isEmpty())
            throw new IllegalArgumentException("key 不能为空");
        if(value==null || value.trim().isEmpty())
            throw new IllegalArgumentException("value 不能为空");
        return AjaxResponseModel.getSuccessResponse(JedisStaticHelper.set(key, value));
    }
}