package net.ytoec.kernel.dao;

import java.util.List;

/**
 * 服务所对应的地区
 * @author shitianzeng
 * 2012-07-05
 */
public interface SMSServiceAreaDao<T> {
	/**
	 * 添加数据
	 * @param smsServiceArea
	 */
	public boolean addSMSServiceArea(T smsServiceArea);
	
	/**
	 * 删除数据
	 * @param id
	 */
	public boolean deleteSMSServiceArea(Integer id);
	
	/**
	 * 更新数据
	 * @param smsServiceArea
	 */
	public boolean updateSMSServiceArea(T smsServiceArea);
	
	/**
	 * 查找数据
	 * @param id
	 * @return
	 */
	public T getSMSServiceAreaById(Integer id);
	
	/**
	 * 根据serviceId,userId获取此服务使用的地区列表
	 * @param smsTypeId
	 * @param userId
	 * @return
	 */
	public List<T> getSMSServiceAreaByParams(Integer smsTypeId, Integer userId);
}
