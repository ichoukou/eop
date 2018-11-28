package net.ytoec.kernel.service.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import net.ytoec.kernel.util.JDBCUtilSingle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class QuestionnaireCompensateHelper {

    private static Logger logger=LoggerFactory.getLogger(QuestionnaireCompensateHelper.class);

    public static void getQuestionnairFromJingang(String quesIdBuffer,Map<String, String> quesMap) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = JDBCUtilSingle.getInstance().getConnection();
            logger.info("-------------开始查询金刚问题件----------------");
            String sql = "select ID,STATUS"
                    + " from YTEXP.t_exp_waybill_issue "
                    + "where ID in( "+quesIdBuffer
                    
                    + " ) ";
            logger.info("sql:"+sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                quesMap.put(rs.getString("id"),rs.getString("status"));
                
            }
        } catch (Exception e) {
            logger.error("从金刚获取问题件信息出错",e);
        } finally {
            JDBCUtilSingle.free(rs, ps, con);
        }
    }
}
