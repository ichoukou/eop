/**
 * 
 */
package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Wangyong
 * 产品信息表实体类
 */
public class Product implements Serializable {

	private static final long serialVersionUID = -7970848646314840509L;
	
	private Integer id;
	private String itemName;
	private Integer itemNumber;
	private String remark;
	private Integer orderId;
	private String logisticId;
	private String mailNo;
	private Date createTime;
	private Date updateTime;
	private double itemValue;
	
	private Date partitionDate;
	
	public Date getPartitionDate() {
		return partitionDate;
	}
	public void setPartitionDate(Date partitionDate) {
		this.partitionDate = partitionDate;
	}
	public double getItemValue() {
		return itemValue;
	}
	public void setItemValue(double itemValue) {
		this.itemValue = itemValue;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Integer getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(Integer itemNumber) {
		this.itemNumber = itemNumber;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
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
	public String getLogisticId() {
		return logisticId;
	}
	public void setLogisticId(String logisticId) {
		this.logisticId = logisticId;
	}
	public String getMailNo() {
		return mailNo;
	}
	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}
	
}
