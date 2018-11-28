package net.ytoec.kernel.dataobject;

import java.util.Date;

/**
 * 网点沟通记录内容<br>
 * 一个问题件可以有多个网点沟通记录. 在页面显示问题件的时候，处理内容实时去金刚的表中查询数据
 * @date 2011-12-06
 * @update 2012-01-12
 * @author wangyong
 */
public class QuestionnaireDeal {
	/** 处理内容 */
	private String dealContent;
	/** 处理网点名称 */
	private String dealBranckText;
	/** 处理人名称 */
	private String dealUserText;
	/** 处理时间 */
	private Date dealTime;

	public String getDealContent() {
		return dealContent;
	}

	public void setDealContent(String dealContent) {
		this.dealContent = dealContent;
	}

	public String getDealBranckText() {
		return dealBranckText;
	}

	public void setDealBranckText(String dealBranckText) {
		this.dealBranckText = dealBranckText;
	}

	public String getDealUserText() {
		return dealUserText;
	}

	public void setDealUserText(String dealUserText) {
		this.dealUserText = dealUserText;
	}

	public Date getDealTime() {
		return dealTime;
	}

	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}
}
