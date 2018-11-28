package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.Date;

/**
 * 短信的详细信息
 * @author shitianzeng
 * 2012-07-05
 */
public class SMSInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2304807143760691952L;

	private Integer id;
	
	private Integer smsTypeId;	//服务ID
	
	private Integer smsTemplateId;	//模版ID
	
	private String serviceType; // 服务类型      发货提醒(GOT)，派件提醒(SENT_SCAN)，签收提醒(SIGNED)
	
	private String mailNo;	//运单号
	
	private String buyName;	//买家姓名
	
	private String buyLoginName;	//登录名称
	
	private String buyMobile;	//买家手机号码
	
	private Date sendTime;	//发送时间
	
	private String content;	//短信内容
	
	private Integer count; //此内容已多少条短信发送
	
	private String status;	//短信状态 0 发送成功 1 等待发送 2 发送失败 3发送失败,再次发送中
	
	private String errorSend; //短信发送失败原因
	
	private Integer userId;	//主帐号的ID
	
	private Date createTime;	//创建时间
	
	private Integer createUserId;	//创建人
	
	//0 表示未删除 1表示删除 （逻辑删除）
	private Integer isDel; //是否删除

	
	public String getErrorSend() {
		return errorSend;
	}

	public void setErrorSend(String errorSend) {
		this.errorSend = errorSend;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

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

	public Integer getSmsTemplateId() {
		return smsTemplateId;
	}

	public void setSmsTemplateId(Integer smsTemplateId) {
		this.smsTemplateId = smsTemplateId;
	}

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public String getBuyName() {
		return buyName;
	}

	public void setBuyName(String buyName) {
		this.buyName = buyName;
	}

	public String getBuyLoginName() {
		return buyLoginName;
	}

	public void setBuyLoginName(String buyLoginName) {
		this.buyLoginName = buyLoginName;
	}

	public String getBuyMobile() {
		return buyMobile;
	}

	public void setBuyMobile(String buyMobile) {
		this.buyMobile = buyMobile;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}
	
}
