/**
 * MonitorService.java
 * Wangyong
 * 2011-8-9 上午09:48:11
 */
package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;

/**
 * 运单监控业务接口
 * @author Wangyong
 * @2011-8-9
 * net.ytoec.kernel.service
 */
public interface MonitorService<T> {

	/**
	 * 卖家运单监控下，查询卖家的订单表监控信息。 根据搜索条件查询:访问dao层时，将查询参数封装进map对象里，map键对应以下参数名。
	 * @param customerId 当前用户customerId
	 * @param status
	 *            1(成功订单)、2(正在派送)、3(走件中)、5(接单中)
	 * @param startTime
	 *            寄件时间起点
	 * @param endTime
	 *            寄件时间终点
	 * @param mailNO
	 *            运单号
	 * @param orderBy
	 *            排序方式
	 * @param pagination
	 *            分页信息
	 * @param flag
	 *            是否分页
	 * @param bindedId TODO
	 * @return @
	 */
	public List<T> getOrderMonitor(String customerId, Integer status, String startTime,
			String endTime, Integer numProv, Integer numCity, Integer numDistrict, String mailNO, String buyerName, String buyerPhone, Integer orderBy,
			Pagination pagination, boolean flag, List bindedId,String mailNoOrderType);
	
	/**
	 * 卖家运单监控下，查询大商家的订单表监控信息。 根据搜索条件查询:访问dao层时，将查询参数封装进map对象里，map键对应以下参数名。
	 * @param customerId 当前用户customerId
	 * @param status
	 *            1(成功订单)、2(正在派送)、3(走件中)、5(接单中)
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
	public List<T> getMerchantMonitorOrderList(String customerId, Integer status,
			String startTime, String endTime, Integer numProv, Integer numCity, Integer numDistrict, Integer vipId, String mailNO, String buyerName, String buyerPhone,
			Integer orderBy, Pagination pagination, boolean flag,String mailNoOrderType);
	
    /**
     * 统计运单监控各个tab列表的总数
     * @param isAll ：true表示统计平台用户查询所有卖家是的条件
     * @param curUserId 当前用户Id，在平台用户统计所有卖家信息时要用到
     * @param status 1(成功订单)、2(正在派送)、3(走件中)4(退货单)、5(接单中)
     * @param startTime
     * @param endTime
     * @param mailNO
     * @param buyerName
     * @param buyerPhone
     * @param bindedId
     * @param numProv
     * @param numCity
     * @param numDistrict
     * @param pagination
     * @return
     */
    public int countOrderMonitorList(boolean isAll, Integer curUserId, Integer status, String startTime, String endTime, String mailNO, String buyerName, String buyerPhone, List bindedId, Integer numProv, Integer numCity, Integer numDistrict, Pagination pagination,String mailNoOrderTyp);
    /**
     * 统计运单监控各个tab列表的总数
     * @param isAll ：true表示统计平台用户查询所有卖家是的条件
     * @param curUserId 当前用户Id，在平台用户统计所有卖家信息时要用到
     
     * @param startTime
     * @param endTime
     * @param mailNO
     * @param buyerName
     * @param buyerPhone
     * @param bindedId
     * @param numProv
     * @param numCity
     * @param numDistrict
     * @param pagination
     * @return
     */
    public Map<String, Integer> countOrderMonitorListNew(boolean isAll, Integer curUserId, String startTime, String endTime, String mailNO, String buyerName, String buyerPhone, List bindedId, Integer numProv, Integer numCity, Integer numDistrict, Pagination pagination,String mailNoOrderTyp);
    
    /**
     * 网点运单监控查询方法
     * @param tabFlag tab标示：1(订单)、2(关注运单)
     * @param status 1(成功订单)、2(正在派送)、3(走件中) 5(接单中)
     * @param userId 当前网点用户id
     * @param startTime
     * @param endTime
     * @param mailNO
     * @param buyerName
     * @param buyerPhone
     * @param orderBy 排序方式：1(升序).2(降序)
     * @param pagination
     * @param bindedId 网点所选客户的customerId集合
     * @param numProv
     * @param numCity
     * @param numDistrict
     * @param mailNoOrderType 订单类型：分为常规订单和退货订单
     * @return
     */
    public List<T> getSiteMonitorList(Integer tabFlag, Integer status, Integer userId, String startTime, String endTime, String mailNO, String buyerName, String buyerPhone, Integer orderBy, Pagination pagination, List<String> bindedId, Integer numProv, Integer numCity, Integer numDistrict,String mailNoOrderType);
    
    /**
     * 网点运单监控我的关注统计方法
     * @param userId 当前网点用户id
     * @param startTime
     * @param endTime
     * @param mailNO
     * @param buyerName
     * @param buyerPhone
     * @param numProv
     * @param numCity
     * @param numDistrict
     * @return
     */
    public int countSiteMonitorAttentionList(Integer userId, String startTime, String endTime, String mailNO, String buyerName, String buyerPhone, Integer numProv, Integer numCity, Integer numDistrict);
    
    /**
     * 卖家运单监控查询方法
     * @param tabFlag tab标示：1(订单)、2(关注运单)
     * @param status 1(成功订单)、2(正在派送)、3(走件中) 5(接单中)
     * @param customerId 当前卖家customerId
     * @param startTime
     * @param endTime
     * @param mailNO
     * @param buyerName
     * @param buyerPhone
     * @param orderBy 排序方式：1(升序).2(降序)
     * @param pagination
     * @param bindedId 卖家关联店铺customerId集合
     * @param numProv
     * @param numCity
     * @param numDistrict
     * @param mailNoOrderType 订单类型：分为常规订单和退货订单
     * @return
     */
    public List<T> getSellerMonitorList(Integer tabFlag, Integer status, String customerId, String startTime, String endTime, String mailNO, String buyerName, String buyerPhone, Integer orderBy, Pagination pagination, List<String> bindedId, Integer numProv, Integer numCity, Integer numDistrict,String mailNoOrderType);
    
    /**
     * 卖家运单监控关注订单统计方法
     * @param startTime
     * @param endTime
     * @param mailNO
     * @param buyerName
     * @param buyerPhone
     * @param numProv
     * @param numCity
     * @param numDistrict
     * @param bindedId
     * @return
     */
    public int countSellerMonitorAttentionList(String startTime, String endTime, String mailNO, String buyerName, String buyerPhone, Integer numProv, Integer numCity, Integer numDistrict, List<String> bindedId);
    
    /**
     * 平台用户运单监控查询方法:平台用户没有我的关注
     * @param isAll 是否是查询所有业务账号
     * @param tabFlag tab标示：1(订单)、2(关注运单)
     * @param status 1(成功订单)、2(正在派送)、3(走件中) 5(接单中)
     * @param userId 当前平台用户id或者其所选业务账号的用户id
     * @param startTime
     * @param endTime
     * @param mailNO
     * @param buyerName
     * @param buyerPhone
     * @param orderBy 排序方式：1(升序).2(降序)
     * @param pagination
     * @param numProv
     * @param numCity
     * @param numDistrict
     * @param mailNoOrderType 订单类型：分为常规订单和退货订单
     * @return
     */
    public List<T> getPlatMonitorList(boolean isAll, Integer tabFlag, Integer status, Integer userId, String startTime, String endTime, String mailNO, String buyerName, String buyerPhone, Integer orderBy, Pagination pagination, Integer numProv, Integer numCity, Integer numDistrict,String mailNoOrderType);
    
    
}
