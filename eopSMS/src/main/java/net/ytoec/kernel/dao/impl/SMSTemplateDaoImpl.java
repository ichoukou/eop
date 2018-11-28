package net.ytoec.kernel.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.SMSTemplateDao;
import net.ytoec.kernel.dataobject.SMSTemplate;
import net.ytoec.kernel.mapper.SMSTemplateMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * 短信服务中模版的dao层实现类
 * @author 
 *
 * @param <T>
 */
@Repository
public class SMSTemplateDaoImpl<T extends SMSTemplate> implements SMSTemplateDao<T> {

	private static final Logger logger=LoggerFactory.getLogger(SMSTemplateDaoImpl.class);
	
	@Inject 
	private SMSTemplateMapper<T> mapper;
	
	@Override
	public List<SMSTemplate> getSMSTemplateByUserId(Map<String, Object> params) {
		return mapper.getSMSTemplateByUserId(params);
	}
	
	@Override
	public Integer getSMSTemplateCountByUserId(Map<String, Object> params) {
		return mapper.getSMSTemplateCountByUserId(params);
	}


	@Override
	public boolean updateSMSTemplateToDefault(Integer templateId) {
		return mapper.updateSMSTemplateToDefault(templateId) > 0 ? true : false;
	}
	
	@Override
	public boolean addSMSTemplate(T temple) {
		boolean flag = false;
		try{
			mapper.addSMSTemplate(temple);
			flag = true;
		} catch(Exception e){
			
			flag = false;
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public boolean delSMSTemplateById(Integer id) {
		boolean flag = false;
		try{
			mapper.delSMSTemplateById(id);
			flag = true;
		} catch(Exception e){
			
			flag = false;
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public boolean editSMSTemplate(T temple) {
		boolean flag = false;
		try{
			mapper.editSMSTemplate(temple);
			flag = true;
		} catch(Exception e){
			
			flag = false;
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public T getSMSTemplateById(Integer id) {
		T temple = null;
		try{
			temple = mapper.getSMSTemplateById(id);
		} catch(Exception e){
			
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		
		return temple;
	}

	@Override
	public List<SMSTemplate> getTemplates(SMSTemplate info) {
		
		List<SMSTemplate> templates = null;
		try{
			templates = mapper.getTemplatesDesc(info);
		}catch(Exception e){
			
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		
		return templates;
	}

	@Override
	public boolean updateSMSTemplateToUnDefault(Integer templateId,
			Integer smsTypeId) {
		if (templateId == null || smsTypeId== null) {
			return false;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("templateId", templateId);
		params.put("smsTypeId", smsTypeId);
		return mapper.updateSMSTemplateToUnDefault(params) > 0 ? true : false;
	}

	@Override
	public boolean editTemplateOfAdminByParams(Map<String, Object> params) {
		boolean bool = false;
		try{
			mapper.editTemplateOfAdminByParams(params);
			bool = true;
		}catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return bool;
	}

	@Override
	public void updateSendCount(Map<String, Object> params) {
		
		try{
			mapper.updateSendCount(params);
		}catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		
	}

	@Override
	public SMSTemplate getDefaltTemplate(Map<String, Object> params)
			throws Exception {
		return mapper.getDefaltTemplate(params);
	}
}
