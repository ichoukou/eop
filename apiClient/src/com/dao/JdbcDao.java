package com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.api.OrderUpload;
import com.model.JdbcConfig;

/**
 * Jdbc操作
 * 
 * @author huangtianfu
 * @date 2013-10-14
 */
public class JdbcDao {

	private static String sql;

	/** 日期格式化:年-月-日 **/
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	static {
		sql = "insert into surfacebill(mail_no,customer_code,sequence,create_time) values("
				+ "?,?,?,?)";
	}

	/**
	 * 查询用户需要上传的订单
	 * 
	 * @param customerCode
	 *            商家代码
	 * @return
	 */
	public static List<OrderUpload> selectOrderUploadsOfUser(
			String customerCode, JdbcConfig jdbcConfig) {
		List<OrderUpload> orderUploads = new ArrayList<OrderUpload>();
		OrderUpload orderUpload;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			// 1:创建连接
			conn = openConnection(jdbcConfig);
			// 2:查询
			String sql = "select * from surfacebill_upload where upload_result='W' limit 1000";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs != null && rs.next()) {
				orderUpload = new OrderUpload();
				orderUpload.setAcceptTime(rs.getTimestamp("accept_time"));
				orderUpload.setAgencyFund(rs.getString("agency_fund"));
				orderUpload.setBigPen(rs.getString("big_pen"));
				orderUpload.setBuyAddress(rs.getString("buy_address"));
				orderUpload.setBuyCity(rs.getString("buy_city"));
				orderUpload.setBuyDistrict(rs.getString("buy_district"));
				orderUpload.setBuyMobile(rs.getString("buy_mobile"));
				orderUpload.setBuyName(rs.getString("buy_name"));
				orderUpload.setBuyPostcode(rs.getString("buy_postcode"));
				orderUpload.setBuyProv(rs.getString("buy_prov"));
				orderUpload.setBuyServiceFee(rs
						.getBigDecimal("buy_service_fee"));
				orderUpload.setBuyTelphone(rs.getString("buy_telphone"));
				orderUpload.setClientId(rs.getString("client_id"));
				orderUpload.setCodSplitFee(rs.getBigDecimal("cod_split_fee"));
				orderUpload.setCommodityInfo(rs.getString("commodity_info"));
				orderUpload.setCustomerId(rs.getString("customer_id"));
				orderUpload.setDeliverNo(rs.getString("deliver_no"));
				orderUpload.setFreight(rs.getBigDecimal("freight"));
				orderUpload.setFreightType(rs.getString("freight_type"));
				orderUpload.setGoodsValue(rs.getBigDecimal("goods_value"));
				orderUpload.setInsuranceValue(rs
						.getBigDecimal("insurance_value"));
				orderUpload.setIsPrint(rs.getString("is_print"));
				orderUpload.setItemName(rs.getString("item_name"));
				orderUpload.setItemNumber(rs.getInt("item_number"));
				orderUpload.setItemsValue(rs.getBigDecimal("items_value"));
				orderUpload.setLineType(rs.getString("line_type"));
				orderUpload.setMailNo(rs.getString("mail_no"));
				orderUpload.setOrderService(rs.getInt("order_service"));
				orderUpload.setOrderStatus(rs.getString("order_status"));
				orderUpload.setOrderType(rs.getInt("order_type"));
				orderUpload.setSaleAddress(rs.getString("sale_address"));
				orderUpload.setSaleCity(rs.getString("sale_city"));
				orderUpload.setSaleDistrict(rs.getString("sale_district"));
				orderUpload.setSaleMobile(rs.getString("sale_mobile"));
				orderUpload.setSaleName(rs.getString("sale_name"));
				orderUpload.setSalePostcode(rs.getString("sale_postcode"));
				orderUpload.setSaleProv(rs.getString("sale_prov"));
				orderUpload.setSaleTelPhone(rs.getString("sale_telphone"));
				orderUpload.setSendEndTime(rs.getTimestamp("send_end_time"));
				orderUpload
						.setSendStartTime(rs.getTimestamp("send_start_time"));
				orderUpload.setSpecial(rs.getString("special"));
				orderUpload.setTotalServiceFee(rs
						.getBigDecimal("total_service_fee"));
				orderUpload.setTraderNo(rs.getString("trader_no"));
				orderUpload.setTrimFreight(rs.getBigDecimal("trim_freight"));
				orderUpload.setTxLogisticId(rs.getString("tx_logistic_id"));
				orderUpload.setVipId(rs.getString("vip_id"));
				orderUpload.setWeight(rs.getBigDecimal("weight"));
				orderUpload.setWeightUpdateFlag(rs
						.getString("weight_update_flag"));
				orderUploads.add(orderUpload);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeCollection(rs, stmt, conn);
		}
		return orderUploads;
	}

	/**
	 * 保存电子面单
	 * 
	 * @param jdbcConfig
	 */
	public static boolean saveMailNos(List<String> mailNos,
			String customerCode, String sequence, JdbcConfig jdbcConfig) {
		boolean isSuccess = true;
		Connection conn = null;
		try {
			// 1:创建pstmt
			conn = openConnection(jdbcConfig);
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);
			// 2:保存面单
			for (String mailNo : mailNos) {
				pstmt.setString(1, mailNo);
				pstmt.setString(2, customerCode);
				pstmt.setString(3, sequence);
				pstmt.setString(4, sdf.format(new Date()));
				pstmt.addBatch();
			}
			// 3:执行，清空并关闭pstmt
			pstmt.executeBatch();
			conn.commit();
			pstmt.clearBatch();
			pstmt.close();
			pstmt = null;
		} catch (SQLException e) {
			isSuccess = false;
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			closeCollection(null, null, conn);
		}
		return isSuccess;
	}

	/**
	 * 同步面单失败时，保存易通返回的报错信息
	 * 
	 * @param message
	 * @param customerCode
	 * @param sequence
	 */
	public static void saveMessage(String message, String customerCode,
			String sequence, String callApi, JdbcConfig jdbcConfig) {
		Connection conn = null;
		Statement stmt = null;
		try {
			// 1:创建pstmt
			conn = openConnection(jdbcConfig);
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			// 2:保存
			String insertSql = "insert into surfacebill_log(sequence,customer_code,message,call_api,create_time)"
					+ " value('"
					+ sequence
					+ "','"
					+ customerCode
					+ "','"
					+ message
					+ "','"
					+ callApi
					+ "','"
					+ sdf.format(new Date()) + "')";
			stmt.execute(insertSql);
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			closeCollection(null, stmt, conn);
		}
	}

	/**
	 * 获取面单数
	 * 
	 * @param jdbcConfig
	 * @return count
	 */
	public static int selectSurfaceBill(JdbcConfig jdbcConfig) {
		int count = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			// 1:创建pstmt
			conn = openConnection(jdbcConfig);
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			// 2:保存
			String countSql = "select count(*) from surfacebill";
			rs = stmt.executeQuery(countSql);
			while (rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			closeCollection(rs, stmt, conn);
		}
		return count;

	}

	/**
	 * 获取回传表数量
	 * 
	 * @param jdbcConfig
	 * @return count
	 */
	public static int selectOrderUpload(JdbcConfig jdbcConfig) {
		int count = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			// 1:创建pstmt
			conn = openConnection(jdbcConfig);
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			// 2:保存
			String countSql = "select count(*) from surfacebill_upload";
			rs = stmt.executeQuery(countSql);
			while (rs != null && rs.next()) {
				count = rs.getInt(1);
			}
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			closeCollection(rs, stmt, conn);
		}
		return count;

	}

	/**
	 * 打开数据库连接
	 * 
	 * @param jdbcConfig
	 * 
	 * @return
	 */
	private static Connection openConnection(JdbcConfig jdbcConfig) {
		Connection conn = null;
		try {
			Class.forName(jdbcConfig.getDriver());
			conn = DriverManager.getConnection(jdbcConfig.getUrl(),
					jdbcConfig.getUsername(), jdbcConfig.getPassword());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 关闭数据库连接
	 */
	private static void closeCollection(ResultSet rs, Statement stmt,
			Connection conn) {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新订单的上传状态
	 * 
	 * @param txLogisticId
	 *            订单物流号
	 * @param uploadResult
	 *            订单上传状态(Y:成功, W:等待上传中)
	 */
	public static void updateUploadOrderState(String txLogisticId,
			String uploadResult, JdbcConfig jdbcConfig) {
		Connection conn = null;
		Statement stmt = null;
		try {
			String sql = "update upload_order set upload_result='"
					+ uploadResult + "' where tx_logistic_id='" + txLogisticId
					+ "'";
			// 1:创建pstmt
			conn = openConnection(jdbcConfig);
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			// 2:更新
			stmt.execute(sql);
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			closeCollection(null, stmt, conn);
		}
	}
}
