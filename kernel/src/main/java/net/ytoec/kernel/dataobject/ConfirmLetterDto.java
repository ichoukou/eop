package net.ytoec.kernel.dataobject;

import java.io.Serializable;

public class ConfirmLetterDto implements Serializable {
	/**
	 * 未确认差异列表显示类
	 */
	private static final long serialVersionUID = 1L;
	private String waybillNo;
	private String informTime;
	private double initialValue;
	private String informValue;
	public double getInitialValue() {
		return initialValue;
	}
	private String confirmFlg;
	private String confirmStatus;
	private String companyUndoReason;
	private String subcompanyUndoReason;
	public String getWaybillNo() {
		return waybillNo;
	}
	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}
	public String getInformTime() {
		return informTime;
	}
	public void setInformTime(String informTime) {
		this.informTime = informTime;
	}
	public void setConfirmStatus(String confirmStatus) {
		this.confirmStatus = confirmStatus;
	}
	public void setInitialValue(double initialValue) {
		this.initialValue = initialValue;
	}
	public String getInformValue() {
		return informValue;
	}
	public void setInformValue(String informValue) {
		this.informValue = informValue;
	}
	public String getConfirmFlg() {
		return confirmFlg;
	}
	public void setConfirmFlg(String confirmFlg) {
		this.confirmFlg = confirmFlg;
	}
	public String getConfirmStatus() {
		if ("0".equals(confirmFlg)) {
			confirmStatus = "处理中";
		} else if ("1".equals(confirmFlg)) {
			confirmStatus = "已处理";
		} else if ("2".equals(confirmFlg)) {
			confirmStatus = "被驳回";
		} else if ("3".equals(confirmFlg)) {
			confirmStatus = "已上报";
		} else {
			confirmStatus = "被驳回";
		}
		return confirmStatus;
	}
	public String getCompanyUndoReason() {
		return companyUndoReason;
	}
	public void setCompanyUndoReason(String companyUndoReason) {
		this.companyUndoReason = companyUndoReason;
	}
	public String getSubcompanyUndoReason() {
		return subcompanyUndoReason;
	}
	public void setSubcompanyUndoReason(String subcompanyUndoReason) {
		this.subcompanyUndoReason = subcompanyUndoReason;
	}
	
}