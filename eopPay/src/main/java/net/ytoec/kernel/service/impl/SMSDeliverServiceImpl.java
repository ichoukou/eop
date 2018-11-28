package net.ytoec.kernel.service.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dao.SMSDeliverDao;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.service.SMSDeliverService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 短信表实体类Service实现类
 * @author guolongchao
 * 20120808
 */
@Service
@Transactional
@SuppressWarnings("all")
public class SMSDeliverServiceImpl<T extends SMSObject> implements SMSDeliverService<T> {

	private static final Logger logger = LoggerFactory.getLogger(SMSDeliverServiceImpl.class);
	
	@Inject
	private SMSDeliverDao<SMSObject> smsDeliverDao;
	
	@Override
	public boolean add(T entity) {		
		return smsDeliverDao.add(entity);
	}	

	@Override
	public List<T> getList(Map map) throws DataAccessException {
		return (List<T>) smsDeliverDao.getList(map);
	}

	@Override
	public boolean edit(T entity) {
		return false;
	}

	@Override
	public boolean remove(T entity) {
		return false;
	}

	@Override
	public T get(T entity) {
		return null;
	}

	@Override
	public boolean removeByLimit(int limit) throws DataAccessException {
		if(limit<=0) {
			throw new RuntimeException("参数limit不能为负数或0");
		}
		return smsDeliverDao.removeByLimit(limit);
	}

	@Override
	public boolean removeByIDs(List<T> list) throws DataAccessException {
		return smsDeliverDao.removeByIDs((List<SMSObject>) list);
	}
}
