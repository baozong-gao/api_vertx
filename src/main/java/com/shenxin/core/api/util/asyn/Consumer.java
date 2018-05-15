package com.shenxin.core.api.util.asyn;

import com.lmax.disruptor.WorkHandler;
import com.shenxin.core.api.Main;
import com.shenxin.core.api.persistence.AccessDao;

/**
 * @Author: gaobaozong
 * @Description: 消费者
 * @Date: Created in 2017/10/27 - 16:16
 * @Version: V1.0
 */
public class Consumer implements WorkHandler<Event> {

    @Override
    public void onEvent(Event event) throws Exception {
       Main.context.getBean("sql_log",AccessDao.class).insert(event.po);
    }
}
