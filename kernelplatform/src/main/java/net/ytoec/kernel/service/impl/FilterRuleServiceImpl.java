package net.ytoec.kernel.service.impl;



import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dao.FilterRuleDao;
import net.ytoec.kernel.dataobject.FilterResult;
import net.ytoec.kernel.dataobject.FilterRule;
import net.ytoec.kernel.service.DFA;
import net.ytoec.kernel.service.FilterRuleService;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class FilterRuleServiceImpl<T extends FilterRule> implements FilterRuleService<T> {

	@Inject
	private FilterRuleDao<T> filterRuleDao;
	
	@SuppressWarnings("rawtypes")
	private static Map<Integer, List> filterRuleTypesCache = new HashMap<Integer, List>();
	
	/**
	 *新增过滤规则
	 */
	@Override
	public void addFilterRule(T entity) throws DataAccessException {
		// TODO Auto-generated method stub
		filterRuleDao.addFilterRule(entity);
	}

	/**
	 * 修改过滤规则信息
	 */
	@Override
	public void editFilterRule(T entity) throws DataAccessException {
		// TODO Auto-generated method stub
		filterRuleDao.editFilterRule(entity);
	}

	/**
	 * 通过filterRuleID获取一个FilterRule对象
	 */
	@Override
	public T getFilterRule(Integer filterRuleId) throws DataAccessException {
		// TODO Auto-generated method stub
		return filterRuleDao.getFilterRule(filterRuleId);
	}

	/**
	 * 逻辑删除过滤规则
	 */
	@Override
	public void delFilterRule(T entity) throws DataAccessException {
		// TODO Auto-generated method stub
		filterRuleDao.delFilterRule(entity);
	}

	/**
	 * 根据参数查询过滤规则，并分页
	 */
	@Override
	public Pagination getListPage(Pagination pageParams)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return filterRuleDao.getListPage(pageParams);
	}
	
	/**
	 * 验证目标字条串是否含有关键字，并返回结果集。
	 * @param target 要过滤的目标字符串
	 * @param type 过滤规则类别，取值参考FilterRule中定义的常量。
	 * @return FilterResult 过滤结果，含两个属性:
	 * 	1).isvalid－关键过滤是否通过，boolean型，取值列表：true－通过；false－不通过（内容带有敏感词）
	 * 	2).invalidWords-目标内容含有的敏感词列表，List型。
	 */
	public FilterResult filter(String target,Integer type){
		DFA dfa = new DFA();
		List<T> list = getFilterRulesFromCache(type);
		for (FilterRule filterRule : list) {
			//dfa.AddKeyword(filterRule.getWords());
			String[] wordArray = filterRule.getWordArray();
			for(String word : wordArray){
				dfa.AddKeyword(word);
			}
		}
		List<String> invalidWords = dfa.filter(target);
		FilterResult result = new FilterResult();
		result.setIsvalid(invalidWords.size() == 0);
		result.setInvalidWords(invalidWords);
		return result;
	}
	
	/**
	 * 后台过滤报道内容，不读缓存
	 * @param target
	 * @param type
	 * @return
	 */
	public FilterResult bfilter(String target,Integer type){
		DFA dfa = new DFA();
		List<T> list = getFilterRulesByType(type);
		for (FilterRule filterRule : list) {
			//dfa.AddKeyword(filterRule.getWords());
			String[] wordArray = filterRule.getWordArray();
			for(String word : wordArray){
				dfa.AddKeyword(word);
			}
		}
		List<String> invalidWords = dfa.filter(target);
		FilterResult result = new FilterResult();
		result.setIsvalid(invalidWords.size() == 0);
		result.setInvalidWords(invalidWords);
		return result;
	}
	
	/**
	 * 按类别动态读取过滤规则
	 */
	public List<T> getFilterRulesByType(Integer type)
	throws DataAccessException {
		// TODO Auto-generated method stub
			List<T> resultList = filterRuleDao.getFilterRulsByType(type);
			return resultList;
	}
	
	/**
	 * 按类别取过滤规则列表，如果缓存中有，则从缓存中读取；如果缓存中没有，则访问数据库后返回，并存入缓存中。
	 * @param type 过滤规则类别，取值参考FilterRule中定义的常量。
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> getFilterRulesFromCache(Integer type)
			throws DataAccessException {
		// TODO Auto-generated method stub
		if(filterRuleTypesCache.containsKey(type)){
			return filterRuleTypesCache.get(type);
		}
		else{
			List<T> resultList = filterRuleDao.getFilterRulsByType(type);
			filterRuleTypesCache.put(type, resultList);
			return resultList;
		}
	}
	
	/**
	 * 刷新过滤规则缓存
	 */
	public void refreshFilterRulesCache(){
		Set<Integer> keySet = filterRuleTypesCache.keySet();
		filterRuleTypesCache.clear();
		for(Integer type : keySet){
			getFilterRulesFromCache(type);
		}
	}

	/**
	 * 修改
	 * @throws Exception 
	 */
	@Override
	public boolean updateFilterRule(FilterRule rule) throws Exception {
		if(rule == null){
			throw new Exception("##添加关键词传参为空！");
		}
		return	filterRuleDao.updateFilterRule(rule);
	}
	
}
