package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.BuildSearchStatusWeightIndex;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * <p>
 * ClassName: BuildSearchStatusWeightIndexMapper
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
public interface BuildSearchStatusWeightIndexMapper<T extends BuildSearchStatusWeightIndex> extends BaseSqlMapper<T> {

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param limit
     * @return
     */
    public List<T> getBuildSearchByLimit(int limit);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param limit
     * @return
     */
    public Boolean removeBuildSearchByLimit(int limit);

    /**
     * <p>
     * Description: 监控ec_core_build_search_status_weight
     * </p>
     * 
     * @return Integer 记录总数
     */
    public Integer countBuildSearchStatusWeight();

    /**
     * 统计build_search_status_weight_index表的数量
     * 
     * @return
     */
    public Integer countBuildSearchStatusWeightIndex();

    /**
     * 统计solr状态增量分压表 version
     * 
     * @return
     */
    public Integer countBuildSearchStatusWeightVersion();

    /**
     * 统计solr状态增量分压表pressure
     * 
     * @return
     */
    public Integer countBuildSearchStatusWeightIndexPressure();

    /**
     * 新增订单到addBuildSearchStatusWeightIndex表
     * 
     * @param entity
     */
    public void addBuildSearchStatusWeightIndex(T entity);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param map
     * @return
     */
    public List<T> getResultByLimit(Map map);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param map
     * @return
     */
    public List<T> getResultByLimit2(Map map);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param map
     * @return
     */
    public List<T> getResultByLimitforUpdate2(Map map);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param map
     * @return
     */
    public List<T> getResultByLimitforUpdate3(Map map);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param map
     * @return
     */
    public List<T> getResultByLimitforUpdate4(Map map);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param map
     * @return
     */
    public Boolean removeBuildSearchById(Map map);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param map
     * @return
     */
    public Boolean removeBuildSearchById2(Map map);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param map
     * @return
     */
    public Boolean removeBuildSearchByIdforUpdate2(Map map);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param map
     * @return
     */
    public Boolean removeBuildSearchByIdforUpdate3(Map map);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param map
     * @return
     */
    public Boolean removeBuildSearchByIdforUpdate4(Map map);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param entity
     */
    public void addBuildSearchStatusForUpdate(T entity);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param entity
     */
    public void addBuildSearchStatusForUpdate2(T entity);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param entity
     */
    public void addBuildSearchStatusForUpdate3(T entity);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param entity
     */
    public void addBuildSearchStatusForUpdate4(T entity);
    
    public void addBuildSearchForWeight1(T entity);
    
    public void addBuildSearchForWeight2(T entity);
    
    public List<T> getResultByLimitforWeight1(Map map);
    
    public List<T> getResultByLimitforWeight2(Map map);
    
    public Boolean removeBuildWeightById1(Map map);
    
    public Boolean removeBuildWeightById2(Map map);


}
