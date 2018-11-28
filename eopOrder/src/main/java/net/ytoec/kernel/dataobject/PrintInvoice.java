package net.ytoec.kernel.dataobject;

import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

/**
 * 发货单(韩都衣舍)
 * @author mabo
 *
 */
public class PrintInvoice {

	private Integer id;
	
	//运单号
	private String mailNo;
	
	//订单号
	private String txLogisticId;
	
	//发货单号
	private String deliverNo;
	
	//客户身份标识
	private String customerId;
	
	//用户名 
	private String userName;
	
	//备注
	private String remark;
	
	//重量
	private Float weight;
	
	//订单状态，预留字段
	private String status;
	
	//订单是否打印快递单标识
	private String isPrint;
	
	//买家姓名
	private String buyName;
	
	//买家移动电话（手机）
	private String buyMobile;
	
	//买家固定电话
	private String buyTelPhone;
	
	//买家省份
	private String buyProv;
	//买家所属市
	private String buyCity;
	
	//买家的所属地区
	private String buyDistrict;
	
	//买家的地址，不包括省市区
	private String buyAddress;
	
	//买家的邮政编码
	private String buyPostCode;
	
	//大头笔信息
	private String bigPen;
	
	//商品金额，包括优惠和运费和服务费
	private Double totalValue;

	//日期
	private Date date;
	
	//代收款
	private Double agencyFund;
	
	//内容品名
	private String itemName;
	
	//商品数量
	private Integer itemNumber;
	
	//用户ID
	private Integer userId;
	
	//系统或者模块的唯一标识
	private String appCode;
	
	

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(Integer itemNumber) {
		this.itemNumber = itemNumber;
	}

	public Double getAgencyFund() {
		return agencyFund;
	}

	public void setAgencyFund(Double agencyFund) {
		this.agencyFund = agencyFund;
	}

	public Double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(Double totalValue) {
		this.totalValue = totalValue;
	}

	@JSON(format="yyyy-MM-dd") 
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public String getTxLogisticId() {
		return txLogisticId;
	}

	public void setTxLogisticId(String txLogisticId) {
		this.txLogisticId = txLogisticId;
	}

	public String getDeliverNo() {
		return deliverNo;
	}

	public void setDeliverNo(String deliverNo) {
		this.deliverNo = deliverNo;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsPrint() {
		return isPrint;
	}

	public void setIsPrint(String isPrint) {
		this.isPrint = isPrint;
	}

	public String getBuyName() {
		return buyName;
	}

	public void setBuyName(String buyName) {
		this.buyName = buyName;
	}

	public String getBuyMobile() {
		return buyMobile;
	}

	public void setBuyMobile(String buyMobile) {
		this.buyMobile = buyMobile;
	}

	public String getBuyTelPhone() {
		return buyTelPhone;
	}

	public void setBuyTelPhone(String buyTelPhone) {
		this.buyTelPhone = buyTelPhone;
	}

	public String getBuyProv() {
		return buyProv;
	}

	public void setBuyProv(String buyProv) {
		this.buyProv = buyProv;
	}

	public String getBuyCity() {
		return buyCity;
	}

	public void setBuyCity(String buyCity) {
		this.buyCity = buyCity;
	}
	

	public String getBuyDistrict() {
		return buyDistrict;
	}

	public void setBuyDistrict(String buyDistrict) {
		this.buyDistrict = buyDistrict;
	}

	public String getBuyAddress() {
		return buyAddress;
	}

	public void setBuyAddress(String buyAddress) {
		this.buyAddress = buyAddress;
	}

	public String getBuyPostCode() {
		return buyPostCode;
	}

	public void setBuyPostCode(String buyPostCode) {
		this.buyPostCode = buyPostCode;
	}

	public String getBigPen() {
		return bigPen;
	}

	public void setBigPen(String bigPen) {
		this.bigPen = bigPen;
	}

	
	
	
}
