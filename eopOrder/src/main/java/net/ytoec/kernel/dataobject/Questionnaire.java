/**
 * 
 */
package net.ytoec.kernel.dataobject;

import java.util.Date;
import java.util.List;

/**
 * 问题单处理bean对象
 * @author Wangyong
 * @2011-8-1
 * net.ytoec.kernel.dataobject
 */
public class Questionnaire {

	//问题单主键
	private Integer id;
	//物流运单号
	private String mailNO;
	/**
	 * 金刚推送问题件最终处理状态：根据统计标识。
	 *  10：已揽收
	 *	20：中转
	 *	30：换单
	 *	35：派送
	 *	40：派送成功
	 *	39：派送失败
	 *	50：签单返回
	 */
	private String mailType;
	//寄件时间
	private Date senderTime;
	//处理状态:默认未通知（1），处理后更改为已通知（2），其他（3）
	private String dealStatus;
	
	//处理状态:1-未处理；2－已处理；3－其它
	public static final String DEAL_STATUS_UNDO = "1";
	public static final String DEAL_STATUS_DID = "2";
	public static final String DEAL_STATUS_OTHER = "3";
	
	//处理信息
	private String dealInfo;
	/**
	 * 新增以下三个字段:
	 */
	/**
	 * 卖家问题件状态:未处理（1），处理中（2），已处理（3）（注：卖家只关注网点已通知状态下的问题件）
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
	
	/**问题件类型。从金刚同步过来的数据。
	 *  PR11	快件到我公司已破损
	 *	PR100	有进无出
	 *	PR110	有出无进
	 *	PR120	有单无货
	 *	PR130	有货无单
	 *	PR210	收件客户拒收
	 *	PR211	地址不详电话联系不上
	 *	PR212	收件客户已离职
	 *	PR213	收件客户要求改地址
	 *	PR214	地址不详且电话为传真或无人接听
	 *	PR215	地址不详电话与收件客户本人不符
	 *	PR216	地址不详无收件人的电话
	 *	PR220	错发
	 *	PR230	延误
	 *	PR240	遗失
	 *	PR250	违禁品
	 *	PR260	快件污染
	 *	PR50	到付费
	 *	PR60	代收款
	 *	PR270	其它原因
	 *	PR001	无头件上报
	 *	PR002	面单填写不规范
	 *	R70	           节假日客户休息
	 *  PR10	破损
	 *	PR20	超区
	 *	PR30	内件短少
	 *	PR40	超重
	 *  PR140	签收失败（自动上报）
	 */
	private String feedbackInfo;
	//揽收网点id;
	private String branchId;
	//创建时间
	private Date createTime;
	//处理人
	private Integer dealUserId;
	//处理时间
	private Date dealTime;
	
	//备用字段
	private String backupInfo;
	
	/** 问题id, 直接同步金刚表的值 */
	private String issueId;
	/** 问题描述 */
	private String issueDesc;
	/** 问题状态. 直接从金刚同步过来的数据.PD10/未处理；PD20/处理中；PD30/处理完成；PD40/取消； */
	private String issueStatus;
	/** 揽收网点 */
	private String branckText;
	/** 上报人 */
	private String issueCreateUserText;
	/** 上报时间 */
	private Date issueCreateTime;
	/** 接收网点 */
	private String recBranckText;
	/** 上报网点 */
	private String reportBranckText;
	/** 上报网点编码 */
	private String reportBranckCode;
	/** 替换vipId的作用. 通过mailNo从order表中通过过来  */
	private String customerId;
	
	/**
	 * 表中新增buyer_phone、buyer_mobile、buyer_name、IMG1、IMG2、IMG3、IMG4；并删除了
	 * destination、vip_id、vip_name、vip_textname、vip_phone、vip_cellphone、
	 * buy_username、buy_userphone、province、city;
	 * country、address字段
	 * 2012-05-30
	 * @author wangyong
	 */
	//	
	private String buyerPhone;
	//买家电话
	private String buyerMobile;
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
	 * 处理内容的list. 显示数据的时候实时从金刚的表中查询的数据.<br>
	 * {@link QuestionnaireDeal}
	 */
	private List<QuestionnaireDeal> quesDealList;
	
	/** 客户信息 */
	private User customer;
	
	/** 分区字段 */
	private Date partitionDate;
	
	/** 问题件标签id */
	private Integer tagId;
	
	private String otherPartitionDate;
	
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
	public String getMailType() {
		return mailType;
	}
	public void setMailType(String mailType) {
		this.mailType = mailType;
	}
	public Date getSenderTime() {
		return senderTime;
	}
	public void setSenderTime(Date senderTime) {
		this.senderTime = senderTime;
	}
	public String getDealStatus() {
		return dealStatus;
	}
	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}
	public String getDealInfo() {
		return dealInfo;
	}
	public void setDealInfo(String dealInfo) {
		this.dealInfo = dealInfo;
	}
	public String getFeedbackInfo() {
		return feedbackInfo;
	}
	public void setFeedbackInfo(String feedbackInfo) {
		this.feedbackInfo = feedbackInfo;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getDealUserId() {
		return dealUserId;
	}
	public void setDealUserId(Integer dealUserId) {
		this.dealUserId = dealUserId;
	}
	public Date getDealTime() {
		return dealTime;
	}
	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}
	public String getBackupInfo() {
		return backupInfo;
	}
	public void setBackupInfo(String backupInfo) {
		this.backupInfo = backupInfo;
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
	public void setIssueDesc(String issueDesc) {
		this.issueDesc = issueDesc;
	}
	public String getBranckText() {
		return branckText;
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
	public String getReportBranckCode() {
		return reportBranckCode;
	}
	public void setReportBranckCode(String reportBranckCode) {
		this.reportBranckCode = reportBranckCode;
	}
	public String getIssueStatus() {
		return issueStatus;
	}
	public void setIssueStatus(String issueStatus) {
		this.issueStatus = issueStatus;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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
	public String getBuyerPhone() {
		return buyerPhone;
	}
	public void setBuyerPhone(String buyerPhone) {
		this.buyerPhone = buyerPhone;
	}
	public String getBuyerMobile() {
		return buyerMobile;
	}
	public void setBuyerMobile(String buyerMobile) {
		this.buyerMobile = buyerMobile;
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
	public Date getPartitionDate() {
		return partitionDate;
	}
	public void setPartitionDate(Date partitionDate) {
		this.partitionDate = partitionDate;
	}
	public Integer getTagId() {
		return tagId;
	}
	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}
	public String getOtherPartitionDate() {
		return otherPartitionDate;
	}
	public void setOtherPartitionDate(String otherPartitionDate) {
		this.otherPartitionDate = otherPartitionDate;
	}
}
