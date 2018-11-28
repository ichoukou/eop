package com.ytoec.cms.bean;

import java.io.Serializable;

public class Article implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 文章状态：1－未审核；2－正常（已审核已发布）；3－已删除     
	 */
	public static final Integer STATUS_UNAPPROVE = 1;
	public static final Integer STATUS_NORMAL = 2;
	public static final Integer STATUS_DELETE = 3;
	

    private Integer articleId;

    private String title;

    private Integer columnId;
    
    /**
     * 文章允许的易通平台读者类型，多个类型用","隔开。
     * 0－所有用户，1-卖家；2-网点；3－大商家；4－管理员；
     * 
     */
    private String readerType;

    private String content;

    private Integer status;

    private Integer createUser;

    private java.util.Date createTime;

    private Integer updateUser;

    private java.util.Date updateTime;
    
    private Integer seq;

    private String remark;
    
    public Article(){
    }
    public void setArticleId(Integer articleId){
    	this.articleId = articleId;
    }
    
    public Integer getArticleId( ){
    	return this.articleId;
    }
    public void setTitle(String title){
    	this.title = title;
    }
    
    public String getTitle( ){
    	return this.title;
    }
    public void setColumnId(Integer columnId){
    	this.columnId = columnId;
    }
    
    public Integer getColumnId( ){
    	return this.columnId;
    }
    public void setContent(String content){
    	this.content = content;
    }
    
    public String getContent( ){
    	return this.content;
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
    public void setRemark(String remark){
    	this.remark = remark;
    }
    
    public String getRemark( ){
    	return this.remark;
    }

	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public Integer getSeq() {
		return seq;
	}
	public boolean equals(Object obj){
		if(obj == null){
			return false;
		}
		if(!(obj instanceof Article)){
			return false;
		}
		
		Article o = (Article)obj;
		if(this.articleId == null){
			 return o.getArticleId() == null;
		}
		return this.articleId.equals(o.getArticleId());
	}

	public int hashCode(){
		if(this.getArticleId() == null){
			return super.hashCode();
		}
		return this.getArticleId().hashCode();
	}
	public void setReaderType(String readerType) {
		this.readerType = readerType;
	}
	public String getReaderType() {
		return readerType;
	}
}