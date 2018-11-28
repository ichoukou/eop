package net.ytoec.kernel.dataobject;

import java.util.Date;

public class CoreTaskLog {

	private static final long serialVersionUID = -7970848646314840509L;

	private int Id;

	private String requestURL;

	private Date requestTime;

	private Date createTime;

	private int orderId;

	private String clientId;

	private String failMessage;

	private String requestParams;

	private Date partitiondate;

	public Date getPartitiondate() {
		return partitiondate;
	}

	public void setPartitiondate(Date partitiondate) {
		this.partitiondate = partitiondate;
	}

	public String getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getFailMessage() {
		return failMessage;
	}

	public void setFailMessage(String failMessage) {
		this.failMessage = failMessage;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public String getRequestURL() {
		return requestURL;
	}

	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
