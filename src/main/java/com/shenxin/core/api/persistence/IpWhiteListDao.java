package com.shenxin.core.api.persistence;

/**
 * @Author: gaobaozong
 * @Description: 白名单服务类
 * @Date: Created in 2017/9/18 - 15:47
 * @Version: V1.0
 */
public interface IpWhiteListDao {

    boolean isInTheList(String ip, String platFormId);

}
