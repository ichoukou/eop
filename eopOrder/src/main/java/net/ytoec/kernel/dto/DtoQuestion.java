package net.ytoec.kernel.dto;

import java.util.Date;
import java.util.List;

import net.ytoec.kernel.dataobject.QuestionaireRemark;
import net.ytoec.kernel.dataobject.QuestionaireExchange;
import net.ytoec.kernel.dataobject.QuestionnaireDeal;
import net.ytoec.kernel.dataobject.User;

/**
 * 问题件的数据传输对象；封装前端的查询条件和数据的展示属性。
 * @author wangyong
 * 2012-01-12
 */
public class DtoQuestion {

	/** 问题单主键*/
	private Integer id;
	/** 运单号*/
	private String mailNO;
	/** 直客信息客户编码 */
	private String userCode;
	/** 直客信息用户名 */
	private String userName;
	/** 网点处理状态:默认未通知（1），已通知（2）,其他（3） */
	private String dealStatus;
	/**
	 * 卖家处理状态:未处理（1），处理中（2），已处理（3）（注：卖家只关注网点已通知状态下的问题件）
	 */
	private String vipStatus;
	/**
	 * 网点未读（0），网点已读（1）
	 */
	private String wdIsRead;
	/**
	 * 卖家未读（0），卖家已读（1）
	 */
	private String mjIsRead;
	/** 卖家customerId  */
	private String customerId;
	/** 揽收网点code;*/
	private String branchId;
	//买家联系方式
	private String contactWay;
	//买家姓名
	private String buyerName;
	//图片地址
	private String IMG1;
	private String IMG2;
	private String IMG3;
	private String IMG4;
	
	/**
	 * 订单状态:1(已签收)；0(未签收)
	 */
	private String orderStatus;
	
	/** 会员名 */
	private String taobaoLoginName;
	
	/**
	 * 问题单类型
	 */
	private String feedbackInfo;
	/** 问题id, 直接同步金刚表的值 */
	private String issueId;
	/** 问题描述 */
	private String issueDesc;
	/** 卖家问题描述：显示网点和卖家之间交互的第一条记录 */
	private String mjQuesDesc;
	/** 问题状态. 直接从金刚同步过来的数据.PD10/未处理；PD20/处理中；PD30/处理完成；PD40/取消； */
	private String issueStatus;
	/** 揽收网点 */
	private String branckText;
	/** 上报人 */
	private String issueCreateUserText;
	/** 上报时间 */
	private Date issueCreateTime;
	/** 
	 * 接收网点 
	 */
	private String recBranckText;
	/** 上报网点 */
	private String reportBranckText;
	/** 上报网点联系方式 */
	private String reportBranckContact;
	/**
	 * 网点沟通记录的list. 显示数据的时候实时从金刚的表中查询的数据.
	 */
	private List<QuestionnaireDeal> quesDealList;
	
	/**
	 * 网点沟通记录的list. 显示数据的时候实时从金刚的表中查询的数据.
	 */
	private String quesDealString;
	
	/** 
	 * 卖家客户信息：根据卖家customerId获取 
	 */
	private User customer;
	/**
	 * 问题件备注信息
	 */
	private QuestionaireRemark questionaireRemark;
	/**
	 * 网点同卖家交互信息
	 */
	private List<QuestionaireExchange> questionaireExchangeList;
	
	/**
	 *  网点同卖家交互信息拼接成字符串
	 */
	private String questionaireExchangeString;
	
	/**
	 * 网点同卖家交互信息拼接成字符串的截取部分在页面上部分显示
	 */
	private String questionaireExchangeCutString;
	
	/**
	 * 排序方式：前端页面按上报时间的排序方式：有 1(升序) 2（降序）两种
	 */
	private Integer sortType;
	
	/** 卖家对应网点联系方式 */
	private String relationShip;
	
	/** 卖家对应网点名称 */
	private String siteName;
	
	/** 卖家对应网点上报问题件时间 */
	private Date siteReportTime;
	
	/**
	 * 是否是其他客户的问题件（1，是；0否）
	 */
	private Integer isElseCustomer;
	
	/** 问题件标签id */
	private Integer tagId;
	
	/** 店铺名称 */
	private String shopName;
	
	/**
	 *  网点同卖家交互信息拼接成字符串
	 */
	public String getQuestionaireExchangeString() {
		return questionaireExchangeString;
	}
	
	public void setQuestionaireExchangeString(String questionaireExchangeString) {
		this.questionaireExchangeString = questionaireExchangeString;
	}
	
	
	public String getQuesDealString() {
		return quesDealString;
	}

	public void setQuesDealString(String quesDealString) {
		this.quesDealString = quesDealString;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMailNO() {
		return mailNO;
	}
	public void setMailNO(String mailNO) {
		this.mailNO = mailNO;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDealStatus() {
		return dealStatus;
	}
	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getContactWay() {
		return contactWay;
	}
	public void setContactWay(String contactWay) {
		this.contactWay = contactWay;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	public String getIMG1() {
		return IMG1;
	}
	public void setIMG1(String iMG1) {
		IMG1 = iMG1;
	}
	public String getIMG2() {
		return IMG2;
	}
	public void setIMG2(String iMG2) {
		IMG2 = iMG2;
	}
	public String getIMG3() {
		return IMG3;
	}
	public void setIMG3(String iMG3) {
		IMG3 = iMG3;
	}
	public String getIMG4() {
		return IMG4;
	}
	public void setIMG4(String iMG4) {
		IMG4 = iMG4;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getTaobaoLoginName() {
		return taobaoLoginName;
	}
	public void setTaobaoLoginName(String taobaoLoginName) {
		this.taobaoLoginName = taobaoLoginName;
	}
	public String getFeedbackInfo() {
		return feedbackInfo;
	}
	public void setFeedbackInfo(String feedbackInfo) {
		this.feedbackInfo = feedbackInfo;
	}
	public String getIssueId() {
		return issueId;
	}
	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}
	public String getIssueDesc() {
		return issueDesc;
	}
	public String getMjQuesDesc() {
		return mjQuesDesc;
	}
	public void setMjQuesDesc(String mjQuesDesc) {
		this.mjQuesDesc = mjQuesDesc;
	}
	public void setIssueDesc(String issueDesc) {
		this.issueDesc = issueDesc;
	}
	public String getIssueStatus() {
		return issueStatus;
	}
	public void setIssueStatus(String issueStatus) {
		this.issueStatus = issueStatus;
	}
	public String getBranckText() {
		return branckText;
	}
	public String getReportBranckContact() {
		return reportBranckContact;
	}
	public void setReportBranckContact(String reportBranckContact) {
		this.reportBranckContact = reportBranckContact;
	}
	public void setBranckText(String branckText) {
		this.branckText = branckText;
	}
	public String getIssueCreateUserText() {
		return issueCreateUserText;
	}
	public void setIssueCreateUserText(String issueCreateUserText) {
		this.issueCreateUserText = issueCreateUserText;
	}
	public Date getIssueCreateTime() {
		return issueCreateTime;
	}
	public void setIssueCreateTime(Date issueCreateTime) {
		this.issueCreateTime = issueCreateTime;
	}
	public String getRecBranckText() {
		return recBranckText;
	}
	public void setRecBranckText(String recBranckText) {
		this.recBranckText = recBranckText;
	}
	public String getReportBranckText() {
		return reportBranckText;
	}
	public void setReportBranckText(String reportBranckText) {
		this.reportBranckText = reportBranckText;
	}
	public List<QuestionnaireDeal> getQuesDealList() {
		return quesDealList;
	}
	public void setQuesDealList(List<QuestionnaireDeal> quesDealList) {
		this.quesDealList = quesDealList;
	}
	public User getCustomer() {
		return customer;
	}
	public void setCustomer(User customer) {
		this.customer = customer;
	}
	public QuestionaireRemark getQuestionaireRemark() {
		return questionaireRemark;
	}
	public void setQuestionaireRemark(QuestionaireRemark questionaireRemark) {
		this.questionaireRemark = questionaireRemark;
	}
	public List<QuestionaireExchange> getQuestionaireExchangeList() {
		return questionaireExchangeList;
	}
	public void setQuestionaireExchangeList(
			List<QuestionaireExchange> questionaireExchangeList) {
		this.questionaireExchangeList = questionaireExchangeList;
	}
	public Integer getSortType() {
		return sortType;
	}
	public void setSortType(Integer sortType) {
		this.sortType = sortType;
	}
	public String getVipStatus() {
		return vipStatus;
	}
	public void setVipStatus(String vipStatus) {
		this.vipStatus = vipStatus;
	}
	public String getWdIsRead() {
		return wdIsRead;
	}
	public void setWdIsRead(String wdIsRead) {
		this.wdIsRead = wdIsRead;
	}
	public String getMjIsRead() {
		return mjIsRead;
	}
	public void setMjIsRead(String mjIsRead) {
		this.mjIsRead = mjIsRead;
	}
	public String getRelationShip() {
		return relationShip;
	}
	public void setRelationShip(String relationShip) {
		this.relationShip = relationShip;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public Date getSiteReportTime() {
		return siteReportTime;
	}
	public void setSiteReportTime(Date siteReportTime) {
		this.siteReportTime = siteReportTime;
	}
	public Integer getIsElseCustomer() {
		return isElseCustomer;
	}
	public void setIsElseCustomer(Integer isElseCustomer) {
		this.isElseCustomer = isElseCustomer;
	}

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getQuestionaireExchangeCutString() {
		return questionaireExchangeCutString;
	}

	public void setQuestionaireExchangeCutString(
			String questionaireExchangeCutString) {
		this.questionaireExchangeCutString = questionaireExchangeCutString;
	}
}
