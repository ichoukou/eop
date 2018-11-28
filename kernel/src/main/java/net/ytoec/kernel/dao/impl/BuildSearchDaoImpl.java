package net.ytoec.kernel.dao.impl;

import java.util.List;

import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.BuildSearchDao;
import net.ytoec.kernel.dataobject.BuildSearch;
import net.ytoec.kernel.mapper.BuildSearchMapper;

@Repository
public class BuildSearchDaoImpl<T extends BuildSearch> implements
		BuildSearchDao<T> {

	private Logger logger = Logger.getLogger(BuildSearchDaoImpl.class);
	@Inject
	private BuildSearchMapper<T> mapper;
	

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
		 flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;

	}

    @Override
    public Integer countBuildSearch() {
        // TODO Auto-generated method stub
        Integer integer=null;
        try {
           integer = mapper.countBuildSearch();
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("查询数据库出现异常！");
        }
        return integer;
    }

	@Override
	public boolean addBuildSearch(T entity) {
		boolean flag = false;
		try {
		 mapper.add(entity);
		 flag = true;
		} catch (Exception e) {
			logger.error("数据库添加记录出现异常！", e);
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean addBuildSearchByUpdateOrder(T entity) {
		boolean flag = false;
		try{
			mapper.addBuildSearch(entity);
			flag = true;
		}catch(Exception e){
			logger.error("订单更新时BuildSearch添加数据出现异常！");
			flag = false;
		}
		return flag;
	}

}
