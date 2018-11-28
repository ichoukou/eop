package net.ytoec.kernel.dataobject;

import java.util.Date;

public class WarnValue {
	private Integer id;
	private String sellerId;
	private String destination;
	private String warnValue;
	private Date createTime;
	private Date updateTime;
	private String extends1;
	private Integer begin;
	private Integer end;
	
	public Integer getBegin() {
		return begin;
	}
	public void setBgin(Integer begin) {
		this.begin = begin;
	}
	public Integer getEnd() {
		return end;
	}
	public void setEnd(Integer end) {
		this.end = end;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getWarnValue() {
		return warnValue;
	}
	public void setWarnValue(String warnValue) {
		this.warnValue = warnValue;
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
	public String getExtends1() {
		return extends1;
	}
	public void setExtends1(String extends1) {
		this.extends1 = extends1;
	}
	
	
}
