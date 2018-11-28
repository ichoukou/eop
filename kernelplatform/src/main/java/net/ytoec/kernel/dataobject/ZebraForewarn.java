package net.ytoec.kernel.dataobject;

import java.util.Date;

public class ZebraForewarn {
	private Integer forewarnId;

	private Integer branckId;

	private String customerCode;

	private Integer branckWarnValue;

	private Integer customerWarnValue;

	private Integer customerWarnHistoryValue;

	private String phone;

	private String remarkPhone;

	private String email;

	private Boolean phoneWarn;

	private Boolean emailWarn;

	private Boolean initState;

	private Boolean warnState;

	private Boolean sendPhoneState;

	private Boolean sendMailState;

	private Date crateTime;

	private Date updateTime;

	public Integer getForewarnId() {
		return forewarnId;
	}

	public void setForewarnId(Integer forewarnId) {
		this.forewarnId = forewarnId;
	}

	public Integer getBranckId() {
		return branckId;
	}

	public void setBranckId(Integer branckId) {
		this.branckId = branckId;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public Integer getBranckWarnValue() {
		return branckWarnValue;
	}

	public void setBranckWarnValue(Integer branckWarnValue) {
		this.branckWarnValue = branckWarnValue;
	}

	public Integer getCustomerWarnValue() {
		return customerWarnValue;
	}

	public void setCustomerWarnValue(Integer customerWarnValue) {
		this.customerWarnValue = customerWarnValue;
	}

	public Integer getCustomerWarnHistoryValue() {
		return customerWarnHistoryValue;
	}

	public void setCustomerWarnHistoryValue(Integer customerWarnHistoryValue) {
		this.customerWarnHistoryValue = customerWarnHistoryValue;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRemarkPhone() {
		return remarkPhone;
	}

	public void setRemarkPhone(String remarkPhone) {
		this.remarkPhone = remarkPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean isPhoneWarn() {
		return phoneWarn;
	}

	public void setPhoneWarn(Boolean phoneWarn) {
		this.phoneWarn = phoneWarn;
	}

	public Boolean isEmailWarn() {
		return emailWarn;
	}

	public void setEmailWarn(Boolean emailWarn) {
		this.emailWarn = emailWarn;
	}

	public Boolean isInitState() {
		return initState;
	}

	public void setInitState(Boolean initState) {
		this.initState = initState;
	}

	public Boolean isWarnState() {
		return warnState;
	}

	public void setWarnState(Boolean warnState) {
		this.warnState = warnState;
	}

	public Boolean isSendPhoneState() {
		return sendPhoneState;
	}

	public void setSendPhoneState(Boolean sendPhoneState) {
		this.sendPhoneState = sendPhoneState;
	}

	public Boolean isSendMailState() {
		return sendMailState;
	}

	public void setSendMailState(Boolean sendMailState) {
		this.sendMailState = sendMailState;
	}

	public Date getCrateTime() {
		return crateTime;
	}

	public void setCrateTime(Date crateTime) {
		this.crateTime = crateTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}