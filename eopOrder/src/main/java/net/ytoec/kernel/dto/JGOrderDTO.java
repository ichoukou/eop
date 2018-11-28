package net.ytoec.kernel.dto;

import java.util.Date;

/**
 * description     从金刚获得订单数据的实体类
 * created by      hufei@2013-04-18
 *
 */
public class JGOrderDTO {
	
	/**
	 * 订单下单类型:正常订单.
	 */
	public static final int ORDERTYPE_0 = 0;
	/**
	 * 订单下单类型:异常订单.
	 */
	public static final int logisticProviderIdEmpty = 1;
	public static final int txLogisticIdEmpty = 2;
	public static final int traderInfoNameEmpty = 3;
	public static final int traderInfoAddressEmpty = 4;
	public static final int traderInfoMobileandPhoneEmpty = 5;
	public static final int traderInfoCityEmpty = 6;
	public static final int traderInfoProvEmpty = 7;
	public static final int receiverMobileandPhoneEmpty = 8;
	public static final int getIsOfflineandmailnoisnullandisPrint = 9;
	public static final int unknowerror=10; 
	private String id;
	private Integer commandType;
	private String createTime;
	private String orderLogisticsCode;
	private String orderChannelCode;
	private Integer status;
	private Integer isOffline;
	private String commendContent;
	private int orderType;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getCommandType() {
		return commandType;
	}
	public void setCommandType(Integer commandType) {
		this.commandType = commandType;
	}
	
	public String getOrderLogisticsCode() {
		return orderLogisticsCode;
	}
	public void setOrderLogisticsCode(String orderLogisticsCode) {
		this.orderLogisticsCode = orderLogisticsCode;
	}
	public String getOrderChannelCode() {
		return orderChannelCode;
	}
	public void setOrderChannelCode(String orderChannelCode) {
		this.orderChannelCode = orderChannelCode;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getIsOffline() {
		return isOffline;
	}
	public void setIsOffline(Integer isOffline) {
		this.isOffline = isOffline;
	}
	public String getCommendContent() {
		return commendContent;
	}
	public void setCommendContent(String commendContent) {
		this.commendContent = commendContent;
	}
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}
