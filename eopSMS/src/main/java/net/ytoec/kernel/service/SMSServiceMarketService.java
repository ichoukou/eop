package net.ytoec.kernel.service;

import net.ytoec.kernel.dataobject.SMSServiceMarket;

/**
 * 营销活动表
 * @author shitianzeng
 * 2012-07-05
 */
public interface SMSServiceMarketService {
	
	/**
	 * 添加数据
	 * @param smsServiceMarket
	 */
	public boolean addSMSServiceMarket(SMSServiceMarket smsServiceMarket);
	
	/**
	 * 删除数据
	 * @param id
	 */
	public boolean deleteSMSServiceMarket(Integer id);
	
	/**
	 * 更新数据
	 * @param smsServiceMarket
	 */
	public boolean updateSMSServiceMarket(SMSServiceMarket smsServiceMarket);
	
	/**
	 * 查找数据
	 * @param id
	 * @return
	 */
	public SMSServiceMarket getSMSServiceMarketById(Integer id);
}
