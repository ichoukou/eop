package net.ytoec.kernel.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

public interface BuildSearchStatusWeightDao<T> {

	public List<T> getBuildSearchByLimit(Integer limit)
			throws DataAccessException;

	public Boolean removeBuildSearchByLimit(Integer limit)
			throws DataAccessException;
}
