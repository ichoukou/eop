package net.ytoec.kernel.dataobject;

import java.io.Serializable;

public class DredgeService implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 和ec_core_dredgeService表主键保持一致
	 */
	private Integer id;

	/**
	 * User 表中的id 与EC_CORE_USER 的id 一致
	 */
	private Integer userId;
	/**
	 * User 表中的id 与EC_CORE_USER 的id 一致
	 */
	private String userName;

	/**
	 * 与ec_core_payService 的id 一致
	 */
	private Integer serviceId;
	/**
	 * 服务名称
	 * */
	private String serviceName;
	/**
	 * 用户类型
	 * */
	private String userType;

	/**
	 * 是否启用 0创建1启用2停用3到期 4 用户删除
	 */
	private String flag;

	/**
	 * 低于余额 低于多少钱时短信提醒
	 */
	private Double balance;

	/**
	 * 服务周期 0月、1季、2、半年3、年
	 */
	private String circle;

	/**
	 * 开始日期
	 */
	private java.util.Date beginDate;
	/**
	 * 结束日期
	 */
	private java.util.Date endDate;

	/**
	 * 是否自动续费 0否 1是
	 */
	private String autoRenew;

	/**
	 * 可使用短信数 （累计购买短信量-累计发送量）
	 */
	private Integer smsUsecount;
	/**
	 * 累计购买短信量
	 */
	private Integer smsAllcount;
	/**
	 * 累计发送量
	 */
	private Integer smsSendcount;
	/**
	 * 不足条数
	 */
	private Integer smsLess;

	/**
	 * 费用
	 */
	private Double cost;

	/**
	 * 是否已经提醒 0 否 1 是
	 */
	private String remindFlag;

	/**
	 * 创建时间
	 */
	private java.util.Date createTime;
	/**
	 * 修改时间
	 */
	private java.util.Date updateTime;
	
	/**
	 * 离服务到期还有几天
	 */
	private int days;

	/**
	 * 备注
	 */
	private String remark;
    /**
     * 判断是子账号开通的 还是父账号开通的
     * 0 子账号开通
     * 1 父账号开通
     */
	private String openFromFlag;
	

	public String getOpenFromFlag() {
		return openFromFlag;
	}

	public void setOpenFromFlag(String openFromFlag) {
		this.openFromFlag = openFromFlag;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
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

	public Integer getSmsUsecount() {
		if(smsUsecount==null){
			return 0;
		}
		return smsUsecount;
	}

	public void setSmsUsecount(Integer smsUsecount) {
		this.smsUsecount = smsUsecount;
	}

	public Integer getSmsAllcount() {
		if(smsAllcount==null){
			return 0;
		}
		return smsAllcount;
	}

	public void setSmsAllcount(Integer smsAllcount) {
		this.smsAllcount = smsAllcount;
	}

	public Integer getSmsSendcount() {
		if(smsSendcount==null){
			return 0;
		}
		return smsSendcount;
	}

	public void setSmsSendcount(Integer smsSendcount) {
		this.smsSendcount = smsSendcount;
	}

	public Integer getSmsLess() {
		if(smsLess==null){
			return 0;
		}
		return smsLess;
	}

	public void setSmsLess(Integer smsLess) {
		this.smsLess = smsLess;
	}

	public Double getCost() {
		if(cost==null){
			return 0.00;
		}
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public String getRemindFlag() {
		return remindFlag;
	}

	public void setRemindFlag(String remindFlag) {
		this.remindFlag = remindFlag;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public java.util.Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

}
