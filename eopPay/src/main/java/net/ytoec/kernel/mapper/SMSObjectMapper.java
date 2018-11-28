package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 短信表实体类Mapper(待发送短信表)
 * @author guolongchao
 * 20120808
 */
@SuppressWarnings("all")
public interface SMSObjectMapper<T extends SMSObject> extends BaseSqlMapper<T> {

	/**
	 * 根据查询条件查询记录，用作短信发送
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
	public void removeByLimit(int limit) throws DataAccessException;
	
	/**
	 * 根据查询条件查询记录 ，用作查询短信发送状态
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getStatusList(Map map) throws DataAccessException;
}
