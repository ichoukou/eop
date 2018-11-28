package net.ytoec.kernel.dto;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.Product;

/**
 * 订单打印页面要用到的bean对象<br>
 * 主要包括 {@link Order}对象,{@link Product}对象,收件人信息,发件人信息
 * 
 * @author ChenRen
 * @date 2012-02-22
 */
public class DtoOrderPrint {
	/*
	 * s开头的是卖家 b开头买家 sname spost_code sphone smobile sprov scity sdistrict
	 * saddress bname bpost_code bphone bmobile bprov bcity bdistrict baddress
	 * order_id backup1 backup2 create_time tx_logistic_id 订单号 line_type 线上/线下
	 * client_id 客户代号
	 */
	/** 订单信息对象 */
	private Order order;

	/** 产品信息对象 */
	private List<Product> product;

	// s开头的是卖家
	private String sname;
	/** 邮编 */
	private String spostcode;
	private String sphone;
	private String smobile;
	private String sprov;
	private String scity;
	private String sdistrict;
	private String saddress;
	private String sshopname;// 网店名称

	// b开头买家
	private String bname;
	/** 邮编 */
	private String bpostcode;
	private String bphone;
	private String bmobile;
	private String bprov;
	private String bcity;
	private String bdistrict;
	private String baddress;

	private String backup1;
	private String backup2;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public List<Product> getProduct() {
		return product;
	}

	public void setProduct(List<Product> product) {
		this.product = product;
	}

	public String getSname() {
		return StringUtils.defaultString(sname);
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getSpostcode() {
		return StringUtils.defaultString(spostcode);
	}

	public void setSpostcode(String spostcode) {
		this.spostcode = spostcode;
	}

	public String getSphone() {
		return StringUtils.defaultString(sphone);
	}

	public void setSphone(String sphone) {
		this.sphone = sphone;
	}

	public String getSmobile() {
		return StringUtils.defaultString(smobile);
	}

	public void setSmobile(String smobile) {
		this.smobile = smobile;
	}

	public String getSprov() {
		return StringUtils.defaultString(sprov);
	}

	public void setSprov(String sprov) {
		this.sprov = sprov;
	}

	public String getScity() {
		return StringUtils.defaultString(scity);
	}

	public void setScity(String scity) {
		this.scity = scity;
	}

	public String getSdistrict() {
		return StringUtils.defaultString(sdistrict);
	}

	public void setSdistrict(String sdistrict) {
		this.sdistrict = sdistrict;
	}

	public String getSaddress() {
		return StringUtils.defaultString(saddress);
	}

	public void setSaddress(String saddress) {
		this.saddress = saddress;
	}

	public String getBname() {
		return StringUtils.defaultString(bname);
	}

	public void setBname(String bname) {
		this.bname = bname;
	}

	public String getBpostcode() {
		return StringUtils.defaultString(bpostcode);
	}

	public void setBpostcode(String bpostcode) {
		this.bpostcode = bpostcode;
	}

	public String getBphone() {
		return StringUtils.defaultString(bphone);
	}

	public void setBphone(String bphone) {
		this.bphone = bphone;
	}

	public String getBmobile() {
		return StringUtils.defaultString(bmobile);
	}

	public void setBmobile(String bmobile) {
		this.bmobile = bmobile;
	}

	public String getBprov() {
		return StringUtils.defaultString(bprov);
	}

	public void setBprov(String bprov) {
		this.bprov = bprov;
	}

	public String getBcity() {
		return StringUtils.defaultString(bcity);
	}

	public void setBcity(String bcity) {
		this.bcity = bcity;
	}

	public String getBdistrict() {
		return StringUtils.defaultString(bdistrict);
	}

	public void setBdistrict(String bdistrict) {
		this.bdistrict = bdistrict;
	}

	public String getBaddress() {
		return StringUtils.defaultString(baddress);
	}

	public void setBaddress(String baddress) {
		this.baddress = baddress;
	}

	public String getBackup1() {
		return backup1;
	}

	public void setBackup1(String backup1) {
		this.backup1 = backup1;
	}

	public String getBackup2() {
		return backup2;
	}

	public void setBackup2(String backup2) {
		this.backup2 = backup2;
	}

	public String getSshopname() {
		return StringUtils.defaultString(sshopname);
	}

	public void setSshopname(String sshopname) {
		this.sshopname = sshopname;
	}

}
