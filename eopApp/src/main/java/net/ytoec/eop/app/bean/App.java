package net.ytoec.eop.app.bean;

import java.io.Serializable;

import net.ytoec.kernel.dataobject.AppProvider;

public class App implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 应用状态常量定义:1-开发中
	 */
	public static final String STATUS_DEVING = "1";
	/**
	 * 应用状态常量定义:2-测试中
	 */
	public static final String STATUS_TESTING = "2";
	/**
	 * 应用状态常量定义:3-审核中
	 */
	public static final String STATUS_APPROVING = "3";
	/**
	 * 应用状态常量定义:4-应用中
	 */
	public static final String STATUS_IN_USE = "4";
	/**
	 * 应用状态常量定义:5-已禁用
	 */
	public static final String STATUS_LOCKED = "5";

    private Integer id;

    private String appKey;

    private String appName;

    private String secret;

    private String clientid;

    private String apptype;

    private String appstatus;

    private String testAppurl;

    private String testCallbackUrl;

    private String appurl;

    private String callbackUrl;

    private String operatemode;

    private String applogo;

    private String appdetails;

    private String message;

    private Integer userId;

    private String account;

    private java.util.Date createTime;

    private java.util.Date updateTime;

    private String remark;
    
    private AppProvider provider;
    
    public App(){
    }
    public void setId(Integer id){
    	this.id = id;
    }
    
    public Integer getId( ){
    	return this.id;
    }
    public void setAppKey(String appKey){
    	this.appKey = appKey;
    }
    
    public String getAppKey( ){
    	return this.appKey;
    }
    public void setAppName(String appName){
    	this.appName = appName;
    }
    
    public String getAppName( ){
    	return this.appName;
    }
    public void setSecret(String secret){
    	this.secret = secret;
    }
    
    public String getSecret( ){
    	return this.secret;
    }
    public void setClientid(String clientid){
    	this.clientid = clientid;
    }
    
    public String getClientid( ){
    	return this.clientid;
    }
    public void setApptype(String apptype){
    	this.apptype = apptype;
    }
    
    public String getApptype( ){
    	return this.apptype;
    }
    public void setAppstatus(String appstatus){
    	this.appstatus = appstatus;
    }
    
    public String getAppstatus( ){
    	return this.appstatus;
    }
    public void setTestAppurl(String testAppurl){
    	this.testAppurl = testAppurl;
    }
    
    public String getTestAppurl( ){
    	return this.testAppurl;
    }
    public void setTestCallbackUrl(String testCallbackUrl){
    	this.testCallbackUrl = testCallbackUrl;
    }
    
    public String getTestCallbackUrl( ){
    	return this.testCallbackUrl;
    }
    public void setAppurl(String appurl){
    	this.appurl = appurl;
    }
    
    public String getAppurl( ){
    	return this.appurl;
    }
    public void setCallbackUrl(String callbackUrl){
    	this.callbackUrl = callbackUrl;
    }
    
    public String getCallbackUrl( ){
    	return this.callbackUrl;
    }
    public void setOperatemode(String operatemode){
    	this.operatemode = operatemode;
    }
    
    public String getOperatemode( ){
    	return this.operatemode;
    }
    public void setApplogo(String applogo){
    	this.applogo = applogo;
    }
    
    public String getApplogo( ){
    	return this.applogo;
    }
    public void setAppdetails(String appdetails){
    	this.appdetails = appdetails;
    }
    
    public String getAppdetails( ){
    	return this.appdetails;
    }
    public void setMessage(String message){
    	this.message = message;
    }
    
    public String getMessage( ){
    	return this.message;
    }
    public void setUserId(Integer userId){
    	this.userId = userId;
    }
    
    public Integer getUserId( ){
    	return this.userId;
    }
    public void setAccount(String account){
    	this.account = account;
    }
    
    public String getAccount( ){
    	return this.account;
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
		if(!(obj instanceof App)){
			return false;
		}
		
		App o = (App)obj;
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
	public void setProvider(AppProvider provider) {
		this.provider = provider;
	}
	public AppProvider getProvider() {
		return provider;
	}
}