package net.ytoec.kernel.mapper;

import java.util.List;

import net.ytoec.kernel.dataobject.CoreTaskLog;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * <b>
 * 
 * @author fulan
 * @package net.ytoec.kernel
 * @project kernel
 * @email
 * @version 1.0
 */
public interface CoreTaskLogMapper<T extends CoreTaskLog> extends
		BaseSqlMapper<T> {
	public List<T> getAllCoreTaskLog();

}