/**
 * JgWaybill.java
 * Wangyong
 * 2011-8-18 上午10:08:34
 */
package net.ytoec.kernel.dataobject;

import java.io.Serializable;

/**
 * 金刚运单接口信息数据对象
 */
public class JgWaybillUpdate implements Serializable {

	private static final long serialVersionUID = -7970848646314840509L;
   
	private Integer id;
	private String mailNo;
	private String logisticId;
	private String clientID;
	
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
	public String getLogisticId() {
		return logisticId;
	}
	public void setLogisticId(String logisticId) {
		this.logisticId = logisticId;
	}
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	
	
}
