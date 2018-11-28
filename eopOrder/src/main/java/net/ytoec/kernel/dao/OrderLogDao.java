package net.ytoec.kernel.dao;

import net.ytoec.kernel.dataobject.OrderLog;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;

public interface OrderLogDao {

    
    /**
     * 添加订单物流号信息，到ec_core_order_log中
     * @param txLogisticId
     * @return
     * @throws DataAccessException
     */
    public boolean addOrderLog(String txLogisticId)throws DuplicateKeyException,DataAccessException;
    
    /**
     * 修改  （记录COD信息）
     */
    public void updateOrderLog(String txLogisticId, String payAmount, String payTime,
            String unitId, String employeeId, String remark);
}
