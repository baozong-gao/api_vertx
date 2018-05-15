package com.shenxin.core.api.service.impl.dubbo;

import com.shenxin.core.account.facade.request.CommonRequestDTO;
import com.shenxin.core.account.facade.response.CommonResponseDTO;
import com.shenxin.core.account.facade.service.ICashierService;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @Author: gaobaozong
 * @Description: 收银服务
 * @Date: Created in 2017/10/13 - 16:54
 * @Version: V1.0
 */
@Named("ICashierService")
public class CashierService extends  UserCenterService{
    @Inject
    ICashierService cashierService;

    @Override
    public CommonResponseDTO run(CommonRequestDTO commonRequestDTO) {
        return cashierService.doCashierServiceCall(commonRequestDTO);
    }

}
