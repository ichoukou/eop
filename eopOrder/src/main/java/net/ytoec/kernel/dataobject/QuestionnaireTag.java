package net.ytoec.kernel.dataobject;

import java.util.Date;

/**
 * 问题单处理bean对象
 * @author wangjianzhong
 * @2012-8-2
 * net.ytoec.kernel.dataobject
 */
public class QuestionnaireTag {

	//主键ID
	private Integer id;
	//标签名称
	private String tagName;
	//标签位置
	private Integer tagPos;
	//标签所属ID
	private Integer tagUserThreadId;
	//标签类型
	private Integer tagType;
	//标签备注
	private String tagRemark;
	//创建时间
	private Date createTime;
	//更新时间
	private Date updateTime;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public Integer getTagPos() {
		return tagPos;
	}
	public void setTagPos(Integer tagPos) {
		this.tagPos = tagPos;
	}
	public Integer getTagUserThreadId() {
		return tagUserThreadId;
	}
	public void setTagUserThreadId(Integer tagUserThreadId) {
		this.tagUserThreadId = tagUserThreadId;
	}
	public Integer getTagType() {
		return tagType;
	}
	public void setTagType(Integer tagType) {
		this.tagType = tagType;
	}
	public String getTagRemark() {
		return tagRemark;
	}
	public void setTagRemark(String tagRemark) {
		this.tagRemark = tagRemark;
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
	
}
