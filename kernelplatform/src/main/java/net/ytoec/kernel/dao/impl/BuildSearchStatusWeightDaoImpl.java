package net.ytoec.kernel.dao.impl;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.BuildSearchStatusWeightDao;
import net.ytoec.kernel.dataobject.BuildSearchStatusWeight;
import net.ytoec.kernel.mapper.BuildSearchStatusWeightMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class BuildSearchStatusWeightDaoImpl<T extends BuildSearchStatusWeight> implements
		BuildSearchStatusWeightDao<T> {

	private static Logger logger = LoggerFactory.getLogger(BuildSearchStatusWeightDaoImpl.class);
	@Inject
	private BuildSearchStatusWeightMapper<T> mapper;

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
		 logger.error("remove from build search status");
		 flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;

	}

}
