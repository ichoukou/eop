package net.ytoec.kernel.action.remote.process;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.util.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 



public class RequestOrderProcessorTest {
	
	private static Logger logger = LoggerFactory.getLogger(RequestOrderProcessorTest.class);
	public static void main(String[] args) {
		Date startDate=null;
		Date stopDate=null;
		startDate=new Date();
		logger.debug("开始发送时间============="+startDate);
		for (int i = 0; i <10; i++) {
			RequestOrderProcessorTest t = new RequestOrderProcessorTest();
			t.testParseString(i);
		}
		logger.debug("结束发送时间============="+new Date());
		stopDate=new Date();
		int minute =(int)DateUtil.minuteInterval(startDate,stopDate);
		logger.debug("===================耗时=======minute================"+minute);
		
/*		RequestOrderProcessorTest t = new RequestOrderProcessorTest();
		t.testParseString();*/
	}

	public void testParseString(int i) {

		XmlSender xmlSender = new XmlSender();
//		XmlSender xmlSender = new XmlSender(new DomDriver("utf-8"));

		// 创建订单
		//文档提供  有错误
/*		String logisticsInterface = "<RequestOrder version=\"2.0\">"
			+"<logisticProviderID>STO</logisticProviderID>"
			+"<txLogisticID>LP07082300225709</txLogisticID>"
			+"<tradeNo>2007082300225709</tradeNo>"
			+"<mailNo>0571115987</mailNo>"
			+"<flag>1</flag>"
			+"<sender>"
			+"<name>老办法</name>"
			+"<postCode>310013</postCode>"
			+"<phone>231234134</phone>"
			+"<mobile>13575745195</mobile>"
			+"<prov>浙江</prov>"
			+"<city>杭州,西湖区</city>"
			+"<address>华星科技大厦9层</address>"
			+"</sender>"
			+"<receiver>"
			+"<name>可人</name>"
			+"<postCode>100000</postCode>"
			+"<phone>231234134</phone>"
			+"<prov>北京</prov>"
			+"<city>北京市</city>"
			+"<address>address</address>"
			+"</receiver>"
			+"<sendStartTime>2005-08-24 08:00:00.0 CST</sendStartTime>"
			+"<sendEndTime>2005-08-24 12:00:00.0 CST</sendEndTime>"
			+"<items>"
			+"<item>"
			+"<itemName>joi3</itemName>"
			+"<Number>1</Number>"
			+"<remark>ASDFAS</remark>"
			+"</item>"
			+"<item>"
			+"<itemName>joi3</itemName>"
			+"<Number>1</Number>"
			+"<remark>ASDFAS</remark>"
			+"</item>"
			+"</items>"
			+"<insuranceValue>0.0</insuranceValue>"
			+"<packageOrNot>false</packageOrNot>"
			+"<special>0</special>"
			+"<remark>汉字</remark>"
			+"</RequestOrder>";*/
		
		//20110902  liug	淘宝 验证		
/*		String logisticsInterface="<RequestOrder  version=\"2.0\">"
//			 +"<clientID>360buy</clientID>"//xinjia  不可以有
			+"<logisticProviderID>YTO</logisticProviderID>" //n
			+"<txLogisticID>STO64641</txLogisticID>" + //n
//		"<tradeNo>2222222222</tradeNo>" +
//		"<customerId>11</customerId>" +
//		"<mailNo>"+seq+"</mailNo>" +
//		"<mailNo>W238842006</mailNo>" + 
//		"<type>1</type>"+ //xinjia 不可以有
		
//		"<flag>0</flag>"+
		"<sender>" +
			"<name>刘刚6</name>" + //n
			"<postCode></postCode>" +
//			"<phone>0571-88155188</phone>" +
			"<mobile>13905715858</mobile>" +
			"<prov>广东省</prov>" +//n
			"<city>广州市,花都区</city>" +//n
			"<address>新华镇华侨花园15号801室2</address>" +//n
		"</sender>" +
		"<receiver>" +
			"<name>李花都</name>" + //n
			"<postCode></postCode>" +
			"<phone>021-88999999</phone>" +
//			"<mobile>13958585855</mobile>" +
			"<prov>广东省</prov>" +
			"<city>广州市,花都区</city>" +
			"<address>花东镇机场大道1号16F</address>" +
		"</receiver>" +
//		"<sendStartTime>2010-12-19 10:00:00.0 CST</sendStartTime>" +
//		"<sendEndTime>2010-12-19 16:00:00.0 CST</sendEndTime>"+   
//		"<itemsValue>2</itemsValue>"+ //xinjia 不可以有
//		  "<itemsWeight>0</itemsWeight>"+ //xinjia 不可以有
		  
		"<items>" +
			"<item>" +
				"<itemName>测试商品1</itemName>" + //n
				"<number>2</number>" + //n
//				"<remark>测试备注</remark>" +
			"</item>" +
			"<item>" +
				"<itemName>测试商品2</itemName>" +
				"<number>1</number>" +
				"<remark>测试备注</remark>" +
			"</item>" +
		"</items>" +
//		"<insuranceValue>0.00</insuranceValue>" +
//		"<packageOrNot>false</packageOrNot>" +
//		"<special>0</special>" +
//		"<remark>自动联调测试</remark>" +
	"</RequestOrder>";*/
		
//20110902  liug	vip 验证	
/*		String logisticsInterface ="<RequestOrder>"
  +"<clientID>360buy</clientID>" //n
  +"<logisticProviderID>YTO</logisticProviderID>" //n
  +"<txLogisticID>F1107227456533</txLogisticID>" //n
  +"<customerId></customerId>" //w k
//  +"<tradeNo></tradeNo>" //xinjia
//  +"<mailNo></mailNo>"
  +"<type>1</type>"//n
//  +"<flag></flag>"
  +"<sender>"
  +"<name>11122</name>"//n
  +"<postCode></postCode>" 
//  +"<phone></phone>" //xinjia
//  +"<mobile></mobile>"
  +"<prov></prov>"//
  +"<city></city>"
  +"<address>123123123</address>"//n
  +"</sender>"
  +"<receiver>"
  +"<name></name>"
  +"<postCode></postCode>"
//+"<phone></phone>" //xinjia
//  +"<mobile></mobile>"
  +"<prov></prov>"
  +"<city></city>"
  +"<address></address>"
  +"</receiver>"
//  +"<sendStartTime></sendStartTime>"
//  +"<sendEndTime>2011-07-22 00:00:00.0 CST</sendEndTime>"
  +"<itemsValue>2</itemsValue>"
  +"<itemsWeight>0</itemsWeight>"
  +"<items>"
  +"<item>"
  +"<itemName></itemName>"
  +"<number>1</number>"//n
//  +"<remark>CC:Cc;AA:Bb</remark>"
  +"</item>"
  +"<item>"
  +"<itemName></itemName>"
  +"<number>1</number>"//n
  +"</item>"
  +"</items>"
//  +"<insuranceValue></insuranceValue>"
//  +"<packageOrNot>false</packageOrNot>"
//  +"<special>0</special>"
//  +"<remark>中文备注</remark>"
  +"</RequestOrder>";*/
//		vip 正常
/*				String logisticsInterface ="<RequestOrder>"
	  +"<clientID>360buy</clientID>"
	  +"<logisticProviderID>YTO</logisticProviderID>"
	  +"<txLogisticID>F110722745653"+i+"</txLogisticID>"
	  +"<customerId>anonymous</customerId>"
	  +"<mailNo>9997"+i+"</mailNo>"
	  +"<type>1</type>"
	  +"<flag>1</flag>"
	  +"<sender>"
	  +"<name>11122</name>"
	  +"<postCode>512000</postCode>"
	  +"<mobile>13111111111</mobile>"
	  +"<prov>重庆</prov>"
	  +"<city>涪陵</city>"
	  +"<address>123123123</address>"
	  +"</sender>"
	  +"<receiver>"
	  +"<name>曹跃波</name>"
	  +"<postCode>512000</postCode>"
	  +"<mobile>15989138005</mobile>"
	  +"<prov>广东</prov>"
	  +"<city>广州 天河区</city>"
	  +"<address>先烈中路80号</address>"
	  +"</receiver>"
	  +"<sendStartTime>2011-07-22 00:00:00.0 CST</sendStartTime>"
	  +"<sendEndTime>2011-07-22 00:00:00.0 CST</sendEndTime>"
	  +"<itemsValue>2</itemsValue>"
	  +"<itemsWeight>0</itemsWeight>"
	  +"<items>"
	  +"<item>"
	  +"<itemName>testdate2</itemName>"
	  +"<number>1</number>"
	  +"<remark>CC:Cc;AA:Bb</remark>"
	  +"</item>"
	  +"<item>"
	  +"<itemName>XZXZ</itemName>"
	  +"<number>1</number>"
	  +"</item>"
	  +"</items>"
	  +"<insuranceValue>201.00</insuranceValue>"
	  +"<packageOrNot>false</packageOrNot>"
	  +"<special>0</special>"
	  +"<remark>中文备注</remark>"
	  +"</RequestOrder>";*/		
		
		// 取消订单
		// String logisticsInterface =
		// "<UpdateInfo><txLogisticID>LP3555082300555709009</txLogisticID><clientID>360buy</clientID><infoType>INSTRUCTION</infoType><infoContent>WITHDRAW</infoContent><remark>test</remark></UpdateInfo>"
		// ;
		// 更新订单
/*		 String logisticsInterface =
		 "<UpdateInfo><txLogisticID>F110722745653"+i+"</txLogisticID><mailNo>99999999</mailNo><clientID>360buy</clientID><infoType>INSTRUCTION</infoType><infoContent>UPDATE</infoContent><remark>绑定</remark></UpdateInfo>"
		 ;*/
		// 批量查询订单
/*		 String logisticsInterface =
		 "<BatchQueryRequest><logisticProviderID>YTO</logisticProviderID><clientID>360buy</clientID><orders><order><mailNo>0571115987</mailNo></order></orders></BatchQueryRequest>"
		 ;*/
		//String logisticsInterface = "<UpdateInfo><txLogisticID>AP2005032912345678</txLogisticID><logisticProviderID>YTO</logisticProviderID> <infoType>STATUS</infoType><infoContent>ACCEPT</infoContent><remark></remark></UpdateInfo>";
//		String logisticsInterface = "<order></order>";
		//淘宝接口	
/*		 String logisticsInterface ="<RequestOrder>"
			 +"<ecCompanyId>Taobao</ecCompanyId>"
			 +"<logisticProviderID>YTO</logisticProviderID>"
			 +"<customerId>a92266073246b3ed2a2f0ff4d0b2bf5e</customerId>"
			 +"<txLogisticID>LP6543211234560"+i+"</txLogisticID>"
			 +"<tradeNo>2007082300225709</tradeNo>"
			 +"<mailNo>12457954662"+i+"</mailNo>"
			 +"<totalServiceFee>32.0</totalServiceFee>"
			 +"<codSplitFee>200</codSplitFee>"
			 +"<buyServiceFee>20.0</buyServiceFee>"
			 +"<orderType>1</orderType>"
			 +"<serviceType>0</serviceType>"
			 +"<flag>1</flag>"
			 +"<sender>"
			 +"<name>张三</name>"
			 +"<postCode>310013</postCode>"
			 +"<phone>231234134</phone>"
			 +"<mobile>13575745195</mobile>"
			 +"<prov>浙江</prov>"
			 +"<city>杭州,西湖区</city>"
			 +"<address>华星科技大厦9层</address>"
			 +"</sender>"
			 +"<receiver>"
			 +"<name>李四</name>"
			 +"<postCode>100000</postCode>"
			 +"<phone>231234134</phone>"
			 +"<prov>北京</prov>"
			 +"<city>北京市</city>"
			 +"<address>华星科技大厦9层</address>"
			 +"</receiver>"
			 +"<sendStartTime>2005-08-24 08:00:00.0 CST</sendStartTime>"
			 +"<sendEndTime>2005-08-24 12:00:00.0 CST</sendEndTime>"
			 +"<goodsValue>1900</goodsValue>"
			 +"<itemsValue>2000</itemsValue>"
			 +"<items>"
			 +"<item>"
			 +"<itemName>Nokia N73</itemName>"
			 +"<number>2</number>"
			 +"<itemValue>2</itemValue>"
			 +"</item>"
			 +"<item>"
			 +"<itemName>Nokia N72</itemName>"
			 +"<number>1</number>"
			 +"<itemValue>2</itemValue>"
			 +"</item>"
			 +"</items>"
			 +"<insuranceValue>0.0</insuranceValue>"
			 +"<special>0</special>"
			 +"<remark>易碎品</remark>"
			 +"</RequestOrder>";*/	
		 
		 String logisticsInterface ="<UpdateInfo>"
			 +"<logisticProviderID>YTO</logisticProviderID>"
			 +"<txLogisticID>LP6543211234560"+i+"</txLogisticID>"
			 +"<infoType>STATUS</infoType>"
			 +"<infoContent>ACCEPT</infoContent>"
			 +"<remark></remark>"
			 +"</UpdateInfo>";
		 
		String chart ="UTF-8";
		String utf8LogisticsInterface = encode(logisticsInterface,chart);
		String dataDigest = logisticsInterface + "jNpKcyXrHfNJ";
		String md5DataDigest = Md5Encryption.MD5Encode(dataDigest);
//		String md5DataDigest = Md5Encryption.MD5EncodeGBK(dataDigest);
		logger.debug("utf8LogisticsInterface:===:" + utf8LogisticsInterface+":===liug==");
		logger.debug("md5DataDigest:===:" + md5DataDigest+":===liug==");
		
		String utf8DataDigest = encode(md5DataDigest,chart);
		logger.debug("md5utf8DataDigest:===:" + utf8DataDigest+":===liug==");



//		String url = "http://localhost:8080/TaoBaoOrderServlet.action?type=online";
//		String url = "http://localhost:8080/VipOrderServlet.action";
//		String url = "http://localhost:8080/orderLocalProcessAction.action";
//		String url = "http://localhost:8080/AppServlet";
//		String url = "http://10.1.205.17:7801/VipOrderServlet.action";
//		String url = "http://eccore.yto.net.cn/VipOrderServlet.action";
//		eccore.yto.net.cn  测试域名 
//		10.1.5.21       金刚内网ip
//		String url = "http://localhost:8080/kingGang2TaoBao.action";
		String url = "http://localhost:8080/taoBaoStatusNotify.action?type=online";
//		String url = "http://10.1.198.71/kernel/VipOrderServlet.action";
//		String url = "http://116.228.70.232:8888/kernel/VipOrderServlet.action";
//		String url = "http://jingangtest.yto56.com.cn/ordws/VipOrderServlet";
//		String url = "http://jingangtest.yto56.com.cn/ordws/TaoBaoOrderServlet";
		
		xmlSender.setUrlString(url);
		xmlSender.setRequestMethod(XmlSender.POST_REQUEST_METHOD);   
		logger.error("================="+System.getProperty("file.encoding"));   
//		xmlSender.setRequestParams("logistics_interface=" + utf8LogisticsInterface + "&data_digest=" + utf8DataDigest+ "&clientID=360buy");
		xmlSender.setRequestParams("logistics_interface=" + utf8LogisticsInterface + "&data_digest=" + utf8DataDigest+"&ecCompanyId=Taobao");
		String result = xmlSender.send();
		logger.error(result);

	}

	/**
	 * 
	 * URL编码.
	 * @param arg
	 * @param charset
	 * @return
	 */
	private static String encode(String arg, String charset) {
		try {
			return java.net.URLEncoder.encode(arg, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
