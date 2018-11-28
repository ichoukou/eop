package net.ytoec.kernel.dataobject;

import java.io.Serializable;

public class AccountUser implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 和ec_core_accountUser表主键保持一致
	 */
	private Integer id;
	
	/**
	 *支付宝的账号
	 */
	private String payAccount;
	
	/**
	 * User 表中的id 与EC_CORE_USER 的id 一致
	 */
	private Integer userId;
	/**
	 * 用户类型
	 * 
	 * */
	private String userType;
	
	private String userName;

	/**
	 * 余额	（总充值-总消费）
	 */
	private Double balance;
	
	/**
	 * 总余额（admin可用总余额）
	 * 
	 * */
	private Double sysUserBalance;
	/**
	 * 系统总消费（admin）
	 * */
	private Double sysallConsume;
	
	/**
	 *可用余额
	 */
	private Double useBalance;
	
	/**
	 *电话
	 */
	private String cellPhone;
	
	/**
	 *总消费
	 */
	private Double allConsume;
	
	/**
	 *总充值
	 */
	private Double allRecharge;
	
	
	/**
	 *备注
	 */
	private String remark;
	
	/**
	 *是否可用	0 否 1 是
	 */
	private String flag;

	/**
	*创建时间
	*/
	private java.util.Date createTime;
	/**
	*修改时间
	*/
	private java.util.Date updateTime;
	
	/**
	 *验证码
	 */
	private String phoneCode;
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getPayAccount() {
		return payAccount;
	}
	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Double getBalance() {
		if(balance==null){
			return 0.00;
		}
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public Double getUseBalance() {
		if(useBalance==null){
			return 0.00;
		}
		return useBalance;
	}
	public void setUseBalance(Double useBalance) {
		this.useBalance = useBalance;
	}
	public String getCellPhone() {
		return cellPhone;
	}
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	public Double getAllConsume() {
		if(allConsume==null){
			return 0.00;
		}
		return allConsume;
	}
	public void setAllConsume(Double allConsume) {
		this.allConsume = allConsume;
	}
	public Double getAllRecharge() {
		if(allRecharge==null){
			return 0.00;
		}
		return allRecharge;
	}
	public void setAllRecharge(Double allRecharge) {
		this.allRecharge = allRecharge;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
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
	public String getPhoneCode() {
		return phoneCode;
	}
	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Double getSysUserBalance() {
		return sysUserBalance;
	}
	public void setSysUserBalance(Double sysUserBalance) {
		this.sysUserBalance = sysUserBalance;
	}
	public Double getSysallConsume() {
		return sysallConsume;
	}
	public void setSysallConsume(Double sysallConsume) {
		this.sysallConsume = sysallConsume;
	}
	
	
   
}
