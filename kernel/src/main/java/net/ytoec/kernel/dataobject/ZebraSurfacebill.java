package net.ytoec.kernel.dataobject;

import java.util.Date;

public class ZebraSurfacebill {
	private String waybillNo;

	private String orderNo;

	private String currentUserNo;

	private Date fbCreatedate;

	private Date fbUpdatedate;

	private Date fbDownloaddate;

	private Date fbUploaddate;

	private Date fbPrintdate;

	private Integer fbsendStatus;

	private Integer fbsendTimes;

	private String fbsendDescribe;

	private String versionNo;

	private Boolean use;

	private Boolean print;

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo == null ? null : waybillNo.trim();
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo == null ? null : orderNo.trim();
	}

	public String getCurrentUserNo() {
		return currentUserNo;
	}

	public void setCurrentUserNo(String currentUserNo) {
		this.currentUserNo = currentUserNo == null ? null : currentUserNo
				.trim();
	}

	public Date getFbCreatedate() {
		return fbCreatedate;
	}

	public void setFbCreatedate(Date fbCreatedate) {
		this.fbCreatedate = fbCreatedate;
	}

	public Date getFbUpdatedate() {
		return fbUpdatedate;
	}

	public void setFbUpdatedate(Date fbUpdatedate) {
		this.fbUpdatedate = fbUpdatedate;
	}

	public Date getFbDownloaddate() {
		return fbDownloaddate;
	}

	public void setFbDownloaddate(Date fbDownloaddate) {
		this.fbDownloaddate = fbDownloaddate;
	}

	public Date getFbUploaddate() {
		return fbUploaddate;
	}

	public void setFbUploaddate(Date fbUploaddate) {
		this.fbUploaddate = fbUploaddate;
	}

	public Date getFbPrintdate() {
		return fbPrintdate;
	}

	public void setFbPrintdate(Date fbPrintdate) {
		this.fbPrintdate = fbPrintdate;
	}

	public Integer getFbsendStatus() {
		return fbsendStatus;
	}

	public void setFbsendStatus(Integer fbsendStatus) {
		this.fbsendStatus = fbsendStatus;
	}

	public Integer getFbsendTimes() {
		return fbsendTimes;
	}

	public void setFbsendTimes(Integer fbsendTimes) {
		this.fbsendTimes = fbsendTimes;
	}

	public String getFbsendDescribe() {
		return fbsendDescribe;
	}

	public void setFbsendDescribe(String fbsendDescribe) {
		this.fbsendDescribe = fbsendDescribe == null ? null : fbsendDescribe
				.trim();
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

}