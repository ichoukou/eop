package net.ytoec.kernel.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.BuildSearchDao;
import net.ytoec.kernel.dataobject.BuildSearch;
//import net.ytoec.kernel.dataobject.BuildSearchStatusWeightIndex;
import net.ytoec.kernel.mapper.BuildSearchMapper;
//import net.ytoec.kernel.mapper.BuildSearchStatusWeightIndexMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * ClassName: BuildSearchDaoImpl
 * </p>
 * <p>
 * Description: 订单BuildSearchDao实现类
 * </p>
 * <p>
 * Author:
 * </p>
 * <p>
 * Date: Oct 25, 2013
 * </p>
 */
@Repository
public class BuildSearchDaoImpl<T extends BuildSearch> implements BuildSearchDao<T> {

    private static Logger logger = LoggerFactory.getLogger(BuildSearchDaoImpl.class);
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
    public Boolean removeBuildSearchById(int minId, int maxId) {
        boolean flag = false;
        try {
            Map map = new HashMap();
            map.put("minId", minId);
            map.put("maxId", maxId);
            mapper.removeBuildSearchById(map);
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
        Integer integer = null;
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
        try {
            mapper.addBuildSearch(entity);
            flag = true;
        } catch (Exception e) {
            logger.error("BuildSearch添加数据出现异常！" + e);
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean addBuildSearchForAddByOrder(T entity) {
        boolean flag = false;
        try {
            mapper.addBuildSearchForAdd(entity);
            flag = true;
        } catch (Exception e) {
            logger.error("BuildSearch添加数据出现异常！" + e);
            flag = false;
        }
        return flag;
    }

    @Override
    public List<T> getBuildSearchForAddByLimit(Integer limit) throws DataAccessException {
        List<T> result = null;
        try {
            result = this.mapper.getBuildSearchForAddByLimit(limit);
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
        }
        return result;
    }

    @Override
    public Boolean removeBuildSearchForAddByLimit(Integer limit) throws DataAccessException {
        boolean flag = false;
        try {
            mapper.removeBuildSearchForAddByLimit(limit);
            flag = true;
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
            flag = false;
        }
        return flag;
    }

    @Override
    public Boolean removeBuildSearchForAddById(int minId, int maxId) {
        boolean flag = false;
        try {
            Map map = new HashMap();
            map.put("minId", minId);
            map.put("maxId", maxId);
            mapper.removeBuildSearchForAddById(map);
            flag = true;
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
            flag = false;
        }
        return flag;
    }

    /**
     * <p>
     * Title: countBuildSearchForupdateFour
     * </p>
     * <p>
     * Description:监控ec_core_build_search_forupdate4表记录
     * </p>
     * 
     * @return Integer 记录数
     * @see net.ytoec.kernel.dao.BuildSearchDao#countBuildSearchForupdateFour()
     */
    @Override
    public Integer countBuildSearchForupdateFour() {
        Integer integer = null;
        try {
            integer = mapper.countBuildSearchForupdateFour();
        } catch (Exception e) {
            logger.error("查询数据库出现异常！");
        }
        return integer;
    }

    /**
     * <p>
     * Title: countBuildSearchForupdateThree
     * </p>
     * <p>
     * Description:监控ec_core_build_search_forupdate3表记录
     * </p>
     * 
     * @return Integer 记录数
     * @see net.ytoec.kernel.dao.BuildSearchDao#countBuildSearchForupdateThree()
     */
    @Override
    public Integer countBuildSearchForupdateThree() {
        Integer integer = null;
        try {
            integer = mapper.countBuildSearchForupdateThree();
        } catch (Exception e) {
            logger.error("查询数据库出现异常！");
        }
        return integer;
    }

    /**
     * <p>
     * Title: countBuildSearchForupdateTwo
     * </p>
     * <p>
     * Description:监控ec_core_build_search_forupdate2表记录
     * </p>
     * 
     * @return Integer 记录数
     * @see net.ytoec.kernel.dao.BuildSearchDao#countBuildSearchForupdateTwo()
     */
    @Override
    public Integer countBuildSearchForupdateTwo() {
        Integer integer = null;
        try {
            integer = mapper.countBuildSearchForupdateTwo();
        } catch (Exception e) {
            logger.error("查询数据库出现异常！");
        }
        return integer;
    }

    /**
     * <p>
     * Title: countBuildSearchForupdate
     * </p>
     * <p>
     * Description: 监控ec_core_build_search_forupdate表记录
     * </p>
     * 
     * @return Integer 记录数
     * @see net.ytoec.kernel.dao.BuildSearchDao#countBuildSearchForupdate()
     */
    @Override
    public Integer countBuildSearchForupdate() {
        Integer integer = null;
        try {
            integer = mapper.countBuildSearchForupdate();
        } catch (Exception e) {
            logger.error("查询数据库出现异常！");
        }
        return integer;
    }

    /**
     * <p>
     * Title: countBuildSearchForadd
     * </p>
     * <p>
     * Description:监控ec_core_build_search_foradd表记录
     * </p>
     * 
     * @return Integer 记录数
     * @see net.ytoec.kernel.dao.BuildSearchDao#countBuildSearchForadd()
     */
    @Override
    public Integer countBuildSearchForadd() {
        Integer integer = null;
        try {
            integer = mapper.countBuildSearchForadd();
        } catch (Exception e) {
            logger.error("查询数据库出现异常！");
        }
        return integer;
    }

}