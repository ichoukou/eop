package net.ytoec.kernel.action.sms;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.dataobject.FilterRule;
import net.ytoec.kernel.service.FilterRuleService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 过滤词管理
 * @author yangqinghua
 *
 */
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class SmsFilterWordsAction extends AbstractActionSupport {

	private static final long serialVersionUID = -6396325181757062681L;
	
	private Logger logger = LoggerFactory.getLogger(SmsFilterWordsAction.class);
	
	@Inject
	private FilterRuleService filterRuleService;
	
	private FilterRule filterRule;
	private String keyWords;
	private Integer filterRuleId;
	private String resultString;
	
	/**
	 * 过滤词首页 list
	 * 1:问题件通知
	 * @return
	 */
	public String index(){
		
		List<FilterRule> filterRuleList = filterRuleService.getFilterRulesByType(FilterRule.TYPE_ISSUEREPORT);
		if(filterRuleList == null || filterRuleList.size() <= 0){
			logger.error("##没有任何过滤词");
			FilterRule filterRuleTemp = new FilterRule();
			filterRuleTemp.setType(1);
			filterRuleTemp.setWords("");
			filterRuleService.addFilterRule(filterRuleTemp);
			filterRule = ((List<FilterRule>)(filterRuleService.getFilterRulesByType(FilterRule.TYPE_ISSUEREPORT))).get(0);
		}else{
			filterRule = filterRuleList.get(0);
		}
		
		return "index";
	}
	
	/**
	 * 过滤词首页 index
	 * 1:问题件通知
	 * @return
	 */
	public String list(){
		
		List<FilterRule> filterRuleList = filterRuleService.getFilterRulesByType(FilterRule.TYPE_ISSUEREPORT);
		if(filterRuleList == null || filterRuleList.size() <= 0){
			logger.error("##没有任何过滤词");
			FilterRule filterRuleTemp = new FilterRule();
			filterRuleTemp.setType(1);
			filterRuleService.addFilterRule(filterRuleTemp);
			filterRule = ((List<FilterRule>)(filterRuleService.getFilterRulesByType(FilterRule.TYPE_ISSUEREPORT))).get(0);
		}else{
			filterRule = filterRuleList.get(0);
		}
		
		return "index";
	}
	
	/**
	 * 添加关键词
	 * @return
	 */
	public String add(){
		
		FilterRule rule = (FilterRule) filterRuleService.getFilterRule(filterRuleId);
		
		/*String resultString1 = "";
		if(!"".equals(rule.getWords())){
			String rules[] = rule.getWords().split("、");
			ArrayList<String> wordsList = new ArrayList<String>();
			for(int i = 0 ; i < rules.length ; i++){
				wordsList.add(rules[i]);
			}
			
			String addRules[] = keyWords.split("、");
			ArrayList<String> addWordsList = new ArrayList<String>();
			for(int i = 0 ; i < addRules.length ; i++){
				addWordsList.add(addRules[i]);
			}
			
			StringBuffer inRules = new StringBuffer();
			StringBuffer outRules = new StringBuffer();
			for(int i = 0 ; i < addWordsList.size() ; i++){
				//如果添加数据已存在 则返回页面提示
				if(wordsList.contains(addWordsList.get(i)))outRules.append(addWordsList.get(i)+"、");
				else inRules.append(addWordsList.get(i)+"、");
			}
			
			if("".equals(inRules.toString())){
				resultString="抱歉！关键词：\""+outRules.toString().substring(0, outRules.toString().length()-1)+"\"均已存在！无法添加";
				return "add";
			}
			
			rule.setWords(rule.getWords()+"、"+inRules.toString().substring(0, inRules.toString().length()-1));
			
			if("".equals(outRules.toString()))resultString1="";
			if(!"".equals(outRules.toString()))resultString1="关键字：\""+inRules.toString().substring(0, inRules.toString().length()-1)+"\"添加成功！但关键字：\""+outRules.toString().substring(0, outRules.toString().length()-1)+"\"已存在，无法添加！";
		}else{
			rule.setWords(keyWords);
		}*/
		
		if(!"".equals(rule.getWords())){
			rule.setWords(rule.getWords()+"、"+keyWords);
		}else{
			rule.setWords(keyWords);
		}
		
		boolean bool = false;
		try {
			 bool = filterRuleService.updateFilterRule(rule);
			 logger.info("过滤词添加成功与否："+bool);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		/*if(bool && "".equals(resultString1))resultString="添加成功！";
		if(bool && !"".equals(resultString1))resultString=resultString1;
		if(!bool) resultString ="抱歉！系统繁忙，请稍后再试！";*/
		
		resultString = ((FilterRule) filterRuleService.getFilterRule(filterRuleId)).getWords();
		
		return "add";
	}
	
	/**
	 * 删除
	 * @return
	 */
	public String del(){
		
		FilterRule rule = (FilterRule) filterRuleService.getFilterRule(filterRuleId);
		/*String rules[] = rule.getWordArray();
		ArrayList<String> rulesList = new ArrayList<String>();
		for(int i = 0 ; i < rules.length ; i++){
			rulesList.add(rules[i]);
		}
		
		String delRules[] = keyWords.split("、");
		ArrayList<String> inWoardsList = new ArrayList<String>();
		for(int i = 0 ; i < delRules.length ; i++){
			inWoardsList.add(delRules[i]);
		}
		
		boolean flag = false;
		StringBuffer newWords  = new StringBuffer();
		for(int i = 0 ; i < inWoardsList.size() ; i++){
			if(rulesList.contains(inWoardsList.get(i))){
				int index = 0;
				for(int j = 0 ; j < rulesList.size() ; j++){
					if(inWoardsList.get(i).equals(rulesList.get(j)) ){
						index = j;
						break;
					}
				}
				rulesList.remove(index);
				flag = true;
			}
		}
		
		if(!flag){
			resultString = "抱歉！关键词都不存在无法删除！";
			return "del";
		}
		
		for(int z = 0 ;z < rulesList.size(); z++){
			if(z < rulesList.size() - 1)
				newWords.append(rulesList.get(z)+"、");
			else
				newWords.append(rulesList.get(z));
		}*/
		
		String rules[] = rule.getWordArray();
		ArrayList<String> rulesList = new ArrayList<String>();
		for(int i = 0 ; i < rules.length ; i++){
			rulesList.add(rules[i]);
		}
		
		int index = 0;
		for(int i = 0 ; i < rulesList.size() ; i++){
			if(rulesList.get(i).equals(keyWords)){
				index = i;
				break;
			}
		}
		rulesList.remove(index);
		
		StringBuffer newWords = new StringBuffer();
		for(int z = 0 ;z < rulesList.size(); z++){
			if(z < rulesList.size() - 1)
				newWords.append(rulesList.get(z)+"、");
			else
				newWords.append(rulesList.get(z));
		}
		rule.setWords(newWords.toString());
		boolean bool = false;
		try {
			 bool = filterRuleService.updateFilterRule(rule);
			 logger.info("过滤词修改成功与否："+bool);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		/*if(bool)resultString="删除成功！";
		else resultString ="抱歉！系统繁忙，请稍后再试！";*/
		
		resultString = newWords.toString();
		
		return "del";
	}
	
	
	public FilterRule getFilterRule() {
		return filterRule;
	}
	public void setFilterRule(FilterRule filterRule) {
		this.filterRule = filterRule;
	}
	public String getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	public Integer getFilterRuleId() {
		return filterRuleId;
	}
	public void setFilterRuleId(Integer filterRuleId) {
		this.filterRuleId = filterRuleId;
	}
	public String getResultString() {
		return resultString;
	}
	public void setResultString(String resultString) {
		this.resultString = resultString;
	}
	
	
	
}
