package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

/**
 * 已处理的短信表-ec_core_deliverSMS
 * DAO
 * @author guolongchao
 * 20120808
 */
@SuppressWarnings("all")
public interface SMSDeliverDao<T> extends BaseDao<T>{

	/**
	 * 根据查询条件进行查询数据
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
	public boolean removeByLimit(int limit) throws DataAccessException;
	
	/**
	 * 根据id删除list集合
	 * @param list
	 * @throws DataAccessException
	 */
	public boolean removeByIDs(List<T> list) throws DataAccessException;
}
