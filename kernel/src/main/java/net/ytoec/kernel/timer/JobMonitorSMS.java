package net.ytoec.kernel.timer;

import net.ytoec.kernel.common.PayEnumConstants;
import net.ytoec.kernel.common.TechcenterEnum;
import net.ytoec.kernel.dataobject.BuildSearch;
import net.ytoec.kernel.dataobject.BuildSearchStatusWeightIndex;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.dataobject.SendTask;
import net.ytoec.kernel.dataobject.SendTaskToTB;
import net.ytoec.kernel.search.service.BuildSearchService;
import net.ytoec.kernel.service.SMSObjectService;
import net.ytoec.kernel.service.SendTaskService;
import net.ytoec.kernel.service.SendTaskToTBService;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
/**
 * 监控积压情况，发送通知短信
 * @author chenfeng 2013-05-02
 */
public class JobMonitorSMS extends QuartzJobBean {

	public static Logger logger = Logger.getLogger(JobMonitor.class);

	private SMSObjectService<SMSObject> sMSObjectService;

	private BuildSearchService<BuildSearch> buildSearchService;
	private BuildSearchService<BuildSearchStatusWeightIndex> weightBuildSearchService;
	private BuildSearchService service;

	private SendTaskService<SendTask> sendTaskService;
	private SendTaskToTBService<SendTaskToTB> sendTaskToTBService;

	// 一级限阀标识
	private static boolean solrFlag = false;
	private static boolean statusFlag = false;

	// 二级限阀标识
	private static boolean solrFlag2 = false;
	private static boolean statusFlag2 = false;

	// 三级限阀标识
	private static boolean solrFlag3 = false;
	private static boolean statusFlag3 = false;

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {

		// 标识solr订单增量积压,一级阀限10000
		int taskBuildSearch = buildSearchService.countBuildSearch();

		int i = weightBuildSearchService.countBuildSearchStatusWeightIndex();
		int p = service.countBuildSearchStatusWeightIndexPressure();
		int v = service.countBuildSearchStatusWeightVersion();

		// 表示订单状态增量积压，一级阀限100000
		int taskBuildSearchStatusWeightIndex = i + p + v;

		// 短信内容
		String sMSContent = "";
		sMSContent += "solr订单增量积压：" + taskBuildSearch + "；";
		sMSContent += "订单状态积压：" + taskBuildSearchStatusWeightIndex + "；";

		// 是否为一级限阀发送短信，一级限阀里任意一项突破则发送短信，但不重复为相同的突破发送
		boolean isSend = false;
		// 一级阀限
		if (taskBuildSearch > 10000 && !solrFlag) {
			solrFlag = true;
			isSend = true;
		} else if (taskBuildSearch < 10000 && solrFlag) {
			solrFlag = false;
		}
		if (taskBuildSearchStatusWeightIndex > 100000 && !statusFlag) {
			statusFlag = true;
			isSend = true;
		} else if (taskBuildSearchStatusWeightIndex < 100000 && statusFlag) {
			statusFlag = false;
		}

		boolean isSend2 = false;
		// 二级限阀
		if (taskBuildSearch > 10000 * 10 && !solrFlag2) {
			solrFlag2 = true;
			isSend2 = true;
		} else if (taskBuildSearch < 10000 * 10 && solrFlag2) {
			solrFlag2 = false;
		}
		if (taskBuildSearchStatusWeightIndex > 100000 * 10 && !statusFlag2) {
			statusFlag2 = true;
			isSend2 = true;
		} else if (taskBuildSearchStatusWeightIndex < 100000 * 10 && statusFlag2) {
			statusFlag2 = false;
		}

		boolean isSend3 = false;
		// 三级限阀
		if (taskBuildSearch > 10000 * 10 * 10 && !solrFlag3) {
			solrFlag3 = true;
			isSend3 = true;
		} else if (taskBuildSearch < 10000 * 10 * 10 && solrFlag3) {
			solrFlag3 = false;
		}
		if (taskBuildSearchStatusWeightIndex > 100000 * 10 * 10 && !statusFlag3) {
			statusFlag3 = true;
			isSend3 = true;
		} else if (taskBuildSearchStatusWeightIndex < 100000 * 10 * 10 && statusFlag3) {
			statusFlag3 = false;
		}

		// 为一级限阀或二级或三级限阀发送短息
		if (isSend || isSend2 || isSend3) {
			sendAlarmSMS(sMSContent);
		}

	}

	private void sendAlarmSMS(String sMSContent) {
		String[] notifyMobiles = {"13795286101","18621380695","18217114407","18302125756"};
		// 发送短信
		SMSObject sMSObject = new SMSObject();
		// w00132 测试账号
		sMSObject.setUserId(16573);
		sMSObject.setMessageContent(sMSContent);
		// 陈峰
		//sMSObject.setDestMobile("13795286101");
		sMSObject.setStatus(TechcenterEnum.TECHCENTERFLAG.WAIT.getValue());
		sMSObject.setSmsType(String.valueOf(PayEnumConstants.SERVICE.MATURITY
				.getValue()));
		for (String mobile : notifyMobiles) {
			sMSObject.setDestMobile(mobile);
			sMSObjectService.add(sMSObject);
		}
		
	}

	public SMSObjectService<SMSObject> getsMSObjectService() {
		return sMSObjectService;
	}

	public void setsMSObjectService(SMSObjectService<SMSObject> sMSObjectService) {
		this.sMSObjectService = sMSObjectService;
	}

	public BuildSearchService<BuildSearch> getBuildSearchService() {
		return buildSearchService;
	}

	public void setBuildSearchService(
			BuildSearchService<BuildSearch> buildSearchService) {
		this.buildSearchService = buildSearchService;
	}

	public BuildSearchService<BuildSearchStatusWeightIndex> getWeightBuildSearchService() {
		return weightBuildSearchService;
	}

	public void setWeightBuildSearchService(
			BuildSearchService<BuildSearchStatusWeightIndex> weightBuildSearchService) {
		this.weightBuildSearchService = weightBuildSearchService;
	}

	public BuildSearchService getService() {
		return service;
	}

	public void setService(BuildSearchService service) {
		this.service = service;
	}

	public SendTaskService<SendTask> getSendTaskService() {
		return sendTaskService;
	}

	public void setSendTaskService(SendTaskService<SendTask> sendTaskService) {
		this.sendTaskService = sendTaskService;
	}

	public SendTaskToTBService<SendTaskToTB> getSendTaskToTBService() {
		return sendTaskToTBService;
	}

	public void setSendTaskToTBService(
			SendTaskToTBService<SendTaskToTB> sendTaskToTBService) {
		this.sendTaskToTBService = sendTaskToTBService;
	}

}
