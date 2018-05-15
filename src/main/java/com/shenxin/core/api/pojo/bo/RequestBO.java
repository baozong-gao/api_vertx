package com.shenxin.core.api.pojo.bo;

import com.shenxin.core.api.pojo.dto.RequestDTO;
import lombok.Data;

/**
 * @Author: gaobaozong
 * @Description: vertx 内部传输请求
 * @Date: Created in 2017/9/19 - 13:30
 * @Version: V1.0
 */
@Data
public class RequestBO {

    private RequestDTO requestDTO;

    //请求ip，用于白名单校验
    private String ip;

    //日志
    private String id;

    //商户表
    private String requestSource;
}
