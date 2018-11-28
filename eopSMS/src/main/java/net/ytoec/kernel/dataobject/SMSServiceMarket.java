package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.Date;

/**
 * 营销活动表
 * @author shitianzeng
 * 2012-07-05
 */
public class SMSServiceMarket implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -718251484035789574L;

	private Integer id;
	
	private String name;	//营销活动名称（此字段预留）
	
	private Integer userId;	//主帐号的ID
	
	private Date createTime;	//创建时间
	
	private Integer createUserId;	//创建服务的用户ID

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}
	
}
