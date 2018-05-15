package com.shenxin.core.api.util.asyn;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;
import com.shenxin.core.api.util.constants.SystemConstants;

import javax.inject.Named;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Author: gaobaozong
 * @Description: 事件处理类
 * @Date: Created in 2017/10/27 - 16:16
 * @Version: V1.0
 */
@Named
public class AsynHeader {
    ExecutorService executors = Executors.newCachedThreadPool();
    public RingBuffer<Event> ringBuffer =
            RingBuffer.create(ProducerType.MULTI,Event::new,
                    1024 * 1024,
                    new TimeoutBlockingWaitStrategy(1, TimeUnit.MILLISECONDS));


    public AsynHeader() {
        SequenceBarrier barrier = ringBuffer.newBarrier();

        Consumer[] consumers = new Consumer[SystemConstants.asyn_thread_count];
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new Consumer();
        }

        WorkerPool<Event> workerPool =
                new WorkerPool<Event>(ringBuffer,
                        barrier,
                        new IntEventExceptionHandler(),
                        consumers);
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
        workerPool.start(executors);
    }


}

class IntEventExceptionHandler implements ExceptionHandler {
    @Override
    public void handleEventException(Throwable arg0, long arg1, Object arg2) {
    }

    @Override
    public void handleOnShutdownException(Throwable arg0) {
    }

    @Override
    public void handleOnStartException(Throwable arg0) {
    }
}
