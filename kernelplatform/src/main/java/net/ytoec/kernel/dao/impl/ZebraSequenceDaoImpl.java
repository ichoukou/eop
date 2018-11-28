package net.ytoec.kernel.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dao.ZebraSequenceDao;
import net.ytoec.kernel.dataobject.QuerySequenceCondition;
import net.ytoec.kernel.dataobject.ZebraSequence;
import net.ytoec.kernel.mapper.ZebraSequenceMapper;

import org.springframework.stereotype.Repository;

@Repository
public class ZebraSequenceDaoImpl<T extends ZebraSequence> implements
		ZebraSequenceDao<T> {

	@Inject
	private ZebraSequenceMapper<ZebraSequence> mapper;
	/**
	 * @作者：罗典
	 * @时间：2013-10-23
	 * @描述：根据序列查询条件统计序列总数
	 * */
	public int countByCondition(QuerySequenceCondition condition){
		return mapper.countByCondition(condition);
	}
	/**
	 * @作者：罗典
	 * @时间：2013-10-23
	 * @描述：根据序列查询条件查询序列相关信息
	 * */
	public List<ZebraSequence> queryByCondition(QuerySequenceCondition condition){
		return mapper.queryByCondition(condition);
	}
	
	/**
	 * @作者：罗典
	 * @时间：2013-08-30
	 * @描述：判断商家时间段内是否某状态的序列
	 * @参数：customerCode 商家编码，state：序列状态，timeInterval：时间区间(分钟)
	 * */
	@Override
	public Boolean isExistSequence(String customerCode,int state,int timeInterval){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("timeInterval", timeInterval);
		params.put("customerCode", customerCode);
		params.put("state", state);
		return mapper.isExistSequence(params) == null ? false : true;
	}

	/**
	 * @作者：罗典
	 * @时间：2013-09-01
	 * @描述：根据序列号查询有效的序列信息
	 * @参数：sequenceId序列号
	 * */
	@Override
	public ZebraSequence queryZebraSequence(String sequenceId) {
		return mapper.queryZebraSequence(sequenceId);
	}
	
	
	/**
	 * @作者：罗典
	 * @时间：2013-09-01
	 * @描述：根据序列号修改序列信息
	 * @参数：sequenceId序列号
	 * */
	@Override
	public int updateStateById(String sequenceId,int state) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sequenceId", sequenceId);
		params.put("state", state);
		return mapper.updateStateById(params);
	}


	@Override
	public int insertZebraSequence(ZebraSequence zebraSequence) {
		return mapper.insertZebraSequence(zebraSequence);
	}

	@Override
	public boolean updateState(int sequence, String customerCode, int state) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sequence", sequence);
		params.put("customerCode", customerCode);
		params.put("state", state);
		return mapper.updateState(params) > 0;
	}

	@Override
	public String selectLastSyningTime(String customerCode) {
		return mapper.selectLastSyningTime(customerCode);
	}

	@Override
	public boolean updateSecondCreateTime(String sequence,String customerCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sequence", sequence);
		params.put("customerCode", customerCode);
		return mapper.updateSecondCreateTime(params) > 0;
	}
}
