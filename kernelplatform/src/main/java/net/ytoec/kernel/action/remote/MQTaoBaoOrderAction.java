package net.ytoec.kernel.action.remote;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import net.ytoec.kernel.action.common.Constants;
import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.action.remote.process.ProcessorUtils;
import net.ytoec.kernel.action.remote.xml.Response;
import net.ytoec.kernel.action.remote.xml.UpdateWaybillInfo;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dataobject.BuildSearchStatusWeightIndex;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.OrderFormInfo;
import net.ytoec.kernel.service.MQService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.util.ConfigUtilSingle;
import net.ytoec.kernel.util.DateUtil;
import net.ytoec.kernel.util.SystemHelper;

import org.apache.commons.lang.StringUtils;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;

@Controller
@Scope("prototype")
public class MQTaoBaoOrderAction extends AbstractInterfaceAction {

	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory
			.getLogger(MQTaoBaoOrderAction.class);

	private static final String GET_METHOD_RESPONSE = "Success";
	// 消息内容
	private static final String LOGISTICS_INTERFACE_PARAM = "logistics_interface";

	// 消息签名
	private static final String DATA_DIGEST_PARAM = "data_digest";

	// 电商标识
	private static final String CLIENT_ID_PARAM = "ecCompanyId";

	// 消息类型
	private static final String MSG_TYPE = "msg_type";

	public static String ORDER_BIND = "TaoBaoOrderBindSchema.xsd";
	public static String ORDER_CANCEL = "TaoBaoOrderCancelSchema.xsd";
	public static String ORDER_CREATE = "TaoBaoOrderCreateSchema.xsd";
	public static String ORDER_QUERY = "TaoBaoOrderQuerySchema.xsd";
	public static String ORDER_PAY = "TaoBaoCODSchema.xsd";
	@Autowired
	public MQService mqService;
	
	@Autowired
	private OrderService<Order> orderService;

	/**
	 * TAOBAO的接口调用
	 * 
	 * @return
	 * @throws Exception
	 */
	public String vipOrderServlet() throws Exception {

		logger.info(" TAOBAO的接口调用 into start");

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

		/**/
		classpath = classpath.replace("file:", "");
		logger.error("classpath:" + classpath);

		// 获取参数值.
		String logisticsInterface = request
				.getParameter(LOGISTICS_INTERFACE_PARAM);
		String dataDigest = request.getParameter(DATA_DIGEST_PARAM);

		// 电商标识
		String clientID = request.getParameter(CLIENT_ID_PARAM);

		// 消息类型
		String msgType = request.getParameter(MSG_TYPE);

		String ip = null;
		if (request.getHeader("x-forwarded-for") == null) {
			ip = request.getRemoteAddr();
		} else {
			ip = request.getHeader("x-forwarded-for");
		}

		// 判断淘宝发送给物流公司的参数是否为空
		if (logisticsInterface == null || dataDigest == null
				|| clientID == null || msgType == null) {
			print(Response.DATA_INSECURITY_RESPONSE_5.toXmlFragment());
			return null;
		}

		// 对非法IP的权限限制

		/*
		 * boolean ipFlag = IpWhiteListProcessor.checkIp(clientID, ip); if
		 * (!ipFlag) { print("您好，此IP为非法IP，如有疑问请与管理员联系 QQ：2294882345！");
		 * logger.error("非法用户访问！ip：" + ip + "  clientId:" + clientID); return
		 * null; }
		 */

		// 判断接口是否是（test.jsp）测试使用
		if (request.getParameter("test") != null) {
			logisticsInterface = decode(logisticsInterface,
					XmlSender.UTF8_CHARSET);
			dataDigest = decode(dataDigest, XmlSender.UTF8_CHARSET);
		}

		// 数字签名校验
		if (!validateData(logisticsInterface, dataDigest)) {
			print(Response.DATA_INSECURITY_RESPONSE.toXmlFragment());
			logger.error(LogInfoEnum.PARAM_INVALID.getValue() + ",S02"
					+ "logisticsInterface:" + logisticsInterface
					+ ",dataDigest:" + dataDigest);
			return null;
		}

		// 接受参数已经处理
		String xmlFragment = logisticsInterface;
		Response response = new Response();
		try {

			// 支付接口 --> 支付成功
			if (xmlFragment.contains("<UpdateInfo>")
					&& xmlFragment.contains("</UpdateInfo>")
					&& xmlFragment.contains("STATUS")
					&& xmlFragment.contains("PAY_SUCCESS")) {

				logger.error("===============================支付成功开始");

				// xml 格式校验
				if (!ProcessorUtils.validateXML(logisticsInterface, classpath
						+ ORDER_PAY)) {

					// 非法的XML格式
					print(Response.DATA_INSECURITY_RESPONSE_1.toXmlFragment());
					logger.error("xml验证失败:" + logisticsInterface
							+ ",classpath:" + classpath);
					return null;
				}

				UpdateWaybillInfo updateInfo = new UpdateWaybillInfo()
						.toObject(xmlFragment);

				// 消息值设置
				String msgs = Constants.COD_SUCCESS + Constants.MQ_MSG_SPLIT
						+ xmlFragment;

				// 写入消息列中
				if (mqService.send(msgs,
						net.ytoec.kernel.common.Constants.ONLINE_TYPE)) {

					// 返回给淘宝的成功xml
					response.setTxLogisticId(updateInfo.getTxLogisticId());
					response.setLogisticProviderId(updateInfo
							.getLogisticProviderId());
					response.setSuccess("true");
					logger.error("===============================支付成功添加到MQ");
					print(response.toXmlFragment2());
				} else {

					// 系统异常，请重试
					print(Response.DATA_INSECURITY_RESPONSE_7.toXmlFragment());
				}
				logger.error("===============================支付成功结束");
			}

			// 支付接口 --> 支付失败
			else if (xmlFragment.contains("<UpdateInfo>")
					&& xmlFragment.contains("</UpdateInfo>")
					&& xmlFragment.contains("STATUS")
					&& xmlFragment.contains("PAY_FAILED")) {

				logger.error("===============================支付失败开始");

				// xml 格式校验
				if (!ProcessorUtils.validateXML(logisticsInterface, classpath
						+ ORDER_PAY)) {

					// 非法的XML格式
					print(Response.DATA_INSECURITY_RESPONSE_1.toXmlFragment());
					logger.error("xml验证失败:" + logisticsInterface
							+ ",classpath:" + classpath);
					return null;
				}

				UpdateWaybillInfo updateInfo = new UpdateWaybillInfo()
						.toObject(xmlFragment);

				// 消息值的设置
				String msgs = Constants.COD_FAILED + Constants.MQ_MSG_SPLIT
						+ xmlFragment;

				// 写入消息列中成功的场合
				if (mqService.send(msgs,
						net.ytoec.kernel.common.Constants.ONLINE_TYPE)) {

					// 返回给淘宝的xml
					response.setTxLogisticId(updateInfo.getTxLogisticId());
					response.setLogisticProviderId(updateInfo
							.getLogisticProviderId());
					response.setSuccess("true");
					print(response.toXmlFragment2());
					logger.error("===============================支付失败添加到MQ");
				}

				// 写入消息列中失败的场合
				else {

					// 系统异常，请重试
					print(Response.DATA_INSECURITY_RESPONSE_7.toXmlFragment());
				}
				logger.error("===============================支付失败结束");
			}

			// 订单创建
			else if (StringUtils.equalsIgnoreCase("ORDERCREATE", msgType)) {

				logger.error("===============================订单创建开始");

				// xml 格式校验
				if (!ProcessorUtils.validateXML(logisticsInterface, classpath
						+ ORDER_CREATE)) {

					// 非法的XML格式
					print(Response.DATA_INSECURITY_RESPONSE_1.toXmlFragment());
					logger.error("xml验证失败:" + logisticsInterface
							+ ",classpath:" + classpath);
					return null;
				}

				// 报文对象化
				OrderFormInfo requestOrder = null;
				try {
					requestOrder = new OrderFormInfo().toObject(xmlFragment);
				} catch (Exception spe) {
					print("解析xml出错." + request.getMethod()
							+ Constants.MQ_MSG_SPLIT + spe.getMessage());
					logger.error(LogInfoEnum.PARSE_INVALID.getValue(), spe);
					return null;
				}
				logger.debug("orderCreate" + requestOrder.getTxLogisticId());

				// 根据服务类型来判断 下单类型(online/offline)
				String type = getType(requestOrder.getServiceType());
				if (StringUtils.isEmpty(requestOrder.getOrderType())
						|| StringUtils.isEmpty(requestOrder.getServiceType())
						|| (StringUtils.isEmpty(requestOrder.getSender()
								.getMobile()) && StringUtils
								.isEmpty(requestOrder.getSender().getPhone()))
						|| (StringUtils.isEmpty(requestOrder.getReceiver()
								.getMobile()) && StringUtils
								.isEmpty(requestOrder.getReceiver().getPhone()))
						|| (StringUtils.equals("offline", type) && StringUtils
								.isEmpty(requestOrder.getMailNo()))) {

					requestOrder.getResponse().setTxLogisticId(
							requestOrder.getTxLogisticId());
					requestOrder.getResponse().setLogisticProviderId(
							requestOrder.getLogisticProviderId());
					requestOrder.getResponse().setSuccess("false");
					requestOrder.getResponse().setReason("S05");
					String toXmlFragment = requestOrder.getResponse()
							.toXmlFragment1();

					return toXmlFragment;
				}

				String partitiondate = DateUtil.format(new Date(),
						Constants.DATE_FORMAT_yyyyMMdd);

				// 消息值的设置
				String msgs = msgType + Constants.MQ_MSG_SPLIT + xmlFragment
						+ Constants.MQ_MSG_SPLIT + partitiondate;

				// 写入消息列中成功的场合
				if (mqService.send(msgs, type)) {

					// 返回给淘宝的xml
					response.setTxLogisticId(requestOrder.getTxLogisticId());
					response.setLogisticProviderId(requestOrder
							.getLogisticProviderId());
					response.setSuccess("true");
					logger.error("===============================订单创建添加到MQ");
					print(response.toXmlFragment2());
				}

				// 写入消息列中失败的场合
				else {

					// 返回给淘宝的xml
					print(Response.DATA_INSECURITY_RESPONSE_7.toXmlFragment());
					return null;
				}
				logger.error("===============================订单创建结束");
			}

			// 订单更新/消除订单
			else if (StringUtils.equalsIgnoreCase("UPDATE", msgType)) {
				logger.error("===============================订单更新/订单取消开始");
				if (!ProcessorUtils.validateXML(logisticsInterface, classpath
						+ ORDER_CANCEL)) {

					// 非法的XML格式
					print(Response.DATA_INSECURITY_RESPONSE_1.toXmlFragment());
					logger.error("xml验证失败:" + logisticsInterface
							+ ",classpath:" + classpath);
					return null;
				}

				// 消息值设置
				String msgs = msgType + Constants.MQ_MSG_SPLIT + xmlFragment;

				// 写入消息列中成功的场合
				if (mqService.send(msgs,
						net.ytoec.kernel.common.Constants.UPDATE_TYPE)) {

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

	private static String getType(String serviceType) {

		// 将服务类型转换成二进制标识
		serviceType = Integer.toBinaryString(Integer.parseInt(serviceType));
		serviceType = serviceType.substring(serviceType.length() - 1,
				serviceType.length());
		if (StringUtils.equals("1", serviceType)) {
			return "online";
		} else if (StringUtils.equals("0", serviceType)) {
			return "offline";
		}
		return "offline";
	}

/*	*//**
	 * 易通solar订单更新MQ消息设置
	 * 
	 * @param orderUpdateInfo
	 * @return
	 *//*
	public String getObjectFromJson(String txLogisticId) {
		try {
			BuildSearchStatusWeightIndex bean = orderService
					.getSolarDto(txLogisticId);
			if (bean != null) {
				String jsonString = JSON.toJSONString(bean);
				return jsonString;
			}
		} catch (Exception e) {
			Log.error(
					"MQTaoBaoOrderAction getObjectFromJson error ",
					e);
		}
		return null;
	}*/
}
