package net.ytoec.kernel.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"txLogisitcId","billCode","stockSysNo","sendName","sendAddress",
		"sendPostCode","sendPhone","sendMobile","sendProv","sendCity","sendCounty",
		"itemName","piece","special","remark","insuranceValue",
		"receiveName","receiveAddress","receivePostCode","receivePhone","receiveProv",
		"receiveCity","receiveCounty","freight","packedFee","weight","logisticProviderId",
		"backFund","itemsValue","goods","subPackages"})
@XmlRootElement(name = "TabUniteOrder")
public class TabUniteOrder {

	private String 	txLogisitcId;	// 物流号
	private String 	billCode;
	private int 	stockSysNo;		//分仓编号
	private String 	sendName;		//寄件客户姓名
	private String	sendAddress;	//寄件客户地址
	private String	sendPostCode;	//寄件客户邮编
	private String	sendPhone;		//寄件客户座机
	private String	sendMobile;		//寄件客户手机
	private String 	sendProv;		//寄件客户所属省
	private String 	sendCity;		//寄件客户所属市
	private String 	sendCounty;		//客户所属县/区
	private String	itemName;		//商品名称
	private int		piece;			//个数
	private int		special;		//特殊商品性质
	private String 	remark;			//商品描述
	private String	insuranceValue;	//保值金额（目前为支付宝的成交额）
	private String	receiveName;	//收件客户姓名
	private String	receiveAddress;	//收件客户地址
	private String 	receivePostCode;//收件客户邮编
	private String 	receivePhone;	//收件客户电话
	private String 	receiveProv;	//收件客户省份
	private String	receiveCity;	//收件客户市
	private String	receiveCounty;	//收件客户地区
	private String	freight;		//运费
	private String	packedFee;		//打包费
	private String	weight;			//重量
	private String	logisticProviderId;	//公司 比如说 易迅就直接写中文也可以
	private String	backFund;		//应退金额
	private String	itemsValue;		//代收货款
	private List<GoodInfo> goods;	//商品列表
	private List<String> subPackages;
	
	@XmlElementWrapper(name="subPackages")
	@XmlElement(name="subPackage")
	public List<String> getSubPackages() {
		return subPackages;
	}
	public void setSubPackages(List<String> subPackages) {
		this.subPackages = subPackages;
	}
	@XmlElementWrapper(name="goods")
	@XmlElement(name="good")
	public List<GoodInfo> getGoods() {
		return goods;
	}
	public void setGoods(List<GoodInfo> goods) {
		this.goods = goods;
	}

	public int getStockSysNo() {
		return stockSysNo;
	}
	public void setStockSysNo(int stockSysNo) {
		this.stockSysNo = stockSysNo;
	}
	public String getSendName() {
		return sendName;
	}
	public void setSendName(String sendName) {
		this.sendName = sendName;
	}
	public String getSendAddress() {
		return sendAddress;
	}
	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}
	public String getSendPostCode() {
		return sendPostCode;
	}
	public void setSendPostCode(String sendPostCode) {
		this.sendPostCode = sendPostCode;
	}
	public String getSendPhone() {
		return sendPhone;
	}
	public void setSendPhone(String sendPhone) {
		this.sendPhone = sendPhone;
	}
	public String getSendMobile() {
		return sendMobile;
	}
	public void setSendMobile(String sendMobile) {
		this.sendMobile = sendMobile;
	}
	public String getSendProv() {
		return sendProv;
	}
	public void setSendProv(String sendProv) {
		this.sendProv = sendProv;
	}
	public String getSendCity() {
		return sendCity;
	}
	public void setSendCity(String sendCity) {
		this.sendCity = sendCity;
	}
	public String getSendCounty() {
		return sendCounty;
	}
	public void setSendCounty(String sendCounty) {
		this.sendCounty = sendCounty;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public int getPiece() {
		return piece;
	}
	public void setPiece(int piece) {
		this.piece = piece;
	}
	public int getSpecial() {
		return special;
	}
	public void setSpecial(int special) {
		this.special = special;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getInsuranceValue() {
		return insuranceValue;
	}
	public void setInsuranceValue(String insuranceValue) {
		this.insuranceValue = insuranceValue;
	}
	public String getReceiveName() {
		return receiveName;
	}
	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	public String getReceiveAddress() {
		return receiveAddress;
	}
	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}
	public String getReceivePostCode() {
		return receivePostCode;
	}
	public void setReceivePostCode(String receivePostCode) {
		this.receivePostCode = receivePostCode;
	}
	public String getReceivePhone() {
		return receivePhone;
	}
	public void setReceivePhone(String receivePhone) {
		this.receivePhone = receivePhone;
	}
	public String getReceiveProv() {
		return receiveProv;
	}
	public void setReceiveProv(String receiveProv) {
		this.receiveProv = receiveProv;
	}
	public String getReceiveCity() {
		return receiveCity;
	}
	public void setReceiveCity(String receiveCity) {
		this.receiveCity = receiveCity;
	}
	public String getReceiveCounty() {
		return receiveCounty;
	}
	public void setReceiveCounty(String receiveCounty) {
		this.receiveCounty = receiveCounty;
	}
	public String getFreight() {
		return freight;
	}
	public void setFreight(String freight) {
		this.freight = freight;
	}
	public String getPackedFee() {
		return packedFee;
	}
	public void setPackedFee(String packedFee) {
		this.packedFee = packedFee;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getLogisticProviderId() {
		return logisticProviderId;
	}
	public void setLogisticProviderId(String logisticProviderId) {
		this.logisticProviderId = logisticProviderId;
	}
	public String getBackFund() {
		return backFund;
	}
	public void setBackFund(String backFund) {
		this.backFund = backFund;
	}
	public String getItemsValue() {
		return itemsValue;
	}
	public void setItemsValue(String itemsValue) {
		this.itemsValue = itemsValue;
	}
	public String getTxLogisitcId() {
		return txLogisitcId;
	}
	public void setTxLogisitcId(String txLogisitcId) {
		this.txLogisitcId = txLogisitcId;
	}
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	
	
}
