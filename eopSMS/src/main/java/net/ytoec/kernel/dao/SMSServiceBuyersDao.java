package net.ytoec.kernel.dao;

/**
 * 营销定制服务所对应的买家
 * @author shitianzeng
 * 2012-07-05
 */
public interface SMSServiceBuyersDao<T> {
	
	/**
	 * 添加数据
	 * @param smsServiceBuyers
	 */
	public boolean addSMSServiceBuyers(T smsServiceBuyers);
	
	/**
	 * 删除数据
	 * @param id
	 */
	public boolean deleteSMSServiceBuyers(Integer id);
	
	/**
	 * 更新数据
	 * @param smsServiceBuyers
	 */
	public boolean updateSMSServiceBuyers(T smsServiceBuyers);
	
	/**
	 * 查找数据
	 * @param id
	 * @return
	 */
	public T getSMSServiceBuyersById(Integer id);
}
