package net.ytoec.kernel.dataobject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ytoec.kernel.action.remote.process.RequestOrderProcessor;
import net.ytoec.kernel.action.remote.xml.Response;

/**
 * 
 * 订单信息.<RequestOrder>
 * 
 */
public class OrderFormInfo {
	// 版本号主要用于升级
	private String version;
	// 物流公司ID
	private String logisticProviderId;
	private String clientId;
	private String customerId;

	private Integer id;

	// 这是物流平台交易生成的物流号
	private String txLogisticId;
	// 业务的交易号（可选）
	private String tradeNo;
	// 物流公司的运单号
	private String mailNo;
	// 发货单号
	private String deliverNo;
	// 发货单 代收款
	private double agencyFundEle;

	//
	private String flag;
	// 发货方信息
	private TraderInfo sender = new TraderInfo();
	// 收货方信息
	private TraderInfo receiver = new TraderInfo();
	//
	private String sendStartTime;
	//
	private String sendEndTime;
	// 商口列表
	private List<Product> items = new ArrayList<Product>();

	// 保价
	private double insuranceValue;
	// 打包
	private boolean packageOrNot;
	// 特殊商品性质
	private SpecialType special;
	// 备注
	private String remark;

	private String createTimeStr;
	private Date createTime;

	private String vipId;

	private String lineType;

	private String orderType;

	private String serviceType;

	private String goodsValue;

	private String itemsValue;

	private String totalServiceFee;

	private String buyServiceFee;

	private String codSplitFee;

	private String type;

	private String itemsWeight;

	private String product;
	private String num;
	private String shopName;

	private Date partitiondate;
	
	private boolean isFromJGCreate;

	/**
	 * 区分是哪个模块调用， 1,我要发货， 2其他,3淘宝
	 */
	private String status;

	public double getAgencyFundEle() {
		return agencyFundEle;
	}

	public void setAgencyFundEle(double agencyFundEle) {
		this.agencyFundEle = agencyFundEle;
	}

	public String getDeliverNo() {
		return deliverNo;
	}

	public void setDeliverNo(String deliverNo) {
		this.deliverNo = deliverNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getItemsWeight() {
		return itemsWeight;
	}

	public void setItemsWeight(String itemsWeight) {
		this.itemsWeight = itemsWeight;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getGoodsValue() {
		return goodsValue;
	}

	public void setGoodsValue(String goodsValue) {
		this.goodsValue = goodsValue;
	}

	public String getItemsValue() {
		return itemsValue;
	}

	public void setItemsValue(String itemsValue) {
		this.itemsValue = itemsValue;
	}

	public String getTotalServiceFee() {
		return totalServiceFee;
	}

	public void setTotalServiceFee(String totalServiceFee) {
		this.totalServiceFee = totalServiceFee;
	}

	public String getBuyServiceFee() {
		return buyServiceFee;
	}

	public void setBuyServiceFee(String buyServiceFee) {
		this.buyServiceFee = buyServiceFee;
	}

	public String getCodSplitFee() {
		return codSplitFee;
	}

	public void setCodSplitFee(String codSplitFee) {
		this.codSplitFee = codSplitFee;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setInsuranceValue(double insuranceValue) {
		this.insuranceValue = insuranceValue;
	}

	private Date updateTime;

	// 响应对象.
	private Response response = new Response();

	private RequestOrderProcessor requestOrderProcessor = new RequestOrderProcessor(
			this);

	public OrderFormInfo() {

	}

	public OrderFormInfo(String xmlFragment) {

	}

	/**
	 * 根据xml片段信息生成对象数据.
	 * 
	 * @param xmlFragment
	 *            xml片段.
	 * @return 返回RequestOrder对象.
	 */
	public OrderFormInfo toObject(String xmlFragment) {

		OrderFormInfo requestOrder = requestOrderProcessor.parse(xmlFragment);
		return requestOrder;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public void addItem(Product item) {
		this.items.add(item);
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

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getSendStartTime() {
		return sendStartTime;
	}

	public void setSendStartTime(String sendStartTime) {
		this.sendStartTime = sendStartTime;
	}

	public String getSendEndTime() {
		return sendEndTime;
	}

	public void setSendEndTime(String sendEndTime) {
		this.sendEndTime = sendEndTime;
	}

	public List<Product> getItems() {
		return items;
	}

	public void setItems(List<Product> items) {
		this.items = items;
	}

	public double getInsuranceValue() {
		return insuranceValue;
	}

	public boolean isPackageOrNot() {
		return packageOrNot;
	}

	public void setPackageOrNot(boolean packageOrNot) {
		this.packageOrNot = packageOrNot;
	}

	public SpecialType getSpecial() {
		return special;
	}

	public void setSpecial(SpecialType special) {
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

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("version=");
		sb.append(this.version);
		sb.append(",logisticProviderId=");
		sb.append(this.logisticProviderId);
		sb.append(",txLogisticId=");
		sb.append(this.txLogisticId);
		sb.append(",sender=");
		sb.append(this.getSender());
		sb.append(",special=");
		sb.append(this.special.getDescriptor());
		return sb.toString();
	}

	public TraderInfo getSender() {
		return sender;
	}

	public void setSender(TraderInfo sender) {
		this.sender = sender;
	}

	public TraderInfo getReceiver() {
		return receiver;
	}

	public void setReceiver(TraderInfo receiver) {
		this.receiver = receiver;
	}

	public RequestOrderProcessor getRequestOrderProcessor() {
		return requestOrderProcessor;
	}

	public void setRequestOrderProcessor(
			RequestOrderProcessor requestOrderProcessor) {
		this.requestOrderProcessor = requestOrderProcessor;
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

	public String verifyData() {

		return "";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Date getPartitiondate() {
		return partitiondate;
	}

	public void setPartitiondate(Date partitiondate) {
		this.partitiondate = partitiondate;
	}

	public boolean isFromJGCreate() {
		return isFromJGCreate;
	}

	public void setFromJGCreate(boolean isFromJGCreate) {
		this.isFromJGCreate = isFromJGCreate;
	}

}
