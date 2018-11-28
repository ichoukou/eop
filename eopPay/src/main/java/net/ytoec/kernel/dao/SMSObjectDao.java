package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

/**
 * 短信表实体类DAO
 * @author guolongchao
 * 20120808
 */
@SuppressWarnings("all")
public interface SMSObjectDao<T> extends BaseDao<T>{

	/**
	 * 根据查询条件进行分页查询数据 ，用作发送短信
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getList(Map map) throws DataAccessException;
	
	/**
	 * 根据查询条件获取总的记录数
	 * @param map
	 * @return Integer
	 */
	public Integer getRecordsCount(Map map);
	
	/**
	 * 通过mysql的limit函数删除记录
	 * @param limit
	 * @throws DataAccessException
	 */
	public boolean removeByLimit(int limit) throws DataAccessException;

	/**
	 * 根据查询条件进行分页查询数据，用作查询短信发送状态
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getStatusList(Map map);
}
