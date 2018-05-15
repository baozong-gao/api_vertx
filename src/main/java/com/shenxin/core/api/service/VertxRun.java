package com.shenxin.core.api.service;

import com.shenxin.core.api.controller.Controller;
import com.shenxin.core.api.pojo.bo.MessageCodec;
import com.shenxin.core.api.pojo.bo.VertxMessage;
import com.shenxin.core.api.util.constants.ServiceConfigUtils;
import com.shenxin.core.api.util.constants.SystemConstants;
import com.shenxin.core.api.util.hystrix.Monitor;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.ServiceHelper;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.VertxFactory;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

/**
 * @Author: gaobaozong
 * @Description: 运行类
 * @Date: Created in 2017/9/18 - 10:56
 * @Version: V1.0
 */
@Named
@Slf4j
public class VertxRun {

    @Inject
    @Named("vertxService")
    ServiceHelper vertxService;
    @Inject
    ServiceHeader service;
    @Inject
    Controller http;
    @Inject
    Monitor monitor;

    @Inject
    ServiceConfigUtils serviceConfigUtils;

    @PostConstruct
    public void run() {
        VertxFactory factory = (VertxFactory) vertxService.loadFactory(VertxFactory.class);
        Vertx vertx = factory.vertx(new VertxOptions().setWorkerPoolSize(SystemConstants.vertx_work_pool));
        vertx.eventBus().registerDefaultCodec(VertxMessage.class, new MessageCodec());

        DeploymentOptions options = new DeploymentOptions().setWorker(true);
        for (int i = 0; i < SystemConstants.service_thread_count; i++)
            vertx.deployVerticle(service, options, result -> Optional.ofNullable(result).filter(re -> result.succeeded()).ifPresent(re -> log.info("service 注册成功")));
        for (int i = 0; i< SystemConstants.web_thread_count; i++)
            vertx.deployVerticle(http, options, result -> Optional.ofNullable(result).filter(re -> result.succeeded()).ifPresent(re -> log.info("http服务 注册成功")));

        //vertx.deployVerticle(monitor, options, result -> Optional.ofNullable(result).filter(re -> result.succeeded()).ifPresent(re -> log.info("monitor服务 注册成功")));


        vertx.setPeriodic(60_000*5, id-> serviceConfigUtils.loadYaml());

    }
}
