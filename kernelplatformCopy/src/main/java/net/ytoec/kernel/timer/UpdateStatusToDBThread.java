package net.ytoec.kernel.timer;

import java.util.concurrent.LinkedBlockingQueue;

import net.ytoec.kernel.action.remote.process.OrderProcessor;
import net.ytoec.kernel.action.remote.xml.UpdateInfo;
import net.ytoec.kernel.action.remote.xml.UpdateWaybillInfo;
import net.ytoec.kernel.dataobject.JgWaybillUpdate;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.service.JgWaybillUpdateService;
import net.ytoec.kernel.service.OrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class UpdateStatusToDBThread extends Thread {

	private static Logger logger = LoggerFactory
			.getLogger(UpdateStatusToDBThread.class);
	private OrderService<Order> orderService;
	
	OrderProcessor orderProcessor = new OrderProcessor();
	
	private static String partitiondateFormat = "yyyy-MM-dd";
	
	private JgWaybillUpdateService<JgWaybillUpdate> jgWaybillUpdateService;
	
	private boolean stop = false;
	
	private int threadNum=5;
	
	LinkedBlockingQueue<UpdateInfo> jgQueue = new LinkedBlockingQueue<UpdateInfo>();

	public void put(UpdateInfo bean) {
		try {
			jgQueue.put(bean);
		} catch (InterruptedException e) {
			logger.error("从jgQueue取值错误", e);
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (!stop) {
				UpdateWaybillInfo updateWaybillInfo = new UpdateWaybillInfo();
				UpdateInfo orderUpdateInfo = jgQueue.take();
				updateWaybillInfo
						.setAcceptTime(orderUpdateInfo.getAcceptTime());
				updateWaybillInfo.setClientId(orderUpdateInfo.getClientId());
				updateWaybillInfo.setMailNo(orderUpdateInfo.getMailNo());
				updateWaybillInfo.setTxLogisticId(orderUpdateInfo
						.getTxLogisticID());
				if ("GOT".equalsIgnoreCase(orderUpdateInfo.getInfoContent())) {
					updateWaybillInfo.setType(orderUpdateInfo.getAcceptTime());
				}

				updateWaybillInfo.setWeight(orderUpdateInfo.getWeight());
				updateWaybillInfo.setInfoContent(orderUpdateInfo
						.getInfoContent());
				orderService.orderStatusNodify(updateWaybillInfo);

			}
		} catch (Exception e) {
			logger.error("SendOrderStatusUpdate2dbThread error", e);
		} finally {

		}
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
