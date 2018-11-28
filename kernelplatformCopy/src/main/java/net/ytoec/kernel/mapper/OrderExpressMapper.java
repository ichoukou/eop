package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import net.ytoec.kernel.dataobject.OrderExpress;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

public interface OrderExpressMapper<T extends OrderExpress> extends BaseSqlMapper<T> {
	
	public List<T> getPageList(Map<String, Object> params) throws DataAccessException;

	public List<T> getAll(Map<String, Object> params) throws DataAccessException;

	public Integer getTotal(Map<String, Object> map) throws DataAccessException;
	
	public List<T> getOrderExpressList(Map<String, Object> params) throws DataAccessException;
	
	public List<T> getOrderExpressByStoreId(Map<String, Object> params) throws DataAccessException;
	
	/**
	 * 根据storeIds获取面单模版信息  by wus
	 * @param params
	 * @return
	 */
	public List<T> getOrderExpressByStoreIds(Map<String, Object> params) throws DataAccessException;
	
}