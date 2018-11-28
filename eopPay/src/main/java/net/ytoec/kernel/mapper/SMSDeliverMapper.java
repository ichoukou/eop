package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 已处理的短信表-ec_core_deliverSMS对应的Mapper
 * @author guolongchao
 * 20120808
 */
@SuppressWarnings("all")
public interface SMSDeliverMapper<T extends SMSObject> extends BaseSqlMapper<T> {

	/**
	 * 根据查询条件查询记录
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getList(Map map) throws DataAccessException;
	
	/**
	 * 通过mysql的limit函数删除记录
	 * @param limit
	 * @throws DataAccessException
	 */
	public void removeByLimit(int limit) throws DataAccessException;
	
	
	/**
	 * 根据id删除list集合
	 * @param list
	 * @throws DataAccessException
	 */
	public void removeByIDs(List<T> list) throws DataAccessException;
}
