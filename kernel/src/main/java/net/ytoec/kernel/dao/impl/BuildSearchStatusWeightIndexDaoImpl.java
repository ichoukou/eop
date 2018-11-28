package net.ytoec.kernel.dao.impl;

import java.util.List;

import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.BuildSearchDao;
import net.ytoec.kernel.dao.BuildSearchStatusDao;
import net.ytoec.kernel.dao.BuildSearchStatusWeightIndexDao;
import net.ytoec.kernel.dataobject.BuildSearch;
import net.ytoec.kernel.dataobject.BuildSearchStatus;
import net.ytoec.kernel.dataobject.BuildSearchStatusWeightIndex;
import net.ytoec.kernel.mapper.BuildSearchMapper;
import net.ytoec.kernel.mapper.BuildSearchStatusMapper;
import net.ytoec.kernel.mapper.BuildSearchStatusWeightIndexMapper;

@Repository
public class BuildSearchStatusWeightIndexDaoImpl<T extends BuildSearchStatusWeightIndex> implements
		BuildSearchStatusWeightIndexDao<T> {

	private Logger logger = Logger.getLogger(BuildSearchStatusWeightIndexDaoImpl.class);
	@Inject
	private BuildSearchStatusWeightIndexMapper<T> mapper;

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
	
    @Override
    public Integer countBuildSearchStatusWeightIndex() {
        // TODO Auto-generated method stub
        Integer integer = null;
        try {
            integer = mapper.countBuildSearchStatusWeightIndex();
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("查询数据库出错！");
        }
        return integer;
    }

	@Override
	public Integer countBuildSearchStatusWeightVersion() {
		// TODO Auto-generated method stub
		Integer integer = null;
		try {
			integer = mapper.countBuildSearchStatusWeightVersion();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("分流表version查询异常");
		}
		return mapper.countBuildSearchStatusWeightVersion();
	}

	@Override
	public Integer countBuildSearchStatusWeightIndexPressure() {
		// TODO Auto-generated method stub
		Integer integer = null;
		try {
			integer = mapper.countBuildSearchStatusWeightIndexPressure();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("分流表IndexPressure查询异常！");
		}
		return integer;
	}

}
