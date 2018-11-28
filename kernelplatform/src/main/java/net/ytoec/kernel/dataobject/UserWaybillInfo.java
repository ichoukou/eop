package net.ytoec.kernel.dataobject;

/**
 * @作者：罗典
 * @描述: 商家面单统计信息
 * @时间：2013-10-22
 * */
public class UserWaybillInfo {
	// 商家编码
	private String customerCode;
	// 商家名称
	private String customerName;
	// 总个数
	private int totalCount;
	// 总箱数
	private double totalBoxNum;
	// 剩余个数
	private int remainCount;
	// 剩余箱数
	private double remainBoxNum = 0;
	// 已使用个数
	private int usedCount;
	// 已使用箱数
	private double usedBoxNum;
	// 可拉取箱数
	private int canUsedBoxNum;

	public int getCanUsedBoxNum() {
		return canUsedBoxNum;
	}

	public void setCanUsedBoxNum(int canUsedBoxNum) {
		this.canUsedBoxNum = canUsedBoxNum;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public double getTotalBoxNum() {
		return totalBoxNum;
	}

	public void setTotalBoxNum(double totalBoxNum) {
		this.totalBoxNum = totalBoxNum;
	}

	public int getRemainCount() {
		return remainCount;
	}

	public void setRemainCount(int remainCount) {
		this.remainCount = remainCount;
	}

	public double getRemainBoxNum() {
		return remainBoxNum;
	}

	public void setRemainBoxNum(double remainBoxNum) {
		this.remainBoxNum = remainBoxNum;
		this.canUsedBoxNum = (int)remainBoxNum;
	}

	public int getUsedCount() {
		return usedCount;
	}

	public void setUsedCount(int usedCount) {
		this.usedCount = usedCount;
	}

	public double getUsedBoxNum() {
		return usedBoxNum;
	}

	public void setUsedBoxNum(double usedBoxNum) {
		this.usedBoxNum = usedBoxNum;
	}

}
