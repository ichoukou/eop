package net.ytoec.kernel.dataobject;

import java.util.Date;

public class ZebraSequence {
	private Integer sequenceId;

	private String customerCode;

	private String parternCode;

	private Date createTime;

	public Integer getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode == null ? null : customerCode.trim();
	}

	public String getParternCode() {
		return parternCode;
	}

	public void setParternCode(String parternCode) {
		this.parternCode = parternCode == null ? null : parternCode.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}