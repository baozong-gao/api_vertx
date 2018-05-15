package com.shenxin.core.api.pojo.bo;

import lombok.Data;

/**
 * @Author: gaobaozong
 * @Description: vertx 内部传输应答
 * @Date: Created in 2017/9/19 - 13:31
 * @Version: V1.0
 */
@Data
public class ResponseBO {
    private String requestId;

    private String responseTime;      //  返回时间
    private String responseBody;      //  返回体
    private String responseStatus;      //  返回状态
    private String responseCode;      //  返回码
    private String responseMessage;      //  返回信息
}
