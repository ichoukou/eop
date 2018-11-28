package net.ytoec.kernel.dataobject;

import java.io.Serializable;

/**
 * 短信模块url配置表
 * @author guolongchao
 * 20120808
 */
public class SMSConfigURL implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键自动生成
	 */
	private Integer id;
	
	/**
	 * 5 短信模块
	 */
	private String type;
	
	/**
	 * 创建人
	 */
	private Integer userId;
	
	/**
	 *创建时间
	 */
	private java.util.Date createTime;
	
	/**
	 * 其它模块返回的URL
	 */
	private String confUrl;

	
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

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setConfUrl(String confUrl) {
		this.confUrl = confUrl;
	}

	public String getConfUrl() {
		return confUrl;
	}
}
