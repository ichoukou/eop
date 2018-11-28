package net.ytoec.kernel.dataobject;

import java.util.Date;

/**
 * @作者：罗典
 * @描述：日志记录实体类
 * @时间：2013-08-29
 * */
public class ApiLog {
	// 主键ID
	private int id;
	// 创建时间
	private Date createTime;
	// 最后修改时间
	private Date lastUpdateTime;
	// 是否出现了异常0:正常，1:异常
	private boolean isException = false;
	// 日志类型(1，电子面单下发接口日志: synWaybill)
	private String logType;
	// 接口耗费时间(单位:毫秒)
	private Long usedtime;
	// 请求参数信息
	private String requestMsg;
	// 接口响应消息
	private String responseMsg;
	// 出现异常时的部分异常堆栈消息
	private String exceptionMsg;
	// 请求者IP地址
	private String requestIP;
	// 请求的标志信息，用于标示某个请求(如: 创建订单接口，此处可填订单号)。
	private String businessId;
	// 其他备注
	private String description;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public boolean isException() {
		return isException;
	}

	public void setException(boolean isException) {
		this.isException = isException;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public Long getUsedtime() {
		return usedtime;
	}

	public void setUsedtime(Long usedtime) {
		this.usedtime = usedtime;
	}

	public String getRequestMsg() {
		return requestMsg;
	}

	public void setRequestMsg(String requestMsg) {
		this.requestMsg = requestMsg;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public String getExceptionMsg() {
		return exceptionMsg;
	}

	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}

	public String getRequestIP() {
		return requestIP;
	}

	public void setRequestIP(String requestIP) {
		this.requestIP = requestIP;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
