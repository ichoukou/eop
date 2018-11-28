package net.ytoec.kernel.timer;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dataobject.SendTask;
import net.ytoec.kernel.service.SendTaskMailNoService;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class NoticeMailNoTimer<T> extends QuartzJobBean {

	private static Logger logger = LoggerFactory.getLogger(NoticeMailNoTimer.class);

	private int limit = 1;

	private SendTaskMailNoService<SendTask> sendTaskMailNoService;

	public SendTaskMailNoService<SendTask> getSendTaskMailNoService() {
		return sendTaskMailNoService;
	}

	public void setSendTaskMailNoService(
			SendTaskMailNoService<SendTask> sendTaskMailNoService) {
		this.sendTaskMailNoService = sendTaskMailNoService;
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		logger.error("timer....");
		try {

			List<SendTask> mailNoList = new ArrayList<SendTask>();
			mailNoList = sendTaskMailNoService.getMailNoListByLimit(limit);
			SendTask sendTask = new SendTask();
//			logger.error(mailNoList);
			XmlSender xmlSender = new XmlSender();
			for (int i = 0; i < mailNoList.size(); i++) {
				sendTask = (SendTask) mailNoList.get(i);
				xmlSender.setRequestMethod(XmlSender.POST_REQUEST_METHOD);
				String xmlstring = sendTask.getRequestParams();
				String parternId = Resource.getSecretId(sendTask.getClientId());
				String params = "mailno_interface="
						+ encode(xmlstring, XmlSender.UTF8_CHARSET)
						+ "&"
						+ "data_digest="
						+ encode(
								Md5Encryption.MD5Encode(xmlstring + parternId),
								XmlSender.UTF8_CHARSET);
				xmlSender.setRequestParams(params);
				xmlSender.setUrlString(sendTask.getRequestURL());
				logger.error("url:" + sendTask.getRequestURL());
				logger.error("发送内容：" + sendTask.getRequestParams());
				logger.error("mailNo:" + sendTask.getMailNo());
				String result = xmlSender.send();
				logger.error("mailNo:" + sendTask.getMailNo() + " ," + result);
				if (StringUtils.contains(result, "true")) {
					sendTaskMailNoService.removeSendTask(sendTask.getMailNo(),
							sendTask.getClientId());
					logger.error("发送成功！");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("------------====================error:    "+e.getMessage());
			e.printStackTrace();
		}
	}

	private static String encode(String arg, String charset) {
		try {
			return java.net.URLEncoder.encode(arg, charset);
		} catch (UnsupportedEncodingException e) {
			logger.error(LogInfoEnum.PARSE_INVALID.getValue(), e);
			throw new RuntimeException(e);
		}
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}
