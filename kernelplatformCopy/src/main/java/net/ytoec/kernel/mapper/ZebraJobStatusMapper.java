package net.ytoec.kernel.mapper;

import net.ytoec.kernel.dataobject.ZebraJobStatus;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

public interface ZebraJobStatusMapper<T extends ZebraJobStatus> extends BaseSqlMapper<T> {

	int getJobStatus(ZebraJobStatus jobStatus);

	int updateJobStatus(ZebraJobStatus jobStatus);

	int addJobStatus(ZebraJobStatus jobStatus);
}
