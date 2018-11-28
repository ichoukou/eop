package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.dataobject.SMSConfigURL;

/**
 * 短信模块url配置表service
 * @author guolongchao
 * 20120808
 */
@SuppressWarnings("all")
public interface SMSConfigURLService<T extends SMSConfigURL> extends BaseService<T>{

	/**
	 * 通过mysql的limit函数删除记录
	 * @param limit
	 */
	public boolean removeByLimit(int limit) throws DataAccessException;
}
