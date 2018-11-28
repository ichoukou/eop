package net.ytoec.kernel.action.bill;

import java.io.Serializable;
import java.util.Date;

public class BillDetailDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String detailId;
	private String billNo;
	private String waybillNo;
	private String collectInTime;
	private String sendTime;
	private String signoffFlg;
	private String signoffTime;
	private String signoffEmpName;
	private Double codMoney;
	private Double codMoneyReal;
	private String customerName;
	private String sendOrgCode;
	private String sendOrgName;
	private String desOrgCode;
	private String desOrgName;
	private String confirmFlg;
	private String customerConfirmStatus;
	private String billCreateTime;
	private String customerConfirmFlg;
	private String diffStatus;
	private String customerPayFlg;
	private String customerPayStatus;
	private String customerPayTime;
	private String strWaybillStatus;
	private String recordTime; //录单时间
	private String diffFlg;//【差异标志，0代表无差异，1代表已上报，2代表已确认，3代表已否决】
	private String chargeType; //收款类型
	private String deliveryOrgCode;
	private String deliveryOrgName;
	
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

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public void setCustomerConfirmStatus(String customerConfirmStatus) {
		this.customerConfirmStatus = customerConfirmStatus;
	}

	public void setDiffStatus(String diffStatus) {
		this.diffStatus = diffStatus;
	}
	public String getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}

	public String getDiffFlg() {
		return diffFlg;
	}

	public void setDiffFlg(String diffFlg) {
		this.diffFlg = diffFlg;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public void setCustomerPayStatus(String customerPayStatus) {
		this.customerPayStatus = customerPayStatus;
	}

	public void setStrWaybillStatus(String strWaybillStatus) {
		this.strWaybillStatus = strWaybillStatus;
	}

	public String getCustomerConfirmStatus() {
		if (null == confirmFlg || "0".equals(confirmFlg)) {
			customerConfirmStatus = "未确认";
		} else {
			customerConfirmStatus = "已确认";
		}
		return customerConfirmStatus;
	}

	public String getCustomerPayStatus() {
		if (null == customerPayFlg || "0".equals(customerPayFlg)) {
			customerPayStatus = "未支付";
		} else {
			customerPayStatus = "已支付";
		}
		return customerPayStatus;
	}

	public String getStrWaybillStatus() {
		if (signoffFlg.equals("0")) {
			strWaybillStatus = "未签收";
		} else {
			strWaybillStatus = "已签收";
		}
		return strWaybillStatus;
	}

	public String getCustomerPayFlg() {
		return customerPayFlg;
	}

	public void setCustomerPayFlg(String customerPayFlg) {
		this.customerPayFlg = customerPayFlg;
	}

	public String getCustomerPayTime() {
		return customerPayTime;
	}

	public void setCustomerPayTime(String customerPayTime) {
		this.customerPayTime = customerPayTime;
	}

	public String getDiffStatus() {
		if ("4".equals(confirmFlg)) {
			diffStatus = "已驳回";
		} else if ("6".equals(confirmFlg)) {
			diffStatus = "处理中";
		} else if ("7".equals(confirmFlg)) {
			diffStatus = "已驳回";
		} else if ("8".equals(confirmFlg)) {
			diffStatus = "已处理";
		} else if ("3".equals(confirmFlg)) {
			diffStatus = "已上报";
		} else {
			diffStatus = "无差异";
		}
		return diffStatus;
	}

	public String getCustomerConfirmFlg() {
		return customerConfirmFlg;
	}

	public void setCustomerConfirmFlg(String customerConfirmFlg) {
		this.customerConfirmFlg = customerConfirmFlg;
	}

	public String getBillCreateTime() {
		return billCreateTime;
	}

	public void setBillCreateTime(String billCreateTime) {
		this.billCreateTime = billCreateTime;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getCollectInTime() {
		return collectInTime;
	}

	public void setCollectInTime(String collectInTime) {
		this.collectInTime = collectInTime;
	}

	public String getSignoffTime() {
		return signoffTime;
	}

	public void setSignoffTime(String signoffTime) {
		this.signoffTime = signoffTime;
	}

	public String getSignoffEmpName() {
		return signoffEmpName;
	}

	public void setSignoffEmpName(String signoffEmpName) {
		this.signoffEmpName = signoffEmpName;
	}

	public Double getCodMoney() {
		return codMoney;
	}

	public void setCodMoney(Double codMoney) {
		this.codMoney = codMoney;
	}

	public Double getCodMoneyReal() {
		return codMoneyReal;
	}

	public void setCodMoneyReal(Double codMoneyReal) {
		this.codMoneyReal = codMoneyReal;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getSendOrgName() {
		return sendOrgName;
	}

	public void setSendOrgName(String sendOrgName) {
		this.sendOrgName = sendOrgName;
	}

	public String getDetailId() {
		return detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}

	public String getSendOrgCode() {
		return sendOrgCode;
	}

	public void setSendOrgCode(String sendOrgCode) {
		this.sendOrgCode = sendOrgCode;
	}

	public String getDesOrgCode() {
		return desOrgCode;
	}

	public void setDesOrgCode(String desOrgCode) {
		this.desOrgCode = desOrgCode;
	}

	public String getDesOrgName() {
		return desOrgName;
	}

	public void setDesOrgName(String desOrgName) {
		this.desOrgName = desOrgName;
	}

	public String getConfirmFlg() {
		return confirmFlg;
	}

	public void setConfirmFlg(String confirmFlg) {
		this.confirmFlg = confirmFlg;
	}

	public String getSignoffFlg() {
		return signoffFlg;
	}

	public void setSignoffFlg(String signoffFlg) {
		this.signoffFlg = signoffFlg;
	}
}
