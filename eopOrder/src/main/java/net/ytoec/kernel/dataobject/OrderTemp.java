package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.Date;

import sun.tools.tree.ThisExpression;

/**
 * 订单导入时使用的临时表
 * 
 * @author wuguiqiang
 * @date 2012-6-11
 */
@SuppressWarnings("all")
public class OrderTemp implements Serializable {

	private static final long serialVersionUID = -3213518267865090250L;

	private int id;
	// 订单号
	private String mailNo;
	// 运单号
	private String txLogisticId;
	// 买家姓名
	private String buyerName;
	// 收货人姓名
	private String name;
	// 省
	private String prov;
	// 市
	private String city;
	// 区县
	private String district;
	// 详细地址ַ
	private String address;
	// 邮政编码
	private String postCode;
	// 固定电话
	private String phone;
	// 移动电话
	private String mobile;
	// 发货人姓名
	private String sendName;
	// 发货地址
	private String sendAddress;
	// 发货人手机
	private String sendMobile;
	// 发货人电话
	private String sendPhone;
	// 订单创建时间
	private String createTime;
	// 物品名称
	private String goodsName;
	// 物品总价
	private String totalPrice;
	// 物品单价
	private String singlePrice;
	// 物品数量
	private String quantity;
	// 创建临时订单的时间
	private Date partitionDate;
	// 临时订单过期时间
	private Date endDate;
	// 渠道clientId
	private String clientId;
	// 预留字段（无意义）
	private String remark;

	// getter and setter
	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public String getSendMobile() {
		return sendMobile;
	}

	public void setSendMobile(String sendMobile) {
		this.sendMobile = sendMobile;
	}

	public String getSendPhone() {
		return sendPhone;
	}

	public void setSendPhone(String sendPhone) {
		this.sendPhone = sendPhone;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	
	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getSinglePrice() {
		return singlePrice;
	}

	public void setSinglePrice(String singlePrice) {
		this.singlePrice = singlePrice;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public Date getPartitionDate() {
		return partitionDate;
	}

	public void setPartitionDate(Date partitionDate) {
		this.partitionDate = partitionDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("id = ");
		sb.append(this.id);
		sb.append(",mailNo = ");
		sb.append(this.mailNo);
		sb.append(",txLogisticId = ");
		sb.append(this.txLogisticId);
		sb.append(",buyerName = ");
		sb.append(this.buyerName);
		sb.append(",name = ");
		sb.append(this.name);
		sb.append(",postCode = ");
		sb.append(this.postCode);
		sb.append(",phone = ");
		sb.append(this.phone);
		sb.append(",mobile = ");
		sb.append(this.mobile);
		sb.append(",sendName = ");
		sb.append(this.sendName);
		sb.append(",sendAddress = ");
		sb.append(this.sendAddress);
		sb.append(",sendMobile = ");
		sb.append(this.sendMobile);
		sb.append(",sendPhone = ");
		sb.append(this.sendPhone);
		sb.append(",prov = ");
		sb.append(this.prov);
		sb.append(",city = ");
		sb.append(this.city);
		sb.append(",address = ");
		sb.append(this.address);
		sb.append(",district = ");
		sb.append(this.district);
		sb.append(",createTime = ");
		sb.append(this.createTime);
		sb.append(",goodsName = ");
		sb.append(this.goodsName);
		sb.append(",totalPrice = ");
		sb.append(this.totalPrice);
		sb.append(",singlePrice = ");
		sb.append(this.singlePrice);
		sb.append(",quantity = ");
		sb.append(this.quantity);
		sb.append(",partitionDate = ");
		sb.append(this.partitionDate);
		sb.append(",endDate = ");
		sb.append(this.endDate);
		return sb.toString();
	}

}
