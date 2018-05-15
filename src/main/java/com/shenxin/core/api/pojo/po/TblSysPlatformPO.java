package com.shenxin.core.api.pojo.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: gaobaozong
 * @Description: 商户表
 * @Date: Created in 2017/9/18 - 15:28
 * @Version: V1.0
 */
@Data
public class TblSysPlatformPO  implements Serializable {

    private String id;//UUID KEY
    private String instId;//机构号
    private String agentId;//客户号
    private String appId;//应用编号
    private String appType;//应用类型 PAYMENT, BASIS
    private String appName;//应用名称
    private String appDesc;//应用描述
    private String accessToken;//授权编码
    private String secretKey;//加密KEY
    private String platformStatus;//状态: I, N
    private String platformResv1;//平台扩展字段1
    private String platformResv2;//平台扩展字段2
    private String platformResv3;//平台扩展字段3

}
