/**
 * 
 */
package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.Date;

/**
 * 省市县区级联信息表实体
 * @author Wangyong
 */
public class Region implements Serializable {
	
	private static final long serialVersionUID = -7970848646314840509L;
	
	private Integer id;
	private String regionNumber;
	private String regionName;
	private Integer parentId;
	private String remark;
	private Date createTime;
	private Date updateTime;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRegionNumber() {
		return regionNumber;
	}
	public void setRegionNumber(String regionNumber) {
		this.regionNumber = regionNumber;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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

}
