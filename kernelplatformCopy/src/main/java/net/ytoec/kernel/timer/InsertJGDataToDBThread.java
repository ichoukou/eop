package net.ytoec.kernel.timer;

import java.util.concurrent.LinkedBlockingQueue;

import net.ytoec.kernel.action.remote.process.OrderProcessor;
import net.ytoec.kernel.action.remote.xml.UpdateWaybillInfo;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.JgWaybillUpdate;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.OrderFormInfo;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.dto.JGOrderDTO;
import net.ytoec.kernel.service.JgWaybillUpdateService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.util.DateUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;

public class InsertJGDataToDBThread extends Thread {

	private static Logger logger = LoggerFactory
			.getLogger(InsertJGDataToDBThread.class);
	private OrderService<Order> orderService;
	
	OrderProcessor orderProcessor = new OrderProcessor();
	
	private static String partitiondateFormat = "yyyy-MM-dd";
	
	private JgWaybillUpdateService<JgWaybillUpdate> jgWaybillUpdateService;
	
	private boolean stop = false;
	
	private int threadNum=5;
	
	LinkedBlockingQueue<JGOrderDTO> jgQueue = new LinkedBlockingQueue<JGOrderDTO>();

	public void put(JGOrderDTO bean) {
		try {
			jgQueue.put(bean);
		} catch (InterruptedException e) {
			logger.error("从jgQueue取值错误", e);
			e.printStackTrace();
		}
	}
	
	public void startRun(){
		InsertJGDataToDBThread[] threads = new InsertJGDataToDBThread[threadNum];
		for(int i = 0; i < threadNum; i++ ){
			 
			threads[i] = new InsertJGDataToDBThread();
			threads[i].setName("订单入库线程创建"+i);
			threads[i].start();
			logger.error("订单入库线程创建:"+threads[i].getName());
    }
	}

	@Override
	public void run() {
		try {
			while(!stop){
				JGOrderDTO jGOrderDTO = jgQueue.take();
				Integer orderStatus = jGOrderDTO.getCommandType();
				if (orderStatus == 10) {
					// 订单创建
					orderCreate(jGOrderDTO);
				} else if (orderStatus == 30) {
					// 面单绑定
					orderUpdate(jGOrderDTO);
				} else if (orderStatus == 40) {
					// 订单取消
					orderCancel(jGOrderDTO);
				}
			}

		} catch (Exception e) {
			logger.error("SendOrderCreateThread error", e);
		} finally {

		}
	}
	
	/**
	 * description 订单创建 created by hufei@2013-04-22
	 */
	private boolean orderCreate(JGOrderDTO jgOrderDTO) throws Exception {
		boolean flag = false;
		OrderFormInfo requestOrder = orderProcessor.createToObject(jgOrderDTO);
		TraderInfo traderInfo = requestOrder.getSender();
		TraderInfo receiver = requestOrder.getReceiver();
		boolean isPrint = Resource.getIsPrint(requestOrder.getClientId());// 判断是否面单打印
		if (StringUtils.isEmpty(requestOrder.getLogisticProviderId())
				|| StringUtils.isEmpty(requestOrder.getTxLogisticId())
				|| StringUtils.isEmpty(traderInfo.getName())
				|| StringUtils.isEmpty(traderInfo.getAddress())
				|| StringUtils.isEmpty(traderInfo.getPostCode())
				|| (StringUtils.isEmpty(traderInfo.getMobile()) && StringUtils
						.isEmpty(traderInfo.getPhone()))
				|| StringUtils.isEmpty(traderInfo.getCity())
				|| StringUtils.isEmpty(traderInfo.getProv())
				|| (StringUtils.isEmpty(receiver.getMobile()) && StringUtils
						.isEmpty(receiver.getPhone()))
				|| (jgOrderDTO.getIsOffline() == 1
						&& StringUtils.isEmpty(requestOrder.getMailNo()) && true != isPrint)) {
			logger.error("创建失败,错误原因为S05");
			return flag;
		}
		// 线上下单
		// 通过serviceType来判
		if (jgOrderDTO.getIsOffline() == 0)
			requestOrder.setLineType(Order.ONLINE_TYPE);
		else if (jgOrderDTO.getIsOffline() == 1)
			requestOrder.setLineType(Order.OFFLINE_TYPE);
		else
			requestOrder.setLineType(String.valueOf(jgOrderDTO.getIsOffline()));
		if (jgOrderDTO.getOrderChannelCode().equals("TAOBAO_STD")) {
			requestOrder.setClientId("Taobao");
		}
		try {
			requestOrder.setStatus("2");
			requestOrder.setPartitiondate(DateUtil.valueof(
					jgOrderDTO.getCreateTime(), partitiondateFormat));
			this.orderService.addOrderFormInfo(requestOrder);
			flag = true;
			logger.info("orderCreate" + "订单创建成功");
		} catch (DuplicateKeyException de) {
			logger.error("物流号不能重复！物流号：" + requestOrder.getTxLogisticId(), de);
			flag = false;
		} catch (Exception e) {
			logger.error(jgOrderDTO.getOrderLogisticsCode() + "订单创建失败", e);
			flag = false;
		}
		return flag;
	}
	
	/**
	 * description 订单取消 created by hufei@2013-04-22
	 */
	private boolean orderCancel(JGOrderDTO jgOrderDTO) throws Exception {
		boolean flag = false;
		UpdateWaybillInfo updateInfo = orderProcessor
				.cancelOrWayMergerToObject(jgOrderDTO, 1); // 订单取消和面单绑定json解析
		logger.debug("订单取消");
		if (jgOrderDTO.getOrderChannelCode().equals("TAOBAO_STD")) {
			updateInfo.setClientId("Taobao");
		}
		try {
			orderService.updateOrderMailNoByLogisticIdAndClientId(
					updateInfo.getTxLogisticId(), updateInfo.getClientId(),
					updateInfo.getMailNo(), updateInfo.getInfoContent());
			flag = true;
			logger.error(jgOrderDTO.getOrderLogisticsCode() + "订单取消修改成功");
		} catch (Exception e) {
			logger.error("订单取消失败", e);
			flag = false;
		}
		return flag;
	}

	/**
	 * description 面单绑定 created by hufei@2013-04-22
	 */
	private boolean orderUpdate(JGOrderDTO jgOrderDTO) {
		boolean flag = false;
		UpdateWaybillInfo updateInfo = orderProcessor
				.cancelOrWayMergerToObject(jgOrderDTO, 2);
		logger.debug("面单绑定");
		if (jgOrderDTO.getOrderChannelCode().equals("TAOBAO_STD")) {
			updateInfo.setClientId("Taobao");
		}
		try {
			orderService.updateOrderMailNoByLogisticIdAndClientId(
					updateInfo.getTxLogisticId(), updateInfo.getClientId(),
					updateInfo.getMailNo(), updateInfo.getInfoContent());
			flag = true;
			logger.error(jgOrderDTO.getOrderLogisticsCode() + "面单绑定修改成功");
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		if (jgOrderDTO.getIsOffline() == 1) {
			JgWaybillUpdate jgWaybillUpdate = new JgWaybillUpdate();
			jgWaybillUpdate.setLogisticId(updateInfo.getTxLogisticId());
			jgWaybillUpdate.setMailNo(updateInfo.getMailNo());
			jgWaybillUpdate.setClientID(updateInfo.getClientId());
			jgWaybillUpdateService.addJgWaybillUpdate(jgWaybillUpdate);
		}
		return flag;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public OrderService<Order> getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService<Order> orderService) {
		this.orderService = orderService;
	}

	public JgWaybillUpdateService<JgWaybillUpdate> getJgWaybillUpdateService() {
		return jgWaybillUpdateService;
	}

	public void setJgWaybillUpdateService(
			JgWaybillUpdateService<JgWaybillUpdate> jgWaybillUpdateService) {
		this.jgWaybillUpdateService = jgWaybillUpdateService;
	}
}
