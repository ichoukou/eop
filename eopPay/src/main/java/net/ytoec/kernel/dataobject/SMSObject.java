package net.ytoec.kernel.dataobject;

import java.io.Serializable;

/**
 * 短信表实体类
 * @author guolongchao
 * 20120808
 */
public class SMSObject implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键自动生成
	 */
	private Integer id;
	
	/**
	 * 短信类型
     * 1 、余额不足提醒 2、交易提醒  3、服务到期提醒 4、短信不足提醒(参考枚举PayEnumConstants类 SERVICE)
	 */
	private String smsType;
	
	
	/**
	 * sequenceID 唯一
	 */
	private Integer sequenceID;
	
	/**
	 * 发送人(关联EC_CORE_USER的id)
	 */
	private Integer userId;
	
	/**
	 *创建时间
	 */
	private java.util.Date createTime;
	
	/**
	 *发送时间
	 */
	private java.util.Date sendTime;
	
	/**
	 *	客户手机接收时间
	 */
	private java.util.Date deliverTime;
	
    /**
     * 发送的手机号
     */
	private String sendMobile;
	
    /**
     * 目的手机号
     */
	private String destMobile;
	
	/**
	 * 此短信实际已多少条发送
	 */
	private Integer pkTotal;
	
    /**
     * 短信发送内容
     */
	private String messageContent;
	
	 /**
     * 发送状态   
     * 88 易通平台发送0 发送成功 1 网关失败 2 发送失败
     */
	private String status;
	
	 /**
     * 消息错误码（默认填写 ""）
     */
	private String errorCode;
	
	/**
	 * 成功短信数数
	 */
	private Integer succTotal;
	
    /**
     * 短信接收者姓名
     */
	private String receiveName;
	
	/**
	 * 成功短信数数
	 */
	private Integer failTotal;
	
	/**
	 * 短信批次编号
	 */
	private Integer smsBatchNum;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSmsType() {
		return smsType;
	}

	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}


	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public java.util.Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(java.util.Date sendTime) {
		this.sendTime = sendTime;
	}

	public java.util.Date getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(java.util.Date deliverTime) {
		this.deliverTime = deliverTime;
	}

	public String getSendMobile() {
		return sendMobile;
	}

	public void setSendMobile(String sendMobile) {
		this.sendMobile = sendMobile;
	}

	public String getDestMobile() {
		return destMobile;
	}

	public void setDestMobile(String destMobile) {
		this.destMobile = destMobile;
	}

	public Integer getPkTotal() {
		if(pkTotal == null){
			return 0;
		}
		return pkTotal;
	}

	public void setPkTotal(Integer pkTotal) {
		this.pkTotal = pkTotal;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public void setSequenceID(Integer sequenceID) {
		this.sequenceID = sequenceID;
	}

	public Integer getSequenceID() {
		return sequenceID;
	}

	public Integer getSuccTotal() {
		if(succTotal == null){
			return 0;
		}
		return succTotal;
	}

	public void setSuccTotal(Integer succTotal) {
		this.succTotal = succTotal;
	}

	public String getReceiveName() {
		return receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getUserId() {
		return userId;
	}

	public Integer getSmsBatchNum() {
		return smsBatchNum;
	}

	public void setSmsBatchNum(Integer smsBatchNum) {
		this.smsBatchNum = smsBatchNum;
	}

	public Integer getFailTotal() {
		if(failTotal == null){
			return 0;
		}
		return failTotal;
	}

	public void setFailTotal(Integer failTotal) {
		this.failTotal = failTotal;
	}
	
	
}
