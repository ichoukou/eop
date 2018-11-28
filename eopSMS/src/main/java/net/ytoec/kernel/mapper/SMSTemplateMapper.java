package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.SMSTemplate;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

public interface SMSTemplateMapper<T extends SMSTemplate> extends BaseSqlMapper<T> {

	/**
	 * 获取当前用户的模版数量
	 * @param params
	 *  userId			(此处如果为子帐号，请传入主帐号的ID)
	 *	smsTypeId 		(服务ID)
	 *  moduleStatus	(模版状态 Y 审核通过 N 未通过, M 审核中, D已删除 ,S系统默认模版)
	 *	shopName		(店铺名称)
	 *	isAdmin			(是否是admin) 此字段必需填写 1为admin， 0为其他
	 * @return
	 */
	public Integer getSMSTemplateCountByUserId(Map<String, Object> params);
	
	/**
	 * 获取当前用户的模版列表
	 * @param params
	 *  userId			(此处如果为子帐号，请传入主帐号的ID)
	 *	smsTypeId 		(服务ID)
	 *  moduleStatus	(模版状态)
	 *	shopName		(店铺名称)
	 *	isAdmin			(是否是admin) 此字段必需填写 1为admin， 0为其他
	 * @return
	 */
	public List<SMSTemplate> getSMSTemplateByUserId(Map<String, Object> params);
	
	/**
	 * 根据模版ID把当前模版改为默认模版
	 * @param templateId
	 * @return
	 */
	public Integer updateSMSTemplateToDefault(Integer templateId);
	
	/**
	 * 根据params把当前模版改为非默认模版
	 * @param Map<String, Object> params
	 * @return
	 */
	public Integer updateSMSTemplateToUnDefault(Map<String, Object> params);
	
	
	/**
	 * 添加模版
	 * @param temple
	 */
	public void addSMSTemplate(T temple);

	/**
	 * 删除模版
	 * @param id
	 */
	public void delSMSTemplateById(Integer id);

	/**
	 * 修改
	 * @param temple
	 */
	public void editSMSTemplate(T temple);

	/**
	 * 根据Id获取模版
	 * @param id
	 * @return
	 */
	public T getSMSTemplateById(Integer id);

	/**
	 * 查询模版类型ID下的模版集合
	 * @param info
	 * @return
	 */
	public List<SMSTemplate> getTemplates(SMSTemplate info);

	/**
	 * 管理员审核短信模版
	 * @param params
	 * @return
	 */
	public void editTemplateOfAdminByParams(Map<String, Object> params);

	/**
	 * 根据模版ID修改通过该模版发送的短信数量
	 * @param params  
	 * id  			模版ID		必输
	 * sendCount    发送数量		必输
	 * @throws Exception 
	 * 
	 */
	public void updateSendCount(Map<String, Object> params);
	
	/**
	 * 获取用户此功能的默认模版
	 * @return
	 * @throws Exception
	 */
	public SMSTemplate getDefaltTemplate(Map<String, Object> params) throws Exception;

	/**
	 * 模版管理updateTime降序
	 * @param info
	 * @return
	 */
	public List<SMSTemplate> getTemplatesDesc(SMSTemplate info);

}
