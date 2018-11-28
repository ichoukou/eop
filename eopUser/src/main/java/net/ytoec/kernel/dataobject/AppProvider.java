package net.ytoec.kernel.dataobject;

import java.io.Serializable;

public class AppProvider implements Serializable{
	
	private static final long serialVersionUID = 1L;

	//服务商类型:1-电子商务服务平台;2-软件服务商;3-个人开发者
	public static final String TYPE_PLATFORM = "1";
	public static final String TYPE_SOFTWARE = "2";
	public static final String TYPE_DEVELOPER = "3";
	
	//证件类型:1-身份证；2-营业执照
	public static final String PAPER_TYPE_ID = "1";
	public static final String PAPER_TYPE_TRADE = "2";

    private Integer id;

    private String servicesType;

    private String companyName;

    private String companyUrl;
    
    private String companyAddress;

    private String linkman;

    private String paperType;

    private String tradingCertificate;

    private String tradingCertificatePath;

    private String identityCard;

    private String identityCardPath;

    private String servicesAptitude;

    private String customerContact;

    private String customerMail;

    private String customerPhone;

    private Integer userId;

    private java.util.Date createTime;

    private java.util.Date updateTime;

    private String remark;
    
    public AppProvider(){
    }
    public void setId(Integer id){
    	this.id = id;
    }
    
    public Integer getId( ){
    	return this.id;
    }
    public void setServicesType(String servicesType){
    	this.servicesType = servicesType;
    }
    
    public String getServicesType( ){
    	return this.servicesType;
    }
    public void setCompanyName(String companyName){
    	this.companyName = companyName;
    }
    
    public String getCompanyName( ){
    	return this.companyName;
    }
    public void setCompanyUrl(String companyUrl){
    	this.companyUrl = companyUrl;
    }
    
    public String getCompanyUrl( ){
    	return this.companyUrl;
    }
    public void setLinkman(String linkman){
    	this.linkman = linkman;
    }
    
    public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public String getLinkman( ){
    	return this.linkman;
    }
    public void setPaperType(String paperType){
    	this.paperType = paperType;
    }
    
    public String getPaperType( ){
    	return this.paperType;
    }
    public void setTradingCertificate(String tradingCertificate){
    	this.tradingCertificate = tradingCertificate;
    }
    
    public String getTradingCertificate( ){
    	return this.tradingCertificate;
    }
    public void setTradingCertificatePath(String tradingCertificatePath){
    	this.tradingCertificatePath = tradingCertificatePath;
    }
    
    public String getTradingCertificatePath( ){
    	return this.tradingCertificatePath;
    }
    public void setIdentityCard(String identityCard){
    	this.identityCard = identityCard;
    }
    
    public String getIdentityCard( ){
    	return this.identityCard;
    }
    public void setIdentityCardPath(String identityCardPath){
    	this.identityCardPath = identityCardPath;
    }
    
    public String getIdentityCardPath( ){
    	return this.identityCardPath;
    }
    public void setServicesAptitude(String servicesAptitude){
    	this.servicesAptitude = servicesAptitude;
    }
    
    public String getServicesAptitude( ){
    	return this.servicesAptitude;
    }
    public void setCustomerContact(String customerContact){
    	this.customerContact = customerContact;
    }
    
    public String getCustomerContact( ){
    	return this.customerContact;
    }
    public void setCustomerMail(String customerMail){
    	this.customerMail = customerMail;
    }
    
    public String getCustomerMail( ){
    	return this.customerMail;
    }
    public void setCustomerPhone(String customerPhone){
    	this.customerPhone = customerPhone;
    }
    
    public String getCustomerPhone( ){
    	return this.customerPhone;
    }
    public void setUserId(Integer userId){
    	this.userId = userId;
    }
    
    public Integer getUserId( ){
    	return this.userId;
    }
    public void setCreateTime(java.util.Date createTime){
    	this.createTime = createTime;
    }
    
    public java.util.Date getCreateTime( ){
    	return this.createTime;
    }
    public void setUpdateTime(java.util.Date updateTime){
    	this.updateTime = updateTime;
    }
    
    public java.util.Date getUpdateTime( ){
    	return this.updateTime;
    }
    public void setRemark(String remark){
    	this.remark = remark;
    }
    
    public String getRemark( ){
    	return this.remark;
    }

	public boolean equals(Object obj){
		if(obj == null){
			return false;
		}
		if(!(obj instanceof AppProvider)){
			return false;
		}
		
		AppProvider o = (AppProvider)obj;
		if(this.id == null){
			 return o.getId() == null;
		}
		return this.id.equals(o.getId());
	}

	public int hashCode(){
		if(this.getId() == null){
			return super.hashCode();
		}
		return this.getId().hashCode();
	}
}