package net.ytoec.kernel.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.SMSService;

/**
 * 短信服务中的service  dao层
 * @author 
 *
 * @param <T>
 */
public interface SMSServiceDao<T> {

	/**
	 * 获取短信服务列表
	 * @return
	 */
	public List<SMSService> getSMSServiceAll(Integer userId);
	
	/**
	 * 根据服务名称获取服务
	 * @param serviceName
	 * @param userId 
	 * @return
	 */
	public SMSService getSMSServiceByServiceName(Map<String,Object> params);
	
	/**
	 * 添加
	 * @param service
	 * @return
	 */
	public boolean addSMSService(T service);

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public boolean delSMSServiceById(Integer id);

	/**
	 * 修改
	 * @param service
	 * @return
	 */
	public boolean editSMSService(T service);

	/**
	 * 查询对象
	 * @param id
	 * @return
	 */
	public T getSMSServiceById(Integer id);

	/**
	 * 根据主账号ID下已开启的短信服务
	 * @param integer
	 * @return
	 */
	public List<SMSService> getSMSServicesByUserId(Integer userId);

	/**
	 * 分仓用户获取服务
	 * @param params
	 * @return
	 */
	public SMSService getSMSServiceByServiceNameFenCang(Map<String, Object> params);
	
	public List<SMSService> getSMSServiceByServiceNameFenCangList(Map<String, Object> params);

	public List<SMSService> getSMSServiceAllForToAdd(Integer userId);

	/**
	 * 分仓开启服务时使用
	 * @param service
	 * @return
	 */
	public boolean updateServiceForFenCang(SMSService service);

}
