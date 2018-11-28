package net.ytoec.kernel.dao;

import net.ytoec.kernel.dataobject.ZebraSequence;

/**
 * 获取电子面单的请求序列dao
 * 
 * @author huangtianfu
 * 
 */
public interface ZebraSequenceDao<T> {

	int insertZebraSequence(ZebraSequence zebraSequence);

}
