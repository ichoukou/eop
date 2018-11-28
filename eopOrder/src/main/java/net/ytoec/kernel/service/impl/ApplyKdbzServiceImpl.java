package net.ytoec.kernel.service.impl;

import java.util.Date;

import javax.inject.Inject;

import net.ytoec.kernel.dao.ApplyKdbzDao;
import net.ytoec.kernel.dao.SendTaskDao;
import net.ytoec.kernel.dao.SendTaskToTBDao;
import net.ytoec.kernel.dataobject.ApplyKdbz;
import net.ytoec.kernel.dataobject.SendTask;
import net.ytoec.kernel.dataobject.SendTaskToTB;
import net.ytoec.kernel.service.ApplyKdbzService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ApplyKdbzServiceImpl<T extends ApplyKdbz>
		implements ApplyKdbzService<T> {
	private static Logger logger = LoggerFactory.getLogger(ApplyKdbzServiceImpl.class);

	@Inject
	private ApplyKdbzDao<ApplyKdbz> applyKdbzDao;
	@Autowired
	private SendTaskDao<SendTask> sendTaskDao;
	@Inject
	private SendTaskToTBDao<SendTaskToTB> sendTaskToTBDao;

	@Override
	public boolean addApplyKdbz(ApplyKdbz applyKdbz, SendTask sendTask) {
		try {

			//applyKdbz.setCreateTime(new Date());
//			applyKdbz.setEcCompanyId(applyKdbz.getEcCompanyId());
//			applyKdbz.setLogisticProvider(applyKdbz.getLogisticProvider());
//			applyKdbz.setCustomerId(applyKdbz.getCustomerId());
//			applyKdbz.setWangWang(applyKdbz.getWangWang());
//			applyKdbz.setStatus(applyKdbz.getStatus());
//			applyKdbz.setRequestNo(applyKdbz.getRequestNo());
//			applyKdbz.setServiceType(applyKdbz.getServiceType());
//			applyKdbz.setName(applyKdbz.getName());
//			applyKdbz.setPhone(applyKdbz.getPhone());
//			applyKdbz.setMobile(applyKdbz.getMobile());
//			applyKdbz.setAddress(applyKdbz.getAddress());
//			applyKdbz.setRemark(applyKdbz.getRemark());

			Boolean flag = applyKdbzDao.addApplyKdbz(applyKdbz);
			if (flag == true) {

				sendTaskDao.addSendTask(sendTask);
			}
		} catch (Exception e) {
			logger.error("applyKdbz create failed", e);
			return false;
		}
		return true;
	}

	@Override
	public boolean editApplyKdbz(ApplyKdbz cancelKdbz, SendTask sendTask) {
		try {
			
			//FIXME 
			
			Boolean flag = applyKdbzDao.editApplyKdbz(cancelKdbz);
			if (flag == true) {

				sendTaskDao.addSendTask(sendTask);
			}
		} catch (Exception e) {
			logger.error("applyKdbz cancel failed", e);
			return false;
		}
		return true;
	}

	public boolean applyStatusNodify(ApplyKdbz applyKdbz, SendTaskToTB sendTaskToTB) {
		try {
			applyKdbz.setUpdateTime(new Date());
			applyKdbz.setStatus(applyKdbz.getStatus());
			Boolean flag = applyKdbzDao.editApplyKdbz(applyKdbz);
			if (flag == true) {

				sendTaskToTBDao.addSendTaskToTB(sendTaskToTB);
			}
		} catch (Exception e) {
			logger.error("状态通知失败", e);
			return false;
		}
		return true;
	}

}
