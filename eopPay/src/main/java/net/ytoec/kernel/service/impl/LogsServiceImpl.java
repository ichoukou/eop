package net.ytoec.kernel.service.impl;

import javax.inject.Inject;

import net.ytoec.kernel.dao.LogsDao;
import net.ytoec.kernel.dataobject.Logs;
import net.ytoec.kernel.service.LogsService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class LogsServiceImpl<T extends Logs> implements LogsService<T> {

	@Inject
	private LogsDao<Logs> logsDao;
	
	@Override
	public boolean add(T entity) {
		
		return logsDao.add(entity);
	}

	@Override
	public boolean edit(T entity) {
		return false;
	}

	@Override
	public boolean remove(T entity) {
		return logsDao.remove(entity);
	}

	@Override
	public T get(T entity) {
		return null;
	}

}
