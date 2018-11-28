package net.ytoec.eop.app.bean;

import java.io.Serializable;

public class AppTest implements Serializable{
	
	private static final long serialVersionUID = 1L;


    private Integer id;

    private Integer appId;

    private String clientId;

    private String txLogisticsId;

    private String encodeMsg;

    private String decodeMsg;

    private String msgType;
    
    //报文类型：1-下单；2－更新；3－取消；4－查询
    public static final String MSG_TYPE_CREATE="1";
    public static final String MSG_TYPE_UPDATE="2";
    public static final String MSG_TYPE_CANCEL="3";
    public static final String MSG_TYPE_QUERY="4";

    private String orderType;
    
    //下单类型：在线下单-online；线下下单-offline  
    public static final String ORDER_TYPE_ONLINE="online";
    public static final String ORDER_TYPE_OFFLINE="offline";

    private String status;
    
  //状态：0－失败；1－成功；
    public static final String STATUS_SUCCESS="1";
    public static final String STATUS_ERROR="0";

    private String errorMsg;

    private java.util.Date createTime;

    private String remark;
    
    private App app;
    
    
    public AppTest(){
    }
    public void setId(Integer id){
    	this.id = id;
    }
    
    public Integer getId( ){
    	return this.id;
    }
    public void setAppId(Integer appId){
    	this.appId = appId;
    }
    
    public Integer getAppId( ){
    	return this.appId;
    }
    public void setClientId(String clientId){
    	this.clientId = clientId;
    }
    
    public String getClientId( ){
    	return this.clientId;
    }
    public void setTxLogisticId(String txLogisticsId){
    	this.txLogisticsId = txLogisticsId;
    }
    
    public String getTxLogisticsId( ){
    	return this.txLogisticsId;
    }
    public void setEncodeMsg(String encodeMsg){
    	this.encodeMsg = encodeMsg;
    }
    
    public String getEncodeMsg( ){
    	return this.encodeMsg;
    }
    public void setDecodeMsg(String decodeMsg){
    	this.decodeMsg = decodeMsg;
    }
    
    public String getDecodeMsg( ){
    	return this.decodeMsg;
    }
    public void setMsgType(String msgType){
    	this.msgType = msgType;
    }
    
    public String getMsgType( ){
    	return this.msgType;
    }
    public void setOrderType(String orderType){
    	this.orderType = orderType;
    }
    
    public String getOrderType( ){
    	return this.orderType;
    }
    public void setStatus(String status){
    	this.status = status;
    }
    
    public String getStatus( ){
    	return this.status;
    }
    public void setErrorMsg(String errorMsg){
    	this.errorMsg = errorMsg;
    }
    
    public String getErrorMsg( ){
    	return this.errorMsg;
    }
    public void setCreateTime(java.util.Date createTime){
    	this.createTime = createTime;
    }
    
    public java.util.Date getCreateTime( ){
    	return this.createTime;
    }
    public void setRemark(String remark){
    	this.remark = remark;
    }
    
    public String getRemark( ){
    	return this.remark;
    }

	public void setApp(App app) {
		this.app = app;
	}
	public App getApp() {
		return app;
	}
	public boolean equals(Object obj){
		if(obj == null){
			return false;
		}
		if(!(obj instanceof AppTest)){
			return false;
		}
		
		AppTest o = (AppTest)obj;
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