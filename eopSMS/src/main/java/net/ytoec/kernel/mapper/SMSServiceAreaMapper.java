package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.SMSServiceArea;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 服务所对应的地区
 * @author shitianzeng
 * 2012-07-05
 */
public interface SMSServiceAreaMapper<T extends SMSServiceArea> extends BaseSqlMapper<T> {

	/**
	 * 根据serviceId,userId获取此服务使用的地区列表
	 * @param smsTypeId
	 * @param userId
	 * @return
	 */
	public List<T> getSMSServiceAreaByParams(Map<String, Object> params);
	
	/**
	 * 添加数据
	 * @param smsServiceArea
	 */
	public void addSMSServiceArea(T smsServiceArea);
	
	/**
	 * 删除数据
	 * @param id
	 */
	public void deleteSMSServiceArea(Integer id);
	
	/**
	 * 更新数据
	 * @param smsServiceArea
	 */
	public void updateSMSServiceArea(T smsServiceArea);
	
	/**
	 * 查找数据
	 * @param id
	 * @return
	 */
	public T getSMSServiceAreaById(Integer id);
}
