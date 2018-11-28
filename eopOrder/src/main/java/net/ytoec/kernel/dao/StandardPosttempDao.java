package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

/**
 * 
 * 标准运费模版数据访问接口.
 *
 * @param <T> StandardPosttemp.
 */
public interface StandardPosttempDao<T> {
	/**
	 * 增加标准运费模版对象.
	 * @param StandardPosttemp
	 * @throws DataAccessException
	 */
	public void addStandardPosttemp(T standardPosttemp) throws DataAccessException;
	/**
	 * 修改标准运费模版对象.
	 * @param standardPosttemp
	 * @throws DataAccessException
	 */
	public void editStandardPosttemp(T standardPosttemp) throws DataAccessException;
	/**
	 * 删除标准运费模版对象.
	 * @param standardPosttemp
	 * @throws DataAccessException
	 */
	public void removeStandardPosttemp(T standardPosttemp) throws DataAccessException;
	/**
	 * 根据标准运费模版id获取标准运费模版对象.
	 * @param standardPosttempId
	 * @return
	 * @throws DataAccessException
	 */
	public T getStandardPosttempById(Integer standardPosttempId) throws DataAccessException;
	/**
	 * 获取标准运费模版列表对象.
	 * @return
	 * @throws DataAccessException
	 */
	public  List<T> getAll() throws DataAccessException;
	
	
	/**
	 * 获取标准运费模版的分页数据列表。分页的参数startIndex（开始索引）和pageNum（每次查询数目）封装在Map中。
	 * @param limit
	 * @return
	 */

	public List<T> getStandardPosttempListBySourceId(Map map) throws DataAccessException;
	public List<T> getStandardPosttempByProv(Map map) throws DataAccessException;

	

}
