package net.ytoec.kernel.dao.impl;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.BuildSearchStatusVersionDao;
import net.ytoec.kernel.dataobject.BuildSearchStatus;
import net.ytoec.kernel.mapper.BuildSearchStatusVersionMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class BuildSearchStatusDaoVersionImpl<T extends BuildSearchStatus>
		implements BuildSearchStatusVersionDao<T> {

	private static Logger logger = LoggerFactory
			.getLogger(BuildSearchStatusDaoVersionImpl.class);
	@Inject
	private BuildSearchStatusVersionMapper<T> mapper;

	@Override
	public Boolean removeBuildSearchByLimit(Integer limit) {
		boolean flag = false;
		try {
			mapper.removeBuildSearchByLimit(limit);
			logger.error("remove from build search status version...：" + limit);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;

	}

	@Override
	public boolean addBuildSearchStatusVersion(T entity)
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

	@Override
	public boolean addBuildSearchStatusWeightIndex(T entity)
			throws DataAccessException {
		boolean flag = false;

		try {
			mapper.addWeightIndex(entity);
			flag = true;
		} catch (DataAccessException e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			throw e;
		}

		return flag;
	}

}
