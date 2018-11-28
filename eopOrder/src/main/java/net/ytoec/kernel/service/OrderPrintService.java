package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.OrderPrint;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
/**
 * 面单打印服务类接口
 * @author wusha
 *
 * @param <T>
 */
public interface OrderPrintService<T extends OrderPrint>{
	
	/**
	 * 查询满足条件的记录  // 批量打印列表
	 * @param param
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getAllOrderPrint(Map<String, Object> param) throws DataAccessException;
	
	/**
	 * 查询满足条件的记录数
	 * @param param 
	 * @return
	 * @throws DataAccessException
	 */
	public Integer getTotal(Map<String, Object> param) throws DataAccessException;
	
	/**
	 * 查询满足条件的记录  // 代发货列表
	 * @param param
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getUndeliverOrderPrint(Map<String, Object> param) throws DataAccessException;
	
	/**
	 * 代发货列表记录数
	 * @param param
	 * @return
	 * @throws DataAccessException
	 */
	public Integer getUndeliverTotal(Map<String, Object> param) throws DataAccessException;
	
	/**
	 * 查询满足条件的记录  // 已发货列表
	 * @param param
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getDeliveredOrderPrint(Map<String, Object> param) throws DataAccessException;
	
	/**
	 * 已发货列表记录数
	 * @param param
	 * @return
	 * @throws DataAccessException
	 */
	public Integer getDeliveredTotal(Map<String, Object> param) throws DataAccessException;
	
	/**
	 * 添加面单打印EC_CORE_ORDER_PRINT中的记录
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public boolean addOrderPrint(T entity) throws DuplicateKeyException,DataAccessException ;
	
	/**
	 * 根据ID获取面单打印对象
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public Object getOrderPrint(Integer id) throws DataAccessException;
	
	/**
	 * 根据ID更新此面单打印对象
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public void updateOrderPrint(OrderPrint orderPrint) throws DataAccessException;
	
	/**
	 * 根据ID更新此面单号
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public void updateOrderNo(Map<String, Object> param) throws DataAccessException;
	
	/**
	 * 解密xml串
	 */
	public String unescape(String src);
	
	/**
	 * XML 转 JSONE 串
	 */
	public String convertXMLToJson(String src);
	
	/**
	 * 获取给定ids的对象 用in来获取
	 */
	public List<OrderPrint> getServalOrderPrintLists(Map<String, Object> param) throws DataAccessException;
	
	/**
	 *  通过 parent_id 查出订单列表
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public  List<T> getOrderPrintByParentId(Integer integer) throws DataAccessException;
	
	/**
	 * 根据ID 修改打印快速单状态
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public void updateOrderPrintIsPrint(Map map) throws DataAccessException;
	
	
	/**
	 * 根据ID 修改打印发货单状态
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public void updateOrderPrintIsSendPrint(Map map) throws DataAccessException;	

	/**
	 * 根据ID 修改批量打印状态
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public void updateOrderPrintIsSend(Map map) throws DataAccessException;	
	
	/**
	 * 不想看到它 update is_print为N
	 * @param param
	 * @throws DataAccessException
	 */
	public void updateOrderPrintByIds(Map param) throws DataAccessException;
	
	  /**
	   * 批量发货
	   * @param orderPrintIds
	   * @param topSession
	   * @param failedMap  发货不成功的orderPrintId，及原因
	   * @throws DataAccessException
	   */
    public Map<String, Object> batchOrderPrintSend(String orderPrintIds,String topSession) throws DataAccessException;
}