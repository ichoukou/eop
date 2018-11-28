package net.ytoec.kernel.dataobject;

import java.util.Date;

/**
 * @作者：罗典
 * @时间：2013-10-23
 * @描述：查询面单拉取日志记录sequence条件
 * */
public class QuerySequenceCondition {
	// 序列批次号
	private Integer sequence;
	// 商家编码
	private String customerCode;
	// 查询起始时间
	private Date startDate;
	// 查询结束时间
	private Date endDate;
	// 每页起始行数（行数= (起始页数-1)*每页记录数）
	private Integer startLine;
	// 每页记录数
	private Integer limit;

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getStartLine() {
		return startLine;
	}

	public void setStartLine(Integer startLine) {
		this.startLine = startLine;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}
}
