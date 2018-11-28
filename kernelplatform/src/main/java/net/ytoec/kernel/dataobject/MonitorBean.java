/**
 * MonitorBean.java
 * Wangyong
 * 2011-8-8 下午05:41:08
 */
package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.Date;

import net.ytoec.kernel.action.remote.xml.StepInfo;

/**
 * 买家对运单监控管理。通过从问题单提取运单号、目的地、状态、寄件时间、客服信息等信息。<br>
 * 根据运单号查询订单表id,再通过点单表id查询交易信息表，从中获取买家电话信息。
 * @author Wangyong
 * @2011-8-8
 * net.ytoec.kernel.dataobject
 */
public class MonitorBean implements Serializable {
	
	private static final long serialVersionUID = 5314495939524421370L;
	
	//id标识
	private Integer id;
	
	private String mailNO;
	private String destination;
	//买家姓名
	private String userName;
	//买家电话
	private String phone;
	//处理状态:PD10:未处理;PD20:处理中,PD30:处理完成,PD40:取消
	private String dealStatus;
	//寄件时间
	private Date senderTime;
	//预计到达时间
	private Date arriveTime;
	//是否超时:1、未超时，2、已超时
	private Integer dateOut;
	//反馈客户信息
	private String feedbackInfo;
	//acceptTime
	private Date acceptTime;
	//最近的一条物流信息
	private StepInfo stepInfo;
	//是否加入关注，1表示是，2表示否
	private int isAttention;
	//0：线上下单    1：线下下单
	private String lineType;
	
	/**
	 * 订单创建(0)、更新(UPDATE)、取消(WITHDRAW)、接单(ACCEPT)、 不接单(UNACCEPT)、揽收成功(GOT)、揽收失败(NOT_SEND)、失败(FAILED)、
	 * 送达(SIGNED)、派件扫描(SENT_SCAN)
	 */
	private String status;
	//店铺名称
	private String shopName;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMailNO() {
		return mailNO;
	}
	public void setMailNO(String mailNO) {
		this.mailNO = mailNO;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDealStatus() {
		return dealStatus;
	}
	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}
	public Date getSenderTime() {
		return senderTime;
	}
	public void setSenderTime(Date senderTime) {
		this.senderTime = senderTime;
	}
	public Date getArriveTime() {
		return arriveTime;
	}
	public void setArriveTime(Date arriveTime) {
		this.arriveTime = arriveTime;
	}
	public Integer getDateOut() {
		return dateOut;
	}
	public void setDateOut(Integer dateOut) {
		this.dateOut = dateOut;
	}
	public String getFeedbackInfo() {
		return feedbackInfo;
	}
	public void setFeedbackInfo(String feedbackInfo) {
		this.feedbackInfo = feedbackInfo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getAcceptTime() {
		return acceptTime;
	}
	public void setAcceptTime(Date acceptTime) {
		this.acceptTime = acceptTime;
	}
    
    public StepInfo getStepInfo() {
        return stepInfo;
    }
    
    public void setStepInfo(StepInfo stepInfo) {
        this.stepInfo = stepInfo;
    }
    
    public int getIsAttention() {
        return isAttention;
    }
    
    public void setIsAttention(int isAttention) {
        this.isAttention = isAttention;
    }
	public String getLineType() {
		return lineType;
	}
	public void setLineType(String lineType) {
		this.lineType = lineType;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

}
