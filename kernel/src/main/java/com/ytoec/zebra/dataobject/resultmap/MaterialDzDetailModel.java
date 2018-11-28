package com.ytoec.zebra.dataobject.resultmap;

import java.util.Date;
/**
 * 物料拆分明细表
 * @author zhangcong 
 * @version 1.0 2013-03-13
 *
 */
public class MaterialDzDetailModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7180526817467365047L;

	private String waybillNo;// 运单号码
	private String waybillName;// 运单名称
	private Date waybillExpireTime;//运单过期时间
	private String createUserCode;// 创建用户编码
	private String createUserName;// 创建用户名称
	private Date createTime;// 创建时间
	private String updateUserCode;// 更新用户编码
	private String updateUserName;// 更新用户名称
	private Date updateTime;// 更新时间
	private String uploadUserCode;// 上传用户编码
	private String uploadUserName;// 上传用户名称
	private Date uploadTime;// 上传时间
	private String uploadNo;// 上传订单编码
	private Integer uploadSendTime;// 上传次数
	private Integer uploadIsPrint;// 是否打印
	private Date uploadIsPrintTime;//上传打印时间
	private String downloadUserCode;// 下载用户编码
	private String downloadUserName;// 下载用户名称
	private Date downloadTime;// 下载时间
	private Integer downloadStatus;// 下载状态（可下载：0 不可下载：1）
	private Integer status;// 运单状态（正常：0 非正常：1）
	private Long versionNo;// 版本号

	/* 无参构造器 */
	public MaterialDzDetailModel() {
		super();
	}

	/* 有参构造器 */
	public MaterialDzDetailModel(String waybillNo, String waybillName,Date waybillExpireTime,
			String createUserCode, String createUserName, Date createTime,
			String updateUserCode, String updateUserName, Date updateTime,
			String uploadUserCode, String uploadUserName, Date uploadTime,
			String uploadNo, Integer uploadSendTime, Integer uploadIsPrint,
			Date uploadIsPrintTime,	String downloadUserCode, String downloadUserName,
			Date downloadTime, Integer downloadStatus, Integer status,
			Long versionNo) {
		super();
		this.waybillNo = waybillNo;
		this.waybillName = waybillName;
		this.waybillExpireTime = waybillExpireTime;
		this.createUserCode = createUserCode;
		this.createUserName = createUserName;
		this.createTime = createTime;
		this.updateUserCode = updateUserCode;
		this.updateUserName = updateUserName;
		this.updateTime = updateTime;
		this.uploadUserCode = uploadUserCode;
		this.uploadUserName = uploadUserName;
		this.uploadTime = uploadTime;
		this.uploadNo = uploadNo;
		this.uploadSendTime = uploadSendTime;
		this.uploadIsPrint = uploadIsPrint;
		this.uploadIsPrintTime = uploadIsPrintTime;
		this.downloadUserCode = downloadUserCode;
		this.downloadUserName = downloadUserName;
		this.downloadTime = downloadTime;
		this.downloadStatus = downloadStatus;
		this.status = status;
		this.versionNo = versionNo;
	}

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getWaybillName() {
		return waybillName;
	}

	public void setWaybillName(String waybillName) {
		this.waybillName = waybillName;
	}

	public Date getWaybillExpireTime() {
		return waybillExpireTime;
	}

	public void setWaybillExpireTime(Date waybillExpireTime) {
		this.waybillExpireTime = waybillExpireTime;
	}

	public String getCreateUserCode() {
		return createUserCode;
	}

	public void setCreateUserCode(String createUserCode) {
		this.createUserCode = createUserCode;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUserCode() {
		return updateUserCode;
	}

	public void setUpdateUserCode(String updateUserCode) {
		this.updateUserCode = updateUserCode;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUploadUserCode() {
		return uploadUserCode;
	}

	public void setUploadUserCode(String uploadUserCode) {
		this.uploadUserCode = uploadUserCode;
	}

	public String getUploadUserName() {
		return uploadUserName;
	}

	public void setUploadUserName(String uploadUserName) {
		this.uploadUserName = uploadUserName;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getUploadNo() {
		return uploadNo;
	}

	public void setUploadNo(String uploadNo) {
		this.uploadNo = uploadNo;
	}

	public Integer getUploadSendTime() {
		return uploadSendTime;
	}

	public void setUploadSendTime(Integer uploadSendTime) {
		this.uploadSendTime = uploadSendTime;
	}

	public Integer getUploadIsPrint() {
		return uploadIsPrint;
	}

	public void setUploadIsPrint(Integer uploadIsPrint) {
		this.uploadIsPrint = uploadIsPrint;
	}

	public Date getUploadIsPrintTime() {
		return uploadIsPrintTime;
	}

	public void setUploadIsPrintTime(Date uploadIsPrintTime) {
		this.uploadIsPrintTime = uploadIsPrintTime;
	}

	public String getDownloadUserCode() {
		return downloadUserCode;
	}

	public void setDownloadUserCode(String downloadUserCode) {
		this.downloadUserCode = downloadUserCode;
	}

	public String getDownloadUserName() {
		return downloadUserName;
	}

	public void setDownloadUserName(String downloadUserName) {
		this.downloadUserName = downloadUserName;
	}

	public Date getDownloadTime() {
		return downloadTime;
	}

	public void setDownloadTime(Date downloadTime) {
		this.downloadTime = downloadTime;
	}

	public Integer getDownloadStatus() {
		return downloadStatus;
	}

	public void setDownloadStatus(Integer downloadStatus) {
		this.downloadStatus = downloadStatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Long versionNo) {
		this.versionNo = versionNo;
	}

	
}
