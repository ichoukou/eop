package net.ytoec.kernel.service.helper;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.action.remote.process.ProcessorUtils;
import net.ytoec.kernel.action.remote.xml.UpdateWaybillInfo;
import net.ytoec.kernel.common.Constants;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.OrderFormInfo;
import net.ytoec.kernel.dataobject.SendTask;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 电商平台 订单创建，取消，更新，状态反馈辅助类
 * 
 * @author Administrator
 */
public class OrderOperateHelper {

	// 淘宝电商平台
	private static final String DATA_TAOBAO = "TAOBAO";
	
	private static Logger logger = LoggerFactory
			.getLogger(OrderOperateHelper.class);

	public static SendTask createTask(String txLogiscticId, String clientId,
			String mailNo, String infoContent) {

		StringBuffer xml = new StringBuffer("<UpdateInfo>");

		if (StringUtils.equalsIgnoreCase(clientId, "TAOBAO")
				|| StringUtils.isEmpty(clientId)) {
			xml.append("<txLogisticID>" + txLogiscticId + "</txLogisticID>");
			if (StringUtils.equals(infoContent, "UPDATE")) {
				xml.append("<mailNo>" + mailNo + "</mailNo>");
			}

			xml.append("<logisticProviderID>YTO</logisticProviderID>");
			xml.append("<infoType>INSTRUCTION</infoType>");
			xml.append("<infoContent>" + infoContent + "</infoContent>");
			if (StringUtils.equals(infoContent, "UPDATE")) {
				xml.append("<remark>绑定</remark>");
			} else {
				xml.append("<remark>取消绑定</remark>");
			}
			xml.append("</UpdateInfo>");
		} else {
			xml.append("<txLogisticID>" + txLogiscticId + "</txLogisticID>");
			if (StringUtils.equals(infoContent, "UPDATE")) {
				xml.append("<mailNo>" + mailNo + "</mailNo>");
			}
			xml.append("<clientID>" + clientId + "</clientID>");
			xml.append("<logisticProviderID>YTO</logisticProviderID>");
			xml.append("<infoType>INSTRUCTION</infoType>");
			xml.append("<infoContent>" + infoContent + "</infoContent>");
			if (StringUtils.equals(infoContent, "UPDATE")) {
				xml.append("<remark>绑定</remark>");
			} else {
				xml.append("<remark>取消绑定</remark>");
			}
			xml.append("</UpdateInfo>");
		}

		// 获取密文
		String key = Resource.getSecretId(clientId);
		String dataDigest = Md5Encryption.MD5Encode(xml.toString() + key);

		// 对报文进行UTF8编码
		String logisticsInterface = ProcessorUtils.encode(xml.toString(),
				XmlSender.UTF8_CHARSET);
		dataDigest = ProcessorUtils.encode(dataDigest, XmlSender.UTF8_CHARSET);

		SendTask sendTask = new SendTask();
		sendTask.setOrderId(0);
		sendTask.setClientId(clientId);
		sendTask.setRemark("orderPlace");
		sendTask.setTaskFlagId(0);
		sendTask.setTaskFlag("0");
		sendTask.setTxLogisticId(txLogiscticId);
		if (StringUtils.equalsIgnoreCase(clientId, DATA_TAOBAO)
				|| StringUtils.isEmpty(clientId)) {
			sendTask.setRequestURL(Resource
					.getChannel(Constants.JIN_GANG_TAOBAO_ORDER));
		} else {
			sendTask.setRequestURL(Resource
					.getChannel(Constants.JIN_GANG_COMMON_ORDER));
		}
		sendTask.setRequestParams("logistics_interface=" + logisticsInterface
				+ "&data_digest=" + dataDigest);
		return sendTask;
	}

	/**
	 * 构建订单创建http params
	 * 
	 * @param msg
	 * @param orderFormInfo
	 * @return
	 */
	public static SendTask makeOrderCreateTask(String msg,
			OrderFormInfo orderFormInfo) {

		if (StringUtils.isBlank(msg)) {
			logger.debug("args is blank,msg:" + msg );
			return new SendTask();
		}

		// 获取密文
		String key = Resource.getSecretId(orderFormInfo.getClientId());
		String dataDigest = Md5Encryption.MD5Encode(msg + key);

		// 对报文进行UTF8编码
		String logisticsInterface = ProcessorUtils.encode(msg,
				XmlSender.UTF8_CHARSET);
		dataDigest = ProcessorUtils.encode(dataDigest, XmlSender.UTF8_CHARSET);

		SendTask sendTask = new SendTask();
		sendTask.setOrderId(0);
		sendTask.setClientId(orderFormInfo.getClientId());
		sendTask.setTaskFlagId(getflagid(orderFormInfo.getTxLogisticId()));
		sendTask.setTaskFlag(getflag(orderFormInfo.getTxLogisticId()));
		sendTask.setTxLogisticId(orderFormInfo.getTxLogisticId());
		if (StringUtils.equalsIgnoreCase(orderFormInfo.getClientId(), "TAOBAO")
				|| StringUtils.isEmpty(orderFormInfo.getClientId())) {
			sendTask.setRequestURL(Resource
					.getChannel(Constants.JIN_GANG_TAOBAO_ORDER));
		} else {
			sendTask.setRequestURL(Resource
					.getChannel(Constants.JIN_GANG_COMMON_ORDER));
		}
		sendTask.setRequestParams("logistics_interface=" + logisticsInterface
				+ "&data_digest=" + dataDigest+ "&msg_type=" +Constants.ORDER_CREATE);
		return sendTask;

	}

	/**
	 * 构造订单update http params（包括COD）
	 * 
	 * @param msg
	 * @param updateWaybillInfo
	 * @return
	 */
	public static SendTask makeOrderUpdateTask(String msg,
			UpdateWaybillInfo updateWaybillInfo) {

		if (StringUtils.isBlank(msg)) {
			logger.debug("args is blank,msg:" + msg);
			return new SendTask();
		}

		// 获取密文
		String key = Resource.getSecretId(DATA_TAOBAO);
		String dataDigest = Md5Encryption.MD5Encode(msg + key);

		// 对报文进行UTF8编码
		String logisticsInterface = ProcessorUtils.encode(msg,
				XmlSender.UTF8_CHARSET);
		dataDigest = ProcessorUtils.encode(dataDigest, XmlSender.UTF8_CHARSET);

		SendTask sendTask = new SendTask();
		sendTask.setOrderId(0);
		sendTask.setClientId(updateWaybillInfo.getClientId());
		sendTask.setTaskFlagId(getflagid(updateWaybillInfo.getTxLogisticId()));
		sendTask.setTaskFlag(getflag(updateWaybillInfo.getTxLogisticId()));
		sendTask.setTxLogisticId(updateWaybillInfo.getTxLogisticId());
		if (StringUtils.equalsIgnoreCase(updateWaybillInfo.getClientId(),
				"TAOBAO")
				|| StringUtils.isEmpty(updateWaybillInfo.getClientId())) {
			sendTask.setRequestURL(Resource
					.getChannel(Constants.JIN_GANG_TAOBAO_ORDER));
		} else {
			sendTask.setRequestURL(Resource
					.getChannel(Constants.JIN_GANG_COMMON_ORDER));
		}
		sendTask.setRequestParams("logistics_interface=" + logisticsInterface
				+ "&data_digest=" + dataDigest+"&msg_type=" +Constants.ORDER_UPDATE);
		return sendTask;

	}

	public static int getflagid(String arg) {

		if (StringUtils.isBlank(arg)) {
			logger.error("arg is blank:" + arg);
			return 0;
		}
		arg = arg.substring(arg.length() - 1);
		logger.debug(arg);
		int result=0;
		try {
			result = Integer.parseInt(arg);
		} catch (Exception e) {
			logger.error("arg is not end with digit:" + arg);
		}
		return result ;

	}

	public static String getflag(String arg) {
		if (StringUtils.isBlank(arg)) {
			logger.error("arg is blank:" + arg);
			return StringUtils.EMPTY;
		}
		// String s="klllll123456";
		arg = arg.substring(arg.length() - 1);
		// 按照对应关系取配置文件
		/*
		 * if(sid){ s= }
		 */
		logger.debug(arg);
		return arg;
	}
}
