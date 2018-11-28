package net.ytoec.kernel.dataobject;

import java.util.Date;

/**
 * @作者：罗典
 * @描述：业务并发控制实体
 * @时间：2013-09-16
 * */
public class SyncLock {
	// 主键，自增长
	private String id;
	// 创建时间
	private Date createTime ;
	// 创建人
	private String creator;
	// 最后修改时间
	private Date updatetime;
	// 最后修改人
	private String modifier;
	// 锁所控制的方法名，或唯一标示某个业务场景
	private String method;
	// 锁所控制的并发对象
	private String businessId;
	// 是否已经锁定
	private boolean isLock;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public boolean isLock() {
		return isLock;
	}

	public void setLock(boolean isLock) {
		this.isLock = isLock;
	}
}
