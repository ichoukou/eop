package net.ytoec.kernel.dataobject;


import java.util.Date;

public class FilterRule {
	/**
	 * 过滤规则类别--问题件通知短信
	 */
	public static final Integer TYPE_ISSUEREPORT = 1;
	
	
	/**
	 * 主键ID
	 */
	private Integer filterRuleId;
	
	/**
	 * 过滤规则类别
	 */
	private Integer type;
	
	/**
	 * 过滤规则对应的关键字
	 */
	private String words;
	
	private Date createTime;
	
	private Date updateTime;
	
	/**
	 * 过滤规则关键字列表。
	 * @return
	 */
	private String[] wordArray;

	public Integer getFilterRuleId() {
		return filterRuleId;
	}

	public void setFilterRuleId(Integer filterRuleId) {
		this.filterRuleId = filterRuleId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
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

	public String[] getWordArray() {
		//words = words.replaceAll("，", ",");
		wordArray = words.split("、");
		return wordArray;
	}
/*
	public void setWordList(List<String> wordList) {
		this.wordList = wordList;
	}*/
	
	
}
