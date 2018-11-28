package net.ytoec.kernel.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.OrderPrintDao;
import net.ytoec.kernel.dataobject.OrderPrint;
import net.ytoec.kernel.mapper.OrderPrintMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
/**
 * 面单打印实体dao实现类
 * @author wusha
 *
 * @param <T>
 */
@Repository
public class OrderPrintDaoImpl<T extends OrderPrint> implements
		OrderPrintDao<T> {

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory.getLogger(OrderPrintDaoImpl.class);
	
	@Inject
	private OrderPrintMapper<T> mapper;

	@Override
	public List<T> getAllOrderPrint(Map<String, Object> param)
			throws DataAccessException {
		return mapper.getAllOrderPrint(param);
	}
	
	@Override
	public Integer getTotal(Map<String, Object> param)
			throws DataAccessException {
		return mapper.getTotal(param);
	}

	@Override
	public List<T> getUndeliverOrderPrint(Map<String, Object> param)
			throws DataAccessException {
		return mapper.getUndeliverOrderPrint(param);
	}

	@Override
	public Integer getUndeliverTotal(Map<String, Object> param)
			throws DataAccessException {
		return mapper.getUndeliverTotal(param);
	}

	@Override
	public List<T> getDeliveredOrderPrint(Map<String, Object> param)
			throws DataAccessException {
		return mapper.getDeliveredOrderPrint(param);
	}

	@Override
	public Integer getDeliveredTotal(Map<String, Object> param)
			throws DataAccessException {
		return mapper.getDeliveredTotal(param);
	}
	
	@Override
	public boolean addOrderPrint(T entity) throws DuplicateKeyException,DataAccessException {
		boolean flag = false;
		try {
			mapper.addOrderPrint(entity);
			flag = true;
		}
		catch (DuplicateKeyException e) {
            // TODO: handle exception
		    throw e;
        } 
		return flag;
	}

	@Override
	public Object getOrderPrint(Integer id) throws DataAccessException {
		return mapper.getOrderPrint(id);
	}

	@Override
	public void updateOrderPrint(OrderPrint orderPrint) throws DataAccessException {
		mapper.updateOrderPrint(orderPrint);
	}

	@Override
	public List<OrderPrint> getServalOrderPrintLists(Map<String, Object> param)
			throws DataAccessException {
		return mapper.getServalOrderPrintLists(param);
	}
	
	@Override
	public List<T> getOrderPrintByParentId(Integer parentId) throws DataAccessException {
		return mapper.getOrderPrintByParentId(parentId);
	}

	@Override
	public void updateOrderPrintIsPrint(Map map) throws DataAccessException {
		mapper.updateOrderPrintIsPrint(map);
	}

	@Override
	public void updateOrderPrintIsSend(Map map) throws DataAccessException {
		mapper.updateOrderPrintIsSend(map);
	}
	
	@Override
	public void updateOrderPrintIsSendPrint(Map map) throws DataAccessException {
		mapper.updateOrderPrintIsSendPrint(map);
	}
	
	@Override
	public void updateOrderPrintByIds(Map param) throws DataAccessException {
		mapper.updateOrderPrintByIds(param);
	}

	@Override
	public void updateOrderPrintByTxLogisticIdIsSend(String txLogisticId)throws DataAccessException {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("txLogisticId", txLogisticId);
		try {
			mapper.updateOrderPrintByTxLogisticIdIsSend(map);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
	}

	@Override
	public void updateOrderNo(Map<String, Object> param)
			throws DataAccessException {
		mapper.updateOrderNo(param);
	}

}