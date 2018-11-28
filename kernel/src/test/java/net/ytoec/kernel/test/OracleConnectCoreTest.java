package net.ytoec.kernel.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.ytoec.kernel.action.remote.xml.UpdateInfo;
import net.ytoec.kernel.dataobject.NotifyType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.ibm.icu.text.SimpleDateFormat;

/**
 * oracle thin 连接测试
 * oracle连接有oci 和 thin 两种方式
 * @author wmd
 *
 */
public class OracleConnectCoreTest {

	private static final String url = "jdbc:oracle:thin:@(DESCRIPTION =" +
	"(ADDRESS_LIST =" +
	"(ADDRESS = (PROTOCOL = TCP)" +
				  "(HOST = 10.1.195.151)" +
				  "(PORT = 1521)" +
	"))" +
	"(CONNECT_DATA =(SID = exprd1)" +
					"(SERVER = DEDICATED)" +
	"))";
	private static final String userName = "YTORD";
	private static final String passWord = "YTORD";
	private static final String tableName = "YTORD.T_ORD_ORDER_MESSAGE";
	
	/**
	 * 物流公司编号
	 */
	private static final String LOGISTIC_PROVIDER_ID = "YTO";
	/**
	 * 
	 */
	private static final String INFO_TYPE_INSTRUCTION = "INSTRUCTION";

	/**
	 * 确认订单
	 */
	private static final String INFO_CONTENT_CONFIRM = "CONFIRM";

	/**
	 * 
	 */
	private static final String INFO_TYPE = "STATUS";

	/**
	 * 接单
	 */
	private static final String INFO_CONTENT_ACCEPT = "ACCEPT";

	/**
	 * 不接单
	 */
	private static final String INFO_CONTENT_UNACCEPT = "UNACCEPT";

	/**
	 * 揽收成功
	 */
	private static final String INFO_CONTENT_GOT = "GOT";

	/**
	 * 揽收不成功
	 */
	private static final String INFO_CONTENT_NOT_SEND = "NOT_SEND";

	/**
	 * 送达成功
	 */
	private static final String INFO_CONTENT_SIGNED = "SIGNED";
	/**
	 * 派件扫描
	 */
	private static final String INFO_CONTENT_SENT_SCAN = "SENT_SCAN";

	/**
	 * 送达失败
	 */
	private static final String INFO_CONTENT_FAILED = "FAILED";
	/**
	 * 出站
	 */
	private static final String STATUS_STATION_OUT = "TMS_STATION_OUT";
	
	private static Connection con = null;
	private static PreparedStatement ps = null;
	private static ResultSet res = null;
	
	private static Date startTime = null;
	private static final int BATCH_SIZE = 1000; //一次读1000条
	private static Logger logger = Logger.getLogger(OracleConnectCoreTest.class);
	public static void main(String[] args) throws SQLException {
		
        initDrives(); //注册驱动
        con = getConnection(); //获取连接
        
        if(con == null){
        	logger.error("创建连接失败!");
        	return ;
        }
        Integer batchSize = 1;
        Integer batchNo = 0;
        initParams(); //初始化查询条件
        /**
         * OrderStatusConsumerImpl
         * MESSAGE_TYPE:
         * 		INTERFACE((short)1, "接口通知"),
		 * 		MAIL((short)2, "邮件通知"),
		 * 		SMS((short)3, "短信通知"),
		 * 		PDA((short)4, "PDA通知"),
		 * 		TAOBAO_INTERFACE((short)5, "淘宝接口通知")
		 * where MESSAGE_TIME >= "+startTime+" ORDER BY MESSAGE_TIME DESC
         */
        String sql = "select message.ID id,message.ORDER_ID orderId,message.ORDER_LOGISTICS_CODE orderLogisticsCode," +
		"message.ORDER_CHANNEL_CODE orderChannelCode,message.WAYBILL_NO waybillNo, message.MESSAGE_TYPE messageType, " +
		"message.MESSAGE_TIME messageTime, message.INFORM_CHANNEL_TYPE informChannelType, message.INFORM_CONTENT informContent, " +
		"message.OPER_TIME operTime, message.OPER_ORG_CODE operOrgCode, message.OPER_ORG_NAME operOrgName, message.OPER_EMP_CODE operEmpCode, " +
		"message.OPER_EMP_NAME operEmpName, message.CURR_CITY_ID currCityId, message.CURR_CITY_NAME currCityName, message.DEST_CITY_ID destCityId, " +
		"message.DEST_CITY_NAME destCityName, message.WEIGHT weight, message.IS_OFFLINE isOffline,message.LOGIN_ID loginId " +
		"from ( select * from "+tableName+" ORDER BY MESSAGE_TIME DESC) message where ORA_HASH(SUBSTR(ORDER_LOGISTICS_CODE,-2), "+ 
		( batchSize - 1 ) + ") = "+batchNo+" and MESSAGE_TYPE = "+1+" and rownum <= "+BATCH_SIZE+"";
        
        
        logger.error("更新订单状态, SQL:" + sql);
        System.out.println("sql is :"+sql);
        ps = con.prepareStatement(sql);
        res = ps.executeQuery();
        System.out.println("查询结果对象值为："+res);
        UpdateInfo updateInfo = new UpdateInfo(); 
        List<UpdateInfo> updateOrderList = new ArrayList<UpdateInfo>();
        String clientId = "Taobao" ;
        while (res.next()){
        	String txLogisticId = res.getString("orderLogisticsCode");  // 物流平台的物流号（不能为空）
        	String logisticProviderId = LOGISTIC_PROVIDER_ID; //物流公司编号
			String acceptTime = res.getString("operTime"); //揽收时间
			String mailNo = res.getString("waybillNo");    //运单号
			Boolean isOffline = res.getBoolean("isOffline"); //线上，线下
			
			String type = (isOffline != null && isOffline)?"offline":"online";
			System.err.println("订单类型为:"+type);
			/**创建通知类型**/
			System.err.println("informChannelType = " +res.getShort("informChannelType"));
			createNotifyTypeProcessor(updateInfo,res.getShort("informChannelType")); 
			
			updateInfo.setTxLogisticID(txLogisticId);
			updateInfo.setLogisticProviderID(logisticProviderId);//物流平台的物流号
			//updateInfo.setAcceptTime(acceptTime);
			updateInfo.setMailNo(mailNo);
			updateInfo.setClientId(clientId);
			updateInfo.setSuccess("true");
			
			/**参数校验**/
			if(validateParams(updateInfo)){
				logger.info(".......为空校验失败.........!");
				continue;
			}
			updateOrderList.add(updateInfo);
        }
        System.err.println("共查询到淘宝订单"+updateOrderList.size()+"条数据");
	}

	/**
	 * 查询订单状态数据校验
	 */
	private static boolean validateParams(UpdateInfo info) {
		// TODO Auto-generated method stub
		boolean b = false ;
		if (StringUtils.isEmpty(info.getTxLogisticID()) || StringUtils.isEmpty(info.getInfoType())
				|| StringUtils.isEmpty(info.getInfoContent()) || ("SIGNED".equals(info.getInfoContent().toUpperCase()) 
						 && StringUtils.isEmpty(info.getMailNo()))) {
			System.err.println(info.getTxLogisticID());
			System.err.println(info.getInfoType());
			System.err.println(info.getInfoContent());
			System.err.println(info.getAcceptTime());
			System.err.println(info.getMailNo());
			b = true ;
		}
		return  b;
	}

	/**
	 * 创建订单状态
	 * @param short1
	 */
	private static void createNotifyTypeProcessor(UpdateInfo updateInfo,short informChannelType) {
		if (NotifyType.ACCEPT.getValue().equals(informChannelType)) {
			updateInfo.setInfoContent(INFO_CONTENT_ACCEPT);
			updateInfo.setInfoType(INFO_TYPE);
		}
		
		if (NotifyType.UNACCEPT.getValue().equals(informChannelType)) {
			updateInfo.setInfoContent(INFO_CONTENT_UNACCEPT);
			updateInfo.setInfoType(INFO_TYPE);
		}
		
		if (NotifyType.GOT.getValue().equals(informChannelType)) {
			updateInfo.setInfoContent(INFO_CONTENT_GOT);
			updateInfo.setInfoType(INFO_TYPE);
		}
		
		if (NotifyType.NOT_SEND.getValue().equals(informChannelType)) {
			updateInfo.setInfoContent(INFO_CONTENT_NOT_SEND);
			updateInfo.setInfoType(INFO_TYPE);
		}
		
		if (NotifyType.SIGNED.getValue().equals(informChannelType)) {
			updateInfo.setInfoContent(INFO_CONTENT_SIGNED);
			updateInfo.setInfoType(INFO_TYPE);
		}
		
		if (NotifyType.FAILED.getValue().equals(informChannelType)) {
			updateInfo.setInfoContent(INFO_CONTENT_FAILED);
			updateInfo.setInfoType(INFO_TYPE);
		}
		
		if (NotifyType.CONFIRM.getValue().equals(informChannelType)) {
			updateInfo.setInfoContent(INFO_CONTENT_CONFIRM);
			updateInfo.setInfoType(INFO_TYPE_INSTRUCTION);
		}
		
		if (NotifyType.PACKAGE.getValue().equals(informChannelType)) {
			// 建包 ??
		}

		if (NotifyType.DELIVERY.getValue().equals(informChannelType)) {
			updateInfo.setInfoContent(INFO_CONTENT_SENT_SCAN);
			updateInfo.setInfoType(INFO_TYPE);
		}
		
		if (NotifyType.TRANSFER.getValue().equals(informChannelType)) {
			updateInfo.setInfoType(STATUS_STATION_OUT);
		}
		
	}
	/**
	 * 初始化查询条件
	 */
	 private static void initParams() {
		// TODO Auto-generated method stub
		Date now = Calendar.getInstance().getTime();
		// 只查询5天前发生的数据
		startTime = DateUtils.addHours(now, -120);
		//String dateFormat = new SimpleDateFormat("yyyy-MM-dd hs24:mm:ss").format(startTime);
		System.out.println("起始日期为："+startTime);
	}

	/**
	  *  释放资源
	  * @param rs
	  * @param stmt
	  * @param conn
	  */
    public static void free(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {

        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {

            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {

                }
            }
        }
    }

	/**
	 * 获取连接
	 */
	private static Connection getConnection() {
		// TODO Auto-generated method stub
		 try {
			return DriverManager.getConnection(url, userName, passWord);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 注册驱动
	 */
	private static void initDrives() {
		// TODO Auto-generated method stub
		 // 注册驱动
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
	}
}
