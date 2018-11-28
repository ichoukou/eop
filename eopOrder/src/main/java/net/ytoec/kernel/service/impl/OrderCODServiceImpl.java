package net.ytoec.kernel.service.impl;

import javax.inject.Inject;

import net.ytoec.kernel.action.remote.xml.UpdateWaybillInfo;
import net.ytoec.kernel.dao.OrderLogDao;
import net.ytoec.kernel.dataobject.SendTask;
import net.ytoec.kernel.service.OrderCODService;
import net.ytoec.kernel.service.SendTaskService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@SuppressWarnings("all")
public class OrderCODServiceImpl implements OrderCODService {

	private static Logger logger = LoggerFactory
			.getLogger(OrderCODServiceImpl.class);

	@Inject
	private OrderLogDao orderLogDao;

	@Inject
	private SendTaskService<SendTask> sendTaskService;

	@Override
	public String addCOD(UpdateWaybillInfo updateInfo, SendTask sendTask) {
		boolean flag = false;
		// TODO Auto-generated method stub
		orderLogDao.updateOrderLog(updateInfo.getTxLogisticId(),
				updateInfo.getPayAmount(), updateInfo.getPayTime(),
				updateInfo.getUnitId(), updateInfo.getEmployeeId(),
				updateInfo.getRemark());
		flag = true;
		updateInfo.getResponse().setLogisticProviderId(
				updateInfo.getLogisticProviderId());
		updateInfo.getResponse().setTxLogisticId(updateInfo.getTxLogisticId());
		String toXmlFragment = null;

		if (flag) {
			updateInfo.getResponse().setSuccess("true");
			sendTask.setRemark(updateInfo.getResponse().toXmlFragment());
			
			// 以"ZJ"开头的压测数据不进入sendtask表
			if(!updateInfo.getTxLogisticId().startsWith("ZJ")){
				if (!sendTaskService.addSendTask(sendTask)) {
					updateInfo.getResponse().setSuccess("false");
					updateInfo.getResponse().setReason("S07");
				}
			}
		} else {
			updateInfo.getResponse().setSuccess("false");
			updateInfo.getResponse().setReason("S07");
		}
		toXmlFragment = updateInfo.getResponse().toXmlFragment();
		return toXmlFragment;
	}
}