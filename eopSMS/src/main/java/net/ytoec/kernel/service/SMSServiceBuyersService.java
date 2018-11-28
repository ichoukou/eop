package net.ytoec.kernel.service;

import java.util.List;
import net.ytoec.kernel.dataobject.SMSBuyers;
import net.ytoec.kernel.dataobject.SMSServiceBuyers;

/**
 * 营销定制服务所对应的买家
 * @author shitianzeng
 * 2012-07-05
 */
public interface SMSServiceBuyersService {
	
	/**
	 * 根据marketId获取营销活动针对的买家
	 * @param marketId
	 * @return
	 */
	public List<SMSBuyers> getSMSBuyersByMarketId(Integer marketId);
	
	/**
	 * 添加数据
	 * @param smsServiceBuyers
	 */
	public boolean addSMSServiceBuyers(SMSServiceBuyers smsServiceBuyers);
	
	/**
	 * 删除数据
	 * @param id
	 */
	public boolean deleteSMSServiceBuyers(Integer id);
	
	/**
	 * 更新数据
	 * @param smsServiceBuyers
	 */
	public boolean updateSMSServiceBuyers(SMSServiceBuyers smsServiceBuyers);
	
	/**
	 * 查找数据
	 * @param id
	 * @return
	 */
	public SMSServiceBuyers getSMSServiceBuyersById(Integer id);
	
}
