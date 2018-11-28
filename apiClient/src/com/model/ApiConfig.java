package com.model;

/**
 * 同步面单和上传订单的url地址
 * 
 * @author huangtianfu
 * 
 */
public class ApiConfig {

	private int apiId;

	/**
	 * 同步面单url
	 */
	private String synWaybillUrl;

	/**
	 * 上传订单url
	 */
	private String uploadOrderUrl;

	public int getApiId() {
		return apiId;
	}

	public void setApiId(int apiId) {
		this.apiId = apiId;
	}

	public String getSynWaybillUrl() {
		return synWaybillUrl;
	}

	public void setSynWaybillUrl(String synWaybillUrl) {
		this.synWaybillUrl = synWaybillUrl;
	}

	public String getUploadOrderUrl() {
		return uploadOrderUrl;
	}

	public void setUploadOrderUrl(String uploadOrderUrl) {
		this.uploadOrderUrl = uploadOrderUrl;
	}

}
