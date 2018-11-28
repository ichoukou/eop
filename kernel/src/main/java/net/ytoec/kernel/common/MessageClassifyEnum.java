/**
 * net.ytoec.kernel.common
 * MessageClassifyEnum.java
 * 2012-7-2下午03:45:39
 * @author wangyong
 */
package net.ytoec.kernel.common;

/**
 * 消息分类枚举类
 * @author wangyong
 * 2012-7-2
 */
public enum MessageClassifyEnum {
	
	/**
	 * 所有消息
	 */
	ALL("所有消息","0"),
	
	/**
	 * 平台用户  发件箱
	 */
	PLANT_SEND("发件箱","0"),
	
	/**
	 * 管理员消息
	 */
	ADMIN("管理员","1"),
	
	/**
	 * 平台用户 分仓账号消息
	 */
	PLANT_FC("管理员","1"),
	
	/**
	 * 系统消息
	 */
	SYSTEM("系统消息","2"),
	
	/**
	 * 网点
	 */
	SITE("网点","2"),
	
	/**
	 * 卖家消息
	 */
	VIP("卖家","3"),
	
	/**
	 * 其他消息
	 */
	ELSE("其他消息","3");
	
	private String name;
	
	private String value;
	
	private MessageClassifyEnum(String name, String value){
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
