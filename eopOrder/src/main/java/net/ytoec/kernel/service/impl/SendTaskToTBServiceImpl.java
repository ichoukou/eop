package net.ytoec.kernel.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.action.remote.xml.Response;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.CoreTaskLogToTBDao;
import net.ytoec.kernel.dao.ErrorMessageDao;
import net.ytoec.kernel.dao.FailedTaskDao;
import net.ytoec.kernel.dao.SendTaskToTBDao;
import net.ytoec.kernel.dataobject.CoreTaskLogToTB;
import net.ytoec.kernel.dataobject.ErrorMessage;
import net.ytoec.kernel.dataobject.FailedTask;
import net.ytoec.kernel.dataobject.SendTaskToTB;
import net.ytoec.kernel.service.SendTaskToTBService;
import net.ytoec.kernel.util.WebUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 任务发送服务接口实现类.
 * 
 * @param <T>
 *            SendTaskToTB
 */
@Service
@Transactional
public class SendTaskToTBServiceImpl<T extends SendTaskToTB> implements
		SendTaskToTBService<T> {

	private static Logger logger = LoggerFactory.getLogger(SendTaskToTBServiceImpl.class);
	@Autowired
	private SendTaskToTBDao<SendTaskToTB> dao;
	@Autowired
	private CoreTaskLogToTBDao<CoreTaskLogToTB> coreTaskLogDaoToTB;
	@Autowired
	private ErrorMessageDao<ErrorMessage> errorMessageDao;
	@Autowired
	private FailedTaskDao<FailedTask> failedTaskDao;

	@Override
	public boolean addSendTaskToTB(T taskToTB) {

		try {
			this.dao.addSendTaskToTB(taskToTB);
		} catch (DataAccessException dae) {
			return false;
		}
		return true;
	}

	@Override
	public boolean finishedSendTaskToTB(SendTaskToTB taskToTB) {
		long t1 = System.currentTimeMillis();
		try {
			this.dao.removeSendTaskToTB(taskToTB); //
			if (taskToTB.getRemark() != null) {
				FailedTask failedTask = new FailedTask();
				failedTask.setFailedReason(taskToTB.getRemark());
				failedTask.setRequestParams(taskToTB.getRequestParams());
				failedTask.setRequestUrl(taskToTB.getRequestURL());
				failedTask.setTaskDest("1");// 发给淘宝
				failedTask.setTaskStatus("open");
				failedTask.setTxLogisticId(taskToTB.getTxLogisticId());
				logger.error(taskToTB.getTxLogisticId() + "发送失败");
				this.failedTaskDao.addFailedTask(failedTask);
			} else {
				CoreTaskLogToTB taskLog = new CoreTaskLogToTB();
				taskLog.setClientId(taskToTB.getClientId());
				taskLog.setRequestURL(taskToTB.getRequestURL());
				taskLog.setRequestTime(taskToTB.getCreateTime());
				// taskLog.setOrderId(taskToTB.getOrderId());
				taskLog.setFailMessage(taskToTB.getRemark());
				taskLog.setRequestParams(taskToTB.getRequestParams());
				logger.error(taskToTB.getTxLogisticId() + "发送成功");
				// TODO 测试库中由于表中没有分区字段会有报错的现象需要在生产环境上放开
				this.coreTaskLogDaoToTB.addCoreTaskLog(taskLog);
			}
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.toString(), e.toString());
			return false;
		}
		logger.info("dao single2tb:" + (System.currentTimeMillis() - t1));
		return true;
	}

	@Override
	public boolean errfinishedSendTaskToTB(T taskToTB) {
		try {
			// this.dao.removeSendTaskToTB(taskToTB); //先不删除

			ErrorMessage errMessage = new ErrorMessage();
			errMessage.setErrorType(taskToTB.getTxLogisticId());
			errMessage.setErrorReason(taskToTB.getRemark());
			errMessage.setRemark(WebUtil.decode(taskToTB.getRequestParams(),
					"UTF-8"));
			errMessage.setUpdateTime(taskToTB.getCreateTime());
			this.errorMessageDao.addErrorMessage(errMessage);
		} catch (DataAccessException dae) {
			logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue(), dae);
			return false;
		}

		return true;
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> getSendTaskToTBList(int n) {
		return (List<T>) this.dao.getSendTaskToTBListByLimit(n);
	}

	@Override
	public boolean updateSendTaskToTBLastSendTime(T taskToTB) {

		return false;
	}

	public List<T> getPaginationSendTaskToTBList(Pagination pagination,
			boolean flag) {
		Map map = new HashMap();
		if (flag) {
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
		}
		return (List<T>) dao.getPaginationSendTaskToTBList(map);
	}

	@Override
	public List<T> getSendTaskToTBListByflag(int taskFlagId, String taskFlag,
			Pagination pagination, boolean flag) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("taskFlag", taskFlag);
		map.put("taskFlagId", taskFlagId);
		if (flag) {
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
		}
		return (List<T>) dao.getSendTaskToTBListByflag(map);

	}

	@Override
	public List<T> getSendTaskToTBListByflag(Map map, Pagination pagination,
			boolean flag) {
		// TODO Auto-generated method stub
		if (flag) {
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
		}
		return (List<T>) dao.getSendTaskToTBListByflag(map);
	}

	@Override
	public List<SendTaskToTB> getSendTaskListByFlagsAndLimit(
			List<Integer> flags, Integer limit) {
		if (flags == null || flags.isEmpty()) {
			logger.error("flags is empty");
			return Collections.EMPTY_LIST;
		}
		logger.error("flagList:" + flags.get(0) + ","
				+ flags.get(flags.size() - 1) + "," + limit);
		Map map = new HashMap<String, Object>();
		map.put("flags", flags);
		map.put("limit", limit);
		return dao.getSendTaskListByFlagsAndLimit(map);
	}

	@Override
	public boolean sendTaskToTB(SendTaskToTB taskToTB, XmlSender xmlSender) {

		logger.info("start send task: id--" + taskToTB.getId() + "-- "
				+ taskToTB.getRequestURL() + "," + taskToTB.getRequestParams());
		long t1 = System.currentTimeMillis();
		String result = null;
		
		// 淘宝1.4.1接口 订单状态 、重量、面单号更新由金刚推送给淘宝
		if (StringUtils.isNotBlank(taskToTB.getRequestURL())
				&& taskToTB.getRequestURL().toUpperCase().indexOf("TAOBAO") > -1) {
			
			// 易通表中若有推送到淘宝的数据直接删除
			taskToTB.setRemark(null);
			this.finishedSendTaskToTB(taskToTB);
			return true;
			/*result = xmlSender.sendTaoBao();
			logger.error("推送订单物流号:" + taskToTB.getTxLogisticId());
			logger.info("<Response>:" + result);
			logger.info("single2tb:" + (System.currentTimeMillis() - t1));
			// 检查结果,如果发送成功,则调用finishedSendTaskToTB()方法,如果调用失败,则
			// 仍留在SendTaskToTB表中,以待下次发送.

			List<Response> responseList = new Response().toObject2(result);
			if (responseList == null || (responseList != null && responseList.size() == 0)) {
				logger.error("response false! response content:" + result);
				return false;
			} else {
				boolean flg = false;
				for (Response response : responseList) {
					if (Response.SUCCESS_TRUE.equals(response.getSuccess())) {
						taskToTB.setRemark(null);
						taskToTB.setTxLogisticId(response.getTxLogisticId());
						flg = this.finishedSendTaskToTB(taskToTB);
					} else if (response.getReason().startsWith("B")
							|| (response.getReason().startsWith("S") && !response
									.getReason().equals("S07"))) {
						taskToTB.setRemark(response.getReason());
						taskToTB.setTxLogisticId(response.getTxLogisticId());
						flg = this.finishedSendTaskToTB(taskToTB);
					} else {
						logger.error("response false," + response.getReason()
								+ "," + taskToTB.getTxLogisticId());
						taskToTB.setTxLogisticId(response.getTxLogisticId());
						flg = this.errfinishedSendTaskToTB((T) taskToTB);
					}
				}
				return flg;  
			}*/
		} else {
			result = xmlSender.send();
			logger.error("推送订单物流号:" + taskToTB.getTxLogisticId());
			logger.error("电商平台返回报文:" + result);
			logger.error("打开电商平台连接耗时:" + (System.currentTimeMillis() - t1));
			// 检查结果,如果发送成功,则调用finishedSendTaskToTB()方法,如果调用失败,则
			// 仍留在SendTaskToTB表中,以待下次发送.

			if("YIXUN".equals(taskToTB.getClientId())){
				if(result.contains("error")){
					String str_1 = "<msg>";
					int index_1 = result.indexOf("<msg>");
					String msg_1 = result.substring(index_1+str_1.length());
					int index_2 = msg_1.indexOf("</msg>");
					String msg_2 = msg_1.substring(0,index_2);
					taskToTB.setRemark(msg_2);
					return this.finishedSendTaskToTB(taskToTB);
				}else{
					taskToTB.setRemark(null);
					return this.finishedSendTaskToTB(taskToTB);
				}
			}
			
			if(result.contains(Response.SUCCESS_TRUE)){
				taskToTB.setRemark(null);
				return this.finishedSendTaskToTB(taskToTB);
			}else{
				logger.error("response false,result:" + result + ","
						+ taskToTB.getTxLogisticId());
				return this.errfinishedSendTaskToTB((T) taskToTB);
			}
		}
	}

	@Override
	public Integer countTask() {
		// TODO Auto-generated method stub
		return this.dao.countTask();
	}

	/**
	 * 批量添加发送淘宝任务记录
	 * 
	 * @author wmd
	 * @param dendTaskToTBList
	 *            发送淘宝记录
	 * @return b
	 */
	@Override
	public boolean batchAddSendTaskToTB(List<T> sendTaskToTBList) {
		try {
			this.dao.batchAddSendTask2TB((List<SendTaskToTB>) sendTaskToTBList);
		} catch (DataAccessException dae) {
			return false;
		}
		return true;
	}
}
