package com.shenxin.core.api.persistence.impl;

import com.shenxin.core.api.persistence.AccessDao;
import com.shenxin.core.api.pojo.po.TblAccessJournalPO;
import com.shenxin.core.api.util.exception.PersistException;
import lombok.extern.slf4j.Slf4j;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @Author: gaobaozong
 * @Description: 日志实现类
 * @Date: Created in 2017/9/18 - 16:43
 * @Version: V1.0
 */
@Named("sql_log")
@Slf4j
public class TblAccessJournalDao implements AccessDao {

    @Inject
    Sql2o sql2o;

    @Override
    public void insert(TblAccessJournalPO po) throws PersistException {
        String sql = "insert into TBL_ACCESS_JOURNAL(ID,REQUEST_SOURCE,REQUEST_TYPE,REQUEST_TIME,REQUEST_BODY,RESPONSE_TIME,RESPONSE_BODY,RESPONSE_STATUS,RESPONSE_CODE,RESPONSE_MESSAGE,JOURNAL_RESV1,JOURNAL_RESV2,JOURNAL_RESV3)"
                + "values(:id,:requestSource,:requestType,:requestTime,:requestBody,:responseTime,:responseBody,:responseStatus,:responseCode,:responseMessage,:journalResv1,:journalResv2,:journalResv3)";

        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", po.getId())
                    .addParameter("requestSource", po.getRequestSource())
                    .addParameter("requestType", po.getRequestType())
                    .addParameter("requestTime", po.getRequestTime())
                    .addParameter("requestBody", po.getRequestBody())
                    .addParameter("responseTime", po.getResponseTime())
                    .addParameter("responseBody", po.getResponseBody())
                    .addParameter("responseStatus", po.getResponseStatus())
                    .addParameter("responseCode", po.getResponseCode())
                    .addParameter("responseMessage", po.getResponseMessage())
                    .addParameter("journalResv1", po.getJournalResv1())
                    .addParameter("journalResv2", po.getJournalResv2())
                    .addParameter("journalResv3", po.getJournalResv3())
                    .executeUpdate();
        } catch (Exception e) {
            log.error("插入日志失败：【{}】", po.toString(), e);
            throw new PersistException();
        }
    }

    @Override
    public void update(TblAccessJournalPO po) throws PersistException {
        String sql = "update TBL_ACCESS_JOURNAL set REQUEST_SOURCE=:requestSource,REQUEST_TYPE=:requestType,REQUEST_TIME=:requestTime,REQUEST_BODY=:requestBody,RESPONSE_TIME=:responseTime,RESPONSE_BODY=:responseBody,RESPONSE_STATUS=:responseStatus,RESPONSE_CODE=:responseCode,RESPONSE_MESSAGE=:responseMessage,JOURNAL_RESV1=:journalResv1,JOURNAL_RESV2=:journalResv2,JOURNAL_RESV3=:journalResv3,update_date=:updateDate"
                + " where id=:id";

        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", po.getId())
                    .addParameter("requestSource", po.getRequestSource())
                    .addParameter("requestType", po.getRequestType())
                    .addParameter("requestTime", po.getRequestTime())
                    .addParameter("requestBody", po.getRequestBody())
                    .addParameter("responseTime", po.getResponseTime())
                    .addParameter("responseBody", po.getResponseBody())
                    .addParameter("responseStatus", po.getResponseStatus())
                    .addParameter("responseCode", po.getResponseCode())
                    .addParameter("responseMessage", po.getResponseMessage())
                    .addParameter("journalResv1", po.getJournalResv1())
                    .addParameter("journalResv2", po.getJournalResv2())
                    .addParameter("journalResv3", po.getJournalResv3())
                    .addParameter("updateDate", new Timestamp(new Date().getTime()))
                    .executeUpdate();
        } catch (Exception e) {
            log.error("更新日志失败：【{}】", po.toString(), e);
            throw new PersistException();
        }

    }

    @Override
    public TblAccessJournalPO selectByKey(String key) {
        String sql = "select ID as id,REQUEST_SOURCE as requestSource,REQUEST_TYPE as requestType,REQUEST_TIME as requestTime,REQUEST_BODY as requestBody,RESPONSE_TIME as responseTime,RESPONSE_BODY as responseBody,RESPONSE_STATUS as responseStatus,RESPONSE_CODE as responseCode,RESPONSE_MESSAGE as responseMessage,JOURNAL_RESV1 as journalResv1,JOURNAL_RESV2 as journalResv2,JOURNAL_RESV3 as journalResv3,create_date as createDate,update_date as updateDate from TBL_ACCESS_JOURNAL "
                + "where id=:id";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id", key)
                    .executeAndFetchFirst(TblAccessJournalPO.class);
        } catch (Exception e) {
            log.error("获取日志信息失败，field:key[{}]", key, e);
        }
        return null;
    }
}
