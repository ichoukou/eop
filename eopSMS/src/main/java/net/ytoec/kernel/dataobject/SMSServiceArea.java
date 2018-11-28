package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.Date;

/**
 * 服务所对应的地区(地区包括:省和直辖市)
 * @author shitianzeng
 * 2012-07-05
 */
public class SMSServiceArea implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1845864059766341929L;

	private Integer id;
	
	private Integer smsTypeId;	//短信类型ID
	
	private String code;	//对应编码
	
	private Date updateTime;	//修改时间
	
	private Integer updateUserId;	//修改人
	
	private Date createTime;	//创建时间
	
	private Integer createUserId;	//创建人

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSmsTypeId() {
		return smsTypeId;
	}

	public void setSmsTypeId(Integer smsTypeId) {
		this.smsTypeId = smsTypeId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Integer updateUserId) {
		this.updateUserId = updateUserId;
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
