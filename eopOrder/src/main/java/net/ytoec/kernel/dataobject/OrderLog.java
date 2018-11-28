package net.ytoec.kernel.dataobject;

import java.util.Date;

public class OrderLog {

	private String txLogisticId;
	private String payAmount;
	private Date payTime;
	private String unitId;
	private String employeeId;
	private String remark;
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTxLogisticId() {
		return txLogisticId;
	}
	public void setTxLogisticId(String txLogisticId) {
		this.txLogisticId = txLogisticId;
	}
	public String getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
}
