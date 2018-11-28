package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

/**
 * 运费模板
 * 
 * @author ChenRen
 * @date 2011-09-09
 * 
 * @param <T>
 */
public interface PosttempDao<T> {

	/**
	 * 创建运费模板
	 * 
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public boolean addPosttemp(T entity) throws DataAccessException;

	/**
	 * 根据Id读取单个运费模板
	 * 
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public T getPosttempById(Integer id) throws DataAccessException;

	/**
	 * 更新运费模板
	 * 
	 * @return
	 */
	public boolean editPosttemp(T entity) throws DataAccessException;

	/**
	 * 删除运费模板
	 * 
	 * @param id
	 * @return
	 */
	public boolean delPosttemp(T entity) throws DataAccessException;

	/**
	 * 根据网点Id(在数据库等于创建者Id)查询运费模板
	 * 
	 * @param userId
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getPosttempByBranchId(Map map) throws DataAccessException;

	/**
	 * 根据模板类型查询运费模板
	 * 
	 * @param type
	 *            模板类型: 1为系统模板, 其余暂时为空
	 * @author ChenRen/2011-10-21
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getPosttempByType(String type) throws DataAccessException;
}
