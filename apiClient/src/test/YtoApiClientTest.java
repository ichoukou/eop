package test;

import com.api.MailNoResponse;
import com.api.OrderUpload;
import com.api.OrderUploadResponse;
import com.api.YtoApiClient;
import com.model.JdbcConfig;

/**
 * 测试圆通api客户端调用
 * 
 * @author huangtianfu
 * 
 */
public class YtoApiClientTest {
	/*
	 * // 面单同步url // private static String synWaybillUrl = //
	 * "http://116.228.70.232:7800/service_1/api!synWaybill.action";// 易通测试库 //
	 * private static String synWaybillUrl = //
	 * "http://124.74.41.130:8888/api!synWaybill.action";// 易通本地调试-外网(我自己的电脑)
	 * private static final String synWaybillUrl =
	 * "http://localhost:9999/api!synWaybill.action";// 易通本地调试-外网(我自己的电脑)
	 * 
	 * // 回传订单url // private static String uploadOrderUrl //
	 * ="http://116.228.70.232:7800/service_1/CommonOrderServlet.action"; //
	 * private static String uploadOrderUrl = //
	 * "http://124.74.41.130:8888/CommonOrderServlet.action";// //
	 * 易通本地调试-外网(我自己的电脑) private static final String uploadOrderUrl =
	 * "http://localhost:9999/CommonOrderServlet.action";// 易通本地调试-外网(我自己的电脑)
	 */
	/** 请求参数 **/
	private static final String customerCode = "K10101010";// 商家代码
	private static final String parternId = "123456";// 商家密钥
	private static final String clientId = "K10101010";// 电商标识(和商家代码值一样)
	private static final String synWaybillUrl = "http://localhost:9999/api!synWaybill.action";// 同步面单地址
	private static final String uploadOrderUrl = "http://localhost:9999/CommonOrderServlet.action";// 创建订单地址
	//http://192.168.5.26:10000/
	
	/** JDBC参数 **/
	private static String dbDriver = "com.mysql.jdbc.Driver";
	private static String dbUrl = "jdbc:mysql://localhost:3306/surfacebill?characterEncoding=utf-8&autoReconnect=true";
	private static String dbUsername = "root";
	private static String dbPassword = "mysql";

	private static JdbcConfig jdbcConfig;

	static {
		jdbcConfig = new JdbcConfig();
		jdbcConfig.setDriver(dbDriver);
		jdbcConfig.setUrl(dbUrl);
		jdbcConfig.setUsername(dbUsername);
		jdbcConfig.setPassword(dbPassword);
	}

	public static void main(String[] args) {
		//testSynWaybillFromYto();
		 testUploadOrderToYto();
	}

	// 测试同步面单
	public static void testSynWaybillFromYto() {
		// JdbcDao.init(dbUrl, dbUsername, dbPassword, dbDriver);
		MailNoResponse mailNoResponse = YtoApiClient.synWaybillFromYto(
				synWaybillUrl, customerCode, parternId, clientId, jdbcConfig);
		System.out.println(mailNoResponse);
	}

	// 测试回传订单
	public static void testUploadOrderToYto() {
		// JdbcDao.init(dbUrl, dbUsername, dbPassword, dbDriver);
		OrderUpload order = new OrderUpload();
		order.setClientId("K10101010");
		order.setTxLogisticId("DD6663512");
		order.setMailNo("8888222279");
		OrderUploadResponse orderUploadResponse = YtoApiClient
				.uploadOrderToYto(order, uploadOrderUrl, parternId, clientId,
						jdbcConfig);
		System.out.print(orderUploadResponse);
	}

}
