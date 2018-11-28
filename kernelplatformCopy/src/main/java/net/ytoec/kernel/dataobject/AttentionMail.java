/**
 * AttentionMail.java
 * 2011 2011-12-13 下午02:36:31
 * wangyong
 */
package net.ytoec.kernel.dataobject;

import java.util.Date;


/**
 * 关注中运单对象
 * @author wangyong
 * TODO
 */
public class AttentionMail {

    private Integer id;
    //运单号
    private String mailNo;
    //目的地
    private String destination;
    //买家姓名
    private String buyerName;
    //买家电话
    private String buyerPhone;
    //订单创建(0)、更新(UPDATE)、取消(WITHDRAW)、接单(ACCEPT)、 不接单(UNACCEPT)、揽收成功(GOT)、揽收失败(NOT_SEND)、失败(FAILED)、送达(SIGNED)、派件扫描(SENT_SCAN)
    private String status;
    //客户编码
    private String customerId;
    //运单发货时间
    private Date sendTime;
    //运单流转时间（比如运单创建、揽收、送达时间等等）
    private Date acceptTime;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //运单预计到达时间
    private Date arriveTime;
    //是否超时(1：未超时，2：已超时)
    private Integer dateOut;
    //备用字段
    private String backup;
    //0：线上下单    1：线下下单
	private String lineType;
	//关注内容
	private String attentionContent;
    
	
    public String getAttentionContent() {
		return attentionContent;
	}

	public void setAttentionContent(String attentionContent) {
		this.attentionContent = attentionContent;
	}

	public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getMailNo() {
        return mailNo;
    }
    
    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }
    
    public String getDestination() {
        return destination;
    }
    
    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    public String getBuyerName() {
        return buyerName;
    }
    
    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }
    
    public String getBuyerPhone() {
        return buyerPhone;
    }
    
    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Date getSendTime() {
        return sendTime;
    }
    
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
    
    public Date getAcceptTime() {
        return acceptTime;
    }
    
    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
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
    
    public String getBackup() {
        return backup;
    }
    
    public void setBackup(String backup) {
        this.backup = backup;
    }

    
    public String getCustomerId() {
        return customerId;
    }

    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

	public String getLineType() {
		return lineType;
	}

	public void setLineType(String lineType) {
		this.lineType = lineType;
	}
    
}
