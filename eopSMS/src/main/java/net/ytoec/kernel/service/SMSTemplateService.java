package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.SMSTemplate;

public interface SMSTemplateService {
	
	/**
	 * 根据模版ID修改通过该模版发送的短信数量
	 * @param params  
	 * id  			模版ID		必输
	 * sendCount    发送数量		必输
	 * @throws Exception 
	 * 
	 */
	public void updateSendCountById(Map<String,Object> params) throws Exception;
	
	/**
	 * 获取当前用户的模版数量
	 * @param params
	 *  userId			(此处如果为子帐号，请传入主帐号的ID)
	 *	name 		(服务名)
	 *  smsTypeId   服务ID
	 *  moduleStatus (模版状态 Y 审核通过 N 未通过, M 审核中, D已删除 ,S系统默认模版)   注：以“,”分割
	 *	shopName		(店铺名称)
	 *	isAdmin			(是否是admin) 此字段必需填写 1为admin， 0为其他
	 * @return
	 */
	public Integer getSMSTemplateCountByUserId(Map<String, Object> params);
	
	/**
	 * 获取当前用户的模版列表
	 * @param params
	 *  userId			(此处如果为子帐号，请传入主帐号的ID)
	 *	name 		(服务名)
	  * smsTypeId   服务ID
	 *  moduleStatus	(模版状态 Y 审核通过 N 未通过, M 审核中, D已删除 ,S系统默认模版) 注：以“,”分割
	 *	shopName		(店铺名称)
	 *	isAdmin			(是否是admin) 此字段必需填写 1为admin， 0为其他
	 *  pagination
	 * @return
	 */
	public List<SMSTemplate> getSMSTemplateByUserId(Map<String, Object> params,Pagination<SMSTemplate> pagination);
	
	/**
	 * 根据模版ID把当前模版改为默认模版
	 * @param templateId
	 * @return
	 */
	public boolean updateSMSTemplateToDefault(Integer templateId, Integer smsTypeId);
	
	/**
	 * 添加模版
	 * @param temple
	 * @return
	 */
	public boolean addSMSTemplate(SMSTemplate temple);
	
	/**
	 * 删除模版
	 * @param id
	 * @return
	 */
	public boolean delSMSTemplateById(Integer id);
	
	/**
	 * 修改模版
	 * @param temple
	 * @return
	 */
	public boolean editSMSTemplate(SMSTemplate temple);
	
	/**
	 * 查询单个模版
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public SMSTemplate getSMSTemplateById(Integer id) throws Exception;

	/**
	 * 根据模版类型ID和主账号ID查询模版集合
	 * @param info
	 * @return
	 * @throws Exception 
	 */
	public List<SMSTemplate> getTemplates(SMSTemplate info) throws Exception;

	/**
	 * 管理员审核短信模版
	 * @param params
	 * @return
	 */
	public boolean editTemplateOfAdminByParams(Map<String, Object> params);
	
	/**
	 * 获取用户此功能的默认模版
	 * @return
	 * @throws Exception
	 */
	public SMSTemplate getDefaltTemplate(Map<String, Object> params) throws Exception;
}
