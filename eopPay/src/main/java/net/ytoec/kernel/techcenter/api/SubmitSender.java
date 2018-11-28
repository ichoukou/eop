package net.ytoec.kernel.techcenter.api;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.ytoec.kernel.common.PayEnumConstants;
import net.ytoec.kernel.common.TechcenterEnum;
import net.ytoec.kernel.dataobject.DredgeService;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.service.DredgeServiceService;
import net.ytoec.kernel.service.SMSDeliverService;
import net.ytoec.kernel.service.SMSHistoryInfoService;
import net.ytoec.kernel.service.SMSObjectService;
import net.ytoec.kernel.service.SMSOtherWaitService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techcenter.client.api.ISubmitSender;
import com.techcenter.protocol.standard12.Standard_Head;
import com.techcenter.protocol.standard12.Standard_Submit;

public class SubmitSender implements ISubmitSender {
	protected static final Logger logger = LoggerFactory
			.getLogger(SubmitSender.class);
	private SMSObjectService<SMSObject> SMSObjectService = SMSSender
			.getSMSObjectService();
	private SMSDeliverService<SMSObject> SMSDeliverService = SMSSender
			.getSMSDeliverService();
	private DredgeServiceService<DredgeService> dredgeServiceService = SMSSender
			.getDredgeServiceService();

	// 发送短信历史记录表
	private SMSHistoryInfoService<SMSObject> smsHistoryInfoService = SMSSender
			.getSMSHistoryInfoService();
	// 待发送短信表（供其他代理商（如：本草堂）使用
	private SMSOtherWaitService<SMSObject> smsOtherWaitService = SMSSender
			.getSMSOtherWaitService();

	private Properties properties = SMSSender.getPropertiesInstance();

	@Override
	public Standard_Head send() {
		// 2)第二种方式,单条查询
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", TechcenterEnum.TECHCENTERFLAG.WAIT.getValue());
		map.put("limit", 1);

		logger.error("易通发送短信-------start");
		List<SMSObject> sMSObjectLis = SMSObjectService.getList(map);
		if (CollectionUtils.isNotEmpty(sMSObjectLis)) {
			for (int i = 0; i < sMSObjectLis.size(); i++) {
				SMSObject sMSObject = sMSObjectLis.get(i);
				Standard_Submit ssm = new Standard_Submit();
				if (sMSObject.getMessageContent().length() > 70) {
					ssm.setMessageFormat((short) 32);
				} else {
					ssm.setMessageFormat((short) 15);
				}

				ssm.setContentString(sMSObject.getMessageContent());
				int count = sMSObject.getMessageContent().length();
				if (count <= 60) {
					sMSObject.setPkTotal(1);
				} else {
					sMSObject.setPkTotal((count / 57 + 1));
				}

				ssm.setSrcNumber("");
				ssm.setMessagePriority((short) 3);
				ssm.setReportType((short) 1);
				ssm.setSequenceId(sMSObject.getId());
				ssm.setDestMobile(sMSObject.getDestMobile());
				ssm.setProductID(Integer.parseInt(properties
						.getProperty("tech_ID")));
				sMSObject.setStatus(TechcenterEnum.TECHCENTERFLAG.YTOXL
						.getValue());
				sMSObject.setSendTime(new Date());
				SMSObjectService.edit(sMSObject);
				logger.error("=============================Standard_Submit对象发生"
						+ ssm.getSequenceId());
				return ssm;
			}

		}
		logger.error("易通发送短信-------end");

		// sequenceid不能为负数

		// logger.error("代运营发送短信-------------start");
		// List<SMSObject> list = smsOtherWaitService.getList(map);
		// if (CollectionUtils.isNotEmpty(list)) {
		// SMSObject sMSObject = list.get(0);
		// Standard_Submit ssm = new Standard_Submit();
		// if (sMSObject.getMessageContent().length() > 70) {
		// ssm.setMessageFormat((short) 32);
		// } else {
		// ssm.setMessageFormat((short) 15);
		// }
		//
		// ssm.setContentString(sMSObject.getMessageContent());
		// int count = sMSObject.getMessageContent().length();
		// if (count <= 70) {
		// sMSObject.setPkTotal(1);
		// } else {
		// sMSObject.setPkTotal((count / 67 + 1));
		// }
		//
		// ssm.setSrcNumber("");
		// ssm.setMessagePriority((short) 3);
		// ssm.setReportType((short) 1);
		// ssm.setSequenceId(sMSObject.getId() * (-1));
		// ssm.setDestMobile(sMSObject.getDestMobile());
		// ssm.setProductID(Integer.parseInt(properties.getProperty("tech_ID")));
		// sMSObject.setStatus(TechcenterEnum.TECHCENTERFLAG.YTOXL.getValue());
		// sMSObject.setSendTime(new Date());
		// smsOtherWaitService.edit(sMSObject);
		// logger.error("SubmitSenderOther========Standard_Submit对象发生"
		// + ssm.getSequenceId());
		// return ssm;
		// }
		// logger.error("代运营发送短信-------------end");

		return null;
	}

	/**
	 * 下面是response返回的几个重要值 response.getSequenceId(); // 与发送时设置的SequenceId一至
	 * response.getCommandStatus(); // response返回状态码，为0是接收成功，其它为错误请参考枚举类
	 **/
	public void receive(Standard_Head submit, Standard_Head response) {
		logger.error("发送日志submit-->" + submit);// 发送日志
		if (response.getCommandStatus() != 0) {

			// 若submit.getSequenceId()>0,则认为是易通平台的短信；否则认为是代运营的短信
			if ((int) submit.getSequenceId() > 0) {
				logger.error("易通发送短信平台接收异常-------------start");
				SMSObject sMSObject = new SMSObject();
				sMSObject.setId((int) submit.getSequenceId());
				sMSObject.setStatus(TechcenterEnum.TECHCENTERFLAG.SEND_FAIL
						.getValue());
				sMSObject.setDeliverTime(new Date());
				sMSObject.setErrorCode("平台接收异常");
				SMSObjectService.edit(sMSObject);
				sMSObject.setId(null);
				SMSDeliverService.add(sMSObject);
				// 短信数量退回(插入数据短信数量已经减少)
				if (StringUtils
						.equals(sMSObject.getSmsType(), String
								.valueOf(PayEnumConstants.SERVICE.SMS
										.getValue()))) {
					updateAddSmscount(sMSObject.getUserId(),
							sMSObject.getPkTotal());// 改变作法
				}

				SMSObject smsInfo = new SMSObject();
				smsInfo.setId((int) submit.getSequenceId());
				smsInfo = SMSObjectService.get(smsInfo);
				boolean bool = false;

				// 将回调过的短信插入到历史记录表中
				smsInfo.setId(null);
				bool = smsHistoryInfoService.add(smsInfo);
				if (bool == false)
					logger.error("从ec_core_paysms插入到发送短信历史记录表:ec_core_paysms_info  id="
							+ (int) submit.getSequenceId() + "失败...");

				// 将待发送短信表ec_core_paysms的对应的记录删除掉
				smsInfo.setId((int) submit.getSequenceId());
				bool = SMSObjectService.remove(smsInfo);
				if (bool == false)
					logger.error("删除待发送短信ec_core_paysms表  id="
							+ (int) submit.getSequenceId() + "失败...");

				logger.error("易通发送短信平台接收异常-------------end");
			} else {
				logger.error("代运营发送短信平台接收异常-------------start");
				SMSObject smsInfo = new SMSObject();
				smsInfo.setId((int) submit.getSequenceId() * (-1));
				smsInfo.setStatus(TechcenterEnum.TECHCENTERFLAG.SEND_FAIL
						.getValue());
				smsInfo.setDeliverTime(new Date());
				smsInfo.setErrorCode("平台接收异常");
				smsOtherWaitService.edit(smsInfo);

				// 将回调过的短信插入到短信历史记录表中
				boolean bool = false;
				smsInfo.setId((int) submit.getSequenceId() * (-1));
				smsInfo = smsOtherWaitService.get(smsInfo);
				smsInfo.setId(null);
				bool = smsHistoryInfoService.add(smsInfo);
				if (bool == false)
					logger.error("从ec_core_sms_wait插入到发送短信历史记录表:ec_core_paysms_info  id="
							+ (int) submit.getSequenceId() * (-1) + "失败...");

				// 将待发送短信表ec_core_sms_wait的对应的记录删除掉
				smsInfo.setId((int) submit.getSequenceId() * (-1));
				bool = smsOtherWaitService.remove(smsInfo);
				if (bool == false)
					logger.error("删除待发送短信ec_core_sms_wait表  id="
							+ (int) submit.getSequenceId() * (-1) + "失败...");

				logger.error("代运营发送短信平台接收异常-------------end");
			}
		}
	}

	public void updateAddSmscount(Integer userId, Integer smsCount) {
		DredgeService dredgeService = null;
		Map map = new HashMap();
		map.put("userId", userId);
		map.put("serviceId", PayEnumConstants.SERVICE.SMS.getValue());
		map.put("flag", PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
		List<DredgeService> dredgeServiceList = dredgeServiceService
				.getServiceRecordByUserId(map);
		if (CollectionUtils.isNotEmpty(dredgeServiceList)) {
			dredgeService = dredgeServiceList.get(0);
		}
		dredgeService.setSmsUsecount(dredgeService.getSmsUsecount() + smsCount);
		dredgeService.setSmsSendcount(dredgeService.getSmsSendcount()
				- smsCount);
		dredgeServiceService.edit(dredgeService);

	}
}
