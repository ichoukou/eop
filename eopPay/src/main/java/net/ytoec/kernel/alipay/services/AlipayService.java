package net.ytoec.kernel.alipay.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.alipay.config.AlipayConfig;
import net.ytoec.kernel.alipay.util.AlipaySubmit;
import net.ytoec.kernel.dto.AlipayConfigMessage;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/* *
 *类名：AlipayService
 *功能：支付宝各接口构造类
 *详细：构造支付宝各接口请求参数
 */

public class AlipayService {

	/**
	 * 支付宝提供给商户的服务接入网关URL(新)
	 */
	private static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";

	/**
	 * 构造即时到帐接口
	 * 
	 * @param sParaTemp
	 *            请求参数集合
	 * @return 表单提交HTML信息
	 */
	public static String create_direct_pay_by_user(
			Map<String, String> sParaTemp,
			AlipayConfigMessage alipayConfigMessage) {

		// 增加基本配置
		
		if (alipayConfigMessage != null) {
			// 1)没有传账号信息时系统默认的配置
			AlipayConfig.partner = alipayConfigMessage.getAlipayPartner();
			AlipayConfig.seller_email = alipayConfigMessage
					.getAlipaySellerEmail();
			AlipayConfig.input_charset = alipayConfigMessage
					.getAlipayInputCharset();
			AlipayConfig.return_url = alipayConfigMessage.getReturnUrl();
			AlipayConfig.notify_url = alipayConfigMessage.getNotifyUrl();
			AlipayConfig.key = alipayConfigMessage.getAlipayKey();
		}
		// 1)没有传账号信息时系统默认的配置
		sParaTemp.put("service", "create_direct_pay_by_user");
		sParaTemp.put("partner", AlipayConfig.partner);
		sParaTemp.put("return_url", AlipayConfig.return_url); //  + "?userId=" + sParaTemp.get("userId")
		sParaTemp.put("notify_url", AlipayConfig.notify_url);
		sParaTemp.put("seller_email", AlipayConfig.seller_email);
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.remove("userId");
		String strButtonName = "确认";

		return AlipaySubmit.buildForm(sParaTemp, ALIPAY_GATEWAY_NEW, "get",
				strButtonName);
	}

	/**
	 * 用于防钓鱼，调用接口query_timestamp来获取时间戳的处理函数 注意：远程解析XML出错，与服务器是否支持SSL等配置有关
	 * 
	 * @return 时间戳字符串
	 * @throws IOException
	 * @throws DocumentException
	 * @throws MalformedURLException
	 */
	public static String query_timestamp() throws MalformedURLException,
			DocumentException, IOException {

		// 构造访问query_timestamp接口的URL串
		String strUrl = ALIPAY_GATEWAY_NEW + "service=query_timestamp&partner="
				+ AlipayConfig.partner;
		StringBuffer result = new StringBuffer();

		SAXReader reader = new SAXReader();
		Document doc = reader.read(new URL(strUrl).openStream());

		List<Node> nodeList = doc.selectNodes("//alipay/*");

		for (Node node : nodeList) {
			// 截取部分不需要解析的信息
			if (node.getName().equals("is_success")
					&& node.getText().equals("T")) {
				// 判断是否有成功标示
				List<Node> nodeList1 = doc
						.selectNodes("//response/timestamp/*");
				for (Node node1 : nodeList1) {
					result.append(node1.getText());
				}
			}
		}

		return result.toString();
	}
}
