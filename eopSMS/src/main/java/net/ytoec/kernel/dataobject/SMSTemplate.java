package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 短信模板表(针对短信类型)
 * @author Administrator
 *
 */
public class SMSTemplate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5499944107075260737L;
	
	private int id;               // 模版主键id
	
	private int smsTypeId;			//短信类型ID     
	
	private String name;       		//模版标题     	
	
	private String content;     	//模板内容
	
	private String isDefault;       //是否是默认模板 Y 是 N 否(默认为N，当用户第一次为此短信类型创建模版时设置为Y)
	
	private String status;          //Y 审核通过 N 未通过, M 审核中, D已删除
	
	private int sendCount;       	//通过此模版发送了多少条短信
	
	private int auditUser;			//审核人
	
	private Date auditTime;       	//审核时间
	
	private int userId;           	//主帐号的ID
	
	private int updateUserId;    	//修改人
	
	private Date updateTime;      	//修改时间
	
	private Date createTime;      	//创建时间即提交审核时间
	
	private int  createUserId;    	//创建模版的用户ID
	
	private String remark;			//存放管理员审核失败的条件
	
	private String shopName;        //店铺名称
	
	private String serviceName;     //短信模版名称
	
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getSmsTypeId() {
		return smsTypeId;
	}
	public void setSmsTypeId(int smsTypeId) {
		this.smsTypeId = smsTypeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getSendCount() {
		return sendCount;
	}
	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}
	public int getAuditUser() {  
		return auditUser;
	}
	public void setAuditUser(int auditUser) {
		this.auditUser = auditUser;
	}
	public Date getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(int updateUserId) {
		this.updateUserId = updateUserId;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
