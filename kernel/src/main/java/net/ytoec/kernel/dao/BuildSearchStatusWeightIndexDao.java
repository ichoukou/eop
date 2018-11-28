package net.ytoec.kernel.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

public interface BuildSearchStatusWeightIndexDao<T> {

	public List<T> getBuildSearchByLimit(Integer limit)
			throws DataAccessException;

	public Boolean removeBuildSearchByLimit(Integer limit)
			throws DataAccessException;
	
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
}
