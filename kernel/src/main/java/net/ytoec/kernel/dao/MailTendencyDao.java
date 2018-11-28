/**
 * Feb 21, 201211:41:16 AM
 * wangyong
 */
package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

/**
 * 运单走势dao
 * @author wangyong
 * Feb 21, 2012
 */
@SuppressWarnings("all")
public interface MailTendencyDao<T> {
	
	/**
	 * 从order表中添加数据，注意mailno_create_time对应订单表的partitiondate
	 * @param entity
	 * @return
	 */
	public boolean addMailTendency(T entity);
	
	public boolean removeMailTendency(T entity);

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
