/**
 * Feb 21, 201210:26:49 AM
 * wangyong
 */
package net.ytoec.kernel.dataobject;

import java.util.Date;

/**
 * 运单走势bean。
 * @author wangyong
 * Feb 21, 2012
 */
public class MailTendency {
	
	private Integer id;
	
	private Integer siteId;//网点id
	
	private Integer userId;//用户id
	
	private Integer mailNoNum;//卖家每日订单数
	
	private String type;//类型
	
	private Date createTime;
	
	private Date updateTime;
	
	private Date mailNoCreateTime;
	
	private String remark;
	
	private Date partitionDate;
	
	private Integer userthreadId;//直客id

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getMailNoNum() {
		return mailNoNum;
	}

	public void setMailNoNum(Integer mailNoNum) {
		this.mailNoNum = mailNoNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public Date getMailNoCreateTime() {
		return mailNoCreateTime;
	}

	public void setMailNoCreateTime(Date mailNoCreateTime) {
		this.mailNoCreateTime = mailNoCreateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getPartitionDate() {
		return partitionDate;
	}

	public void setPartitionDate(Date partitionDate) {
		this.partitionDate = partitionDate;
	}

	public Integer getUserthreadId() {
		return userthreadId;
	}

	public void setUserthreadId(Integer userthreadId) {
		this.userthreadId = userthreadId;
	}

}
