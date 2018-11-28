/**
 * 2012-5-14下午06:54:54
 * wangyong
 */
package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.Date;

/**
 * 存放异常订单、特殊订单、调整订单记录
 * @author wangyong
 * 2012-5-14
 */
public class TempOrder implements Serializable {

	/**
	 * 2012-5-14
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private Integer orderId;
	
	private Integer tempType;//1、代表异常订单2、代表调整订单3、代表退货订单
	
	private String tempKey;
	
	private Date createTime;
	
	private Date updateTime;
	
	private String backup;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getTempType() {
		return tempType;
	}

	public void setTempType(Integer tempType) {
		this.tempType = tempType;
	}

	public String getTempKey() {
		return tempKey;
	}

	public void setTempKey(String tempKey) {
		this.tempKey = tempKey;
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

	public String getBackup() {
		return backup;
	}

	public void setBackup(String backup) {
		this.backup = backup;
	}
	
}
