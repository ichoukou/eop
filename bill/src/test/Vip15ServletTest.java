package test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import xml.GetStapeInfo;
import xml.QueryOrder;

public class Vip15ServletTest {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {

//		GetStapeInfo getStapeInfo=new GetStapeInfo();
//		File savefile = new File("D:\\单号查询.xls");
//		String[][] result=getStapeInfo.getData(savefile, 1);
//		int exclLength=result.length;
//		String[] logisticsId=new String[50];
//		int k=0;
//		int j=1;
//		int index=1;
//		for(int i=1;i<exclLength+1;i++){
//			logisticsId[k] = new String(result[i-1][0].toString());
//			k++;
//			if(i%50==0){
//				System.out.println("----------请求金刚接口   写入文件     第"+index+"次"+"开始--------");
//				send(xml(logisticsId),j);
//				System.out.println("----------请求金刚接口  写入文件      第"+index+"次"+"结束----------");
//				System.out.println();
//				System.out.println();
//				index++;
//				j++;
//				k=0;
//				logisticsId=new String[50];
//			}
//			
//		}
		
		send();

	}

//	public static void send(String xml,int step) {
		
		public static void send() {
		try {			
//			String clientId="vipshop";
//			String parterid="jNpKcyXrHfNJ";
// 			String parterid="xendrtgcnk";
			
			
			String clientId="K22002902";
			String parterid="36s8Rg3C";
			
//			String clientId="K999999";
//			String parterid="4VcFF42y";
			
//			String clientId="Ke0000140";
//			String parterid="y7bZuSh7";
// 			
			//五洲在线
//			String clientId="K10101010";
//			String parterid="123456";
			
			
//			String clientId="CANGPEITONG";
//			String parterid="CANGPEITONG123456";
			
//			String clientId="AMAZON";
//			String parterid="abxdfkdakfj";
			
			java.util.Date d = new java.util.Date();
			HttpClient client = new DefaultHttpClient();
			String seq = d.getTime() + "";
			seq = "TT2000000002";
			String logistics_interface = getCreateXml(seq,clientId);

			// String url="http://localhost:8080/ordws/Vip15Servlet";

			// String url="http://192.168.2.202/ordws/Vip15Servlet";//sit

//			String url ="http://jingang.yto56.com.cn/ordws/Vip15Servlet";// uat
//			String url="http://jingang.yto56.com.cn/ordws/TaoBao14Servlet";
//			String url="http://service.yto56.net.cn/CommonOrderServlet.action";
//			String url="http://58.246.240.250:8888/CommonOrderServlet.action";//本机
//			String url="http://192.168.5.26:10000/service_1/CommonOrderServlet.action";//测试环境  内网
//			String url="http://116.228.70.232:7800/service_1/CommonOrderServlet.action";
			String url="http://test.eccore.ytoxl.com/service_1/CommonOrderServlet.action";
//			String url="http://jingangtest.yto56.com.cn/ordws/TaoBao14Servlet";

			// String
			// url="http://10.1.206.146:9082/ordws/Vip15Servlet";//jingang

			java.util.ArrayList<NameValuePair> al = new java.util.ArrayList<NameValuePair>();

			NameValuePair nv1 = new BasicNameValuePair("logistics_interface",
					logistics_interface);
			
			
//			NameValuePair nv1 = new BasicNameValuePair("logistics_interface",
//					xml);
			
			al.add(nv1);
//			NameValuePair nv2 = new BasicNameValuePair("data_digest",
//					EncryptMD5(xml, parterid));// jNpKcyXrHfNJ
			NameValuePair nv2 = new BasicNameValuePair("data_digest",
					EncryptMD5(logistics_interface, parterid));// jNpKcyXrHfNJ
			al.add(nv2);
			NameValuePair type = new BasicNameValuePair("type", "offline");
			al.add(type);
			NameValuePair clientId1 = new BasicNameValuePair("clientId", clientId);
			al.add(clientId1);

			UrlEncodedFormEntity uefe = new UrlEncodedFormEntity(al, "UTF-8");
			HttpPost method = new HttpPost(url);
			method.setEntity(uefe);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String result = client.execute(method, responseHandler);
//			GetStapeInfo traderInbfo=new GetStapeInfo();
			
//			List<QueryOrder> order=traderInbfo.find(result);
			System.out.print(result);
			
			
			//写入文本
			
			
//	        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("D:/steptinfo/stapeInfo.txt"));
			
			/*
	        BufferedWriter osw =new BufferedWriter(new OutputStreamWriter(
	        	      new FileOutputStream("D:/steptinfo/stapeInfo1.txt", true)));
	        System.out.println("\t----------写入文件开始----------");
	        osw.write("走件信息\n");
			for(QueryOrder queryOrder:order){
				if(queryOrder.getMailNo()!=null){
					osw.write("运单号:"+queryOrder.getMailNo().toString()+"\n");
				}
				if(queryOrder.getTxLogisticId()!=null){
					 osw.write("物流号:"+queryOrder.getTxLogisticId().toString()+"\n");
				}
				 for(StepInfo stepInfo:queryOrder.getSteps()){
					 if(stepInfo.getRemark()!=null){
						 osw.write("    地址:"+stepInfo.getAcceptAddress().toString());
					 }
					 if(stepInfo.getName()!=null){
						 osw.write("    姓名:"+stepInfo.getName().toString());
					 }
					 if(stepInfo.getStatus()!=null){
						 osw.write("    走件状态:"+stepInfo.getRemark().toString());
					 }
					 osw.write("\n");
				 }
				 osw.write("\n");
				 
			}
			 osw.flush();
			 osw.close();
			 System.out.println("\t----------写入文件结束----------");
			
			
//			System.out.println(result);
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String EncryptMD5(String input, String parterid)
			throws Exception {

		return new String(Base64.encodeBase64(DigestUtils
				.md5((input + parterid).getBytes("UTF-8"))));
	}
	
	public static String getCreateXml(String seq, String clientId) {

		String xml = "<RequestOrder><clientID>K22000014</clientID><logisticProviderID>YTO</logisticProviderID><customerId>K22000014</customerId><txLogisticID>K22000014jamycnTJ13843767121FC44</txLogisticID><tradeNo>0</tradeNo><agencyFund>0</agencyFund><mailNo>8000804872</mailNo><type>1</type><orderType>1</orderType><serviceType>0</serviceType><flag>0</flag><sendStartTime></sendStartTime><sendEndTime></sendEndTime><goodsValue>0</goodsValue><totalValue>0</totalValue><itemsValue>0</itemsValue><itemsWeight>0</itemsWeight><sender><name>thejamy.tmall.hk(thejamy海外旗舰店)</name><postCode>200000</postCode><phone>1</phone><mobile>1</mobile><prov>上海</prov><city>上海市</city><address>国内退货地址：联明路 555号 7栋 101室.</address></sender><receiver><name>韩愈明</name><postCode>202150</postCode><phone>0</phone><mobile>13701882591</mobile><prov>上海</prov><city>上海</city><address>上海上海市崇明县 城桥镇南门路281号旅游咨询中心</address></receiver><items><item><itemName>배색*레깅스</itemName><number>1</number><itemValue>34800.00</itemValue></item></items><insuranceValue>0</insuranceValue><special></special><packageOrNot>NO</packageOrNot><remark></remark><totalServiceFee>0</totalServiceFee><codSplitFee>0</codSplitFee><orderSource>0</orderSource></RequestOrder>";

		String updateXml = "<UpdateInfo>"
				+ "<txLogisticID>RTN000005</txLogisticID>"
				+ "<mailNo>11000005</mailNo>"
				+ "<logisticProviderID>YTO</logisticProviderID>"
				+ "<infoType>INSTRUCTION</infoType>"
				+ "<infoContent>UPDATE</infoContent>" + "<remark>绑定</remark>"
				+ "</UpdateInfo>";

		String cancelXml = "<UpdateInfo>"
				+ "<logisticProviderID>YTO</logisticProviderID>"
				+ "<txLogisticID>LR100001660350092</txLogisticID>"
				+ "<infoType>INSTRUCTION</infoType>"
				+ "<infoContent>WITHDRAW</infoContent>"
				+ "<remark>商品没了</remark>" + "</UpdateInfo>";

		String queryXml = "<BatchQueryRequest>"
				+ "<logisticProviderID>YTO</logisticProviderID>"
//				+ "<clientID>VIPSHOP</clientID>" 
				+ "<orders>" 
				+ "<order>"
				+ "<mailNo>3253253370</mailNo>" 
				+"</order>" 
				+ "<order>"
				+ "<mailNo>8006818401</mailNo>" 
				+"</order>"
				+ "</orders>" + "</BatchQueryRequest>";

//		String testXml = "<RequestOrder><clientID>K57500007</clientID><logisticProviderID>YTO</logisticProviderID><customerId>K57500007</customerId><txLogisticID>K57500007130124669843</txLogisticID><tradeNo>130124669843</tradeNo><agencyFund>0</agencyFund><deliverNo>0</deliverNo><mailNo>8022233317</mailNo><orderType>1</orderType><serviceType>0</serviceType><flag>0</flag><sendStartTime>2013-01-28 05:30:15</sendStartTime><sendEndTime>2013-01-28 09:30:15</sendEndTime><goodsValue>22</goodsValue><itemsValue>8</itemsValue><totalValue>15</totalValue><itemsWeight>0.56</itemsWeight><sender><name>长生鸟物流中心</name><postCode>311804</postCode><phone>400-600-8585</phone><mobile>13575745195</mobile><prov>浙江</prov><city>诸暨市山下湖镇工业园区</city><address>浙江长生鸟珍珠生物科技有限公司</address></sender><receiver><name>ayang</name><postCode>430000</postCode><phone>0571-56836250</phone><mobile>15257147325</mobile><prov>广东省</prov><city>广州市,海珠区</city><address>广东省 广州市 海珠区 昌岗中路211号信和中心1811室</address></receiver><items><item><itemName>长生鸟产品（详见包裹内部）</itemName><number>0</number><itemValue>0.00</itemValue></item></items><totalServiceFee>0</totalServiceFee><codSplitFee>0</codSplitFee><orderSource>0</orderSource><insuranceValue>0.0</insuranceValue><special>0</special><packageOrNot>NO</packageOrNot><remark>暂无</remark></RequestOrder>";
		String testxml="<BatchQueryRequest><logisticProviderID>YTO</logisticProviderID><clientID>"+clientId+"</clientID>"+
				"<orders>" +
				"<order><mailNo>8080719628</mailNo></order>" +
				//"<order><mailNo>8037251633</mailNo></order>" +
				//"<order><mailNo>8037251506</mailNo></order>" +
//				"<order><mailNo>3306446620</mailNo></order>" +
//				"<order><mailNo>3306427073</mailNo></order>" +
//				"<order><mailNo>3306427134</mailNo></order>" +
//				"<order><mailNo>3306427179</mailNo></order>" +
//				"<order><mailNo>3306427207</mailNo></order>" +
//				"<order><mailNo>3306427194</mailNo></order>" +
//				"<order><mailNo>3306427413</mailNo></order>" +
				"</orders></BatchQueryRequest>";
		
		
		
		
		return xml;
	}

	public static String xml(String[] logisticsId){
		StringBuilder sb = new StringBuilder();
		sb.append("<BatchQueryRequest><logisticProviderID>YTO</logisticProviderID><orders>");
		for (int i = 0; i < logisticsId.length; i++) {
			sb.append("<order><mailNo>");
			sb.append(logisticsId[i]);
			sb.append("</mailNo></order>");
		}
		sb.append("</orders></BatchQueryRequest>");
		String logisticsInterface = sb.toString();
		return logisticsInterface;
		
	}
	
	
	
	
}

