package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.Date;

/**
 * 渠道信息表 bean对象
 * 
 * @author ChenRen
 * @date 2011-7-19
 */
public class Channel implements Serializable {

	private static final long serialVersionUID = 1609190165338281629L;
	public static final String DISPATCH = "1";

	// 是否推送运单号 1为推送，0 为不推送
	public static final String ISSEND = "1";
	// 是否打印面单 1：打印 0 不打印
	public static final String ISPRINT = "1";

	// === fields ===
	private Integer id;
	/** 属性名 */
	private String key;
	/** 属性值 */
	private String value;
	private String clientId;

	private String parternId;

	private String ip;

	private String isSend;

	private String isPrint;

	private String userName;
	private String userCode;

	private String lineType;

	private String userType;// 用户类型，0:第三方对接商家,1:仓配通商家

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	// 判断用户是否通过新龙渠道查件 Y:是 ,N:否
	private String vip;

	public String getVip() {
		return vip;
	}

	public void setVip(String vip) {
		this.vip = vip;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIsSend() {
		return isSend;
	}

	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}

	public String getIsPrint() {
		return isPrint;
	}

	public void setIsPrint(String isPrint) {
		this.isPrint = isPrint;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getParternId() {
		return parternId;
	}

	public void setParternId(String parternId) {
		this.parternId = parternId;
	}

	/** url中除参数外部分 */
	private String ipAddress;
	private Date createTime;
	private Date updateTime;

	// === constructor ===
	public Channel() {
	};

	// === getter && setter
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	// === method ===
	@Override
	public String toString() {

		return this.id + "#" + this.key + "#" + this.value + "#"
				+ this.ipAddress + "#" + this.createTime + "#"
				+ this.updateTime;
	}

	public void setLineType(String lineType) {
		this.lineType = lineType;
	}

	public String getLineType() {
		return lineType;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

}
