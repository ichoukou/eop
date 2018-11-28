package net.ytoec.kernel.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.action.order.QueryOrder;
import net.ytoec.kernel.action.remote.xml.UpdateInfo;
import net.ytoec.kernel.action.remote.xml.UpdateWaybillInfo;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.BuildSearch;
import net.ytoec.kernel.dataobject.BuildSearchStatusWeightIndex;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.OrderFormInfo;
import net.ytoec.kernel.dataobject.Posttemp;
import net.ytoec.kernel.dataobject.SearchReportBean;
import net.ytoec.kernel.dataobject.SendTaskToTB;
import net.ytoec.kernel.dto.DtoOrderPrint;
import net.ytoec.kernel.dto.OrderWeightUpdateDTO;
import net.ytoec.kernel.dto.TabUniteOrder;
import net.ytoec.kernel.search.dto.EccoreSearchResultDTO;

import org.springframework.dao.DataAccessException;

/**
 * 订单相关服务接口
 * 
 * @author huaiwen
 * 
 * @param <T>
 */
public interface OrderService<T> {

	/*	*//**
	 * 发货之后同步到淘宝
	 * 
	 * @param orderList
	 * @return
	 * @throws ApiException
	 */
	/*
	 * public boolean toTaobao(List<T> orderList);
	 */

	/**
	 * 发货之后同步到 金刚
	 * 
	 * @param orderList
	 * @return
	 */
	public boolean toJgWaybill(List<T> orderList);

	/**
	 * 创建订单信息（订单相关表：product、traderInfo）
	 * 
	 * @param entity
	 * @return @
	 */

	public boolean addOrder(T entity);

	/**
	 * 创建订单信息,由OrderFormInfo中获取相关信息.（订单相关表：product、traderInfo）
	 * 
	 * @param orderFormInfo
	 * @return @
	 */
	public boolean addOrderFormInfo(OrderFormInfo orderFormInfo);
	
	/**
	 * 创建订单信息,由OrderFormInfo中获取相关信息.
	 * @param orderFormInfo
	 * @return
	 * @throws Exception
	 */
	public Order addOrderFormInfoForTB(OrderFormInfo orderFormInfo)throws Exception;

	/**
	 * 根据Id读取单个订单（订单相关表：product、traderInfo）
	 * 
	 * @param id
	 * @return @
	 */
	public T getOrderById(Integer id);

	/**
	 * 根据物流号LogisticId读取单个订单（订单相关表：product、traderInfo）
	 * 
	 * @param id
	 * @return @
	 */
	public T getOrderByLogisticId(String id);

	/**
	 * 根据物流号LogisticId读取单个订单（订单相关表：product、traderInfo）
	 * 
	 * @param id
	 * @return @
	 */
	public T getOrderByMailNo(String mailNo);

	/**
	 * 根据订单号tradeNo读取单个订单（订单相关表：product、traderInfo）
	 * 
	 * @param id
	 * @return @
	 */
	public T getOrderByTradeNo(String tradeNo);

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
	public boolean updateOrderWeightByMailNo(String mailNo, Float weight,
			Date starttime, Date endtime);

	/**
	 * 根据物流号和ClientId更新订单的运单号(mailNo)或订单状态(status)<br>
	 * logisticId和clientId必填;否则抛异常;<br>
	 * mailNo/status 可能出现单独更新某一个字段的情况, 如果更新某一个自段, 另一个字段可以传 null 或者 ""
	 * 
	 * @param logisticId
	 *            物流号
	 * @param clientId
	 * @param mailNo
	 *            交易号
	 * @param status
	 *            状态
	 * @return
	 */
	public boolean updateOrderMailNoByLogisticIdAndClientId(String logisticId,
			String clientId, String mailNo, String status);

	/**
	 * 根据物流号和ClientId批量更新订单的运单号(mailNo)或订单状态(status)<br>
	 * 
	 * @param updateInfoList
	 * @return
	 * @author liuchunyan
	 */
	public boolean updOrderMailNoByLogisticIdAndClientId(
			List<UpdateWaybillInfo> updateInfoList, String jinGangOrder,
			String clientID) throws Exception;

	public boolean updateOrderByMailNo(String mailNo, String freightType,
			double trimFreight);

	/**
	 * 状态通知
	 * 
	 * @param updateInfo
	 * @return @
	 */
	public boolean orderStatusNodify(UpdateWaybillInfo updateInfo);

	public boolean orderAlterStatusNodify(UpdateWaybillInfo updateInfo,
			SendTaskToTB sendTaskToTB);

	public Map<String, Object> orderListStatusNodify(
			List<UpdateWaybillInfo> updateInfoList);

	public boolean orderListStatusNodifyFromMQ(
			List<UpdateWaybillInfo> updateInfoList) throws Exception;

	/**
	 * 根据订单号更新订单的重量信息
	 * 
	 * @param weight
	 * @return
	 */
	public boolean updateOrderWeightByTradeNo(String tradeNo, Float weight);

	/**
	 * 根据客户Id批量查找订单信息（订单相关表：product、traderInfo）
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
	 * 获得所有订单
	 * 
	 * @return @
	 */
	public abstract List<T> getAllOrder();

	/**
	 * 修改一个订单
	 * 
	 * @param entity
	 * @return @
	 */
	public abstract boolean updateOrder(T entity);

	/**
	 * 删除一个订单
	 * 
	 * @param entity
	 * @return @
	 */
	public abstract boolean delOrder(T entity);

	/**
	 * <pre>
	 * 电子对账改造：
	 *   1.数据来源改成从缓存查
	 *   2.页面逻辑改动：
	 *   	不区分按地区、按模板对账。统一按模板对账，且不显示 选择模板 的select. 因为卖家只有一个运费模板
	 *   	默认就显示 地区select(不显示首重、超重). 如果选择了地区就只查询某一个区域的订单. 否则查询所有地区
	 * </pre>
	 */
	public Map ecAccountList(Map map,
			Pagination<EccoreSearchResultDTO> pagination, Integer posttempId,
			Posttemp sysPt);

	/**
	 * 把数据导出成csv文件
	 * 
	 * @return 将DB查出的结果集用csv字符串格式返回
	 */
	public String output2csv(Map map);

	/**
	 * 把数据导出成csv文件<br>
	 * 使用运费模板计算数据
	 * 
	 * @param posttempId
	 *            运费模板Id
	 * @param sysPt
	 * @return 将DB查出的结果集用csv字符串格式返回
	 */
	public String output2csvByPosttemp(Map map, Integer posttempId,
			Posttemp sysPt);

	public String output2csv3(Map map, Integer posttempId, Posttemp sysPt,
			String customerCode);

	/**
	 * 根据运单号查询订单记录<br>
	 * 如果订单存在多个，返回第一个
	 * 
	 * @param mailNo
	 * @return
	 */
	public abstract T getOrderByMailNo(T entity);

	/**
	 * 根据时间点查询无重量信息订单记录
	 * 
	 * @param time
	 * @return @
	 */
	public List<T> getOrdersByWeight(String startTime, String endTime,
			int start, int limit);

	public List<String> getMailNoByPartitionDate(String startTime,
			String endTime, String customerId);

	public int countOrdersByWeight(String startTime, String endTime);

	/**
	 * 根据订单状态查询订单记录数据列表。
	 * 
	 * @param status
	 * @return @
	 */
	public List<T> getOrderByStatus(String status);

	/**
	 * 根据订单状态status为1和0不同查询监控订单信息<br>
	 * 
	 * @param status
	 *            1(成功订单)、2(正在派送)、3(走件中)、4(退货单)
	 * @param startTime
	 *            寄件时间起点
	 * @param endTime
	 *            寄件时间终点
	 * @param vipId
	 *            卖家用户ID
	 * @param mailNO
	 *            运单号
	 * @param orderBy
	 *            排序方式
	 * @param pagination
	 *            分页信息
	 * @param flag
	 *            是否分页
	 * @param bindedId
	 *            TODO
	 * @return @
	 */
	public List<T> getOrderMonitorList(Integer status, String startTime,
			String endTime, Integer vipId, String mailNO, String buyerName,
			String buyerPhone, Integer orderBy, Pagination pagination,
			boolean flag, List bindedId);

	/**
	 * 统计订单状态status为1和0不同查询监控订单信息<br>
	 * 
	 * @param status
	 *            1(成功订单)、2(正在派送)、3(走件中)、4(退货单)
	 * @param startTime
	 *            寄件时间起点
	 * @param endTime
	 *            寄件时间终点
	 * @param vipId
	 *            卖家用户ID
	 * @param mailNO
	 *            运单号
	 * @param bindedId
	 *            TODO
	 * @return @
	 */
	public int countOrderMonitorList(Integer status, String startTime,
			String endTime, Integer vipId, String mailNO, String buyerName,
			String buyerPhone, List bindedId);

	/**
	 * 大商家角色监控订单信息方法 根据订单状态status为1和0不同查询监控订单信息<br>
	 * 
	 * @param status
	 *            1(成功订单)、2(正在派送)、3(走件中)、4(退货单)
	 * @param startTime
	 *            寄件时间起点
	 * @param endTime
	 *            寄件时间终点
	 * @param vipId
	 *            卖家用户ID
	 * @param mailNO
	 *            运单号
	 * @param orderBy
	 *            排序方式
	 * @param pagination
	 *            分页信息
	 * @param flag
	 *            是否分页
	 * @return @
	 */
	public List<T> getMerchantMonitorOrderList(Integer status,
			String startTime, String endTime, Integer vipId, String mailNO,
			String buyerName, String buyerPhone, Integer orderBy,
			Pagination pagination, boolean flag);

	/**
	 * 统计大商家角色监控订单信息大小 根据订单状态status为1和0不同查询监控订单信息<br>
	 * 
	 * @param status
	 *            1(成功订单)、2(正在派送)、3(走件中)、4(退货单)
	 * @param startTime
	 *            寄件时间起点
	 * @param endTime
	 *            寄件时间终点
	 * @param vipId
	 *            卖家用户ID
	 * @param mailNO
	 *            运单号
	 * @return @
	 */
	public int countMerchantMonitorOrderList(Integer status, String startTime,
			String endTime, Integer vipId, String mailNO, String buyerName,
			String buyerPhone);

	/**
	 * 卖家、平台用户和管理员按时间查询，先查询VIP用户下的订单信息，该时间段下的所有mailNo信息，再从金刚接口中查询物流信息
	 * 
	 * @param startTime
	 * @param endTime
	 * @param vipId
	 * @param bindedId
	 *            TODO
	 * @return @
	 */
	public List getOrderMailNoListByTime(String startTime, String endTime,
			Integer vipId, List bindedId);

	/**
	 * 卖家、平台用户和管理员按时间查询，先根据时间和买家信心查询VIP用户下的订单信息，该时间段下的所有mailNo信息，再从金刚接口中查询物流信息
	 * 
	 * @param startTime
	 * @param endTime
	 * @param vipId
	 * @param buyerName
	 * @param buyerPhone
	 * @param bindedId
	 *            TODO
	 * @return
	 */
	public List getOrderMailNoListByBuyerInfo(String startTime, String endTime,
			Integer vipId, String buyerName, String buyerPhone, List bindedId);

	public List cleanOrderList(String partitiondate, String startTime,
			String endTime, Integer limit) throws DataAccessException;

	/**
	 * 根据运单号查询订单列表
	 * 
	 * @param mailNo
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> queryOrderListByMailNo(String mailNo)
			throws DataAccessException;

	/**
	 * 根据运单号和当前用户信息查询订单列表
	 * 
	 * @param mailNo
	 * @param currentUserId
	 * @return
	 */
	public List<T> queryOrderListByMailNoAndCustomerid(String mailNo,
			Integer currentUserId);

	/**
	 * 根据运单号查询订单表和交易人信息表。获取部分订单信息和交易人信息。
	 * 
	 * @param mailNo
	 * @return
	 * @throws DataAccessException
	 */
	public T queryOrderTranderInfoByMailNo(String mailNo);

	/**
	 * 多customerId账号：按时间查询，先查询VIP用户下的订单信息，该时间段下的所有mailNo信息，再从金刚接口中查询物流信息
	 * 
	 * @param startTime
	 * @param endTime
	 * @param userCode
	 * @return
	 */
	public List relatedOrderMailNoListByTime(String startTime, String endTime,
			String userCode, Date ucUpdateTime);

	/**
	 * 多customerId账号：按时间查询，先根据时间和买家信心查询VIP用户下的订单信息，该时间段下的所有mailNo信息，
	 * 再从金刚接口中查询物流信息
	 * 
	 * @param startTime
	 * @param endTime
	 * @param userCode
	 * @param buyerName
	 * @param buyerPhone
	 * @return
	 */
	public List relatedOrderMailNoListByBuyerInfo(String startTime,
			String endTime, String userCode, String buyerName,
			String buyerPhone, Date ucUpdateTime);

	/**
	 * 根据mailNo和customerId查询订单信息
	 * 
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public T getOrderbyMailNoAndCustomerId(String mailNo, List customerId)
			throws DataAccessException;

	/**
	 * 通过运单号查找交易人信息
	 * 
	 * @param mailNo
	 * @return
	 * @throws DataAccessException
	 */
	public List getBuyerInfoByMailNo(String mailNo) throws DataAccessException;

	public List<T> getOrderbyMailNoCustomerId(Map map)
			throws DataAccessException;

	public List getOrderbyMailNoCustomerId(List mailNo, List customerId)
			throws DataAccessException;

	public List getOrderbyMailNoCustomerId(String mailNo, String customerId)
			throws DataAccessException;

	/**
	 * 根据custormerIdList查询某天内的订单数目(除‘UNACCEPT’和‘WITHDRAW’状态的订单)
	 * 
	 * @param customerIdList
	 * @param dateString
	 * @return
	 * @throws DataAccessException
	 */
	public int countCurrentDayOrderByCustormerId(List<String> customerIdList,
			String dateString) throws DataAccessException;

	public boolean sendOrders(List<Order> orders);

	// public Map queryOrderPrint(Map map, Pagination<T> pagination);
	public List<DtoOrderPrint> queryOrderPrint(Map map, Pagination<T> pagination);

	public Map queryOrderPrintData(Map map);

	/**
	 * 根据关联账号和运单状态获取某时间段内的运单数量
	 * 
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public int countOrderByStatusAndCustormerId(Integer status,
			String startTime, String endTime, List<String> custormerId)
			throws DataAccessException;

	/**
	 * 获取某一天卖家运单饼状图:map中封装"SIGNED","SENT_SCAN","ONGOING"键值分别代表已签收、正在派送和走件中的运单的数目
	 * 
	 * @param someDay
	 * @param custormerId
	 * @return
	 */
	public Map<String, Number> getPieChart(String someDay,
			List<String> custormerId);

	/**
	 * 分页查找订单信息
	 * 
	 **/
	public List<Order> queryOrderByPage(QueryOrder queryOrder,
			Pagination pagination);

	public boolean toSendOrder(Map map);

	public int countOrders(QueryOrder queryOrder);

	public boolean updateMailNoById(String id, String mailNo);

	/**
	 * 根据分区时间查询订单(查询当前时间的前一天)
	 * 
	 * @param partitiondate
	 * @return
	 */
	public List<OrderWeightUpdateDTO> getOrderByPartitiondate(
			String partitiondate, Integer limit);

	/**
	 * 根据分区时间查询订单(查询当前时间的前一天)
	 * 
	 * @param partitiondate
	 * @return
	 */
	public List<OrderWeightUpdateDTO> getOrderByPartitiondateById(
			String partitiondate, int id, Integer limit);

	/**
	 * 我要发货绑定或更新运单号 （更新order表，还要生产一个task，转发给金刚）
	 * 
	 * @param orderId
	 *            order表id
	 * @param mailNo
	 *            运单号
	 * @param txLogiscticId
	 *            物流号
	 * @param clientId
	 *            平台标示（淘宝和非淘宝的 task内容不一样,要分别处理）
	 * @return
	 */
	public boolean bindedOrUpdateMailNo(Integer orderId, String mailNo,
			String txLogiscticId, String clientId);

	/**
	 * 我要发货 撤销订单（更定order表status为WITHDRAW,生产一个task，转发给金刚）
	 * 
	 * @param orderId
	 *            order表id
	 * @param txLogiscticId
	 *            物流号
	 * @param clientId
	 *            平台标示（淘宝和非淘宝的 task内容不一样,要分别处理）
	 * @return
	 */
	public boolean cancelOrder(Integer orderId, String txLogiscticId,
			String clientId);

	/**
	 * 
	 * @param orderWeightUpdateDTOs
	 */
	public void updateOrderWeight(
			List<OrderWeightUpdateDTO> orderWeightUpdateDTOs);

	/**
	 * 在solr中通过订单list集合查询订单集合
	 * 
	 * @param orderId
	 * @return
	 */
	public List<Order> getOrderEccoreSearchByOrderIds(List<Integer> orderId,
			Pagination pagination, String sortType);

	/**
	 * 修改从order_print表同步过来的运单号和商品价值
	 * 
	 * @param entiry
	 */
	public boolean updateMailNoValue(T entiry);

	/**
	 * 查询一个卖家最近的10条订单
	 * 
	 * @param customerId
	 *            卖家的taobaoEncodeKey
	 * @param limit
	 *            返回结果的数量
	 * @return
	 */
	public List<String> queryTenOrdersByCustomerId(Map<String, Object> params);

	/**
	 * 根据条件查询 每日成功签收比率 [每天成功数/每天发货数]
	 * 
	 * @param String
	 *            userType 用户类型 String theId 卖家customerId或平台用户的ID String
	 *            startDate 查询开始日期 String endDate 查询结束日期
	 * @return
	 */
	public List<SearchReportBean> getSignRatioByPamams(int userType,
			String theId, String startDate, String endDate);

	/**
	 * 根据条件查询 卖家 每日成功签收比率 [同一发货时间,afterDay天内的接受数/同一发货时间的总数]
	 * 
	 * @param String
	 *            theId 卖家customerId或平台用户的ID String startDate 查询开始日期 int
	 *            afterDay 之后多少天的数据
	 * @return
	 */
	public List<SearchReportBean> getSellSignRatioForDayByPamams(String theId,
			String startDate, int afterDay);

	/**
	 * 根据条件查询 平台 每日成功签收比率 [同一发货时间,afterDay天内的接受数/同一发货时间的总数]
	 * 
	 * @param String
	 *            theId 平台用户的ID String startDate 查询开始日期 int afterDay 之后多少天的数据
	 * @return
	 */
	public List<SearchReportBean> getPlatformSignRatioForDayByPamams(
			String theId, String startDate, int afterDay);

	/**
	 * 根据条件查询 卖家 签收完成时间比率
	 * 
	 * @param String
	 *            theId 卖家customerId String startDate 查询开始日期 String endDate
	 *            查询结束日期
	 * @return
	 */
	public List<SearchReportBean> getSellHourRatioByPamams(String theId,
			String startDate, String endDate);

	/**
	 * 根据条件查询 平台 签收完成时间比率
	 * 
	 * @param String
	 *            theId 平台用户的ID String startDate 查询开始日期 String endDate 查询结束日期
	 * @return
	 */
	public List<SearchReportBean> getPlatformHourRatioByPamams(String theId,
			String startDate, String endDate);

	/**
	 * 打印韩都衣舍运单的时候，发送单号
	 * 
	 * @param txLogisticId
	 * @param mailNo
	 * @param clientId
	 * @param type
	 * @return
	 */
	public boolean sendMailNoToEC(String txLogisticId, String mailNo,
			String clientId, String type);

	public void insertImportDb();

	/**
	 * wmd
	 * 
	 * @param list
	 */
	public void updateOrderMailNoByLogisticIdAndClientIdBatch(
			List<UpdateInfo> list);

	/*
    *//**
	 * taobao状态通知
	 * 
	 * @param updateInfo
	 * @return @
	 */
	/*
	 * public boolean orderStatusNodifyByTaobao(List<UpdateWaybillInfo>
	 * updateInfoList);
	 */

	public boolean orderStatusNodifyByPartition(UpdateWaybillInfo updateInfo)
			throws Exception;

	public boolean orderStatusNodifyAndIntoSolr(UpdateWaybillInfo updateInfo)
			throws Exception;

	/**
	 * 根据订单创建报文消息创建订单
	 * 
	 * @param msg
	 * @return
	 */
	public BuildSearch addOrderFormInfoByMsg(String msg, String partitiondate) throws Exception;

	/**
	 * 创建易迅订单
	 * 
	 * @param tabOrder
	 * @return
	 */
	public int addOrderForYixun(TabUniteOrder tabOrder, String cusCode);

	/**
	 * 获取Solar对象
	 */
	public BuildSearchStatusWeightIndex getSolarDto(String txLogisticID);

	/**
	 * 根据物流号LogisticId读取多个订单（订单相关表：product、traderInfo）
	 * 
	 * @param id
	 * @return @
	 */
	public List<T> getListOrderByMailNo(String mailNo);

	/**
	 * 获取重量更新Solar对象
	 * 
	 * @param mailNO
	 * @return
	 */
	public List<BuildSearchStatusWeightIndex> getSolarUpdateWeight(String mailNO);
	
	/**
	 * 获取重量更新Solar对象
	 * 
	 * @param mailNO
	 * @return
	 */
	public BuildSearch getSolarDtoForAdd(Order order);
}
