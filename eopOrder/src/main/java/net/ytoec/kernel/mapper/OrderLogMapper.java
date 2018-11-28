package net.ytoec.kernel.mapper;

import java.util.Map;

import org.springframework.dao.DataAccessException;


import net.ytoec.kernel.dataobject.OrderLog;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;



public interface OrderLogMapper extends BaseSqlMapper{
	
	/**
	 * 添加订单物流号信息，到ec_core_order_log中
	 * @param txLogisticId
	 * @return
	 * @throws DataAccessException
	 */
	public void addOrderLog(String txLogisticId) throws DataAccessException;
	
	/**
	 * 修改(记录COD信息)
	 * @param map
	 */
	public void updateOrderLog(OrderLog orderLog);
	

}
