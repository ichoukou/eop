package net.ytoec.kernel.mapper;



import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.FilterRule;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

import org.springframework.dao.DataAccessException;


public interface FilterRuleMapper<T extends FilterRule> extends BaseSqlMapper<T> {
	/**
	 * 逻辑删除信息
	 * @param entity
	 * @throws DataAccessException
	 */
	public void delFilterRule(T entity) throws DataAccessException;
	
	/**
	 * 分页查询
	 * @param map
	 * @return List<T>
	 */
	public List<T> getListPage(Map<String, Object> map);
	
	/**
	 * 分页查询的总记录数
	 * @param map
	 * @return 总记录数
	 */
	public Integer getTotal(Map<String, Object> map);
	
	/**
	 * 按类型取规则列表。
	 * @param type
	 * @return
	 */
	public List<T> getFilterRulesByType(Integer type);

	/**
	 * 修改关键词列表
	 * @param rule
	 */
	public void updateFilterRule(FilterRule rule);
}
