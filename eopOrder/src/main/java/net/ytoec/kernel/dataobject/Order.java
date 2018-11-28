package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.Date;

import javassist.expr.NewArray;

/**
 * 订单表
 * 
 * @author ChenRen
 * @date 2011-7-20
 */
public class Order implements Serializable {
	/**
	 * 订单下单类型:线上下单.
	 */
	public static final String ONLINE_TYPE = "0";
	/**
	 * 订单下单类型:线下下单.
	 */
	public static final String OFFLINE_TYPE = "1";
	
	/**
	 * 服务类型 :在线下单
	*/
	public static final String SERVICETYPE_1= "1";
	/**
	 * 订单状态:创建订单.
	 */
	public static final String CREATE_ORDER_TYPE = "0";
	/**
	 * 订单状态:更新订单.
	 */
	public static final String UPDATE_ORDER_TYPE = "1";
	/**
	 * 订单状态:取消订单.
	 */
	public static final String CANCEL_ORDER_TYPE = "2";
	/**
	 * 订单状态:接收订单.
	 */
	public static final String ACCEPT_ORDER_TYPE = "3";
//	/**
//	 * 订单状态:不接收订单.
//	 */
	public static final String UNACCEPT_ORDER_TYPE = "4";
	/**
	 * 订单状态:打印订单.
	 */
	public static final String PRINT_ORDER_TYPE = "4";
	/**
	 * 订单状态:揽收成功.
	 */
	public static final String GOT_ORDER_TYPE = "5";
	/**
	 * 订单状态:揽收成功.
	 */
	public static final String NOT_SEND_ORDER_TYPE = "6";
	/**
	 * 订单状态:送达失败.
	 */
	public static final String FAILED_ORDER_TYPE = "7";
	/**
	 * 订单状态:送达成功.
	 */
	public static final String SIGNED_ORDER_TYPE = "8";
	
	/**
	 * 订单状态:打印订单.
	 */
	public static final String PRINT_ORDER_CONTENT = "NOPRINT";

	private static final long serialVersionUID = -7562759429088087797L;

	// === fields ===
	private Integer id;
	/** 版本号主要用于升级 */
	private String version;
	/** 物流公司ID; 是物流公司在物流平台的表示 */
	private String logisticProviderId;
	/** 物流号; 由物流平台交易生成; 有一定的编码和校验规则 */
	private String txLogisticId;
	/** 业务的交易号 */
	private String tradeNo;
	private String customerId;
	/**
	 * 物流公司的运单号<br>
	 * 应该做唯一性限制
	 */
	private String mailNo;
	private String type;
	/** 订单的flag标识，便于以后分拣和标识（暂时不用） */
	private String flag;
	private Date createTime;
	private Date updateTime;
	private Date sendStartTime;
	private Date sendEndTime;
	/** 保价 */
	private float insuranceValue;

	// private BigDecimal insuranceValue; //OrderFormInfo 中此字段类型.

	// private boolean packageOrNot; //OrderFormInfo 中此字段类型.
	/** 是否打包(true/false) */
	private String packageOrNot;
	/** 特殊商品性质，如：普通（0）易碎（1）、液态（2）、化学品（3）、白色粉末状（4）、香烟（5）等，可以考虑用枚举变量表示 */
	private String special;
	private String remark;
	public String getNumProv() {
		return numProv;
	}
	public void setNumProv(String numProv) {
		this.numProv = numProv;
	}
	private String clientId;
	/**
	 * 订单创建（0）、更新、取消、接单(ACCEPT)、 不接单(UNACCEPT)、揽收成功(GOT)、揽收失败(NOT_SEND)、失败(FAILED)、
	 * 送达(SIGNED)、派件扫描(SENT_SCAN)
	 */
	private String status;

	// 订单下单类型:线上下单,线下下单, 其值取ONLINE_TYPE,OFFLINE_TYPE其中之一.
	private String lineType;

	/** 重量 */
	private Float weight;
	/** 单价 */
	private float signPrice;
	private String vipId;
	private Date acceptTime;

	/**
	 * 寄件地;对应{@link TraderInfo#getTradeType()} 的值为<code>0</code><br>
	 * <b>该字段不存在Order表中, 是SQL中外查出的字段, 用于将字段值返回到页面.</b>
	 * 
	 * @see src\main\java\net\ytoec\kernel\mapper\OrderMapper.xml#ecAccount
	 */
	private String fromAddr;
	/**
	 * 目的地;对应{@link TraderInfo#getTradeType()} 的值为<code>1</code><br>
	 * <b>该字段不存在Order表中, 是SQL中外查出的字段, 用于将字段值返回到页面.</b>
	 * 
	 * @see src\main\java\net\ytoec\kernel\mapper\OrderMapper.xml#ecAccount
	 */
	private String toAddr;

	/**
	 * (费率)首重<br>
	 * <b>该字段用于在页面显示值和计算实收运费({@link #netFreight})</b>
	 */
	private float firstWeightPirce;
	/**
	 * (费率)超重<br>
	 * <b>该字段用于在页面显示值和计算实收运费({@link #netFreight})</b>
	 */
	private float overWeightPirce;
	/**
	 * 实收运费 <br>
	 * <b>该字段用于在页面显示值</b><br>
	 * 计算公式：
	 * <code>{@link #weight} > 1 ? {@link #firstWeightPirce} + ({@link #weight}-1)*{@link #overWeightPirce} : {@link #weight}*{@link #firstWeightPirce}</code>
	 */
	private float netFreight;

	/**
	 * 买家姓名（此字段不存在于数据表中，用户页面显示）
	 */
	private String buyName;
	
	/**
	 * 买家手机（此字段不存在于数据表中，用户页面显示）
	 */
	private String buyMobile;
	/**
	 * 买家电话（此字段不存在于数据表中，用户页面显示）
	 */
	private String buyPhone;
	
	private Integer orderType;
	private Long serviceType;
	private Double goodsValue;
	private Double itemsValue;
	private Double totalServiceFee;
	private Double buyServiceFee;
	private Double codSplitFee;
	
	private Date partitionDate;
	
		/**
	 * 0:调账订单   1：未调账订单
	 */
	private String freightType="1";
	private Double freight=0.0;
	private Double trimFreight=0.0;
	
	/**
	 * 省份的Id<br>
	 * DB中不存在，sql中返回该字段
	 * @author ChenRen/2011-11-08
	 * 
	 * 2012-03-14/ChenRen
	 * @deprecated 字段不够用.定义不明确
	 */
	private String numProv;
	
    /** 收件地/目的地/买家 省份编码 */
    private String bprovCode;
    
    /** 寄件地/发件地/卖家 省份编码 */
    private String sprovCode;
    
    /** 寄件地/发件地/卖家 市名 */
    private String scity;// 电子对账. 当省份为广东的时候会用到这个值来判断珠三角地区
    
    /** 网点省份编码 */
    private String wprovCode;	// 当用户类型为4会用到	// @2012-03-26/ChenRen
    /** 网点省份名称 */
    private String wprovText;	// 当用户类型为4会用到	// @2012-03-26/ChenRen

	//店铺名称
	private String shopName;
	
	/**
	 * 增加首重重量、固定价格、重量单价、最低收费价格；这些属性不存在与数据库表
	 */
	/** 最低收费价格（除固定收费外其他3中模板方式都有） */
	private float floorPirce;
	/** 首重 （续重统计单位和续重价格收费模板使用）*/
	private float firstWeight;
	/** 固定价格 (固定价格收费模版方式)*/
	private float fixedPirce;
	/** 重量单价(简单重量收费模板方式使用) */
	private float weightPirce;	
	
	private String weightUpdateFlag;
	
	private TraderInfo receiver;
	
	private TraderInfo sender;
	
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Long getServiceType() {
		return serviceType;
	}

	public void setServiceType(Long serviceType) {
		this.serviceType = serviceType;
	}

	public Double getGoodsValue() {
		return goodsValue;
	}

	public void setGoodsValue(Double goodsValue) {
		this.goodsValue = goodsValue;
	}

	public Double getItemsValue() {
		return itemsValue;
	}

	public void setItemsValue(Double itemsValue) {
		this.itemsValue = itemsValue;
	}

	public Double getTotalServiceFee() {
		return totalServiceFee;
	}

	public void setTotalServiceFee(Double totalServiceFee) {
		this.totalServiceFee = totalServiceFee;
	}

	public Double getBuyServiceFee() {
		return buyServiceFee;
	}

	public void setBuyServiceFee(Double buyServiceFee) {
		this.buyServiceFee = buyServiceFee;
	}

	public Double getCodSplitFee() {
		return codSplitFee;
	}

	public void setCodSplitFee(Double codSplitFee) {
		this.codSplitFee = codSplitFee;
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

	// === constructor ===
	public Order() {
	}

	// === getter && setter ===
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getLogisticProviderId() {
		return logisticProviderId;
	}

	public void setLogisticProviderId(String logisticProviderId) {
		this.logisticProviderId = logisticProviderId;
	}

	public String getTxLogisticId() {
		return txLogisticId;
	}

	public void setTxLogisticId(String txLogisticId) {
		this.txLogisticId = txLogisticId;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getSendStartTime() {
		return sendStartTime;
	}

	public void setSendStartTime(Date sendStartTime) {
		this.sendStartTime = sendStartTime;
	}

	public Date getSendEndTime() {
		return sendEndTime;
	}

	public void setSendEndTime(Date sendEndTime) {
		this.sendEndTime = sendEndTime;
	}

	public float getInsuranceValue() {
		return insuranceValue;
	}

	public void setInsuranceValue(float insuranceValue) {
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLineType() {
		return lineType;
	}

	public void setLineType(String lineType) {
		this.lineType = lineType;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float float1) {
		this.weight = float1;
	}

	public float getSignPrice() {
		return signPrice;
	}

	public void setSignPrice(float signPrice) {
		this.signPrice = signPrice;
	}

	public String getVipId() {
		return vipId;
	}

	public void setVipId(String vipId) {
		this.vipId = vipId;
	}

	public String getFromAddr() {
		return fromAddr;
	}

	public void setFromAddr(String fromAddr) {
		this.fromAddr = fromAddr;
	}

	public String getToAddr() {
		return toAddr;
	}

	public void setToAddr(String toAddr) {
		this.toAddr = toAddr;
	}

	public float getFirstWeightPirce() {
		return firstWeightPirce;
	}

	public void setFirstWeightPirce(float firstWeightPirce) {
		this.firstWeightPirce = firstWeightPirce;
	}

	public float getOverWeightPirce() {
		return overWeightPirce;
	}

	public void setOverWeightPirce(float overWeightPirce) {
		this.overWeightPirce = overWeightPirce;
	}

	public Date getPartitionDate() {
		return partitionDate;
	}

	public void setPartitionDate(Date partitionDate) {
		this.partitionDate = partitionDate;
	}

	/**
	 * 实收运费 <br>
	 * <b>该字段用于在页面显示值</b><br>
	 * 计算公式：
	 * <code>{@link #weight} > 1 ? {@link #firstWeightPirce} + ({@link #weight}-1)*{@link #overWeightPirce} : {@link #weight}*{@link #firstWeightPirce}</code>
	 */
	public float getNetFreight() {
		return netFreight;
	}

	/**
	 * 实收运费 <br>
	 * <b>该字段用于在页面显示值</b><br>
	 * 计算公式：四种计费方式，参见EcAccountHelper.java
	 */
	public void setNetFreight(float netFreight) {
		this.netFreight = netFreight;
	}

	public Date getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(Date acceptTime) {
		this.acceptTime = acceptTime;
	}

	@Override
	public String toString() {
		return this.id + "#" + this.version + "#" + this.logisticProviderId
				+ "#" + this.txLogisticId + "#" + this.tradeNo + "#"
				+ this.customerId + "#" + this.mailNo + "#" + this.type + "#"
				+ this.flag + "#" + this.createTime + "#" + this.updateTime
				+ "#" + this.sendStartTime + "#" + this.sendEndTime + "#"
				+ this.insuranceValue + "#" + this.packageOrNot + "#"
				+ this.special + "#" + this.remark + "#" + this.clientId + "#"
				+ this.status + "#" + this.weight + "#" + this.vipId + "#"
				+ this.signPrice + "#" + this.lineType
				+ "#toAddr:"+this.toAddr+ "#fromAddr:"+this.fromAddr;
	}

	public String getBuyPhone() {
		return buyPhone;
	}

	public void setBuyPhone(String buyPhone) {
		this.buyPhone = buyPhone;
	}

	public String getFreightType() {
		return freightType;
	}

	public void setFreightType(String freightType) {
		this.freightType = freightType;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public Double getTrimFreight() {
		return trimFreight;
	}

	public void setTrimFreight(Double trimFreight) {
		this.trimFreight = trimFreight;
	}
	public String getBprovCode() {
		return bprovCode;
	}
	public void setBprovCode(String bprovCode) {
		this.bprovCode = bprovCode;
	}
	public String getSprovCode() {
		return sprovCode;
	}
	public void setSprovCode(String sprovCode) {
		this.sprovCode = sprovCode;
	}
	public String getScity() {
		return scity;
	}
	public void setScity(String scity) {
		this.scity = scity;
	}
	public String getWprovCode() {
		return wprovCode;
	}
	public void setWprovCode(String wprovCode) {
		this.wprovCode = wprovCode;
	}
	public String getWprovText() {
		return wprovText;
	}
	public void setWprovText(String wprovText) {
		this.wprovText = wprovText;
	}
	public float getFloorPirce() {
		return floorPirce;
	}
	public void setFloorPirce(float floorPirce) {
		this.floorPirce = floorPirce;
	}
	public float getFirstWeight() {
		return firstWeight;
	}
	public void setFirstWeight(float firstWeight) {
		this.firstWeight = firstWeight;
	}
	public float getFixedPirce() {
		return fixedPirce;
	}
	public void setFixedPirce(float fixedPirce) {
		this.fixedPirce = fixedPirce;
	}
	public float getWeightPirce() {
		return weightPirce;
	}
	public void setWeightPirce(float weightPirce) {
		this.weightPirce = weightPirce;
	}
	public String getWeightUpdateFlag() {
		return weightUpdateFlag;
	}
	public void setWeightUpdateFlag(String weightUpdateFlag) {
		this.weightUpdateFlag = weightUpdateFlag;
	}
	public TraderInfo getReceiver() {
		return receiver;
	}
	public void setReceiver(TraderInfo receiver) {
		this.receiver = receiver;
	}
	public TraderInfo getSender() {
		return sender;
	}
	public void setSender(TraderInfo sender) {
		this.sender = sender;
	}

}
