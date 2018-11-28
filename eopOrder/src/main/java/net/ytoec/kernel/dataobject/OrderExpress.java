package net.ytoec.kernel.dataobject;

import java.util.Date;
import java.io.Serializable;

public class OrderExpress implements Serializable{
	
	private static final long serialVersionUID = 1L;


    private Integer id;

    private String orderexpressname;

    private Integer shippingmethodid;

    private Integer width;

    private Integer height;

    private String templatedata;

    private String backgroundimageurl;

    private Integer sortorder;

    private Integer status;

    private java.util.Date createtime;

    private java.util.Date updatetime;

    private Integer version;

    private Integer storeid;
    
    public OrderExpress(){
    }
    public void setId(Integer id){
    	this.id = id;
    }
    
    public Integer getId( ){
    	return this.id;
    }
    public void setOrderexpressname(String orderexpressname){
    	this.orderexpressname = orderexpressname;
    }
    
    public String getOrderexpressname( ){
    	return this.orderexpressname;
    }
    public void setShippingmethodid(Integer shippingmethodid){
    	this.shippingmethodid = shippingmethodid;
    }
    
    public Integer getShippingmethodid( ){
    	return this.shippingmethodid;
    }
    public void setWidth(Integer width){
    	this.width = width;
    }
    
    public Integer getWidth( ){
    	return this.width;
    }
    public void setHeight(Integer height){
    	this.height = height;
    }
    
    public Integer getHeight( ){
    	return this.height;
    }
    public void setTemplatedata(String templatedata){
    	this.templatedata = templatedata;
    }
    
    public String getTemplatedata( ){
    	return this.templatedata;
    }
    public void setBackgroundimageurl(String backgroundimageurl){
    	this.backgroundimageurl = backgroundimageurl;
    }
    
    public String getBackgroundimageurl( ){
    	return this.backgroundimageurl;
    }
    public void setSortorder(Integer sortorder){
    	this.sortorder = sortorder;
    }
    
    public Integer getSortorder( ){
    	return this.sortorder;
    }
    public void setStatus(Integer status){
    	this.status = status;
    }
    
    public Integer getStatus( ){
    	return this.status;
    }
    public void setCreatetime(java.util.Date createtime){
    	this.createtime = createtime;
    }
    
    public java.util.Date getCreatetime( ){
    	return this.createtime;
    }
    public void setUpdatetime(java.util.Date updatetime){
    	this.updatetime = updatetime;
    }
    
    public java.util.Date getUpdatetime( ){
    	return this.updatetime;
    }
    public void setVersion(Integer version){
    	this.version = version;
    }
    
    public Integer getVersion( ){
    	return this.version;
    }
    public void setStoreid(Integer storeid){
    	this.storeid = storeid;
    }
    
    public Integer getStoreid( ){
    	return this.storeid;
    }

	public boolean equals(Object obj){
		if(obj == null){
			return false;
		}
		if(!(obj instanceof OrderExpress)){
			return false;
		}
		
		OrderExpress o = (OrderExpress)obj;
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
