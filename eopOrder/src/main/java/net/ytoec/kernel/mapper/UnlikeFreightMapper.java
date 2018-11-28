/**
 * 
 */
package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.dataobject.UnlikeFreight;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;


public interface UnlikeFreightMapper<T extends UnlikeFreight> extends	BaseSqlMapper<T> {


	public List<T> getUnlikeFreightList(Map map);
	
	public T getUnlikeFreightByMailNo(T entity) throws DataAccessException;
	
	
}
