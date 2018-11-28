package net.ytoec.kernel.dao;


import org.springframework.dao.DataAccessException;

public interface BuildSearchStatusVersionDao<T> {

	public Boolean removeBuildSearchByLimit(Integer limit)
			throws DataAccessException;
	/**
	 *  创建一条记录
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public boolean addBuildSearchStatusVersion(T entity) throws DataAccessException;
	
	 /**
	 *  ec_core_build_search_status_weight_index 创建一条记录
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public boolean addBuildSearchStatusWeightIndex(T entity) throws DataAccessException;
}
