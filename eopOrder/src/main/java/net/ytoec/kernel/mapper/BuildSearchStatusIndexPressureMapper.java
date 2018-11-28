package net.ytoec.kernel.mapper;

import net.ytoec.kernel.dataobject.BuildSearchStatus;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

public interface BuildSearchStatusIndexPressureMapper<T extends BuildSearchStatus> extends BaseSqlMapper<T> {
	
	public Boolean removeBuildSearchByLimit(int limit);
}
