package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.Date;

/**
 * 买家等级标准
 * @author Administrator
 *
 */
public class SMSBuyersGrade implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6692891595573619889L;
	
	private int id;  
	
	private double highAccount;     //高级会员为：大于此值交易额
	
	private int highCount;       	//高级会员为：大于此值交易量
	
	private double vipAccount;      //VIP会员为：大于此值交易额
	
	private int vipCount;        	//VIP会员为：大于此值交易量
	
	private double vipHighAccount; 	//至尊VIP会员为：大于此值交易额
	
	private int vipHighCount;   	//至尊VIP会员为：大于此值交易量
	
	private int userId;           	//主帐号的ID
	
	private Date updateTime;      	//修改时间
	
	private int updateUserId;    	//修改人
	
	private Date createTime;      	//创建时间
	
	private int createUserId;    	//创建人
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public double getHighAccount() {
		return highAccount;
	}
	public void setHighAccount(double highAccount) {
		this.highAccount = highAccount;
	}
	public int getHighCount() {
		return highCount;
	}
	public void setHighCount(int highCount) {
		this.highCount = highCount;
	}
	public double getVipAccount() {
		return vipAccount;
	}
	public void setVipAccount(double vipAccount) {
		this.vipAccount = vipAccount;
	}
	public int getVipCount() {
		return vipCount;
	}
	public void setVipCount(int vipCount) {
		this.vipCount = vipCount;
	}
	public double getVipHighAccount() {
		return vipHighAccount;
	}
	public void setVipHighAccount(double vipHighAccount) {
		this.vipHighAccount = vipHighAccount;
	}
	public int getVipHighCount() {
		return vipHighCount;
	}
	public void setVipHighCount(int vipHighCount) {
		this.vipHighCount = vipHighCount;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getUpdateUserId() {
		return updateUserId;
	}
	
	public void setUpdateUserId(int updateUserId) {
		this.updateUserId = updateUserId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	

}
