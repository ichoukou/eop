package net.ytoec.kernel.service.impl;

import javax.inject.Inject;

import net.ytoec.kernel.dao.SMSTempDao;
import net.ytoec.kernel.dataobject.SMSTemp;
import net.ytoec.kernel.service.SMSTempService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@SuppressWarnings("all")
public class SMSTempServiceImpl<T extends SMSTemp> implements SMSTempService<T> {

	
	@Inject
	private SMSTempDao<SMSTemp> smsTempDao;

	@Override
	public boolean add(T entity) {
		return smsTempDao.add(entity);
	}

	@Override
	public boolean edit(T entity) {
		return smsTempDao.edit(entity);
	}

	@Override
	public boolean remove(T entity) {
		return smsTempDao.remove(entity);
	}

	@Override
	public T get(T entity) {
		return (T) smsTempDao.get(entity);
	}

}
