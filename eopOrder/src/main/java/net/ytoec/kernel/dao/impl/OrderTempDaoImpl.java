package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.OrderTempDao;
import net.ytoec.kernel.dataobject.OrderTemp;
import net.ytoec.kernel.mapper.OrderTempMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("all")
public class OrderTempDaoImpl<T extends OrderTemp> implements OrderTempDao<T> {

	private static Logger logger = LoggerFactory.getLogger(OrderTempDaoImpl.class);

	@Inject
	private OrderTempMapper<T> mapper;

	@Override
	public boolean addOrderTemp(T entity) {
		boolean flag = false;
		try {
			mapper.add(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}

		return flag;
	}

	@Override
	public T getOrderTempById(Integer id) throws DataAccessException {
		OrderTemp orderTemp = new OrderTemp();
		orderTemp.setId(id);
		T entity = null;

		try {
			entity = mapper.get((T) orderTemp);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return entity;
	}

	@Override
	public List<T> getAllOrderTemp() throws DataAccessException {
		List<T> list = null;

		try {
			list = mapper.getAllOrderTemp();
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public boolean delOrderTemp(T entity) throws DataAccessException {
		boolean flag = false;

		try {
			mapper.remove(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}

		return flag;
	}

	@Override
	public List<T> getOrderTempByMailNoRemark(T entity)
			throws DataAccessException {
		List<T> list = null;
		try {
			list = mapper.getOrderTempByMailNoRemark(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public List<T> getOrderTempListByRemark(String remark)
			throws DataAccessException {
		List<T> list = null;
		try {
			list = mapper.getOrderTempListByRemark(remark);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public List<T> getOrderTempListByRemarkPage(Map map) {
		List<T> list = null;
		try {
			list = mapper.getOrderTempListByRemarkPage(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public List<T> getOrderTempLogisticIdRemark(T entity) {
		List<T> list = null;
		try {
			list = mapper.getOrderTempLogisticIdRemark(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public int countOrderTempListByMap(Map map) throws DataAccessException {
		return mapper.countOrderTempListByMap(map);
	}

	@Override
	public boolean addOrderTempList(List<T> list) throws DataAccessException {
		boolean flag = false;
		try {
			for (T s : list) {
				mapper.add(s);
				flag = true;
			}
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}
}
