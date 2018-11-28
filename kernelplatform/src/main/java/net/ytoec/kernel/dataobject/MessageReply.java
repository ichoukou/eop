/**
 * 2012-4-16下午03:55:00
 * wangyong
 */
package net.ytoec.kernel.dataobject;

import java.util.Date;

/**
 * 消息回复bean
 * @author wangyong
 * 2012-4-16
 */
public class MessageReply {

	private Integer id;
	
	//消息主键id
	private Integer messageId;
	
	//回复人id
	private Integer replyUser;
	
	//回复内容
	private String replyContent;
	
	private Date replyTime;
	
	private Date updateTime;
	
	//不存在于数据库中
	private String replyUserName;
	
	//不存在于数据库中
	private String replyUserPhone;
	
	//不存在于数据库中
	private String replyUserMobile;
	
	//回复类型：表示当期回复属于那种用户回复的（1:网点；2其他），用于页面上颜色区分
	private String replyType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public Integer getReplyUser() {
		return replyUser;
	}

	public void setReplyUser(Integer replyUser) {
		this.replyUser = replyUser;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public Date getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getReplyUserName() {
		return replyUserName;
	}

	public void setReplyUserName(String replyUserName) {
		this.replyUserName = replyUserName;
	}

	public String getReplyUserPhone() {
		return replyUserPhone;
	}

	public void setReplyUserPhone(String replyUserPhone) {
		this.replyUserPhone = replyUserPhone;
	}

	public String getReplyUserMobile() {
		return replyUserMobile;
	}

	public void setReplyUserMobile(String replyUserMobile) {
		this.replyUserMobile = replyUserMobile;
	}

	public String getReplyType() {
		return replyType;
	}

	public void setReplyType(String replyType) {
		this.replyType = replyType;
	}
}
