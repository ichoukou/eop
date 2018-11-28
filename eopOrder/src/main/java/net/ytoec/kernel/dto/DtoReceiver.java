/**
 * net.ytoec.kernel.dto
 * DtoReceiver.java
 * 2012-7-2上午10:39:21
 * @author wangyong
 */
package net.ytoec.kernel.dto;

/**
 * 消息发送时接收人信息：网点接收人是直客；管理员接收人是网点和直客
 * @author wangyong
 * 2012-7-2
 */
public class DtoReceiver {
	
	//用户id
	private Integer userId;
	
	//用户code
	private String userCode;
	
	//用户名
	private String userName;
	
	//用户类型：1代表直客； 2代表网点
	private Integer userType;

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

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}
}
