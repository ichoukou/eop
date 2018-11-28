package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.action.remote.xml.UpdateInfo;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.SearchReportBean;
import net.ytoec.kernel.dto.OrderWeightUpdateDTO;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;
import net.ytoec.kernel.search.dto.MailObjectDTO;

import org.springframework.dao.DataAccessException;

/**
 * 
 * @author ChenRen
 * @date 2011-7-20
 */
@SuppressWarnings("all")
public interface OrderMapper<T extends Order> extends BaseSqlMapper<T> {

	/**
	 * 根据电话号码更新淘宝号进入订单
	 * @param params
	 * buyerNick
	 * customerId
	 * buyerMobile
	 */
	public void updateBuyerNickToOrder(Map<String, Object> params);
	
	public List<T> getAllOrder();

	/**
	 * 根据logistic获取Order对象.
	 * 
	 * @param logisticId
	 * @return
	 */
	public T getOrderByLogisticId(String logisticId);
	
	/**
	 * 根据mailNo获取Order对象.
	 * 
	 * @param logisticId
	 * @return
	 */
	public T getOrderbyMailNo(String mailNo);

	/**
	 * 根据tradeNo获取Order对象.
	 * 
	 * @param tradeNo
	 * @return
	 */
	public T getOrderByTradeNo(String tradeNo);

	/**
	 * 电子账单
	 * 
	 * @return
	 */
	public List<T> ecAccount(Map map);
	
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
	public List<T> ecAccountHalf(Map map);
	
	/**
	 * 电子账单/通过运费模板统计
	 * 
	 * @return
	 */
	public List<T> ecAccountByPosttemp(Map map);

	/**
	 * 根据运单号查询一个订单记录<br>
	 * <b>运单号在数据库中是唯一的</b>
	 * 
	 * @param mailNo
	 * @return
	 */
	public List<T> getOrderByMailNo(String mailNo);

	/**
	 * 根据物流号更新重量信息
	 * 
	 * @param LogisticId
	 * @param weight
	 */
	public int updateOrderWeightByMailNo(Map map);

	/**
	 * 根据订单号更新重量信息
	 * 
	 * @param LogisticId
	 * @param weight
	 */
	public void updateOrderWeightByTradeNo(Map map);
	
	public int updateOrderByMailNo(Map map);

	public List<T> getOrdersByWeight(Map map) throws Exception;
	
	public List<String> getMailNoByPartitionDate(Map map) throws Exception;
 
	public int countOrdersByWeight(Map map) throws Exception;
	
	/**
	 * 根据物流号更新订单的业务的交易号
	 * 
	 * @param logisticId
	 * @param clientId
	 * @param tradeNo
	 * @return
	 */
	public void updateOrderMailNoByLogisticIdAndClientId(Map map) throws Exception;
	
	public int updateOrderMailNoByLogisticIdAndPartition(Map map) throws Exception;
	
	public void updateMailNoAndWeightByLogisticId(Map map) throws Exception;
	
	/**
	 * 根据订单状态status为1和0不同查询监控订单信息<br>
	 * 1表示订单送达成功、0表示除送达成功外其他订单。status封装在Map对象里
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<T> getOrderMonitorList(Map map) throws DataAccessException;
	
	/**
	 * 统计订单状态status为1和0不同查询监控订单信息<br>
	 * 1表示订单送达成功、0表示除送达成功外其他订单。status封装在Map对象里
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

	/**
	 * 根据orderId和交易人类型查询order信息
	 * 
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getAddrByOrderId(Map map) throws DataAccessException;
	
	/**
	 * 根据运单号查询订单列表
	 * @param mailNo
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> queryOrderListByMailNo(String mailNo) throws DataAccessException;
	
	/**
	 * 根据运单号查询订单列表和客户编码（客户编码参数是一个集合）
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> queryOrderListByMailNoAndCustomerid(Map map) throws DataAccessException;
	
	/**
	 * 根据运单号查询订单表和交易人信息表。获取部分订单信息和交易人信息。
	 * @param mailNo
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> queryOrderTranderInfoByMailNo(String mailNo) throws DataAccessException;
	
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
	 * 多customerId账号：根据订单表时间和交易人信息表买家姓名和电话查询某个用户下的运单号列表。
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
	public List getOrderbyMailNoAndCustomerId(Map map) throws DataAccessException;
	
	/**
	 * 通过运单号查找交易人信息
	 * @param mailNo
	 * @return
	 * @throws DataAccessException
	 */
	public List getBuyerInfoByMailNo(String mailNo) throws DataAccessException;
	
	public List getOrderbyMailNoCustomerId(Map map) throws DataAccessException;

	public List<T> queryOrderPrint(Map map);

	public List<T> queryOrderList(Map map);
	
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

    /**
     * 全量build数据到solr
     */
	 public List<MailObjectDTO> bulidEccoreData(Map map);
	 
	 
	 public int countOrdersByPages(Map map);
	
     public int updateMailNoById(Map map);	
     
     public void cancelOrder(Map map);

    /**
     * 增量build数据到solr
     */
    public List<MailObjectDTO> bulidPartEccoreData(Map map);
    
    /**
     * 用来查询上一天的order记录
     * @param map
     * @return
     */
    public List<OrderWeightUpdateDTO> getOrderByPartitiondate(Map map);
    
    public List<OrderWeightUpdateDTO> getOrderByPartitiondateById(Map map);
    
    /**
     * 根据id更新重量
     * @param map
     * @return
     */
    public Integer updateWeightByIdAndPartitiondate(OrderWeightUpdateDTO orderWeightUpdateDTO);
    
    

    /**
     * 增量build数据到solr,
     */
    public List<MailObjectDTO> bulidPartEccoreStatusData(Map map);
    
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
     * 查询一个卖家最近的10条订单
     * @param customerId 卖家的taobaoEncodeKey
     * @return
     */
    public List<String> queryTenOrdersByCustomerId(Map<String, Object> params);
    

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
     * 根据条件查询  卖家 签收完成时间比率
     * @param map
     *        String theId   卖家customerId
     *        String startDate    查询开始日期
     *        String endDate      查询结束日期
     * @return
     */
    public List<SearchReportBean> getSellHourRatioByPamams(Map<String, Object> map);
    
    /**
     * 根据条件查询  平台 签收完成时间比率
     * @param map
     *        String theId   平台用户的id
     *        String startDate    查询开始日期
     *        String endDate      查询结束日期
     * @return
     */
    public List<SearchReportBean> getPlatformHourRatioByPamams(Map<String, Object> map);
    
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
     * add new
     * @param list
     */
    public void updateOrderByTxlogisc(Map<String, Object> map);
    
    /**
     * 增量build数据到solr
     */
    public MailObjectDTO bulidPartEccoreDataByOrderId(Map map);
    
    
    /**
     * 增量build数据到solr
     */
    public List<MailObjectDTO> bulidPartEccoreDataOther(Map map);
}
