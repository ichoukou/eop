package net.ytoec.kernel.dataobject;

import java.util.Date;


public class StatsMessage {

	private int id; 
	private String statsType;
	private String statsReason;
	private Date createTime;
	private Date updateTime;
	private String remark;
	private Date loginTime;
	private Date logoutTime;
	private int userId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStatsType() {
		return statsType;
	}
	public void setStatsType(String statsType) {
		this.statsType = statsType;
	}
	public String getStatsReason() {
		return statsReason;
	}
	public void setStatsReason(String statsReason) {
		this.statsReason = statsReason;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	public Date getLogoutTime() {
		return logoutTime;
	}
	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	} 

	
}
