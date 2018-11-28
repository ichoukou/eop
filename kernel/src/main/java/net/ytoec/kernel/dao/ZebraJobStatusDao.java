package net.ytoec.kernel.dao;

import net.ytoec.kernel.dataobject.ZebraJobStatus;

public interface ZebraJobStatusDao<T> {

	/**
	 * 根据job名称获取job状态
	 * 
	 * @param jobName
	 * @return
	 */
	int getJobStatus(ZebraJobStatus jobStatus);

	/**
	 * 根据job名称更新job状态
	 * 
	 * @param jobName
	 * @return
	 */
	int updateJobStatus(ZebraJobStatus jobStatus);

	/**
	 * 根据job名称插入job状态
	 * 
	 * @param jobName
	 */
	int saveJobStatus(ZebraJobStatus jobStatus);
}
