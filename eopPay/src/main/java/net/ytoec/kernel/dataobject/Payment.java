package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.Date;

import net.ytoec.kernel.common.DateUtil;

public class Payment implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 和ec_core_payment表主键保持一致
	 */
	private Integer id;
	
	/**
	 * User 表中的id 与EC_CORE_USER 的id 一致
	 * 存储主账号ID
	 */
	private Integer userId;
	
	/**
	 * User 表中的userName与EC_CORE_USER 的username一致
	 * */
	private String userName;
	
	/**
	 * User 表中的userName与EC_CORE_USER 的user_name_text一致
	 * */
	private String userNameText;
	
	/**
	 * 店铺名称
	 */
	private String shopName;
	
	/**
	 *与ec_core_accountUser 的id 一致
	 */
	private Integer auserId;
	
	/**
	 * 交易额
	 */

	private Double dealMoney;
	/**
	 * 用户类型
	 * */
	private String userType;
	
	/**
	 *交易类型	0 在线充值，1订购服务，2续费服务3购买短信包
	 */
	private String dealType;
	
	
	/**
	 *交易名称	问题件预警    
	 *短信体验套餐   短信初级短信套餐
     *短信中级套餐
     *短信高级套餐
     *其它   超时件服务   支付操作
	 */
	private String dealName;
	

	/**
	*交易时间
	*/
	private java.util.Date dealTime;
	/**
	*付款时间
	*/
	private java.util.Date payTime;	
	
	/**
	 *交易状态	0 等待付款 1正在充值2成功3失败4已关闭（超时系统关闭\手动关闭）
	 * 5 逻辑删除（为了处理支付宝返回的交易金额不一致添加）
	 */
	private String dealStatus;
	
	/**
	 *状态	0 出账  1 进账（相对于账号来说）
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
	 *备注
	 */
	private String remark;
	/**
	 *错误原因
	 */
	private String reason;
	
	/**
	 *用户删除状态	0 否 1 删除
	 *
	 * 
	 */
	private String delFlag;
	
	
	/**
	 * 促销费用	实际服务的费用[不存在交易明细表中 ,只在页面显示]
	 */
	private Double promCost;
	
	
	/**
	 * 操作人ID
	 * 若操作人是主账号，则存储主账号ID
	 * 若操作人是子账号，则存储子账号ID
	 */
	private Integer createrId;
	

	public String getDealName() {
		return dealName;
	}

	public void setDealName(String dealName) {
		this.dealName = dealName;
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

	public Integer getAuserId() {
		return auserId;
	}

	public void setAuserId(Integer auserId) {
		this.auserId = auserId;
	}

	public Double getDealMoney() {
		return dealMoney;
	}

	public void setDealMoney(Double dealMoney) {
		this.dealMoney = dealMoney;
	}

	public String getDealType() {
		return dealType;
	}

	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

	public java.util.Date getDealTime() {
		return dealTime;
	}

	public void setDealTime(java.util.Date dealTime) {
		this.dealTime = dealTime;
	}

	public java.util.Date getPayTime() {
		return payTime;
	}

	public void setPayTime(java.util.Date payTime) {
		this.payTime = payTime;
	}

	public String getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	/**
	*有效时间
	*/
	private java.util.Date limitTime;
	
	public java.util.Date getLimitTime() {
		if(this.createTime!=null)
		{
			limitTime=DateUtil.getDateBefore(createTime, 7);			
			//若当前日期小于等于有效时间则有效
			if(DateUtil.compareDay(new Date(),limitTime)) {
				effective="1";
			}
			else {
				effective="0";
			}
		}
		return limitTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setLimitTime(java.util.Date limitTime) {
		this.limitTime = limitTime;
	}
	
	/**
	 * 判断本条交易记录是否有效
	 */
	private String effective;
	
	public void setEffective(String effective) {
		this.effective = effective;
	}

	public String getEffective() {
		getLimitTime();
		return effective;
	}

	public Double getPromCost() {
		return promCost;
	}

	public void setPromCost(Double promCost) {
		this.promCost = promCost;
	}

	public void setCreaterId(Integer createrId) {
		this.createrId = createrId;
	}

	public Integer getCreaterId() {
		return createrId;
	}

	public void setUserNameText(String userNameText) {
		this.userNameText = userNameText;
	}

	public String getUserNameText() {
		return userNameText;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopName() {
		return shopName;
	}
	
}
