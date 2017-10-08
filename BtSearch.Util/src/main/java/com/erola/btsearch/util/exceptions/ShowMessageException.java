package com.erola.btsearch.util.exceptions;

/**
 * 用于展示错误提示信息的Exception，方便在异常捕捉时对不同类型的错误进行区别处理
 * Created by Erola on 2017/9/21.
 */
public class ShowMessageException extends Exception {
    /**
     *
     * @param tipMessage
     */
    public ShowMessageException(String tipMessage){
        super(tipMessage);
    }
}