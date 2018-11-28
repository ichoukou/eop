package net.ytoec.kernel.mapper;

import java.util.Map;

import net.ytoec.kernel.dataobject.ZebraSequence;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 获取面单的请求序列mapper
 * 
 * @author huangtianfu
 * 
 * @param <T>
 */
public interface ZebraSequenceMapper<T extends ZebraSequence> extends
		BaseSqlMapper<T> {
	public int updateStateById(Map<String,Object> map);
	public ZebraSequence queryZebraSequence(String sequenceId);
	public String isExistSequence(Map<String,Object> map);
	
	int insertZebraSequence(ZebraSequence zebraSequence);

	int updateState(Map<String, Object> params);

	String selectLastSyningTime(String customerCode);

	int updateSecondCreateTime(Map<String, Object> params);
}
