package net.ytoec.kernel.action.remote;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.action.remote.process.ProcessorUtils;
import net.ytoec.kernel.action.remote.xml.Response;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dataobject.ApplyKdbz;
import net.ytoec.kernel.dataobject.Customer;
import net.ytoec.kernel.dataobject.SendTask;
import net.ytoec.kernel.dataobject.SendTaskToTB;
import net.ytoec.kernel.service.ApplyKdbzService;
import net.ytoec.kernel.util.ConfigUtilSingle;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 提供给TAOBAO的接口地址Action.
 */
@Controller
@Scope("prototype")
public class TaoBaoServiceApplyAction extends AbstractInterfaceAction {

	private static final long serialVersionUID = 1031890960559598071L;
	private static final String LOGISTICS_INTERFACE_PARAM = "logistics_interface";
	private static final String DATA_DIGEST_PARAM = "data_digest";
	private static final String CLIENT_ID_PARAM = "ecCompanyId";
	private static final String GET_METHOD_RESPONSE = "Success";
	// 消息类型
	private static final String MSG_TYPE = "msg_type";

	public static String ORDER_BIND = "TaoBaoOrderBindSchema.xsd";
	public static String ORDER_CANCEL = "TaoBaoOrderCancelSchema.xsd";
	public static String ORDER_CREATE = "TaoBaoOrderCreateSchema.xsd";
	public static String ORDER_QUERY = "TaoBaoOrderQuerySchema.xsd";

	/**
	 * 金刚接口访问地址.
	 */
	private static final String JIN_GANG_SERVICEAPPLY_ORDER = "JINGGANG.SERVICEAPPLY.ORDER";

	@Autowired
	private ApplyKdbzService<ApplyKdbz> applyKdbzService;

	private static Logger logger = LoggerFactory
			.getLogger(TaoBaoServiceApplyAction.class);

	/**
	 * 解析接口调用,淘宝调用金刚.
	 * 
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public String taobaoServiceApplyServlet() throws IOException {
		try {

			// 1.获取参数值.
			String logisticsInterface = request
					.getParameter(LOGISTICS_INTERFACE_PARAM);
			String dataDigest = request.getParameter(DATA_DIGEST_PARAM);
			String clientID = request.getParameter(CLIENT_ID_PARAM);
			String type = request.getParameter(MSG_TYPE);
			
			if (!ProcessorUtils.PROCESS_SWITCH) {
				return null;
			}
			// 判断接口是否是（test.jsp）测试使用
			if (request.getParameter("test") != null) {
				logisticsInterface = decode(logisticsInterface,
						XmlSender.UTF8_CHARSET);
				dataDigest = decode(dataDigest, XmlSender.UTF8_CHARSET);
			}
			// liug编码---发送给金刚
			String jglogisticsInterface = null;
			String jgdataDigest = null;

			jglogisticsInterface = encode(logisticsInterface,
					XmlSender.UTF8_CHARSET);
			jgdataDigest = encode(dataDigest, XmlSender.UTF8_CHARSET);

			// 验证
			logger.debug("taobaoServiceApplyServlet:logisticsInterface==="
					+ logisticsInterface);
			logger.debug("taobaoServiceApplyServlet:dataDigest===" + dataDigest);

			String xmlFragment = logisticsInterface;
			ApplyKdbz applyKdbz = new ApplyKdbz();
			applyKdbz.toObject(xmlFragment);
			String responseString = null;

			try {
				if (xmlFragment.contains("<ServiceApply")
						&& xmlFragment.contains("</ServiceApply")) {

					SendTask sendTask = new SendTask();
					sendTask.setOrderId(0);
					sendTask.setClientId(clientID);
					sendTask.setRemark(responseString);
					sendTask.setTaskFlagId(0);
					sendTask.setTaskFlag(String.valueOf(0));
					sendTask.setTxLogisticId(applyKdbz.getRequestNo());
					sendTask.setRequestURL(Resource
							.getChannel(JIN_GANG_SERVICEAPPLY_ORDER));
					sendTask.setRequestParams(LOGISTICS_INTERFACE_PARAM + "="
							+ jglogisticsInterface + "&" + DATA_DIGEST_PARAM
							+ "=" + jgdataDigest+ "&" + MSG_TYPE + "="
							+ type);

					responseString = applyKdbzCreate(applyKdbz, sendTask);
					super.print(responseString);
					return null;
				} else if (xmlFragment.contains("<ServiceCancel")
						&& xmlFragment.contains("</ServiceCancel")) {

					SendTask sendTask = new SendTask();
					sendTask.setOrderId(0);
					sendTask.setClientId(clientID);
					sendTask.setRemark(responseString);
					sendTask.setTaskFlagId(0);
					sendTask.setTaskFlag(String.valueOf(0));
					sendTask.setTxLogisticId(applyKdbz.getRequestNo());
					sendTask.setRequestURL(Resource
							.getChannel(JIN_GANG_SERVICEAPPLY_ORDER));
					sendTask.setRequestParams(LOGISTICS_INTERFACE_PARAM + "="
							+ jglogisticsInterface + "&" + DATA_DIGEST_PARAM
							+ "=" + jgdataDigest + "&" + MSG_TYPE + "=" + type);
					responseString = applyKdbzCancel(applyKdbz, sendTask);
					super.print(responseString);
					return null;

				}
			} catch (Exception re) {
				logger.error("", re);
				responseString = Response.DATA_INSECURITY_RESPONSE_9
						.toXmlFragment();
				print(responseString);
				logger.error("order create failed:" + logisticsInterface);
				return null;
			}
		} catch (Exception re) {
			logger.error("", re);
			print(Response.DATA_INSECURITY_RESPONSE_9.toXmlFragment());
			return null;
		}
		return null;
	}

	// 申请服务
	private String applyKdbzCreate(ApplyKdbz applyKdbz, SendTask sendTask) {
		try {
			logger.debug("申请服务");
			Customer customer = applyKdbz.getCustomer();
			if (StringUtils.isEmpty(applyKdbz.getEcCompanyId())
					|| StringUtils.isEmpty(applyKdbz.getLogisticProvider())
					|| StringUtils.isEmpty(applyKdbz.getRequestNo())
					|| StringUtils.isEmpty(applyKdbz.getCustomerId())
					|| StringUtils.isEmpty(applyKdbz.getServiceType())
					|| StringUtils.isEmpty(applyKdbz.getStatus())
					|| StringUtils.isEmpty(customer.getName())
					|| (StringUtils.isEmpty(customer.getMobile()) && StringUtils
							.isEmpty(customer.getPhone()))
					|| StringUtils.isEmpty(customer.getAddress())) {

				applyKdbz.getResponse().setLogisticProviderId(
						applyKdbz.getLogisticProvider());
				applyKdbz.getResponse().setRequestNo(applyKdbz.getRequestNo());
				applyKdbz.getResponse().setSuccess("false");
				String toXmlFragment = applyKdbz.getResponse().toXmlFragment1();
				logger.error("serviceApply parma is empty,xml:" + toXmlFragment);
				return toXmlFragment;
			}
			applyKdbz.setName(customer.getName());
			applyKdbz.setMobile(customer.getMobile());
			applyKdbz.setPhone(customer.getPhone());
			applyKdbz.setAddress(customer.getAddress());

			boolean flag = false;
			try {

				flag = this.applyKdbzService.addApplyKdbz(applyKdbz, sendTask);
			} catch (Exception e) {
				logger.error("服务申请失败", e);
			}

			applyKdbz.getResponse().setLogisticProviderId(
					applyKdbz.getLogisticProvider());
			applyKdbz.getResponse().setRequestNo(applyKdbz.getRequestNo());
			if (flag) {
				logger.debug("applyKdbzCreate" + "申请成功");
				applyKdbz.getResponse().setSuccess("true");

			} else {
				logger.error("applyKdbzCreate" + "申请失败");
				applyKdbz.getResponse().setSuccess("false");

			}
			String toXmlFragment = applyKdbz.getResponse().toXmlFragment1();
			return toXmlFragment;
		} catch (Exception re) {
			logger.error("", re);
			try {
				print(Response.DATA_INSECURITY_RESPONSE_9.toXmlFragment());
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			return null;
		}
	}

	// 取消服务
	private String applyKdbzCancel(ApplyKdbz cancelKdbz, SendTask sendTask) {
		try {
			logger.debug("取消服务");
			if (StringUtils.isEmpty(cancelKdbz.getEcCompanyId())
					|| StringUtils.isEmpty(cancelKdbz.getLogisticProvider())
					|| StringUtils.isEmpty(cancelKdbz.getRequestNo())
					|| StringUtils.isEmpty(cancelKdbz.getCustomerId())
					|| StringUtils.isEmpty(cancelKdbz.getServiceType())
					|| StringUtils.isEmpty(cancelKdbz.getStatus())) {

				cancelKdbz.getResponse().setLogisticProviderId(
						cancelKdbz.getLogisticProvider());
				cancelKdbz.getResponse()
						.setRequestNo(cancelKdbz.getRequestNo());
				cancelKdbz.getResponse().setSuccess("false");
				String toXmlFragment = cancelKdbz.getResponse()
						.toXmlFragment1();
				logger.error("serviceApply params is empty");
				return toXmlFragment;

			}

			boolean flag = false;
			try {

				flag = this.applyKdbzService
						.editApplyKdbz(cancelKdbz, sendTask);
			} catch (Exception e) {
				logger.error("serviceApply update failed", e);
			}
			cancelKdbz.getResponse().setLogisticProviderId(
					cancelKdbz.getLogisticProvider());
			cancelKdbz.getResponse().setRequestNo(cancelKdbz.getRequestNo());

			if (flag) {
				logger.debug("applyKdbzCancel" + "取消成功");
				cancelKdbz.getResponse().setSuccess("true");

			} else {
				logger.debug("applyKdbzCancel" + "取消失败");
				cancelKdbz.getResponse().setSuccess("false");

			}
			String toXmlFragment = cancelKdbz.getResponse().toXmlFragment1();
			return toXmlFragment;

		} catch (Exception re) {
			logger.error("", re);
			try {
				print(Response.DATA_INSECURITY_RESPONSE_9.toXmlFragment());
			} catch (IOException e) {

				e.printStackTrace();
			}
			return null;
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
//		String string0 = logisticsInterface
//				+ ConfigUtilSingle.getInstance().getPARTERID_TAOBAO();
		String string0 = logisticsInterface
		+ "4VcFF42y";
		String string1 = Md5Encryption.MD5Encode(string0);
		logger.debug(string1);
		logger.debug(dataDigest);
		if (dataDigest.equals(string1)) {
			return true;
		}
		return false;

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
	 * 使用指定的字符集charset对字符串arg进行编码,将编码后的字符串返回.
	 * 
	 * @param arg
	 *            待编码字符串.
	 * @param charset
	 * @return
	 */
	private static String encode(String arg, String charset) {
		try {
			return java.net.URLEncoder.encode(arg, charset);
		} catch (UnsupportedEncodingException e) {
			logger.error(LogInfoEnum.PARSE_INVALID.getValue(), e);
			throw new RuntimeException(e);
		}
	}

	private static int getflagid(String arg) {
		String s = arg;
		// String s="klllll123456";
		s = s.substring(s.length() - 1);
		logger.debug(s);
		return Integer.parseInt(s);

	}

	private static String getflag(String arg) {
		String sid = arg;
		// String s="klllll123456";
		sid = sid.substring(sid.length() - 1);
		String s = sid;
		// 按照对应关系取配置文件
		/*
		 * if(sid){ s= }
		 */
		logger.debug(s);
		return s;

	}

	/**
	 * 解析接口调用,金刚调用淘宝接口.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String kingGang2TaoBao() {
		logger.debug("into kingGang2TaoBao");
		try {
			if (XmlSender.GET_REQUEST_METHOD.equals(request.getMethod())) {
				super.print(GET_METHOD_RESPONSE);
				return null;
			}

			logger.debug("kinggang2taobao" + super.request.getRemoteAddr()
					+ "," + super.request.getRemoteHost() + "---------------"
					+ System.getProperty("file.encoding") + "---------------"
					+ super.request.getCharacterEncoding() + "---------------");

			// // 1.获取参数值.
			String logisticsInterface = super.request
					.getParameter(LOGISTICS_INTERFACE_PARAM);
			String dataDigest = super.request.getParameter(DATA_DIGEST_PARAM);
			String clientId = "TAOBAOSERVICE";
			String type = super.request.getParameter(MSG_TYPE);
			if (StringUtils.isEmpty(type)) {
				type = "offline";
			}
			String url = Resource.getChannel(clientId);

			if (logisticsInterface == null || dataDigest == null) {
				print(Response.DATA_INSECURITY_RESPONSE_1.toXmlFragment());
				logger.error("kinggang2taobao:logisticsInterface="
						+ logisticsInterface + ",dataDigest=" + dataDigest);
				return null;
			}

			// liug编码
			String dslogisticsInterface = null;
			String dsdataDigest = null;
			if (request.getParameter("test") != null) {
				dslogisticsInterface = logisticsInterface;
				dsdataDigest = dataDigest;
			} else {
				dslogisticsInterface = encode(logisticsInterface,
						XmlSender.UTF8_CHARSET);
				dsdataDigest = encode(dataDigest, XmlSender.UTF8_CHARSET);
			}

			// 2.转码
			if (request.getParameter("test") != null) {
				logisticsInterface = decode(logisticsInterface,
						XmlSender.UTF8_CHARSET);
				dataDigest = decode(dataDigest, XmlSender.UTF8_CHARSET);
			}

			// 3.验证
			logger.debug("jingang_logisticsInterface===" + logisticsInterface);
			logger.debug("jinggang_dataDigest===" + dataDigest);
			if (!validateData(logisticsInterface, dataDigest)) {
				Response response = new Response();
				response.setSuccess("false");
				print(response.toXmlFragment1());
				return null;

			}

			logger.debug("验证通过!");
			String xmlFragment = logisticsInterface;
			if (xmlFragment.contains("<ServiceApply>")
					&& xmlFragment.contains("</ServiceApply>")) {
				ApplyKdbz applyKdbz = new ApplyKdbz().toObject(xmlFragment);
				if (StringUtils.isEmpty(applyKdbz.getLogisticProvider())
						|| StringUtils.isEmpty(applyKdbz.getEcCompanyId())
						|| StringUtils.isEmpty(applyKdbz.getRequestNo())
						|| StringUtils.isEmpty(applyKdbz.getServiceType())
						|| (StringUtils.isEmpty(applyKdbz.getVip()) && "APPROVED"
								.equals(applyKdbz.getStatus().toUpperCase()))) {
					print(Response.DATA_INSECURITY_RESPONSE_1.toXmlFragment1());
					logger.error("serviceApply params from jg is empty");
					return null;
				}
				applyKdbz.getResponse().setLogisticProviderId(
						applyKdbz.getLogisticProvider());
				applyKdbz.getResponse().setRequestNo(applyKdbz.getRequestNo());
				applyKdbz.getResponse().setSuccess("true");
				SendTaskToTB sendTaskToTB = new SendTaskToTB();

				sendTaskToTB.setClientId(clientId);
				sendTaskToTB.setRequestURL(url);
				sendTaskToTB.setOrderId(0);

				sendTaskToTB.setRemark(applyKdbz.getResponse().toXmlFragment());
				sendTaskToTB.setTaskFlagId(0);
				sendTaskToTB.setTaskFlag(String.valueOf(0));
				sendTaskToTB.setTxLogisticId(applyKdbz.getRequestNo());

				sendTaskToTB.setRequestParams(LOGISTICS_INTERFACE_PARAM + "="
						+ dslogisticsInterface + "&" + DATA_DIGEST_PARAM + "="
						+ dsdataDigest + "&msg_type =" + type);
				try {
					boolean flag = this.applyKdbzService.applyStatusNodify(
							applyKdbz, sendTaskToTB);
					if (flag)
						super.print(applyKdbz.getResponse().toXmlFragment1());
					else
						super.print("<serviceResponse><logisticproviderid></logisticproviderid><requestNo></requestNo><success>false</success></serviceResponse>");
				} catch (Exception e) {
					super.print("<serviceResponse><logisticproviderid></logisticproviderid>requestNo></requestNo><success>false</success></serviceResponse>");
				}
				logger.debug("响应发送完毕!");

			}

		} catch (Exception re) {
			logger.error("", re);
			try {
				print(Response.DATA_INSECURITY_RESPONSE_9.toXmlFragment());
			} catch (IOException e) {

				e.printStackTrace();
			}
			return null;

		}

		return null;

	}

}
