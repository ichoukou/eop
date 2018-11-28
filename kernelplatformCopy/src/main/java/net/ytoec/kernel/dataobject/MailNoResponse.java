package net.ytoec.kernel.dataobject;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

/**
 * @作者：罗典
 * @时间：2013-08-29
 * @描述：拉取面单接口响应消息实体
 * */
@XmlType(propOrder = { "success", "message", "quantity", "sequence",
		"mailNoList" })
@XmlRootElement(name = "MailNoResponse")
public class MailNoResponse {
	// 是否成功 true ,false
	private Boolean success;
	// 返回消息
	private String message;
	// 同步成功的数量
	private int quantity;
	// 面单集合
	private MailNo mailNoList;
	// 同步的批次号，每次同步，圆通电商平台为本次同步分配一个唯一的批次号
	private Integer sequence;
	// 响应消息命名空间(此处为无命名空间)
	public static final QName QNAME = new QName("","MailNoResponse");
	
	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public MailNo getMailNoList() {
		return mailNoList;
	}

	public void setMailNoList(MailNo mailNoList) {
		this.mailNoList = mailNoList;
	}

}
