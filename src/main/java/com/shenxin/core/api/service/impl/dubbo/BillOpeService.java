package com.shenxin.core.api.service.impl.dubbo;

import com.shenxin.core.api.pojo.bo.RequestBO;
import com.shenxin.core.api.pojo.bo.ResponseBO;
import com.shenxin.core.api.service.impl.BaseService;
import com.shenxin.core.publicfee.facade.request.CommonRequestDTO;
import com.shenxin.core.publicfee.facade.response.CommonResponseDTO;
import com.shenxin.core.publicfee.facade.service.IBillOpeService;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @Author: gaobaozong
 * @Description:  缴费
 * @Date: Created in 2017/10/30 - 15:49
 * @Version: V1.0
 */
@Named("IBillOpeService")
public class BillOpeService extends BaseService<CommonRequestDTO,CommonResponseDTO> {
    @Inject
    IBillOpeService iBillOpeService;

    @Override
    public CommonResponseDTO run(CommonRequestDTO commonRequestDTO) {
        return iBillOpeService.doBillOpeServiceCall(commonRequestDTO);
    }

    @Override
    public CommonRequestDTO convertRequest(RequestBO request) {
        CommonRequestDTO dto = new CommonRequestDTO();
        dto.setRequestId(request.getId());
        dto.setRequestTransCode(request.getRequestDTO().getTranscode());
        dto.setRequestSource(request.getRequestSource());
        dto.setRequestBody(request.getRequestDTO().getRequestbody());
        dto.setRequestTime(request.getRequestDTO().getRequesttime());
        return dto;
    }

    @Override
    public ResponseBO convertResponse(CommonResponseDTO response) {
        ResponseBO bo = new ResponseBO();
        bo.setRequestId(response.getRequestId());
        bo.setResponseBody(response.getResponseBody());
        bo.setResponseStatus(response.getRequestStatus());
        bo.setResponseTime(response.getResponseTime());
        bo.setResponseCode(response.getResponseCode());
        bo.setResponseMessage(response.getResponseMessage());
        return bo;
    }
}
