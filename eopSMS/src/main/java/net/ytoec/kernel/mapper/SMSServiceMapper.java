package net.ytoec.kernel.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.SMSService;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

public interface SMSServiceMapper<T extends SMSService> extends BaseSqlMapper<T> {


	/**
	 * 获取短信服务列表
	 * @return
	 */
	public List<SMSService> getSMSServiceAll(Integer userId);
	
	/**
	 * 根据服务名称获取服务
	 * @param serviceName
	 * @return
	 */
	public SMSService getSMSServiceByServiceName(Map<String,Object> params);
	
	/**
	 * 添加短信子服务
	 * @param service
	 */
	public void addSMSService(T service);

	/**
	 * 删除短息子服务
	 * @param id
	 */
	public void delSMSServiceById(Integer id);

	/**
	 * 修改短息子服务
	 * @param service
	 */
	public void editSMSService(T service);

	/**
	 * 查询对象
	 * @param id
	 * @return
	 */
	public T getSMSServiceById(Integer id);

	/**
	 * 根据主账号ID下已开启的短信服务
	 * @param userId
	 * @return
	 */
	public List<SMSService> getSMSServicesByUserId(Integer userId);

	/**
	 * 分仓账号获取服务
	 * @param params
	 * @return
	 */
	public SMSService getSMSServiceByServiceNameFenCang(Map<String, Object> params);

	/**
	 * toAdd模版页面时获取短信类型
	 * @param userId
	 * @return
	 */
	public List<SMSService> getSMSServiceAllForToAdd(Integer userId);

	/**
	 * 分仓账号登陆时获取平台账号的服务
	 * @param createUserId
	 * @return
	 */
	public List<SMSService> getSMSServiceByCreateUserId(Integer createUserId);

	public List<SMSService> getSMSServiceByServiceNameFenCangList(
			Map<String, Object> params);

	/**
	 * 分仓开启服务
	 * @param service
	 * @return
	 */
	public void updateServiceForFenCang(SMSService service);

}
