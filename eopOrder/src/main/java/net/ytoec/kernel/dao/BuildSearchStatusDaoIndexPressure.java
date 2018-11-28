package net.ytoec.kernel.dao;


import org.springframework.dao.DataAccessException;

public interface BuildSearchStatusDaoIndexPressure<T> {

	public Boolean removeBuildSearchByLimit(Integer limit)
			throws DataAccessException;
	/**
	 *  创建一条记录
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public boolean addBuildSearchStatusDaoIndexPressure(T entity) throws DataAccessException;
}
