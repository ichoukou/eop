package net.ytoec.kernel.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dao.JgWaybillDao;
import net.ytoec.kernel.dao.OrderDao;
import net.ytoec.kernel.dao.OrderLogDao;
import net.ytoec.kernel.dao.OrderTempDao;
import net.ytoec.kernel.dao.ProductDao;
import net.ytoec.kernel.dao.TraderInfoDao;
import net.ytoec.kernel.dataobject.JgWaybill;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.OrderFormInfo;
import net.ytoec.kernel.dataobject.OrderTemp;
import net.ytoec.kernel.dataobject.Product;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.service.OrderTempService;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单导入业务接口实现类
 * 
 * @author wuguiqiang
 * @date 2012-6-11
 */

@Service
@Transactional
@SuppressWarnings("all")
public class OrderTempServiceImpl<T extends OrderTemp> implements
		OrderTempService<T>, SessionAware {

	private static Logger logger = LoggerFactory.getLogger(OrderTempServiceImpl.class);

	@Inject
	private OrderTempDao<T> dao;

	@Inject
	private OrderDao<Order> orderDao;

	@Inject
	private TraderInfoDao<TraderInfo> traderDao;

	@Inject
	private ProductDao<Product> productDao;

	@Inject
	private JgWaybillDao<JgWaybill> jgWaybillDao;
	
	@Inject
	private OrderLogDao orderLogDao;

	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean addOrderTemp(T entity) {
		if (entity == null) {
			logger.error(OrderTemp.class.getName() + "对象参数信息为空!");
			return false;
		}

		return dao.addOrderTemp(entity);
	}

	@Override
	public T getOrderTempById(Integer id) {
		return dao.getOrderTempById(id);
	}

	@Override
	public List<T> getAllOrderTemp() {
		// TODO Auto-generated method stub
		return dao.getAllOrderTemp();
	}

	@Override
	public boolean delOrderTemp(T entity) {
		boolean result = false;
		if (entity == null) {
			logger.error(Order.class.getName() + "对象参数信息为空!");
			return false;
		}

		result = dao.delOrderTemp(entity);
		return result;
	}

	@Override
	public List<T> getOrderTempByMailNoRemark(String mailNo, String remark) {
		if (mailNo == null || remark == null)
			return null;
		if (mailNo.trim().equals("") || remark.trim().equals(""))
			return null;
		OrderTemp orderTemp = new OrderTemp();
		orderTemp.setMailNo(mailNo);
		orderTemp.setRemark(remark);
		return dao.getOrderTempByMailNoRemark((T) orderTemp);
	}

	@Override
	public List<T> getOrderTempListByRemark(String remark) {
		return dao.getOrderTempListByRemark(remark);
	}

	public List<T> getOrderTempListByRemarkPage(String remark,
			Pagination pagination, String mailNoOrTraderNo) {
		Map map = new HashMap();
		map.put("remark", remark);
		if (!StringUtils.isEmpty(mailNoOrTraderNo)) {
			map.put("mailNoOrTraderNo", mailNoOrTraderNo);
		}
//		pagination.setTotalRecords(dao.countOrderTempListByMap(map));
		if (pagination.getPageNum() != null
				&& pagination.getStartIndex() != null) {
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
		}

		return dao.getOrderTempListByRemarkPage(map);
	}

	@Override
	public List<T> getOrderTempLogisticIdRemark(String txLogisticId,
			String remark) {
		if (txLogisticId == null || remark == null)
			return null;
		if (txLogisticId.trim().equals("") || remark.trim().equals(""))
			return null;
		OrderTemp orderTemp = new OrderTemp();
		orderTemp.setTxLogisticId(txLogisticId);
		orderTemp.setRemark(remark);
		return dao.getOrderTempLogisticIdRemark((T) orderTemp);
	}

	@Override
	public boolean addOrderFormInfo(OrderFormInfo orderFormInfo)
			throws DuplicateKeyException {
		if (orderFormInfo == null) {
			logger.error(OrderTemp.class.getName() + "对象参数信息为空!");
			return false;
		}

		try {
			Order order = new Order();

			order.setClientId(orderFormInfo.getClientId());
			order.setCustomerId(orderFormInfo.getCustomerId());
			order.setMailNo(orderFormInfo.getMailNo());
			order.setLogisticProviderId(orderFormInfo.getLogisticProviderId());
			order.setTxLogisticId(orderFormInfo.getTxLogisticId());
			order.setVipId(orderFormInfo.getVipId());
			order.setTradeNo(orderFormInfo.getTradeNo());
			order.setFlag(orderFormInfo.getFlag());
			order.setRemark(orderFormInfo.getRemark());
			order.setStatus(Order.CREATE_ORDER_TYPE);
			order.setVersion(orderFormInfo.getVersion());
			order.setLineType(orderFormInfo.getLineType());
			order.setPartitionDate(new Date());
			TraderInfo sender = orderFormInfo.getSender();
			sender.setTradeType(TraderInfo.SENDE_TYPE);
			TraderInfo receiver = orderFormInfo.getReceiver();
			receiver.setTradeType(TraderInfo.RECEIVE_TYPE);
			List<Product> products = orderFormInfo.getItems();
			
			orderLogDao.addOrderLog(orderFormInfo.getTxLogisticId());
			this.orderDao.addOrder(order);

			//order.setTxLogisticId("IMP" + order.getId());
			//this.orderDao.updateOrder(order);

			JgWaybill jgWaybill = new JgWaybill();

			jgWaybill.setServiceType(orderFormInfo.getServiceType());
			jgWaybill.setbAddress(orderFormInfo.getReceiver().getAddress());
			jgWaybill.setbCity(orderFormInfo.getReceiver().getCity());
			jgWaybill.setbDistrict(orderFormInfo.getReceiver().getDistrict());
			jgWaybill.setbProv(orderFormInfo.getReceiver().getProv());
			jgWaybill.setbMobile(orderFormInfo.getReceiver().getMobile());
			jgWaybill.setbName(orderFormInfo.getReceiver().getName());
			jgWaybill.setbPhone(orderFormInfo.getReceiver().getPhone());
			jgWaybill.setbPostCode(orderFormInfo.getReceiver().getPostCode());

			jgWaybill.setMailNo(orderFormInfo.getMailNo());
			jgWaybill.setOrderId(order.getId());

			jgWaybill.setsProv(orderFormInfo.getSender().getProv());
			jgWaybill.setsCity(orderFormInfo.getSender().getCity());
			jgWaybill.setsDistrict(orderFormInfo.getSender().getDistrict());
			jgWaybill.setsAddress(orderFormInfo.getSender().getAddress());
			jgWaybill.setsMobile(orderFormInfo.getSender().getMobile());
			jgWaybill.setsName(orderFormInfo.getSender().getName());
			jgWaybill.setsPhone(orderFormInfo.getSender().getPhone());
			jgWaybill.setsPostCode(orderFormInfo.getSender().getPostCode());

			jgWaybill.setLogisticId(order.getTxLogisticId());
			jgWaybill.setLineType("1");
			jgWaybill.setClientID("Taobao");
			this.jgWaybillDao.addJgWaybill(jgWaybill);
			
			sender.setOrderId(order.getId());
			sender.setPartitionDate(new Date());
			this.traderDao.addTraderInfo(sender);
			receiver.setPartitionDate(new Date());
			receiver.setOrderId(order.getId());
			this.traderDao.addTraderInfo(receiver);

			for (Product product : products) {
				product.setLogisticId(order.getTxLogisticId());
				product.setOrderId(order.getId()); // 所对应的订单编号
				this.productDao.addProduct(product);// 插入产品表信息
			}
		} catch (DuplicateKeyException ex) {
			throw ex;
		} catch (Exception e) {
			logger.error("order create failed", e);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public int countOrderTempListByMap(String remark) {
		Map map = new HashMap();
		map.put("remark", remark);
		return dao.countOrderTempListByMap(map);
	}

	@Override
	public boolean addOrderTempList(List<T> list) {
		if (null == list || list.size() == 0) {
			logger.error("上传数据为空！");
		}
		return dao.addOrderTempList(list);
	}
}
