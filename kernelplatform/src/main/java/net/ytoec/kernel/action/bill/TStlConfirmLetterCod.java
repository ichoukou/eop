package net.ytoec.kernel.action.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TStlConfirmLetterCod implements Serializable{
	private static final long serialVersionUID = 4535337333510950027L;
	private String waybillNo;
	private String confirmFlg;
	private BigDecimal initialCodAmount;
	private BigDecimal actualCodAmount;
	private String currencyType;
	private BigDecimal exchangeRate;
	private String sendOrgCode;
	private String sendOrgName;
	private String deliveryOrgCode;
	private String deliveryOrgName;
	private String pubReason;
	private String pubMemo;
	private Date pubTime;
	private String pubEmpNo;
	private String pubEmpName;
	private Date pubModifyTime;
	private String pubModifyEmpCode;
	private String pubModifyEmpName;
	private String pubOrgCode;
	private String pubOrgName;
	private String pubContact;
	private String pubTel;
	private String reOrgCode;
	private String reOrgName;
	private String reContent;
	private String reMemo;
	private Date reTime;
	private String reEmpNo;
	private String reEmpName;
	private Date modifyTime;
	private String modifyEmployeeNo;
	private String modifyEmployeeName;
	private String modifyOrgCode;
	private String modifyOrgName;
	private String memo;
	private String deleteFlg;
	private String pubStlOrgCode;
	private String sendCustomerCode;
	private String sendCustomerName;
	
	//
	private String diffItemCode;
	private String diffItemName;
	private String initialValue;
	private String informValue;
	private String informReason;
	private String informOrgCode;
	private String informOrgName;
	private Date informTime;
	private String informEmployeeNo;
	private String informEmployeeName;
	private Date modifyDiffTime;
	private String modifyDiffEmployeeNo;
	private String modifyDiffEmployeeName;
	private String modifyDiffOrgCode;
	private String modifyDiffOrgName;
	private String img1;
	private String img2;
	private String img3;
	private String img4;
	private String informMemo;
	private String settleOrgCode;
	private String settleOrgName;
	private String customerConfFlg;
	private String diffType;
	private String reDiffOrgCode;
	private String reDiffOrgName;
	private String reDiffMemo;
	private String reDiffEmpNo;
	private String reDiffEmpName;
	private Date reDiffTime;
	
	
	public String getWaybillNo() {
		return waybillNo;
	}
	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}
	public String getConfirmFlg() {
		return confirmFlg;
	}
	public void setConfirmFlg(String confirmFlg) {
		this.confirmFlg = confirmFlg;
	}
	public BigDecimal getInitialCodAmount() {
		return initialCodAmount;
	}
	public void setInitialCodAmount(BigDecimal initialCodAmount) {
		this.initialCodAmount = initialCodAmount;
	}
	public BigDecimal getActualCodAmount() {
		return actualCodAmount;
	}
	public void setActualCodAmount(BigDecimal actualCodAmount) {
		this.actualCodAmount = actualCodAmount;
	}
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public String getSendOrgCode() {
		return sendOrgCode;
	}
	public void setSendOrgCode(String sendOrgCode) {
		this.sendOrgCode = sendOrgCode;
	}
	public String getSendOrgName() {
		return sendOrgName;
	}
	public void setSendOrgName(String sendOrgName) {
		this.sendOrgName = sendOrgName;
	}
	public String getDeliveryOrgCode() {
		return deliveryOrgCode;
	}
	public void setDeliveryOrgCode(String deliveryOrgCode) {
		this.deliveryOrgCode = deliveryOrgCode;
	}
	public String getDeliveryOrgName() {
		return deliveryOrgName;
	}
	public void setDeliveryOrgName(String deliveryOrgName) {
		this.deliveryOrgName = deliveryOrgName;
	}
	public String getPubReason() {
		return pubReason;
	}
	public void setPubReason(String pubReason) {
		this.pubReason = pubReason;
	}
	public String getPubMemo() {
		return pubMemo;
	}
	public void setPubMemo(String pubMemo) {
		this.pubMemo = pubMemo;
	}
	public Date getPubTime() {
		return pubTime;
	}
	public void setPubTime(Date pubTime) {
		this.pubTime = pubTime;
	}
	public String getPubEmpNo() {
		return pubEmpNo;
	}
	public void setPubEmpNo(String pubEmpNo) {
		this.pubEmpNo = pubEmpNo;
	}
	public String getPubEmpName() {
		return pubEmpName;
	}
	public void setPubEmpName(String pubEmpName) {
		this.pubEmpName = pubEmpName;
	}
	public Date getPubModifyTime() {
		return pubModifyTime;
	}
	public void setPubModifyTime(Date pubModifyTime) {
		this.pubModifyTime = pubModifyTime;
	}
	public String getPubModifyEmpCode() {
		return pubModifyEmpCode;
	}
	public void setPubModifyEmpCode(String pubModifyEmpCode) {
		this.pubModifyEmpCode = pubModifyEmpCode;
	}
	public String getPubModifyEmpName() {
		return pubModifyEmpName;
	}
	public void setPubModifyEmpName(String pubModifyEmpName) {
		this.pubModifyEmpName = pubModifyEmpName;
	}
	public String getPubOrgCode() {
		return pubOrgCode;
	}
	public void setPubOrgCode(String pubOrgCode) {
		this.pubOrgCode = pubOrgCode;
	}
	public String getPubOrgName() {
		return pubOrgName;
	}
	public void setPubOrgName(String pubOrgName) {
		this.pubOrgName = pubOrgName;
	}
	public String getPubContact() {
		return pubContact;
	}
	public void setPubContact(String pubContact) {
		this.pubContact = pubContact;
	}
	public String getPubTel() {
		return pubTel;
	}
	public void setPubTel(String pubTel) {
		this.pubTel = pubTel;
	}
	public String getReOrgCode() {
		return reOrgCode;
	}
	public void setReOrgCode(String reOrgCode) {
		this.reOrgCode = reOrgCode;
	}
	public String getReOrgName() {
		return reOrgName;
	}
	public void setReOrgName(String reOrgName) {
		this.reOrgName = reOrgName;
	}
	public String getReContent() {
		return reContent;
	}
	public void setReContent(String reContent) {
		this.reContent = reContent;
	}
	public String getReMemo() {
		return reMemo;
	}
	public void setReMemo(String reMemo) {
		this.reMemo = reMemo;
	}
	public Date getReTime() {
		return reTime;
	}
	public void setReTime(Date reTime) {
		this.reTime = reTime;
	}
	public String getReEmpNo() {
		return reEmpNo;
	}
	public void setReEmpNo(String reEmpNo) {
		this.reEmpNo = reEmpNo;
	}
	public String getReEmpName() {
		return reEmpName;
	}
	public void setReEmpName(String reEmpName) {
		this.reEmpName = reEmpName;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getModifyEmployeeNo() {
		return modifyEmployeeNo;
	}
	public void setModifyEmployeeNo(String modifyEmployeeNo) {
		this.modifyEmployeeNo = modifyEmployeeNo;
	}
	public String getModifyEmployeeName() {
		return modifyEmployeeName;
	}
	public void setModifyEmployeeName(String modifyEmployeeName) {
		this.modifyEmployeeName = modifyEmployeeName;
	}
	public String getModifyOrgCode() {
		return modifyOrgCode;
	}
	public void setModifyOrgCode(String modifyOrgCode) {
		this.modifyOrgCode = modifyOrgCode;
	}
	public String getModifyOrgName() {
		return modifyOrgName;
	}
	public void setModifyOrgName(String modifyOrgName) {
		this.modifyOrgName = modifyOrgName;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getDeleteFlg() {
		return deleteFlg;
	}
	public void setDeleteFlg(String deleteFlg) {
		this.deleteFlg = deleteFlg;
	}
	public String getPubStlOrgCode() {
		return pubStlOrgCode;
	}
	public void setPubStlOrgCode(String pubStlOrgCode) {
		this.pubStlOrgCode = pubStlOrgCode;
	}
	public String getSendCustomerCode() {
		return sendCustomerCode;
	}
	public void setSendCustomerCode(String sendCustomerCode) {
		this.sendCustomerCode = sendCustomerCode;
	}
	public String getSendCustomerName() {
		return sendCustomerName;
	}
	public void setSendCustomerName(String sendCustomerName) {
		this.sendCustomerName = sendCustomerName;
	}
	
	//
	public String getDiffItemCode() {
		return diffItemCode;
	}
	public void setDiffItemCode(String diffItemCode) {
		this.diffItemCode = diffItemCode;
	}
	public String getDiffItemName() {
		return diffItemName;
	}
	public void setDiffItemName(String diffItemName) {
		this.diffItemName = diffItemName;
	}
	public String getInitialValue() {
		return initialValue;
	}
	public void setInitialValue(String initialValue) {
		this.initialValue = initialValue;
	}
	public String getInformValue() {
		return informValue;
	}
	public void setInformValue(String informValue) {
		this.informValue = informValue;
	}
	public String getInformReason() {
		return informReason;
	}
	public void setInformReason(String informReason) {
		this.informReason = informReason;
	}
	public String getInformOrgCode() {
		return informOrgCode;
	}
	public void setInformOrgCode(String informOrgCode) {
		this.informOrgCode = informOrgCode;
	}
	public String getInformOrgName() {
		return informOrgName;
	}
	public void setInformOrgName(String informOrgName) {
		this.informOrgName = informOrgName;
	}
	public Date getInformTime() {
		return informTime;
	}
	public void setInformTime(Date informTime) {
		this.informTime = informTime;
	}
	public String getInformEmployeeNo() {
		return informEmployeeNo;
	}
	public void setInformEmployeeNo(String informEmployeeNo) {
		this.informEmployeeNo = informEmployeeNo;
	}
	public String getInformEmployeeName() {
		return informEmployeeName;
	}
	public void setInformEmployeeName(String informEmployeeName) {
		this.informEmployeeName = informEmployeeName;
	}
	public Date getModifyDiffTime() {
		return modifyDiffTime;
	}
	public void setModifyDiffTime(Date modifyDiffTime) {
		this.modifyDiffTime = modifyDiffTime;
	}
	public String getModifyDiffEmployeeNo() {
		return modifyDiffEmployeeNo;
	}
	public void setModifyDiffEmployeeNo(String modifyDiffEmployeeNo) {
		this.modifyDiffEmployeeNo = modifyDiffEmployeeNo;
	}
	public String getModifyDiffEmployeeName() {
		return modifyDiffEmployeeName;
	}
	public void setModifyDiffEmployeeName(String modifyDiffEmployeeName) {
		this.modifyDiffEmployeeName = modifyDiffEmployeeName;
	}
	public String getModifyDiffOrgCode() {
		return modifyDiffOrgCode;
	}
	public void setModifyDiffOrgCode(String modifyDiffOrgCode) {
		this.modifyDiffOrgCode = modifyDiffOrgCode;
	}
	public String getModifyDiffOrgName() {
		return modifyDiffOrgName;
	}
	public void setModifyDiffOrgName(String modifyDiffOrgName) {
		this.modifyDiffOrgName = modifyDiffOrgName;
	}
	public String getImg1() {
		return img1;
	}
	public void setImg1(String img1) {
		this.img1 = img1;
	}
	public String getImg2() {
		return img2;
	}
	public void setImg2(String img2) {
		this.img2 = img2;
	}
	public String getImg3() {
		return img3;
	}
	public void setImg3(String img3) {
		this.img3 = img3;
	}
	public String getImg4() {
		return img4;
	}
	public void setImg4(String img4) {
		this.img4 = img4;
	}
	public String getInformMemo() {
		return informMemo;
	}
	public void setInformMemo(String informMemo) {
		this.informMemo = informMemo;
	}
	public String getSettleOrgCode() {
		return settleOrgCode;
	}
	public void setSettleOrgCode(String settleOrgCode) {
		this.settleOrgCode = settleOrgCode;
	}
	public String getSettleOrgName() {
		return settleOrgName;
	}
	public void setSettleOrgName(String settleOrgName) {
		this.settleOrgName = settleOrgName;
	}
	public String getCustomerConfFlg() {
		return customerConfFlg;
	}
	public void setCustomerConfFlg(String customerConfFlg) {
		this.customerConfFlg = customerConfFlg;
	}
	public String getDiffType() {
		return diffType;
	}
	public void setDiffType(String diffType) {
		this.diffType = diffType;
	}
	public String getReDiffOrgCode() {
		return reDiffOrgCode;
	}
	public void setReDiffOrgCode(String reDiffOrgCode) {
		this.reDiffOrgCode = reDiffOrgCode;
	}
	public String getReDiffOrgName() {
		return reDiffOrgName;
	}
	public void setReDiffOrgName(String reDiffOrgName) {
		this.reDiffOrgName = reDiffOrgName;
	}
	public String getReDiffMemo() {
		return reDiffMemo;
	}
	public void setReDiffMemo(String reDiffMemo) {
		this.reDiffMemo = reDiffMemo;
	}
	public String getReDiffEmpNo() {
		return reDiffEmpNo;
	}
	public void setReDiffEmpNo(String reDiffEmpNo) {
		this.reDiffEmpNo = reDiffEmpNo;
	}
	public String getReDiffEmpName() {
		return reDiffEmpName;
	}
	public void setReDiffEmpName(String reDiffEmpName) {
		this.reDiffEmpName = reDiffEmpName;
	}
	public Date getReDiffTime() {
		return reDiffTime;
	}
	public void setReDiffTime(Date reDiffTime) {
		this.reDiffTime = reDiffTime;
	}
	
}
