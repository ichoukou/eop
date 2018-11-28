package net.ytoec.kernel;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 圆通api客户端调用
 * 
 * @author huangtianfu
 * 
 */
public class YtoApiClient {

	/**
	 * 从圆通同步电子面单<br>
	 * 
	 * 第一次请求: sequence=""<br>
	 * 第二次请求：<br>
	 * 1:保存面单成功，sequence=第一次请求服务器下发的值<br>
	 * 2:保存面单有错，sequence=0
	 * 
	 * @param apiUrl
	 *            api接口的url地址
	 * @param customerCode
	 *            商家代码
	 * @param parternId
	 *            商家密钥
	 * @param clientId
	 *            商家clientId
	 * @param sequence
	 *            请求序列
	 * @return MailNoResponse.java的对象对应的json字符串
	 */
	public static String synWaybillFromYto(String apiUrl, String customerCode,
			String parternId, String clientId, String sequence) {
		String xmlstring = "<?xml version='1.0' encoding='UTF-8'?>"
				+ "<MailNoRequest>" + "<customerCode>" + customerCode
				+ "</customerCode>" + "<sequence>" + sequence + "</sequence>"
				+ "</MailNoRequest>";
		String result = send(xmlstring, apiUrl, parternId, clientId);
		return result;
	}

	/**
	 * 回传订单和电子面单关系给圆通(即:创建订单)
	 * 
	 * @param order
	 * @param apiUrl
	 *            api接口的url地址
	 * @param parternId
	 *            密钥
	 * @param clientId
	 *            商家clientId
	 * @return
	 */
	public static String uploadOrderToYto(OrderInfoUpload order, String apiUrl,
			String parternId, String clientId) {
		// String xmlstring = createXml(order).trim();
		StringBuffer xml = new StringBuffer();

		xml.append("<RequestOrder>");
		xml.append("<clientID>K10101010</clientID>");
		xml.append("<logisticProviderID>YTO</logisticProviderID>");
		xml.append("<customerId>K10101010</customerId>");
		xml.append("<txLogisticID>K1010101012091228010655</txLogisticID>");
		xml.append("<tradeNo>115793345720477</tradeNo>");
		xml.append("<agencyFund>0</agencyFund>");
		xml.append("<mailNo>8004057596</mailNo>");
		xml.append("<type>1</type>");
		xml.append("<orderType>1</orderType>");
		xml.append("<serviceType>0</serviceType>");
		xml.append("<flag>0</flag>");
		xml.append("<sendStartTime/>");
		xml.append("<sendEndTime/>");
		xml.append("<goodsValue>0</goodsValue>");
		xml.append("<itemsValue>0</itemsValue>");
		xml.append("<totalValue>0</totalValue>");
		xml.append("<itemsWeight>0</itemsWeight>");
		xml.append("<sender>");
		xml.append("<name>北京爱慕在线科技有限公司</name>");
		xml.append("<postCode>100012</postCode>");
		xml.append("<phone>400-206-2008</phone>");
		xml.append("<mobile>400-206-2008</mobile>");
		xml.append("<prov>北京</prov>");
		xml.append("<city>北京,朝阳区</city>");
		xml.append("<address>爱慕大厦</address>");
		xml.append("</sender>");
		xml.append("<receiver>");
		xml.append("<name>aaa</name>");
		xml.append("<postCode>111111</postCode>");
		xml.append("<phone>400-258-6953</phone>");
		xml.append("<mobile>13569856985</mobile>");
		xml.append("<prov>北京</prov>");
		xml.append("<city>北京</city>");
		xml.append("<address>爱慕大厦</address>");
		xml.append("</receiver>");
		xml.append("<items>");
		xml.append("<item>");
		xml.append("<itemName>爱慕内衣</itemName>");
		xml.append("<number>0</number>");
		xml.append("<itemValue>0</itemValue>");
		xml.append("</item>");
		xml.append("</items>");
		xml.append("<totalServiceFee>0</totalServiceFee>");
		xml.append("<codSplitFee>0</codSplitFee>");
		xml.append("<orderSource>0</orderSource>");
		xml.append("<insuranceValue>0</insuranceValue>");
		xml.append("<special/>");
		xml.append("<packageOrNot>NO</packageOrNot>");
		xml.append("<remark>易碎品</remark>");
		xml.append("</RequestOrder>");

		String xml1="<RequestOrder><clientID>K10101010</clientID><logisticProviderID>YTO</logisticProviderID><customerId>K10101010</customerId><txLogisticID>K10101010130124669843</txLogisticID><tradeNo>130124669843</tradeNo><agencyFund>0</agencyFund><deliverNo>0</deliverNo><mailNo>8022233317</mailNo><orderType>1</orderType><serviceType>0</serviceType><flag>0</flag><sendStartTime>2013-01-28 05:30:15</sendStartTime><sendEndTime>2013-01-28 09:30:15</sendEndTime><goodsValue>22</goodsValue><itemsValue>8</itemsValue><totalValue>15</totalValue><itemsWeight>0.56</itemsWeight><sender><name>长生鸟物流中心</name><postCode>311804</postCode><phone>400-600-8585</phone><mobile>13575745195</mobile><prov>浙江</prov><city>诸暨市山下湖镇工业园区</city><address>浙江长生鸟珍珠生物科技有限公司</address></sender><receiver><name>ayang</name><postCode>430000</postCode><phone>0571-56836250</phone><mobile>15257147325</mobile><prov>广东省</prov><city>广州市,海珠区</city><address>广东省 广州市 海珠区 昌岗中路211号信和中心1811室</address></receiver><items><item><itemName>长生鸟产品（详见包裹内部）</itemName><number>0</number><itemValue>0.00</itemValue></item></items><totalServiceFee>0</totalServiceFee><codSplitFee>0</codSplitFee><orderSource>0</orderSource><insuranceValue>0.0</insuranceValue><special>0</special><packageOrNot>NO</packageOrNot><remark>暂无</remark></RequestOrder>";
		String result = send(xml1, apiUrl, parternId, clientId);
		return result;
	}

	public static String queryOrderInfo(String apiUrl, String clientId,
			String parternId, List<String> mailNos) {
		StringBuilder sb = new StringBuilder();
		sb.append("<BatchQueryRequest><logisticProviderID>YTO</logisticProviderID><clientID>");
		sb.append(clientId);
		sb.append("</clientID><orders><order>");
		for (String mailNo : mailNos) {
			sb.append("<mailNo>");
			sb.append(mailNo);
			sb.append("</mailNo>");
		}
		sb.append("</order></orders></BatchQueryRequest>");
		String result = send(sb.toString(), apiUrl, parternId, clientId);
		return result;
	}

	/**
	 * 发送http请求
	 * 
	 * @param xmlstring
	 *            xml串
	 * @param apiUrl
	 *            api服务地址
	 * @param parternId
	 *            密钥
	 * @param clientId
	 *            clientId
	 * @return
	 */
	private static String send(String xmlstring, String apiUrl,
			String parternId, String clientId) {
		HttpClient httpClient = new HttpClient();
		String params = "logistics_interface="
				+ encode(xmlstring, HttpClient.UTF8_CHARSET)
				+ "&"
				+ "data_digest="
				+ encode(Md5Encryption.MD5Encode(xmlstring + parternId),
						HttpClient.UTF8_CHARSET) + "&clientId="
				+ encode(clientId, HttpClient.UTF8_CHARSET);
		httpClient.setUrlString(apiUrl);
		httpClient.setRequestMethod(HttpClient.POST_REQUEST_METHOD);
		httpClient.setRequestParams(params);
		String result = httpClient.send();
		return result;
	}

	/**
	 * 请求参数encode
	 * 
	 * @param param
	 *            请求参数
	 * @param charset
	 *            字符集
	 * @return
	 */
	private static String encode(String param, String charset) {
		try {
			return java.net.URLEncoder.encode(param, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 创建订单回传xml串
	 * 
	 * @param order
	 *            订单回传对象
	 * @return
	 */
	private static String createXml(OrderInfoUpload order) {
		StringBuffer sb = new StringBuffer();
		/* 订单基本信息 */
		// sb.append("<?xml version='1.0' encoding='UTF-8'?>");
		sb.append("<RequestOrder>");
		sb.append("<clientID>");
		sb.append(StringUtil.nullValue(order.getClientId(), ""));
		sb.append("</clientID>");
		sb.append("<logisticProviderID>YTO</logisticProviderID>");
		sb.append("<customerId>");
		sb.append(StringUtil.nullValue(order.getCustomerId(), ""));
		sb.append("</customerId>");
		sb.append("<txLogisticID>");
		String newlogistic = StringUtil.nullValue(order.getClientId(), "")
				+ StringUtil.nullValue(order.getTxLogisticId(), "");
		sb.append(newlogistic);
		sb.append("</txLogisticID>");
		sb.append("<tradeNo>");
		if (StringUtils.isEmpty(order.getTraderNo())) {
			sb.append("0");
		} else {
			sb.append(order.getTraderNo());
		}
		sb.append("</tradeNo>");
		sb.append("<deliverNo>");
		if (StringUtils.isEmpty(order.getDeliverNo())) {
			sb.append("0");
		} else {
			sb.append(StringUtil.nullValue(order.getDeliverNo(), "0"));
		}
		sb.append("</deliverNo>");
		sb.append("<agencyFund>");
		if (StringUtils.isEmpty(order.getAgencyFund())) {
			sb.append("0");
		} else {
			sb.append(order.getAgencyFund());
		}
		sb.append("</agencyFund>");
		sb.append("<mailNo>");
		if (StringUtils.isEmpty(order.getMailNo())) {
			sb.append("0");
		} else {
			sb.append(order.getMailNo());
		}
		sb.append("</mailNo>");
		sb.append("<orderType>");
		sb.append(order.getOrderType());
		sb.append("</orderType>");
		sb.append("<serviceType>");
		sb.append(order.getOrderService());
		sb.append("</serviceType>");
		sb.append("<type>1</type>");
		sb.append("<flag>1</flag>");

		/* 物流公司上门取货时间段 */
		sb.append("<sendStartTime>");
		// if(order.getSendStartTime() != null)
		// sb.append(order.getSendStartTime().toString());
		// else{
		// Timestamp time = new Timestamp(new Date().getTime());
		// sb.append(time.toString());
		// }
		sb.append("</sendStartTime>");
		sb.append("<sendEndTime>");
		// if(order.getSendEndTime() != null)
		// sb.append(order.getSendEndTime().toString());
		// else{
		// Timestamp time = new Timestamp(new Date().getTime());
		// sb.append(time.toString());
		// }
		sb.append("</sendEndTime>");

		/* 商品信息 */
		sb.append("<goodsValue>0</goodsValue>");
		sb.append("<itemsValue>0</itemsValue>");
		sb.append("<totalValue>0</totalValue>");
		sb.append("<itemsWeight>0</itemsWeight>");

		/* 发货方信息 */
		sb.append("<sender>");
		sb.append("<name>");
		if (StringUtils.isEmpty(order.getSaleName())) {
			sb.append("暂无");
		} else {
			sb.append(order.getSaleName());
		}
		sb.append("</name>");
		sb.append("<postCode>");
		if (StringUtils.isEmpty(order.getSalePostcode())) {
			sb.append("暂无");
		} else {
			sb.append(order.getSalePostcode());
		}
		sb.append("</postCode>");
		sb.append("<phone>");
		if (StringUtils.isEmpty(order.getSaleTelPhone())) {
			sb.append("暂无");
		} else {
			sb.append(order.getSaleTelPhone());
		}
		sb.append("</phone>");
		sb.append("<mobile>");
		if (StringUtils.isEmpty(order.getSaleMobile())) {
			sb.append("暂无");
		} else {
			sb.append(order.getSaleMobile());
		}
		sb.append("</mobile>");
		sb.append("<prov>");
		if (StringUtils.isEmpty(order.getSaleProv())) {
			sb.append("暂无");
		} else {
			sb.append(order.getSaleProv());
		}
		sb.append("</prov>");
		sb.append("<city>");
		if (StringUtils.isEmpty(order.getSaleCity())) {
			sb.append("暂无");
		} else {
			sb.append(order.getSaleCity() + "," + order.getSaleDistrict());
		}
		sb.append("</city>");
		sb.append("<address>");
		if (StringUtils.isEmpty(order.getSaleAddress())) {
			sb.append("暂无");
		} else {
			sb.append(order.getSaleAddress());
		}
		sb.append("</address>");
		sb.append("</sender>");

		/* 收货方信息 */
		sb.append("<receiver>");
		sb.append("<name>");
		if (StringUtils.isEmpty(order.getBuyName())) {
			sb.append("暂无");
		} else {
			sb.append(order.getBuyName());
		}
		sb.append("</name>");
		sb.append("<postCode>");
		if (StringUtils.isEmpty(order.getBuyPostcode())) {
			sb.append("暂无");
		} else {
			sb.append(order.getBuyPostcode());
		}
		sb.append("</postCode>");
		sb.append("<phone>");
		if (StringUtils.isEmpty(order.getBuyTelphone())) {
			sb.append("暂无");
		} else {
			sb.append(order.getBuyTelphone());
		}
		sb.append("</phone>");
		sb.append("<mobile>");
		if (StringUtils.isEmpty(order.getBuyMobile())) {
			sb.append("暂无");
		} else {
			sb.append(order.getBuyMobile());
		}
		sb.append("</mobile>");
		sb.append("<prov>");
		if (StringUtils.isEmpty(order.getBuyProv())) {
			sb.append("暂无");
		} else {
			sb.append(order.getBuyProv());
		}
		sb.append("</prov>");
		sb.append("<city>");
		if (StringUtils.isEmpty(order.getBuyCity())) {
			sb.append("暂无");
		} else {
			sb.append(order.getBuyCity() + "," + order.getBuyDistrict());
		}
		sb.append("</city>");
		sb.append("<address>");
		if (StringUtils.isEmpty(order.getBuyAddress())) {
			sb.append("暂无");
		} else {
			sb.append(order.getBuyAddress());
		}
		sb.append("</address>");
		sb.append("</receiver>");

		// 取出该订单中的所有商品
		sb.append("<items>");
		sb.append("<item>");
		sb.append("<itemName>");
		if (StringUtils.isEmpty(order.getItemName())) {
			sb.append("暂无");
		} else {
			sb.append(order.getItemName());
		}
		sb.append("</itemName>");
		sb.append("<number>");
		sb.append(order.getItemNumber());
		sb.append("</number>");
		sb.append("<itemValue>0</itemValue>");
		sb.append("<remark>");
		if (StringUtils.isEmpty(order.getRemark())) {
			sb.append("暂无");
		} else {
			sb.append(order.getRemark());
		}
		sb.append("</remark>");
		sb.append("</item>");
		sb.append("</items>");

		sb.append("<insuranceValue>0</insuranceValue>");
		sb.append("<special></special>");
		sb.append("<packageOrNot>NO</packageOrNot>");
		sb.append("<remark>");
		if (StringUtils.isEmpty(order.getRemark())) {
			sb.append("暂无");
		} else {
			sb.append(order.getRemark());
		}
		sb.append("</remark>");

		sb.append("<totalServiceFee>0</totalServiceFee>");
		sb.append("<codSplitFee>0</codSplitFee>");
		sb.append("<orderSource>0</orderSource>");
		sb.append("</RequestOrder>");
		return sb.toString();
	}

	/**
	 * 物流状态通知(商家发送物流状态给易通， 易通然后发给金刚)
	 * 
	 * @param orderStatus
	 * @param apiUrl
	 * @param parternid
	 * @param clientid
	 * @return
	 */
	public static String sendOrderStatusToYto(OrderStautsNodify orderStatus,
			String apiUrl, String parternid, String clientid) {
		
		return null;
	}

}
