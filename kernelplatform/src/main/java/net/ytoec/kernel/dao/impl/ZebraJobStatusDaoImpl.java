package net.ytoec.kernel.dao.impl;

import javax.inject.Inject;

import net.ytoec.kernel.dao.ZebraJobStatusDao;
import net.ytoec.kernel.dataobject.ZebraJobStatus;
import net.ytoec.kernel.mapper.ZebraJobStatusMapper;

import org.springframework.stereotype.Repository;

@Repository
public class ZebraJobStatusDaoImpl<T extends ZebraJobStatus> implements ZebraJobStatusDao<T> {

	@Inject
	private ZebraJobStatusMapper<ZebraJobStatus> mapper;

	@Override
	public int getJobStatus(ZebraJobStatus jobStatus) {
		return mapper.getJobStatus(jobStatus);
	}

	@Override
	public int updateJobStatus(ZebraJobStatus jobStatus) {
		return mapper.updateJobStatus(jobStatus);
	}

	@Override
	public int saveJobStatus(ZebraJobStatus jobStatus) {
		return mapper.addJobStatus(jobStatus);
	}

}
