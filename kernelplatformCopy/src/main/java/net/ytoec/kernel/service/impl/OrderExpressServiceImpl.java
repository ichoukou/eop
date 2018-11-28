package net.ytoec.kernel.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dao.OrderExpressDao;
import net.ytoec.kernel.dataobject.OrderExpress;
import net.ytoec.kernel.service.OrderExpressService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * 接口实现类.
 * 
 * @param <T>
 *            OrderExpress
 */
@Service
@Transactional
public class OrderExpressServiceImpl<T extends OrderExpress> implements
		OrderExpressService<T> {
	private static Logger logger = LoggerFactory.getLogger(OrderExpressServiceImpl.class);
	@Autowired
	private OrderExpressDao<T> dao;

	@Override
	public boolean addOrderExpress(T task) {
		boolean flag = false;
		try {
			flag = this.dao.addOrderExpress(task);
		} catch (DataAccessException dae) {
			flag = false;
		}
		return flag;
	}

	@Override
	public List<T> getOrderExpressList(Map map, Pagination pagination,
			boolean flag) {
		// TODO Auto-generated method stub
		if (flag) {
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
		}
		return (List<T>) dao.getOrderExpressList(map);
	}

	@Override
	public boolean editOrderExpress(T orderExpress) {
		// TODO Auto-generated method stub
		try {
			this.dao.editOrderExpress(orderExpress);
		} catch (DataAccessException dae) {
			return false;
		}
		return true;
	}

	@Override
	public boolean deleteOrderExpress(T orderExpress) {
		// TODO Auto-generated method stub
		try {
			this.dao.removeOrderExpress(orderExpress);
		} catch (DataAccessException dae) {
			return false;
		}
		return true;
	}

	@Override
	public List<T> getOrderExpressListbyStoreId(Integer storeId,
			Pagination pagination, boolean flag) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("storeId", storeId);
		return getOrderExpressList(map, pagination, flag);
	}

	@Override
	public OrderExpress getOrderExpressById(int id) {
		OrderExpress orderExpress = null;
		// TODO Auto-generated method stub
		try {
			orderExpress = this.dao.getOrderExpressById(id);
		} catch (DataAccessException dae) {
			return orderExpress;
		}
		return orderExpress;
	}

	@Override
	public List<T> getOrderExpressByStoreId(Map map) {
		return (List<T>) dao.getOrderExpressByStoreId(map);
	}

	@Override
	public List<T> getOrderExpressByStoreIds(Map params) {
		
		return (List<T>) dao.getOrderExpressByStoreIds(params);
	}

}
