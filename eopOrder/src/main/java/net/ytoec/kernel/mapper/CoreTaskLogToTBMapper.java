package net.ytoec.kernel.mapper;

import java.util.List;

import net.ytoec.kernel.dataobject.CoreTaskLog;
import net.ytoec.kernel.dataobject.CoreTaskLogToTB;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * <b>
 * 
 * @author fulan
 * @package net.ytoec.kernel
 * @project kernel
 * @email
 * @version 1.0S
 */
public interface CoreTaskLogToTBMapper<T extends CoreTaskLogToTB> extends
		BaseSqlMapper<T> {
	public List<T> getAllCoreTaskLog();

}