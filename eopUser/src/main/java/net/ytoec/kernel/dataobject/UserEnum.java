/**
 * net.ytoec.kernel.dataobject
 * UserEnum.java
 * 2012-8-22下午01:59:58
 * @author wangyong
 */
package net.ytoec.kernel.dataobject;

/**
 * 用户类型枚举
 * @author wangyong
 * 2012-8-22
 */
public enum UserEnum {
	
	//卖家（包括业务账号和分仓）主账号
	SELLER_MAIN("卖家", new Short("1")),
	
	//卖家客服账号
	SELLER_KEFU("卖家客服", new Short("11")),
	
	//卖家财务账号
	SELLER_CAIWU("卖家财务", new Short("12")),
	
	//卖家经理账号
	SELLER_MANAGER("卖家经理", new Short("13")),
	
	//网点主账号
	SITE_MAIN("网点", new Short("2")),
	
	//网点客服账号
	SITE_KEFU("卖家客服", new Short("21")),
	
	//网点财务账号
	SITE_CAIWU("卖家财务", new Short("22")),
	
	//网点经理账号
	SITE_MANAGER("卖家经理", new Short("23")),
	
	//承包区账号
	CONTRACT_USER("承包区", new Short("2")),
	
	//平台主账号
	PLANT_MAIN("平台", new Short("4")),
	
	//承包区账号
	PLANT_KEFU("平台客服", new Short("41")),
	
	//管理员账号
	ADMIN("承包区", new Short("3"));
	
	private Short value;
	
	private String name;
	
	private UserEnum(String name, Short value){
		this.name = name;
		this.value = value;
	}

	public Short getValue() {
		return value;
	}

	public void setValue(Short value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
