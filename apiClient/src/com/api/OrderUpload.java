package com.api;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 订单回传类
 * 
 * @author huangtianfu
 * 
 */
public class OrderUpload {

	protected String txLogisticId; // 物流订单号
	protected String mailNo; // 物流运单号
	protected String customerId; // 客户身份标识
	protected String clientId; // 电商标识
	protected String traderNo; // 业务交易号
	protected int orderType; // 订单类型
	protected int orderService; // 服务类型
	protected String deliverNo; // 发货单号
	protected String orderStatus; // 订单状态
	protected String saleName; // 卖家姓名
	protected String saleMobile; // 卖家电话
	protected String saleTelPhone; // 卖家手机
	protected String saleProv; // 卖家省份
	protected String saleCity; // 卖家所属市
	protected String saleDistrict; // 卖家所属区
	protected String saleAddress; // 卖家详细地址，不包括省市区
	protected String salePostcode; // 卖家邮编
	protected String buyName; // 买家名称
	protected String buyMobile; // 买家电话
	protected String buyTelphone; // 买家手机
	protected String buyProv; // 买家省份
	protected String buyCity; // 买家所属市
	protected String buyDistrict; // 买家所属区
	protected String buyAddress; // 买家详细地址
	protected String buyPostcode; // 买家邮编
	protected String commodityInfo; // 商品信息，格式为：商品1,数量,单价;商品2,数量,单价;
	protected Timestamp sendStartTime; // 物流取货开始时间
	protected Timestamp sendEndTime; // 物流取货结束时间
	protected BigDecimal insuranceValue;// 保值金额
	protected String packageOrNot; // 是否需要包装
	protected String special; // 特殊包裹
	protected BigDecimal weight; // 订单重量
	protected String vipId; // user表的Id
	protected String lineType; // 在线状态 0:online 1:offline
	protected String weightUpdateFlag; // 订单重量更新状态
	protected BigDecimal goodsValue; // 商品金额+运费
	protected BigDecimal itemsValue; // 商品金额
	protected BigDecimal totalServiceFee; // 总服务费
	protected BigDecimal buyServiceFee; // 买家服务费
	protected Timestamp acceptTime; // 状态更新时间
	protected BigDecimal codSplitFee; // cod分润
	protected String freightType; // 是否为调整运费订单
	protected BigDecimal freight;
	protected BigDecimal trimFreight;
	protected String isPrint; // 订单是否打印快递单标识，Y为已打印，N为未打印 ，默认值为N
	protected String agencyFund; // 代收款
	protected String bigPen; // 大头笔
	protected Date date; // 发货单日期
	protected String itemName; // 商品名称
	protected int itemNumber; // 商品数量
	protected String remark; // 备注
	protected Timestamp createTime; // 订单创建时间
	protected Timestamp updateTime; // 订单修改时间
	protected String batchNo; // 批次号
	protected String childLogisticId;
	protected String uploadResult; // 订单上传结果,Y为成功，N为失败,W为等待上传，默认为W
	protected String failedReason; // 订单上传失败原因

	public String getCommodityInfo() {
		return commodityInfo;
	}

	public void setCommodityInfo(String commodityInfo) {
		this.commodityInfo = commodityInfo;
	}

	public OrderUpload() {
		super();
	}

	public OrderUpload(String txLogisticId, String childLogisticId,
			String mailNo, String customerId, String clientId, String traderNo,
			int orderType, int orderService, String deliverNo,
			String orderStatus, String saleName, String saleMobile,
			String saleTelPhone, String saleProv, String saleCity,
			String saleDistrict, String saleAddress, String salePostcode,
			String buyName, String buyMobile, String buyTelphone,
			String buyProv, String buyCity, String buyDistrict,
			String buyAddress, String buyPostcode, String commodityInfo,
			Timestamp sendStartTime, Timestamp sendEndTime,
			BigDecimal insuranceValue, String packageOrNot, String special,
			BigDecimal weight, String vipId, String lineType,
			String weightUpdateFlag, BigDecimal goodsValue,
			BigDecimal itemsValue, BigDecimal totalServiceFee,
			BigDecimal buyServiceFee, Timestamp acceptTime,
			BigDecimal codSplitFee, String freightType, BigDecimal freight,
			BigDecimal trimFreight, String isPrint, String agencyFund,
			String bigPen, Date date, String itemName, int itemNumber,
			String remark, Timestamp createTime, Timestamp updateTime,
			String batchNo, String uploadResult, String failedReason) {

		super();
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
		this.commodityInfo = commodityInfo;
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
		this.uploadResult = uploadResult;
		this.failedReason = failedReason;
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

	public String getFreightType() {
		return freightType;
	}

	public void setFreightType(String freightType) {
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

	public int getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(int itemNumber) {
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

	public String getUploadResult() {
		return uploadResult;
	}

	public void setUploadResult(String uploadResult) {
		this.uploadResult = uploadResult;
	}

	public String getFailedReason() {
		return failedReason;
	}

	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}
}
