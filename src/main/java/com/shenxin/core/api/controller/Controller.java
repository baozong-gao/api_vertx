package com.shenxin.core.api.controller;

import com.shenxin.core.api.pojo.bo.RequestBO;
import com.shenxin.core.api.pojo.bo.VertxMessage;
import com.shenxin.core.api.pojo.dto.RequestDTO;
import com.shenxin.core.api.pojo.dto.ResponseDTO;
import com.shenxin.core.api.service.ServiceHeader;
import com.shenxin.core.api.util.BeanUtil;
import com.shenxin.core.api.util.constants.SystemConstants;
import com.shenxin.core.api.util.exception.ParamException;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.Validator;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: gaobaozong
 * @Description: 提供http服务
 * @Date: Created in 2017/9/15 - 16:00
 * @Version: V1.0
 */
@Named
@Slf4j
public class Controller extends AbstractVerticle {

    private static final String ERROR_CODE = "error";

    @Inject
    @Named("validator")
    Validator validator;


    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.post("/api/allround").consumes("application/json").handler(ctx -> {
            RequestBO requestBO = new RequestBO();
            RequestDTO request = null;
            ResponseDTO response = new ResponseDTO();
            try {
                String body = ctx.getBodyAsString();
                request = BeanUtil.json2Object(body, RequestDTO.class);
                requestBO.setRequestDTO(request);

                String ip = ctx.request().getHeader("X-Forwarded-For");
                if (StringUtils.isEmpty(ip)) {
                    ip = ctx.request().remoteAddress().host();
                }
                requestBO.setIp(ip);

                BeanUtil.validateJSR303(validator, request);
            } catch (ParamException e) {
                response.setRequeststatus(ERROR_CODE);
                response.setResponsebody("参数不合法" + e.getMessage());
                ctx.response().putHeader("content-type", "application/json; charset:UTF-8").end(BeanUtil.object2Json(response));
            } catch (Exception e) {
                log.error("参数不合法", e);
                response.setRequeststatus(ERROR_CODE);
                response.setResponsebody("参数格式不合法");
                ctx.response().putHeader("content-type", "application/json; charset:UTF-8").end(BeanUtil.object2Json(response));
            }


            BeanUtils.copyProperties(request, response);
            VertxMessage message = new VertxMessage();
            message.setRequestBO(requestBO);

            vertx.eventBus().<VertxMessage>send(ServiceHeader.WHITE_LIST_SERVICE_ADDRESS, message,
                    result -> {
                        response.setResponsetime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

                        if (result.succeeded()) {
                            response.setResponsebody(result.result().body().getResponseBO().getResponseBody());
                            response.setRequeststatus(result.result().body().getResponseBO().getResponseStatus());
                            response.setResponseCode(result.result().body().getResponseBO().getResponseCode());
                            response.setResponseMessage(result.result().body().getResponseBO().getResponseMessage());
                        } else {
                            response.setResponsebody(result.cause().getMessage());
                            response.setRequeststatus(ERROR_CODE);
                        }

                        if (!ctx.response().headWritten())
                            ctx.response().putHeader("content-type", "application/json;charset:utf-8").end(BeanUtil.object2Json(response));

                    });
        });
        HttpServerOptions options = new HttpServerOptions();
        options.setMaxWebsocketFrameSize(10_0000);
        vertx.createHttpServer(options).requestHandler(router::accept).listen(SystemConstants.port);
    }
}
