package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.Date;

/**
 * 短信管理的子服务对应的模版为默认模版
 * @author Administrator
 *
 */
public class SMSService implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5875730061435283363L;
	
	private int id;            
	
	private String name; 			//服务名称 比如：发货提醒、到货提醒、签收提醒、营销定制、问题件通知         
	
	private String imageUrl;     	//服务对应的图片
	
	private String introduction;  	//服务简介
	
	private String sendTimeType;	//此服务发送短信时间:例如：8：00--20：00之间为1,其他以此类推
	
	private String isOn;         	//标志此服务是否开启，Y已开启、 N为未开启，默认为N',  问题件默认为开启，其他为未开启
	
	private String isAutoSend;  	//是否自动发送
	
	private int userId;        		//主帐号的ID
	
	private int updateUserId; 		//修改人
	
	private Date updateTime;   		//修改时间
	
	private Date createTime;   		//创建时间
	
	private int createUserId;		//创建服务的用户ID'
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public String getIntroduction() {
		return introduction;
	}
	
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	public String getSendTimeType() {
		return sendTimeType;
	}
	
	public void setSendTimeType(String sendTimeType) {
		this.sendTimeType = sendTimeType;
	}
	
	public String getIsOn() {
		return isOn;
	}
	
	public void setIsOn(String isOn) {
		this.isOn = isOn;
	}
	
	public String getIsAutoSend() {
		return isAutoSend;
	}
	
	public void setIsAutoSend(String isAutoSend) {
		this.isAutoSend = isAutoSend;
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

}
