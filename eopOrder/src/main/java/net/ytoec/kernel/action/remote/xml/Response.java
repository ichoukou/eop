package net.ytoec.kernel.action.remote.xml;

import java.util.List;

import net.ytoec.kernel.action.remote.process.ResponseProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 响应实体类.
 * 
 */
public class Response implements ObjectToXmlFragment {
	/**
	 * 数据安全校验失败，数据不安全.
	 */
	public static final Response DATA_INSECURITY_RESPONSE = new Response(null,
			"YTO", "false", "S02");// 非法的数字签名
	public static final Response DATA_INSECURITY_RESPONSE_1 = new Response(
			null, "YTO", "false", "S01");// 非法的XML格式
	public static final Response DATA_INSECURITY_RESPONSE_2 = new Response(
			null, "YTO", "false", "数据安全校验失败，数据不安全 ，xml");

	public static final Response DATA_INSECURITY_RESPONSE_3 = new Response(
			null, "YTO", "false", "S03");// 非法的物流公司
	public static final Response DATA_INSECURITY_RESPONSE_4 = new Response(
			null, "YTO", "false", "S04");// 非法的通知类型
	public static final Response DATA_INSECURITY_RESPONSE_5 = new Response(
			null, "YTO", "false", "S05");// 非法的通知内容
	public static final Response DATA_INSECURITY_RESPONSE_7 = new Response(
			null, "YTO", "false", "S07");// 系统异常，请重试
	public static final Response DATA_INSECURITY_RESPONSE_8 = new Response(
			null, "YTO", "false", "S08");// 非法的电商平台
	public static final Response DATA_INSECURITY_RESPONSE_9 = new Response(
			null, "YTO", "false", "system error");// 系统错误
	public static final Response DATA_INSECURITY_RESPONSE_10 = new Response(
			null, "YTO", "false", "非法的电子面单，电子面单不以8开头！");// 电子面单格式错误
	public static final Response DATA_HEAD_RESPONSE = new Response("YTO"); //taobao接口1.4.1版本将响应对象格式化为xml片段(头片段).
	
	public static final String SUCCESS_TRUE = "true";
	public static final String SUCCESS_FALSE = "false";
	private String txLogisticId;
	private String logisticProviderId;
	private String success;
	private String reason;
	private String customerId;
	private String requestNo;
	private String fieldName;
	private String ecCompanyId;
	private String fieldValue;
	private String remark;
	private static Logger logger = LoggerFactory.getLogger(Response.class);
	//private 

	private ResponseProcessor responseProcessor = new ResponseProcessor();

	public Response() {

	}

	public Response(String logisticProviderId) {
		this.logisticProviderId = logisticProviderId;
	}

	private Response(String txLogisticId, String logisticProviderId,
			String success, String reason) {
		this.txLogisticId = txLogisticId;
		this.logisticProviderId = logisticProviderId;
		this.success = success;
		this.reason = reason;
	}
	
	public Response toObject(String xmlFragment) {
		Response response = null;
		try {
			response = this.responseProcessor.parse(xmlFragment);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return response;
	}

	public Response toObject1(String xmlFragment) {
		Response response = null;
		try {
			response = this.responseProcessor.parseApplyKdbz(xmlFragment);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return response;
	}
	
	public List<Response> toObject2(String xmlFragment) {
		List<Response> response = null;
		try {
			response = this.responseProcessor.parseTaoBao(xmlFragment);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return response;
	}
	
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public String getTxLogisticId() {
		return txLogisticId;
	}

	public void setTxLogisticId(String txLogisticId) {
		this.txLogisticId = txLogisticId;
	}

	public String getLogisticProviderId() {
		return logisticProviderId;
	}

	public void setLogisticProviderId(String logisticProviderId) {
		this.logisticProviderId = logisticProviderId;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * 将响应对象格式化为xml片段.
	 * 
	 * @return
	 */
	public String toXmlFragment() {
		StringBuilder sb = new StringBuilder();
		sb.append("<Response>");
		
		if (!isBlank(this.getLogisticProviderId())) {
			sb.append("<logisticProviderID>");
			sb.append(this.getLogisticProviderId());
			sb.append("</logisticProviderID>");
		}
		if (!isBlank(this.getTxLogisticId())) {
			sb.append("<txLogisticID>");
			sb.append(this.getTxLogisticId());
			sb.append("</txLogisticID>");
		}
		sb.append("<success>");
		sb.append(this.getSuccess());
		sb.append("</success>");
		if (SUCCESS_FALSE.equals(this.success)) {
			sb.append("<reason>");
			sb.append(this.getReason());
			sb.append("</reason>");
		}
		sb.append("</Response>");
		return sb.toString();
	}

	public String toXmlFragment1() {
		StringBuilder sb = new StringBuilder();
		sb.append("<ServiceResponse>");
		if (!isBlank(this.getLogisticProviderId())) {
			sb.append("<logisticProviderID>");
			sb.append(this.getLogisticProviderId());
			sb.append("</logisticProviderID>");
		}
		if (!isBlank(this.getRequestNo())) {
			sb.append("<requestNo>");
			sb.append(this.getRequestNo());
			sb.append("</requestNo>");
		}
		sb.append("<success>");
		sb.append(this.getSuccess());
		sb.append("</success>");
		if (SUCCESS_FALSE.equals(this.success)) {
			sb.append("<reason>");
			sb.append(this.getReason());
			sb.append("</reason>");
		}
		sb.append("</ServiceResponse>");
		return sb.toString();
	}
	
	/**
	 * taobao接口1.4.1版本 将响应对象格式化为xml片段(订单创建).
	 * 
	 * @return
	 * @author liuchunyan
	 */
	public String toXmlFragment2() {
		StringBuilder sb = new StringBuilder();
		sb.append("<responses>");
		if (!isBlank(this.getLogisticProviderId())) {
			sb.append("<logisticProviderID>");
			sb.append(this.getLogisticProviderId());
			sb.append("</logisticProviderID>");
		}
		
		sb.append("<responseItems>");
		sb.append("<response>");
		if (!isBlank(this.getTxLogisticId())) {
			sb.append("<txLogisticID>");
			sb.append(this.getTxLogisticId());
			sb.append("</txLogisticID>");
		}
		sb.append("<success>");
		sb.append(this.getSuccess());
		sb.append("</success>");
		if (SUCCESS_FALSE.equals(this.success)) {
			sb.append("<reason>");
			sb.append(this.getReason());
			sb.append("</reason>");
		}
		sb.append("</response>");
		sb.append("</responseItems>");
		sb.append("</responses>");
		return sb.toString();
	}

	/**
	 * taobao接口1.4.1版本将响应对象格式化为xml片段(订单更新/订单消除).
	 * 
	 * @return
	 * @author liuchunyan
	 */
	public String toXmlFragment3() {
		StringBuilder sb = new StringBuilder();
		sb.append("<response>");
		if (!isBlank(this.getTxLogisticId())) {
			sb.append("<txLogisticID>");
			sb.append(this.getTxLogisticId());
			sb.append("</txLogisticID>");
		}
		if (!isBlank(this.getFieldName())) {
			sb.append("<fieldName>");
			sb.append(this.getFieldName());
			sb.append("</fieldName>");
		}
		sb.append("<success>");
		sb.append(this.getSuccess());
		sb.append("</success>");
		if (SUCCESS_FALSE.equals(this.success)) {
			sb.append("<reason>");
			sb.append(this.getReason());
			sb.append("</reason>");
		}

		sb.append("</response>");
		return sb.toString();
	}
	
	/**
	 * taobao接口1.4.1版本将响应对象格式化为xml片段(头片段).
	 * 
	 * @return
	 * @author liuchunyan
	 */
	public String toXmlFragment5() {
		StringBuilder sb = new StringBuilder();
		sb.append("<responses>");
		
		sb.append("<logisticProviderID>");
		sb.append(this.getLogisticProviderId());
		sb.append("</logisticProviderID>");
		
		sb.append("<responseItems>");
		return sb.toString();
	}
	
	private boolean isBlank(String arg) {
		if (arg == null || "".equals(arg)) {
			return true;
		}
		return false;
	}
	
	/**
	 * taobao接口1.4.1版本传送给金刚的报文头片段格式
	 * 
	 * @return
	 * @author liuchunyan
	 */
	public String toXmlFragment4() {
		StringBuilder sb = new StringBuilder();
		sb.append("<UpdateInfo>");
		if (!isBlank(this.getLogisticProviderId())) {
			sb.append("<logisticProviderID>");
			sb.append(this.getLogisticProviderId());
			sb.append("</logisticProviderID>");
		}
		if (!isBlank(this.getEcCompanyId())) {
			sb.append("<ecCompanyId>");
			sb.append(this.getEcCompanyId());
			sb.append("</ecCompanyId>");
		}
		sb.append("<fieldList>");
		return sb.toString();
	}
	
	/**
	 * taobao接口1.4.1版本传送给金刚的报文内容格式
	 * 
	 * @return
	 * @author liuchunyan
	 */
	public String toXmlFragment6() {
		StringBuilder sb = new StringBuilder();
		sb.append("<field>");
		if (!isBlank(this.getTxLogisticId())) {
			sb.append("<txLogisticID>");
			sb.append(this.getTxLogisticId());
			sb.append("</txLogisticID>");
		}
		if (!isBlank(this.getFieldName())) {
			sb.append("<fieldName>");
			sb.append(this.getFieldName());
			sb.append("</fieldName>");
		}
		if (!isBlank(this.getFieldName())) {
			sb.append("<fieldValue>");
			sb.append(this.getFieldValue());
			sb.append("</fieldValue>");
		}
		if (!isBlank(this.getRemark())) {
			sb.append("<remark>");
			sb.append(this.getRemark());
			sb.append("</remark>");
		}
		sb.append("</field>");
		return sb.toString();
	}
	

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("txLogisticID=");
		sb.append(this.getTxLogisticId());
		sb.append(",logisticProviderId=");
		sb.append(this.getLogisticProviderId());
		sb.append(",success=");
		sb.append(this.getSuccess());
		sb.append(",reason=");
		sb.append(this.getReason());
		return sb.toString();
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getEcCompanyId() {
		return ecCompanyId;
	}

	public void setEcCompanyId(String ecCompanyId) {
		this.ecCompanyId = ecCompanyId;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	

}
