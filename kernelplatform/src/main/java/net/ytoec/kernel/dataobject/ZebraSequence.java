package net.ytoec.kernel.dataobject;

import java.util.Date;

public class ZebraSequence {
	private Integer sequenceId;

	private String customerCode;

	private String parternCode;

	private Date createTime;
	// 本次请求获取的面单总数量
	private int totalCount;
	// 获取的面单总箱数
	private double totalBoxNum;
	// 同步面单状态(0:第一次请求失败，1，第一次请求中，2:第一次请求成功)
	private int state;
	// 更新时间
	private Date updateTime;

	public Integer getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public double getTotalBoxNum() {
		return totalBoxNum;
	}

	public void setTotalBoxNum(double totalBoxNum) {
		this.totalBoxNum = totalBoxNum;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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
