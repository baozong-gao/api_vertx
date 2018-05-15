package com.shenxin.core.api.persistence.impl;

import com.shenxin.core.api.persistence.PlatformDao;
import com.shenxin.core.api.pojo.po.TblSysPlatformPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @Author: gaobaozong
 * @Description: 商户服务实现
 * @Date: Created in 2017/9/18 - 16:21
 * @Version: V1.0
 */
@Named
@Slf4j
public class TblSysPlatformDao implements PlatformDao {

    @Inject
    Sql2o sql2o;


    @Override
    @Cacheable(value="run_cache", key="'selectByUniqueKey('+#instId+#agentId+#appId+')'")
    public TblSysPlatformPO selectByUniqueKey(String instId, String agentId, String appId) {
        String sql = "select  ID as id, INST_ID as instId, AGENT_ID as agentId, APP_ID as appId, APP_TYPE as appType, APP_NAME as appName, APP_DESC as appDesc, ACCESS_TOKEN as accessToken, SECRET_KEY as secretKey, PLATFORM_STATUS as platformStatus, PLATFORM_RESV1 as platformResv1, PLATFORM_RESV2 as platformResv2, PLATFORM_RESV3 as platformResv3 from TBL_SYS_PLATFORM "
                +"where  INST_ID=:instId and AGENT_ID=:agentId and APP_ID=:appId";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("instId", instId)
                    .addParameter("agentId", agentId)
                    .addParameter("appId", appId)
                    .executeAndFetchFirst(TblSysPlatformPO.class);
        } catch (Exception e) {
            log.error("获取商户信息失败，field:instId[{}],agentId[{}],appId[{}]", instId, agentId, appId, e);
        }
        return null;
    }
}
