package com.shenxin.core.api.util.asyn;

import com.lmax.disruptor.RingBuffer;
import com.shenxin.core.api.pojo.po.TblAccessJournalPO;

/**
 * @Author: gaobaozong
 * @Description: 生产者
 * @Date: Created in 2017/10/27 - 16:16
 * @Version: V1.0
 */
public class Producer {
    private final RingBuffer<Event> ringBuffer;

    public Producer(RingBuffer<Event> ringBuffer){
        this.ringBuffer=ringBuffer;
    }

    public void onData(TblAccessJournalPO data){
        long sequence = ringBuffer.next();
        try {
            Event event = ringBuffer.get(sequence);
            event.po = data;
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            ringBuffer.publish(sequence);
        }
    }
}
