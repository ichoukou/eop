package net.ytoec.kernel.action.remote;

import java.io.UnsupportedEncodingException;
import java.util.List;

import net.ytoec.kernel.action.common.Constants;
import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.action.remote.process.ProcessorUtils;
import net.ytoec.kernel.action.remote.xml.Response;
import net.ytoec.kernel.action.remote.xml.UpdateWaybillInfo;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.service.MQService;
import net.ytoec.kernel.util.ConfigUtilSingle;
import net.ytoec.kernel.util.SystemHelper;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public class MQKingGangOrderAction extends AbstractInterfaceAction {

	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory
			.getLogger(MQKingGangOrderAction.class);

	private static final String GET_METHOD_RESPONSE = "Success";
	// 消息内容
	private static final String LOGISTICS_INTERFACE_PARAM = "logistics_interface";

	// 消息签名
	private static final String DATA_DIGEST_PARAM = "data_digest";
	// TP编号
	private static final String DATA_LOGISTIC_PROVIDER_PARAM = "logistic_provider_id";

	// 电商标识
	private static final String DATA_YTO = "YTO";

	// 消息类型
	private static final String MSG_TYPE = "msg_type";

	public static String ORDER_CANCEL = "TaoBaoOrderCancelSchema.xsd";

	@Autowired
	public MQService mqService;

	/**
	 * 金刚的接口调用
	 * 
	 * @return
	 * @throws Exception
	 */
	public String vipOrderServlet() throws Exception {

		logger.info("易通接收数据 into start");
		if (XmlSender.GET_REQUEST_METHOD.equals(request.getMethod())) {
			super.print(GET_METHOD_RESPONSE);
			return null;
		}
		String classpath = Thread.currentThread().getContextClassLoader()
				.getResource("/").toString();
		if (StringUtils.contains(SystemHelper.OS_NAME.toLowerCase(), "window")) {
			classpath = this.getClass().getProtectionDomain().getCodeSource()
					.getLocation().toString();
		}

		classpath = classpath.replace("file:", "");
		logger.error("classpath:" + classpath);

		logger.debug("kinggang2taobao" + super.request.getRemoteAddr() + ","
				+ super.request.getRemoteHost() + "---------------"
				+ System.getProperty("file.encoding") + "---------------"
				+ super.request.getCharacterEncoding() + "---------------");

		// 1.获取参数值.
		String logisticsInterface = super.request
				.getParameter(LOGISTICS_INTERFACE_PARAM);
		String dataDigest = super.request.getParameter(DATA_DIGEST_PARAM);
		String provider = super.request
				.getParameter(DATA_LOGISTIC_PROVIDER_PARAM);
		String msgType = super.request.getParameter(MSG_TYPE);

		if (logisticsInterface == null || dataDigest == null
				|| provider == null || msgType == null) {

			print(Response.DATA_INSECURITY_RESPONSE_5.toXmlFragment());
			return null;
		}

		// 非法的物流公司
		if (!StringUtils.equals(provider, DATA_YTO)) {
			print(Response.DATA_INSECURITY_RESPONSE_3.toXmlFragment());
			return null;
		}

		// 判断接口是否是（test.jsp）测试使用
		if (request.getParameter("test") != null) {
			logisticsInterface = decode(logisticsInterface,
					XmlSender.UTF8_CHARSET);
			dataDigest = decode(dataDigest, XmlSender.UTF8_CHARSET);
		}

		logger.error("logisticsInterface===" + logisticsInterface);
		logger.debug("dataDigest===" + dataDigest);

		// 金刚安全验证
		if (!validateData(logisticsInterface, dataDigest)) {
			print(Response.DATA_INSECURITY_RESPONSE.toXmlFragment());
			return null;

		}
		// 处理参数
		String xmlFragment = logisticsInterface;

		try {

			// 订单状态更新
			if (StringUtils.equalsIgnoreCase("UPDATE", msgType)) {

				logger.error("===============================订单更新/订单取消开始");
				if (!ProcessorUtils.validateXML(logisticsInterface, classpath
						+ ORDER_CANCEL)) {
					print(Response.DATA_INSECURITY_RESPONSE_1.toXmlFragment());
					logger.error("xml验证失败:" + logisticsInterface
							+ ",classpath:" + classpath);
					return null;
				}

				// 消息值设置
				String msgs = Constants.JINGANG_UPDATE + Constants.MQ_MSG_SPLIT
						+ xmlFragment;

				// 写入消息列中成功的场合
				if (mqService.send(msgs,
						net.ytoec.kernel.common.Constants.DATA_JINGANG)) {

					StringBuffer toXmlFragment = new StringBuffer();

					// 返回报文的头片段设置
					toXmlFragment.append(Response.DATA_HEAD_RESPONSE
							.toXmlFragment5());

					// 获取订单信息数据
					List<UpdateWaybillInfo> updateInfoList = new UpdateWaybillInfo()
							.toObjectTaoBao(xmlFragment);

					// 返回给淘宝的xml文拼接
					if (updateInfoList != null && updateInfoList.size() > 0) {
						for (UpdateWaybillInfo bean : updateInfoList) {
							bean.getResponse().setTxLogisticId(
									bean.getTxLogisticId());
							bean.getResponse()
									.setFieldName(bean.getFieldName());
							bean.getResponse().setLogisticProviderId(
									bean.getLogisticProviderId());
							bean.getResponse().setSuccess("true");
							toXmlFragment.append(bean.getResponse()
									.toXmlFragment3());
						}
						toXmlFragment.append("</responseItems>");
						toXmlFragment.append("</responses>");
						logger.error("===============================订单更新/订单取消 添加到MQ");
						print(toXmlFragment.toString());
					} else {

						// 非法的通知内容
						print(Response.DATA_INSECURITY_RESPONSE_5
								.toXmlFragment());
						return null;
					}

				}

				// 写入消息列中失败的场合
				else {

					// 系统异常，请重试
					print(Response.DATA_INSECURITY_RESPONSE_7.toXmlFragment());
					return null;
				}
				logger.error("===============================订单更新/订单取消结束");
			}

		} catch (Exception re) {
			logger.error("error", re);
		}

		return null;
	}

	/**
	 * 使用指定的字符集charset对字符串arg进行解码,将解码后的字符串返回.
	 * 
	 * @param arg
	 *            待解码字符串.
	 * @param charset
	 * @return
	 */
	private static String decode(String arg, String charset) {

		try {
			return java.net.URLDecoder.decode(arg, charset);
		} catch (UnsupportedEncodingException e) {
			logger.error(LogInfoEnum.PARSE_INVALID.getValue(), e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * 验证参数值.
	 * 
	 * @param logisticsInterface
	 * @param dataDigest
	 * @return
	 */
	private static boolean validateData(String logisticsInterface,
			String dataDigest) {
		String string0 = logisticsInterface
				+ ConfigUtilSingle.getInstance().getPARTERID_TAOBAO();
		String string1 = Md5Encryption.MD5Encode(string0);
		logger.debug(string1 + "    "
				+ ConfigUtilSingle.getInstance().getPARTERID_TAOBAO());
		logger.debug(dataDigest);
		if (dataDigest.equals(string1)) {
			return true;
		}
		return false;

	}
}
