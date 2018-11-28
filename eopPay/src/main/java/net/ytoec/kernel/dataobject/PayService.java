package net.ytoec.kernel.dataobject;

import java.io.Serializable;

public class PayService implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 和ec_core_payService表主键保持一致
	 */
	private Integer id;
	
	/**
	 * 服务名
	 */
	private String name;

	/**
	 * 单价（月）
	 */
	private Double unitPrice;
	
	/**
	 *是否可用	0 否 1 是
	 */
	private String flag;
	
	/**
	 *服务类型	0 免费服务 1短信服务 2 其它
	 */
	private String serviceType;
	
	
	/**
	 *备注
	 */
	private String remark;
	

	/**
	*创建时间
	*/
	private java.util.Date createTime;
	/**
	*修改时间
	*/
	private java.util.Date updateTime;
	
	/**
	*是否关闭(付款) 不映射到数据库
	*/
	private String isShow;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	public java.util.Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null){
			return false;
		}else if(obj instanceof PayService){
			PayService payService = (PayService)obj;
			if(payService.id == this.id){
				return true;
			}
		}
		return false;
	}
	@Override
	public int hashCode() {
		return id.hashCode();
	}
    
}
