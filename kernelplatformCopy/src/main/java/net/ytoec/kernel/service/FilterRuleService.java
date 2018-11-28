package net.ytoec.kernel.service;



import java.util.List;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.FilterResult;
import net.ytoec.kernel.dataobject.FilterRule;

import org.springframework.dao.DataAccessException;



public interface FilterRuleService<T> {
	
	public void addFilterRule(T entity) throws DataAccessException;

	public void editFilterRule(T entity) throws DataAccessException;

	public T getFilterRule(Integer filterRuleId) throws DataAccessException;

	public void delFilterRule(T entity) throws DataAccessException;

	public Pagination getListPage(Pagination pageParams)
			throws DataAccessException;
	
	public List<T> getFilterRulesByType(Integer type)throws DataAccessException;
	
	public FilterResult filter(String target,Integer type);
	
	public FilterResult bfilter(String target,Integer type);
	
	public List<T> getFilterRulesFromCache(Integer type)throws DataAccessException;
	
	public void refreshFilterRulesCache();

	public boolean updateFilterRule(FilterRule rule) throws Exception;
}
