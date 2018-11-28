package net.ytoec.kernel.dataobject;

import java.io.Serializable;

/**
 * Mail的javabean对象
 * 
 * @author ChenRen
 * @Date 2011-08-10
 */
public class Mail implements Serializable{
	private static final long serialVersionUID = -7468020771309272438L;
	/** 发件人邮箱地址 */
	private String fromMail;
	/** 发件人显示名称<br>如果显示名称为空, 则显示值为邮箱地址 */
	private String fromMailText;
	/** 收件人地址; 如果有多个以";"为分隔符 */
	private String sendToMail;
	/** 收件人地址; 如果有多个以";"为分隔符 */
	private String sendCCMail;
	/** 主题 */
	private String subject;
	private String content;

	/* === constructors === */
	public Mail() {
	}

	public String getSendCCMail() {
		return sendCCMail;
	}

	public void setSendCCMail(String sendCCMail) {
		this.sendCCMail = sendCCMail;
	}

	public String getFromMail() {
		return fromMail;
	}

	public void setFromMail(String fromMail) {
		this.fromMail = fromMail;
	}

	public String getFromMailText() {
		return fromMailText == null ? fromMail : fromMailText;
	}

	public void setFromMailText(String fromMailText) {
		this.fromMailText = fromMailText;
	}

	public String getSendToMail() {
		return sendToMail;
	}

	public void setSendToMail(String sendToMail) {
		this.sendToMail = sendToMail;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
