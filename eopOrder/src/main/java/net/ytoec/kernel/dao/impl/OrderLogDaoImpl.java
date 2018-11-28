package net.ytoec.kernel.dao.impl;


import javax.inject.Inject;

import net.ytoec.kernel.dao.OrderLogDao;
import net.ytoec.kernel.dataobject.OrderLog;
import net.ytoec.kernel.mapper.OrderLogMapper;
import net.ytoec.kernel.util.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

@Repository
public class OrderLogDaoImpl implements OrderLogDao {

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory.getLogger(OrderPrintDaoImpl.class);
	
	@Inject
	private OrderLogMapper mapper;
	
	@Override
	public boolean addOrderLog(String txLogisticId) throws DuplicateKeyException,DataAccessException{
		// TODO Auto-generated method stub
		boolean flag = false;
		try {
			this.mapper.addOrderLog(txLogisticId);
			flag = true;
		} catch (DuplicateKeyException e) {
			// TODO: handle exception
			throw e;
		}
		return flag;
	}

	@Override
	public void updateOrderLog(String txLogisticId, String payAmount, String payTime,
			String unitId, String employeeId, String remark) {
		// TODO Auto-generated method stub
		OrderLog orderLog = new OrderLog();
		orderLog.setEmployeeId(employeeId);
		orderLog.setTxLogisticId(txLogisticId);
		orderLog.setPayAmount(payAmount);
		orderLog.setPayTime(DateUtil.valueof(payTime, "yyyy-MM-dd HH:mm:ss"));
		orderLog.setUnitId(unitId);
		orderLog.setRemark(remark);
		mapper.updateOrderLog(orderLog);
	}

}
