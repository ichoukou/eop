package net.ytoec.kernel.dataobject;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 订单信息类
 * 
 * @author 2013-03-06 zhangcong
 * @version 1.0
 */

public class OrderInfo {

	private int id;//
	private String txLogisticId;// 物流订单号
	private String childLogisticId;
	private String mailNo;// 物流运单号
	private String customerId;// 客户身份标识
	private String clientId;
	private String traderNo;// 业务交易号
	private int orderType;// 订单类型
	private int orderService;// 服务类型
	private String deliverNo;// 发货单号
	private String orderStatus;// 订单状态
	private String saleName;// 卖家姓名
	private String saleMobile;// 卖家电话
	private String saleTelPhone;// 卖家手机
	private String saleProv;// 卖家省份
	private String saleCity;// 卖家所属市
	private String saleDistrict;// 卖家所属区
	private String saleAddress;// 卖家详细地址，不包括省市区
	private String salePostcode;// 卖家邮编
	private String buyName;// 买家名称
	private String buyMobile;// 买家电话
	private String buyTelphone;// 买家手机
	private String buyProv;// 买家省份
	private String buyCity;// 买家所属市
	private String buyDistrict;// 买家所属区
	private String buyAddress;//买家详细地址
	private String buyPostcode;// 买家邮编
	private Timestamp sendStartTime;
	private Timestamp sendEndTime;
	private BigDecimal insuranceValue;
	private String packageOrNot;
	private String special;// 特殊包裹
	private BigDecimal weight;
	private String vipId;
	private String lineType;
	private String weightUpdateFlag;
	private BigDecimal goodsValue;
	private BigDecimal itemsValue;
	private BigDecimal totalServiceFee;
	private BigDecimal buyServiceFee;// 买家服务费
	private Timestamp acceptTime;// 状态更新时间
	private BigDecimal codSplitFee;// cod分润
	private char freightType;// 是否为调整运费订单
	private BigDecimal freight;
	private BigDecimal trimFreight;
	private String isPrint;// 订单是否打印快递单标识，Y为已打印，N为未打印 ，默认值为N
	private String agencyFund;// 代收款

	private String bigPen;// 大头笔
	private Date date;// 发货单日期
	private String itemName;// 商品名称
	private String itemNumber;// 商品数量
	private String remark;// 备注
	private Timestamp createTime;// 订单创建时间
	private Timestamp updateTime;// 订单修改时间
	private String batchNo;// 批次号

	public OrderInfo() {
		super();
	}

	public OrderInfo(int id, String txLogisticId, String childLogisticId,
			String mailNo, String customerId, String clientId, String traderNo,
			int orderType, int orderService, String deliverNo,
			String orderStatus, String saleName, String saleMobile,
			String saleTelPhone, String saleProv, String saleCity,
			String saleDistrict, String saleAddress, String salePostcode,
			String buyName, String buyMobile, String buyTelphone,
			String buyProv, String buyCity, String buyDistrict,
			String buyAddress, String buyPostcode, Timestamp sendStartTime,
			Timestamp sendEndTime, BigDecimal insuranceValue,
			String packageOrNot, String special, BigDecimal weight,
			String vipId, String lineType, String weightUpdateFlag,
			BigDecimal goodsValue, BigDecimal itemsValue,
			BigDecimal totalServiceFee, BigDecimal buyServiceFee,
			Timestamp acceptTime, BigDecimal codSplitFee, char freightType,
			BigDecimal freight, BigDecimal trimFreight, String isPrint,
			String agencyFund, String bigPen, Date date, String itemName,
			String itemNumber, String remark, Timestamp createTime,
			Timestamp updateTime, String batchNo) {
		super();
		this.id = id;
		this.txLogisticId = txLogisticId;
		this.childLogisticId = childLogisticId;
		this.mailNo = mailNo;
		this.customerId = customerId;
		this.clientId = clientId;
		this.traderNo = traderNo;
		this.orderType = orderType;
		this.orderService = orderService;
		this.deliverNo = deliverNo;
		this.orderStatus = orderStatus;
		this.saleName = saleName;
		this.saleMobile = saleMobile;
		this.saleTelPhone = saleTelPhone;
		this.saleProv = saleProv;
		this.saleCity = saleCity;
		this.saleDistrict = saleDistrict;
		this.saleAddress = saleAddress;
		this.salePostcode = salePostcode;
		this.buyName = buyName;
		this.buyMobile = buyMobile;
		this.buyTelphone = buyTelphone;
		this.buyProv = buyProv;
		this.buyCity = buyCity;
		this.buyDistrict = buyDistrict;
		this.buyAddress = buyAddress;
		this.buyPostcode = buyPostcode;
		this.sendStartTime = sendStartTime;
		this.sendEndTime = sendEndTime;
		this.insuranceValue = insuranceValue;
		this.packageOrNot = packageOrNot;
		this.special = special;
		this.weight = weight;
		this.vipId = vipId;
		this.lineType = lineType;
		this.weightUpdateFlag = weightUpdateFlag;
		this.goodsValue = goodsValue;
		this.itemsValue = itemsValue;
		this.totalServiceFee = totalServiceFee;
		this.buyServiceFee = buyServiceFee;
		this.acceptTime = acceptTime;
		this.codSplitFee = codSplitFee;
		this.freightType = freightType;
		this.freight = freight;
		this.trimFreight = trimFreight;
		this.isPrint = isPrint;
		this.agencyFund = agencyFund;
		this.bigPen = bigPen;
		this.date = date;
		this.itemName = itemName;
		this.itemNumber = itemNumber;
		this.remark = remark;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.childLogisticId = childLogisticId;
		this.batchNo = batchNo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTxLogisticId() {
		return txLogisticId;
	}

	public void setTxLogisticId(String txLogisticId) {
		this.txLogisticId = txLogisticId;
	}

	public String getChildLogisticId() {
		return childLogisticId;
	}

	public void setChildLogisticId(String childLogisticId) {
		this.childLogisticId = childLogisticId;
	}

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getTraderNo() {
		return traderNo;
	}

	public void setTraderNo(String traderNo) {
		this.traderNo = traderNo;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public int getOrderService() {
		return orderService;
	}

	public void setOrderService(int orderService) {
		this.orderService = orderService;
	}

	public String getDeliverNo() {
		return deliverNo;
	}

	public void setDeliverNo(String deliverNo) {
		this.deliverNo = deliverNo;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getSaleName() {
		return saleName;
	}

	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}

	public String getSaleMobile() {
		return saleMobile;
	}

	public void setSaleMobile(String saleMobile) {
		this.saleMobile = saleMobile;
	}

	public String getSaleTelPhone() {
		return saleTelPhone;
	}

	public void setSaleTelPhone(String saleTelPhone) {
		this.saleTelPhone = saleTelPhone;
	}

	public String getSaleProv() {
		return saleProv;
	}

	public void setSaleProv(String saleProv) {
		this.saleProv = saleProv;
	}

	public String getSaleCity() {
		return saleCity;
	}

	public void setSaleCity(String saleCity) {
		this.saleCity = saleCity;
	}

	public String getSaleDistrict() {
		return saleDistrict;
	}

	public void setSaleDistrict(String saleDistrict) {
		this.saleDistrict = saleDistrict;
	}

	public String getSaleAddress() {
		return saleAddress;
	}

	public void setSaleAddress(String saleAddress) {
		this.saleAddress = saleAddress;
	}

	public String getSalePostcode() {
		return salePostcode;
	}

	public void setSalePostcode(String salePostcode) {
		this.salePostcode = salePostcode;
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

	public String getBuyTelphone() {
		return buyTelphone;
	}

	public void setBuyTelphone(String buyTelphone) {
		this.buyTelphone = buyTelphone;
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

	public String getBuyPostcode() {
		return buyPostcode;
	}

	public void setBuyPostcode(String buyPostcode) {
		this.buyPostcode = buyPostcode;
	}

	public Timestamp getSendStartTime() {
		return sendStartTime;
	}

	public void setSendStartTime(Timestamp sendStartTime) {
		this.sendStartTime = sendStartTime;
	}

	public Timestamp getSendEndTime() {
		return sendEndTime;
	}

	public void setSendEndTime(Timestamp sendEndTime) {
		this.sendEndTime = sendEndTime;
	}

	public BigDecimal getInsuranceValue() {
		return insuranceValue;
	}

	public void setInsuranceValue(BigDecimal insuranceValue) {
		this.insuranceValue = insuranceValue;
	}

	public String getPackageOrNot() {
		return packageOrNot;
	}

	public void setPackageOrNot(String packageOrNot) {
		this.packageOrNot = packageOrNot;
	}

	public String getSpecial() {
		return special;
	}

	public void setSpecial(String special) {
		this.special = special;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public String getVipId() {
		return vipId;
	}

	public void setVipId(String vipId) {
		this.vipId = vipId;
	}

	public String getLineType() {
		return lineType;
	}

	public void setLineType(String lineType) {
		this.lineType = lineType;
	}

	public String getWeightUpdateFlag() {
		return weightUpdateFlag;
	}

	public void setWeightUpdateFlag(String weightUpdateFlag) {
		this.weightUpdateFlag = weightUpdateFlag;
	}

	public BigDecimal getGoodsValue() {
		return goodsValue;
	}

	public void setGoodsValue(BigDecimal goodsValue) {
		this.goodsValue = goodsValue;
	}

	public BigDecimal getItemsValue() {
		return itemsValue;
	}

	public void setItemsValue(BigDecimal itemsValue) {
		this.itemsValue = itemsValue;
	}

	public BigDecimal getTotalServiceFee() {
		return totalServiceFee;
	}

	public void setTotalServiceFee(BigDecimal totalServiceFee) {
		this.totalServiceFee = totalServiceFee;
	}

	public BigDecimal getBuyServiceFee() {
		return buyServiceFee;
	}

	public void setBuyServiceFee(BigDecimal buyServiceFee) {
		this.buyServiceFee = buyServiceFee;
	}

	public Timestamp getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(Timestamp acceptTime) {
		this.acceptTime = acceptTime;
	}

	public BigDecimal getCodSplitFee() {
		return codSplitFee;
	}

	public void setCodSplitFee(BigDecimal codSplitFee) {
		this.codSplitFee = codSplitFee;
	}

	public char getFreightType() {
		return freightType;
	}

	public void setFreightType(char freightType) {
		this.freightType = freightType;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public BigDecimal getTrimFreight() {
		return trimFreight;
	}

	public void setTrimFreight(BigDecimal trimFreight) {
		this.trimFreight = trimFreight;
	}

	public String getIsPrint() {
		return isPrint;
	}

	public void setIsPrint(String isPrint) {
		this.isPrint = isPrint;
	}

	public String getAgencyFund() {
		return agencyFund;
	}

	public void setAgencyFund(String agencyFund) {
		this.agencyFund = agencyFund;
	}

	public String getBigPen() {
		return bigPen;
	}

	public void setBigPen(String bigPen) {
		this.bigPen = bigPen;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

}
