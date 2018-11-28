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

/**
 * <p>
 * ClassName: BuildSearchStatusWeightIndexDaoImpl
 * </p>
 * <p>
 * Description: TODO
 * </p>
 * <p>
 * Author:
 * </p>
 * <p>
 * Date: Oct 24, 2013
 * </p>
 */
public class BuildSearchStatusWeightIndexDaoImpl<T extends BuildSearchStatusWeightIndex> implements
        BuildSearchStatusWeightIndexDao<T> {
    private static Logger logger = LoggerFactory.getLogger(BuildSearchStatusWeightIndexDaoImpl.class);
    /**
     * <p>
     * Field mapper: TODO
     * </p>
     */
    @Inject
    private BuildSearchStatusWeightIndexMapper<T> mapper;

    /**
     * <p>
     * Title: getBuildSearchByLimit
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param limit
     * @return
     * @see net.ytoec.kernel.dao.BuildSearchStatusWeightIndexDao#getBuildSearchByLimit(java.lang.Integer)
     */
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

    /**
     * <p>
     * Title: removeBuildSearchByLimit
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param limit
     * @return
     * @see net.ytoec.kernel.dao.BuildSearchStatusWeightIndexDao#removeBuildSearchByLimit(java.lang.Integer)
     */
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

    /**
     * <p>
     * Title: countBuildSearchStatusWeightIndex
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return
     * @see net.ytoec.kernel.dao.BuildSearchStatusWeightIndexDao#countBuildSearchStatusWeightIndex()
     */
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

    /**
     * <p>
     * Title: countBuildSearchStatusWeightVersion
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return
     * @see net.ytoec.kernel.dao.BuildSearchStatusWeightIndexDao#countBuildSearchStatusWeightVersion()
     */
    @Override
    public Integer countBuildSearchStatusWeightVersion() {
        Integer integer = null;
        try {
            integer = mapper.countBuildSearchStatusWeightVersion();
        } catch (Exception e) {
            logger.error("分流表version查询异常");
        }
        return mapper.countBuildSearchStatusWeightVersion();
    }

    /**
     * <p>
     * Title: countBuildSearchStatusWeightIndexPressure
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return
     * @see net.ytoec.kernel.dao.BuildSearchStatusWeightIndexDao#countBuildSearchStatusWeightIndexPressure()
     */
    @Override
    public Integer countBuildSearchStatusWeightIndexPressure() {
        Integer integer = null;
        try {
            integer = mapper.countBuildSearchStatusWeightIndexPressure();
        } catch (Exception e) {
            logger.error("分流表IndexPressure查询异常！");
        }
        return integer;
    }
    
    /**
     * <p>
     * Title: countBuildSearchStatusWeight
     * </p>
     * <p>
     * Description:监控ec_core_build_search_status_weight表
     * </p>
     * 
     * @return Integer 记录数
     * @see net.ytoec.kernel.dao.BuildSearchStatusWeightIndexDao#countBuildSearchStatusWeight()
     */
    @Override
    public Integer countBuildSearchStatusWeight() {
        Integer integer = null;
        try {
            integer = mapper.countBuildSearchStatusWeight();
        } catch (Exception e) {
            logger.error("分流表IndexPressure查询异常！");
        }
        return integer;
    }

    /**
     * <p>
     * Title: getResultByLimit
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param limit
     * @return
     * @throws DataAccessException
     * @see net.ytoec.kernel.dao.BuildSearchStatusWeightIndexDao#getResultByLimit(java.lang.Integer)
     */
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

    /**
     * <p>
     * Title: getResultByLimit2
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param limit
     * @param type
     * @return
     * @throws DataAccessException
     * @see net.ytoec.kernel.dao.BuildSearchStatusWeightIndexDao#getResultByLimit2(java.lang.Integer, int)
     */
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

    /**
     * <p>
     * Title: removeBuildSearchById
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param minId
     * @param maxId
     * @return
     * @see net.ytoec.kernel.dao.BuildSearchStatusWeightIndexDao#removeBuildSearchById(int, int)
     */
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

    /**
     * <p>
     * Title: removeBuildSearchById2
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param minId
     * @param maxId
     * @param type
     * @return
     * @see net.ytoec.kernel.dao.BuildSearchStatusWeightIndexDao#removeBuildSearchById2(int, int, int)
     */
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

    /**
     * <p>
     * Title: addBuildSearchByUpdateOrderForWeightIndex
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param entity
     * @return
     * @see net.ytoec.kernel.dao.BuildSearchStatusWeightIndexDao#addBuildSearchByUpdateOrderForWeightIndex(java.lang.Object)
     */
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

    /**
     * <p>
     * Title: addBuildSearchByUpdateOrderStatus
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param entity
     * @return
     * @see net.ytoec.kernel.dao.BuildSearchStatusWeightIndexDao#addBuildSearchByUpdateOrderStatus(java.lang.Object)
     */
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

    /**
     * <p>
     * Title: addBuildSearchByUpdateOrderStatus2
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param entity
     * @return
     * @see net.ytoec.kernel.dao.BuildSearchStatusWeightIndexDao#addBuildSearchByUpdateOrderStatus2(java.lang.Object)
     */
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

    /**
     * <p>
     * Title: addBuildSearchByUpdateOrderStatus3
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param entity
     * @return
     * @see net.ytoec.kernel.dao.BuildSearchStatusWeightIndexDao#addBuildSearchByUpdateOrderStatus3(java.lang.Object)
     */
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

    /**
     * <p>
     * Title: addBuildSearchByUpdateOrderStatus4
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param entity
     * @return
     * @see net.ytoec.kernel.dao.BuildSearchStatusWeightIndexDao#addBuildSearchByUpdateOrderStatus4(java.lang.Object)
     */
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
    
    @Override
	public boolean addBuildSearchForWeight1(T entity) {
		boolean flag = false;
		try{
			mapper.addBuildSearchForWeight1(entity);
			flag = true;
		}catch(Exception e){
			logger.error("addBuildSearchByUpdateOrderStatus添加数据出现异常！"+e);
			flag = false;
		}
		return flag;
	}
	@Override
	public boolean addBuildSearchForWeight2(T entity) {
		boolean flag = false;
		try{
			mapper.addBuildSearchForWeight2(entity);
			flag = true;
		}catch(Exception e){
			logger.error("addBuildSearchByUpdateOrderStatus添加数据出现异常！"+e);
			flag = false;
		}
		return flag;
	}
	
	@Override
	public List<T> getResultByLimitWeight1(Integer limit) throws DataAccessException {
		List<T> result = null;
		try {
			Map map=new HashMap();
			map.put("limit", limit);
			
			result = this.mapper.getResultByLimitforWeight1(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return result;
	}
	
	@Override
	public List<T> getResultByLimitWeight2(Integer limit) throws DataAccessException {
		List<T> result = null;
		try {
			Map map=new HashMap();
			map.put("limit", limit);
			
			result = this.mapper.getResultByLimitforWeight2(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return result;
	}

	@Override
	public Boolean removeBuildWeightByLimit1(int minId,int maxId){
	boolean flag = false;
	try {
	Map map=new HashMap();
	map.put("minId", minId);
	map.put("maxId", maxId);
	 mapper.removeBuildWeightById1(map);
	 flag = true;
	} catch (Exception e) {
		logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		flag = false;
	}
	return flag;
	}

	@Override
	public Boolean removeBuildWeightByLimit2(int minId,int maxId){
				boolean flag = false;
				try {
				Map map=new HashMap();
				map.put("minId", minId);
				map.put("maxId", maxId);
				 mapper.removeBuildWeightById2(map);
				 flag = true;
				} catch (Exception e) {
					logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
					flag = false;
				}
				return flag;
	}

}
