package net.ytoec.kernel.dataobject;

import java.io.Serializable;

public class UserCustom implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3694636014305758899L;

	public static final String RELATIONAL="1";//关联账号个性化配置
	public static final String FIRST_VISIT="2";//页面初次访问个性化配置
	public static final String SITE_PROMPT="3";//网点页面是否弹出提示框个性化配置
	public static final String VISITED="1";//页面初次访问个性化配置
	public static final String OPENNOTIFY = "4";//如果表中存在type=4的数据，则用户名对应的网点用户表示开启了自动通知客户功能。
	public static final String TYPE_REMAIN ="7";//判断是否进行风险提示
	
	private String userName;
	private String bindedUserName;
	private String customerId;
	private String type;
	private String relationalQuery;
	private String field001;
	private String field002;
	private String field003;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getBindedUserName() {
		return bindedUserName;
	}
	public void setBindedUserName(String bindedUserName) {
		this.bindedUserName = bindedUserName;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRelationalQuery() {
		return relationalQuery;
	}
	public void setRelationalQuery(String relationalQuery) {
		this.relationalQuery = relationalQuery;
	}
	public String getField001() {
		return field001;
	}
	public void setField001(String field001) {
		this.field001 = field001;
	}
	public String getField002() {
		return field002;
	}
	public void setField002(String field002) {
		this.field002 = field002;
	}
	public String getField003() {
		return field003;
	}
	public void setField003(String field003) {
		this.field003 = field003;
	}
	
	
	
}
