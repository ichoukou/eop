package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.DredgeServiceDao;
import net.ytoec.kernel.dataobject.DredgeService;
import net.ytoec.kernel.mapper.DredgeServiceMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("all")
public class DredgeServiceDaoImpl<T extends DredgeService> implements DredgeServiceDao<T> {
	
	private static final Logger logger = LoggerFactory.getLogger(DredgeServiceDaoImpl.class);

	@Inject
	private DredgeServiceMapper<T> mapper;
	
	
	@Override
	public boolean updateDredgeserviceBalanceById(T dredgeService) {
		boolean flag=true;
		try{
			   mapper.updateDredgeserviceBalanceById(dredgeService);
			   
		}catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(),e);
			flag=false;
		}
		return flag;
	}

	@Override
	public List<T> getDredgeserviceList(Map map) {
		List<T> list=null;
		try {
			list=mapper.getDredgeserviceList(map);
		}
		catch(Exception e) {
			logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue(), e);
		}
		return list;
	}

	@Override
	@Deprecated
	public boolean updateBalanceByUserId(T dredgeService) {
		boolean flag=true;
		try{
			mapper.updateBalanceByUserId(dredgeService);
		}catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(),e);
			flag=false;
		}
		return flag;
	}

	@Override
	public boolean updateFlagById(T dredgeService) {
		boolean flag=true;
		try{
			mapper.updateFlagById(dredgeService);
		}catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(),e);
			flag=false;
		}
		return flag;
	}

	@Override
	public List<T> getOpenserviceList(Map map) {
		List<T> list=null;
		try{
			list = mapper.getOpenserviceList(map);
		}catch(Exception e){
			logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue(),e);
		}
		return list;
	}

	@Override
	public boolean add(T entity) {
		boolean flag=true;
		try{
			mapper.add(entity);
			
		}catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(),e);
			flag=false;
		}
		return flag;
	}

	@Override
	public boolean edit(T entity) {
		boolean flag=true;
		try{
			mapper.edit(entity);
			
		}catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(),e);
			flag=false;
		}
		return flag;
	}

	@Override
	public boolean remove(T entity) {
		boolean flag=true;
		try{
			mapper.remove(entity);
			
		}catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(),e);
			flag=false;
		}
		return flag;
	}

	@Override
	public T get(T entity) {
		
		try{
			return mapper.get(entity);			
		}catch(Exception e){
			logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue(),e);			
		}
		return null;
	}

	@Override
	public List<T> getServiceRecordByUserId(Map map){	
		
		List<T> list=null;
		try {
			list=mapper.getServiceRecordByUserId(map);
		}
		catch(Exception e) {
			logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue(), e);
		}
		return list;		
	}

	@Override
	public boolean updateDredgeserviceBySMS(T Dredgeservice) {
		int i=	mapper.updateDredgeserviceBySMS(Dredgeservice);
		if(i==1){
			return true;
		}
		return false;
	}

	@Override
	public boolean insertBatch(List<T> list) {
		boolean flag=true;
		try{
			mapper.insertBatch(list);
			
		}catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(),e);
			flag=false;
		}
		return flag;
	}

	@Override
	public List<T> getOpenserviceTimeList() {
		
		return mapper.getOpenserviceTimeList();
	}

	@Override
	@Deprecated
	public boolean updateBatch(List<DredgeService> list) {
		return mapper.updateBatch(list);
	}

	/**
	 *根据id,serviceId,flag(主要用于支付、付款操作)
	 * @return boolean
	 */
	@Override
	public boolean updateDredgeserviceUseAlipay(T Dredgeservice) {	
		boolean flag=true;
		try{
			mapper.updateDredgeserviceUseAlipay(Dredgeservice);			
		}catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(),e);
			flag=false;
		}
		return flag;
	}
    
	/**
	 * 判断用户是否开通了某项服务
	 * @param map(userId,serviceName)
	 * @return boolean
	 */
	public boolean getServiceByUserIdAndServiceName(Map map)
	{
		Integer res = -1;
		try{
			res = mapper.getServiceByUserIdAndServiceName(map);		
		}catch(Exception e){
			logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue(),e);
		}
		
		if(res > 0)
			return true;
		else
			return false;
	}

	@Override
	public Integer getDredgeserviceListCount(Map map) {
		// TODO Auto-generated method stub
		return mapper.getDredgeserviceListCount(map);
	}

	@Override
	public List<T> getOpenserviceListByName(List<DredgeService> list) {
		
		return mapper.getOpenserviceListByName(list);
	}
}
