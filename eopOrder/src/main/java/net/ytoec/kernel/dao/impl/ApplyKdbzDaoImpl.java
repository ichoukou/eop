package net.ytoec.kernel.dao.impl;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.ApplyKdbzDao;
import net.ytoec.kernel.dataobject.ApplyKdbz;
import net.ytoec.kernel.mapper.ApplyKdbzMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("all")
public class ApplyKdbzDaoImpl<T extends ApplyKdbz> implements ApplyKdbzDao<T> {

	private static Logger logger = LoggerFactory
			.getLogger(ApplyKdbzDaoImpl.class);

	@Inject
	private ApplyKdbzMapper<T> mapper;

	@Override
	public boolean addApplyKdbz(T entity) {
		boolean flag = false;

		try {
			mapper.add(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}

		return flag;
	}

	@Override
	public boolean editApplyKdbz(T entity) {

		boolean flag = false;

		try {
			mapper.edit(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}

		return flag;
	}

}
