package net.ytoec.kernel.dataobject;

import java.util.Date;

/**
 * 
 * 货物信息.
 * 
 * private Integer id;
	private String itemName;
	private Integer itemNumber;
	private String remark;
	private Integer orderId;
	private Date createTime;
	private Date updateTime;
 * 
 */
@Deprecated
public class CommodityInfo {

	private int id;
	private int orderId;
	private String itemName;
	private String number;
	private String remark;
	private Date createTime;
	private Date updateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
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

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
