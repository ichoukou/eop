/**
 * 
 */
package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.Product;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * @author Wangyong
 * 产品信息映射器.
 */
public interface ProductMapper<T extends Product> extends BaseSqlMapper<T> {

	/**
	 * 获取所有产品信息列表
	 * @return
	 */
	public List<T> getAllProduct();
	
	/**
	 * 根据货物名称查询产品信息列表
	 * @param itemName 货物名称
	 * @return
	 */
	public List<T> getProductListByItemName(String itemName);
	
	/**
	 * 根据orderID查询产品信息列表
	 * @param itemName 货物名称
	 * @return
	 */
	public List<T> getProductListByOrder(Integer orderId);
	
	public Integer removeByorderId(Map map);
}
