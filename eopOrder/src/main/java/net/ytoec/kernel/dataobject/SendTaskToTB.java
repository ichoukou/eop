package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务发送实体.
 */
public class SendTaskToTB implements Serializable {

	private static final long serialVersionUID = 660923831590532123L;
	private int id;
	private String requestURL;
	// 请求参数. 因为需要使用HTTP POST方式发送数据,因此URL与参数值分开来保存.
	private String requestParams;
	private Date lastSendTime;
	private Date createTime;
	private int orderId;
	private String clientId;
	private String remark;
	private int taskFlagId;
	private String taskFlag;
	private String txLogisticId;


	public int getTaskFlagId() {
		return taskFlagId;
	}

	public void setTaskFlagId(int taskFlagId) {
		this.taskFlagId = taskFlagId;
	}


	public String getTaskFlag() {
		return taskFlag;
	}

	public void setTaskFlag(String taskFlag) {
		this.taskFlag = taskFlag;
	}

	public String getTxLogisticId() {
		return txLogisticId;
	}

	public void setTxLogisticId(String txLogisticId) {
		this.txLogisticId = txLogisticId;
	}


	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRequestURL() {
		return requestURL;
	}

	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}

	public Date getLastSendTime() {
		return lastSendTime;
	}

	public void setLastSendTime(Date lastSendTime) {
		this.lastSendTime = lastSendTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}

}
