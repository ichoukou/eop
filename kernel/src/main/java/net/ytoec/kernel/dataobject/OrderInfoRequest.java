package net.ytoec.kernel.dataobject;

public class OrderInfoRequest{
	
	private String txLogisticId;// 主订单编号
	private String childLogisticId;// 自订单编号
	private String deliverNo;// 发货单号
	private String saleName;// 卖家姓名
	private String buyName;// 买家姓名
	private String buyMobile;// 买家电话
	private String buyTelphone;// 买家手机
	private String buyProv;// 买家收货省
	private String buyCity;// 买家收货市
	private String buyDistrict;// 买家收货区
	private String buyAddress;// 买家收货详细地址
	private String buyPostcode;// 买家邮编
	private String agencyFund;// 代收金额
	private String bigPen;// 大头笔
	private String itemName;// 商品名称
	private String itemNumber;// 商品数量
	private String customerId;// 客户标识
	private String remark;// 备注

	public OrderInfoRequest() {
		super();
	}

	public OrderInfoRequest(String txLogisticId, String childLogisticId,
			String deliverNo, String saleName, String buyName,
			String buyMobile, String buyTelphone, String buyProv,
			String buyCity, String buyDistrict, String buyAddress,
			String buyPostcode, String agencyFund, String bigPen,
			String itemName, String itemNumber, String customerId, String remark) {
		super();
		this.txLogisticId = txLogisticId;
		this.childLogisticId = childLogisticId;
		this.deliverNo = deliverNo;
		this.saleName = saleName;
		this.buyName = buyName;
		this.buyMobile = buyMobile;
		this.buyTelphone = buyTelphone;
		this.buyProv = buyProv;
		this.buyCity = buyCity;
		this.buyDistrict = buyDistrict;
		this.buyAddress = buyAddress;
		this.buyPostcode = buyPostcode;
		this.agencyFund = agencyFund;
		this.bigPen = bigPen;
		this.itemName = itemName;
		this.itemNumber = itemNumber;
		this.customerId = customerId;
		this.remark = remark;
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

	public String getDeliverNo() {
		return deliverNo;
	}

	public void setDeliverNo(String deliverNo) {
		this.deliverNo = deliverNo;
	}

	public String getSaleName() {
		return saleName;
	}

	public void setSaleName(String saleName) {
		this.saleName = saleName;
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

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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
}
