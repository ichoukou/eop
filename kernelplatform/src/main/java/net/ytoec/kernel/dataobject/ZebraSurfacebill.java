package net.ytoec.kernel.dataobject;

import java.util.Date;

public class ZebraSurfacebill {

	private String waybillNo;

	private String customerCode;

	private Date fbCreatedate;

	private Date fbDownloaddate;

	private String versionNo;

	private Boolean use;

	private Boolean print;

	private Date jgCreateTime;

	private Date thirdCreateTime;

	private Date updateTime;

	private int sequence;

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo == null ? null : waybillNo.trim();
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode == null ? null : customerCode.trim();
	}

	public Date getFbCreatedate() {
		return fbCreatedate;
	}

	public void setFbCreatedate(Date fbCreatedate) {
		this.fbCreatedate = fbCreatedate;
	}

	public Date getFbDownloaddate() {
		return fbDownloaddate;
	}

	public void setFbDownloaddate(Date fbDownloaddate) {
		this.fbDownloaddate = fbDownloaddate;
	}

	public String getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo == null ? null : versionNo.trim();
	}

	public Boolean isUse() {
		return use;
	}

	public void setUse(Boolean use) {
		this.use = use;
	}

	public Boolean isPrint() {
		return print;
	}

	public void setPrint(Boolean print) {
		this.print = print;
	}

	public Date getJgCreateTime() {
		return jgCreateTime;
	}

	public void setJgCreateTime(Date jgCreateTime) {
		this.jgCreateTime = jgCreateTime;
	}

	public Date getThirdCreateTime() {
		return thirdCreateTime;
	}

	public void setThirdCreateTime(Date thirdCreateTime) {
		this.thirdCreateTime = thirdCreateTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

}