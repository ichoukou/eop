package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.OrderFormInfo;

import org.apache.struts2.interceptor.SessionAware;
import org.springframework.dao.DataAccessException;

/**
 * 订单导入的临时订单的服务入口
 * */
@SuppressWarnings("all")
public interface OrderTempService<T> extends SessionAware {

	/**
	 * 创建临时订单信息
	 * 
	 * @param entity
	 * @return @
	 */

	public boolean addOrderTemp(T entity);

	/**
	 * 根据Id读取单个临时订单
	 * 
	 * @param id
	 * @return @
	 */
	public T getOrderTempById(Integer id);

	/**
	 * 获得所有临时订单
	 * 
	 * @return @
	 */
	public List<T> getAllOrderTemp();

	/**
	 * 删除一个临时订单
	 * 
	 * @param entity
	 * @return @
	 */
	public abstract boolean delOrderTemp(T entity);

	/**
	 * 根据运单号读取单个临时
	 * 
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getOrderTempByMailNoRemark(String mailNo,String remark);

	
	/**
	 * 获得当前用户导入的临时数据
	 * 
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getOrderTempListByRemark(String remark);
	
	public List<T> getOrderTempListByRemarkPage(String remark,Pagination pagination,String mailNoOrTraderNo); 

	/**
	 * 根据物流号读取单个临时
	 * 
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getOrderTempLogisticIdRemark(String txLogisticId ,String remark);

	
	/**
	 * 创建订单信息,由OrderFormInfo中获取相关信息.（订单相关表：product、traderInfo）
	 * 
	 * @param orderFormInfo
	 * @return @
	 */
	public boolean addOrderFormInfo(OrderFormInfo orderFormInfo);
	
	public int countOrderTempListByMap(String remark);
	
	public boolean addOrderTempList(List<T> list);
	
}
