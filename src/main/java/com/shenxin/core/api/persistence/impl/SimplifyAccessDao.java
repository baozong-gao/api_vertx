package com.shenxin.core.api.persistence.impl;

import com.shenxin.core.api.persistence.AccessDao;
import com.shenxin.core.api.pojo.po.TblAccessJournalPO;
import com.shenxin.core.api.util.exception.PersistException;
import lombok.extern.slf4j.Slf4j;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @Author: gaobaozong
 * @Description: 对日志进行简化
 * @Date: Created in 2017/10/27 - 11:17
 * @Version: V1.0
 */
@Slf4j
@Named("simp_sql_log")
public class SimplifyAccessDao implements AccessDao {

    @Inject
    Sql2o sql2o;

    @Override
    public void update(TblAccessJournalPO po) throws PersistException {
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

}
