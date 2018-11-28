package net.ytoec.kernel.dataobject;

import java.io.Serializable;

/**
 * 短信发送[其它]
 * @author wangguoliang
 * 20120812
 */
public class SMSTemp implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键自动生成
	 */
	private Integer id;
	
	
	private String type;
	
	
	private String name;
	
	private String mobile;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
}
