package net.ytoec.kernel.action.bill;

public enum BillInfo {
	CUSTOMER_TYPE("1", "客户"),
	ORG_TYPE("0", "网点"),
	MONEY_TYPE("1", "金额"),
	
	/**
	 * 客户已确认
	 */
	CUSTOMER_CONF_FLG("1", "已确认"),
	/**
	 * 客户未确认
	 */
	CUSTOMER_NONE_CONF("0", "未确认"),
	
	/**
	 * 网点未确认
	 */
	ORG_NONE_CONFIRMED("0", "未确认"),
	/**
	 * 网点已确认
	 */
	ORG_CONFIRMED("1", "已确认"),
	
	DELETED_NONE("0", "未删除"),
	DELETED("1", "已删除"),
	
	DIFF_TO_COMPANY("0", "已转总公司"),
	DIFF_COMPANY_CONFIRMED("1", "总公司已确认"),
	DIFF_COMPANY_REJECTED("2", "总公司已撤消"),
	DIFF_TO_SUBCOMPANY("3", "已转分公司"),
	DIFF_SUBCOMPANY_REJECTED("4", "分公司已驳回"),
	DIFF_NONE("5", "无差异"),
	
	AUDIT_NONE("0", "未审核"),
	
	FOLDER_PATH("", "C:/yto/tmp"),
	CLIENT_REPLY("1", "client");
	
	private String key;
	private String value;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	private BillInfo(String key, String value){
		this.key = key;
		this.value = value;
	}
}
