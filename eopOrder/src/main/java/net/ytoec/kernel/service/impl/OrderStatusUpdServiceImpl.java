package net.ytoec.kernel.service.impl;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.action.remote.xml.UpdateInfo;
import net.ytoec.kernel.dao.OrderStatusDao;
import net.ytoec.kernel.dataobject.SendTaskToTB;
import net.ytoec.kernel.service.OrderStatusUpdService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 批量操作实现类 create by wangmindong create timer 2013-03-25
 * 
 */
@Service
@Transactional
@SuppressWarnings("all")
public class OrderStatusUpdServiceImpl implements OrderStatusUpdService {

	@Inject
	private OrderStatusDao dao;

	/**
	 * 更新订单状态
	 */
	@Override
	public boolean batchUpdateOrdStatus(List<UpdateInfo> list,
			List<UpdateInfo> questionList) {

		boolean result = dao.batchModifyOrdStatus(list);
		if (questionList.size() > 0) {
			dao.batchUpdateOrderStatusByMailNo(questionList);
		}
		return result;
	}

	/**
	 * 批量添加task 到 ec_core_send_taskToTB
	 */
	@Override
	public boolean batchAddTask2TB(List<SendTaskToTB> list) {
		return dao.batchAddSendTaskToTB(list);
	}

}
