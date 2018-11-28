package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 
 * 联系人数据映射器, (联系人指发件人、收件人).
 * 
 * @param <T>
 *            TraderInfo
 */
public interface TraderInfoMapper<T extends TraderInfo> extends BaseSqlMapper<T> {
	public List<T> getAllTraderInfo();

	/**
	 * 根据订单Id查询交易人信息
	 * 
	 * @param orderId
	 * @return
	 */
	public List<T> getTraderInfoByOrderId(int orderId);
	
	/**
	 * 根据订单Id和tradeType查询交易人信息
	 * 
	 * @param orderId
	 * @return
	 */
	public List<T> getTraderInfo4Question(Map map);
	
	/**
	 * 根据订单分区和Id查询交易人信息
	 * 
	 * @param orderId
	 * @return
	 */
	public List<T> getTraderInfoByPartitiondate(Map map);

	/**
	 * 根据订单Id删除交易人信息
	 * 
	 * @param orderId
	 * @return 受影响的行数
	 */
	public int delTraderInfoByOrderId(int orderId);
	
	/**
	 * 交易人信息表时间任务，从列表中根据分区时间查询prov、numProv信息情况。
	 * 每次查询pageNum个记录
	 * @param map
	 * @return
	 */
	public List<T> traderInfoTimerList(Map map);
	
	public int countTraderInfoTimerList();
	
	   /**
     * 查询traderInfo 表
     * @param 封装查询参数，partition，limit
     * @return
     */
    public List<T> getTraderInfo(Map map);
    
    /**
     * 查询traderInfo表
     * @param map 封装查询参数 partition，limit  traderId
     * @return
     */
    public List<T> getTraderInfoById(Map map);
    
    /**
     * 根据prov跟新numProv
     * @param traderInfo
     * @return
     */
    public Integer updateProviceInfo(TraderInfo traderInfo);
    
    /**
     * 根据订单Id和type查询交易人信息
     * @param orderId
     * @return
     */
    public TraderInfo getTraderInfoByIdAndByTraderType(Map map);
}
