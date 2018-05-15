package com.shenxin.core.api.persistence;

import com.shenxin.core.api.pojo.po.TblSysPlatformPO;

/**
 * @Author: gaobaozong
 * @Description: 商户服务接口
 * @Date: Created in 2017/9/18 - 16:17
 * @Version: V1.0
 */
public interface PlatformDao {

    TblSysPlatformPO selectByUniqueKey(String instId, String agentId, String appId);

}
