/**
 * 2012-4-16下午03:49:45
 * wangyong
 */
package net.ytoec.kernel.dataobject;

import java.util.Date;

/**
 * 消息bean
 * @author wangyong
 * 2012-4-16
 */
public class Message {

	//消息主键id
	private Integer id;
	
	//消息主题
	private String messageTheme;
	
	//发送方式:现阶段默认为1、站内留言/2、邮件/3、短信
	private String sendWay;
	
	//消息类型：1、卖家所发；2、网点所发；3、管理员所发；4、系统消息；5、平台用户所发消息
	private String messageType;
	
	//发件人：网点为id(网点子账取网点主账号的id)，卖家为userCode，管理员为id，平台用户为id
	private String sendUser;
	
	//接收人：网点为id(网点子账取网点主账号的id)，卖家为userCode，管理员为id，平台用户为id
	private String receiveUser;
	
	//发起人用户Id
	private String sendUserId;
	
	//消息内容
	private String messageContent;
	
	//消息状态：1已读/0未读（此字段是连接EC_CORE_MESSAGE_USER表查询的结果，不存在于EC_CORE_MESSAGE表中）
	private Integer messageStatus;
	
	//每条消息回复数目（不存在于EC_CORE_MESSAGE表中）
	private int replyNum;
	
	//消息发起人名称（不存在于EC_CORE_MESSAGE表中）
	private String sendUserName;
	
	private Date sendTime;
	
	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMessageTheme() {
		return messageTheme;
	}

	public void setMessageTheme(String messageTheme) {
		this.messageTheme = messageTheme;
	}

	public String getSendWay() {
		return sendWay;
	}

	public void setSendWay(String sendWay) {
		this.sendWay = sendWay;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getSendUser() {
		return sendUser;
	}

	public void setSendUser(String sendUser) {
		this.sendUser = sendUser;
	}

	public String getReceiveUser() {
		return receiveUser;
	}

	public void setReceiveUser(String receiveUser) {
		this.receiveUser = receiveUser;
	}

	public String getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public Integer getMessageStatus() {
		return messageStatus;
	}

	public int getReplyNum() {
		return replyNum;
	}

	public void setReplyNum(int replyNum) {
		this.replyNum = replyNum;
	}

	public void setMessageStatus(Integer messageStatus) {
		this.messageStatus = messageStatus;
	}

	public String getSendUserName() {
		return sendUserName;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
