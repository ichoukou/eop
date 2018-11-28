package net.ytoec.kernel.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.action.remote.xml.UpdateInfo;
import net.ytoec.kernel.dataobject.SearchReportBean;
import net.ytoec.kernel.dto.OrderWeightUpdateDTO;
import net.ytoec.kernel.search.dto.MailObjectDTO;

import org.springframework.dao.DataAccessException;

/**
 * 订单相关接口
 * 
 * @author huaiwen
 * 
 * @param <T>
 */
public interface OrderDao<T> {
	
	/**
	 * 根据电话号码更新淘宝号进入订单
	 * @param params
	 * buyerNick
	 * customerId
	 * buyerMobile
	 */
	public void updateBuyerNickToOrder(Map<String, Object> params);
	
	/**
	 * 创建訂单信息(订单本身对应的表)
	 * 
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public boolean addOrder(T entity) throws DataAccessException;

	/**
	 * 根据Id读取单个订单
	 * 
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public T getOrderById(Integer id) throws DataAccessException;

	/**
	 * 根据物流号LogisticId读取单个订单
	 * 
	 * @param logisticId
	 * @return
	 * @throws DataAccessException
	 */
	public T getOrderByLogisticId(String logisticId) throws DataAccessException;

	/**
	 * 根据订单号tradeNo读取单个订单
	 * 
	 * @param tradeNo
	 * @return
	 * @throws DataAccessException
	 */
	public T getOrderBytradeNo(String tradeNo) throws DataAccessException;

	/**
	 * 更新订单的状态
	 * 
	 * @param orderStatus
	 * @return
	 */
	public boolean updateOrderStatus(String orderStatus);

	/**
	 * 根据物流号更新订单的重量信息
	 * 
	 * @param weight
	 * @return
	 */
	public boolean updateOrderWeightByMailNo(String mailNo, float weight,Date starttime,Date endtime);

	/**
	 * 根据订单号更新订单的重量信息
	 * 
	 * @param weight
	 * @return
	 */
	public boolean updateOrderWeightByTradeNo(String tradeNo, float weight);
	
	public boolean updateOrderByMailNo(Map map);

	/**
	 * 根据客户Id批量查找订单信息
	 * 
	 * @param vipId
	 * @return
	 */
	public List<T> getOrderListByVipId(int vipId);

	/**
	 * 根据物流号删除订单（订单相关表：product、traderInfo 需要一起做删除）
	 * 
	 * @param LogisticId
	 * @return
	 */
	boolean delOrderByLogisticId(int LogisticId);

	/**
	 * 获取所有订单
	 * 
	 * @return
	 * @throws DataAccessException
	 */
	public abstract List<T> getAllOrder() throws DataAccessException;

	/**
	 * 修改订单信息
	 * 
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public abstract boolean updateOrder(T entity) throws DataAccessException;

	/**
	 * 删除一个订单
	 * 
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public abstract boolean delOrder(T entity) throws DataAccessException;

	/**
	 * 电子账单
	 * 
	 * @param order
	 * @return
	 */
	public abstract List<T> ecAccount(Map map) throws DataAccessException;

	/**
	 * 电子账单<br>
	 * 
	 * @author ChenRen/2011-09-26<br>
	 *         两表联查，只查询目的地或发件地；程序中要另外处理把目的地和发件地整合到一个对象里<br>
	 * 
	 * @param map
	 *            map中有必传参数k(v)：addr(fromAddr/toAddr);tradeType(0/1);vipId;
	 *            starttime;endtime<br>
	 *            其中: addr和tradeType里的参数顺便要保持一致；即fromAddr对应tradeType的0
	 * @return
	 */
	public abstract List<T> ecAccountHalf(Map map) throws DataAccessException;
	
	/**
	 * 根据订单Id和交易人类型查询订单信息<br>
	 * 必填字段：addr的类型(fromAddr/toAddr),orderId, tradeType
	 * 
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public abstract List<T> getAddrByOrderId(Map map) throws DataAccessException;

	/**
	 * 电子账单/通过运费模板统计
	 * 
	 * @param order
	 * @return
	 */
	public abstract List<T> ecAccountByPosttemp(Map map)
			throws DataAccessException;

	/**
	 * 根据运单号查询一个订单记录<br>
	 * <b>运单号在数据库中是唯一的</b>
	 * 
	 * @param mailNo
	 * @return
	 */

	public abstract T getOrderbyMailNo(String mailNo)
			throws DataAccessException;

	/**
	 * 根据运单号查询订单记录<br>
	 * 
	 * @param mailNo
	 * @return
	 */
	public abstract List<T> getOrderByMailNo(String mailNo)
			throws DataAccessException;

	/**
	 * 根据时间查找没有重量信息的订单记录
	 * 
	 * @param time
	 * @return
	 */
	public abstract List<T> getOrdersByWeight(String startTime, String endTime,
			int start, int limit) throws DataAccessException;

	/**
	 * 根据时间查找半个月内的订单
	 * 
	 * @param time
	 * @return
	 */
	public abstract List<String> getMailNoByPartitionDate(String startTime, String endTime,String customerId) throws DataAccessException;
	
	/**
	 * 根据时间统计没有重量信息的订单记录的数量
	 * 
	 * @param time
	 * @return
	 */
	public abstract int countOrdersByWeight(String startTime, String endTime)
			throws DataAccessException;

	/**
	 * 根据物流号和ClientId更新订单的运单号(mailNo)或订单状态(status)<br>
	 * 
	 * @param logisticId
	 * @param clientId
	 * @param tradeNo
	 * @return
	 */
	public boolean updateOrderMailNoByLogisticIdAndClientId(Map map)
			throws DataAccessException;
	
	public boolean updateMailNoAndWeightByLogisticId(Map map)
			throws DataAccessException;

	/**
	 * 根据订单状态status为1和0不同查询监控订单信息<br>
	 * 1表示订单送达成功、0表示除送达成功外其他订单。status封装在Map对象里
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<T> getOrderMonitorList(Map map) throws DataAccessException;
	
	/**
	 * 统计订单状态status为1和0不同查询监控订单信息<br>
	 * 1表示订单送达成功、0表示除送达成功外其他订单。status封装在Map对象里
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int countOrderMonitorList(Map map) throws DataAccessException;
	
	/**
	 * 大商家角色监控订单信息方法
	 * 根据订单状态status为1和0不同查询监控订单信息<br>
	 * 1表示订单送达成功、0表示除送达成功外其他订单。status封装在Map对象里
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<T> getMerchantMonitorOrderList(Map map) throws DataAccessException;
	
	/**
	 * 统计大商家角色监控订单信息数据大小
	 * 根据订单状态status为1和0不同查询监控订单信息<br>
	 * 1表示订单送达成功、0表示除送达成功外其他订单。status封装在Map对象里
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int countMerchantMonitorOrderList(Map map) throws DataAccessException;
	
	/**
	 * 按时间查询，先查询VIP用户下的订单信息，该时间段下的所有mailNo信息，再从金刚接口中查询物流信息
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public List getOrderMailNoListByTime(Map map) throws DataAccessException;
	
	
	public List cleanOrderList(Map map) throws DataAccessException;
	
	public List<T> queryOrderListByMailNo(String mailNo);
	
	/**
	 * 根据运单号查询订单列表和客户编码（客户编码参数是一个集合）
	 * @param map
	 * @return
	 */
	public List<T> queryOrderListByMailNoAndCustomerid(Map map);
	
	/**
     * 根据运单号查询订单表和交易人信息表。获取部分订单信息和交易人信息。
     * @param mailNo
     * @return
     * @throws DataAccessException
     */
    public List<T> queryOrderTranderInfoByMailNo(String mailNo);
    
    /**
	 * 根据订单表时间和交易人信息表买家姓名和电话查询某个用户下的运单号列表。
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public List getOrderMailNoListByBuyerInfo(Map map) throws DataAccessException;
	
	/**
	 * 多customerId账号：按时间查询，先查询VIP用户下的订单信息，该时间段下的所有mailNo信息，再从金刚接口中查询物流信息
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public List relatedOrderMailNoListByTime(Map map) throws DataAccessException;
	
	/**
	 * 根据订单表时间和交易人信息表买家姓名和电话查询某个用户下的运单号列表。
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public List relatedOrderMailNoListByBuyerInfo(Map map) throws DataAccessException;
	
	/**
	 * 根据mailNo和customerId查询订单信息
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getOrderbyMailNoAndCustomerId(Map map) throws DataAccessException;
	
	/**
	 * 通过运单号查找交易人信息
	 * @param mailNo
	 * @return
	 * @throws DataAccessException
	 */
	public List getBuyerInfoByMailNo(String mailNo) throws DataAccessException;
	
	
	public List<T> getOrderbyMailNoCustomerId(Map map) throws DataAccessException;
	
	/**
	 * 根据custormerIdList和partitionDate查询订单数量(除‘UNACCEPT’和‘WITHDRAW’状态的订单)
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public int countCurrentDayOrderByCustormerId(Map map) throws DataAccessException;
	
	/**
	 * 根据关联账号和运单状态获取某时间段内的运单数量
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public int countOrderByStatusAndCustormerId(Map map) throws DataAccessException;

	public List<T> queryOrderPrint(Map map);

	public List<T> queryOrderList(Map map);

    public List<MailObjectDTO> bulidEccoreData(Map map);
    
    public int countOrders(Map map);
    
    public boolean updateMailNoById(Map map);
    
    public boolean cancelOrder(Map map);

    /**
     * 增量build数据到solr
     */
    public List<MailObjectDTO> bulidPartEccoreData(Integer limit);

    /**
     * 根据时间参数查询订单
     * 
     * @param partitiondate 时间参数
     * @param limit 查询的记录数
     * @return List
     */
    public List<OrderWeightUpdateDTO> getOrderByPartitiondate(String partitiondate,Integer limit);
    /**
     * 根据时间参数和id查询订单
     * 
     * @param partitiondate 时间参数
     * @param limit 查询的记录数
     * @return List
     */
    public List<OrderWeightUpdateDTO> getOrderByPartitiondateById(String partitiondate,Integer id,Integer limit);
    
    /**
     * 根据id和分区(可选）更新重量
     * @param map
     * @return
     */
    public void updateWeightByIdAndPartitiondate(Map map);
    
    /**
     * 批量更新重量
     * @param orderWeightUpdateDTOs
     */
    public  void batchUpdateWeight(List<OrderWeightUpdateDTO> orderWeightUpdateDTOs)  ; 
    
    /**
     * 增量build数据到solr
     */
    public List<MailObjectDTO> bulidPartEccoreStatusData(Integer limit, String currentDate, String endDate);
    
    /**
     * 订正了联系人表的数据，要更新order表，以便出发solr 增量操作
     * @param entiry
     */
    public void  updateTimeById(T entiry);
    
    /**
     * 修改从order_print表同步过来的运单号和商品价值
     * @param entiry
     */
    public void  updateMailNoValue(T entiry);
    
    
    /**
     * 根据ids更新订单的旺旺号
     * @param map
     * @return
     */
    public Integer updateOrderByIds(Map<String, Object> map);
    
    /**
     * 根据条件查询 每日成功签收比率
     * @param map
     *        String customerId   卖家或平台用户的customerId
     *        String startDate    查询开始日期
     *        String endDate      查询结束日期
     * @return
     */
    public List<SearchReportBean> getSignRatioByPamams(Map<String, Object> map);
    
    /**
     * 根据条件查询 卖家 每日成功签收比率 [同一发货时间,afterDay天内的接受数/同一发货时间的总数]
     * @param 
     *        String theId        卖家customerId
     *        String startDate    发货开始日期
     *        String endDate      发货结束日期
     * @return
     */
    public int getSellSignRatioForDayByPamams(Map<String, Object> map);
    
    /**
     * 根据条件查询 平台 每日成功签收比率 [同一发货时间,afterDay天内的接受数/同一发货时间的总数]
     * @param 
     *        String theId        平台用户的ID
     *        String startDate    发货开始日期
     *        String endDate      发货结束日期
     * @return
     */
    public int getPlatformSignRatioForDayByPamams(Map<String, Object> map);
    
    /**
     * 根据条件查询 卖家 签收完成时间比率
     * @param map
     *        String theId        卖家customerId
     *        String startDate    查询开始日期
     *        String endDate      查询结束日期
     * @return
     */
    public List<SearchReportBean> getSellHourRatioByPamams(Map<String, Object> map);
    
    /**
     * 根据条件查询 平台 签收完成时间比率
     * @param map
     *        String theId        平台用户的id
     *        String startDate    查询开始日期
     *        String endDate      查询结束日期
     * @return
     */
    public List<SearchReportBean> getPlatformHourRatioByPamams(Map<String, Object> map);

    /**
     * 查询一个卖家最近的10条订单
     * @param customerId 卖家的taobaoEncodeKey
     * @return
     */
    public List<String> queryTenOrdersByCustomerId(Map<String, Object> params);
    
    public void updaetBatchOrderByTxlogisc(List<UpdateInfo> list);
    
    public boolean updateOrderMailNoByLogisticIdAndPartition(Map map) throws Exception;
    
    public MailObjectDTO bulidPartEccoreDataByOrderId(int id,String partitiondate);
    
    public List<MailObjectDTO> bulidPartEccoreDataOther(Integer limit);
    
}
