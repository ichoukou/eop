package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.Payment;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * Mapper
 * @author guoliang.wang
 *
 * @param <T>
 */
public interface PaymentMapper<T extends Payment> extends BaseSqlMapper<T>  {

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
	public void updateRemarkById(T payment);

	/**
	 * 通过id修改del_flag
	 * @param T
	 */
	public void updateDeleteFlagById(T payment);
	
	
	/**
	 * 查询交易（支付）根据用户id
	 */
	public  List<T> getPaymentListByUserId(Map map);
	
	
	/**
	 * 自动续费
	 *批量修改交易状态为：等待付款（交易（支付）明细表-ec_core_payment）
	 */
	public void updateBatchByDealStatus(Map map);
	
	/**
	 * 根据id降序排列
	 * @param map(userId,dealStatus,delFlag,dealName)
	 * @return
	 */
	public List<T> getList(Map map); 
	
	/**
	 * 根据id 更新dealMoney
	 * @param payment
	 */
	public void updateDealMoneyById(T Payment);
	
	public  T getPaymentById(Map map);
}
