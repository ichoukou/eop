package net.ytoec.kernel.mapper;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.dataobject.BuildSearchStatus;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

public interface BuildSearchStatusVersionMapper<T extends BuildSearchStatus> extends BaseSqlMapper<T> {
	
	public Boolean removeBuildSearchByLimit(int limit);
	
	public void addWeightIndex(T entity) throws DataAccessException;
}
