package net.ytoec.kernel.dataobject;
/**
 * 打印快递单中自定义的实体
 * orderprint 和 模版中的字段相对应
 * @author wus
 *
 */
public class PrintTemp {
	
	/**
	 * 网店名称
	 */
	private String storeName;
	
	/**
	 * 收货人姓名
	 */
	private String shipName;
	
	/**
	 * 收货人地区
	 */
	private String shipRegionFullName;
	
	/**
	 * 收货人地址
	 */
	private String shipAddress;
	
	/**
	 * 收货人-电话
	 */
	private String shipTel;
	
	/**
	 * 收货人-邮编
	 */
	private String shipZip;
	
	/**
	 * 发货人-姓名
	 */
	private String dlyName;
	
	/**
	 * 发货人-地区
	 */
	private String dlyRegionFullName;
	
	/**
	 * 发货人-地址
	 */
	private String dlyAddress;
	
	/**
	 * 发货人-邮编
	 */
	private String dlyZip;
	
	/**
	 * 发货人-手机
	 */
	private String dlyMobile;
	
	/**
	 * 发货人-电话
	 */
	private String dlyTel;
	
	/**
	 * 当前日期-年
	 */
	private String dateYear;
	
	/**
	 * 当前日期-月
	 */
	private String dateMoth;
	
	/**
	 * 当前日期-日
	 */
	private String dateDay;
	
	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 面单号
	 */
	private String mailNo;
	
	/**
	 * 发货单-物品数量
	 */
	private String shipmentItemCount;
	
	/**
	 *  是否选中
	 */
	private String tick;
	
	/**
	 * 自定义内容
	 */
	private String text;

	/**
	 * 发货单号
	 * @return
	 */
	private String shipmentNo;
	
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getShipName() {
		return shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName;
	}

	public String getShipRegionFullName() {
		return shipRegionFullName;
	}

	public void setShipRegionFullName(String shipRegionFullName) {
		this.shipRegionFullName = shipRegionFullName;
	}

	public String getShipAddress() {
		return shipAddress;
	}

	public void setShipAddress(String shipAddress) {
		this.shipAddress = shipAddress;
	}

	public String getShipTel() {
		return shipTel;
	}

	public void setShipTel(String shipTel) {
		this.shipTel = shipTel;
	}

	public String getShipZip() {
		return shipZip;
	}

	public void setShipZip(String shipZip) {
		this.shipZip = shipZip;
	}

	public String getDlyName() {
		return dlyName;
	}

	public void setDlyName(String dlyName) {
		this.dlyName = dlyName;
	}

	public String getDlyRegionFullName() {
		return dlyRegionFullName;
	}

	public void setDlyRegionFullName(String dlyRegionFullName) {
		this.dlyRegionFullName = dlyRegionFullName;
	}

	public String getDlyAddress() {
		return dlyAddress;
	}

	public void setDlyAddress(String dlyAddress) {
		this.dlyAddress = dlyAddress;
	}

	public String getDlyZip() {
		return dlyZip;
	}

	public void setDlyZip(String dlyZip) {
		this.dlyZip = dlyZip;
	}

	public String getDlyMobile() {
		return dlyMobile;
	}

	public void setDlyMobile(String dlyMobile) {
		this.dlyMobile = dlyMobile;
	}

	public String getDlyTel() {
		return dlyTel;
	}

	public void setDlyTel(String dlyTel) {
		this.dlyTel = dlyTel;
	}

	public String getDateYear() {
		return dateYear;
	}

	public void setDateYear(String dateYear) {
		this.dateYear = dateYear;
	}

	public String getDateMoth() {
		return dateMoth;
	}

	public void setDateMoth(String dateMoth) {
		this.dateMoth = dateMoth;
	}

	public String getDateDay() {
		return dateDay;
	}

	public void setDateDay(String dateDay) {
		this.dateDay = dateDay;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public String getShipmentItemCount() {
		return shipmentItemCount;
	}

	public void setShipmentItemCount(String shipmentItemCount) {
		this.shipmentItemCount = shipmentItemCount;
	}

	public String getTick() {
		return tick;
	}

	public void setTick(String tick) {
		this.tick = tick;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getShipmentNo() {
		return shipmentNo;
	}

	public void setShipmentNo(String shipmentNo) {
		this.shipmentNo = shipmentNo;
	}
}
