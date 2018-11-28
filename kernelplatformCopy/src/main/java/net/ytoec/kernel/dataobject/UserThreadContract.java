package net.ytoec.kernel.dataobject;

public class UserThreadContract {
	/** 主键'*/
	private Integer id;
	/**网点id*/
	private Integer siteId;
	
	/**承包区id*/
	private Integer conractAreaId;
	/**创建时间*/
	private String createTime;
	/**更新时间*/
	private String updateTime;
	
	/**新增用户的名称（数据库唯一）*/
	
	private String addUserName;
	private String accountType;
	/** 用户所属网点,保存网点的编码 */
	private String site;
	
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

	public Integer getConractAreaId() {
		return conractAreaId;
	}
	public void setConractAreaId(Integer conractAreaId) {
		this.conractAreaId = conractAreaId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getAddUserName() {
		return addUserName;
	}
	public void setAddUserName(String addUserName) {
		this.addUserName = addUserName;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	
}
