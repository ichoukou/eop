/**
 * Feb 21, 201211:30:38 AM
 * wangyong
 */
package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.MailTendency;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 运单走势Mapper
 * @author wangyong
 * Feb 21, 2012
 */
@SuppressWarnings("all")
public interface MailTendencyMapper<T extends MailTendency> extends
		BaseSqlMapper<T> {

	/**
	 * 根据网点id统计某天内的运单数量map里存在partitionDate键和siteId键
	 * @param map
	 * @return
	 */
	public List<T> countBranchTendency(Map map);
	
	/**
	 * 根据平台用户的分仓统计某天内的运单数量map里存在partitionDate键和userId键
	 * @param map
	 * @return
	 */
	public List<T> countPingTaiTendency(Map map);
	
	/**
	 * 根据网点id获取列表
	 * @param siteId
	 * @return
	 */
	public List<T> getRepeatSiteList(String siteId);
	
	
}
