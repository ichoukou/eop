package net.ytoec.kernel.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dao.SMSTemplateDao;
import net.ytoec.kernel.dao.UserDao;
import net.ytoec.kernel.dataobject.SMSTemplate;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.SMSTemplateService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@SuppressWarnings("all")
public class SMSTemplateServiceImpl implements SMSTemplateService {

	private static final Logger logger = LoggerFactory.getLogger(SMSTemplateServiceImpl.class);
	
	@Inject 
	private SMSTemplateDao sMSTemplatedao;
	
	@Inject
	private UserDao userDao;
	
	@Override
	public List<SMSTemplate> getSMSTemplateByUserId(Map<String, Object> params,Pagination<SMSTemplate> pagination) {
		if (StringUtils.equals((String)params.get("isAdmin"), "1")) {
			if (StringUtils.isNotEmpty((String)params.get("shopName"))) {
				String shopName = (String)params.get("shopName");
				User tempUser = (User) userDao.getUserByShopName(shopName);
				if (tempUser != null) {
					params.put("userId", tempUser.getId());
				} else {
					return new ArrayList<SMSTemplate>();
				}
			} else {
				params.put("userId", "");
			}
		}
		String moduleStatus = (String)params.get("moduleStatus");
		List<String> moduleList = new ArrayList<String>();
		if (StringUtils.isNotEmpty(moduleStatus)) {
			String[] modules = moduleStatus.split(",");
			for (int i = 0; i < modules.length; i++) {
				moduleList.add(modules[i]);
			}
			params.put("moduleStatus", moduleList);
		} else {
			params.put("moduleStatus", null);
		}
		if(pagination != null){
			params.put("startIndex", pagination.getStartIndex());
	    	params.put("pageNum", pagination.getPageNum());
		}
		return sMSTemplatedao.getSMSTemplateByUserId(params);
	}

	@Override
	public Integer getSMSTemplateCountByUserId(
			Map<String, Object> params) {
		if (StringUtils.equals((String)params.get("isAdmin"), "1")) {
			if (StringUtils.isNotEmpty((String)params.get("shopName"))) {
				String shopName = (String)params.get("shopName");
				User tempUser = (User) userDao.getUserByShopName(shopName);
				if (tempUser != null) {
					params.put("userId", tempUser.getId());
				}else {
					return 0;
				}
			} else {
				params.put("userId", "");
			}
		}
		String moduleStatus = (String)params.get("moduleStatus");
		List<String> moduleList = new ArrayList<String>();
		if (StringUtils.isNotEmpty(moduleStatus)) {
			String[] modules = moduleStatus.split(",");
			for (int i = 0; i < modules.length; i++) {
				moduleList.add(modules[i]);
			}
			params.put("moduleStatus", moduleList);
		} else {
			params.put("moduleStatus", null);
		}
		return sMSTemplatedao.getSMSTemplateCountByUserId(params);
	}
	
	@Override
	public boolean updateSMSTemplateToDefault(Integer templateId, Integer smsTypeId) {
		if (sMSTemplatedao.updateSMSTemplateToDefault(templateId) && sMSTemplatedao.updateSMSTemplateToUnDefault(templateId, smsTypeId)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean addSMSTemplate(SMSTemplate temple) {
		boolean flag = false;
		if(temple == null){
			logger.error("##添加模版时传入参数为空！");
			return false;
		}
		return sMSTemplatedao.addSMSTemplate(temple);
	}

	@Override
	public boolean delSMSTemplateById(Integer id) {
		if("".equals(id) || id == null){
			logger.error("##删除模版时传入参数为空！");
			return false;
		}
		return sMSTemplatedao.delSMSTemplateById(id);
	}

	@Override
	public boolean editSMSTemplate(SMSTemplate temple) {
		if(temple == null){
			logger.error("##修改模版时传入参数为空！");
			return false;
		}
		return sMSTemplatedao.editSMSTemplate(temple);
	}

	@Override
	public SMSTemplate getSMSTemplateById(Integer id) throws Exception {
		if(id == null){
			throw new Exception("##根据id查询模版时传入参数为空！");
		}
		return (SMSTemplate) sMSTemplatedao.getSMSTemplateById(id);
	}

	@Override
	public List<SMSTemplate> getTemplates(SMSTemplate info) throws Exception {
		if(info == null){
			throw new Exception("##查询模版类型ID下的模版时传入参数为空");
		}
		return sMSTemplatedao.getTemplates(info);
	}

	@Override
	public boolean editTemplateOfAdminByParams(Map<String, Object> params) {
		if(params == null){
			logger.error("##管理员审核短信模版传参为空！");
		}
		
		return sMSTemplatedao.editTemplateOfAdminByParams(params);
	}

	@Override
	public void updateSendCountById(Map<String, Object> params) throws Exception {
		
		if(params.get("id") == null)
			throw new Exception("##参数模版ID必输！");
		if(params.get("sendCount") == null)
			throw new Exception("##参数短信发送数量必输！");
		
		sMSTemplatedao.updateSendCount(params);
		
	}

	@Override
	public SMSTemplate getDefaltTemplate(Map<String, Object> params)
			throws Exception {
		SMSTemplate smsTemplate = null;
		try {
			smsTemplate = sMSTemplatedao.getDefaltTemplate(params);
		} catch (Exception e) {
			logger.error("获取默认模版错误!", e);
		}
		if (smsTemplate == null) {
			smsTemplate = new SMSTemplate();
		}
		return smsTemplate;
	}
}
