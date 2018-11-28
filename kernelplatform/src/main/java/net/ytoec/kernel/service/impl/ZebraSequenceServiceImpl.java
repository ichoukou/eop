package net.ytoec.kernel.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.ytoec.kernel.dao.ZebraSequenceDao;
import net.ytoec.kernel.dataobject.QuerySequenceCondition;
import net.ytoec.kernel.dataobject.ZebraSequence;
import net.ytoec.kernel.service.ZebraSequenceService;

/**
 * @作者：罗典
 * @描述：操作sequence服务类
 * @时间：2013-10-24
 * */
@Service
public class ZebraSequenceServiceImpl implements ZebraSequenceService {
	@Inject
	private ZebraSequenceDao<ZebraSequence> zebraSequenceDao;

	/**
	 * @作者：罗典
	 * @时间：2013-10-23
	 * @描述：根据序列查询条件统计序列总数
	 * */
	@Override
	public int countByCondition(QuerySequenceCondition condition) {
		return zebraSequenceDao.countByCondition(condition);
	}

	/**
	 * @作者：罗典
	 * @时间：2013-10-23
	 * @描述：根据序列查询条件查询序列相关信息
	 * */
	@Override
	public List<ZebraSequence> queryByCondition(QuerySequenceCondition condition) {
		return zebraSequenceDao.queryByCondition(condition);
	}

}
