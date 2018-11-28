package net.ytoec.kernel.dataobject;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 运费模板详细数据bean对象<br>
 * 该对象只包含基本的运费信息：首重、超重、地区<br>
 * DB中没有对应的表. 只用来做数据转换. <br>
 * 该对象对应{@link net.ytoec.kernel.dataobject.Posttemp#getPostinfo}中的xml字符串.<br>
 * 
 * @author ChenRen
 * @Date 2011-09-09
 */
public class Postinfo implements Serializable {

	private static Logger logger=LoggerFactory.getLogger(Postinfo.class);
	private static final long serialVersionUID = 5222503392034214140L;
	/** 目的地的省份Id */
	private String destId;
	/** 目的地的省份显示值 */
	private String destText;
	/** 始发地的省份Id */
	private String srcId;
	/** 始发地的省份Id */
	private String srcText;
	/** 首重-标准价 */
	private float fwStandardPrice;
	/** 首重-折扣 */
	private float fwDiscount;
	/** 首重-实收价 */
	private float fwRealPirce;
	/** 超重-标准价 */
	private float owStandardPrice;
	/** 超重-折扣 */
	private float owDiscount;
	/** 超重-实收价 */
	private float owRealPirce;
	
	/** 固定价格 */
	private float fixedPirce;
	
	/** 重量单价 */
	private float weightPirce;	
	
	/** 续重统计单位 */
	private float addWeightChoice;
	
	public float getFirstWeight() {
		return firstWeight;
	}

	public void setFirstWeight(float firstWeight) {
		this.firstWeight = firstWeight;
	}

	/** 最低收费价格 */
	private float floorPirce;
	
	
	/** 首重 */
	private float firstWeight;	
	
	// ->

	public float getFixedPirce() {
		return fixedPirce;
	}

	public void setFixedPirce(float fixedPirce) {
		this.fixedPirce = fixedPirce;
	}

	public float getWeightPirce() {
		return weightPirce;
	}

	public void setWeightPirce(float weightPirce) {
		this.weightPirce = weightPirce;
	}

	public float getFloorPirce() {
		return floorPirce;
	}

	public void setFloorPirce(float floorPirce) {
		this.floorPirce = floorPirce;
	}

	public Postinfo() {
	}

	/**
	 * 首重<br>
	 * 
	 * @deprecated 请参考新字段 {@link #fwRealPirce}
	
	public float getFirstWeightPirce() {
		return firstWeightPirce;
	}
	 */

	/**
	 * 首重<br>
	 * 
	 * @deprecated 请参考新字段 {@link #fwRealPirce}
	public void setFirstWeightPirce(float firstWeightPirce) {
		this.firstWeightPirce = firstWeightPirce;
	}
	 */

	/**
	 * 超重<br>
	 * 
	 * @deprecated 请参考新字段 {@link #owRealPirce}
	public float getOverWeightPirce() {
		return overWeightPirce;
	}
	 */

	/**
	 * 超重<br>
	 * 
	 * @deprecated 请参考新字段 {@link #owRealPirce}
	public void setOverWeightPirce(float overWeightPirce) {
		this.overWeightPirce = overWeightPirce;
	}

	public String getProvs() {
		return provs;
	}

	public void setProvs(String provs) {
		this.provs = provs;
	}
	 */

	public float getFwStandardPrice() {
		return fwStandardPrice;
	}

	public void setFwStandardPrice(float fwStandardPrice) {
		this.fwStandardPrice = fwStandardPrice;
	}

	public float getFwDiscount() {
		return fwDiscount;
	}

	public void setFwDiscount(float fwDiscount) {
		this.fwDiscount = fwDiscount;
	}

	public float getFwRealPirce() {
		return fwRealPirce;
	}

	public void setFwRealPirce(float fwRealPirce) {
		this.fwRealPirce = fwRealPirce;
	}

	public float getOwStandardPrice() {
		return owStandardPrice;
	}

	public void setOwStandardPrice(float owStandardPrice) {
		this.owStandardPrice = owStandardPrice;
	}

	public float getOwDiscount() {
		return owDiscount;
	}

	public void setOwDiscount(float owDiscount) {
		this.owDiscount = owDiscount;
	}

	public float getOwRealPirce() {
		return owRealPirce;
	}

	public void setOwRealPirce(float owRealPirce) {
		this.owRealPirce = owRealPirce;
	}

	public String getDestId() {
		return destId;
	}

	public void setDestId(String destId) {
		this.destId = destId;
	}

	public String getDestText() {
		return destText;
	}

	public void setDestText(String destText) {
		this.destText = destText;
	}

	public String getSrcId() {
		return srcId;
	}

	public void setSrcId(String srcId) {
		this.srcId = srcId;
	}

	public String getSrcText() {
		return srcText;
	}

	public void setSrcText(String srcText) {
		this.srcText = srcText;
	}

	public float getAddWeightChoice() {
		return addWeightChoice;
	}

	public void setAddWeightChoice(float addWeightChoice) {
		this.addWeightChoice = addWeightChoice;
	}
	
	


	/**
	 * 实现一个排序的方法；让postinfo按地区排序，实现多个无序的postinfo，地区为other的排最下面
	@Override
	public int compareTo(Postinfo pi) {
		// 地区：其他
		// if(pi.getProvs().indexOf("other") > -1) {
		// return -1;
		// }
		if (this.getProvs().indexOf("other") > -1) {
			return 1;
		}

		return 0;
	}
	 */

	/*
	 * public static void main(String[] args) { Postinfo p1 = new Postinfo();
	 * p1.setProvs("other"); Postinfo p2 = new Postinfo(); p2.setProvs("上海市");
	 * Postinfo p3 = new Postinfo(); p3.setProvs("湖南省,湖北省"); Postinfo p4 = new
	 * Postinfo(); p4.setProvs("北京市");
	 * 
	 * List<Postinfo> list = new ArrayList(); list.add(p3); list.add(p2);
	 * list.add(p4); list.add(p1);
	 * 
	 * for (Object obj : list) { logger.debug(obj); } Collections.sort(list);
	 * logger.debug("=============="); for (Object obj : list) {
	 * logger.debug(obj); } /* 输出结果： 首重：0.0; 超重：0.0; 地区：湖南省,湖北省 首重：0.0; 超重：0.0;
	 * 地区：上海市 首重：0.0; 超重：0.0; 地区：北京市 首重：0.0; 超重：0.0; 地区：other ==============
	 * 首重：0.0; 超重：0.0; 地区：湖南省,湖北省 首重：0.0; 超重：0.0; 地区：上海市 首重：0.0; 超重：0.0; 地区：北京市
	 * 首重：0.0; 超重：0.0; 地区：other
	 */
	// }

}
