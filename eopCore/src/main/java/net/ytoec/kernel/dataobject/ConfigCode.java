package net.ytoec.kernel.dataobject;

import java.io.Serializable;

/**
 * 配置项表
 * 
 * @author ChenRen
 * @date 2011-08-10
 */
public class ConfigCode implements Serializable {

	private static final long serialVersionUID = -277804841057297555L;

	private Integer id;
	private Integer pid;
	/** 唯一字段, key值 */
	private String confKey;
	/** key对应的value值 */
	private String confValue;
	/** 显示值 */
	private String confText;
	
	//配置项类型：1代表单目录项配置。2代表具有多目录项配置。
	private String confType;
	
	/** 排序字段 */
	private Integer seq;
	
	private Integer confLevel; //配置项层次级别，1代表处于顶级、2代表处于第二层级别、以此类推……
	private String remark;

	public ConfigCode() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getConfKey() {
		return confKey;
	}

	public void setConfKey(String confKey) {
		this.confKey = confKey;
	}

	public String getConfValue() {
		return confValue;
	}

	public void setConfValue(String confValue) {
		this.confValue = confValue;
	}

	public String getConfText() {
		return confText;
	}

	public void setConfText(String confText) {
		this.confText = confText;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getConfType() {
		return confType;
	}

	public void setConfType(String confType) {
		this.confType = confType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getConfLevel() {
		return confLevel;
	}

	public void setConfLevel(Integer confLevel) {
		this.confLevel = confLevel;
	}
}
