package net.ytoec.kernel.dataobject;

import java.util.Date;

public class ZebraPartern {
    private int parternId;

    private String customerCode;

    private String parternCode;

    private Date createTime;

    private Date updateTime;

	public int getParternId() {
		return parternId;
	}

	public void setParternId(int parternId) {
		this.parternId = parternId;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getParternCode() {
		return parternCode;
	}

	public void setParternCode(String parternCode) {
		this.parternCode = parternCode;
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