package net.ytoec.kernel.dataobject;

import java.util.Date;

public class QueryUserCondition {

	//是否具有权限（ 0：未开通 ； 1：已开通；2：已关闭）
	private int isCanDownload; 
	//客户编码
	private String customerCode;
	//客户名称
	private String customerName;
	//创建开始日期
	private Date startDate;
	//创建结束日期
	private Date endDate;
	//网点编码
	private String siteCode;
	// 每页起始行数（行数= (起始页数-1)*每页记录数）
	private Integer startLine;
	// 每页记录数
	private Integer limit;
	
	public QueryUserCondition() {
	}

	public QueryUserCondition(int isCanDownload, String customerCode,
			String customerName, Date startDate, Date endDate, String siteCode,
			Integer startLine, Integer limit) {
		this.isCanDownload = isCanDownload;
		this.customerCode = customerCode;
		this.customerName = customerName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.siteCode = siteCode;
		this.startLine = startLine;
		this.limit = limit;
	}

	public int getIsCanDownload() {
		return isCanDownload;
	}

	public void setIsCanDownload(int isCanDownload) {
		this.isCanDownload = isCanDownload;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
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
