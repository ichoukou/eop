package net.ytoec.kernel.mapper;

import net.ytoec.kernel.dataobject.ApiLog;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

public interface ApiLogMapper<T extends ApiLog> extends BaseSqlMapper<T> {
	public int insertApiLog(ApiLog log);
}
