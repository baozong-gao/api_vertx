package com.shenxin.core.api.service.impl.dubbo;

import com.shenxin.core.api.pojo.bo.RequestBO;
import com.shenxin.core.api.pojo.bo.ResponseBO;
import com.shenxin.core.api.service.impl.BaseService;
import com.shenxin.core.bsp.facade.request.CommonRequestDTO;
import com.shenxin.core.bsp.facade.response.CommonResponseDTO;
import com.shenxin.core.bsp.facade.service.IMessageService;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @Author: gaobaozong
 * @Description: 消息服务
 * @Date: Created in 2017/9/26 - 13:27
 * @Version: V1.0
 */
@Named("IMessageService")
public class MessageService extends BaseService<CommonRequestDTO,CommonResponseDTO> {
    @Inject
    IMessageService iMessageService;

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

    @Override
    public CommonResponseDTO run(CommonRequestDTO commonRequestDTO) {
        return iMessageService.doMessageServiceCall(commonRequestDTO);
    }

}
