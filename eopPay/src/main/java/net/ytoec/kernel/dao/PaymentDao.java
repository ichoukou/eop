package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;


/**
 * 账户实体dao
 * @author guoliang.wang
 *
 * @param <T>
 */
public interface PaymentDao<T> extends BaseDao<T>{

	/**
	 * 查询交易（支付）明细表列表总记录数
	 * @param Map  查询条件拼成的map       
	 */	
	public Integer getPaymentListCount(Map map);
	
	/**
	 * 查询交易（支付）明细表列表
	 * @param Map  查询条件拼成的map
	 * @return List
	 */
	public  List<T> getPaymentList(Map map);

	/**
	 * 通过id修改remark标记
	 * @param T
	 */
	public boolean updateRemarkById(T Payment);

	/**
	 * 通过id修改del_flag
	 * @param T
	 */
	public boolean updateDeleteFlagById(T Payment);
	
	/**
	 * 查询交易（支付）根据用户id
	 * 未付款的订单(超过7天删除) 
	 */
	public  List<T> getPaymentListByUserId(Map map);
	
	/**
	 * 自动续费
	 *批量修改交易状态为：等待付款（交易（支付）明细表-ec_core_payment）
	 */
	public boolean updateBatchByDealStatus(Map map);

	/**
	 * 根据id降序排列
	 * @param map(userId,dealStatus,delFlag,dealName)
	 * @return
	 */
	public List<T> getList(Map map); 
	
	/**
	 * 根据ID更新dealMoney
	 * @param Payment
	 * @return
	 */
	public boolean updateDealMoneyById(T Payment);
	
	public  T getPaymentById(Map map);
}
