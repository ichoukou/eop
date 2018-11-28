package com.model;

/**
 * 对接账号信息
 * 
 * @author huangtianfu
 * 
 */
public class UserConfig {

	/**
	 * 主键
	 */
	private int userId;

	/**
	 * 商家代码
	 */
	private String customerCode;

	/**
	 * 商家密钥
	 */
	private String parternId;

	/**
	 * 客户端标识
	 */
	private String clientId;

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getParternId() {
		return parternId;
	}

	public void setParternId(String parternId) {
		this.parternId = parternId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		if ("".equals(clientId)) {
			return "====请选择用户====";
		}
		return this.clientId;
	}

}
