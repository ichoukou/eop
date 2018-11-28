package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.Date;

/**
 * 淘宝任务实体类
 * @author wusha
 *
 */
public class TaobaoTask  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2523185556623027539L;
	
	private Integer id;			// 主键ID
	
	private Integer taskId;		// 淘宝任务ID
	
	private String status;		// 任务状态
	
	private String url;			// 任务完成之后返回的URL
	
	private String startDate;	// 从此时间开始获取订单
	
	private String endDate;		// 获取订单的结束时间
	
	private String flag;	// 是否已下载此订单信息
	
	private Integer userId;	// 创建此任务的主帐号ID
	
	private	Date	createTime;	// 任务创建时间
	
	private Date	updateTime;	// 任务修改时间

	/**
	 * 主键ID
	 * @return
	 */
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 淘宝任务ID
	 * @return
	 */
	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	/**
	 * 任务状态
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 任务完成之后返回的URL
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 从此时间开始获取订单
	 * @return
	 */
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * 获取订单的结束时间
	 * @return
	 */
	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * 是否已下载此订单信息
	 * @return
	 */
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	/**
	 * 创建此任务的主帐号ID
	 * @return
	 */
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * 任务创建时间
	 * @return
	 */
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 任务更新时间
	 * @return
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
