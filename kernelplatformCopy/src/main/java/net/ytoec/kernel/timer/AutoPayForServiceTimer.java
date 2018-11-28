package net.ytoec.kernel.timer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.DateUtil;
import net.ytoec.kernel.common.PayEnumConstants;
import net.ytoec.kernel.common.TechcenterEnum;
import net.ytoec.kernel.dataobject.AccountUser;
import net.ytoec.kernel.dataobject.DredgeService;
import net.ytoec.kernel.dataobject.Logs;
import net.ytoec.kernel.dataobject.PayService;
import net.ytoec.kernel.dataobject.Payment;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.dataobject.ServiceHistory;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.AccountUserService;
import net.ytoec.kernel.service.DredgeServiceService;
import net.ytoec.kernel.service.LogsService;
import net.ytoec.kernel.service.PayServiceService;
import net.ytoec.kernel.service.PaymentService;
import net.ytoec.kernel.service.SMSObjectService;
import net.ytoec.kernel.service.ServiceHistoryService;
import net.ytoec.kernel.service.UserService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.jfree.util.Log;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 服务自动续费
 * 
 * @author huweikang
 * 
 */
public class AutoPayForServiceTimer extends QuartzJobBean {
	private DredgeServiceService<DredgeService> dredgeServiceService;
	private AccountUserService<AccountUser> accountUserService;
	private PaymentService<Payment> paymentService;
	private SMSObjectService<SMSObject> SMSObjectService;
	private ServiceHistoryService<ServiceHistory> serviceHistoryService;
	private PayServiceService<PayService> payServiceService;
	private UserService<User> userService;
	private LogsService<Logs> logsService;

	private static boolean isRunning = false;
	private static Logger logger = LoggerFactory.getLogger(RechargeSMSTimer.class);

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		if (isRunning) {
			return;
		}
		isRunning = true;
		List<DredgeService> dredgeServiceList = dredgeServiceService
				.getOpenserviceTimeList();// 得到开通的服务
		if (CollectionUtils.isNotEmpty(dredgeServiceList)) {
			for (DredgeService dredgeService : dredgeServiceList) {
				if (dredgeService.getServiceId() == PayEnumConstants.SERVICE.BALANCELESS
						.getValue()
						|| dredgeService.getServiceId() == PayEnumConstants.SERVICE.TRANSACTION
								.getValue()
						|| dredgeService.getServiceId() == PayEnumConstants.SERVICE.MATURITY
								.getValue()
						|| dredgeService.getServiceId() == PayEnumConstants.SERVICE.SMSLESS
								.getValue()
						|| dredgeService.getServiceId() == PayEnumConstants.SERVICE.SMS
								.getValue()) {
					// 免费服务与短信服务不做处理
					continue;
				} else {
					// 2)是否自动续费(到期自动续费)
					if (StringUtils.equals(
							PayEnumConstants.AUTO_FLAG.ISAUTO.value,
							dredgeService.getAutoRenew())) {
						Double diff_day = DateUtil.dayInterval(
								dredgeService.getEndDate(), new Date());
						// [时间就是到期前一天的8点钟,如果当天续费失败.后面的程序就会改为到期了]
						// if(DateUtil.isSameDay(dredgeService.getEndDate(), new
						// Date())){
						if (diff_day <= 1) {
							// 自动续费
							Map<String, Object> sHistory_map = new HashMap<String, Object>();
							sHistory_map.put("dserviceId",
									dredgeService.getId());
							sHistory_map.put("userId",
									dredgeService.getUserId());
							List<ServiceHistory> serviceHistoryList = serviceHistoryService
									.getServiceHistoryListByDUserId(sHistory_map);
							if (CollectionUtils.isNotEmpty(serviceHistoryList)) {
								ServiceHistory serviceHistory = serviceHistoryList
										.get(0);
								// 判断余额是否充足
								// <AccountUser> accountUserService
								AccountUser accountUser = accountUserService
										.getAccountUserByUserId(dredgeService
												.getUserId());
								String cellPhone = getCellPhone(accountUser);
								if (accountUser.getUseBalance() < serviceHistory
										.getPromCost()) {
									// （注：如果余额不足，不续费发送短信通知）
									// 等待短信
									if ((cellPhone != null)
											&& (cellPhone != "")) {
										PayService payService = new PayService();
										payService.setId(dredgeService
												.getServiceId());
										payService = payServiceService
												.get(payService);
										String smsContent = "亲，您在易通的余额不足，无法给【"
												+ payService.getName()
												+ "】服务续费，如需继续续费，请充值。【易通】";
										// 发送短信
										SMSObject sMSObject = new SMSObject();
										sMSObject.setUserId(dredgeService
												.getUserId());
										sMSObject.setMessageContent(smsContent);
										sMSObject.setDestMobile(accountUser
												.getCellPhone());
										sMSObject
												.setStatus(TechcenterEnum.TECHCENTERFLAG.WAIT
														.getValue());
										sMSObject
												.setSmsType(String
														.valueOf(PayEnumConstants.SERVICE.BALANCELESS
																.getValue()));
										SMSObjectService.add(sMSObject);
									}

									// 修改开通充值服务表-ec_core_dredgeService 不再自动续费
									dredgeService
											.setAutoRenew(PayEnumConstants.AUTO_FLAG.ISNAUTO.value);
									try {
										dredgeServiceService
												.updateDredgeserviceBalanceById(dredgeService);
									} catch (Exception e) {
										Log.error("自动续费修改开通充值服务表[续费状态]报异常");
									}
									Logs logs = new Logs();
									logs.setOperName("定时器自动续费");
									logs.setOperType(PayEnumConstants.OPERTYPE.TIMER
											.getValue());
									logs.setUserId(dredgeService.getUserId());
									logs.setRemark("定时器自动续费余额不足[不再续费]可用余额: "
											+ accountUser.getUseBalance()
											+ "续费费用: "
											+ serviceHistory.getPromCost());
									logsService.add(logs);
								} else {
									// 自动续费的步骤：1）修改账户表-ec_core_accountUser
									// 2）修改开通充值服务表-ec_core_dredgeService
									// 3）修改交易（支付）明细表-ec_core_payment 插入一条记录
									// 4)插入服务记录历史表-ec_core_serviceHistory

									Boolean bool = paymentService.autoPayment(
											serviceHistory, dredgeService);// 整个操作放到支付的service中实现

									// [5]业务处理完后,判断是否开通了提醒
									if (BooleanUtils.isTrue(bool)) {

										// 5.1)是否开通了交易提醒 5.2)判断用户是否开启了余额不足提醒
										paymentService.sendSMSByUserId(
												dredgeService.getUserId(),
												serviceHistory.getDealName(),
												dredgeService, "");// 发送余额提醒,交易提醒

										// 6）记下整个日志
										Logs logs = new Logs();
										logs.setOperName("定时器自动续费提醒");
										logs.setOperType(PayEnumConstants.OPERTYPE.AUTOFEE
												.getValue());
										logs.setUserId(dredgeService
												.getUserId());
										logs.setRemark("定时器自动续费费用为: "
												+ serviceHistory.getPromCost());
										logsService.add(logs);
									} else {
										Logs logs = new Logs();
										logs.setOperName("定时器自动续费失败");
										logs.setOperType(PayEnumConstants.OPERTYPE.AUTOFAIL
												.getValue());
										logs.setUserId(dredgeService
												.getUserId());
										logs.setRemark("定时器自动续费失败 ,开通服务id:"
												+ dredgeService.getId());
										logsService.add(logs);
									}

								}

							}
						}
					}
				}
			}
		}
		isRunning = false;
	}

	public DredgeServiceService<DredgeService> getDredgeServiceService() {
		return dredgeServiceService;
	}

	public void setDredgeServiceService(
			DredgeServiceService<DredgeService> dredgeServiceService) {
		this.dredgeServiceService = dredgeServiceService;
	}

	public AccountUserService<AccountUser> getAccountUserService() {
		return accountUserService;
	}

	public void setAccountUserService(
			AccountUserService<AccountUser> accountUserService) {
		this.accountUserService = accountUserService;
	}

	public PaymentService<Payment> getPaymentService() {
		return paymentService;
	}

	public void setPaymentService(PaymentService<Payment> paymentService) {
		this.paymentService = paymentService;
	}

	public SMSObjectService<SMSObject> getSMSObjectService() {
		return SMSObjectService;
	}

	public void setSMSObjectService(SMSObjectService<SMSObject> sMSObjectService) {
		SMSObjectService = sMSObjectService;
	}

	public ServiceHistoryService<ServiceHistory> getServiceHistoryService() {
		return serviceHistoryService;
	}

	public void setServiceHistoryService(
			ServiceHistoryService<ServiceHistory> serviceHistoryService) {
		this.serviceHistoryService = serviceHistoryService;
	}

	public PayServiceService<PayService> getPayServiceService() {
		return payServiceService;
	}

	public void setPayServiceService(
			PayServiceService<PayService> payServiceService) {
		this.payServiceService = payServiceService;
	}

	public LogsService<Logs> getLogsService() {
		return logsService;
	}

	public void setLogsService(LogsService<Logs> logsService) {
		this.logsService = logsService;
	}

	public static void main(String[] args) {

		Double diff_day = DateUtil.dayInterval(new Date(
				"Fri Sep 28 09:42:28 CST 2012"), new Date());
		System.out.println(diff_day);
	}

	public UserService<User> getUserService() {
		return userService;
	}

	public void setUserService(UserService<User> userService) {
		this.userService = userService;
	}

	public String getCellPhone(AccountUser accountUser) {
		String cellPhone = "";
		cellPhone = accountUser.getCellPhone();
		if (cellPhone == null) {
			User user = userService.getUserById(accountUser.getUserId());
			if (user != null && user.getMobilePhone() != null
					&& user.getMobilePhone() != "") {
				cellPhone = user.getMobilePhone();
			}
		}
		return cellPhone;
	}
}
