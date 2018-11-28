package net.ytoec.kernel.dataobject;

import java.util.Date;

/**
 * @author wangyong
 * @2012-02-14
 */
public class Branch {

	private Integer id;

	private String provice;// 省

	private String city;// 市

	private String county;// 区县

	private String companyName;// 公司名称

	private String managerName;// 经理

	private String managerPhone;// 经理电话

	private String fax;// 传真

	private String email;// E-mail

	private String msn;// MSN联系

	private String servicePhone;// 查询电话

	private String questionPhone;// 问题件电话

	private String sendScope;// 派送范围

	private String unSendScope;// 不派送范围

	private String sendTimeLimit;// 派送时限

	private String hoursFor24;// 24小时件

	private String specialService;// 特殊服务

	private String remark;// 备注

	private Date createTime;// 创建时间

	private Date updateTime;// 更新时间

	private Integer branchCode;// 网点用户id
	
	/**
	 * 验证唯一，去除自身
	 */
	private String myCompanyName;// 公司名称
	private String myManagerPhone;// 经理电话
	private String myQuestionPhone;// 问题件电话
	private String myServicePhone;// 查询电话

	public Integer getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(Integer branchCode) {
		this.branchCode = branchCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProvice() {
		return provice;
	}

	public void setProvice(String provice) {
		this.provice = provice;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getManagerPhone() {
		return managerPhone;
	}

	public void setManagerPhone(String managerPhone) {
		this.managerPhone = managerPhone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMsn() {
		return msn;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	public String getServicePhone() {
		return servicePhone;
	}

	public void setServicePhone(String servicePhone) {
		this.servicePhone = servicePhone;
	}

	public String getQuestionPhone() {
		return questionPhone;
	}

	public void setQuestionPhone(String questionPhone) {
		this.questionPhone = questionPhone;
	}

	public String getSendScope() {
		return sendScope;
	}

	public void setSendScope(String sendScope) {
		this.sendScope = sendScope;
	}

	public String getUnSendScope() {
		return unSendScope;
	}

	public void setUnSendScope(String unSendScope) {
		this.unSendScope = unSendScope;
	}

	public String getSendTimeLimit() {
		return sendTimeLimit;
	}

	public void setSendTimeLimit(String sendTimeLimit) {
		this.sendTimeLimit = sendTimeLimit;
	}

	public String getHoursFor24() {
		return hoursFor24;
	}

	public void setHoursFor24(String hoursFor24) {
		this.hoursFor24 = hoursFor24;
	}

	public String getSpecialService() {
		return specialService;
	}

	public void setSpecialService(String specialService) {
		this.specialService = specialService;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getMyCompanyName() {
		return myCompanyName;
	}

	public void setMyCompanyName(String myCompanyName) {
		this.myCompanyName = myCompanyName;
	}

	public String getMyManagerPhone() {
		return myManagerPhone;
	}

	public void setMyManagerPhone(String myManagerPhone) {
		this.myManagerPhone = myManagerPhone;
	}

	public String getMyQuestionPhone() {
		return myQuestionPhone;
	}

	public void setMyQuestionPhone(String myQuestionPhone) {
		this.myQuestionPhone = myQuestionPhone;
	}

	public String getMyServicePhone() {
		return myServicePhone;
	}

	public void setMyServicePhone(String myServicePhone) {
		this.myServicePhone = myServicePhone;
	}
	
}
