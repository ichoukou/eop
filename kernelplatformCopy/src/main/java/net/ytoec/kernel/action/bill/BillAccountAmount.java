package net.ytoec.kernel.action.bill;

public class BillAccountAmount {
//	未支付总票数
	private Integer unpaidNumAmount;
//	未支付总金额
	private double unpaidMoneyAmount;
//	已支付总票数
	private Integer paidNumAmount; 
//	已支付总金额
	private double paidMoneyAmount;
//	未确认总票数
	private Integer unconfirmedNumAmount;
//	未确认总金额
	private double unconfirmedMoneyAmount;
//	已确认总票数
	private Integer confirmedNumAmount;
//	已确认总金额
	private double confirmedMoneyAmount;
	
	
	public BillAccountAmount() {
		super();
	}
	public Integer getUnpaidNumAmount() {
		return unpaidNumAmount;
	}
	public void setUnpaidNumAmount(Integer unpaidNumAmount) {
		this.unpaidNumAmount = unpaidNumAmount;
	}
	public double getUnpaidMoneyAmount() {
		return unpaidMoneyAmount;
	}
	public void setUnpaidMoneyAmount(double unpaidMoneyAmount) {
		this.unpaidMoneyAmount = unpaidMoneyAmount;
	}
	public Integer getPaidNumAmount() {
		return paidNumAmount;
	}
	public void setPaidNumAmount(Integer paidNumAmount) {
		this.paidNumAmount = paidNumAmount;
	}
	public double getPaidMoneyAmount() {
		return paidMoneyAmount;
	}
	public void setPaidMoneyAmount(double paidMoneyAmount) {
		this.paidMoneyAmount = paidMoneyAmount;
	}
	public Integer getUnconfirmedNumAmount() {
		return unconfirmedNumAmount;
	}
	public void setUnconfirmedNumAmount(Integer unconfirmedNumAmount) {
		this.unconfirmedNumAmount = unconfirmedNumAmount;
	}
	public double getUnconfirmedMoneyAmount() {
		return unconfirmedMoneyAmount;
	}
	public void setUnconfirmedMoneyAmount(double unconfirmedMoneyAmount) {
		this.unconfirmedMoneyAmount = unconfirmedMoneyAmount;
	}
	public Integer getConfirmedNumAmount() {
		return confirmedNumAmount;
	}
	public void setConfirmedNumAmount(Integer confirmedNumAmount) {
		this.confirmedNumAmount = confirmedNumAmount;
	}
	public double getConfirmedMoneyAmount() {
		return confirmedMoneyAmount;
	}
	public void setConfirmedMoneyAmount(double confirmedMoneyAmount) {
		this.confirmedMoneyAmount = confirmedMoneyAmount;
	}
	
	

}
