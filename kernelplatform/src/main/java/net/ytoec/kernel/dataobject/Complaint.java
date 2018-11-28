/**
 * net.ytoec.kernel.dataobject
 * Complaint.java
 * 2012-7-10下午02:58:36
 * @author wangyong
 */
package net.ytoec.kernel.dataobject;

import java.util.Date;

/**
 * 投诉实体
 * @author wangyong
 * 2012-7-10
 */
public class Complaint {
	
	private Integer id;
	
	//投诉人
	private String complaintUser;
	
	//网点编码
	private String siteUserCode;
	
	//网点名称
	private String siteUserName;
	
	//运单号
	private String mailNo;
	
	//投诉内容
	private String complaintContent;
	
	//网点和卖家沟通内容
	private String exchangeContent;
	
	//网点最后一次回复时间
	private Date lastTimeForUser;
	
	//卖家最后一次回复时间
	private Date lastTimeForSite;
	
	//投诉时间
	private Date complaintTime;
	
	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getComplaintUser() {
		return complaintUser;
	}

	public void setComplaintUser(String complaintUser) {
		this.complaintUser = complaintUser;
	}

	public String getSiteUserCode() {
		return siteUserCode;
	}

	public void setSiteUserCode(String siteUserCode) {
		this.siteUserCode = siteUserCode;
	}

	public String getSiteUserName() {
		return siteUserName;
	}

	public void setSiteUserName(String siteUserName) {
		this.siteUserName = siteUserName;
	}

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public String getComplaintContent() {
		return complaintContent;
	}

	public void setComplaintContent(String complaintContent) {
		this.complaintContent = complaintContent;
	}

	public String getExchangeContent() {
		return exchangeContent;
	}

	public void setExchangeContent(String exchangeContent) {
		this.exchangeContent = exchangeContent;
	}

	public Date getLastTimeForUser() {
		return lastTimeForUser;
	}

	public void setLastTimeForUser(Date lastTimeForUser) {
		this.lastTimeForUser = lastTimeForUser;
	}

	public Date getLastTimeForSite() {
		return lastTimeForSite;
	}

	public void setLastTimeForSite(Date lastTimeForSite) {
		this.lastTimeForSite = lastTimeForSite;
	}

	public Date getComplaintTime() {
		return complaintTime;
	}

	public void setComplaintTime(Date complaintTime) {
		this.complaintTime = complaintTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	

}
