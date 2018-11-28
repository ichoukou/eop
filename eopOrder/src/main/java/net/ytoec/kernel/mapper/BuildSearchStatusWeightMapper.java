package net.ytoec.kernel.mapper;


import java.util.List;

import net.ytoec.kernel.dataobject.BuildSearch;
import net.ytoec.kernel.dataobject.BuildSearchStatus;
import net.ytoec.kernel.dataobject.BuildSearchStatusWeight;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

public interface BuildSearchStatusWeightMapper<T extends BuildSearchStatusWeight> extends BaseSqlMapper<T> {
	
	
	public List<T> getBuildSearchByLimit(int limit);
	
	public Boolean removeBuildSearchByLimit(int limit);

}
