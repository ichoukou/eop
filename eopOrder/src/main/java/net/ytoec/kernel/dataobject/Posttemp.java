package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.List;

import net.ytoec.kernel.util.DateUtil;

/**
 * 运费模板<br>
 * 
 * @author ChenRen
 * @Date 2011-09-09
 */
public class Posttemp implements Serializable {

	private static final long serialVersionUID = 1370784607607202183L;

	private Integer id;
	/** 模板名称 */
	private String ptName;
	/**
	 * 运费信息<br>
	 * 为xml字符串. 字符串内容对应{@link net.ytoec.kernel.dataobject.Postinfo}对象
	 */
	private String postinfo;
	/** 创建人Id, 对应创建人的主键Id */
	private Integer createUser;
	private String createTime;
	private Integer updateUser;
	private String updateTime;
	private String remark;
	private double module;
	private String calclateType;
	private float firstWeight;

	/**
	 * 运费信息对象集合<br>
	 * 将{@link Posttemp#postinfo}的xml字符串解析成{@link Postinfo}对象存在在该list里
	 */
	private List<Postinfo> postinfoList;

	/** 运费模板关联的vip用户(数据格式：显示名(账号 ))；多个用户用";"分隔 */
	private String vipText;
	/** 运费模板关联的vip用户的id字符串；多个Id用";"分隔 */
	private String vipIds;

	/**
	 * 创建人显示名称<br>
	 * DB中没有该字段，因为页面要显示创建人显示值，所以这里字段只给页面先使用<br>
	 * @ ChenRen/2011-10-21
	 */
	private String createUserText;

	/**
	 * 1表示系统模板. 其他值暂无意义<br>
	 * @ ChenRen/2011-10-21
	 */
	private String ptType;

	/**
	 * 运费模板的首发地
	 * @ Qichao/2012-04-06
	 */
	private String ptGround;
	
	
	
	public String getPtGround() {
		return ptGround;
	}

	public void setPtGround(String ptGround) {
		this.ptGround = ptGround;
	}

	public Posttemp() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPtName() {
		return ptName;
	}

	public void setPtName(String ptName) {
		this.ptName = ptName;
	}

	/**
	 * 运费信息<br>
	 * 为xml字符串. 字符串内容对应{@link net.ytoec.kernel.dataobject.Postinfo}对象
	 */
	public String getPostinfo() {
		return postinfo;
	}

	public void setPostinfo(String postinfo) {
		this.postinfo = postinfo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}

	public String getCreateTime() {
		return DateUtil.toSeconds(DateUtil.valueof(createTime,
				"yyyy-MM-dd HH:mm:ss"));
		// createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public List<Postinfo> getPostinfoList() {
		return postinfoList;
	}

	public void setPostinfoList(List<Postinfo> postinfoList) {
		this.postinfoList = postinfoList;
	}

	public String getVipText() {
		return vipText;
	}

	public void setVipText(String vipText) {
		this.vipText = vipText;
	}

	public String getVipIds() {
		return vipIds;
	}

	public void setVipIds(String vipIds) {
		this.vipIds = vipIds;
	}

	public String getCreateUserText() {
		return createUserText;
	}

	public void setCreateUserText(String createUserText) {
		this.createUserText = createUserText;
	}

	public String getPtType() {
		return ptType;
	}

	public void setPtType(String ptType) {
		this.ptType = ptType;
	}

	public Integer getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(Integer updateUser) {
		this.updateUser = updateUser;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public double getModule() {
		return module;
	}

	public void setModule(double module) {
		this.module = module;
	}

	public String getCalclateType() {
		return calclateType;
	}

	public void setCalclateType(String calclateType) {
		this.calclateType = calclateType;
	}

	public float getFirstWeight() {
		return firstWeight;
	}

	public void setFirstWeight(float firstWeight) {
		this.firstWeight = firstWeight;
	}

}
