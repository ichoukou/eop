package net.ytoec.kernel.mapper;

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

	int insertZebraSequence(ZebraSequence zebraSequence);
}