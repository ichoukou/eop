package net.ytoec.kernel.dataobject;

import java.io.Serializable;

/**
 * 运费模板<br>
 * 
 * @author ChenRen
 * @Date 2011-09-09
 */
public class PosttempUser implements Serializable {

	private static final long serialVersionUID = 5222503392034214140L;

	private Integer id;
	/** 网点Id. 对应网点用户的主键Id */
	private Integer branchId;
	/** Vip用户的主键Id. 一个vip用户一条记录 */
	private Integer vipId;
	/** 模板Id */
	private Integer postId;

	public PosttempUser() {
	}

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	public Integer getVipId() {
		return vipId;
	}

	public void setVipId(Integer vipId) {
		this.vipId = vipId;
	}

	public Integer getPostId() {
		return postId;
	}

	public void setPostId(Integer postId) {
		this.postId = postId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
