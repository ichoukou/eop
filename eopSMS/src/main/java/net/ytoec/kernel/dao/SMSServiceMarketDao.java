package net.ytoec.kernel.dao;

/**
 * 营销活动表
 * @author shitianzeng
 * 2012-07-05
 */
public interface SMSServiceMarketDao<T> {

	/**
	 * 添加数据
	 * @param smsServiceMarket
	 */
	public boolean addSMSServiceMarket(T smsServiceMarket);
	
	/**
	 * 删除数据
	 * @param id
	 */
	public boolean deleteSMSServiceMarket(Integer id);
	
	/**
	 * 更新数据
	 * @param smsServiceMarket
	 */
	public boolean updateSMSServiceMarket(T smsServiceMarket);
	
	/**
	 * 查找数据
	 * @param id
	 * @return
	 */
	public T getSMSServiceMarketById(Integer id);
}
