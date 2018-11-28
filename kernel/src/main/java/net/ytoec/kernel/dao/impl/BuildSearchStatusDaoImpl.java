package net.ytoec.kernel.dao.impl;

import java.util.List;

import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.BuildSearchDao;
import net.ytoec.kernel.dao.BuildSearchStatusDao;
import net.ytoec.kernel.dataobject.BuildSearch;
import net.ytoec.kernel.dataobject.BuildSearchStatus;
import net.ytoec.kernel.mapper.BuildSearchMapper;
import net.ytoec.kernel.mapper.BuildSearchStatusMapper;

@Repository
public class BuildSearchStatusDaoImpl<T extends BuildSearchStatus> implements
		BuildSearchStatusDao<T> {

	private Logger logger = Logger.getLogger(BuildSearchStatusDaoImpl.class);
	@Inject
	private BuildSearchStatusMapper<T> mapper;

	@Override
	public List<T> getBuildSearchByLimit(Integer limit) {

		List<T> result = null;
		try {
			result = this.mapper.getBuildSearchByLimit(limit);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return result;
	}

	@Override
	public Boolean removeBuildSearchByLimit(Integer limit) {
		boolean flag = false;
		try {
		 mapper.removeBuildSearchByLimit(limit);
		 logger.error("remove from build search status"+limit);
		 flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;

	}

}
