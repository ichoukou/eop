package net.ytoec.kernel.dataobject;

import java.io.Serializable;

public class ServiceHistory implements Serializable {
	
	
	
	private static final long serialVersionUID = 1L;

	/**
	 * 和c_core_serviceHistory表主键保持一致
	 */
	private Integer id;
	/**
	 *与ec_core_dredgeService 的id 一致
	 */
	private Integer dserviceId;
	
	/**
	 *与ec_core_payment 的id 一致
	 */
	private Integer payId;
	
	/**
	 * User 表中的id 与EC_CORE_USER 的id 一致
	 */
	private Integer userId;
	/**
	 *服务周期	0月、1季、2半年、3年
	 */
	private String circle;
	/**
	*开始日期
	*/
	private java.util.Date beginDate;
	/**
	*到期日期
	*/
	private java.util.Date endDate;	
	
	/**
	 *是否自动续费	0自动续费 1手动续费
	 */
	private String autoRenew;
	
	/**
	 *服务类型	0 在线充值，1订购服务，2续费服务3购买短信包
	 */
	private String dealType;
	
	/**
	 *服务名称  * 余额不足提醒  交易提醒 服务到期提醒  短信不足提醒   问题件预警
     * 发货提醒  派件提醒   短信体验套餐    短信初级短信套餐
     *  短信中级套餐    短信高级套餐    其它
     *  短信服务   超时件服务 支付操作 
	 */
	private String dealName;
	
	
	/**
	 *短信数量
	 */
	private Integer smsCount;
	
	/**
	 * 费用
	 */
	private Double cost;
	/**
	 * 促销费用	实际服务的费用
	 */
	private Double promCost;
	/**
	*创建时间
	*/
	private java.util.Date createTime;
	
	/**
	 *备注
	 */
	private String remark;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDserviceId() {
		return dserviceId;
	}

	public void setDserviceId(Integer dserviceId) {
		this.dserviceId = dserviceId;
	}

	public Integer getPayId() {
		return payId;
	}

	public void setPayId(Integer payId) {
		this.payId = payId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public java.util.Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(java.util.Date beginDate) {
		this.beginDate = beginDate;
	}

	public java.util.Date getEndDate() {
		return endDate;
	}

	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}

	public String getAutoRenew() {
		return autoRenew;
	}

	public void setAutoRenew(String autoRenew) {
		this.autoRenew = autoRenew;
	}

	
	public String getDealType() {
		return dealType;
	}

	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

	public String getDealName() {
		return dealName;
	}

	public void setDealName(String dealName) {
		this.dealName = dealName;
	}

	public Integer getSmsCount() {
		return smsCount;
	}

	public void setSmsCount(Integer smsCount) {
		this.smsCount = smsCount;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Double getPromCost() {
		return promCost;
	}

	public void setPromCost(Double promCost) {
		this.promCost = promCost;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
