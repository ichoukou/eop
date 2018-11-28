package net.ytoec.kernel.test;

import java.io.UnsupportedEncodingException;

import javax.xml.stream.Location;


import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.action.common.XmlSender;

 



public class RequestOrderLocalProcessorTest {
	public static void main(String[] args) {
		RequestOrderLocalProcessorTest t = new RequestOrderLocalProcessorTest();
		t.testParseString();
	}

	public void testParseString() {

		XmlSender xmlSender = new XmlSender();
//		XmlSender xmlSender = new XmlSender(new DomDriver("utf-8"));

		// 创建订单
//		String logisticsInterface = "<RequestOrder><clientID>360buy</clientID> <logisticProviderID>YTO</logisticProviderID><txLogisticID>LP111098989212575666</txLogisticID><customerId>中文</customerId><tradeNo>2005082300225709</tradeNo><mailNo>0571115987</mailNo><type>1</type><flag>1</flag><sender><name>itemname</name><postCode>456300</postCode><phone>231234134</phone><mobile>13575745195</mobile><prov>prov</prov><city>city</city><address>address</address></sender><receiver><name>name</name><postCode>100000</postCode><phone>231234134</phone><mobile>13575745195</mobile><prov>prov</prov><city>city</city><address>address</address></receiver><sendStartTime>2011-07-25 16:00:00.0 CST</sendStartTime><sendEndTime>2011-08-15 17:30:00.0 CST</sendEndTime><itemsValue>2000</itemsValue><itemsWeight>0</itemsWeight><items><item><itemName>Nokia N73</itemName><number>2</number><remark>ASDFAS</remark></item><item><itemName>Nokia N72</itemName><number>1</number><remark>ASDFAS</remark></item></items><insuranceValue>0.0</insuranceValue><packageOrNot>false</packageOrNot><special>5</special><remark>d</remark></RequestOrder>";
		
		String logisticsInterface ="<RequestOrder>"
//  +"<clientID>360buy</clientID>"
  +"<logisticProviderID>YTO</logisticProviderID>"
  +"<txLogisticID>F110722744888</txLogisticID>"
  +"<customerId>anonymous</customerId>"
  +"<mailNo>0722981973</mailNo>"
  +"<type>1</type>"
  +"<flag>1</flag>"
  +"<sender>"
  +"<name>圆通速递8</name>"
  +"<postCode>512000</postCode>"
  +"<mobile>15987985555</mobile>"
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
  +"</RequestOrder>";
			
		// String logisticsInterface =                                                                                                    
		// "<RequestOrder><clientID>360buy</clientID><logisticProviderID>YTO</logisticProviderID><txLogisticID>LP2005082300225709000</txLogisticID><customerId>zhangsan123</customerId><tradeNo>2005082300225709</tradeNo><mailNo>0571115987</mailNo><type>1</type><flag>1</flag><sender> <name>阿毛</name><postCode>456300</postCode><phone>13733828470</phone><mobile>13575745195</mobile><prov>prov</prov><city>city</city><address>address</address></sender><receiver><name>name</name><postCode>100000</postCode><phone>231234134</phone><mobile>13575745195</mobile><prov>prov</prov><city>city</city><address>address</address></receiver><sendStartTime>2011-05-12 16:00:00.0 CST</sendStartTime><sendEndTime>2011-05-12 17:30:00.0 CST</sendEndTime><itemsValue>2000</itemsValue><items><item><itemName>Nokia N73</itemName><number>2</number><remark>ASDFAS</remark></item><item><itemName>Nokia N72</itemName><number>1</number><remark>ASDFAS</remark></item></items><insuranceValue>0.0</insuranceValue><packageOrNot>false</packageOrNot><special>5</special><remark>remarkstring</remark></RequestOrder>"
		// ;
		// logistics_interface=<RequestOrder><clientID>360buy</clientID><
		// logisticProviderID
		// >YTO</logisticProviderID><txLogisticID>LP2005082300225709000
		// </txLogisticID
		// ><customerId>zhangsan123</customerId><tradeNo>2005082300225709
		// </tradeNo
		// ><mailNo>0571115987</mailNo><type>1</type><flag>1</flag><sender>
		// <name
		// >itemname</name><postCode>456300</postCode><phone>231234134</phone
		// ><mobile
		// >13575745195</mobile><prov>prov</prov><city>city</city><address
		// >address
		// </address></sender><receiver><name>name</name><postCode>100000
		// </postCode
		// ><phone>231234134</phone><mobile>13575745195</mobile><prov>prov
		// </prov>
		// <city>city</city><address>address</address></receiver><sendStartTime
		// >2011-05-12 16:00:00.0 CST</sendStartTime><sendEndTime>2011-05-12
		// 17:30:00.0
		// CST</sendEndTime><itemsValue>2000</itemsValue><items><item>
		// <itemName>Nokia
		// N73</itemName><number>2</number><remark>ASDFAS</remark
		// ></item><item><itemName>Nokia
		// N72</itemName><number>1</number><remark>
		// ASDFAS</remark></item></items>
		// <insuranceValue>0.0</insuranceValue><packageOrNot
		// >false</packageOrNot>
		// <special>5</special><remark>remarkstring</remark>
		// </RequestOrder>&data_digest=mGQkdmEu5SZXm1aEHM761Q==
		// String logisticsInterface =
		// "<RequestOrder version='2.0'><clientID>PAIPAI</clientID><logisticProviderID>YTO</logisticProviderID><txLogisticID>201171412846499999</txLogisticID><customerId>PAIPAI</customerId><type>1</type><flag>0</flag><sender><name>kitty</name><postCode></postCode><phone>15929882237</phone><mobile></mobile><prov>陕西</prov><city>渭南市,富平县</city><address>陕西渭南市富平县庄里镇中冶陕压劳人处</address></sender><receiver><name>刘永斌</name><postCode>610000</postCode><phone>13568934389</phone><mobile></mobile><prov>成都市</prov><city>成都市,双流县</city><address>成都公司市场部 转 京东售后仓库 收</address></receiver><sendStartTime>2011-06-30 11:21:48 CST</sendStartTime><sendEndTime>2011-07-03 11:21:48 CST</sendEndTime><items><item><itemName>九牧王旗下格利派蒙 100高档棉时尚渐变短袖T恤(双色可选)CTC02E2610 浅灰色 175/92A（50）</itemName><number>1</number><remark>remark</remark></item></items><insuranceValue>0</insuranceValue><packageOrNot>false</packageOrNot><special>0</special><remark></remark></RequestOrder>"
		// ;
		// -----------------///
		// String logisticsInterface =
		// "<RequestOrder version='2.0'><clientID>PAIPAI</clientID><logisticProviderID>YTO</logisticProviderID><txLogisticID>201271412846499999</txLogisticID><customerId>PAIPAI</customerId><type>1</type><flag>0</flag><sender><name>kitty</name><postCode>456300</postCode><phone>15929882237</phone><mobile>13733828470</mobile><prov>陕西</prov><city>渭南市,富平县</city><address>陕西渭南市富平县庄里镇中冶陕压劳人处</address></sender><receiver><name>刘永斌</name><postCode>610000</postCode><phone>13568934389</phone><mobile>13733728730</mobile><prov>成都市</prov><city>成都市,双流县</city><address>成都公司市场部 转 京东售后仓库 收</address></receiver><sendStartTime>2011-06-30 11:21:48 CST</sendStartTime><sendEndTime>2011-07-03 11:21:48 CST</sendEndTime><items><item><itemName>九牧王旗下格利派蒙 100高档棉时尚渐变短袖T恤(双色可选)CTC02E2610 浅灰色 175/92A（50）</itemName><number>1</number><remark>remark</remark></item></items><insuranceValue>0</insuranceValue><packageOrNot>false</packageOrNot><special>0</special><remark>remark</remark></RequestOrder>"
		// ;
		// 取消订单
		// String logisticsInterface =
		// "<UpdateInfo><txLogisticID>LP3555082300555709009</txLogisticID><clientID>360buy</clientID><infoType>INSTRUCTION</infoType><infoContent>WITHDRAW</infoContent><remark>test</remark></UpdateInfo>"
		// ;
		// 更新订单
/*		 String logisticsInterface =
		 "<UpdateInfo><txLogisticID>LP3555082300555709009</txLogisticID><logisticProviderID>YTO</logisticProviderID><mailNo>0571115987</mailNo><clientID>360buy</clientID><infoType>INSTRUCTION</infoType><infoContent>UPDATE</infoContent><remark>kkk</remark></UpdateInfo>"
		 ;*/
		// 批量查询订单
/*		 String logisticsInterface =
		 "<BatchQueryRequest><logisticProviderID>YTO</logisticProviderID><clientID>360buy</clientID><orders><order><mailNo>0571115987</mailNo></order></orders></BatchQueryRequest>"
		 ;*/
		//String logisticsInterface = "<UpdateInfo><txLogisticID>AP2005032912345678</txLogisticID><logisticProviderID>YTO</logisticProviderID> <infoType>STATUS</infoType><infoContent>ACCEPT</infoContent><remark></remark></UpdateInfo>";

		String utf8LogisticsInterface = encode(logisticsInterface,"ISO-8859-1");
//		String utf8LogisticsInterface = encode(logisticsInterface,"UTF-8");
		String dataDigest = logisticsInterface + "123456";
		String md5DataDigest = Md5Encryption.MD5Encode(dataDigest);
		System.out.println("utf8LogisticsInterface:===" + utf8LogisticsInterface+"===liug==");
		System.out.println("md5DataDigest:===" + md5DataDigest+"===liug==");
		
		String utf8DataDigest = encode(md5DataDigest,xmlSender.UTF8_CHARSET);
		System.out.println("md5utf8DataDigest:===" + utf8DataDigest+"===liug==");

		// String url =
		// "http://jingangtest.yto56.com.cn/ordws/VipOrderServlet?logistics_interface="
		// + utf8LogisticsInterface + "&data_digest=" + utf8DataDigest;
//		String url = "http://jingangtest.yto56.com.cn/ordws/VipOrderServlet";

		String url = "http://localhost:8080/VipOrderLocalServlet.action";
//		String url = "http://192.168.4.113:8080/VipOrderServlet";
//		String url = "http://10.1.198.71/kernel/VipOrderServlet";
//		String url = "http://116.228.70.232/kernel/VipOrderServlet";
//		String url = "http://jingangtest.yto56.com.cn/ordws/VipOrderServlet";
		//String url = "http://10.1.198.71/kernel/kingGang2TaoBao";
//		String url = "http://localhost:8080/kernel/kingGang2TaoBao?clientID=360buy";
		xmlSender.setUrlString(url);
//		xmlSender.
		xmlSender.setRequestMethod(XmlSender.POST_REQUEST_METHOD);   
		System.out.println("================="+System.getProperty("file.encoding"));   
		System.out.println("@@@@@@@"+utf8LogisticsInterface);
		xmlSender.setRequestParams("logistics_interface=" + utf8LogisticsInterface + "&data_digest=" + utf8DataDigest + "&clientID=360buy");
//		xmlSender.setRequestParams("logistics_interface=" + utf8LogisticsInterface + "&data_digest=" + utf8DataDigest);
		String result = xmlSender.send();
		System.out.println(result);

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
	/*
	 * try { //System.out.println(new String("你好123456".getBytes("GBK")));
	 * md5DataDigest = Md5Encryption.MD5Encode(new
	 * String(dataDigest.getBytes("UTF-8"))); System.out.println(md5DataDigest);
	 * } catch (UnsupportedEncodingException e) { // TODO Auto-generated catch
	 * block e.printStackTrace(); }
	 */

}
