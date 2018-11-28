package net.ytoec.kernel.dao.impl;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.remote.xml.UpdateInfo;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.OrderDao;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.SearchReportBean;
import net.ytoec.kernel.dto.OrderWeightUpdateDTO;
import net.ytoec.kernel.mapper.OrderMapper;
import net.ytoec.kernel.search.dto.MailObjectDTO;
import net.ytoec.kernel.util.DateUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

/**
 * @author ChenRen
 * @date 2011-7-20
 */
@Repository
@SuppressWarnings("all")
public class OrderDaoImpl<T extends Order> implements OrderDao<T> {

	private Logger logger = LoggerFactory.getLogger(OrderDaoImpl.class);
	@Inject
	private OrderMapper<T> mapper;

	@Override
	public boolean addOrder(T entity)throws DataAccessException  {
		boolean flag = false;

		try {
			mapper.add(entity);
			flag = true;
		} catch (DataAccessException e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			throw e;
		}

		return flag;
	}

	@Override
	public T getOrderById(Integer id) {
		Order order = new Order();
		order.setId(id);
		T entity = null;

		try {
			entity = mapper.get((T) order);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return entity;
	}

	@Override
	public List<T> getAllOrder() {
		List<T> list = null;

		try {
			list = mapper.getAllOrder();
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public boolean updateOrder(T entity) {
		boolean flag = false;

		try {
			mapper.edit(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}

		return flag;
	}

	@Override
	public boolean delOrder(T entity) {
		boolean flag = false;

		try {
			mapper.remove(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}

		return flag;
	}

	@Override
	public T getOrderByLogisticId(String logisticId) {
		Order order = new Order();
		order.setTxLogisticId(logisticId);
		T entity = null;
		try {
			entity = (T) mapper.getOrderByLogisticId(logisticId);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			logger.error("查询错误的物流号为："+logisticId);
		}

		return entity;
	}

	@Override
	public T getOrderbyMailNo(String mailNo) {
		// Order order = new Order();
		// order.setMailNo(mailNo);
		T entity = null;
		try {
			entity = (T) mapper.getOrderbyMailNo(mailNo);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return entity;
	}

	@Override
	public T getOrderBytradeNo(String tradeNo) {
		Order order = new Order();
		order.setTradeNo(tradeNo);
		T entity = null;
		try {
			entity = (T) mapper.getOrderByTradeNo(tradeNo);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return entity;
	}

	@Override
	public boolean updateOrderStatus(String orderStatus) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateOrderWeightByMailNo(String mailNo, float weight,Date starttime,Date endtime) {
		int rs;
		Map map = new HashMap();
		map.put("mailNo", mailNo);
		map.put("weight", weight);
		map.put("starttime", starttime);
		map.put("endtime", endtime);
		try {
			rs = mapper.updateOrderWeightByMailNo(map);
		} catch (Exception e) {
			logger.error("weightUpdate db error!"
					+ LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			return false;
		}
		if (rs > 0)
			return true;
		else
			return false;

	}

	@Override
	public boolean updateOrderWeightByTradeNo(String tradeNo, float weight) {
		Map map = new HashMap();
		map.put("tradeNo", tradeNo);
		map.put("weight", weight);
		try {
			mapper.updateOrderWeightByTradeNo(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return true;
	}

	@Override
	public boolean updateOrderByMailNo(Map map) {
		int rs;
		try {
			rs = mapper.updateOrderByMailNo(map);
		} catch (Exception e) {
			logger.error("weightUpdate db error!"
					+ LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			return false;
		}
		if (rs > 0)
			return true;
		else
			return false;

	}

	@Override
	public List<T> getOrderListByVipId(int vipId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delOrderByLogisticId(int LogisticId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<T> ecAccount(Map map) {
		List<T> list = null;

		try {
			list = mapper.ecAccount(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return list;
	}

	@Override
	public List<T> ecAccountHalf(Map map) {
		List<T> list = null;

		try {
			list = mapper.ecAccountHalf(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			return Collections.EMPTY_LIST;
		}

		return list;
	}

	@Override
	public List<T> ecAccountByPosttemp(Map map) {
		List<T> list = null;

		try {
			list = mapper.ecAccountByPosttemp(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			return Collections.EMPTY_LIST;
		}

		return list;
	}

	@Override
	public List<T> getOrderByMailNo(String mailNo) {
		List<T> list = null;

		try {
			list = mapper.getOrderByMailNo(mailNo);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			return Collections.EMPTY_LIST;
		}

		return list;
	}

	@Override
	public List<T> getOrdersByWeight(String startTime, String endTime,
			int start, int limit) {
		List<T> list = null;
		Map map = new HashMap();
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("start", start);
		map.put("limit", limit);
		try {
			list = mapper.getOrdersByWeight(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	public List<String> getMailNoByPartitionDate(String startTime,
			String endTime, String customerId) {
		List<String> list = null;
		Map map = new HashMap();
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("customerId", customerId);
		try {
			list = mapper.getMailNoByPartitionDate(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public int countOrdersByWeight(String startTime, String endTime) {
		Map map = new HashMap();
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		try {
			return mapper.countOrdersByWeight(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return 0;
	}

	@Override
	public boolean updateOrderMailNoByLogisticIdAndClientId(Map map) {
		boolean flag = false;

		try {
			mapper.updateOrderMailNoByLogisticIdAndClientId(map);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return flag;
	}
	
	@Override
	public boolean updateOrderMailNoByLogisticIdAndPartition(Map map) throws Exception{
		boolean flag = false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -10);
		String partitiondate=DateUtil.format(cal.getTime(), "yyyy-MM-dd");
		map.put("partitiondate", partitiondate);
		logger.info("partitiondate==="+partitiondate);
		try {
			flag = mapper.updateOrderMailNoByLogisticIdAndPartition(map) > 0 ? true : false;
		
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			throw e;
		}

		return flag;
	}

	@Override
	public boolean updateMailNoAndWeightByLogisticId(Map map) {
		boolean flag = false;
		try {
			mapper.updateMailNoAndWeightByLogisticId(map);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public List<T> getOrderMonitorList(Map map) {
		List<T> list = null;
		try {
			list = mapper.getOrderMonitorList(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public int countOrderMonitorList(Map map) {
		int count = 0;
		try {
			count = mapper.countOrderMonitorList(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return count;
	}

	@Override
	public List<T> getMerchantMonitorOrderList(Map map) {
		List<T> list = null;
		try {
			list = mapper.getMerchantMonitorOrderList(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public int countMerchantMonitorOrderList(Map map) {
		int count = 0;
		try {
			count = mapper.countMerchantMonitorOrderList(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return count;
	}

	@Override
	public List getOrderMailNoListByTime(Map map) {
		List list = null;
		try {
			list = mapper.getOrderMailNoListByTime(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public List cleanOrderList(Map map) throws DataAccessException {
		List list = null;
		try {
			list = mapper.cleanOrderList(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public List<T> getAddrByOrderId(Map map) {
		List list = null;
		try {
			list = mapper.getAddrByOrderId(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			return Collections.EMPTY_LIST;
		}
		return list;
	}

	@Override
	public List<T> queryOrderListByMailNo(String mailNo)
			throws DataAccessException {
		List<T> list = null;
		try {
			list = mapper.queryOrderListByMailNo(mailNo);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public List<T> queryOrderListByMailNoAndCustomerid(Map map) {
		List<T> list = null;
		try {
			list = mapper.queryOrderListByMailNoAndCustomerid(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public List<T> queryOrderTranderInfoByMailNo(String mailNo) {
		List<T> list = null;
		try {
			list = mapper.queryOrderTranderInfoByMailNo(mailNo);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public List getOrderMailNoListByBuyerInfo(Map map)
			throws DataAccessException {
		List list = null;
		try {
			list = mapper.getOrderMailNoListByBuyerInfo(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public List relatedOrderMailNoListByTime(Map map)
			throws DataAccessException {
		List list = null;
		try {
			list = mapper.relatedOrderMailNoListByTime(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public List relatedOrderMailNoListByBuyerInfo(Map map)
			throws DataAccessException {
		List list = null;
		try {
			list = mapper.relatedOrderMailNoListByBuyerInfo(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public List<T> getOrderbyMailNoAndCustomerId(Map map)
			throws DataAccessException {
		List<T> list = null;
		try {
			list = mapper.getOrderbyMailNoAndCustomerId(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public List getBuyerInfoByMailNo(String mailNo) throws DataAccessException {
		List<T> list = null;
		try {
			list = mapper.getBuyerInfoByMailNo(mailNo);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public List<T> getOrderbyMailNoCustomerId(Map map)
			throws DataAccessException {
		List<T> list = null;
		try {
			list = mapper.getOrderbyMailNoCustomerId(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public List<T> queryOrderPrint(Map map) {
		List<T> list = null;

		try {
			list = mapper.queryOrderPrint(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			return Collections.EMPTY_LIST;
		}

		return list;
	}

	@Override
	public List<T> queryOrderList(Map map) {
		List<T> list = null;

		try {
			list = mapper.queryOrderList(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			return Collections.EMPTY_LIST;
		}

		return list;
	}

	@Override
	public List<MailObjectDTO> bulidEccoreData(Map map) {
		if (map == null || map.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		return this.mapper.bulidEccoreData(map);
	}

	@Override
	public int countCurrentDayOrderByCustormerId(Map map)
			throws DataAccessException {
		int count = 0;
		try {
			count = mapper.countCurrentDayOrderByCustormerId(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return count;
	}

	@Override
	public int countOrderByStatusAndCustormerId(Map map)
			throws DataAccessException {
		int count = 0;
		try {
			count = mapper.countOrderByStatusAndCustormerId(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return count;
	}

	@Override
	public int countOrders(Map map) {
		int count = 0;
		try {
			count = mapper.countOrdersByPages(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return count;
	}

	@Override
	public boolean updateMailNoById(Map map) {
		return (mapper.updateMailNoById(map) > 0) ? true : false;
	}

	@Override
	public List<MailObjectDTO> bulidPartEccoreData(Integer limit) {
		if (limit == null) {
			return Collections.EMPTY_LIST;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limit", limit);

		return mapper.bulidPartEccoreData(map);
	}
	
	@Override
	public List<MailObjectDTO> bulidPartEccoreDataOther(Integer limit) {
		if (limit == null) {
			return Collections.EMPTY_LIST;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limit", limit);

		return mapper.bulidPartEccoreDataOther(map);
	}
	@Override
	public MailObjectDTO bulidPartEccoreDataByOrderId(int id,String partitiondate){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("partitiondate", partitiondate);
		MailObjectDTO traderinfo=new MailObjectDTO();
		try{
			traderinfo=mapper.bulidPartEccoreDataByOrderId(map);
			if(traderinfo==null){
				traderinfo=new MailObjectDTO();
			}
		}catch(Exception e){
			logger.error("查询traderinfo表出现异常！"+e.getMessage());
		}
		return traderinfo;
	}
	@Override
	public List<OrderWeightUpdateDTO> getOrderByPartitiondate(
			String partitiondate, Integer limit) {
		// TODO Auto-generated method stub

		List<OrderWeightUpdateDTO> list = null;
		if (limit == null || StringUtils.isEmpty(partitiondate)) {
			return Collections.EMPTY_LIST;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("partitiondate", partitiondate);
		map.put("limit", limit);

		try {
			list = mapper.getOrderByPartitiondate(map);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("根据partitiondate查询order出错");
		}

		return list;
	}

	@Override
	public List<OrderWeightUpdateDTO> getOrderByPartitiondateById(
			String partitiondate, Integer id, Integer limit) {
		// TODO Auto-generated method stub

		if (limit == null || StringUtils.isEmpty(partitiondate) || id == null) {
			return Collections.EMPTY_LIST;
		}
		List<OrderWeightUpdateDTO> list = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("partitiondate", partitiondate);
		map.put("id", id);
		map.put("limit", limit);
		try {
			list = mapper.getOrderByPartitiondateById(map);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("根据partitiondate和id查询order出错");
		}
		return list;
	}

	@Override
	public boolean cancelOrder(Map map) {
		boolean result = false;
		try {
			mapper.cancelOrder(map);
			result = true;
		} catch (Exception e) {
			logger.error("撤销运单发生错误！", e);
		}

		return result;
	}

	@Override
	public void updateWeightByIdAndPartitiondate(Map map) {

		try {
			mapper.updateMailNoAndWeightByLogisticId(map);
		} catch (Exception e) {
			logger.error("更新重量失败", e);
		}
	}

	@Override
	public void batchUpdateWeight(
			List<OrderWeightUpdateDTO> orderWeightUpdateDTOs) {
		try {

			Integer result = 0;
			OrderWeightUpdateDTO dto = new OrderWeightUpdateDTO();
			for (int i = 0; i < orderWeightUpdateDTOs.size(); i++) {
				dto = orderWeightUpdateDTOs.get(i);
				if (StringUtils.isEmpty(dto.getMailNo())
						|| dto.getWeight() == null || dto.getWeight() == 0) {
					continue;
				}
				result = mapper.updateWeightByIdAndPartitiondate(dto);

			}
		} catch (Exception e) {
			logger.error("更新重量失败", e);
		}

	}

	@Override
	public List<MailObjectDTO> bulidPartEccoreStatusData(Integer limit, String currentDate, String endDate) {
		if (limit == null) {
			return Collections.EMPTY_LIST;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limit", limit);
		map.put("currentDate", currentDate);
		map.put("endDate", endDate);

		return mapper.bulidPartEccoreStatusData(map);
	}

	@Override
	public void updateTimeById(T entiry) {
		if (entiry == null || entiry.getId() == null) {
			return;
		}
		mapper.updateTimeById(entiry);
	}

	@Override
	public void updateMailNoValue(T entiry) {
		if (entiry == null || entiry.getId() == null) {
			return;
		}
		mapper.updateMailNoValue(entiry);
	}

	@Override
	public void updateBuyerNickToOrder(Map<String, Object> params) {
		try {
			mapper.updateBuyerNickToOrder(params);
		} catch(Exception e) {
			logger.error("更新淘宝号到订单表中出错", e);
		}
	}

    @Override
    public Integer updateOrderByIds(Map<String, Object> map) {
        return mapper.updateOrderByIds(map);
    }

    @Override
    public List<String> queryTenOrdersByCustomerId(Map<String, Object> params) {
        // TODO Auto-generated method stub
        if(params != null){
            if(params.containsKey("customerId") && params.containsKey("limit"))
            return mapper.queryTenOrdersByCustomerId(params);
        }
        return null;
    }
    
    public List<SearchReportBean> getSignRatioByPamams(Map<String, Object> map) {
    	return mapper.getSignRatioByPamams(map);
    }

    @Override
	public int getSellSignRatioForDayByPamams(Map<String, Object> map) {
		return mapper.getSellSignRatioForDayByPamams(map);
	}

    @Override
	public int getPlatformSignRatioForDayByPamams(Map<String, Object> map) {
		return mapper.getSellSignRatioForDayByPamams(map);
	}
    
	public List<SearchReportBean> getSellHourRatioByPamams(Map<String, Object> map) {
		try {
			return mapper.getSellHourRatioByPamams(map);
		} catch(Exception e) {
			logger.error("时间比例查询出错", e);
		}
		return null;
    }
	
	public List<SearchReportBean> getPlatformHourRatioByPamams(Map<String, Object> map) {
		try {
			return mapper.getPlatformHourRatioByPamams(map);
		} catch(Exception e) {
			logger.error("时间比例查询出错", e);
		}
		return null;
    }

	@Override
	public void updaetBatchOrderByTxlogisc(List<UpdateInfo> list) {
		// TODO Auto-generated method stub
		for(int i =0;i<list.size();i++){
			try {
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("logisticId", list.get(i).getTxLogisticID());
				map.put("clientId", list.get(i).getClientId());
				map.put("remark", list.get(i).getRemark());
				map.put("mailNo", list.get(i).getMailNo());
				map.put("status", list.get(i).getStatus());
				map.put("acceptTime", list.get(i).getAcceptTime());
				map.put("type", list.get(i).getType());
				mapper.updateOrderByTxlogisc(map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
