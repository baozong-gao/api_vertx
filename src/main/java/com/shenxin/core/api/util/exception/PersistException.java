package com.shenxin.core.api.util.exception;

/**
 * @Author: gaobaozong
 * @Description: 持久数据异常
 * @Date: Created in 2017/9/18 - 17:17
 * @Version: V1.0
 */
public class PersistException extends Exception {

    public PersistException(){
        super();
    }

    public PersistException(String msg){
        super(msg);
    }
}
