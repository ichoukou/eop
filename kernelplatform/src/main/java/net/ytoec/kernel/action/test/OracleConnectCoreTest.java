package net.ytoec.kernel.action.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	
	private static Connection con = null;
	private static PreparedStatement ps = null;
	private static ResultSet res = null;
	
	private static Date startTime = null;
	private static final int BATCH_SIZE = 1000; //一次读1000条
	private static Logger logger = LoggerFactory
			.getLogger(OracleConnectCoreTest.class);
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
        while (res.next()){
        	String txLogisticId = res.getString("orderLogisticsCode");  // 物流平台的物流号（不能为空）
			String acceptTime = res.getString("operTime"); //揽收时间
			String mailNo = res.getString("waybillNo");    //运单号
			Boolean isOffline = res.getBoolean("isOffline"); //线上，线下
			
			System.err.println("informChannelType = " +res.getShort("informChannelType"));
			
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
