package net.ytoec.kernel.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.BuildSearchStatusWeightIndexDao;
import net.ytoec.kernel.dataobject.BuildSearchStatusWeightIndex;
import net.ytoec.kernel.mapper.BuildSearchStatusWeightIndexMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class BuildSearchStatusWeightIndexDaoImpl<T extends BuildSearchStatusWeightIndex> implements
        BuildSearchStatusWeightIndexDao<T> {

    private static Logger logger = LoggerFactory.getLogger(BuildSearchStatusWeightIndexDaoImpl.class);
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

    @Override
    public boolean addBuildSearchByUpdateOrderForWeightIndex(T entity) {
        boolean flag = false;
        try {
            mapper.addBuildSearchStatusWeightIndex(entity);
            flag = true;
        } catch (Exception e) {
            logger.error("BuildSearchStatusWeightIndex添加数据出现异常！" + e);
            flag = false;
        }
        return flag;
    }

    @Override
    public List<T> getResultByLimit(Integer limit) throws DataAccessException {
        List<T> result = null;
        try {
            Map map = new HashMap();
            map.put("limit", limit);

            result = this.mapper.getResultByLimit(map);
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
        }
        return result;
    }

    @Override
    public List<T> getResultByLimit2(Integer limit, int type) throws DataAccessException {
        List<T> result = null;
        try {
            Map map = new HashMap();
            map.put("limit", limit);

            if (type == 2) {
                result = this.mapper.getResultByLimit2(map);
            } else if (type == 3) {
                result = this.mapper.getResultByLimitforUpdate2(map);
            } else if (type == 4) {
                result = this.mapper.getResultByLimitforUpdate3(map);
            } else if (type == 5) {
                result = this.mapper.getResultByLimitforUpdate4(map);
            }
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
        }
        return result;
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
    public boolean addBuildSearchByUpdateOrderStatus(T entity) {
        boolean flag = false;
        try {
            mapper.addBuildSearchStatusForUpdate(entity);
            flag = true;
        } catch (Exception e) {
            logger.error("addBuildSearchByUpdateOrderStatus添加数据出现异常！" + e);
            flag = false;
        }
        return flag;
    }

    @Override
    public Boolean removeBuildSearchById2(int minId, int maxId, int type) {
        boolean flag = false;
        try {
            Map map = new HashMap();
            map.put("minId", minId);
            map.put("maxId", maxId);
            if (type == 2) {
                mapper.removeBuildSearchById2(map);
            } else if (type == 3) {
                mapper.removeBuildSearchByIdforUpdate2(map);
            } else if (type == 4) {
                mapper.removeBuildSearchByIdforUpdate3(map);
            } else if (type == 5) {
                mapper.removeBuildSearchByIdforUpdate4(map);
            }
            flag = true;
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean addBuildSearchByUpdateOrderStatus2(T entity) {
        boolean flag = false;
        try {
            mapper.addBuildSearchStatusForUpdate2(entity);
            flag = true;
        } catch (Exception e) {
            logger.error("addBuildSearchByUpdateOrderStatus添加数据出现异常！" + e);
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean addBuildSearchByUpdateOrderStatus3(T entity) {
        boolean flag = false;
        try {
            mapper.addBuildSearchStatusForUpdate3(entity);
            flag = true;
        } catch (Exception e) {
            logger.error("addBuildSearchByUpdateOrderStatus添加数据出现异常！" + e);
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean addBuildSearchByUpdateOrderStatus4(T entity) {
        boolean flag = false;
        try {
            mapper.addBuildSearchStatusForUpdate4(entity);
            flag = true;
        } catch (Exception e) {
            logger.error("addBuildSearchByUpdateOrderStatus添加数据出现异常！" + e);
            flag = false;
        }
        return flag;
    }

    /**
     * <p>
     * Title: countBuildSearchStatusWeight
     * </p>
     * <p>
     * Description: 监控ec_core_build_search_status_weight
     * </p>
     * 
     * @return Integer 记录数
     * @see net.ytoec.kernel.dao.BuildSearchStatusWeightIndexDao#countBuildSearchStatusWeight()
     */
    @Override
    public Integer countBuildSearchStatusWeight() {
        logger.error("countBuildSearchStatusWeight begin...........");
        Integer integer = null;
        try {
            logger.error("countBuildSearchStatusWeight exec...........");
            integer = mapper.countBuildSearchStatusWeight();
        } catch (Exception e) {
            logger.error("error message:查询数据库出错！错误原因："+e.getMessage());
        }
        logger.error("countBuildSearchStatusWeight end...........");
        return integer;
    }

}
