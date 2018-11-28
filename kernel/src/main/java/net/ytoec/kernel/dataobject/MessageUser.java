/**
 * 2012-4-16下午03:58:14
 * wangyong
 */
package net.ytoec.kernel.dataobject;

import java.util.Date;

/**
 * 消息和用户关联bean:一条消息创建时添加消息和消息对应的接收方id入库，当用户删除时只删除该用户对应Id的记录。
 * 消息的标记是否已读也放在此处
 * @author wangyong
 * 2012-4-16
 */
public class MessageUser {

	private Integer id;
	
	//消息id
	private Integer messageId;
	
	//用户id
	private Integer userId;
	
	//消息读取状态：1已读/0未读
	private Integer messageStatus;
	
	private Date createTime;
	
	private Date updateTime;

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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getMessageStatus() {
		return messageStatus;
	}

	public void setMessageStatus(Integer messageStatus) {
		this.messageStatus = messageStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}
