package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

/**
 * 短信模块url配置表DAO
 * @author guolongchao
 * 20120808
 */
@SuppressWarnings("all")
public interface SMSConfigURLDao<T> extends BaseDao<T>{

	/**
	 * 通过mysql的limit函数删除记录
	 * @param limit
	 * @throws DataAccessException
	 */
	public boolean removeByLimit(int limit) throws DataAccessException;
}
