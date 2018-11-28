/**
 * 
 */
package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.Set;

/**
 * 此类作为我的客户显示列表类
 * @author wangyong
 * @2012-01-05
 */
public class UserBean implements Serializable {

	private Integer userThreadId;//直客id
	
	private Integer userId;//user表用户id
	
	private String userCode;
	
	private String userName;
	
	private Set<String> loginName;
	
	private String phone;
	
	private String telephone;
	
	private String address;
	
	private String switchEccount;//是否关闭电子对账功能(1:关闭；0：开启)
	
	private String isContractUserFlg;

	public Integer getUserThreadId() {
		return userThreadId;
	}

	public void setUserThreadId(Integer userThreadId) {
		this.userThreadId = userThreadId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Set<String> getLoginName() {
		return loginName;
	}

	public void setLoginName(Set<String> loginName) {
		this.loginName = loginName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSwitchEccount() {
		return switchEccount;
	}

	public void setSwitchEccount(String switchEccount) {
		this.switchEccount = switchEccount;
	}

	public String getIsContractUserFlg() {
		return isContractUserFlg;
	}

	public void setIsContractUserFlg(String isContractUserFlg) {
		this.isContractUserFlg = isContractUserFlg;
	}

}
