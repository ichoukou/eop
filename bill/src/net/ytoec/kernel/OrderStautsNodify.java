package net.ytoec.kernel;

import java.sql.Timestamp;

/**
 * 订单物流状态bean
 * 
 * @author huangtianfu
 * 
 */
public class OrderStautsNodify {

	private String logisticProviderID; // 物流公司ID, (64位，不为空)
	private String clientID;// vip客户标识(客户编号), (64位，不为空)
	private String mailNo;// 运单号(64位，可以为空)
	private String txLogisticID;// 物流订单号, (64位，不为空)
	private String infoType;// 通知类型STATUS：物流状态, (64位，不为空)
	private String infoContent;// 通知内容在infoType为STATUS, (64位，不为空)
	private String Remark;// 备注或失败原因, (256位，可以为空)
	private String name;// 该状态操作人员，签收、派送、揽件, (32位，可以为空)
	private String acceptTime;// 事件发生时间, （不为空）
	private String currentCity;// 现在所在城市, (32位，可以为空)
	private String nextCity;// 目标城市, (32位, 可以为空)
	private String facility;// 网点(128位，可以为空)
	private String contactInfo;// 联系方式（包括手机，电话等）, (256位， 可以为空)
	private double weight;// 称重信息, (可以为空)
	private String trackingInfo;// 流转信息, (256位，可以为空)

	public String getLogisticProviderID() {
		return logisticProviderID;
	}

	public void setLogisticProviderID(String logisticProviderID) {
		this.logisticProviderID = logisticProviderID;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public String getTxLogisticID() {
		return txLogisticID;
	}

	public void setTxLogisticID(String txLogisticID) {
		this.txLogisticID = txLogisticID;
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
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
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

	public String getCurrentCity() {
		return currentCity;
	}

	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}

	public String getNextCity() {
		return nextCity;
	}

	public void setNextCity(String nextCity) {
		this.nextCity = nextCity;
	}

	public String getFacility() {
		return facility;
	}

	public void setFacility(String facility) {
		this.facility = facility;
	}

	public String getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getTrackingInfo() {
		return trackingInfo;
	}

	public void setTrackingInfo(String trackingInfo) {
		this.trackingInfo = trackingInfo;
	}

}
