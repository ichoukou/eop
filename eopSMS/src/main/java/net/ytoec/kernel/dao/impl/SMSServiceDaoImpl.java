package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.SMSServiceDao;
import net.ytoec.kernel.dataobject.SMSService;
import net.ytoec.kernel.mapper.SMSServiceMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class SMSServiceDaoImpl<T extends SMSService> implements SMSServiceDao<T> {

	private static final Logger logger=LoggerFactory.getLogger(SMSServiceDaoImpl.class);
	
	@Inject
	private SMSServiceMapper<T> mapper;
	
	@Override
	public List<SMSService> getSMSServiceAll(Integer userId) {
		return mapper.getSMSServiceAll(userId);
	}
	
	@Override
	public SMSService getSMSServiceByServiceName(Map<String,Object> params) {
		return mapper.getSMSServiceByServiceName(params);
	}
	
	@Override
	public SMSService getSMSServiceByServiceNameFenCang(Map<String,Object> params) {
		return mapper.getSMSServiceByServiceNameFenCang(params);
	}
	
	@Override
	public List<SMSService> getSMSServiceByServiceNameFenCangList(Map<String,Object> params) {
		return mapper.getSMSServiceByServiceNameFenCangList(params);
	}
	
	@Override
	public boolean addSMSService(T service) {
		boolean flag = false;
		try{
			mapper.addSMSService(service);
			flag = true;
		} catch(Exception e){
			
			flag = false;
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public boolean delSMSServiceById(Integer id) {
		boolean flag = false;
		try{
			mapper.delSMSServiceById(id);
			flag = true;
		} catch(Exception e){
			
			flag = false;
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public boolean editSMSService(T service) {
		boolean flag = false;
		try{
			mapper.editSMSService(service);
			flag = true;
		} catch(Exception e){
			
			flag = false;
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public T getSMSServiceById(Integer id) {
		T service = null;
		try{
			service = mapper.getSMSServiceById(id);
		} catch(Exception e){
			
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		
		return service;
	}

	@Override
	public List<SMSService> getSMSServicesByUserId(Integer userId) {
		List<SMSService> service = null;
		try{
			service = mapper.getSMSServicesByUserId(userId);
		}catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return service;
	}

	@Override
	public List<SMSService> getSMSServiceAllForToAdd(Integer userId) {
		return mapper.getSMSServiceAllForToAdd(userId);
	}

	@Override
	public boolean updateServiceForFenCang(SMSService service) {
		boolean flag = false;
		try{
			mapper.updateServiceForFenCang(service);
			flag = true;
		} catch(Exception e){
			flag = false;
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}
}
