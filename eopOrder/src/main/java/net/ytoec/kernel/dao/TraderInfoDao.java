package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.dataobject.TraderInfo;

/**
 * 交易者（买、卖）联系信息相关操作
 * 
 * @author huaiwen
 * 
 * @param <T>
 */
public interface TraderInfoDao<T> {

	/**
	 * 新增一个交易者信息
	 * 
	 * @param traderInfo
	 * @return
	 */
	public boolean addTraderInfo(T traderInfo)throws DataAccessException;
	
	public List<T> getTraderInfoByPartitiondate(int orderId,String partitiondate);

	/**
	 * 批量增加交易者信息
	 * 
	 * @param traderInfo
	 * @return
	 */
	public boolean batchAddTraderInfo(List<T> traderInfo);

	/**
	 * 根据订单Id获取订单对应的交易者信息
	 * 
	 * @param orderId
	 * @return
	 */
	public List<T> getTraderInfoByOrderId(int orderId);
	
	/**
	 * 根据订单Id和tradeType获取订单对应的交易者信息
	 * 
	 * @param orderId
	 * @return
	 */
	public List<T> getTraderInfo4Question(int orderId,String tradeType);

	/**
	 * 根据Id获取订单对应的交易者信息
	 * 
	 * @param id
	 * @return
	 */
	public T getTraderInfoById(int id);

	/**
	 * 根据订单Id删除订单对应的交易者信息
	 * 
	 * @param orderId
	 * @return
	 */
	public boolean delTraderInfoByOrderId(int orderId);

	/**
	 * 查询所有的交易人信息
	 * 
	 * @return
	 */
	public List<T> getAllTraderInfo();

	/**
	 * 修改交易人信息
	 * 
	 * @param entity
	 */
	public boolean editTraderInfo(T entity);

	/**
	 * 删除交易人信息
	 * 
	 * @param entity
	 * @return
	 */
	public boolean delTraderInfo(T entity);
	
	/**
	 * 交易人信息表时间任务，从列表中根据分区时间查询prov、numProv信息情况。
	 * @return
	 */
	public List<T> traderInfoTimerList(Map map);
	
	public int countTraderInfoTimerList();
	
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
     * 批量更新联系人表的numProv字段
     * @param traderInfos
     */
    public void batchUpdateProvince(List<TraderInfo> traderInfos);
    
    /**
     * 根据订单ID 和交易者类型，查询交易者信息
     */
    public TraderInfo getTraderInfoByIdAndByTraderType(int orderId,int traderType);
    
    
}
