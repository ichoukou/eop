package net.ytoec.kernel.dao;


import java.util.List;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.FilterRule;

import org.springframework.dao.DataAccessException;

public interface FilterRuleDao<T> {
	
	public void addFilterRule(T entity) throws DataAccessException;

	public void editFilterRule(T entity) throws DataAccessException;

	public T getFilterRule(Integer filterRuleId) throws DataAccessException;

	public void delFilterRule(T entity) throws DataAccessException;

	public Pagination getListPage(Pagination pageParams)
			throws DataAccessException;
	
	public List<T> getFilterRulsByType(Integer type)throws DataAccessException;

	public boolean updateFilterRule(FilterRule rule);;
	
}
