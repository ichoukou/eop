package net.ytoec.kernel.service.impl;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dao.SMSServiceAreaDao;
import net.ytoec.kernel.dataobject.SMSServiceArea;
import net.ytoec.kernel.service.SMSServiceAreaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shitianzeng
 * 2012-07-05
 */
@Service
@Transactional
@SuppressWarnings("all")
public class SMSServiceAreaServiceImpl implements SMSServiceAreaService {

	@Inject
	private SMSServiceAreaDao smsServiceAreadao;

	protected final Logger logger = LoggerFactory
			.getLogger(SMSServiceAreaServiceImpl.class);
	
	@Override
	public List<SMSServiceArea> getSMSServiceAreaByParams(Integer smsTypeId, Integer userId) {
		return smsServiceAreadao.getSMSServiceAreaByParams(smsTypeId, userId);
	}
	
	@Override
	public boolean addSMSServiceArea(SMSServiceArea smsServiceArea) {
		boolean flag = false;
		if(smsServiceArea == null){
			logger.error(SMSServiceArea.class.getName() + "添加数据异常!");
		}else{
			smsServiceAreadao.addSMSServiceArea(smsServiceArea);
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean deleteSMSServiceArea(Integer id) {
		boolean flag = false;
		if(id == null || ("").equals(id.toString())){
			logger.error(SMSServiceArea.class.getName() + "删除数据异常!");
		}else{
			smsServiceAreadao.deleteSMSServiceArea(id);
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean updateSMSServiceArea(SMSServiceArea smsServiceArea) {
		boolean flag = false;
		if(smsServiceArea == null){
			logger.error(SMSServiceArea.class.getName() + "更新数据异常!");
		}else{
			smsServiceAreadao.updateSMSServiceArea(smsServiceArea);
			flag = true;
		}
		return flag;
	}

	@Override
	public SMSServiceArea getSMSServiceAreaById(Integer id) {
		SMSServiceArea smsServiceArea = null;
		if(id == null){
			logger.error(SMSServiceArea.class.getName() + "查找数据异常!");
		}else{
			smsServiceArea = (SMSServiceArea) smsServiceAreadao.getSMSServiceAreaById(id);
		}
		return smsServiceArea;
	}
}
