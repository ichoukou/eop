package net.ytoec.kernel.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

/**
 * <p>
 * ClassName: BuildSearchStatusWeightIndexDao
 * </p>
 * <p>
 * Description: solr状态DAO
 * </p>
 * <p>
 * Author: 
 * </p>
 * <p>
 * Date: Oct 24, 2013
 * </p>
 */
public interface BuildSearchStatusWeightIndexDao<T> {

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param limit
     * @return
     * @throws DataAccessException
     */
    public List<T> getBuildSearchByLimit(Integer limit) throws DataAccessException;

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param limit
     * @return
     * @throws DataAccessException
     */
    public Boolean removeBuildSearchByLimit(Integer limit) throws DataAccessException;

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
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param entity
     * @return
     */
    public boolean addBuildSearchByUpdateOrderForWeightIndex(T entity);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param limit
     * @return
     * @throws DataAccessException
     */
    public List<T> getResultByLimit(Integer limit) throws DataAccessException;

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param limit
     * @param type
     * @return
     * @throws DataAccessException
     */
    public List<T> getResultByLimit2(Integer limit, int type) throws DataAccessException;

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param minId
     * @param maxId
     * @return
     */
    public Boolean removeBuildSearchById(int minId, int maxId);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param minId
     * @param maxId
     * @param type
     * @return
     */
    public Boolean removeBuildSearchById2(int minId, int maxId, int type);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param entity
     * @return
     */
    public boolean addBuildSearchByUpdateOrderStatus(T entity);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param entity
     * @return
     */
    public boolean addBuildSearchByUpdateOrderStatus2(T entity);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param entity
     * @return
     */
    public boolean addBuildSearchByUpdateOrderStatus3(T entity);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param entity
     * @return
     */
    public boolean addBuildSearchByUpdateOrderStatus4(T entity);

    /**
     * <p>
     * Description: 监控ec_core_build_search_status_weight
     * </p>
     * 
     * @return Integer 总记录数
     */
    public Integer countBuildSearchStatusWeight();
    
	public boolean addBuildSearchForWeight1(T entity) ;
	public boolean addBuildSearchForWeight2(T entity) ;
	
	public List<T> getResultByLimitWeight1(Integer limit);
	public List<T> getResultByLimitWeight2(Integer limit);
	
	public Boolean removeBuildWeightByLimit1(int minId,int maxId)
	throws DataAccessException;
	
	public Boolean removeBuildWeightByLimit2(int minId,int maxId)
	throws DataAccessException;
}
