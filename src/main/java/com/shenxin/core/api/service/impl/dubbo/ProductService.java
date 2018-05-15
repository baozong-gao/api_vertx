package com.shenxin.core.api.service.impl.dubbo;

import com.shenxin.core.account.facade.request.CommonRequestDTO;
import com.shenxin.core.account.facade.response.CommonResponseDTO;
import com.shenxin.core.account.facade.service.IProductService;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @Author: gaobaozong
 * @Description: 产品服务
 * @Date: Created in 2017/10/12 - 13:27
 * @Version: V1.0
 */
@Named("IProductService")
public class ProductService extends  UserCenterService{
    @Inject
    IProductService productService;

    @Override
    public CommonResponseDTO run(CommonRequestDTO commonRequestDTO) {
        return productService.doProdServiceCall(commonRequestDTO);
    }

}
