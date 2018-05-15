package com.shenxin.core.api.util.hystrix;


import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
/**
 * @Author: gaobaozong
 * @Description: dubbo filter
 * @Date: Created in 2017/10/31 - 13:20
 * @Version: V1.0
 */
@Activate(group = Constants.CONSUMER)
public class HystrixFilter implements Filter {

    @Override
    public Result invoke(Invoker invoker, Invocation invocation) throws RpcException {
        DubboHystrixCommand command = new DubboHystrixCommand(invoker, invocation);
        return command.execute();
    }

}