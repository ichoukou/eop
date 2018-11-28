package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.OrderTemp;

import org.springframework.dao.DataAccessException;

/**
 * 导入订单的临时表相关Dao入口
 * */
@SuppressWarnings("all")
public interface OrderTempDao<T> {

	/**
	 * 创建订单临时表
	 * 
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public boolean addOrderTemp(T entity) throws DataAccessException;

	/**
	 * 根据Id读取单个临时
	 * 
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public T getOrderTempById(Integer id) throws DataAccessException;

	/**
	 * 根据物流号读取临时订单
	 * 
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getOrderTempByMailNoRemark(T entity)
			throws DataAccessException;

	/**
	 * 获取所有临时订单
	 * 
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getAllOrderTemp() throws DataAccessException;

	/**
	 * 删除一个临时订单
	 * 
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public abstract boolean delOrderTemp(T entity) throws DataAccessException;

	/**
	 * 获得当前用户导入的临时数据
	 * 
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getOrderTempListByRemark(String remark)
			throws DataAccessException;

	public List<T> getOrderTempListByRemarkPage(Map map);

	/**
	 * 根据物流号读取临时订单
	 * 
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getOrderTempLogisticIdRemark(T entity)
			throws DataAccessException;

	public int countOrderTempListByMap(Map map) throws DataAccessException;
	
	public boolean addOrderTempList(List<T> list) throws DataAccessException;

}
