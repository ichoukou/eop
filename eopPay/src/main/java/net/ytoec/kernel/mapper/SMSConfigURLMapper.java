package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.dataobject.SMSConfigURL;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 短信模块url配置表Mapper
 * @author guolongchao
 * 20120808
 */
@SuppressWarnings("all")
public interface SMSConfigURLMapper<T extends SMSConfigURL> extends BaseSqlMapper<T> {
	
	/**
	 * 通过mysql的limit函数删除记录
	 * @param limit
	 * @throws DataAccessException
	 */
	public void removeByLimit(int limit) throws DataAccessException;
}
