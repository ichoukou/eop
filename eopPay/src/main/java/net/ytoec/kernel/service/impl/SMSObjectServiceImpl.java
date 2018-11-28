package net.ytoec.kernel.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.TechcenterEnum;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.SMSObjectDao;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.service.SMSObjectService;
import net.ytoec.kernel.techcenter.api.SMSSender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techcenter.protocol.standard12.Standard_Submit;

/**
 * 短信表实体类Service实现类
 * @author guolongchao
 * 20120808
 */
@Service
@Transactional
@SuppressWarnings("all")
public class SMSObjectServiceImpl<T extends SMSObject> implements SMSObjectService<T> {

	private static final Logger logger = LoggerFactory.getLogger(SMSObjectServiceImpl.class);
	
	@Inject
	private SMSObjectDao<SMSObject> smsDao;
	
	@Override
	public boolean add(T entity) {		
		return smsDao.add(entity);
	}

	@Override
	public boolean edit(T entity) {
		return smsDao.edit(entity);
	}

	@Override
	public boolean remove(T entity) {
		return smsDao.remove(entity);
	}

	@Override
	public T get(T entity) {
		return (T) smsDao.get(entity);
	}

	@Override
	public List<T> getList(Map map,Pagination<T> pagination) throws DataAccessException {
		if(pagination==null)
		{
			logger.error("参数pagination"+LogInfoEnum.PARAM_EMPTY.getValue());	
			return null;
		}
		
		map.put("startIndex", pagination.getStartIndex()); // 设置分页参数
		map.put("pageNum", pagination.getPageNum());
		
		return (List<T>) smsDao.getList(map);
	}

	@Override
	public Integer getRecordsCount(Map map) {
		return smsDao.getRecordsCount(map);
	}

	@Override
	public List<T> getList(Map map) throws DataAccessException {
		return (List<T>) smsDao.getList(map);
	}

	@Override
	public boolean removeByLimit(int limit) throws DataAccessException {
		if(limit<=0) {
			throw new RuntimeException("参数limit不能为负数或0");
		}
		return smsDao.removeByLimit(limit);
	}
	@Override
	public void addSendSMS(Standard_Submit ssm,SMSObject sMSObject) {
		    SMSSender.getInstance().add(ssm); 
			sMSObject.setStatus(TechcenterEnum.TECHCENTERFLAG.YTOXL.getValue());
			sMSObject.setSendTime(new Date());
			SMSSender.getSMSObjectService().edit(sMSObject);
	}

	@Override
	public List<T> getStatusList(Map map) throws DataAccessException {
		return (List<T>) smsDao.getStatusList(map);
	}
}
