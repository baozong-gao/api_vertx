package com.shenxin.core.api.persistence;

import com.shenxin.core.api.pojo.po.TblAccessJournalPO;
import com.shenxin.core.api.util.exception.PersistException;

/**
 * @Author: gaobaozong
 * @Description: 日志接口
 * @Date: Created in 2017/9/18 - 15:38
 * @Version: V1.0
 */
public interface AccessDao {

    ThreadLocal<TblAccessJournalPO> localPo = new ThreadLocal<>();

    default void insert(TblAccessJournalPO po)  throws PersistException{
        localPo.set(po);
    }

    void update(TblAccessJournalPO po)  throws PersistException;

    default TblAccessJournalPO selectByKey(String key){
            return localPo.get();
    }

}
