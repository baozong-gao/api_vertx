package com.shenxin.core.api.util.exception;

/**
 * @Author: gaobaozong
 * @Description: 请求参数校验异常
 * @Date: Created in 2017/9/20 - 11:20
 * @Version: V1.0
 */
public class ParamException extends Exception {

    public ParamException(String msg){
        super(msg);
    }
}
