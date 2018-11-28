/**
 * JgWaybillDao.java
 * Wangyong
 * 2011-8-18 下午01:08:21
 */
package net.ytoec.kernel.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

/**
 * 金刚运单接口信息数据Dao
 * @author Wangyong
 * @2011-8-18
 * net.ytoec.kernel.dao
 */
public interface JgWaybillDao<T> {

	/**
	 * 增加出错信息规范对象数据
	 * @param jgWaybill
	 * @return
	 * @throws DataAccessException
	 */
	public boolean addJgWaybill(T jgWaybill) throws DataAccessException;
	
	/**
	 * 删除出错信息规范对象数据
	 * @param jgWaybill
	 * @return
	 * @throws DataAccessException
	 */
	public boolean removeJgWaybill(T jgWaybill) throws DataAccessException;
	
	/**
	 * 更新出错信息规范对象数据
	 * @param jgWaybill
	 * @return
	 * @throws DataAccessException
	 */
	public boolean editJgWaybill(T jgWaybill) throws DataAccessException;
	
	/**
	 * 根据id号查询出错信息规范对象
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public T getJgWaybillById(Integer id) throws DataAccessException;
	
	/**
	 * 查询所有出错信息规范对象列表
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getAllJgWaybill() throws DataAccessException;
	
	public boolean delJgWaybillFromJG(int limit);
	
	/**
	 * 统计jgwaybill表的数量
	 * 
	 * @return
	 */
	public Integer countJgwaybill();
}
