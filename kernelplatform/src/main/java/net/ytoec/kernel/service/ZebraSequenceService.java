package net.ytoec.kernel.service;

import java.util.List;

import net.ytoec.kernel.dataobject.QuerySequenceCondition;
import net.ytoec.kernel.dataobject.ZebraSequence;

/**
 * @作者：罗典
 * @描述：操作sequence服务类
 * @时间：2013-10-24
 * */
public interface ZebraSequenceService {
	/**
	 * @作者：罗典
	 * @时间：2013-10-23
	 * @描述：根据序列查询条件统计序列总数
	 * */
	public int countByCondition(QuerySequenceCondition condition);
	/**
	 * @作者：罗典
	 * @时间：2013-10-23
	 * @描述：根据序列查询条件查询序列相关信息
	 * */
	public List<ZebraSequence> queryByCondition(QuerySequenceCondition condition);
	
}
