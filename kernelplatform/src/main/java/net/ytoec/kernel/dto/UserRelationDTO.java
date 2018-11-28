/**
 * net.ytoec.kernel.dto
 * UserRelationDTO.java
 * 2012-9-26下午04:13:02
 * @author wangyong
 */
package net.ytoec.kernel.dto;

/**
 * 用户关联关系javaBean
 * @author wangyong
 * 2012-9-26
 */
public class UserRelationDTO {

	private String userName;
	
	private String shopName;
	
	//是否已经个性化配置：1是；0否
	private Integer isRelated;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Integer getIsRelated() {
		return isRelated;
	}

	public void setIsRelated(Integer isRelated) {
		this.isRelated = isRelated;
	}
	
}
