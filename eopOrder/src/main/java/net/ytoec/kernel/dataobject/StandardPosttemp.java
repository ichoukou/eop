package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.Date;

import net.ytoec.kernel.common.Resource;

/**
 * 标准运费模版
 */
public class StandardPosttemp implements Serializable {

	private static final long serialVersionUID = 660923831590532123L;
	private int id;

	/** 始发地省份id */
	private int sourceId;

	private String standardPrice;

	private String continuationPrice;

	/** 固定价格 */
	private String fixedPirce;

	/** 重量单价 */
	private String weightPirce;

	/** 最低收费价格 */
	private String floorPirce;

	/** 首重 */
	private String firstWeight;

	public String getFirstWeight() {
		return firstWeight;
	}

	public void setFirstWeight(String firstWeight) {
		this.firstWeight = firstWeight;
	}

	/** 目的地省份id */
	private int destId;

	private String name;

	private String remark;

	private Date createTime;
	private Date updateTime;

	// ->
	// @ 2011-11-08/ChenRen 新增属性
	// @ 用于页面要显示de省份名称
	/**
	 * 始发地省份的显示名称<br>
	 * 数据库中没有对应字段，在service中调用{@link Resource#getNameById(Integer)}设置值
	 */
	private String srcText;
	/**
	 * 目的地省份的显示名称<br>
	 * 数据库中没有对应字段，在service中调用{@link Resource#getNameById(Integer)}设置值
	 */
	private String destText;

	// ->

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	public String getStandardPrice() {
		return standardPrice;
	}

	public void setStandardPrice(String standardPrice) {
		this.standardPrice = standardPrice;
	}

	public int getDestId() {
		return destId;
	}

	public void setDestId(int destId) {
		this.destId = destId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getContinuationPrice() {
		return continuationPrice;
	}

	public void setContinuationPrice(String continuationPrice) {
		this.continuationPrice = continuationPrice;
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

	public String getSrcText() {
		return srcText;
	}

	public void setSrcText(String srcText) {
		this.srcText = srcText;
	}

	public String getDestText() {
		return destText;
	}

	public void setDestText(String destText) {
		this.destText = destText;
	}

	public String getFixedPirce() {
		return fixedPirce;
	}

	public void setFixedPirce(String fixedPirce) {
		this.fixedPirce = fixedPirce;
	}

	public String getWeightPirce() {
		return weightPirce;
	}

	public void setWeightPirce(String weightPirce) {
		this.weightPirce = weightPirce;
	}

	public String getFloorPirce() {
		return floorPirce;
	}

	public void setFloorPirce(String floorPirce) {
		this.floorPirce = floorPirce;
	}

}
