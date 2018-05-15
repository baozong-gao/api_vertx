package com.shenxin.core.api.pojo.dto;

import lombok.Data;

/**
 * @Author: gaobaozong
 * @Description: 下游返回
 * @Date: Created in 2017/9/18 - 13:00
 * @Version: V1.0
 */
@Data
public class ResponseDTO {
    private String instid;
    private String agentid;
    private String appid;
    private String accesstoken;
    private String transcode;

    private String requeststatus;
    private String responsebody;
    private String responseCode;      //  返回码
    private String responseMessage;      //  返回信息
    private String responsetime;
}
