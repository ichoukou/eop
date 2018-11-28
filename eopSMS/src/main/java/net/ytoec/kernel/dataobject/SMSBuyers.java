package net.ytoec.kernel.dataobject;



import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 会员管理实体类
 *
 */
public class SMSBuyers implements Serializable {
	 /**
	 * 
	 */
	private static final long serialVersionUID = -38714629096075127L;
	
	 private Integer id;                 
	
	 private String buyerAccount;   	//买家的登录帐号    
	 
	 private String receiverName;       //收货人姓名
	 
	 private String receiverMobile;     //收货人手机号码
	 
	 private String receiverPhone;     //收货人固话
	 
	 private String receiverPostcode;   //收货人邮编
	 
	 private String receiverProvince;   //收货人所属省
	 
	 private String receiverCity;    	//收货人所属市
	 
	 private String receiverDistrict;   //收货人所属区
	 
	 private String receiverAddress;    //收货人的具体联系地址
	 
	 private String sourceStatus;       //买家来源：淘宝，本系统中创建，导入等
	 
	 private int marketingSendCount;	//营销活动发送量
	 
	 private int totalTradeCount;    	//总交易量
	 
	 private double totalTradeAmount;   //总交易额，单位为元
	 
	 private Date theLastTradeTime;   //上一次交易时间
	 
	 private Date theLastMarketTime;  // 最后营销时间
	 
	 private int userId;                //主帐号的ID
	 
	 private Date updateTime;         	//修改时间
	 
	 private int updateUserId;       	//修改人
	 
	 private Date createTime;         	//创建时间
	 
	 private int createUserId;       	//创建人
	 
	 private String remark;				//备注
	 
	 private String shopName;			//所属店铺名称
	 
	 
	
	public String getReceiverPhone() {
		return receiverPhone;
	}
	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}
	public Date getTheLastMarketTime() {
		return theLastMarketTime;
	}
	public void setTheLastMarketTime(Date theLastMarketTime) {
		this.theLastMarketTime = theLastMarketTime;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBuyerAccount() {
		return buyerAccount;
	}
	public void setBuyerAccount(String buyerAccount) {
		this.buyerAccount = buyerAccount;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverMobile() {
		return receiverMobile;
	}
	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}
	public String getReceiverPostcode() {
		return receiverPostcode;
	}
	public void setReceiverPostcode(String receiverPostcode) {
		this.receiverPostcode = receiverPostcode;
	}
	public String getReceiverProvince() {
		return receiverProvince;
	}
	public void setReceiverProvince(String receiverProvince) {
		this.receiverProvince = receiverProvince;
	}
	public String getReceiverCity() {
		return receiverCity;
	}
	public void setReceiverCity(String receiverCity) {
		this.receiverCity = receiverCity;
	}
	public String getReceiverDistrict() {
		return receiverDistrict;
	}
	public void setReceiverDistrict(String receiverDistrict) {
		this.receiverDistrict = receiverDistrict;
	}
	public String getReceiverAddress() {
		return receiverAddress;
	}
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public String getSourceStatus() {
		return sourceStatus;
	}
	public void setSourceStatus(String sourceStatus) {
		this.sourceStatus = sourceStatus;
	}
	public int getMarketingSendCount() {
		return marketingSendCount;
	}
	public void setMarketingSendCount(int marketingSendCount) {
		this.marketingSendCount = marketingSendCount;
	}
	public int getTotalTradeCount() {
		return totalTradeCount;
	}
	public void setTotalTradeCount(int totalTradeCount) {
		this.totalTradeCount = totalTradeCount;
	}
	public double getTotalTradeAmount() {
		return totalTradeAmount;
	}
	public void setTotalTradeAmount(double totalTradeAmount) {
		this.totalTradeAmount = totalTradeAmount;
	}

	public Date getTheLastTradeTime() {
		return theLastTradeTime;
	}
	public void setTheLastTradeTime(Date theLastTradeTime) {
		this.theLastTradeTime = theLastTradeTime;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public int getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(int updateUserId) {
		this.updateUserId = updateUserId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}              
	
}