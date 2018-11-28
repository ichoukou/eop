package net.ytoec.kernel.service.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import net.ytoec.kernel.util.JDBCUtilSingle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderWeightUpdateHelper {

	private static Logger logger=LoggerFactory.getLogger(OrderWeightUpdateHelper.class);

	public static void getWeightFromJingang(String mailNos,Map<String, Float> mailWeightMap) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = JDBCUtilSingle.getInstance().getConnection();
			logger.info("-------------开始更新重量信息----------------");
			String sql = "select WAYBILL_NO,WEIGH_WEIGHT,INPUT_WEIGHT"
					+ " from YTEXP.T_EXP_OP_RECORD_TAKING "
					+ "where WAYBILL_NO in("+mailNos
					
					+ " ) and  STATUS > 0";
			logger.info("sql:"+sql);
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
            String waybillNo="";
            Float weight=null;

			while (rs.next()) {
	            waybillNo = rs.getString("waybill_no");
	            weight = rs.getFloat("WEIGH_WEIGHT");
	            if (weight == 0 || weight == null) {
					weight = rs.getFloat("INPUT_WEIGHT");
				}
			    if (weight == 0 || weight == null) {
                    continue;
                }
				mailWeightMap.put(waybillNo,weight);
				
			}
		} catch (Exception e) {
			logger.error("从金刚获取重量信息出错",e);
		} finally {
			JDBCUtilSingle.free(rs, ps, con);
		}
	}
}
