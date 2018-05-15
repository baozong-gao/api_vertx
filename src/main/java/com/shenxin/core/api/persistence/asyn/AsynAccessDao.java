package com.shenxin.core.api.persistence.asyn;

import com.shenxin.core.api.persistence.AccessDao;
import com.shenxin.core.api.pojo.po.TblAccessJournalPO;
import com.shenxin.core.api.util.asyn.Producer;
import com.shenxin.core.api.util.asyn.AsynHeader;
import com.shenxin.core.api.util.exception.PersistException;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @Author: gaobaozong
 * @Description: 异步日志
 * @Date: Created in 2017/10/27 - 18:36
 * @Version: V1.0
 */
@Slf4j
@Named("asyn_log")
public class AsynAccessDao implements AccessDao{

    @Inject
    AsynHeader asynHeader;

    ThreadLocal<Producer> local = new ThreadLocal<>();


    @Override
    public void update(TblAccessJournalPO po) throws PersistException {
        Producer producer =  local.get();
        if(producer == null){
            producer = new Producer(asynHeader.ringBuffer);
            local.set(producer);
        }
        producer.onData(po);
    }
}
