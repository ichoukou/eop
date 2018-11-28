package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.Date;

import javassist.expr.NewArray;

import net.ytoec.kernel.action.remote.process.RequestOrderProcessor;
import net.ytoec.kernel.action.remote.xml.Response;

public class ApplyKdbz implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Date createTime;
	private Date updateTime;
	private String ecCompanyId;
	private String logisticProvider;
	private String customerId;
	
	private String wangwang;
	private String status;
	private String requestNo;
	private String serviceType;
	private String name;
	private String phone;
	private String mobile;
	private String address;
	private String reason;
	private String remark;
	private String vip;
	private Customer customer = new Customer();
	public ApplyKdbz() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date date) {
		this.createTime = date;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getEcCompanyId() {
		return ecCompanyId;
	}

	public void setEcCompanyId(String ecCompanyId) {
		this.ecCompanyId = ecCompanyId;
	}

	public String getLogisticProvider() {
		return logisticProvider;
	}

	public void setLogisticProvider(String logisticProvider) {
		this.logisticProvider = logisticProvider;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getWangwang() {
		return wangwang;
	}

	public void setWangwang(String wangwang) {
		this.wangwang = wangwang;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	private Response response = new Response();
 	private RequestOrderProcessor requestOrderProcessor = new RequestOrderProcessor(this);

	public ApplyKdbz toObject(String xmlFragment) {
		 
		ApplyKdbz applyKdbz = requestOrderProcessor.parseKdbz(xmlFragment);
		return applyKdbz;
	}
	public void setResponse(Response response) {
		this.response = response;
	}

	public Response getResponse() {
		return response;
	}

	public void setVip(String vip) {
		this.vip = vip;
	}

	public String getVip() {
		return vip;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Customer getCustomer() {
		return customer;
	}

	

	
}
