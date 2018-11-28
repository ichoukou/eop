/**
 * Feb 22, 20123:59:05 PM
 * wangyong
 */
package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

/**
 * 运单走势业务处理
 * @author wangyong
 * Feb 21, 2012
 */
public interface MailTendencyService<T> {

	/**
	 * 从order表中添加数据，注意mailno_create_time对应订单表的partitiondate
	 * @param entity
	 * @return
	 */
	public boolean addMailTendency(T entity);
	
	public boolean removeMailTendecy(T entity);
	
	/**
	 * 获取某个网点下某天的运单数目
	 * @param siteId
	 * @param someDay
	 * @return
	 */
	public int countMailNoNumBySiteAndTime(Integer siteId, String someDay);
	
	/**
	 * 获取网点siteId下期限为timeLimit天内每天的运单数量。并将每天的时间属性及运单数作为键值对放入map集合中。
	 * @param siteId
	 * @param timeLimit
	 * @return
	 */
	public List<Map<String, Integer>> createTimeSeries(Integer siteId, int timeLimit);
	
	/**
	 * 获取某个网点下某天的运单列表
	 * @param siteId
	 * @param someDay
	 * @return
	 */
	public List<T> getMailTendencyBySiteAndTime(Integer siteId, String someDay);
	
	/**
	 * 获取某个网点、用户id下某天的运单列表
	 * @param siteId
	 * @param userId
	 * @param someDay
	 * @return
	 */
	public List<T> getMailTendencyBySiteUserTime(Integer siteId, Integer userId, String someDay);
	
	
	/**
	 * 获取平台分仓下某天的运单数目
	 * @param siteId
	 * @param someDay
	 * @return
	 */
	public int countPingTaiTendency(Integer userId, String someDay);
	
	
	
	
	/**
	 * 获取平台分仓下某天的运单数 userID 开始和结束时间内每天的运单数量。并将每天的时间属性及运单数作为键值对放入map集合中。
	 * @param userId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Map<String, Integer>> singleSeries(Integer userId, String startDate,String endDate,Integer timeLimit);
	
	
	/**
	 * 获取平台下某天的运单数 userID 开始和结束时间内每天的运单数量。并将每天的时间属性及运单数作为键值对放入map集合中。
	 * @param userId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Map<String, Integer>> pingTaiSeries(List<Integer> userId, String startDate,String endDate,Integer timeLimit);
	
	/**
	 * 根据网点id获取列表
	 * @param siteId
	 * @return
	 */
	public List<T> getRepeatSiteList(String siteId);
	
}
