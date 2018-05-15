package com.shenxin.core.api.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author: gaobaozong
 * @Description: 下游请求
 * @Date: Created in 2017/9/18 - 13:00
 * @Version: V1.0
 */
@Data
public class RequestDTO {
    @NotNull(message = "机构号不能为空")
    @Size(max = 8 , message = "机构号长度超限")
    private String instid;

    @NotNull(message = "客户号不能为空")
    @Size(max = 16, message = "客户号长度超限")
    private String agentid;

    @NotNull(message = "应用编号不能为空")
    @Size(max = 16 , message = "应用编号长度超限")
    private String appid;

    @NotNull(message = "授权编码不能为空")
    @Size(max = 16, message = "授权编码长度超限")
    private String accesstoken;

    @NotNull(message = "transcode不能为空")
    private String transcode;

    @NotNull(message = "请求体不能为空")
    @Size(max = 512, message = "请求体长度超限")
    private String requestbody;

    @NotNull(message = "请求时间不能为空")
    @Size(max = 14,min = 14, message = "请求时间格式为yyyymmddhhmmss")
    private String requesttime;
}
