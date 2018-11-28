package net.ytoec.kernel.dto;

import java.util.Date;

import net.ytoec.kernel.action.remote.xml.StepInfo;

public class PassIssueDTO {
	
	private String mailNo;
	private String shopName;
	private StepInfo stepInfo;
	private String name;
	private String phone;
	private String address;
	private String sendTime1;//揽收时间年月日
	private String sendTime2;//揽收时间时分秒
	private String userTime;//jsp显示*天*小时
	private double userTime2;//超时时间=以用时间-预警值=（系统时间-揽收时间）-预警值
	private String passTime;//超时天数
	private Date questionFenQuTime;//问题件分区时间
	
	public Date getQuestionFenQuTime() {
		return questionFenQuTime;
	}
	public void setQuestionFenQuTime(Date questionFenQuTime) {
		this.questionFenQuTime = questionFenQuTime;
	}
	public String getPassTime() {
		return passTime;
	}
	public void setPassTime(String passTime) {
		this.passTime = passTime;
	}
	private String tips;//是否是问题件标示[0-非问题件；1-问题件]
	
	public String getSendTime1() {
		return sendTime1;
	}
	public void setSendTime1(String sendTime1) {
		this.sendTime1 = sendTime1;
	}
	public String getSendTime2() {
		return sendTime2;
	}
	public void setSendTime2(String sendTime2) {
		this.sendTime2 = sendTime2;
	}
	
	public String getTips() {
		return tips;
	}
	public void setTips(String tips) {
		this.tips = tips;
	}
	public String getMailNo() {
		return mailNo;
	}
	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getUserTime() {
		return userTime;
	}
	public void setUserTime(String userTime) {
		this.userTime = userTime;
	}
	public StepInfo getStepInfo() {
		return stepInfo;
	}
	public void setStepIfno(StepInfo stepInfo) {
		this.stepInfo = stepInfo;
	}
	public double getUserTime2() {
		return userTime2;
	}
	public void setUserTime2(double userTime2) {
		this.userTime2 = userTime2;
	}
	public void setStepInfo(StepInfo stepInfo) {
		this.stepInfo = stepInfo;
	}
	
	
	
}
