package com.api;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 订单上传响应类
 * 
 * @author huangtianfu
 * 
 */
@XmlType(propOrder = { "logisticProviderID", "txLogisticID", "success",
		"reason" })
@XmlRootElement(name = "Response")
public class OrderUploadResponse {

	/**
	 * 请求是否成功
	 */
	private boolean success;

	/**
	 * 物流公司ID(固定值=YTO)
	 */
	private String logisticProviderID;

	/**
	 * 物流号
	 */
	private String txLogisticID;

	/**
	 * 报错码
	 */
	private String reason;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getLogisticProviderID() {
		return logisticProviderID;
	}

	public void setLogisticProviderID(String logisticProviderID) {
		this.logisticProviderID = logisticProviderID;
	}

	public String getTxLogisticID() {
		return txLogisticID;
	}

	public void setTxLogisticID(String txLogisticID) {
		this.txLogisticID = txLogisticID;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
