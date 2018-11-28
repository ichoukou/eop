package net.ytoec.kernel.dataobject;

/**
 * 订单状态消息类
 * create by wangmindong
 * create timer 2013-03-28
 *
 */
public enum NotifyType {
	ACCEPT((short)1, "ACCEPT", "接单"),
	UNACCEPT((short)2, "UNACCEPT", "撤单"),
	GOT((short)3, "GOT", "揽收成功"),
	NOT_SEND((short)4, "NOT_SEND", "揽收失败 "),
	DEPARTURE((short)12, "DEPARTURE", "已发出"),
	ARRIVAL((short)13, "ARRIVAL", "已收入"),
	SIGNED((short)7, "SIGNED", "签收成功"),
	FAILED((short)8, "FAILED", "签收失败"),
	CONFIRM((short)9, "CONFIRM", "订单创建确认"),
	PACKAGE((short)10, "PACKAGE", "建包扫描"),
	DELIVERY((short)11, "DELIVERY", "派件中"),
	TRANSFER((short)20, "TRANSFER", "超区换单"),	
	WEIGHT((short)21, "WEIGHT", "重量更新"),	
	MAILNO((short)22, "MAILNO", "面单更新"),	
	;
	
	private Short value;
	private String content;
	private String desc;
	
	private NotifyType(Short value, String content, String desc) {
		this.value = value;
		this.content = content;
		this.desc = desc;
	}

	public Short getValue() {
		return value;
	}

	public void setValue(Short value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
