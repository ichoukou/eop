package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 联系人,指发件人、收件人.
 * 
 * 
 */

public class TraderInfo implements Serializable {
	/**
	 * 联系人标识:发件人.
	 */
	public static final String SENDE_TYPE = "0";
	/**
	 * 联系人标识:收件人.
	 */
	public static final String RECEIVE_TYPE = "1";
	private static final long serialVersionUID = 5314495939524421370L;
	private int id;
	// 姓名
	private String name;
	// 邮政编码
	private String postCode;
	// 固定电话
	private String phone;

	// 联系人标识:0:sender发件人, 1:receiver收件人.
	private String tradeType;

	// 移动电话
	private String mobile;
	// 省
	private String prov;
	// 市
	private String city;
	// 区县
	private String district;
	// 详细地址ַ
	private String address;

	private int orderId;
    
	private String createTimeStr;
	private Date createTime;
	private Date updateTime;
	
	private Date partitionDate;
	
	// 省
	private int numProv;
	// 市
	private String numCity;
	// 区县
	private String numDistrict;
	
	private String remark;

	public int getNumProv() {
		return numProv;
	}

	public void setNumProv(int numProv) {
		this.numProv = numProv;
	}

	public String getNumCity() {
		return numCity;
	}

	public void setNumCity(String numCity) {
		this.numCity = numCity;
	}

	public String getNumDistrict() {
		return numDistrict;
	}

	public void setNumDistrict(String numDistrict) {
		this.numDistrict = numDistrict;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getPartitionDate() {
		return partitionDate;
	}

	public void setPartitionDate(Date partitionDate) {
		this.partitionDate = partitionDate;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
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

	/**
	 * 验证对象数据.
	 * 
	 * @return 验证通过返回true,验证不通过返回false.
	 */
	public boolean validate() {
		// 如果固定电话和移动电话都为空,返回false, "手机和电话两者必需提供一个"。
		if ((this.phone == null) && (this.mobile == null)) {
			return false;
		}
		return true;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("name=");
		sb.append(this.name);
		sb.append(",postCode=");
		sb.append(this.postCode);
		sb.append(",phone=");
		sb.append(this.phone);
		sb.append(",mobile=");
		sb.append(this.mobile);
		sb.append(",prov=");
		sb.append(this.prov);
		sb.append(",city=");
		sb.append(this.city);
		sb.append(",address=");
		sb.append(this.address);
		return sb.toString();
	}

}
