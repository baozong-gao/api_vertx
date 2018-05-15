package com.shenxin.core.api.service.impl.dubbo;

import com.shenxin.core.account.facade.request.CommonRequestDTO;
import com.shenxin.core.account.facade.response.CommonResponseDTO;
import com.shenxin.core.account.facade.service.ISettleService;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @Author: gaobaozong
 * @Description: 批量订单服务
 * @Date: Created in 2017/10/12 - 13:30
 * @Version: V1.0
 */
@Named("ISettleService")
public class SettleService extends  UserCenterService{
    @Inject
    ISettleService settleService;

    @Override
    public CommonResponseDTO run(CommonRequestDTO commonRequestDTO) {
        return settleService.doSettleServiceCall(commonRequestDTO);
    }

}