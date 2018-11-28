package xml;


/**
 * 
 * 步骤详情.
 * 
 */
public class StepInfo implements ObjectToXmlFragment {
	// 接收时间
	private String acceptTime;
	// 接受地点
	private String acceptAddress;
	// 接受时的状态
	private String status;
	// 备注
	private String remark;
	
	//操作方式：派件人、取件人、签收人
	private String operate;
	
	private String name;
	
	//网点编码
	private String siteCode;
	
	//网点联系方式：金刚不会同步过来，需查询后赋值
	private String contactWay;

	public String getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(String acceptTime) {
		this.acceptTime = acceptTime;
	}

	public String getAcceptAddress() {
		return acceptAddress;
	}

	public void setAcceptAddress(String acceptAddress) {
		this.acceptAddress = acceptAddress;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
		/**
		 * 设置操作方式
		 */
		if(remark.contains("派件"))
			setOperate("派件人");
		else if(remark.contains("揽收"))
			setOperate("取件人");
		else if(remark.contains("签收"))
			setOperate("签收人");
		else
			setOperate("操作人");
	}

	public String toXmlFragment() {
		StringBuilder sb = new StringBuilder();
		sb.append("<step>");
		sb.append("<acceptTime>");
		sb.append(this.getAcceptTime());
		sb.append("</acceptTime>");
		sb.append("<acceptAddress>");
		sb.append(this.getAcceptAddress());
		sb.append("</acceptAddress>");
		sb.append("<status>");
		sb.append(this.getStatus());
		sb.append("</status>");
		sb.append("<remark>");
		sb.append(this.getRemark());
		sb.append("</remark>");
		sb.append("</step>");

		return sb.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getContactWay() {
		return contactWay;
	}

	public void setContactWay(String contactWay) {
		this.contactWay = contactWay;
	}
	
}
