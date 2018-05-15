package com.shenxin.core.api.pojo.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: gaobaozong
 * @Description: 记录表
 * @Date: Created in 2017/9/18 - 15:33
 * @Version: V1.0
 */
@Data
public class TblAccessJournalPO implements Serializable {
    private String id;
    private String requestSource;      // 请求来源
    private String requestType;      // 请求类型
    private String requestTime;      // 请求时间
    private String requestBody;      // 请求体
    private String responseTime;      //  返回时间
    private String responseBody;      //  返回体
    private String responseStatus;      //  返回状态
    private String responseCode;      //  返回码
    private String responseMessage;      //  返回信息
    private String journalResv1;
    private String journalResv2;
    private String journalResv3;
    private String createDate;
    private String updateDate;
}
