package com.api;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.dao.JdbcDao;
import com.model.JdbcConfig;
import com.utils.HttpClient;
import com.utils.Md5Encryption;
import com.utils.StringUtil;
import com.utils.XMLUtil;

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
	 * @param jdbcConfig
	 * @param sequence
	 *            请求序列
	 * @return
	 */
	public static MailNoResponse synWaybillFromYto(String synWaybillUrl,
			String customerCode, String parternId, String clientId,
			JdbcConfig jdbcConfig) {
		// 1:第一次请求
		boolean saveMailNoSuccess = true;// 是否成功保存拉取到的面单到本地数据库
		String sequence = "";
		String result = YtoApiClient.synWaybill(synWaybillUrl, customerCode,
				parternId, clientId, sequence, true);
		XMLUtil xMLUtils = new XMLUtil(MailNoResponse.class);
		MailNoResponse mailNoResponse = xMLUtils.toObject(result);
		// 2:保存电子面单，设置确认状态
		// 同步面单成功, 保存面单
		if (mailNoResponse != null) {
			if (mailNoResponse.isSuccess()) {
				sequence = mailNoResponse.getSequence();
				List<String> mailNos = mailNoResponse.getMailNoList();// 电子面单号
				/** 保存电子面单,如果失败,设置saveMailNoSuccess=false **/
				if (!mailNos.isEmpty()) {
					boolean isSuccess = JdbcDao.saveMailNos(mailNos,
							customerCode, sequence, jdbcConfig);
					if (!isSuccess) {
						saveMailNoSuccess = false;
					}
				}
				// 3:第二次请求确认
				result = YtoApiClient.synWaybill(synWaybillUrl, customerCode,
						parternId, clientId, sequence, saveMailNoSuccess);
			}
			// 同步面单失败，保存易通返回的报错信息
			else {
				JdbcDao.saveMessage(mailNoResponse.getMessage(), customerCode,
						sequence, "synWaybillFromYto", jdbcConfig);
			}
			mailNoResponse = xMLUtils.toObject(result);
		}
		return mailNoResponse;
	}

	private static String synWaybill(String apiUrl, String customerCode,
			String parternId, String clientId, String sequence,
			boolean saveMailNoSuccess) {
		String xmlString = "<?xml version='1.0' encoding='UTF-8'?>"
				+ "<MailNoRequest>" + "<customerCode>" + customerCode
				+ "</customerCode>" + "<sequence>" + sequence + "</sequence>"
				+ "<success>" + saveMailNoSuccess + "</success>"
				+ "</MailNoRequest>";
		String result = send(xmlString, apiUrl, parternId, clientId);
		return result;
	}

	/**
	 * 回传订单和电子面单关系给圆通(即:创建订单)
	 * 
	 * @param order
	 *            上传的订单对象
	 * @param apiUrl
	 *            api接口的url地址
	 * @param parternId
	 *            密钥
	 * @param clientId
	 *            商家clientId
	 * @return
	 */
	public static OrderUploadResponse uploadOrderToYto(OrderUpload order,
			String apiUrl, String parternId, String clientId,
			JdbcConfig jdbcConfig) {
		String xmlString = createXml(order).trim();
		// xmlString="<RequestOrder><clientID>K22002902</clientID><logisticProviderID>YTO</logisticProviderID><customerId>dfsfsdf</customerId><txLogisticID>K9999990000010</txLogisticID><deliverNo>0</deliverNo><agencyFund>0.0</agencyFund><orderType>1</orderType><serviceType>1</serviceType><flag>0</flag><type>0</type><sender><name>sdfsd</name><postCode>200000</postCode><phone></phone><mobile>13685655555</mobile><prov>dfsd</prov><city>sdfsd,sdfsd</city><address>werwer</address></sender><receiver><name>sdfsd</name><postCode>200000</postCode><phone></phone><mobile>13685655555</mobile><prov>dfsd</prov><city>sdfsd,sdfsd</city><address>werwer</address></receiver><goodsValue>0</goodsValue><totalValue>0</totalValue><itemsValue>0</itemsValue><itemsWeight>0</itemsWeight><items><item><itemName>暂无</itemName><number>0</number><itemValue>0</itemValue><remark>暂无</remark></item></items><insuranceValue>0</insuranceValue><packageOrNot>NO</packageOrNot><special></special><remark>暂无</remark><totalServiceFee>0</totalServiceFee><codSplitFee>0</codSplitFee><orderSource>0</orderSource></RequestOrder>";
		xmlString = "<RequestOrder><clientID>K10101010</clientID><logisticProviderID>YTO</logisticProviderID><customerId>K10101010</customerId><txLogisticID>43354574</txLogisticID><tradeNo>0</tradeNo><agencyFund>0</agencyFund><mailNo>8057901040</mailNo><type>1</type><orderType>1</orderType><serviceType>0</serviceType><flag>0</flag><sendStartTime></sendStartTime><sendEndTime></sendEndTime><goodsValue>15.00</goodsValue><itemsValue>0</itemsValue><totalValue>0</totalValue><itemsWeight>42296</itemsWeight><sender><name>中易浩富(北京)管理咨询有限公司</name><postCode>100191</postCode><phone>010-58856886</phone><mobile>暂无</mobile><prov>北京</prov><city>北京,海淀区</city><address>文慧园小区16号楼底商，圆通-易中物流分部</address></sender><receiver><name>吕化奇</name><postCode>0</postCode><phone>96904</phone><mobile>0</mobile><prov>安徽省</prov><city>滁州市</city><address>安徽省滁州市腰铺镇工业园</address></receiver><items><item><itemName>易中产品</itemName><number>0</number><itemValue>0</itemValue></item></items><insuranceValue>0.0</insuranceValue><special></special><remark></remark><totalServiceFee>0</totalServiceFee><codSplitFee>0</codSplitFee><orderSource>0</orderSource></RequestOrder>";
		// xmlString =
		// "<RequestOrder><clientID>K10101010</clientID><logisticProviderID>YTO</logisticProviderID><customerId>K22000014</customerId><txLogisticID>K22000014jamycnTJ1383167112D767F</txLogisticID><tradeNo>0</tradeNo><agencyFund>0</agencyFund><mailNo>8000890267</mailNo><type>1</type><orderType>1</orderType><serviceType>0</serviceType><flag>0</flag><sendStartTime></sendStartTime><sendEndTime></sendEndTime><goodsValue>0</goodsValue><totalValue>0</totalValue><itemsValue>0</itemsValue><itemsWeight>0</itemsWeight><sender><name>tomonari</name><postCode>200000</postCode><phone>1</phone><mobile>1</mobile><prov>上海</prov><city>上海市</city><address>上海市 闵行区 联名路 555号 7栋 101室</address></sender><receiver><name>陆励</name><postCode>215500</postCode><phone>0512-52252258</phone><mobile>13806232227</mobile><prov>江苏省</prov><city>苏州市</city><address>江苏省苏州市常熟市 虞山北路46-1爱薇妮婚庆会所二楼（苏州金桥旅行社）</address></receiver><items><item><itemName>투웰브 양기모 면바지겉감과안감이 따스한 양기모!겨울 필수아이템 12color 기모면팬츠!!S/M/L</itemName><number>1</number><itemValue>23000.00</itemValue></item><item><itemName>미로틱 니트 티셔츠[한정수량으로 진행!!]실용적인 고급램스울 니트!!</itemName><number>1</number><itemValue>27000.00</itemValue></item><item><itemName>코너킥 패딩조끼따뜻한 모직소재 누빔안감!!스타일까지 살려주는 패딩조끼!! M/L</itemName><number>1</number><itemValue>46000.00</itemValue></item><item><itemName>브리스 체크 NB따뜻한 모직소재 체크남방!한겨울까지 유용하게 활용도굿! S/M/L</itemName><number>1</number><itemValue>25000.00</itemValue></item><item><itemName>에어풀 니트 가디건촉감좋은 와플짜임!! 따듯한 니트가디건!!</itemName><number>1</number><itemValue>19000.00</itemValue></item><item><itemName>에이틴 코튼팬츠 18컬러 코튼팬츠~!</itemName><number>1</number><itemValue>19000.00</itemValue></item><item><itemName>소울 와플 라운드 니트</itemName><number>1</number><itemValue>24000.00</itemValue></item><item><itemName>어페럴 잔단가라 쭉티</itemName><number>1</number><itemValue>9000.00</itemValue></item><item><itemName>16수 코튼 라운드티</itemName><number>1</number><itemValue>12000.00</itemValue></item><item><itemName>징크 포켓 스판 코튼 팬츠</itemName><number>1</number><itemValue>29000.00</itemValue></item><item><itemName>16수 코튼 라운드티</itemName><number>1</number><itemValue>12000.00</itemValue></item><item><itemName>레글런 스탠다드 니트</itemName><number>1</number><itemValue>42000.00</itemValue></item></items><insuranceValue>0</insuranceValue><special></special><packageOrNot>NO</packageOrNot><remark></remark><totalServiceFee>0</totalServiceFee><codSplitFee>0</codSplitFee><orderSource>0</orderSource></RequestOrder>";
		String result = send(xmlString, apiUrl, parternId, clientId);
		XMLUtil xMLUtils = new XMLUtil(OrderUploadResponse.class);
		OrderUploadResponse orderUploadResponse = xMLUtils.toObject(result);
		// 上传失败，保存失败原因
		if (!orderUploadResponse.isSuccess()) {
			JdbcDao.saveMessage(orderUploadResponse.getReason(), clientId,
					null, "uploadOrderToYto", jdbcConfig);
		}
		return orderUploadResponse;
	}

	/**
	 * 创建发送消息
	 * 
	 * @param order
	 * @return
	 */
	private static String createXml(OrderUpload order) {
		String defaultEmptyStr = "暂无";
		StringBuffer sb = new StringBuffer();
		/* 订单基本信息 */
		sb.append("<RequestOrder>");
		sb.append("<clientID>");
		sb.append(StringUtil.nullValue(order.getClientId(), ""));
		sb.append("</clientID>");
		sb.append("<logisticProviderID>YTO</logisticProviderID>");
		sb.append("<customerId>");
		sb.append(StringUtil.nullValue(order.getCustomerId(),
				order.getClientId()));
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
		if (order.getSendStartTime() != null) {
			sb.append(order.getSendStartTime().toString());
		}
		sb.append("</sendStartTime>");
		sb.append("<sendEndTime>");
		if (order.getSendEndTime() != null) {
			sb.append(order.getSendEndTime().toString());
		}
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
			sb.append(defaultEmptyStr);
		} else {
			sb.append(order.getSaleName());
		}
		sb.append("</name>");
		sb.append("<postCode>");
		if (StringUtils.isEmpty(order.getSalePostcode())) {
			sb.append(defaultEmptyStr);
		} else {
			sb.append(order.getSalePostcode());
		}
		sb.append("</postCode>");
		sb.append("<phone>");
		if (StringUtils.isEmpty(order.getSaleTelPhone())) {
			sb.append(defaultEmptyStr);
		} else {
			sb.append(order.getSaleTelPhone());
		}
		sb.append("</phone>");
		sb.append("<mobile>");
		if (StringUtils.isEmpty(order.getSaleMobile())) {
			sb.append(defaultEmptyStr);
		} else {
			sb.append(order.getSaleMobile());
		}
		sb.append("</mobile>");
		sb.append("<prov>");
		if (StringUtils.isEmpty(order.getSaleProv())) {
			sb.append(defaultEmptyStr);
		} else {
			sb.append(order.getSaleProv());
		}
		sb.append("</prov>");
		sb.append("<city>");
		if (StringUtils.isEmpty(order.getSaleCity())) {
			sb.append(defaultEmptyStr);
		} else {
			sb.append(order.getSaleCity() + "," + order.getSaleDistrict());
		}
		sb.append("</city>");
		sb.append("<address>");
		if (StringUtils.isEmpty(order.getSaleAddress())) {
			sb.append(defaultEmptyStr);
		} else {
			sb.append(order.getSaleAddress());
		}
		sb.append("</address>");
		sb.append("</sender>");

		/* 收货方信息 */
		sb.append("<receiver>");
		sb.append("<name>");
		if (StringUtils.isEmpty(order.getBuyName())) {
			sb.append(defaultEmptyStr);
		} else {
			sb.append(order.getBuyName());
		}
		sb.append("</name>");
		sb.append("<postCode>");
		if (StringUtils.isEmpty(order.getBuyPostcode())) {
			sb.append(defaultEmptyStr);
		} else {
			sb.append(order.getBuyPostcode());
		}
		sb.append("</postCode>");
		sb.append("<phone>");
		if (StringUtils.isEmpty(order.getBuyTelphone())) {
			sb.append(defaultEmptyStr);
		} else {
			sb.append(order.getBuyTelphone());
		}
		sb.append("</phone>");
		sb.append("<mobile>");
		if (StringUtils.isEmpty(order.getBuyMobile())) {
			sb.append(defaultEmptyStr);
		} else {
			sb.append(order.getBuyMobile());
		}
		sb.append("</mobile>");
		sb.append("<prov>");
		if (StringUtils.isEmpty(order.getBuyProv())) {
			sb.append(defaultEmptyStr);
		} else {
			sb.append(order.getBuyProv());
		}
		sb.append("</prov>");
		sb.append("<city>");
		if (StringUtils.isEmpty(order.getBuyCity())) {
			sb.append(defaultEmptyStr);
		} else {
			sb.append(order.getBuyCity() + "," + order.getBuyDistrict());
		}
		sb.append("</city>");
		sb.append("<address>");
		if (StringUtils.isEmpty(order.getBuyAddress())) {
			sb.append(defaultEmptyStr);
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
			sb.append(defaultEmptyStr);
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
			sb.append(defaultEmptyStr);
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
			sb.append(defaultEmptyStr);
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

}
