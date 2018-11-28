package net.ytoec.kernel.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.action.remote.xml.Response;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.CoreTaskLogDao;
import net.ytoec.kernel.dao.ErrorMessageDao;
import net.ytoec.kernel.dao.FailedTaskDao;
import net.ytoec.kernel.dao.SendTaskDao;
import net.ytoec.kernel.dataobject.CoreTaskLog;
import net.ytoec.kernel.dataobject.ErrorMessage;
import net.ytoec.kernel.dataobject.FailedTask;
import net.ytoec.kernel.dataobject.SendTask;
import net.ytoec.kernel.service.SendTaskService;
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
 * @param <T> SendTask
 */
@Service
@Transactional
public class SendTaskServiceImpl<T extends SendTask> implements SendTaskService<T> {

    private static Logger logger = LoggerFactory.getLogger(SendTaskServiceImpl.class);
    @Autowired
    private SendTaskDao<T>                dao;
    @Autowired
    private CoreTaskLogDao<CoreTaskLog>   coreTaskLogDao;
    @Autowired
    private ErrorMessageDao<ErrorMessage> errorMessageDao;
    @Autowired
    private FailedTaskDao<FailedTask>     failedTaskDao;

    @Override
    public boolean addSendTask(T task) {

        try {
            this.dao.addSendTask(task);
        } catch (DataAccessException dae) {
            return false;
        }
        return true;
    }

    @Override
    public boolean finishedSendTask(T task) {

        long t1 = System.currentTimeMillis();
        try {
            this.dao.removeSendTask(task); //
            if (task.getRemark() != null) {
                FailedTask failedTask = new FailedTask();
                failedTask.setFailedReason(task.getRemark());
                failedTask.setRequestParams(task.getRequestParams());
                failedTask.setRequestUrl(task.getRequestURL());
                failedTask.setTaskDest("0");// 发给金刚
                failedTask.setTaskStatus("open");
                failedTask.setTxLogisticId(task.getTxLogisticId());
                this.failedTaskDao.addFailedTask(failedTask);
            } else {
                CoreTaskLog taskLog = new CoreTaskLog();
                taskLog.setClientId(task.getClientId());
                taskLog.setRequestURL(task.getRequestURL());
                taskLog.setRequestTime(task.getCreateTime());
                taskLog.setFailMessage(task.getRemark());
                // taskLog.setOrderId(task.getOrderId());
                taskLog.setRequestParams(WebUtil.decode(task.getRequestParams(), "UTF-8"));
                this.coreTaskLogDao.addCoreTaskLog(taskLog);
            }
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.toString(), e.toString());
            return false;
        }
        logger.info("dao single:" + (System.currentTimeMillis() - t1));
        return true;
    }

    @Override
    public boolean errfinishedSendTask(T task) {
        try {
            // this.dao.removeSendTask(task); // 先不删除

            ErrorMessage errMessage = new ErrorMessage();
            errMessage.setErrorReason(task.getRemark());
            errMessage.setErrorType(task.getTxLogisticId());
            errMessage.setErrorReason(task.getRemark());
            errMessage.setRemark(WebUtil.decode(task.getRequestParams(), "UTF-8"));
            errMessage.setUpdateTime(task.getCreateTime());

            this.errorMessageDao.addErrorMessage(errMessage);
        } catch (DataAccessException dae) {
            logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue(), dae);
            return false;
        }

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> getSendTaskList(Integer n) {
        return this.dao.getSendTaskListByLimit(n);
    }

    @Override
    public boolean updateSendTaskLastSendTime(T task) {

        return false;
    }

    public List<T> getPaginationSendTaskList(Pagination pagination, boolean flag) {
        Map map = new HashMap();
        if (flag) {
            map.put("startIndex", pagination.getStartIndex());
            map.put("pageNum", pagination.getPageNum());
        }
        return (List<T>) dao.getPaginationSendTaskList(map);
    }
    
    @Override
    public boolean sendTask(T task, XmlSender xmlSender) {
    	 logger.debug("start send task:" + task.getRequestURL() + "," + task.getRequestParams());
         long t1 = System.currentTimeMillis();
         String result = xmlSender.send();
         logger.info("single:" + (System.currentTimeMillis() - t1));
         // 检查结果,如果发送成功,则调用finishedSendTask()方法,如果调用失败,则 仍留在SendTask表中,以待下次发送.
         // task.setRemark(result);
         // String result =
         // "<response><txlogisticid></txlogisticid><logisticproviderid></logisticproviderid><success>false</success></response>";
         Response response = new Response().toObject(result);
         if (Response.SUCCESS_TRUE.equals(response.getSuccess())) {
             task.setRemark(null);
             return this.finishedSendTask(task);

         } else if (StringUtils.startsWith(response.getReason(), "B")) {
             task.setRemark(response.getReason());
             return this.finishedSendTask(task);

         } else {
             logger.error("response from jg false," + response.getReason() + "," + task.getTxLogisticId());
             // this.errfinishedSendTask(task);
             return false;
         }
    }
    
    @Override
    public boolean sendTaskJG(T task, XmlSender xmlSender) {

        logger.debug("start send task:" + task.getRequestURL() + "," + task.getRequestParams());
        long t1 = System.currentTimeMillis();
        String result = xmlSender.send();
        logger.info("single:" + (System.currentTimeMillis() - t1));
        // 检查结果,如果发送成功,则调用finishedSendTask()方法,如果调用失败,则 仍留在SendTask表中,以待下次发送.
        // task.setRemark(result);
        // String result =
        // "<response><txlogisticid></txlogisticid><logisticproviderid></logisticproviderid><success>false</success></response>";
       
        if(StringUtils.isBlank(result)){
        	logger.error("response false! response content:" + result);
			return false;
        }else{
        	if(result.contains(Response.SUCCESS_FALSE)){
        		task.setRemark(result);
        		return this.finishedSendTask(task);
        	 }else if(result.contains(Response.SUCCESS_TRUE)){
        		 	task.setRemark(null);
		            return this.finishedSendTask(task);
        	 }else{
        		 	task.setRemark(result);
        		 	return this.errfinishedSendTask(task);
        	 }
        }
        
        /*List<Response> responseList = new Response().toObject2(result);
        if (responseList == null || (responseList != null && responseList.size() == 0)) {
			logger.error("response false! response content:" + result);
			return false;
		} else {
			boolean flg = false;
	        for(Response response : responseList){
		        if (Response.SUCCESS_TRUE.equals(response.getSuccess())) {
		            task.setRemark(null);
		            task.setTxLogisticId(response.getTxLogisticId());
		            flg =  this.finishedSendTask(task);
		
		        } else if (StringUtils.startsWith(response.getReason(), "B") && !StringUtils.startsWith(response.getReason(), "Business")) {
		            task.setRemark(response.getReason());
		            task.setTxLogisticId(response.getTxLogisticId());
		            flg = this.finishedSendTask(task);
		
		        } else {
		            logger.error("response from jg false," + response.getReason() + "," + task.getTxLogisticId());
		            task.setTxLogisticId(response.getTxLogisticId());
		            flg = this.errfinishedSendTask(task);
		        }
	        }
	        return flg;
		}*/
        /*
         * else{ //保存为淘宝等其它电商平台的访问地址,需要根据clientID查询到对应的Url访问地址. task.setRequestURL
         * ("http://121.32.129.68:20080/padmin/yto/logistics.html"); // //sendTask
         * .setRequestURL(this.channelService.getChannelByClientId(clientId ).getIpAddress());
         * xmlSender.setUrlString(task.getRequestURL()); // 设置请求方法为POST方法.
         * xmlSender.setRequestMethod(XmlSender.POST_REQUEST_METHOD); // 设置请求参数.
         * xmlSender.setRequestParams(task.getRequestParams()); String result1 = xmlSender.send(); //
         * super.print(result1); Response response1 = new Response().toObject(result); }
         */

        /*
         * logger.debug("==GBK响应=="+decode(response.getReason(),"GBK")); logger.debug
         * ("======utf8解码后===="+decode(task.getRequestParams(),XmlSender .UTF8_CHARSET));
         * logger.debug("======GBK解码后===="+decode(task.getRequestParams (),"GBK"));
         */
        // 处理result, 如果成功则在sendTask删除此记录,并放入到log表中, 失败则直接跳过.
        //

        // EC_CORE_Send_Task是待发任务表, EC_CORE_Task_Log是发送任务日志表.

        // return false;
    }

    @Override
    public List<T> getSendTaskListByflag(int taskFlagId, String taskFlag, Pagination pagination, boolean flag) {
        // TODO Auto-generated method stub
        Map map = new HashMap();
        map.put("taskFlag", taskFlag);
        map.put("taskFlagId", taskFlagId);
        if (flag) {
            map.put("startIndex", pagination.getStartIndex());
            map.put("pageNum", pagination.getPageNum());
        }
        return (List<T>) dao.getSendTaskListByflag(map);

    }

    @Override
    public List<T> getSendTaskListByflag(Map map, Pagination pagination, boolean flag) {
        // TODO Auto-generated method stub
        if (flag) {
            map.put("startIndex", pagination.getStartIndex());
            map.put("pageNum", pagination.getPageNum());
        }
        return (List<T>) dao.getSendTaskListByflag(map);
    }

    @Override
    public List<T> getSendTaskListByFlagsAndLimit(List<Integer> flags, Integer limit) {
        if (flags == null || flags.isEmpty()) {
            logger.error("flags is null");
            return Collections.EMPTY_LIST;
        }
        Map map = new HashMap<String, Object>();
        map.put("flags", flags);
        map.put("limit", limit);
        return dao.getSendTaskListByFlagsAndLimit(map);
    }

    @Override
    public Integer countTask() {
        return this.dao.countTask();
    }
    
}
