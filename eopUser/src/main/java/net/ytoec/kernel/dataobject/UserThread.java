/**
 * UserThread.java
 * 2011-10-31 下午01:45:02
 * wangyong
 */
package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.Date;

/**
 * 从金刚系统同步过来的用户信息。
 * 
 * @author wangyong
 */
public class UserThread implements Serializable {

	/**
	 * UserThread.java Wangyong 2011-10-31 下午01:45:22 2011-8-18
	 */
	private static final long serialVersionUID = -7970848646314840509L;

	private Integer id;
	// 用户编码
	private String userCode;
	// 用户所属网点
	private String siteCode;
	// 通知类型（CREATE：新增直客；UPDATE：变更直客状态）
	private String type;
	// 直客状态
	private String status;
	// 是否启用，两种状态：启用：Y 禁用：N
	private String used;
	// 用户名
	private String userName;
	// 用户状态
	private String userState;
	// 创建时间
	private Date createTime;

	// 备用字段
	private String backup1;
	private String backup2;
	private String backup3;
	// 是否关闭电子对账功能(1:关闭；0：开启)
	private String switchEccount;
	/** 修改用户编码的时间戳 */
	private Date userCodeUpteTime;
	// 是否被承包出去，两种状态：被承包：true  未被承包：false
	private String contractStatus;
	// 客户是否已经成为运单模板的状态：已经成为：true 未成为 ：false
	private String mailNoStatus;
	//编辑用户的名称
	private String editUserName;
	//判断客户是否可以继续被分配 ：  可继续分配：yes  不可继续分配： no
	private String isAgain;
	
	// 是否允许网点或承包区帮忙打印面单
	private String isPrint;

	/** 
	 * 实现面单下载授权功能添加
	 * 2013-10-22 add BY wangpengfei 
	 */
	//是否具有面单下载权限(0:未开通，1:已开通，2：已关闭)
	private int isCanDownload; 
	//授权操作时间
	private Date operateDate;
	
	
	/**
	 * 是否允许网点或承包区帮忙打印面单
	 * @return
	 */
	public String getIsPrint() {
		return isPrint;
	}

	public void setIsPrint(String isPrint) {
		this.isPrint = isPrint;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserState() {
		return userState;
	}

	public void setUserState(String userState) {
		this.userState = userState;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBackup1() {
		return backup1;
	}

	public void setBackup1(String backup1) {
		this.backup1 = backup1;
	}

	public String getBackup2() {
		return backup2;
	}

	public void setBackup2(String backup2) {
		this.backup2 = backup2;
	}

	public String getBackup3() {
		return backup3;
	}

	public void setBackup3(String backup3) {
		this.backup3 = backup3;
	}

	public String getSwitchEccount() {
		return switchEccount;
	}

	public void setSwitchEccount(String switchEccount) {
		this.switchEccount = switchEccount;
	}

	public Date getUserCodeUpteTime() {
		return userCodeUpteTime;
	}

	public void setUserCodeUpteTime(Date userCodeUpteTime) {
		this.userCodeUpteTime = userCodeUpteTime;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setUsed(String used) {
		this.used = used;
	}

	public String getUsed() {
		return used;
	}

	public String getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}

	public String getMailNoStatus() {
		return mailNoStatus;
	}

	public void setMailNoStatus(String mailNoStatus) {
		this.mailNoStatus = mailNoStatus;
	}

	public String getEditUserName() {
		return editUserName;
	}

	public void setEditUserName(String editUserName) {
		this.editUserName = editUserName;
	}

	public String getIsAgain() {
		return isAgain;
	}

	public void setIsAgain(String isAgain) {
		this.isAgain = isAgain;
	}

	public int getIsCanDownload() {
		return isCanDownload;
	}

	public void setIsCanDownload(int isCanDownload) {
		this.isCanDownload = isCanDownload;
	}

	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

}
