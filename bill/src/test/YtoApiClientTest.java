package test;

import java.util.ArrayList;
import java.util.List;

import net.ytoec.kernel.MailNoResponse;
import net.ytoec.kernel.OrderInfoUpload;
import net.ytoec.kernel.OrderStautsNodify;
import net.ytoec.kernel.XMLUtil;
import net.ytoec.kernel.YtoApiClient;

/**
 * 测试圆通api客户端调用
 * 
 * @author huangtianfu
 * 
 */
public class YtoApiClientTest {

//	private static String apiUrl ="http:localhost:8888/CommonOrderServlet.action";// 创建订单

	// 面单同步url
//	private static String apiUrl = "http://116.228.70.232:7800/service_1/api!synWaybill.action";// 易通测试库
	private static String apiUrl = "http://124.74.41.130:8888/api!synWaybill.action";// 易通本地调试-外网
//	private static String apiUrl = "http://localhost:8888/api!synWaybill.action";// 易通本地调试-外网
	// private static final String
	// apiUrl="http://service.yto56.net.cn/api!synWaybill.action";//易通正式库
	
	//唯品会
	private static final String customerCode = "K10101011";// 商家代码
	private static final String parternId = "123456";// 商家密钥
	private static final String clientId = "K10101011";
	
	//小米武汉仓
	//private static final String customerCode = "K270112279";// 商家代码
	//private static final String parternId = "9oBpXE5x";// 商家密钥
	//private static final String clientId = "K270112279";

	
	public static void main(String[] args) {
		testSynWaybillFromYto();
//		testUploadOrderToYto();
		//testQueryOrder();
	}

	// 测试从圆通同步电子面单
	public static void testSynWaybillFromYto() {
		// 1:第一次请求
		String sequence = "";
		String result = YtoApiClient.synWaybillFromYto(apiUrl, customerCode,
				parternId, clientId, sequence);
		XMLUtil xMLUtils = new XMLUtil(MailNoResponse.class);
		MailNoResponse mailNoResponse = xMLUtils.toObject(result);

		// 2:保存电子面单，设置sequence
		sequence = mailNoResponse.getSequence();// 实际开发时，sequence是需要在第一次请求的返回字符串中，解析出来
		// 保存电子面单,如果失败sequence=0

		// 3:第二次请求
		result = YtoApiClient.synWaybillFromYto(apiUrl, customerCode,
				parternId, clientId, sequence);
		System.out.println(result);
	}

	// 测试上传订单
	public static void testUploadOrderToYto() {
		apiUrl = "http://116.228.70.232:7800/service_1/CommonOrderServlet.action";
//		apiUrl = "http://localhost:8888/CommonOrderServlet.action";
		OrderInfoUpload order = new OrderInfoUpload();
		
		order.setClientId("K10101010");
		String result = YtoApiClient.uploadOrderToYto(order, apiUrl, parternId,
				clientId);
		System.out.print("模拟发送Http请求");
		System.out.print(result);
	}

	// 测试订单详情查询
	public static void testQueryOrder(){
		apiUrl = "http://jingang.yto56.com.cn/ordws/Vip15Servlet";
		//apiUrl = "http://localhost:8888/CommonOrderServlet.action";
		List<String> mailNos=new ArrayList<String>();
		mailNos.add("3253253370");
		String result = YtoApiClient.queryOrderInfo(apiUrl, clientId, parternId, mailNos);
		System.out.println(result);
	}

	// 测试取消订单

	// 测试更新订单
	
	//物流状态通知
	public static void testSendOrderStatusToYto() {
		//apiUrl = "http://116.228.70.232:7800/service_1/CommonOrderServlet.action";
		//apiUrl = "http://10.1.5.82/commonStatusNotify.action";
		apiUrl = "http://localhost:8888/commonStatusNotify.action";
		/**
		 * 接单（针对需要上门揽收服务的订单）
		 * **/
		OrderStautsNodify  orderStatus= new OrderStautsNodify();
		orderStatus.setClientID(clientId);
		orderStatus.setLogisticProviderID("YTO");
		orderStatus.setTxLogisticID("LP07082300225709");
		orderStatus.setInfoType("");
		orderStatus.setInfoContent("ACCEPT");
		orderStatus.setAcceptTime("2007-08-24 08:00:00.0 CST");
		orderStatus.setRemark("");
		String result = YtoApiClient.sendOrderStatusToYto(orderStatus, apiUrl, parternId,
				clientId);
		System.out.print("模拟发送Http请求");
		System.out.print(result);
	}
	

}
