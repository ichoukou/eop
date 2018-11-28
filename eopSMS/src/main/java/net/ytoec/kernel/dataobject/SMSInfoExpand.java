package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.Date;

/**
 * 短信的详细信息扩展
 * @author shitianzeng
 * 2012-07-05
 */
public class SMSInfoExpand extends SMSInfo implements Serializable {
	
	/**
	 * 
	 */
	private String smsTypeName;   //短信类型名称
	
	private String templateName;  //模版名称
	
	private String smsContent;    //短信内容
	
	private String shopName;       //网店名称
	
	private String createUserName;  //创建短信人姓名,发件人

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getSmsTypeName() {
		return smsTypeName;
	}

	public void setSmsTypeName(String smsTypeName) {
		this.smsTypeName = smsTypeName;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getSmsContent() {
		return smsContent;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	
}
