package net.ytoec.kernel.dataobject;

import java.util.Date;

public class FailedTask {

	private Integer id;
	private String requestUrl;
	private String taskStatus;
	private String txLogisticId;
	private Date createTime;
	private String failedReason;
	private String requestParams;
	private String taskDest;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getTxLogisticId() {
		return txLogisticId;
	}
	public void setTxLogisticId(String txLogisticId) {
		this.txLogisticId = txLogisticId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getFailedReason() {
		return failedReason;
	}
	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}
	public String getRequestParams() {
		return requestParams;
	}
	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}
	public String getTaskDest() {
		return taskDest;
	}
	public void setTaskDest(String taskDest) {
		this.taskDest = taskDest;
	}
	
	
}
