package net.ytoec.kernel.search.dto;

import java.util.Date;

import net.ytoec.kernel.common.OrderTypeEnum;
import net.ytoec.kernel.common.StatusEnum;

/**
 * 搜索参数DTO
 * 
 * @author der
 */
public class EccoreSearchQueryDTO {

    /**
     * 开始日期
     */
    private Date          startDate;
    /**
     * 结束日期
     */
    private Date          endDate;
    /**
     * 客户标识
     */
    private String[]      customerIDs;
    /**
     * 运单号
     */
    private String        mailNo;

    /**
     * 省id
     */
    private Integer       numProv;
    /**
     * city的名称，需要到缓存里取得 id
     */
    private Integer       numCity;
    /**
     * 地区的名称，需要到缓存里取得id
     */
    private Integer       numDistrict;

    /**
     * 买家电话或手机
     */
    private String        phone;

    /**
     * 买家姓名
     */
    private String        name;

    /**
     * 订单状态
     */
    private StatusEnum    status;

    /**
     * 订单类型
     */
    private OrderTypeEnum orderType;

    /**
     * 排序方式 desc asc
     */
    private String        sortType;

    /**
     * 每页大小
     */
    private Integer       pageSize;
    /**
     * 从第几个开始
     */
    private Integer       curPage;
    
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
	public String[] getCustomerIDs() {
		return customerIDs;
	}
	public void setCustomerIDs(String[] customerIDs) {
		this.customerIDs = customerIDs;
	}
	public String getMailNo() {
		return mailNo;
	}
	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}
	public Integer getNumProv() {
		return numProv;
	}
	public void setNumProv(Integer numProv) {
		this.numProv = numProv;
	}
	public Integer getNumCity() {
		return numCity;
	}
	public void setNumCity(Integer numCity) {
		this.numCity = numCity;
	}
	public Integer getNumDistrict() {
		return numDistrict;
	}
	public void setNumDistrict(Integer numDistrict) {
		this.numDistrict = numDistrict;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public StatusEnum getStatus() {
		return status;
	}
	public void setStatus(StatusEnum status) {
		this.status = status;
	}
	public OrderTypeEnum getOrderType() {
		return orderType;
	}
	public void setOrderType(OrderTypeEnum orderType) {
		this.orderType = orderType;
	}
	public String getSortType() {
		return sortType;
	}
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getCurPage() {
		return curPage;
	}
	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}
}
