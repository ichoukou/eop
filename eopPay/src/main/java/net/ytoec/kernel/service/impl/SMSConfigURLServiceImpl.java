package net.ytoec.kernel.service.impl;

import javax.inject.Inject;

import net.ytoec.kernel.dao.SMSConfigURLDao;
import net.ytoec.kernel.dataobject.SMSConfigURL;
import net.ytoec.kernel.service.SMSConfigURLService;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *短信模块url配置表service实现类
 * @author guolongchao
 * 20120808
 */
@Service
@Transactional
@SuppressWarnings("all")
public class SMSConfigURLServiceImpl<T extends SMSConfigURL> implements SMSConfigURLService<T> {

	
	@Inject
	private SMSConfigURLDao<SMSConfigURL> smsConfigURLDao;

	@Override
	public boolean add(T entity) {
		// TODO Auto-generated method stub
		return smsConfigURLDao.add(entity);
	}

	@Override
	public boolean edit(T entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(T entity) {
		// TODO Auto-generated method stub
		return smsConfigURLDao.remove(entity);
	}

	@Override
	public T get(T entity) {
		// TODO Auto-generated method stub
		return (T) smsConfigURLDao.get(entity);
	}

	@Override
	public boolean removeByLimit(int limit) throws DataAccessException {
		// TODO Auto-generated method stub
		if(limit<=0) {
			throw new RuntimeException("参数limit不能为负数或0");
		}
		return smsConfigURLDao.removeByLimit(limit);
	}
}
