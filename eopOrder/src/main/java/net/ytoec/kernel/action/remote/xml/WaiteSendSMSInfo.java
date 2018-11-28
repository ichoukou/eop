package net.ytoec.kernel.action.remote.xml;

import java.util.Date;

/**
 * 待发送短信的数据实体类
 * create by wangmindong
 * create timer 2013-04-08
 *
 */
public class WaiteSendSMSInfo {

	private String id;
	/**
	 * 运单号
	 */
	private String mailNo;
	/**
	 * 物流号
	 */
	private String txLogisticID;
	/**
	 * 发送状态 GOT  NOT_SEND SIGNED
	 */
	private String status;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 标识
	 */
	private Integer flag;
	/**
	 * 日期
	 */
	private Date partitiondate;
	
	
	public Date getPartitiondate() {
		return partitiondate;
	}
	public void setPartitiondate(Date partitiondate) {
		this.partitiondate = partitiondate;
	}
	public String getMailNo() {
		return mailNo;
	}
	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}
	public String getTxLogisticID() {
		return txLogisticID;
	}
	public void setTxLogisticID(String txLogisticID) {
		this.txLogisticID = txLogisticID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	
}
