package com.shenxin.core.api.util.hystrix;


import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.netflix.hystrix.*;
import com.shenxin.core.api.util.constants.SystemConstants;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: gaobaozong
 * @Description: 雪崩管理
 * @Date: Created in 2017/10/31 - 13:15
 * @Version: V1.0
 */
@Slf4j
public class DubboHystrixCommand extends HystrixCommand<Result> {

    private static final int DEFAULT_THREADPOOL_CORE_SIZE = SystemConstants.dubbo_thread_count;
    private Invoker       invoker;
    private Invocation       invocation;

    public DubboHystrixCommand(Invoker invoker,Invocation invocation){
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(invoker.getInterface().getName()))
                .andCommandKey(HystrixCommandKey.Factory.asKey(String.format("%s_%d", invocation.getMethodName(),
                        invocation.getArguments() == null ? 0 : invocation.getArguments().length)))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withCircuitBreakerRequestVolumeThreshold(20)
                        .withCircuitBreakerSleepWindowInMilliseconds(3000)
                        .withCircuitBreakerErrorThresholdPercentage(50)
                        .withExecutionTimeoutEnabled(false))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(getThreadPoolCoreSize(invoker.getUrl()))));//线程池为30


        this.invoker=invoker;
        this.invocation=invocation;
    }

    /**
     * 获取线程池大小
     *
     * @param url
     * @return
     */
    private static int getThreadPoolCoreSize(URL url) {
        if (url != null) {
            int size = url.getParameter("ThreadPoolCoreSize", DEFAULT_THREADPOOL_CORE_SIZE);
            if (log.isDebugEnabled()) {
                log.debug("ThreadPoolCoreSize:" + size);
            }
            return size;
        }

        return DEFAULT_THREADPOOL_CORE_SIZE;

    }

    @Override
    protected Result run() throws Exception {
        return invoker.invoke(invocation);
    }
}