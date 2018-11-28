package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.OrderExpress;

public interface OrderExpressService<T> {

	boolean addOrderExpress(T orderExpress);

	boolean editOrderExpress(T orderExpress);

	boolean deleteOrderExpress(T orderExpress);

	public OrderExpress getOrderExpressById(int id);

	public List<T> getOrderExpressList(Map map, Pagination pagination,
			boolean flag);

	public List<T> getOrderExpressListbyStoreId(Integer storeId,
			Pagination pagination, boolean flag);

	public List<T> getOrderExpressByStoreId(Map map);
	
	/**
	 * 根据storeIds获取面单模版信息  by wus
	 * @param params
	 * @return
	 */
	public List<T> getOrderExpressByStoreIds(Map params);
	
}
