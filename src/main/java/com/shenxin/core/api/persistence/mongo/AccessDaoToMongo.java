package com.shenxin.core.api.persistence.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.shenxin.core.api.persistence.AccessDao;
import com.shenxin.core.api.pojo.po.TblAccessJournalPO;
import com.shenxin.core.api.util.MongoDBUtil;
import com.shenxin.core.api.util.exception.PersistException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.inject.Named;
import java.util.Optional;

/**
 * @Author: gaobaozong
 * @Description: 记录操作日志
 * @Date: Created in 2017/10/26 - 17:52
 * @Version: V1.0
 */
@Named("mongo_log")
@Slf4j
public class AccessDaoToMongo implements AccessDao {

    String dbName = "LOG_DB";

    String collName = "api";

    MongoCollection<Document> coll = MongoDBUtil.instance.getCollection(dbName, collName);

    @Override
    public void insert(TblAccessJournalPO po) throws PersistException {
        try {
            Document document = new Document();
            document.putAll(BeanUtils.describe(po));
            coll.insertOne(document);
        } catch (Exception e) {
            log.error("插入日志失败：【{}】", po.toString(), e);
            throw new PersistException();
        }
    }

    @Override
    public void update(TblAccessJournalPO po) throws PersistException {
        try {
            Document document = new Document();
            document.putAll(BeanUtils.describe(po));
            Bson filter = Filters.eq("id", po.getId());
            coll.updateOne(filter, new Document("$set", document));
        } catch (Exception e) {
            log.error("更新日志失败：【{}】", po.toString(), e);
            throw new PersistException();
        }

    }

    @Override
    public TblAccessJournalPO selectByKey(String key) {

        Bson filter = Filters.eq("id", key);
        MongoCursor<Document> c = MongoDBUtil.instance.find(coll, filter);
        return Optional.ofNullable(c)
                .filter(_c -> _c.hasNext())
                .map(_c1 -> _c1.next())
                .map(document -> {
                    try {
                        TblAccessJournalPO po = new TblAccessJournalPO();
                        BeanUtils.populate(po, document);
                        return po;
                    } catch (Exception e) {
                        log.error("获取日志失败 {}", document.toJson());
                    }
                    return null;
                }).orElse(null);
    }
}
