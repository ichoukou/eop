package net.ytoec.kernel.dataobject;

import java.util.Date;
import java.util.List;

import net.ytoec.kernel.dto.DtoBranch;

public class ReportIssue {
	private int id;
	private String mailNo;
	private String msgStatus;
	private Date createTime;
	private Date updateTime;
	private String buyerName;
	private String buyerPhone;
	private String buyerMobile;
	private String issueDesc;
	private String receiveBranchId;
	private String attentionFlag;
	private String reportUserId;
	private Integer createUserId;
	
	public Integer getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}
	private DtoBranch branch = new DtoBranch(); //网点信息
	private User seller = new User();
	private List<WarnUpOper> operList;//处理信息（一对多关系）
	
	//控制层的展开和收起
	private String showOper;
	private String allOper;
	
	public List<WarnUpOper> getOperList() {
		return operList;
	}
	public void setOperList(List<WarnUpOper> operList) {
		this.operList = operList;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMailNo() {
		return mailNo;
	}
	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}
	public String getMsgStatus() {
		return msgStatus;
	}
	public void setMsgStatus(String msgStatus) {
		this.msgStatus = msgStatus;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
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
	public String getIssueDesc() {
		return issueDesc;
	}
	public void setIssueDesc(String issueDesc) {
		this.issueDesc = issueDesc;
	}
	public String getReceiveBranchId() {
		return receiveBranchId;
	}
	public void setReceiveBranchId(String receiveBranchId) {
		this.receiveBranchId = receiveBranchId;
	}
	public String getAttentionFlag() {
		return attentionFlag;
	}
	public void setAttentionFlag(String attentionFlag) {
		this.attentionFlag = attentionFlag;
	}
	public String getReportUserId() {
		return reportUserId;
	}
	public void setReportUserId(String reportUserId) {
		this.reportUserId = reportUserId;
	}
	public DtoBranch getBranch() {
		return branch;
	}
	public void setBranch(DtoBranch branch) {
		this.branch = branch;
	}
	public User getSeller() {
		return seller;
	}
	public void setSeller(User seller) {
		this.seller = seller;
	}
	public String getShowOper() {
		return showOper;
	}
	public void setShowOper(String showOper) {
		this.showOper = showOper;
	}
	public String getAllOper() {
		return allOper;
	}
	public void setAllOper(String allOper) {
		this.allOper = allOper;
	}
	
}
