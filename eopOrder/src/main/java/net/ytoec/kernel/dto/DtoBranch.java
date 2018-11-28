package net.ytoec.kernel.dto;

import java.io.Serializable;

import net.ytoec.kernel.dataobject.User;

/**
 * 网点的dto对象<br>
 * 该对象是{@link User}的一个简化对象。用过存储网点用户的三个属性：编码、名称、状态
 * @author wangyong  添加联系方式：电话和固定电话
 * 
 * @author ChenRen
 * @date 2011-12-20
 */
public class DtoBranch implements Serializable{

	 
	//private static final long serialVersionUID = -2631153906728055659L;
	/** 网点编码 */
	private String code;
	/** 网点名称 */
	private String text;
	/** 网点状态 0(失效)、1(正常)、TBA(未激活状态) */
	private String status;
	/** 网点电话 */
	private String phone;
	/** 网点手机 */
	private String mobile;


	public DtoBranch() {
	}

	public DtoBranch(String code, String text, String status, String phone, String mobile) {
		super();
		this.code = code;
		this.text = text;
		this.status = status;
		this.phone = phone;
		this.mobile = mobile;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
