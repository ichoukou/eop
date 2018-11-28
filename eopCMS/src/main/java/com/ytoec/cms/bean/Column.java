package com.ytoec.cms.bean;

import java.io.Serializable;
import java.util.List;

public class Column implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public final static Integer STATUS_UNAPPROVE = 1;
	public final static Integer STATUS_NORMAL = 2; 
	public final static Integer STATUS_DELETE = 3; 

    private Integer columnId;

    private String columnName;

    private String columnCode;

    private Integer parentColumnId;

    private String mediaPath;

    private Integer status;

    private Integer createUser;

    private java.util.Date createTime;

    private Integer updateUser;

    private java.util.Date updateTime;

    private Integer seq;

    private Integer level;

    private Integer rootId;

    private String remark;
    
    private List<Column> items;
    
    public Column(){
    }
    public void setColumnId(Integer columnId){
    	this.columnId = columnId;
    }
    
    public Integer getColumnId( ){
    	return this.columnId;
    }
    public void setColumnName(String columnName){
    	this.columnName = columnName;
    }
    
    public String getColumnName( ){
    	return this.columnName;
    }
    public void setColumnCode(String columnCode){
    	this.columnCode = columnCode;
    }
    
    public String getColumnCode( ){
    	return this.columnCode;
    }
    public void setParentColumnId(Integer parentColumnId){
    	this.parentColumnId = parentColumnId;
    }
    
    public Integer getParentColumnId( ){
    	return this.parentColumnId;
    }
    public void setMediaPath(String mediaPath){
    	this.mediaPath = mediaPath;
    }
    
    public String getMediaPath( ){
    	return this.mediaPath;
    }
    public void setStatus(Integer status){
    	this.status = status;
    }
    
    public Integer getStatus( ){
    	return this.status;
    }
    public void setCreateUser(Integer createUser){
    	this.createUser = createUser;
    }
    
    public Integer getCreateUser( ){
    	return this.createUser;
    }
    public void setCreateTime(java.util.Date createTime){
    	this.createTime = createTime;
    }
    
    public java.util.Date getCreateTime( ){
    	return this.createTime;
    }
    public void setUpdateUser(Integer updateUser){
    	this.updateUser = updateUser;
    }
    
    public Integer getUpdateUser( ){
    	return this.updateUser;
    }
    public void setUpdateTime(java.util.Date updateTime){
    	this.updateTime = updateTime;
    }
    
    public java.util.Date getUpdateTime( ){
    	return this.updateTime;
    }
    public void setSeq(Integer seq){
    	this.seq = seq;
    }
    
    public Integer getSeq( ){
    	return this.seq;
    }
    public void setLevel(Integer level){
    	this.level = level;
    }
    
    public Integer getLevel( ){
    	return this.level;
    }
    public void setRootId(Integer rootId){
    	this.rootId = rootId;
    }
    
    public Integer getRootId( ){
    	return this.rootId;
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
		if(!(obj instanceof Column)){
			return false;
		}
		
		Column o = (Column)obj;
		if(this.columnId == null){
			 return o.getColumnId() == null;
		}
		return this.columnId.equals(o.getColumnId());
	}

	public int hashCode(){
		if(this.getColumnId() == null){
			return super.hashCode();
		}
		return this.getColumnId().hashCode();
	}
	public void setItems(List<Column> items) {
		this.items = items;
	}
	public List<Column> getItems() {
		return items;
	}
}