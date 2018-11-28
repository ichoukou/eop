package net.ytoec.kernel.dataobject;

/**
 * @作者：罗典
 * @时间：2013-08-29
 * @描述：拉取面单接口请求消息实体
 * */
public class MailNoRequest {
	// 电子面单号
	private String mailNo;
	// 客户编码
	private String customerCode;
	// 同步成功的数量
	private int quantity;
	// 同步的批次号，每次同步，圆通电商平台为本次同步分配一个唯一的批次号
	private String sequence;
	// 更新结果  (不传则表示为成功)
	private Boolean success = true;

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

}
