package net.ytoec.kernel;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 同步电子面单响应类
 * 
 * @author huangtianfu
 * 
 */
@XmlType(propOrder = { "customerCode", "sequence", "quantity", "message",
		"mailNoList" })
@XmlRootElement(name = "MailNoResponse")
public class MailNoResponse {

	/**
	 * 商家代码
	 */
	private String customerCode;

	/**
	 * 请求序列
	 */
	private String sequence;

	/**
	 * 获取的电子面单数量
	 */
	private int quantity;

	/**
	 * 响应消息
	 */
	private String message;

	/**
	 * 获取的电子面单列表
	 */
	private List<String> mailNoList;

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@XmlElementWrapper(name = "mailNoList")
	@XmlElement(name = "mailNo")
	public List<String> getMailNoList() {
		return mailNoList;
	}

	public void setMailNoList(List<String> mailNoList) {
		this.mailNoList = mailNoList;
	}

}
