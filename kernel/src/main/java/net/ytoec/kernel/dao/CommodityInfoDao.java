package net.ytoec.kernel.dao;

import java.util.List;

/**
 * 产品相关的接口
 * 
 * @author huaiwen
 * 
 * @param <T>
 */
public interface CommodityInfoDao<T> {

	/**
	 * 批量插入产品信息
	 * 
	 * @param ProductList
	 * @return
	 */
	boolean batchAddProduct(List<T> ProductList);

	/**
	 * 获取订单对应的所有产品信息
	 * 
	 * @param orderId
	 * @return
	 */
	List<T> getProductByOrderId(int orderId);

	boolean delProductByOrderId(int orderId);

}
