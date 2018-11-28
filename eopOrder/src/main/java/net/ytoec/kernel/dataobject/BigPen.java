package net.ytoec.kernel.dataobject;

/**
 * 大头笔信息表实体
 * @author mabo
 *
 */
public class BigPen {

	private Integer id;
	
	//地区名
	private String areaName;
	
	//地区中文名
	private String fullName;
	
	//大头笔信息
	private String titleName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
	
	
}
