package net.ytoec.kernel.dataobject;

import java.io.Serializable;

public class Logs implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 *操作日志表-ec_core_logs   id
	 */
	private Integer id;
	
	/**
	 * User 表中的id 与EC_CORE_USER 的id 一致
	 */
	private Integer userId;
	
	 /**(操作名称)
     * 1 、余额不足提醒 2、交易提醒  3、服务到期提醒 4、短信不足提醒5、问题件预警
     * 6、发货提醒 7派件提醒8、短信体验套餐9短信初级短信套餐
     * 10短信中级套餐11短信高级套餐 0 其它
     * 12 短信服务13超时件服务 14支付操作 
     */
	private String operName;
	/**
	 *操作类型	0 关闭 1 开启 2其它delete  update
	 */
	private String operType;
	
	/**
	*创建时间
	*/
	private java.util.Date createTime;
	
	/**
	 *备注
	 */
	private String remark;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	
	public String getOperName() {
		return operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
