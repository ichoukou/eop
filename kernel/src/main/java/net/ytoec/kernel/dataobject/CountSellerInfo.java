package net.ytoec.kernel.dataobject;

import java.util.Date;

/**
 * 统计卖家信息实体
 * 
 * @author huangtianfu
 * 
 */
public class CountSellerInfo {

	private int sid;// 主键

	private String phone;// 电话号码

	private int buyNum;// 购买次数

	private Date createTime;// 创建时间

	private Date updateTime;// 更新时间

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
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
