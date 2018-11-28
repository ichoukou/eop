package net.ytoec.kernel.dao.impl;

import javax.inject.Inject;

import net.ytoec.kernel.dao.ZebraSequenceDao;
import net.ytoec.kernel.dataobject.ZebraSequence;
import net.ytoec.kernel.mapper.ZebraSequenceMapper;

import org.springframework.stereotype.Repository;

@Repository
public class ZebraSequenceDaoImpl<T extends ZebraSequence> implements
		ZebraSequenceDao<T> {

	@Inject
	private ZebraSequenceMapper<ZebraSequence> mapper;

	@Override
	public int insertZebraSequence(ZebraSequence zebraSequence) {
		return mapper.insertZebraSequence(zebraSequence);
	}
}
