/**
 * 
 */
package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.OrderExpressDao;
import net.ytoec.kernel.dataobject.OrderExpress;
import net.ytoec.kernel.mapper.OrderExpressMapper;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;


@Repository
public class OrderExpressDaoImpl<T extends OrderExpress> implements OrderExpressDao<T> {

	private Logger logger=Logger.getLogger(OrderExpressDaoImpl.class);
	@Inject
	private OrderExpressMapper<OrderExpress> mapper;
	
	@Override
	public boolean addOrderExpress(T orderExpress) {

		boolean flag = false;
		try{
			mapper.add(orderExpress);
			flag = true;
		} catch(Exception e){
			
			flag = false;
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public T getOrderExpressById(Integer id) {

		OrderExpress errMsg = new OrderExpress();
		T entity = null;
		try{
			errMsg.setId(id);
			entity = (T)mapper.get(errMsg);
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return entity;
	}

	@Override
	public List<T> getOrderExpressList(Map map) {

		List<T> list = null;
		try{
			list = (List<T>)this.mapper.getOrderExpressList(map);
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public boolean removeOrderExpress(T orderExpress) {

		boolean flag = false;
		try{
			mapper.remove(orderExpress);
			flag = true;
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean editOrderExpress(T orderExpress) {

		boolean flag = false;
		try{
			mapper.edit(orderExpress);
			flag = true;
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public List<T> getOrderExpressByStoreId(Map map) {

		List<T> list = null;
		try{
			list = (List<T>)this.mapper.getOrderExpressByStoreId(map);
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public List<T> getOrderExpressByStoreIds(Map params)
			throws DataAccessException {
		List<T> list = null;
		try{
			list = (List<T>)this.mapper.getOrderExpressByStoreIds(params);
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}
}
