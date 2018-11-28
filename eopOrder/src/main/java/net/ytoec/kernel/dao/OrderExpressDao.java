package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;


public interface OrderExpressDao<T> {
	

	public boolean addOrderExpress(T orderExpress) throws DataAccessException;
	

	public boolean removeOrderExpress(T orderExpress) throws DataAccessException;
	

	public boolean editOrderExpress(T orderExpress) throws DataAccessException;
	

	public T getOrderExpressById(Integer id) throws DataAccessException;
	

	public List<T> getOrderExpressList(Map map) throws DataAccessException;
	
	public List<T> getOrderExpressByStoreId(Map map) throws DataAccessException;
	
	/**
	 * 根据storeIds获取面单模版信息  by wus
	 * @param params
	 * @return
	 */
	public List<T> getOrderExpressByStoreIds(Map params) throws DataAccessException;

}
