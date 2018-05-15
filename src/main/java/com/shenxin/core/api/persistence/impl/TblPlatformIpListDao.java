package com.shenxin.core.api.persistence.impl;

import com.shenxin.core.api.persistence.IpWhiteListDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @Author: gaobaozong
 * @Description: 白名单服务实现类
 * @Date: Created in 2017/9/18 - 15:43
 * @Version: V1.0
 */
@Named
@Slf4j
public class TblPlatformIpListDao implements IpWhiteListDao {

    @Inject
    Sql2o sql2o;

    @Override
    @Cacheable(value="run_cache",key="'isInTheList('+#ip+#platFormId+')'")
    public boolean isInTheList(String ip, String platFormId) {
        String sql = "select count(1) from TBL_PLATFORM_IP_LIST "
                +"where STATUS='N' and PLATFORM_ID=:id and WHITE_IP=:ip";
        try (Connection con = sql2o.open()) {
            Integer size = con.createQuery(sql)
                    .addParameter("id", platFormId)
                    .addParameter("ip", ip)
                    .executeScalar(Integer.class);
            if (size > 0) {
                return true;
            }
        } catch (Exception e) {
            log.error("ip[{}]获取白名单列表失败", ip, e);
        }
        return false;
    }
}
