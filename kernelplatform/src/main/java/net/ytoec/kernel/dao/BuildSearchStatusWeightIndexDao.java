package net.ytoec.kernel.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

/**
 * <p>ClassName: BuildSearchStatusWeightIndexDao</p>
 * <p>Description: TODO</p>
 * <p>Author: </p>
 * <p>Date: Oct 25, 2013</p>
 */
public interface BuildSearchStatusWeightIndexDao<T> {

	public List<T> getBuildSearchByLimit(Integer limit)
			throws DataAccessException;

	public Boolean removeBuildSearchByLimit(Integer limit)
			throws DataAccessException;
    /**
     * <p>
     * Description: 监控ec_core_build_search_status_weight
     * </p>
     * 
     * @return Integer 记录数
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
     * @return
     */
    public Integer countBuildSearchStatusWeightVersion();
    
    /**
     * 统计solr状态增量分压表pressure
     * @return
     */
    public Integer countBuildSearchStatusWeightIndexPressure();
    
    public boolean addBuildSearchByUpdateOrderForWeightIndex(T entity);
    
    public List<T> getResultByLimit(Integer limit)
	throws DataAccessException;
    
    public List<T> getResultByLimit2(Integer limit,int type)
	throws DataAccessException;
    
    public Boolean removeBuildSearchById(int minId,int maxId);
    
    public Boolean removeBuildSearchById2(int minId,int maxId,int type);
    
    public boolean addBuildSearchByUpdateOrderStatus(T entity);
    
    public boolean addBuildSearchByUpdateOrderStatus2(T entity);
    
    public boolean addBuildSearchByUpdateOrderStatus3(T entity);
    
    public boolean addBuildSearchByUpdateOrderStatus4(T entity);
    
}
