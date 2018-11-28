package net.ytoec.kernel.service.impl;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.action.remote.process.ProcessorUtils;
import net.ytoec.kernel.action.remote.process.XmlBuildProcessor;
import net.ytoec.kernel.common.Constants;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.JgWaybillDao;
import net.ytoec.kernel.dataobject.JgWaybill;
import net.ytoec.kernel.dataobject.SendTaskToTB;
import net.ytoec.kernel.service.JgWaybillService;
import net.ytoec.kernel.service.SendTaskToTBService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JgWaybillServiceImpl<T extends JgWaybill> implements
		JgWaybillService<T> {
	private static Logger logger = LoggerFactory.getLogger(JgWaybillServiceImpl.class);
	@Inject
	private JgWaybillDao<T> dao;
	@Autowired
	private SendTaskToTBService<SendTaskToTB> sendTaskService;

	public boolean addJgWaybills(List<T> jgWaybill) {
		try {
			for (T jb : jgWaybill) {
				boolean rs = dao.addJgWaybill(jb);
				if (!rs)
					return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean sendPrintOrders(List<T> jgWaybill) {
		// 存入jgwaybill表
		if (this.addJgWaybills(jgWaybill)) {
			// 生成电商通知task记录
			for(JgWaybill jb:jgWaybill){
				String key=Resource.getSecretId(jb.getClientID());
				String dslogisticsInterface=XmlBuildProcessor.getOrderUpdateXML(jb);
				String dsdataDigest=Md5Encryption.MD5Encode(dslogisticsInterface+key);
				dslogisticsInterface = ProcessorUtils.encode(dslogisticsInterface,XmlSender.UTF8_CHARSET);
				dsdataDigest = ProcessorUtils.encode(dsdataDigest,XmlSender.UTF8_CHARSET);
		       // 将SendTask存到数据库表中.
				SendTaskToTB sendTask = new SendTaskToTB();
				sendTask.setClientId(jb.getClientID());
				sendTask.setRequestURL(Resource.getChannel(jb.getClientID()));
				sendTask.setOrderId(0);
				sendTask.setRemark("订单打印,面单更新通知");
				sendTask.setTaskFlagId(ProcessorUtils.getflagid(Constants.VIPtxLogisticID));
				sendTask.setTaskFlag(ProcessorUtils.getflag(Constants.VIPtxLogisticID));
				sendTask.setTxLogisticId(Constants.VIPtxLogisticID);
				sendTask.setRequestParams(Constants.LOGISTICS_INTERFACE_PARAM + "="
						+ dslogisticsInterface + "&" + Constants.DATA_DIGEST_PARAM + "="
						+ dsdataDigest);
				try {
					if(!this.sendTaskService.addSendTaskToTB(sendTask))return false;
					logger.debug("打印订单已存入任务表:"+sendTask.getId());
				} catch (Exception e) {
					logger.debug("打印订单存任务表失败!");
					logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
				} 
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean delJgWaybillFromJG(int limit) {
		boolean result=false;
		try {
			dao.delJgWaybillFromJG(limit);
			result=true;
		} catch (Exception e) {
			result=false;
			logger.error("删除失败", e);
		} 
		return result;
	}

	@Override
	public Integer countJgwaybill() {
		Integer result=null;
		try {
			result=dao.countJgwaybill();
			 
		} catch (Exception e) {
			logger.error("统计JgWaybill失败", e);
		} 
		return result;
	}

}
