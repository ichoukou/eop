package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.Date;

/**
 * 营销定制服务所对应的买家(会员管理中的)
 * @author shitianzeng
 * 2012-07-05
 */
public class SMSServiceBuyers implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 34769744632342748L;

	private Integer id;
	
	private Integer smsMarketId;	//营销活动的ID
	
	private Integer buyId;	//营销定制所对应的买家ID
	
	private Date createTime;	//创建时间
	
	private Integer createUserId;	//创建人

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSmsMarketId() {
		return smsMarketId;
	}

	public void setSmsMarketId(Integer smsMarketId) {
		this.smsMarketId = smsMarketId;
	}

	public Integer getBuyId() {
		return buyId;
	}

	public void setBuyId(Integer buyId) {
		this.buyId = buyId;
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
