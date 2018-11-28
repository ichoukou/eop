/**
 * 2012-5-15上午09:33:31
 * wangyong
 */
package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;

/**
 * 临时订单的业务接口。该表主要是用户电子对账中异常订单、调整订单、退货订单的业务逻辑处理
 * @author wangyong
 * 2012-5-15
 */
public interface TempOrderService<T> {

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
	
	/**
	 * 根据类型、关键字查询列表，可分页
	 * @param tempKey
	 * @param tempType
	 * @param pagination
	 * @return
	 */
	public List<T> getTempList(String tempKey, Integer tempType, Pagination pagination);
	
	/**
	 * 统计根据类型、关键字查询的列表数据
	 * @param tempKey
	 * @param tempType
	 * @param pagination
	 * @return
	 */
	public int countTempList(String tempKey, Integer tempType);
	
}
