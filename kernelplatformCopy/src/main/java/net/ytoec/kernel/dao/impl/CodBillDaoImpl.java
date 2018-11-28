package net.ytoec.kernel.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.action.bill.BillDetailDto;
import net.ytoec.kernel.action.bill.BillInfo;
import net.ytoec.kernel.action.bill.TStlConfirmLetterCod;
import net.ytoec.kernel.action.bill.UUIDHexGenerator;
import net.ytoec.kernel.dao.CodBillDao;
import net.ytoec.kernel.dataobject.ConfirmLetterDto;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.util.JDBCUtilSingle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ibm.icu.text.SimpleDateFormat;

/**
 * 代收货款
 * @author wmd
 * 2013-03-06
 * @param <T>
 */
@Repository
public class CodBillDaoImpl<T> implements CodBillDao<T>{

	private static Logger logger  = LoggerFactory.getLogger(CodBillDaoImpl.class);
	 private Connection con = null;
	 private PreparedStatement ps = null;
	 ResultSet rs = null;

//	 测试地址
//	 private static final String url = "jdbc:oracle:thin:@(DESCRIPTION =" +
//		"(ADDRESS_LIST =" +
//		"(ADDRESS = (PROTOCOL = TCP)" +
//					  "(HOST = 116.228.70.216)" +
//					  "(PORT = 1521)" +
//		"))" + 
//		"(CONNECT_DATA =(SID = exprd1)))";
//		private static final String username = "YTSTL";
//		private static final String password = "YTSTL";
	 //生产地址
	 private static final String url = "jdbc:oracle:thin:@(DESCRIPTION =" +
		"(ADDRESS_LIST =" +
		"(ADDRESS = (PROTOCOL = TCP)" +
					  "(HOST = 10.1.194.25)" +
					  "(PORT = 1521)" +
		"))" + 
		"(CONNECT_DATA =(SID = STDDB1)))";
	 private static final String username = "dbquery";
	 private static final String password = "dbquery";
	 
	// 获取连接
    public Connection getConnection() throws SQLException {
    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		}
		try {
			return DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
    }
	    
	 /**
	  * 查询未确认账单明细总数
	  * dunjie
	  */
	@Override
	public Integer searchTotalCod(Map<String, Object> params) {
		Integer result = 0 ;
		try{
			logger.info("开始查询金刚代收货款表....");
			con = this.getConnection();
			String sql = "SELECT COUNT(*) as c FROM YTSTL.T_STL_BILL_DETAILS_COD B WHERE  B.CONFLETR_FLG ="+1+" AND "
			+"B.SEND_CUSTOMER_CODE = '" +params.get("userCode")+"' AND "
			+"B.CUSTOMER_CONFIRM_FLG =" + params.get("customerConfirmFlg")+
			" ORDER BY WAYBILL_NO DESC";
			
			logger.info("代收货款总数查询SQL :" + sql);
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			rs.next();
			result = rs.getInt(1);
			
			logger.info("查询金刚代收货款表结束....");
			
		}catch(Exception e){
			logger.info("查询金刚代收货款表失败");
			e.printStackTrace();
		}finally{
			JDBCUtilSingle.free(rs, ps, con);
		}
		return result ;
	}

	/**
	 *  综合查询
	 *  chenfeng
	 */
	@Override
	public List<T> searchCodDatils(Map<String, Object> params) {
		List<BillDetailDto> billList = new ArrayList<BillDetailDto>();
		try {
			logger.info("开始查询金刚代收货款表....");
			con = this.getConnection();
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT " +
					"B.ID AS detailId," +
					"B.WAYBILL_NO AS waybillNo, " +
					"TO_CHAR(B.COLLECT_IN_TIME,'yyyy-MM-dd HH24:mi:ss') AS collectInTime, " +
					"TO_CHAR(B.SIGNOFF_TIME,'yyyy-MM-dd HH24:mi:ss') AS signoffTime, " +
					"B.INITIAL_COD_AMOUNT AS codMoney, " +
					"B.ACTUAL_COD_AMOUNT AS codMoneyReal, " +
					"B.SEND_ORG_NAME AS sendOrgName, " +
					"B.CONFIRM_FLG AS confirmFlg, " +
					"B.SIGNOFF_FLG AS signoffFlg, " +
					"B.CUSTOMER_CONFIRM_FLG AS customerConfirmFlg, " +
					"B.BACKUP1 AS customerPayFlg, " +
					"B.BACKUP5 AS chargeType, " +
					"TO_CHAR(B.BACKUP3,'yyyy-MM-dd HH24:mi:ss') AS customerPayTime, " +
					"TO_CHAR(B.RECORD_TIME,'yyyy-MM-dd HH24:mi:ss') AS sendTime ");
			
			sql.append("FROM YTSTL.T_STL_BILL_DETAILS_COD B " +
					"WHERE " +
					"B.ERROR_CODE='0' AND " +
					"B.SEND_CUSTOMER_CODE= '"+params.get("userCode")+"'");
			

			if (params.containsKey("waybillNoStr")) {
				sql.append("AND B.WAYBILL_NO in (" + params.get("waybillNoStr") +  ") ");
//				if (params.containsKey("pageFlag")) {
//					// 如果有确认状态条件
//					if (params.containsKey("customerConfirmFlg")) {
//						sql.append("AND B.CUSTOMER_CONFIRM_FLG = "+params.get("customerConfirmFlg"));
//					}
//				}
			} else {
				Short timeType = Short.parseShort((String) params.get("timeType"));
//				判断用户是否输入查询时间，没有则返回全部时间的记录，有则添加时间限制条件
				if(params.containsKey("detailStartTime") && params.containsKey("detailEndTime")){
					switch (timeType) {
					case 0:		// 如果时间类型为录单时间
						sql.append("AND B.RECORD_TIME >= TO_DATE('"+params.get("detailStartTime")+"', 'yyyy-MM-dd HH24:mi:ss') ");
						sql.append("AND B.RECORD_TIME <= TO_DATE('"+params.get("detailEndTime")+"', 'yyyy-MM-dd HH24:mi:ss') ");
						break;
					case 1:		// 如果时间类型为客户确认时间
						sql.append("AND B.CUSTOMER_CONFIRM_TIME >= TO_DATE('"+params.get("detailStartTime")+"', 'yyyy-MM-dd HH24:mi:ss') ");
						sql.append("AND B.CUSTOMER_CONFIRM_TIME <= TO_DATE('"+params.get("detailEndTime")+"', 'yyyy-MM-dd HH24:mi:ss') ");
						break;
					case 2:		// 如果时间类型为客户支付时间
						sql.append("AND B.BACKUP3 >= TO_DATE('"+params.get("detailStartTime")+"', 'yyyy-MM-dd HH24:mi:ss') ");
						sql.append("AND B.BACKUP3 <= TO_DATE('"+params.get("detailEndTime")+"', 'yyyy-MM-dd HH24:mi:ss') ");
						break;
					case 3:		// 如果时间类型为签收时间
							sql.append("AND B.SIGNOFF_TIME >= TO_DATE('"+params.get("detailStartTime")+"', 'yyyy-MM-dd HH24:mi:ss') ");
							sql.append("AND B.SIGNOFF_TIME <= TO_DATE('"+params.get("detailEndTime")+"', 'yyyy-MM-dd HH24:mi:ss') ");
						break;
					default:
						break;
					}
				}
				
				// 如果有支付状态条件
//				if (params.containsKey("customerPayFlg")) {
//					sql.append("AND B.BACKUP1 ="+params.get("customerPayFlg"));
//				}
				// 如果有确认状态条件
				if (params.containsKey("customerConfirmFlg")) {
					sql.append("AND B.CUSTOMER_CONFIRM_FLG ="+params.get("customerConfirmFlg"));
				}
				// 如果有签收状态条件
				if (params.containsKey("signoffFlg")) {
					sql.append("AND B.SIGNOFF_FLG ="+params.get("signoffFlg"));
				}
			}
//			sql.append(" ) A WHERE ROWNUM <="+params.get("")+" ) WHERE RN >="+params.get("")+" )");
			sql.append(" ORDER BY B.COLLECT_IN_TIME DESC ");
			ps = con.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				BillDetailDto billTemp = new BillDetailDto();
				billTemp.setDetailId(rs.getString("detailId"));
				billTemp.setWaybillNo(rs.getString("waybillNo"));
				billTemp.setCollectInTime(rs.getString("collectInTime"));
				billTemp.setSendTime(rs.getString("sendTime"));
				billTemp.setSignoffTime(rs.getString("signoffTime"));
				billTemp.setCodMoney(rs.getDouble("codMoney"));
				billTemp.setCodMoneyReal(rs.getDouble("codMoneyReal"));
				billTemp.setSendOrgName(rs.getString("sendOrgName"));
				billTemp.setConfirmFlg(rs.getString("confirmFlg"));
				billTemp.setSignoffFlg(rs.getString("signoffFlg"));
				billTemp.setCustomerConfirmFlg(rs.getString("customerConfirmFlg"));
				billTemp.setCustomerPayFlg(rs.getString("customerPayFlg"));
				billTemp.setCustomerPayTime(rs.getString("customerPayTime"));
				billTemp.setChargeType(rs.getString("chargeType"));
				billList.add(billTemp);
			}
		} catch (Exception e) {
			logger.info("代收货款查询失败");
			e.printStackTrace();
		}finally{
			JDBCUtilSingle.free(rs, ps, con);
		}
		return (List<T>) billList;
	}
	
	/***
	 * 查询未确认账单明细
	 * dunjie
	 */
	public List<BillDetailDto> findCodDatils(Map<String, Object> params){
		List<BillDetailDto> billList = new ArrayList<BillDetailDto>();
		try {
			logger.info("开始查询金刚代收货款表....");
			con = this.getConnection();//JDBCUtilSingle.getInstance().getConnection();
			if(con == null){
				logger.error("连接数据库失败");
				return billList;
			}
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT A.ID AS detailId,A.DELIVERY_ORG_NAME AS deliveryOrgName,A.DELIVERY_ORG_CODE AS deliveryOrgCode,"
					+ "A.WAYBILL_NO AS waybillNo,"
					+ "TO_CHAR(A.COLLECT_IN_TIME,'yyyy-MM-dd HH24:mi:ss') AS collectInTime,"
					+ "TO_CHAR(A.SIGNOFF_TIME,'yyyy-MM-dd HH24:mi:ss') AS signoffTime,"
					+ "A.INITIAL_COD_AMOUNT AS codMoney,"
					+ "TO_CHAR(A.ACTUAL_COD_AMOUNT,'9999999999990.00') AS codMoneyReal,"
					+ "A.SEND_ORG_NAME AS sendOrgName,"
					+ "A.CONFIRM_FLG AS confirmFlg,"
					+ "A.SIGNOFF_FLG AS signoffFlg,"
					+ "A.CUSTOMER_CONFIRM_FLG AS customerConfirmFlg,"
					+ "A.BACKUP1 AS customerPayFlg, "
					+ "A.BACKUP3 AS customerPayTime, "
					+ "A.BACKUP5 AS payType, " 
					+ "TO_CHAR(A.RECORD_TIME,'yyyy-MM-dd HH24:mi:ss') as recordTime,"
					+ "A.DIFF_FLG as diffFlg,A.CUSTOMER_CONFIRM_FLG as customer_confirm_flg,"
					+ "A.SEND_ORG_CODE as sendOrgCode,A.DES_ORG_CODE as desOrgCode,A.DES_ORG_NAME as desOrgName");
			sql.append(" FROM  YTSTL.T_STL_BILL_DETAILS_COD A "+
					   " WHERE A.CUSTOMER_CONFIRM_FLG = '0' AND A.SIGNOFF_FLG = '0' AND A.ERROR_CODE='0' AND A.SEND_CUSTOMER_CODE='"+params.get("userCode")+"' " );
						//0=未确认 			                                                         
			if(params.get("start")==null){ //非初始化点击进来	
				String num = (String) params.get("waybillNoStr"); //运单号
				//有运单号,时间条件无效
				if(num!=null){
					sql.append(" AND A.WAYBILL_NO IN ("+num+")");
				}else{
					if(params.get("detailStartTime")!=null || params.get("detailEndTime")!=null){
						sql.append(" AND RECORD_TIME >= TO_DATE('"+params.get("detailStartTime")+"', 'yyyy-MM-dd HH24:mi:ss') ");
						sql.append(" AND RECORD_TIME <= TO_DATE('"+params.get("detailEndTime")+"', 'yyyy-MM-dd HH24:mi:ss')");
					}
				}
			}
			
			sql.append(" ORDER BY WAYBILL_NO DESC");
			//System.err.println("未确认账单明细SQL语句 :"+sql.toString());
			ps = con.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				BillDetailDto billTemp = new BillDetailDto();
				billTemp.setDetailId(rs.getString("detailId"));
				billTemp.setWaybillNo(rs.getString("waybillNo"));//运单号
				if(rs.getString("recordTime")!= null){
					billTemp.setRecordTime(rs.getString("recordTime"));//发件时间
				}
				if(rs.getString("collectInTime")!= null){
					billTemp.setCollectInTime(rs.getString("collectInTime")); //揽件日期
				}
				billTemp.setCodMoney(rs.getDouble("codMoney")); //录单金额
				billTemp.setCodMoneyReal(rs.getDouble("codMoneyReal")); //代收金额
				billTemp.setSendOrgName(rs.getString("sendOrgName"));//发件网点名称
				billTemp.setSignoffFlg(rs.getString("signoffFlg")); //快件状态
				billTemp.setConfirmFlg(rs.getString("confirmFlg")); //差异信息
				billTemp.setChargeType(rs.getString("payType"));
				billTemp.setCustomerConfirmFlg(rs.getString("customer_confirm_flg"));//确认标识
				billTemp.setDesOrgCode(rs.getString("desOrgCode")); //派件网点
				billTemp.setSendOrgCode(rs.getString("sendOrgCode"));
				billTemp.setDesOrgName(rs.getString("desOrgName"));
				billTemp.setDeliveryOrgName(rs.getString("deliveryOrgName"));
				billTemp.setDeliveryOrgCode(rs.getString("deliveryOrgCode"));
				billList.add(billTemp);
			}
		} catch (Exception e) {
			logger.info("代收货款查询失败");
			e.printStackTrace();
		}finally{
			JDBCUtilSingle.free(rs, ps, con);
		}
		return  billList;
	}
	/***
	 * 查询未确认账单明细总金额
	 * dunjie
	 */
	@Override
	public Double getCODCount(Map<String, Object> params) {
//		double count=0;
//		try {
//			logger.info("开始查询金刚代收货款表账单明细总金额....");
//			con = this.getConnection();
//			StringBuffer sql = new StringBuffer();
//			sql.append("SELECT  SUM(B.ACTUAL_COD_AMOUNT) AS totalAmount FROM YTSTL.T_STL_BILL_DETAILS_COD B" +
//					" WHERE B.CONFLETR_FLG = '1' AND B.SIGNOFF_FLG ='1'");// AND B.SEND_CUSTOMER_CODE ='"+params.get("userCode")+"' " );
//												     //网点确认标识【已确认】       发件客户代码
//			if(params.get("start")==null){ //非初始化进来
//				String num = (String) params.get("waybillNo"); //运单号
//				//有运单号,时间条件无效
//				if(!num.equals("")){
//					sql.append(" AND B.WAYBILL_NO = '"+num+"'");
//				}else{
//					
//						sql.append(" AND RECORD_TIME >= TO_DATE('"+params.get("detailStartTime")+"', 'yyyy-MM-dd HH24:mi:ss') ");
//						sql.append(" AND RECORD_TIME <= TO_DATE('"+params.get("detailEndTime")+"', 'yyyy-MM-dd HH24:mi:ss')");
//				}
//			}
//			
//			sql.append(" ORDER BY WAYBILL_NO DESC");
//			System.err.println("SQL语句 :"+sql.toString());
//			ps = con.prepareStatement(sql.toString());
//			rs = ps.executeQuery();
//			
//			while(rs.next()){
//				count=rs.getDouble(1);
//			}
//		} catch (Exception e) {
//			logger.info("代收货款账单总金额查询失败");
//			e.printStackTrace();
//		}finally{
//			JDBCUtilSingle.free(rs, ps, con);
//		}
//		return count;
		return null;
	}

	/**
	 * 查询客户未确认的确认函(即差异)
	 * @param conditions:查询条件
	 * dunjie
	 * @return
	 */
	public List<ConfirmLetterDto> getConfirmLetterCods(Map<String, Object> conditions) {
		List<ConfirmLetterDto> result = new ArrayList<ConfirmLetterDto>();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT r.WAYBILL_NO AS waybillNo,"
				+ "TO_CHAR(r.INFORM_TIME,'yyyy-MM-dd HH24:mi:ss') AS informTime,"
				+ "r.ACTUAL_COD_AMOUNT AS initialValue,"
				+ "r.INFORM_VALUE AS informValue,"
				+ "r.CONFIRM_FLG AS confirmFlg "
				+ "FROM (SELECT rank() over(partition by l.WAYBILL_NO order by l.INFORM_TIME DESC) rank,"
				+ "l.* FROM YTSTL.T_STL_CONFIRM_LETTER_COD l, YTSTL.T_STL_BILL_DETAILS_COD c "
				+ "WHERE l.WAYBILL_NO = c.WAYBILL_NO "
				+ "AND l.DIFF_TYPE = '1' AND l.PUB_ORG_CODE IS NULL "
				+ "AND c.CONFIRM_FLG <> '8' "
				+ "AND l.INFORM_ORG_CODE IS NULL AND l.CONFIRM_FLG <> '1' "
				+ "AND l.INFORM_EMPLOYEE_NO = '"+conditions.get("userCode")+"' ");
		if(conditions.get("start")==null){ //初始化不进来
			// 如果有运单号条件
			if (conditions.containsKey("waybillNo2")) {
				if(!conditions.get("waybillNo2").equals("")){
					sb.append("AND l.WAYBILL_NO = '"+conditions.get("waybillNo2")+"' ");
				}
			}
			// 如果有上报开始时间条件
			if (conditions.containsKey("startTime")) {
				sb.append("AND l.INFORM_TIME >= TO_DATE('"+conditions.get("startTime")+"', 'yyyy-MM-dd HH24:mi:ss') ");
			}
			// 如果有上报结束时间条件
			if (conditions.containsKey("endTime")) {
				sb.append("AND l.INFORM_TIME <= TO_DATE('"+conditions.get("endTime")+"', 'yyyy-MM-dd HH24:mi:ss') ");
			}
		}
		sb.append("ORDER BY l.INFORM_TIME DESC) r WHERE r.rank = 1");
		//System.out.println("未确认差异查询SQL语句:"+sb.toString());
		try {
			con = this.getConnection();
			ps = con.prepareStatement(sb.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				ConfirmLetterDto c = new ConfirmLetterDto();
				c.setWaybillNo(rs.getString("waybillNo"));
				if(rs.getString("informTime")!=null){
					c.setInformTime(rs.getString("informTime"));
				}
				c.setInitialValue(rs.getDouble("initialValue"));
				c.setInformValue(rs.getString("informValue"));
				c.setConfirmFlg(rs.getString("confirmFlg"));
				result.add(c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			JDBCUtilSingle.free(rs, ps, con);
		}
		return result;
	}

	/**
	 * chenfeng
	 */
	@Override
	public List<BillDetailDto> getBillListByMailNos(String waybillNos) {
//		返回查询结构的集合
		List<BillDetailDto> billList = new ArrayList<BillDetailDto>();
		try {
			logger.info("开始查询金刚代收货款表....");
			con = this.getConnection();
			StringBuffer sql = new StringBuffer();
//			拼接字符串，根据运单号查询
			sql.append("SELECT B.ID AS detailId,B.WAYBILL_NO AS waybillNo,TO_CHAR(B.COLLECT_IN_TIME,'yyyy-MM-dd HH24:mi:ss') AS collectInTime,TO_CHAR(B.SIGNOFF_TIME,'yyyy-MM-dd HH24:mi:ss') AS signoffTime,B.INITIAL_COD_AMOUNT AS codMoney,B.ACTUAL_COD_AMOUNT AS codMoneyReal,B.SEND_ORG_NAME AS sendOrgName,B.CONFIRM_FLG AS confirmFlg,B.SIGNOFF_FLG AS signoffFlg,B.CUSTOMER_CONFIRM_FLG AS customerConfirmFlg,B.BACKUP1 AS customerPayFlg,B.BACKUP5 AS chargeType, TO_CHAR(B.BACKUP3,'yyyy-MM-dd HH24:mi:ss') AS customerPayTime, TO_CHAR(B.RECORD_TIME,'yyyy-MM-dd HH24:mi:ss') AS sendTime,B.DIFF_FLG as diffFlg ");
			sql.append("FROM YTSTL.T_STL_BILL_DETAILS_COD B WHERE B.WAYBILL_NO in (" );
//			将运单号分解为单个的运单并添加进sql中
			String[] wayBills = waybillNos.split(",");
			for (String string : wayBills) {
				string = string.trim();
				sql.append("'"+string+"'").append(", ");
			}
//			去除添加的最后一个多余的逗号
			sql.deleteCharAt(sql.length() - 2);
//			拼接最后的括号
			sql.append(" )");
			
			
			logger.info("代收货款总数查询SQL :" + sql.toString());
			ps = con.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				BillDetailDto billTemp = new BillDetailDto();
				billTemp.setDetailId(rs.getString("detailId"));
				billTemp.setWaybillNo(rs.getString("waybillNo"));
				billTemp.setCollectInTime(rs.getString("collectInTime"));
				billTemp.setSendTime(rs.getString("sendTime"));
				billTemp.setSignoffTime(rs.getString("signoffTime"));
				billTemp.setCodMoney(rs.getDouble("codMoney"));
				billTemp.setCodMoneyReal(rs.getDouble("codMoneyReal"));
				billTemp.setSendOrgName(rs.getString("sendOrgName"));
				billTemp.setConfirmFlg(rs.getString("confirmFlg"));
				billTemp.setSignoffFlg(rs.getString("signoffFlg"));
				billTemp.setCustomerConfirmFlg(rs.getString("customerConfirmFlg"));
				billTemp.setCustomerPayFlg(rs.getString("customerPayFlg"));
				billTemp.setCustomerPayTime(rs.getString("customerPayTime"));
				billTemp.setChargeType(rs.getString("chargeType"));
				billTemp.setDiffFlg(rs.getString("diffFlg"));
				billList.add(billTemp);
			}
		} catch (Exception e) {
			logger.info("代收货款查询失败");
			e.printStackTrace();
		}finally{
			JDBCUtilSingle.free(rs, ps, con);
		}
		return billList;
	}

	/**
	 * chenfeng
	 */
	@Override
	public List<T> searchCodBillDatils(Map<String, Object> params) {
		List<BillDetailDto> billList = new ArrayList<BillDetailDto>();
		try {
			logger.info("开始查询金刚代收货款表....");
			con = this.getConnection();
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT B.ID AS detailId,B.WAYBILL_NO AS waybillNo,TO_CHAR(B.COLLECT_IN_TIME,'yyyy-MM-dd HH24:mi:ss') AS collectInTime,TO_CHAR(B.SIGNOFF_TIME,'yyyy-MM-dd HH24:mi:ss') AS signoffTime,B.INITIAL_COD_AMOUNT AS codMoney,B.ACTUAL_COD_AMOUNT AS codMoneyReal,B.SEND_ORG_NAME AS sendOrgName,B.CONFIRM_FLG AS confirmFlg,B.SIGNOFF_FLG AS signoffFlg,B.CUSTOMER_CONFIRM_FLG AS customerConfirmFlg,B.BACKUP1 AS customerPayFlg,B.BACKUP5 AS chargeType, TO_CHAR(B.BACKUP3,'yyyy-MM-dd HH24:mi:ss') AS customerPayTime, TO_CHAR(B.BACKUP4,'yyyy-MM-dd HH24:mi:ss') AS sendTime ");
			
			sql.append("FROM YTSTL.T_STL_BILL_DETAILS_COD B WHERE B.CONFLETR_FLG = '1' AND B.SEND_CUSTOMER_CODE= '"+params.get("userCode")+"'");
			
			Short timeType = Short.parseShort((String) params.get("timeType"));
			if (params.containsKey("waybillNoStr")) {
				sql.append("AND B.WAYBILL_NO in (" + params.get("waybillNoStr") +  ") ");
				if (params.containsKey("pageFlag")) {
					// 如果有确认状态条件
					if (params.containsKey("customerConfirmFlg")) {
						sql.append("AND B.CUSTOMER_CONFIRM_FLG = "+params.get("customerConfirmFlg"));
					}
				}
			} else {
//				判断用户是否输入查询时间，没有则返回全部时间的记录，有则添加时间限制条件
				if(params.containsKey("detailStartTime") && params.containsKey("detailEndTime")){
					switch (timeType) {
					case 0:		// 如果时间类型为录单时间
						sql.append("AND B.RECORD_TIME >= TO_DATE('"+params.get("detailStartTime")+"', 'yyyy-MM-dd HH24:mi:ss') ");
						sql.append("AND B.RECORD_TIME <= TO_DATE('"+params.get("detailEndTime")+"', 'yyyy-MM-dd HH24:mi:ss') ");
						break;
					case 1:		// 如果时间类型为客户确认时间
						sql.append("AND B.CUSTOMER_CONFIRM_TIME >= TO_DATE('"+params.get("detailStartTime")+"', 'yyyy-MM-dd HH24:mi:ss') ");
						sql.append("AND B.CUSTOMER_CONFIRM_TIME <= TO_DATE('"+params.get("detailEndTime")+"', 'yyyy-MM-dd HH24:mi:ss') ");
						break;
					case 2:		// 如果时间类型为客户支付时间
						sql.append("AND B.BACKUP3 >= TO_DATE('"+params.get("detailStartTime")+"', 'yyyy-MM-dd HH24:mi:ss') ");
						sql.append("AND B.BACKUP3 <= TO_DATE('"+params.get("detailEndTime")+"', 'yyyy-MM-dd HH24:mi:ss') ");
						break;
					case 3:		// 如果时间类型为签收时间
							sql.append("AND B.SIGNOFF_TIME >= TO_DATE('"+params.get("detailStartTime")+"', 'yyyy-MM-dd HH24:mi:ss') ");
							sql.append("AND B.SIGNOFF_TIME <= TO_DATE('"+params.get("detailEndTime")+"', 'yyyy-MM-dd HH24:mi:ss') ");
//						}
						break;
					default:
						break;
					}
				}
				
				// 如果有支付状态条件
				if (params.containsKey("customerPayFlg")) {
					sql.append("AND B.BACKUP1 ="+params.get("customerPayFlg"));
				}
				// 如果有确认状态条件
				if (params.containsKey("customerConfirmFlg")) {
					sql.append("AND B.CUSTOMER_CONFIRM_FLG ="+params.get("customerConfirmFlg"));
				}
				// 如果有签收状态条件
				if (params.containsKey("signoffFlg")) {
					sql.append("AND B.SIGNOFF_FLG ="+params.get("signoffFlg"));
				}
			}
//			sql.append(" ) A WHERE ROWNUM <="+params.get("")+" ) WHERE RN >="+params.get("")+" )");
			sql.append(" ORDER BY B.WAYBILL_NO DESC ");
			logger.info("代收货款总数查询SQL :" + sql.toString());
			ps = con.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				BillDetailDto billTemp = new BillDetailDto();
				billTemp.setDetailId(rs.getString("detailId"));
				billTemp.setWaybillNo(rs.getString("waybillNo"));
				billTemp.setCollectInTime(rs.getString("collectInTime"));
				billTemp.setSendTime(rs.getString("sendTime"));
				billTemp.setSignoffTime(rs.getString("signoffTime"));
				billTemp.setCodMoney(rs.getDouble("codMoney"));
				billTemp.setCodMoneyReal(Double.parseDouble(rs.getString("codMoneyReal")));
				billTemp.setSendOrgName(rs.getString("sendOrgName"));
				billTemp.setConfirmFlg(rs.getString("confirmFlg"));
				billTemp.setSignoffFlg(rs.getString("signoffFlg"));
				billTemp.setCustomerConfirmFlg(rs.getString("customerConfirmFlg"));
				billTemp.setCustomerPayFlg(rs.getString("customerPayFlg"));
				billTemp.setCustomerPayTime(rs.getString("customerPayTime"));
				billTemp.setChargeType(rs.getString("chargeType"));
				billList.add(billTemp);
			}
		} catch (Exception e) {
			logger.info("代收货款查询失败");
			e.printStackTrace();
		}finally{
			JDBCUtilSingle.free(rs, ps, con);
		}
		return (List<T>) billList;
	}

	/**
	 * dunjie
	 */
	@Override
	public String getCODCounts(List<BillDetailDto> list) {
		double count=0;
		for (BillDetailDto billDetailDto : list) {
			count=count+billDetailDto.getCodMoneyReal();
		}
		String money =new java.text.DecimalFormat("0.00").format(count).toString();
		return money;
	}

	/***
	 * dunjie
	 */
	@Override
	public void batchConfirmBillDetails(String wayBillNos, User currentUser) {
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String nowDate =df.format(new Date());// new Date()为获取当前系统时间
		sb.append("UPDATE YTSTL.T_STL_BILL_DETAILS_COD D "
				+ "SET D.CUSTOMER_CONFIRM_FLG = '1',"
				+ "D.CUSTOMER_CONFIRM_TIME = TO_DATE('"+nowDate+"', 'yyyy-MM-dd HH24:mi:ss'),"
				+ "D.CUSTOMER_EMPLOYEE_CODE = '"+currentUser.getUserCode()+"',"
				+ "D.CUSTOMER_EMPLOYEE_NAME = '"+currentUser.getUserNameText()+"' "
				+ "WHERE D.ID IN ("+wayBillNos+")"
				);
		try {
			con = this.getConnection();
			//System.err.println("确认账单明细SQL语句:"+sb.toString());
			ps = con.prepareStatement(sb.toString());
			rs = ps.executeQuery();
			//System.out.println("SQL执行无异常");
		} catch (SQLException e) {
			logger.info("确认账单明细失败");
			e.printStackTrace();
		}finally{
			JDBCUtilSingle.free(rs, ps, con);
		}
	}

	@Override
	public boolean saveDiff(TStlConfirmLetterCod confirmLetter) {
		try {
			con = this.getConnection();
			StringBuffer sb = new StringBuffer();
			//uuid
			UUIDHexGenerator uuidHex = new UUIDHexGenerator();
			String uuid = uuidHex.generate();
			//System.out.println("主键是:"+uuid);
			sb.append("INSERT INTO YTSTL.T_STL_CONFIRM_LETTER_COD "
				+"(ID,WAYBILL_NO,PUB_REASON,PUB_TIME,PUB_EMP_NO,SEND_ORG_NAME,DELIVERY_ORG_NAME,ACTUAL_COD_AMOUNT,INFORM_VALUE,"
				+"INFORM_REASON,PUB_EMP_NAME,DIFF_ITEM_CODE,DIFF_ITEM_NAME,DIFF_TYPE,CONFIRM_FLG,DELETE_FLG,INITIAL_VALUE,"
				+"INFORM_EMPLOYEE_NO,INFORM_EMPLOYEE_NAME,SEND_CUSTOMER_CODE,SEND_CUSTOMER_NAME,INFORM_TIME,CUSTOMER_CONF_FLG," 
				+"SEND_ORG_CODE,DELIVERY_ORG_CODE) "
				+"VALUES('"
				+uuid+"','"
				+confirmLetter.getWaybillNo()+"','"
				+confirmLetter.getPubReason()+"',SYSDATE,'"
				//+confirmLetter.getPubTime()+"','"
				+confirmLetter.getPubEmpNo()+"','"
				+confirmLetter.getSendOrgName()+"','"
				+confirmLetter.getDeliveryOrgName()+"',"
				+confirmLetter.getActualCodAmount()+",'"
				+confirmLetter.getInformValue()+"','"
				+confirmLetter.getInformReason()+"','"
				+confirmLetter.getPubEmpName()+"','"
				+confirmLetter.getDiffItemCode()+"','"
				+confirmLetter.getDiffItemName()+"','"
				+confirmLetter.getDiffType()+"','"
				+confirmLetter.getConfirmFlg()+"','"
				+confirmLetter.getDeleteFlg()+"','"
				+confirmLetter.getInitialValue()+"','"
				+confirmLetter.getInformEmployeeNo()+"','"
				+confirmLetter.getInformEmployeeName()+"','"
				+confirmLetter.getSendCustomerCode()+"','"
				+confirmLetter.getSendCustomerName()+"',SYSDATE,'"
				//+confirmLetter.getInformTime()+"','"
				+confirmLetter.getCustomerConfFlg()+"','"
				+confirmLetter.getSendOrgCode()+"','"
				+confirmLetter.getDeliveryOrgCode()+"')");
			//System.err.println("上报差异SQL语句:"+sb.toString());
			ps = con.prepareStatement(sb.toString());
			rs = ps.executeQuery();
		} catch (SQLException e) {
			logger.info("上传差异失败");
			e.printStackTrace();
			return false;
		}finally{
			JDBCUtilSingle.free(rs, ps, con);
		}
		return true;
	}

	@Override
	public void updateStlBillDetail(String waybillNo) {
		try {
			con = this.getConnection();
			String sql="UPDATE YTSTL.T_STL_BILL_DETAILS_COD SET DIFF_FLG='1',CONFIRM_FLG = '"+BillInfo.DIFF_TO_SUBCOMPANY.getKey()+"' WHERE WAYBILL_NO = '"+waybillNo+"'";
			//System.err.println("明细的确认状态SQL语句:"+sql);
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			con.commit();
		} catch (SQLException e) {
			logger.info("明细的确认状态修改失败");
			e.printStackTrace();
		}finally{
			JDBCUtilSingle.free(rs, ps, con);
		}
	}

	@Override
	public String getCompanyUndoReason(String waybillNo) {
		String undoReason = null;
		try {
			con = this.getConnection();
			String sql = "SELECT L.RE_DIFF_MEMO AS companyUndoReason " +
					"FROM YTSTL.T_STL_CONFIRM_LETTER_COD L " +
					"WHERE L.DIFF_TYPE = '1' " +
					"AND PUB_ORG_CODE IS NULL " +
					"AND INFORM_ORG_CODE IS NULL " +
					"AND L.CONFIRM_FLG = '2' " +
					"AND L.WAYBILL_NO = '"+waybillNo+"'";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				undoReason = rs.getString("companyUndoReason");
//				如果驳回理由为空，生成一个空字符串返回
				if(undoReason == null){
					undoReason = new String();
				}
			}
		} catch (Exception e) {
			logger.info("查询总公司驳回理由失败。");
			e.printStackTrace();
		} finally{
			JDBCUtilSingle.free(rs, ps, con);
		}
		return undoReason;
	}

	@Override
	public String getSubCompanyUndoReason(String waybillNo) {
		String undoReason = null;
		try {
			con = this.getConnection();
			String sql = "SELECT L.INFORM_MEMO AS subcompanyUndoReason " +
					"FROM YTSTL.T_STL_CONFIRM_LETTER_COD L " +
					"WHERE L.DIFF_TYPE = '1' " +
					"AND L.CONFIRM_FLG = '4' " +
					"AND PUB_ORG_CODE IS NULL " +
					"AND INFORM_ORG_CODE IS NULL " +
					"AND L.WAYBILL_NO = '"+waybillNo+"'";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				undoReason = rs.getString("subcompanyUndoReason");
//				如果驳回理由为空，生成一个空字符串返回
				if(undoReason == null){
					undoReason = new String();
				}
			}
		} catch (Exception e) {
			logger.info("查询总公司驳回理由失败。");
			e.printStackTrace();
		} finally{
			JDBCUtilSingle.free(rs, ps, con);
		}
		return undoReason;
	}

	@Override
	public int repeatSubmit(String waybillNo) {
		int flg=0;
		try {
			con = this.getConnection();
			String sql="SELECT CONFIRM_FLG FROM YTSTL.T_STL_BILL_DETAILS_COD WHERE  WAYBILL_NO='"+waybillNo+"'";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				flg = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			JDBCUtilSingle.free(rs, ps, con);
		}
		return flg;
	}

}
