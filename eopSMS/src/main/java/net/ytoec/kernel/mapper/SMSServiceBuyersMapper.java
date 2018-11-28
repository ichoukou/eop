package net.ytoec.kernel.mapper;

import net.ytoec.kernel.dataobject.SMSServiceBuyers;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 营销定制服务所对应的买家
 * @author shitianzeng
 * 2012-07-05
 */
public interface SMSServiceBuyersMapper<T extends SMSServiceBuyers> extends BaseSqlMapper<T> {
	
	/**
	 * 添加数据
	 * @param smsServiceBuyers
	 */
	public void addSMSServiceBuyers(T smsServiceBuyers);
	
	/**
	 * 删除数据
	 * @param id
	 */
	public void deleteSMSServiceBuyers(Integer id);
	
	/**
	 * 更新数据
	 * @param smsServiceBuyers
	 */
	public void updateSMSServiceBuyers(T smsServiceBuyers);
	
	/**
	 * 查找数据
	 * @param id
	 * @return
	 */
	public T getSMSServiceBuyersById(Integer id);

}
