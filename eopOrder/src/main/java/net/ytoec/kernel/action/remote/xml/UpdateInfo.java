package net.ytoec.kernel.action.remote.xml;

import java.util.Date;

/**
 * xml订单状态更新数据
 * @author wmd
 * 2013-03-20
 *
 */
public class UpdateInfo {

	/**
	 * 物流平台标识（不能为空）
	 */
	private String txLogisticID;

	/**
	 * 运单号，在没有确定的情况下可能为空，如接单、揽收失败
	 */
	private String mailNo;

	/**
	 * 物流公司编号
	 */
	private String logisticProviderID;
	
	/**
	 * 通知类型  INSTRUCTION：通知指令
	 */
	private String infoType;
	/**
	 * 通知内容 在infoType为INSTRUCTION时：UPDATE ：更新运单号 WITHDRAW：取消订单
	 */
	private String infoContent;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 操作人员
	 */
	private String name;
	/**
	 * 事件发生时间
	 */
	private String acceptTime;
	/**
	 *平台id 
	 */
	private String clientId;

	/**
	 * 揽收成功的时候，将时间写入这个字段中，超时件要使用。
	 */
    private String type;
    
    /**
     * 订单状态(更新问题件订单状态时用到)
     */
    private String orderStatus;
    
    /**
     * 订单状态(更新订单表用)
     */
    private String status;
    
    /**
     * 发送状态
     */
    private String success;
    /**
	 * 称重信息
	 */
	private Double weight;
	
	
	/**
	 * 渠道编码
	 */
	private String orderChannelCode;
	
	/**
	 * 签收人
	 */
	private String signoffName;
	
	/**
	 * 操作时间
	 */
	private Date operTime;

	public UpdateInfo() {
	}

	public String getTxLogisticID() {
		return txLogisticID;
	}

	public void setTxLogisticID(String txLogisticID) {
		this.txLogisticID = txLogisticID;
	}

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public String getLogisticProviderID() {
		return logisticProviderID;
	}

	public void setLogisticProviderID(String logisticProviderID) {
		this.logisticProviderID = logisticProviderID;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getInfoContent() {
		return infoContent;
	}

	public void setInfoContent(String infoContent) {
		this.infoContent = infoContent;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(String acceptTime) {
		this.acceptTime = acceptTime;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getOrderChannelCode() {
		return orderChannelCode;
	}

	public void setOrderChannelCode(String orderChannelCode) {
		this.orderChannelCode = orderChannelCode;
	}

	public String getSignoffName() {
		return signoffName;
	}

	public void setSignoffName(String signoffName) {
		this.signoffName = signoffName;
	}

	public Date getOperTime() {
		return operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}

}
