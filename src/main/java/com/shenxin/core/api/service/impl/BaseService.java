package com.shenxin.core.api.service.impl;

import com.shenxin.core.api.pojo.bo.RequestBO;
import com.shenxin.core.api.pojo.bo.ResponseBO;
import org.springframework.beans.BeanUtils;

import java.util.Optional;

/**
 * @Author: gaobaozong
 * @Description: 服务接口
 * @Date: Created in 2017/9/19 - 17:22
 * @Version: V1.0
 */
public abstract class BaseService<T,V> {

    public final void exec(RequestBO request, ResponseBO  responseBO) {
        T t = convertRequest(request);
        V v = run(t);
        ResponseBO response = Optional.ofNullable(convertResponse(v)).orElseGet(() ->{
            ResponseBO temp = new ResponseBO();
            temp.setRequestId(request.getId());
            temp.setResponseStatus("I");
            temp.setResponseBody("状态未知");
            return temp;
        });
        BeanUtils.copyProperties(response, responseBO);
    }

    public abstract T convertRequest(RequestBO request);

    public abstract ResponseBO convertResponse(V response);

    public abstract V run(T t);

}
