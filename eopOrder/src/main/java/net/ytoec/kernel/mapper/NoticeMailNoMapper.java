package net.ytoec.kernel.mapper;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.SendTask;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;
/**
 * @author mabo
 * 
 * 通知仓配平台面单号映射
 * @param <T>
 */
public interface NoticeMailNoMapper<T extends SendTask> extends BaseSqlMapper<T> {

	/**
	 * 获取发送对象集合
	 * @param limit
	 * @return
	 */
	public List<T> getMailNoListByLimit(Integer limit);
	
	/**
	 * 插入运单号
	 * @param mailNo
	 * @return
	 */
	public Integer addMailNo(Map<String, String> map);
	
	/**
	 * 删除插入成功的运单号
	 * @param map
	 * @return
	 */
	public Integer removeMailNo(Map<String, String> map);
}
