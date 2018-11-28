/**
 * 
 */
package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.TraderInfo;

/**
 * @author wangyong
 *
 */
public interface TraderInfoService<T> {

	public boolean delTraderInfoByOrderId(int orderId);
	
	public T getTradeBuId(int id);
	
	/**
	 * 交易人信息表时间任务，从列表中根据分区时间查询prov、numProv信息情况。
	 * @return
	 */
	public List<T> traderInfoTimerList(Integer startIndex, Integer pageNum);
	
	/**
	 * 统计总数
	 * @return
	 */
	public int countTraderInfoTimerList();
	
	public boolean editTraderInfo(T entity);
	
	public List<T> getTraderInfoByOrderId(int orderId);
	
	public List<T> getTraderInfo4Question(int orderId,String tradeType);
	
	public boolean addTraderInfo(T entity);
	
	/**
	 * 查询traderInfo 表
	 * @param partition 分区时间
	 * @param limit 每次查询的个数
	 * @return
	 */
	public List<TraderInfo> getTraderInfo(String partition, Integer limit);
	
	/**
	 * 查询traderInfo表
	 * @param partition 分区时间
	 * @param traderId 从指定的ID数开始查起
	 * @param limit 每次查询的个数
	 * @return
	 */
	public List<TraderInfo> getTraderInfoById(String partition, Integer traderId, Integer limit);
	
	/**
	 * 更新traderInfo中的省份信息
	 */
	public void updateTraderInfo(List<TraderInfo> traderInfos);
	
	public TraderInfo getTraderInfoByIdAndByTraderType(int orderId,int traderType);
	
}
