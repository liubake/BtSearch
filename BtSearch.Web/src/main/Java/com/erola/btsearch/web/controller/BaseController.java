package com.erola.btsearch.web.controller;

import com.erola.btsearch.web.model.AjaxResponseModel;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Erola on 2017/9/30.
 */
public abstract class BaseController {


    //需要先判断请求类型 是 普通请求 还是 ajax请求
    /*@ResponseBody
    @ExceptionHandler
    public AjaxResponseModel<String> exception(HttpServletRequest request, Exception e) {
        //添加异常处理逻辑，如日志记录　　　


        return new AjaxResponseModel<String>(-1, e.getMessage(), "");
    }*/
}