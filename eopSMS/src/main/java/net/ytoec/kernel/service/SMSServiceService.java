package net.ytoec.kernel.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.SMSService;
import net.ytoec.kernel.dataobject.User;

/**
 * 短信服务service
 * @author 
 *
 */
public interface SMSServiceService {
	
	/**
	 * 当充值服务开启短信服务时插入短信子服务
	 * @return
	 */
	public boolean  saveServiceByIsOn(Integer userId, Integer subUserId);
	
	/**
	 * 根据主账号ID获取所有的短信服务
	 * @return
	 */
	public List<SMSService> getSMSServiceAll(Integer userId);
	
	/**
	 * 根据服务名称和主账号ID获取服务，返回短信服务对象
	 * @param name
	 * @param userId 
	 * @return
	 */
	public SMSService getSMSServiceByServiceName(Map<String,Object> params);
	
	/**
	 * 添加
	 * @param service
	 * @return
	 */
	public boolean addSMSService(SMSService service);
	
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
	public boolean editSMSService(SMSService service);
	
	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public SMSService getSMSServiceById(Integer id) throws Exception;

	/**
	 * 根据主账号ID下已开启的短信服务
	 * @param integer
	 * @return
	 * @throws Exception 
	 */
	public List<SMSService> getSMSServicesByUserId(Integer userId) throws Exception;

	/**
	 * 根据用户获取服务（分仓用户、非分仓用户）
	 * @param param
	 * @return
	 */
	public List<SMSService> getSMSService(HashMap<String, Object> param,boolean bool);

	/**
	 * 获取createUserId为平台用户的分仓用户获取服务[平台账号开启]
	 * @param params
	 * @return
	 */
	public SMSService getSMSServiceByServiceNameFenCang(Map<String, Object> params);
	
	/**
	 * 获取createUserId为平台用户的分仓用户获取服务[平台账号关闭]
	 * @param params
	 * @return
	 */
	public List<SMSService> getSMSServiceByServiceNameFenCangList(Map<String, Object> params);

	public List<SMSService> getSMSServiceAllForToAdd(Integer userId);

	/**
	 * 获取分仓账号的服务
	 * @param userIdParten
	 * @return
	 */
	public List<SMSService> getSMSServiceForFengCang(
			HashMap<String, Object> userIdParten,boolean bool);
	
	/**
	 * 是否开启服务,根据  id,服务类型
	 * @param userId                  当前用户ID
	 * @param serivceType             发货提醒(GOT)，派件提醒(SENT_SCAN)，签收提醒(SIGNED)
	 * @return
	 */
	public boolean isOpeanService(Integer userId,String serivceType);
	/**
	 * 是否开启服务,根据 user,服务类型
	 * @param map
	 * @param user
	 * @return
	 */
	public boolean isServiceOpean(Map<String, Object> map,User user);
	
	/**
	 * 获取开启服务,根据  id,服务类型
	 * @param userId                  当前用户ID
	 * @param serivceType             发货提醒(GOT)，派件提醒(SENT_SCAN)，签收提醒(SIGNED)
	 * @return
	 */
	public SMSService getOpeanService(Integer userId, String serivceType);

	/**
	 * 分仓用户开启服务时，
	 * @param service
	 * @return
	 */
	public boolean updateServiceForFenCang(SMSService service);
}
