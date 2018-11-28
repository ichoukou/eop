package net.ytoec.kernel.service;

import java.util.List;

import net.ytoec.kernel.dataobject.SMSServiceArea;

/**
 * 服务所对应的地区
 * @author shitianzeng
 * 2012-07-05
 */
public interface SMSServiceAreaService {
	
	/**
	 * 根据serviceId,userId获取此服务使用的地区列表
	 * @param smsTypeId
	 * @param userId
	 * @return
	 */
	public List<SMSServiceArea> getSMSServiceAreaByParams(Integer smsTypeId, Integer userId);
	
	/**
	 * 添加数据
	 * @param smsServiceArea
	 */
	public boolean addSMSServiceArea(SMSServiceArea smsServiceArea);
	
	/**
	 * 删除数据
	 * @param id
	 */
	public boolean deleteSMSServiceArea(Integer id);
	
	/**
	 * 更新数据
	 * @param smsServiceArea
	 */
	public boolean updateSMSServiceArea(SMSServiceArea smsServiceArea);
	
	/**
	 * 查找数据
	 * @param id
	 * @return
	 */
	public SMSServiceArea getSMSServiceAreaById(Integer id);
}
