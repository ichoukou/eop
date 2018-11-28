package net.ytoec.kernel.dao.impl;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.BuildSearchStatusDaoIndexPressure;
import net.ytoec.kernel.dataobject.BuildSearchStatus;
import net.ytoec.kernel.mapper.BuildSearchStatusIndexPressureMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class BuildSearchStatusDaoIndexPressureImpl<T extends BuildSearchStatus> implements BuildSearchStatusDaoIndexPressure<T> {

	private static Logger logger = LoggerFactory.getLogger(BuildSearchStatusDaoIndexPressureImpl.class);
	@Inject
	private BuildSearchStatusIndexPressureMapper<T> mapper;

	@Override
	public Boolean removeBuildSearchByLimit(Integer limit) {
		boolean flag = false;
		try {
		 mapper.removeBuildSearchByLimit(limit);
		 logger.error("remove from build search status index pressure："+limit);
		 flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;

	}

	@Override
	public boolean addBuildSearchStatusDaoIndexPressure(T entity)
			throws DataAccessException {
		boolean flag = false;

		try {
			mapper.add(entity);
			flag = true;
		} catch (DataAccessException e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			throw e;
		}

		return flag;
	}

}
