package net.ytoec.kernel.search.dataobject;

import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;

/**
 * @author wangyong
 * @2012-02-14
 */
public class BranchItem {

	@Field("id")
	private Integer id;
	
	@Field("provice")
	private String provice;//省
	
	@Field("city")
	private String city;//市
	
	@Field("county")
	private String county;//区县
	
	@Field("companyName")
	private String companyName;//公司名称
	
	@Field("managerName")
	private String managerName;//经理
	
	@Field("managerPhone")
	private String managerPhone;//经理电话
	
	@Field("fax")
	private String fax;//传真
	
	@Field("email")
	private String email;//E-mail
	
	@Field("msn")
	private String msn;//MSN联系
	
	@Field("servicePhone")
	private String servicePhone;//查询电话
	
	@Field("questionPhone")
	private String questionPhone;//问题件电话

	@Field("sendScope")
	private String sendScope;//派送范围
	
	@Field("unSendScope")
	private String unSendScope;//不派送范围
	
	@Field("sendTimeLimit")
	private String sendTimeLimit;//派送时限
	
	@Field("hoursFor24")
	private String hoursFor24;//24小时件
	
	@Field("specialService")
	private String specialService;//特殊服务
	
	@Field("remark")
	private String remark;//备注
	
	@Field("createTime")
    private Date createTime;//创建时间
	
	@Field("branchCode")
	private Integer branchCode;//网点用户id
	
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

	public Integer getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(Integer branchCode) {
		this.branchCode = branchCode;
	}

}
