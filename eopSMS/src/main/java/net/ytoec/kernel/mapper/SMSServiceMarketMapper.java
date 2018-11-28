package net.ytoec.kernel.mapper;

import net.ytoec.kernel.dataobject.SMSServiceMarket;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 营销活动表
 * @author shitianzeng
 * 2012-07-05
 */
public interface SMSServiceMarketMapper<T extends SMSServiceMarket> extends BaseSqlMapper<T> {

	/**
	 * 添加数据
	 * @param smsServiceMarket
	 */
	public void addSMSServiceMarket(T smsServiceMarket);
	
	/**
	 * 删除数据
	 * @param id
	 */
	public void deleteSMSServiceMarket(Integer id);
	
	/**
	 * 更新数据
	 * @param smsServiceMarket
	 */
	public void updateSMSServiceMarket(T smsServiceMarket);
	
	/**
	 * 查找数据
	 * @param id
	 * @return
	 */
	public T getSMSServiceMarketById(Integer id);
}
