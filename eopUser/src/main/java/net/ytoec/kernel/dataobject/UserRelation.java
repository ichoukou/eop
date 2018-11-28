/**
 * net.ytoec.kernel.dataobject
 * UserRelation.java
 * 2012-9-25下午04:46:57
 * @author wangyong
 */
package net.ytoec.kernel.dataobject;

import java.util.Date;

/**
 * 用户关联关系对象实体
 * @author wangyong
 * 2012-9-25
 */
public class UserRelation {

	private Integer id;
	
	private Integer userId;
	
	private Integer relatedUserId;
	
	private Date createTime;
	
	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRelatedUserId() {
		return relatedUserId;
	}

	public void setRelatedUserId(Integer relatedUserId) {
		this.relatedUserId = relatedUserId;
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
}
