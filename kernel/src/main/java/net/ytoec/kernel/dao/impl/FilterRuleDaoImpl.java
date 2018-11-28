package net.ytoec.kernel.dao.impl;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.action.sms.SmsFilterWordsAction;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.FilterRuleDao;
import net.ytoec.kernel.dataobject.FilterRule;
import net.ytoec.kernel.mapper.FilterRuleMapper;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class FilterRuleDaoImpl<T extends FilterRule> implements FilterRuleDao<T> {

	private Logger logger=Logger.getLogger(FilterRuleDaoImpl.class);
	
	@Inject
	private FilterRuleMapper filterRuleMapper;
	
	/**
	 *新增过滤规则
	 */
	@Override
	public void addFilterRule(T entity) throws DataAccessException {
		// TODO Auto-generated method stub
		filterRuleMapper.add(entity);
	}

	/**
	 * 修改过滤规则
	 */
	@Override
	public void editFilterRule(T entity) throws DataAccessException {
		// TODO Auto-generated method stub
		filterRuleMapper.edit(entity);
	}

	/**
	 * 通过filterRuleID获取一个FilterRule对象
	 */
	@Override
	public T getFilterRule(Integer filterRuleId) throws DataAccessException {
		// TODO Auto-generated method stub
		return (T) filterRuleMapper.get(filterRuleId);
	}

	/**
	 * 删除过滤规则
	 */
	@Override
	public void delFilterRule(T entity) throws DataAccessException {
		// TODO Auto-generated method stub
//		entity.setStatus(FilterRule.DEL_STATUS);
		filterRuleMapper.remove(entity.getFilterRuleId());
	}

	/**
	 * 根据参数查询并分页
	 */
	@Override
	public Pagination getListPage(Pagination pageParams)
			throws DataAccessException {
		// TODO Auto-generated method stub
		if (pageParams.getTotalRecords() == null) {
			Integer total = filterRuleMapper.getTotal(pageParams.getParams());
			pageParams.setTotalRecords(total);
		}
		List<T> list = filterRuleMapper.getListPage(pageParams.getParams());
		pageParams.setRecords(list);
		return pageParams;
	}

	@Override
	public List<T> getFilterRulsByType(Integer type)
			throws DataAccessException {
		// TODO Auto-generated method stub
		if(type != null && type > 0)
			return filterRuleMapper.getFilterRulesByType(type);
		else 
			return new ArrayList<T>();
	}

	@Override
	public boolean updateFilterRule(FilterRule rule) {
		boolean flag = false;
		try{
			filterRuleMapper.updateFilterRule(rule);
			flag = true;
		}catch(Exception e){
			flag = false;
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

}
