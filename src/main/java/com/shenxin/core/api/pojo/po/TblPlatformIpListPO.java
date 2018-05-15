package com.shenxin.core.api.pojo.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: gaobaozong
 * @Description: ip白名单
 * @Date: Created in 2017/9/18 - 15:30
 * @Version: V1.0
 */
@Data
public class TblPlatformIpListPO  implements Serializable {

    private String platformId;      //开发者接入平台 id
    private String whiteIp;      //IP白名单
    private String status;      //N 正常 C 关闭
}
