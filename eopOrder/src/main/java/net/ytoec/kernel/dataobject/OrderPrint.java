package net.ytoec.kernel.dataobject;

import java.io.Serializable;

public class OrderPrint implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3766954508533578917L;
	/**
	 * 和EC_CORE_ORDER表主键保持一致
	 */
	private Integer id;
	/**
	 * 废弃，无需关心
	 */
	private String version;
	/**
	 * 物流公司名称 固定值：YTO
	 */
	private String logisticProviderId;

	/**
	 * 物流号
	 */
	private String txLogisticId;
	/**
	 * 淘宝订单号，在线订单才有值，线下下单没有
	 */
	private String tradeNo;
	/**
	 * 客户身份标识，对应user表 taobao_encode_key
	 */
	private String customerId;
	/**
	 * 运单号
	 */
	private String mailNo;
	/**
	 * 无业务含义，兼容 老系统
	 */
	private String type;
	/**
	 * 无业务含义，兼容 老系统
	 */
	private String flag;
	/**
	*
	*/
	private java.util.Date createTime;
	/**
	*
	*/
	private java.util.Date updateTime;
	/**
	 * 物流取货时间
	 */
	private java.util.Date sendStartTime;
	/**
	 * 物流取货时间
	 */
	private java.util.Date sendEndTime;
	/**
	 * 保价金额
	 */
	private Double insuranceValue;
	/**
	 * 是否需要包装
	 */
	private String packageOrNot;
	/**
	 * 特殊包裹
	 */
	private String special;
	/**
	 * 备注 //暂存单个订单的商品信息
	 */
	private String remark;

	/**
	 * 平台标识，订单来源哪个电商平台
	 */
	private String clientId;
	/**
	 * 订单状态
	 */
	private String status;
	/**
	 * 订单重量
	 */
	private Double weight;
	/**
	 * 无意义
	 */
	private Double signPrice;
	/**
	 * user表 id
	 */
	private String vipId;
	/**
	 * 0:online 1:offline
	 */
	private String lineType;
	/**
	 * 订单重量更新状态
	 */
	private String weightUpdateFlag;
	/**
	 * 订单类型
	 */
	private Integer orderType;
	/**
	 * 服务类型
	 */
	private Integer serviceType;
	/**
	 * 商品金额+运费
	 */
	private Double goodsValue;
	/**
	 * 商品金额
	 */
	private Double itemsValue;
	/**
	 * 总服务费
	 */
	private Double totalServiceFee;
	/**
	 * 买家服务费
	 */
	private Double buyServiceFee;
	/**
	 * cod分润
	 */
	private Double codSplitFee;
	/**
	 * 状态变更时间
	 */
	private java.util.Date acceptTime;
	/**
	 * 分区时间
	 */
	private java.util.Date partitiondate;
	/**
	*
	*/
	private String freightType;
	/**
	*
	*/
	private Double freight;
	/**
	*
	*/
	private Double trimFreight;

	

	/**
	 * 订单是否合并，已合并为Y, 未合并为N。默认值为N
	 */
	private String isCombine;

	/**
	 * 订单是否打印快递单标识，Y为已打印，N为未打印 ，默认值为N
	 */
	private String isPrint;

	/**
	 * 标记此订单是由几个订单合成
	 */
	private Integer joinNum;

	/**
	 * 是否打印发货单标识，Y为已打印，N为未打印，默认值为未打印
	 */
	private String isPrintSend;

	/**
	 * 代表是否批量发货，Y为已打印，N为未打印，默认值为未打印
	 */
	private String isSend;

	public String getIsSend() {
		return isSend;
	}

	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}

	/**
	 * 记录把当前订单和并到哪个订单的（保存此订单的ID），默认保存为 0
	 */
	private Integer parentId;
	/**
	 * 订单中商品按自然排序的第一个商品名称
	 */
	private String firstProductName;
	/**
	 * 保存此订单中商品的所有名称
	 */
	private String productName;


	/**
	 * 买家姓名
	 */
	private String buyName;
	/**
	 * 买家移动电话（手机）
	 */
	private String buyMobile;
	/**
	 * 买家固话
	 */
	private String buyTelphone;
	/**
	 * 买家拼好全称收货地址
	 */
	private String buyFulladdress;
	/**
	 * 买家所属省
	 */
	private String buyProv;
	/**
	 * 买家的所属市
	 */
	private String buyCity;
	/**
	 * 买家的所属地区
	 */
	private String buyDistrict;
	/**
	 * 买家的地址，不包括省市区
	 */
	private String buyAddress;
	/**
	 * 买家的邮政编码
	 */
	private String buyPostcode;

	/**
	 * 卖家姓名
	 */
	private String saleName;
	/**
	 * 卖家移动电话
	 */
	private String saleMobile;
	/**
	 * 卖家固话
	 */
	private String saleTelphone;
	/**
	 * 卖家收货地址，全称包括省市区
	 */
	private String saleFulladdress;
	/**
	 * 卖家所属省
	 */
	private String saleProv;
	/**
	 * 卖家所属市
	 */
	private String saleCity;
	/**
	 * 卖家所属地区
	 */
	private String saleDistrict;
	/**
	 * 卖家的地址，不包括省市区
	 */
	private String saleAddress;
	/**
	 * 老的商品价格
	 */
	private Double oldPrice;

	/**
	 * 买家的邮政编码
	 */
	private String postCode;

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public OrderPrint() {
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersion() {
		return this.version;
	}

	public void setLogisticProviderId(String logisticProviderId) {
		this.logisticProviderId = logisticProviderId;
	}

	public String getLogisticProviderId() {
		return this.logisticProviderId;
	}

	public void setTxLogisticId(String txLogisticId) {
		this.txLogisticId = txLogisticId;
	}

	public String getTxLogisticId() {
		return this.txLogisticId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getFlag() {
		return this.flag;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public java.util.Date getCreateTime() {
		return this.createTime;
	}

	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	public java.util.Date getUpdateTime() {
		return this.updateTime;
	}

	public void setSendStartTime(java.util.Date sendStartTime) {
		this.sendStartTime = sendStartTime;
	}

	public java.util.Date getSendStartTime() {
		return this.sendStartTime;
	}

	public void setSendEndTime(java.util.Date sendEndTime) {
		this.sendEndTime = sendEndTime;
	}

	public java.util.Date getSendEndTime() {
		return this.sendEndTime;
	}

	public void setPackageOrNot(String packageOrNot) {
		this.packageOrNot = packageOrNot;
	}

	public String getPackageOrNot() {
		return this.packageOrNot;
	}

	public void setSpecial(String special) {
		this.special = special;
	}

	public String getSpecial() {
		return this.special;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientId() {
		return this.clientId;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getWeight() {
		return this.weight;
	}

	public void setVipId(String vipId) {
		this.vipId = vipId;
	}

	public String getVipId() {
		return this.vipId;
	}

	public void setLineType(String lineType) {
		this.lineType = lineType;
	}

	public String getLineType() {
		return this.lineType;
	}

	public void setWeightUpdateFlag(String weightUpdateFlag) {
		this.weightUpdateFlag = weightUpdateFlag;
	}

	public String getWeightUpdateFlag() {
		return this.weightUpdateFlag;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getOrderType() {
		return this.orderType;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	public Integer getServiceType() {
		return this.serviceType;
	}

	public void setAcceptTime(java.util.Date acceptTime) {
		this.acceptTime = acceptTime;
	}

	public java.util.Date getAcceptTime() {
		return this.acceptTime;
	}

	public void setPartitiondate(java.util.Date partitiondate) {
		this.partitiondate = partitiondate;
	}

	public java.util.Date getPartitiondate() {
		return this.partitiondate;
	}

	public void setFreightType(String freightType) {
		this.freightType = freightType;
	}

	public String getFreightType() {
		return this.freightType;
	}

	public void setIsCombine(String isCombine) {
		this.isCombine = isCombine;
	}

	public String getIsCombine() {
		return this.isCombine;
	}

	public void setIsPrint(String isPrint) {
		this.isPrint = isPrint;
	}

	public String getIsPrint() {
		return this.isPrint;
	}

	public void setIsPrintSend(String isPrintSend) {
		this.isPrintSend = isPrintSend;
	}

	public String getIsPrintSend() {
		return this.isPrintSend;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setFirstProductName(String firstProductName) {
		this.firstProductName = firstProductName;
	}

	public String getFirstProductName() {
		return this.firstProductName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductName() {
		return this.productName;
	}


	public void setBuyName(String buyName) {
		this.buyName = buyName;
	}

	public String getBuyName() {
		return this.buyName;
	}

	public void setBuyMobile(String buyMobile) {
		this.buyMobile = buyMobile;
	}

	public String getBuyMobile() {
		return this.buyMobile;
	}

	public void setBuyTelphone(String buyTelphone) {
		this.buyTelphone = buyTelphone;
	}

	public String getBuyTelphone() {
		return this.buyTelphone;
	}

	public void setBuyFulladdress(String buyFulladdress) {
		this.buyFulladdress = buyFulladdress;
	}

	public String getBuyFulladdress() {
		return this.buyFulladdress;
	}

	public void setBuyProv(String buyProv) {
		this.buyProv = buyProv;
	}

	public String getBuyProv() {
		return this.buyProv;
	}

	public void setBuyCity(String buyCity) {
		this.buyCity = buyCity;
	}

	public String getBuyCity() {
		return this.buyCity;
	}

	public void setBuyDistrict(String buyDistrict) {
		this.buyDistrict = buyDistrict;
	}

	public String getBuyDistrict() {
		return this.buyDistrict;
	}

	public void setBuyAddress(String buyAddress) {
		this.buyAddress = buyAddress;
	}

	public String getBuyAddress() {
		return this.buyAddress;
	}

	public String getBuyPostcode() {
		return buyPostcode;
	}

	public void setBuyPostcode(String buyPostcode) {
		this.buyPostcode = buyPostcode;
	}

	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}

	public String getSaleName() {
		return this.saleName;
	}

	public void setSaleMobile(String saleMobile) {
		this.saleMobile = saleMobile;
	}

	public String getSaleMobile() {
		return this.saleMobile;
	}

	public void setSaleTelphone(String saleTelphone) {
		this.saleTelphone = saleTelphone;
	}

	public String getSaleTelphone() {
		return this.saleTelphone;
	}

	public void setSaleFulladdress(String saleFulladdress) {
		this.saleFulladdress = saleFulladdress;
	}

	public String getSaleFulladdress() {
		return this.saleFulladdress;
	}

	public void setSaleProv(String saleProv) {
		this.saleProv = saleProv;
	}

	public String getSaleProv() {
		return this.saleProv;
	}

	public void setSaleCity(String saleCity) {
		this.saleCity = saleCity;
	}

	public String getSaleCity() {
		return this.saleCity;
	}

	public void setSaleDistrict(String saleDistrict) {
		this.saleDistrict = saleDistrict;
	}

	public String getSaleDistrict() {
		return this.saleDistrict;
	}

	public void setSaleAddress(String saleAddress) {
		this.saleAddress = saleAddress;
	}

	public String getSaleAddress() {
		return this.saleAddress;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof OrderPrint)) {
			return false;
		}

		OrderPrint o = (OrderPrint) obj;
		if (this.id == null) {
			return o.getId() == null;
		}
		return this.id.equals(o.getId());
	}

	public int hashCode() {
		if (this.getId() == null) {
			return super.hashCode();
		}
		return this.getId().hashCode();
	}

	public Integer getJoinNum() {
		return joinNum;
	}

	public void setJoinNum(Integer joinNum) {
		this.joinNum = joinNum;
	}

	public Double getInsuranceValue() {
		return insuranceValue;
	}

	public void setInsuranceValue(Double insuranceValue) {
		this.insuranceValue = insuranceValue;
	}

	public Double getSignPrice() {
		return signPrice;
	}

	public void setSignPrice(Double signPrice) {
		this.signPrice = signPrice;
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

	public Double getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(Double oldPrice) {
		this.oldPrice = oldPrice;
	}
	
	
}