package net.ytoec.eop.app.bean;

import java.io.Serializable;

public class AppTestResponse implements Serializable{
	
	private static final long serialVersionUID = 1L;


    private Integer id;

    private Integer appTestId;

    private String txLogisticsId;

    private String operType;
    
  //报文操作类型：1－接单；2－接收成功；3－签收成功；
    public static final String OPER_TYPE_RECEIVE="1";
    public static final String OPER_TYPE_RECERVIE_SUCCESS="2";
    public static final String OPER_TYPE_SIGN_SUCCESS="3";

    private String status;
  //状态：0－失败；1－成功；
    public static final String STATUS_SUCCESS="1";
    public static final String STATUS_ERROR="0";

    private String errorMsg;

    private java.util.Date createTime;

    private String remark;
    
    public AppTestResponse(){
    }
    public void setId(Integer id){
    	this.id = id;
    }
    
    public Integer getId( ){
    	return this.id;
    }
    public void setAppTestId(Integer appTestId){
    	this.appTestId = appTestId;
    }
    
    public Integer getAppTestId( ){
    	return this.appTestId;
    }
    public void setTxLogisticsId(String txLogisticsId){
    	this.txLogisticsId = txLogisticsId;
    }
    
    public String getTxLogisticsId( ){
    	return this.txLogisticsId;
    }
    public void setOperType(String operType){
    	this.operType = operType;
    }
    
    public String getOperType( ){
    	return this.operType;
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

	public boolean equals(Object obj){
		if(obj == null){
			return false;
		}
		if(!(obj instanceof AppTestResponse)){
			return false;
		}
		
		AppTestResponse o = (AppTestResponse)obj;
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