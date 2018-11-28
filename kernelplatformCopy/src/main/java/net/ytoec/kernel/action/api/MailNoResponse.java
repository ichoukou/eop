package net.ytoec.kernel.action.api;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "customerCode", "sequence", "quantity", "message",
		"mailNoList" })
@XmlRootElement(name = "MailNoResponse")
public class MailNoResponse {

	private String customerCode;
	private String sequence;
	private int quantity;
	private String message;

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

	@XmlElementWrapper(name="mailNoList")
	@XmlElement(name="mailNo")
	public List<String> getMailNoList() {
		return mailNoList;
	}

	public void setMailNoList(List<String> mailNoList) {
		this.mailNoList = mailNoList;
	}

}
