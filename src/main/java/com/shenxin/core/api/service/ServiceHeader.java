package com.shenxin.core.api.service;

import com.shenxin.core.api.persistence.AccessDao;
import com.shenxin.core.api.persistence.IpWhiteListDao;
import com.shenxin.core.api.persistence.PlatformDao;
import com.shenxin.core.api.pojo.ServiceConfig;
import com.shenxin.core.api.pojo.bo.RequestBO;
import com.shenxin.core.api.pojo.bo.ResponseBO;
import com.shenxin.core.api.pojo.bo.VertxMessage;
import com.shenxin.core.api.pojo.dto.RequestDTO;
import com.shenxin.core.api.pojo.po.TblAccessJournalPO;
import com.shenxin.core.api.pojo.po.TblSysPlatformPO;
import com.shenxin.core.api.service.impl.BaseService;
import com.shenxin.core.api.util.SpringUtil;
import com.shenxin.core.api.util.constants.ServiceConfigUtils;
import com.shenxin.core.api.util.exception.SystemException;
import io.vertx.core.AbstractVerticle;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;
import java.util.UUID;

/**
 * @Author: gaobaozong
 * @Description: 业务类
 * @Date: Created in 2017/9/15 - 15:55
 * @Version: V1.0
 */
@Named
@Slf4j
public class ServiceHeader extends AbstractVerticle {

    @Inject
    IpWhiteListDao ipWhiteListDao;

    @Inject
    PlatformDao platformDao;

    @Inject
    @Named("asyn_log")
    AccessDao accessDao;

    @Inject
    ServiceConfigUtils serviceConfigUtils;

    @Inject
    SpringUtil springUtil;

    public static final String WHITE_LIST_SERVICE_ADDRESS = "White_List_service_address";

    @Override
    public void start() throws Exception {
        vertx.eventBus().<VertxMessage>consumer(WHITE_LIST_SERVICE_ADDRESS, msg -> {
            VertxMessage message = msg.body();
            RequestBO requestBo = message.getRequestBO();

            vertx.executeBlocking(future -> {
                //token 校验
                RequestDTO request = requestBo.getRequestDTO();
                TblSysPlatformPO tblSysPlatformPO = platformDao.selectByUniqueKey(request.getInstid(), request.getAgentid(), request.getAppid());
                if (tblSysPlatformPO == null) {
                    future.fail("商户未注册");
                    return;
                } else if (!tblSysPlatformPO.getAccessToken().equals(request.getAccesstoken())) {
                    future.fail("商户token校验失败");
                    return;
                }
                //白名单 校验
                boolean inTheList = ipWhiteListDao.isInTheList(requestBo.getIp(), tblSysPlatformPO.getId());
                if (!inTheList) {
                    future.fail("商户ip未加入白名单");
                    log.warn("商户ip[{}]未加入白名单", requestBo.getIp());
                    return;
                }
                //记录日志
                String accessLogId = UUID.randomUUID().toString().replaceAll("-", "");
                try {
                    TblAccessJournalPO tblAccessJournalPO = new TblAccessJournalPO();
                    tblAccessJournalPO.setRequestBody(request.getRequestbody());
                    tblAccessJournalPO.setRequestSource(tblSysPlatformPO.getId());
                    tblAccessJournalPO.setRequestTime(request.getRequesttime());
                    tblAccessJournalPO.setRequestType(request.getTranscode());
                    tblAccessJournalPO.setId(accessLogId);
                    accessDao.insert(tblAccessJournalPO);
                } catch (Exception e) {
                    future.fail("系统异常");
                    return;
                }
                //调用服务
                final ResponseBO responseBO = new ResponseBO();
                try {
                    ServiceConfig config = serviceConfigUtils.getConfig(request.getTranscode());
                    Optional.ofNullable(config).orElseThrow(() -> {
                        responseBO.setRequestId(accessLogId);
                        responseBO.setResponseStatus("F");
                        responseBO.setResponseBody("transcode 服务配置未找到");
                        return new SystemException("服务配置未找到");
                    });
                    BaseService service = springUtil.getBean(config.getName(), BaseService.class);
                    requestBo.setId(accessLogId);
                    requestBo.setRequestSource(tblSysPlatformPO.getId());
                    service.exec(requestBo, responseBO);
                } catch (SystemException e) {
                    log.error("调用后台服务异常，参数【{}】", requestBo.toString(), e);
                } catch (Exception e) {
                    log.error("调用后台服务异常，参数【{}】", requestBo.toString(), e);
                    future.fail("调用后台服务异常");
                    return;
                }

                //更新日志
                try {
                    TblAccessJournalPO tblAccessJournalPO = accessDao.selectByKey(responseBO.getRequestId());
                    Optional.ofNullable(tblAccessJournalPO).orElseThrow(() -> new SystemException("未找到日志"));
                    tblAccessJournalPO.setResponseBody(responseBO.getResponseBody());
                    tblAccessJournalPO.setResponseStatus(responseBO.getResponseStatus());
                    tblAccessJournalPO.setResponseMessage(responseBO.getResponseMessage());
                    tblAccessJournalPO.setResponseCode(responseBO.getResponseCode());
                    tblAccessJournalPO.setResponseTime(responseBO.getResponseTime());
                    accessDao.update(tblAccessJournalPO);
                } catch (Exception e) {
                    log.error("更新操作日志失败", e);
                }

                message.setResponseBO(responseBO);
                future.complete(message);
            },false,  result -> {
                if (result.succeeded()) {
                    msg.reply(result.result());
                }
                if (result.failed()) {
                    msg.fail(400, result.cause().getMessage());
                }
            });
        });
    }
}
