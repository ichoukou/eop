/**
 * 2012-5-14下午08:16:27
 * wangyong
 */
package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

/**
 * @author wangyong
 * 2012-5-14
 */
public interface TempOrderDao<T> {
	
	/**
	 * 添加新数据
	 * @param entity
	 * @return
	 */
	public boolean add(T entity);
	
	/**
	 * 根据Id删除数据
	 * @param id
	 * @return
	 */
	public boolean remove(Integer id);
	
	/**
	 * 根据订单号查询
	 * @param orderId
	 * @return
	 */
	public List<T> getByOrderId(String orderId);
	
	 /**
	  * 查询不同temp_key下不同订单列表 tempKey必选项
	  * @param map
	  * @return
	  */
	public List<T> getTempListByMap(Map map);
	
	/**
	 * 统计不同temp_key下不同订单列表 tempKey必选项
	 * @param map
	 * @return
	 */
	public int countTempListByMap(Map map);

}
