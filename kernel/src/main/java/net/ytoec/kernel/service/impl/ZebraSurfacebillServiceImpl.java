package net.ytoec.kernel.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.common.PayEnumConstants;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.common.TechcenterEnum;
import net.ytoec.kernel.common.WebServiceUtils;
import net.ytoec.kernel.dao.SellerInfoDao;
import net.ytoec.kernel.dao.ZebraForewarnDao;
import net.ytoec.kernel.dao.ZebraParternDao;
import net.ytoec.kernel.dao.ZebraSequenceDao;
import net.ytoec.kernel.dao.ZebraSurfacebillDao;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.dataobject.OrderInfoRequest;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.ZebraForewarn;
import net.ytoec.kernel.dataobject.ZebraPartern;
import net.ytoec.kernel.dataobject.ZebraSequence;
import net.ytoec.kernel.dataobject.ZebraSurfacebill;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.SMSObjectService;
import net.ytoec.kernel.service.ZebraSurfacebillService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yto.coresystem.module.matbranch.domain.MaterialDzDetail;

@Service
public class ZebraSurfacebillServiceImpl implements ZebraSurfacebillService {

	private static Logger logger = Logger
			.getLogger(ZebraSurfacebillServiceImpl.class);

	private static final String LOGISTICS_INTERFACE_PARAM = "logistics_interface";
	private static final String DATA_DIGEST_PARAM = "data_digest";
	private static final String CLIENT_ID_PARAM = "clientId";
	private static final String GET_METHOD_RESPONSE = "Success";
	private static final String ADMIN_QQ = "2294882345";// 管理员qq
	private static final int pageSize = 4000;// 每次给商家下发面单的最大数量
	private static final String charset = "UTF-8";

	// 合并信息用Set集合去存放不会有重复记录
	Set<String> setMessage = null;

	// 定义存储XML订单信息的集合
	List<OrderInfoRequest> orderInfoRequestList = null;

	@Inject
	private ZebraForewarnDao<ZebraForewarn> zebraForewarnDao;

	@Value("${orderSyncJgUrl}")
	private String orderSyncJgUrl;

	@Inject
	private MailService<Mail> mailService;

	@Inject
	private SMSObjectService<SMSObject> smsService;

	@Inject
	private ZebraParternDao<ZebraPartern> zebraParternDao;

	@Inject
	private SellerInfoDao<User> sellerInfoDao;

	@Inject
	private ZebraSequenceDao<ZebraSequence> zebraSequenceDao;

	@Inject
	private ZebraSurfacebillDao<ZebraSurfacebill> zebraSurfacebillDao;

	private static final DateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	@Override
	public String selectVersionNo(String userName) {
		return zebraSurfacebillDao.selectVersionNo(userName);
	}

	@Override
	public Integer selectCounts(String userName) {
		return zebraSurfacebillDao.selectCounts(userName);
	}

	@Override
	@Transactional
	public boolean insertBill(ZebraSurfacebill entity) {
		// try {
		// entity.setWaybillNo(Md5Encryption.encryptBASE64(entity
		// .getWaybillNo().getBytes()));
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		int number = zebraSurfacebillDao.insertBill(entity);
		return number > 0 ? true : false;
	}

	/*
	 * 从金刚同步电子面单 到易通的定时任务
	 */
	@Override
	public void waybillTimer() {
		// 1:循环分页查询得到商家代码和密钥
		Map<String, Object> paramMap = new HashMap<String, Object>();
		int totals = zebraParternDao.selectTotal(paramMap);
		int startIndex = 0;
		int pageSize = 1000;
		int pageNum = totals / pageSize != 0 ? (totals % pageSize == 0 ? totals
				/ pageSize : totals / pageSize + 1) : 1;
		List<ZebraPartern> zebraParterns = new ArrayList<ZebraPartern>();
		for (int i = 0; i < pageNum; i++) {
			startIndex = i * pageSize;

			paramMap.put("startIndex", startIndex);
			paramMap.put("pageSize", pageSize);
			zebraParterns = zebraParternDao.selectPageList(paramMap);

			// 2:循环列表，从金刚同步商家的电子面单到易通
			for (ZebraPartern zebraPartern : zebraParterns) {
					logger.info("pull waybill start!");
					syncWaybillNo(zebraPartern);
					logger.info("pull waybill end!");
			}
		}
	}

	/**
	 * 从金刚同步商家电子面单到易通
	 * 
	 * @param customerCode
	 *            商家代码
	 * @return
	 */
	@Transactional
	private String syncWaybillNo(ZebraPartern zebraPartern) {
		String customerCode = zebraPartern.getCustomerCode();
		String parternCode = zebraPartern.getParternCode();

		String message = null;
		int number = 0;// 记录同步数量
		int sum = 0;// 记录库存数量
		String versionNo = "0";
		try {
			try {
				versionNo = zebraSurfacebillDao.selectVersionNo(customerCode);
				if (versionNo == null || "null".equals(versionNo)) {
					versionNo = "0";
				}
				logger.info("step1");
			} catch (Exception e) {
				logger.error("获取versionNo异常！", e);
				versionNo = "0";
			}

			logger.info("step2");
			Object[] objects = WebServiceUtils.invokeWebService(orderSyncJgUrl,
					"http://ws.matbranch.module.coresystem.yto.com",
					"getMaterialDzDetailWebService", new Object[] {
							customerCode, versionNo, parternCode },
					new Class[] { MaterialDzDetail[].class });
			logger.info("step3");

			MaterialDzDetail[] result = (MaterialDzDetail[]) objects[0];
			try {
				sum = zebraSurfacebillDao.selectCounts(customerCode);// 商家的电子面单库存数量(is_use=0)
				logger.info("step4");
			} catch (Exception e) {
				logger.error("商家的电子面单库存数量异常！", e);
				sum = 0;
			}
			/*
			 * if (result == null || result.length <= 0) { message =
			 * "您的运单信息已经为最新！库存数量为：【" + sum + "】张"; return message; }
			 */
			// List<ActivedMaterial> list = new ArrayList<ActivedMaterial>();
			// List<List<ActivedMaterial>> activedMaterialList = new
			// ArrayList<List<ActivedMaterial>>();
			List<MaterialDzDetail> activedMaterials = new ArrayList<MaterialDzDetail>();
			// 从数组里面在取出一个数组
			// 记录同步数量
			for (int i = 0; i < result.length; i++) {
				activedMaterials.add((MaterialDzDetail) result[i]);
			}
			logger.info("step5");

			// 循环数组里面的对象
			ZebraSurfacebill waybill = new ZebraSurfacebill();
			MaterialDzDetail ac = new MaterialDzDetail();
			for (int i = 0; i < activedMaterials.size(); i++) {
				// int startNo =
				// Integer.valueOf(activedMaterial.getStartNo().substring(0,
				// 9));
				// int endNo =
				// Integer.valueOf(activedMaterial.getEndNo().substring(0, 9));
				// int count = endNo + 1 - startNo;
				// for (int i = 0; i < count; i++) {

				try {
					ac = activedMaterials.get(i);

					waybill.setFbCreatedate(Timestamp.valueOf(sdf.format(ac
							.getCreateTime())));
					waybill.setUse(false);// 是否使用(未使用：false；已使用：true)
					waybill.setWaybillNo(ac.getWaybillNo());
					waybill.setFbsendTimes(0);
					waybill.setFbsendStatus(0);// 上传状态(未上传：0；上传失败：1；上传成功：2；异常上传：3)
					waybill.setCurrentUserNo(customerCode);

					waybill.setVersionNo(ac.getVersionNo().toString());

					waybill.setFbsendTimes(0);

					if (zebraSurfacebillDao.insertBill(waybill) > 0) {
						number += 1;
					}
					logger.info("step6 waybillNo:" + waybill.getWaybillNo());

					/*
					 * PropertyUtils.copyProperties(ac, activedMaterial);
					 * //生成使用的运单号码 String waybillNo =
					 * String.valueOf(startNo)+String
					 * .valueOf(StringUtil.encrypt(startNo));
					 * 
					 * //String No =waybillNo.substring(waybillNo.length()-3,
					 * waybillNo.length()); //把对象放入集合中 number += ac.getQty();
					 * ac.setWaybillNo(waybillNo);
					 * 
					 * activedMaterials.add(ac);
					 */
				} catch (Exception e) {
					logger.error("同步数据失败！", e);
					sum = sum + number;
					message = "同步数据失败！库存数量为：【" + sum + "】张," + e.getMessage();
					break;
				}/*
				 * catch (UnsupportedEncodingException e) { message
				 * ="同步数据失败！库存数量为：【" + sum + "】张,"+ e.getMessage(); break;
				 * }catch (Exception e) { message ="同步数据失败！库存数量为：【" + sum +
				 * "】张,"+ e.getMessage(); break; }
				 */
				// }
				// activedMaterialList.add(activedMaterials);
			}
			// 执行添加操作
			/* zebraJdbcDao.batchSaveMatInfo(activedMaterials); */

			// if (activedMaterialList != null && activedMaterialList.size() >0)
			// {
			// for (List<ActivedMaterial> activedMaterials2 :
			// activedMaterialList) {
			// zebraJdbcDao.batchSaveMatInfo(activedMaterials2);
			// }
			// }
			sum = sum + number;
			message = "同步运单成功！同步数量为：【" + number + "】张,库存数量为：【" + sum + "】张";

			// 如果同步成功的面单数>=网点采购预警值，设置商家预警状态、送短信状态、发送邮件状态为正常
			ZebraForewarn zebraForewarn = zebraForewarnDao
					.selectByCustomerCode(customerCode);
			logger.info("step7");
			if (zebraForewarn != null
					&& zebraForewarn.getBranckWarnValue() <= sum) {
				// 设置预警状态 为正常(0:是正常状态,1:预警状态)
				zebraForewarnDao.updateWarnStateByCustomerCodeAndWarnState(
						customerCode, 0);
				// 设置短信发送状态为正常
				zebraForewarnDao.updateSendPhoneStateByCustomerCode(
						customerCode, 0);
				// 设置邮件发送状态为正常
				zebraForewarnDao.updateSendMailStateByCustomerCode(
						customerCode, 0);
				logger.info("step8");
			}
		} catch (Exception e) {
			logger.info("同步异常！", e);
			sum = sum + number;
			message = "同步异常！库存数量为：【" + sum + "】张" + e.getMessage();
		}

		logger.info("message:" + message);

		return message;

	}

	@Override
	public List<ZebraSurfacebill> selectZebraSurfacebillsByCustomerCode(
			String customerCode, int counts, int state) {
		return zebraSurfacebillDao.selectZebraSurfacebillsByCustomerCode(
				customerCode, counts, state);
	}

	@Override
	@Transactional
	public int batchUpdateUseState(List<String> waybillNos, int state) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("waybillNos", waybillNos);
		params.put("state", state);
		return zebraSurfacebillDao.batchUpdateUseState(params);
	}

	@Override
	@Transactional
	public int batchUpdatePrintState(List<Map<String, String>> waybillNos) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("waybillNos", waybillNos);
		return zebraSurfacebillDao.batchUpdatePrintState(params);
	}

	@Override
	public int selectCountsByState(String customerCode, int state) {
		return zebraSurfacebillDao.selectCountsByState(customerCode, state);
	}

	@Override
	public int selectAllCount(String customerCode) {
		return zebraSurfacebillDao.selectAllCountsBycustomerCode(customerCode);
	}

	@Override
	@Transactional
	public int batchInsert(List<Map<String, String>> waybillNos,
			String customerCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("waybillNos", waybillNos);
		params.put("customerCode", customerCode);
		return zebraSurfacebillDao.batchInsert(params);
	}

	@Override
	@Transactional
	public int batchDelete(List<String> waybillNos, String customerCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("waybillNos", waybillNos);
		params.put("customerCode", customerCode);
		return zebraSurfacebillDao.batchDelete(params);
	}

	@Override
	public int selectCountsByCustomerCode(String customerCode) {
		return zebraSurfacebillDao.selectCountsByCustomerCode(customerCode);
	}

	@Override
	public List<String> selectByCustomerCodeAndCounts(int counts,
			String customerCode) {
		return zebraSurfacebillDao.selectByCustomerCodeAndCounts(counts,
				customerCode);
	}

	/**
	 * 检查商家电子面单预警
	 * 
	 * @param customerCode
	 *            商家代码
	 */
	private void checkForewarn(String customerCode) {
		// 1:查询商家未下发状态的电子面单数量
		int noUseSum = zebraSurfacebillDao.selectCountsByState(customerCode, 0);
		// 2:查询商家的电子面单预警信息
		ZebraForewarn zebraForewarn = zebraForewarnDao
				.selectByCustomerCode(customerCode);
		// 3:根据customerCode(即:username)查询用户id(id)
		String siteId = sellerInfoDao
				.findSiteCodeByUserCodeFromUT(customerCode);
		int userId = Resource.getUserBySiteCode(siteId).getId();

		// 4:预警判断
		if (zebraForewarn != null && !zebraForewarn.isWarnState()) {// 商家处于正常状态
			// 商家最低面单预警值
			int customerWarnValue = zebraForewarn.getCustomerWarnValue();
			// 网点给商家预留面单预警值
			int branckWarnValue = zebraForewarn.getBranckWarnValue();
			if ((branckWarnValue > 0 && customerWarnValue > 0)
					&& (branckWarnValue >= customerWarnValue)
					&& (noUseSum < branckWarnValue || noUseSum < customerWarnValue)) {
				List<String> contents = new ArrayList<String>();
				String content = null;
				if (noUseSum < branckWarnValue) {
					content = "商家:" + zebraForewarn.getCustomerCode()
							+ "的电子面单数量为:" + noUseSum + ", 少于网点采购预警值!";
					contents.add(content);
				}
				if (noUseSum < customerWarnValue) {
					content = "商家:" + zebraForewarn.getCustomerCode()
							+ "的电子面单数量为:" + noUseSum + ", 少于商家号码缓冲值!";
					contents.add(content);
				}

				// 设置预警状态为预警(0:正常状态,1:预警状态)
				zebraForewarnDao.updateWarnStateByCustomerCodeAndWarnState(
						customerCode, 1);

				// 发邮件(处于预警状态，并且以前没有发送过邮件，发送一次)
				String toMail = zebraForewarn.getEmail();
				if (StringUtils.isNotEmpty(toMail)
						&& zebraForewarn.isEmailWarn()
						&& !zebraForewarn.isSendMailState()) {
					Mail mail = new Mail();
					for (String mailContent : contents) {
						mail.setFromMail("yto.eccore@gmail.com");
						mail.setFromMailText("");
						mail.setSendToMail(toMail);
						mail.setSubject("电子面单预警");
						mail.setContent(mailContent);
						if (!mailService.sendMail(mail)) {
							logger.error("发送邮件失败");
						} else {
							logger.error("邮件发送成功!");
							// 设置邮件发送状态为已发送
							zebraForewarnDao.updateSendMailStateByCustomerCode(
									customerCode, 1);
						}
					}
				}
				// 发短信(处于预警状态，并且以前没有发送过短信，发送一次)
				List<String> desMobiles = new ArrayList<String>();
				String phone = zebraForewarn.getPhone();
				String remarkPhone = zebraForewarn.getRemarkPhone();
				if (StringUtils.isNotEmpty(phone)) {
					desMobiles.add(phone + "");
				}
				if (StringUtils.isNotEmpty(remarkPhone)) {
					desMobiles.add(remarkPhone + "");
				}
				if (!desMobiles.isEmpty() && zebraForewarn.isPhoneWarn()
						&& !zebraForewarn.isSendPhoneState()) {
					SMSObject sMSObject = new SMSObject();
					for (String smsContent : contents) {
						for (String destMobile : desMobiles) {
							sMSObject.setSequenceID(zebraForewarn
									.getForewarnId());
							sMSObject.setMessageContent(smsContent);
							// sMSObject.setDestMobile(accountUser.getCellPhone());
							sMSObject.setDestMobile(destMobile);
							sMSObject.setUserId(userId);
							sMSObject
									.setStatus(TechcenterEnum.TECHCENTERFLAG.WAIT
											.getValue());
							sMSObject.setSmsType(String
									.valueOf(PayEnumConstants.SERVICE.SMS
											.getValue()));
							if (!smsService.add(sMSObject)) {
								logger.error("发送短信失败");
							} else {
								// 设置短信发送状态为已发送
								zebraForewarnDao
										.updateSendPhoneStateByCustomerCode(
												customerCode, 1);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public int insertSequence(String customerCode, String parternCode) {
		ZebraSequence zebraSequence = new ZebraSequence();
		zebraSequence.setCustomerCode(customerCode);
		zebraSequence.setParternCode(parternCode);
		zebraSequence.setCreateTime(new Date());
		zebraSequenceDao.insertZebraSequence(zebraSequence);
		return zebraSequence.getSequenceId();
	}

	@Override
	public boolean validateSequence(String customerCode, String sequence) {
		String newSequence = zebraSurfacebillDao.validateSequence(customerCode,
				sequence);
		if (StringUtils.isNotEmpty(newSequence)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<String> selectUploadSurfacebillsBySenquence(String sequence) {
		return zebraSurfacebillDao
				.selectUploadSurfacebillsBySenquence(sequence);
	}

	@Override
	public int selectUsedCountsByCustomerCode(String customerCode) {
		return zebraSurfacebillDao.selectUsedCountsByCustomerCode(customerCode);
	}

	@Override
	public void removeUploadSurfacebillAndUpdateWarnHistoryValue(
			String customerCode, int deleteCounts, int customerWarnHistoryValue) {
		List<String> waybillNos = zebraSurfacebillDao
				.selectUploadSurceBillByCustomerCodeAndCounts(customerCode,
						deleteCounts);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("waybillNos", waybillNos);
		params.put("customerCode", customerCode);
		zebraSurfacebillDao.batchDelete(params);
		zebraForewarnDao.updateCustomerWarnHistoryValue(customerCode,
				customerWarnHistoryValue);
	}

	/**
	 * 提供仓配通获取面单接口
	 * 
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void synWaybill(HttpServletRequest request) {

		// 响应xml串
 		String responseXml = null;

		List<ZebraSurfacebill> waybills = new ArrayList<ZebraSurfacebill>();
		int newSequence = 0;

		try {
			// 请求参数校验
			Map<String, Object> params = new HashMap<String, Object>();
			if (!vaidateRequestParams(params, request)) {
				return;
			}

			// 请求参数变量定义
			String logisticsInterface = (String) params
					.get("logisticsInterface");
			String dataDigest = (String) params.get("dataDigest");
			String parternCode = (String) params.get("parternCode");

			Document document = DocumentHelper.parseText(logisticsInterface);
			Node customerCodeNode = document
					.selectSingleNode("/MailNoRequest/customerCode");
			Node sequenceNode = document
					.selectSingleNode("/MailNoRequest/sequence");
			// 商户代码
			String customerCode = customerCodeNode.getText();

			// 请求序列号
			String sequence = null;
			if (sequenceNode != null) {
				sequence = sequenceNode.getText();
			}

			// 验证数字签名
			boolean validateData = validateData(logisticsInterface,
					parternCode, dataDigest);
			if (!validateData) {
				logger.error("数字签名验证失败!");
				responseXml = "<MailNoResponse><success>false</success><message>数字签名验证失败!</message></MailNoResponse>";
				responseXml(responseXml);
				return;
			}

		
			// 给商家补面单
			if (StringUtils.isNotEmpty(customerCode)
					&& StringUtils.isNotEmpty(parternCode)) {
				// 查询商家的预警信息
				ZebraForewarn zebraForewarn = zebraForewarnDao
						.selectByCustomerCode(customerCode);				
				// 商家发送第一次请求，生成请求序列
				if (StringUtils.isEmpty(sequence)) {
					logger.error("synStep1,customerWarnValue:"
							+ zebraForewarn.getCustomerWarnValue()
							+ ",customerWarnHistoryValue:"
							+ zebraForewarn.getCustomerWarnHistoryValue());
					// 查询仓配通回传电子面单表，得到需要要补仓配通的电子面单数量
					int counts = 0;
					// 预警池没有完成初始化
					if (zebraForewarn != null && zebraForewarn.isInitState()) {
						// 已经下发的面单数
						int usedCounts = zebraSurfacebillDao
								.selectUsedCountsByCustomerCode(customerCode);
						// 商家预警值
						int warnValue = zebraForewarn.getCustomerWarnValue();
						counts = warnValue - usedCounts >= pageSize ? pageSize
								: warnValue - usedCounts;
						logger.error("synStep2, usedCounts:" + usedCounts
								+ ",warnValue:" + warnValue + ",counts:"
								+ counts);
					}
					// 预警池变大
					else if (zebraForewarn.getCustomerWarnValue() > zebraForewarn
							.getCustomerWarnHistoryValue()) {
						counts = zebraForewarn.getCustomerWarnValue()
								- zebraForewarn.getCustomerWarnHistoryValue();
						counts = counts >= pageSize ? pageSize : counts;
						logger.error("synStep3,counts:" + counts);
					}
					// 预警池变小
					else if (zebraForewarn.getCustomerWarnValue() < zebraForewarn
							.getCustomerWarnHistoryValue()) {
						counts = 0;
						logger.error("synStep4,counts:" + counts);
					}
					// 预警池补满并且预警池没有变大或者变小,走回传表
					else {
						counts = zebraSurfacebillDao
								.selectCountsByCustomerCode(customerCode);
						logger.error("synStep5,counts:" + counts);
					}

					// 预警池变小，不用下发面单
					if (zebraForewarn.getCustomerWarnValue() < zebraForewarn
							.getCustomerWarnHistoryValue()) {
						logger.error("synStep6,counts:" + counts);
						StringBuilder sb = new StringBuilder();
						sb.append("<MailNoResponse><success>true</success>");
						sb.append("<sequence>" + newSequence + "</sequence>");
						sb.append("<quantity>0</quantity><mailNoList></mailNoList>");
						sb.append("<message>");
						sb.append("你当前可用面单数：");
						sb.append(zebraForewarn.getCustomerWarnHistoryValue());
						sb.append(",网点给你设置的预警值为:");
						sb.append(zebraForewarn.getCustomerWarnValue());
						sb.append(",你的面单足够，故不下发面单！");
						sb.append("</message>");
						sb.append("</MailNoResponse>");
						responseXml(sb.toString());

						responseXml(responseXml);
						return;
					}

					// 查询电子面单表，得到易通实际上能补给仓配通的电子面单
					waybills = zebraSurfacebillDao
							.selectZebraSurfacebillsByCustomerCode(
									customerCode, counts, 0);
					logger.error("synStep7,waybills.size():" + waybills.size());
					// 生成请求序列
					ZebraSequence zebraSequence = new ZebraSequence();
					zebraSequence.setCustomerCode(customerCode);
					zebraSequence.setParternCode(parternCode);
					zebraSequence.setCreateTime(new Date());
					zebraSequenceDao
							.insertZebraSequence(zebraSequence);
					newSequence=zebraSequence.getSequenceId();
					if (newSequence <= 0) {
						logger.error("生成序列错误!");
						responseXml = "<MailNoResponse><success>false</success><message>服务器生成序列错误，请与管理员联系 QQ："
								+ ADMIN_QQ + "！</message></MailNoResponse>";
						responseXml(responseXml);
						return;
					} else {
						// 组装返回xml数据
						StringBuilder sb = new StringBuilder();
						sb.append("<MailNoResponse><success>true</success>");
						sb.append("<sequence>" + newSequence + "</sequence>");
						sb.append("<quantity>");
						sb.append(waybills.size());
						sb.append("</quantity><mailNoList>");
						for (ZebraSurfacebill waybill : waybills) {
							sb.append("<mailNo>");
							sb.append(waybill.getWaybillNo());
							sb.append("</mailNo>");
						}
						sb.append("</mailNoList></MailNoResponse>");
						responseXml(sb.toString());

						// 设置下发的电子面单的状态为某商家请求中，锁住这些电子面单，不让别的商家同步
						List<String> waybillNos = new ArrayList<String>();
						for (ZebraSurfacebill zebraSurfacebill : waybills) {
							waybillNos.add(zebraSurfacebill.getWaybillNo());
						}

						if (!waybillNos.isEmpty()) {
							params = new HashMap<String, Object>();
							params.put("waybillNos", waybillNos);
							params.put("state", 2);
							zebraSurfacebillDao.batchUpdateUseState(params);// 请求中
							logger.error("synStep8,waybillNos.size():"
									+ waybillNos.size());
						}
						return;
					}
				}
				// 商家发送第二次请求，确认同步面单是否成功,此时更新面单状态; 删除面单回传表相应的面单、更新预警历史值
				else {
					// 预警池变小，删除回传表相应数量的记录,并设置预警历史值
					if (zebraForewarn.getCustomerWarnValue() < zebraForewarn
							.getCustomerWarnHistoryValue()) {
						int uploadCounts = zebraSurfacebillDao
								.selectCountsByCustomerCode(customerCode);
						int deleteCounts = zebraForewarn
								.getCustomerWarnHistoryValue()
								- zebraForewarn.getCustomerWarnValue();
						if (deleteCounts > uploadCounts) {
							deleteCounts = uploadCounts;
						}
						logger.error("synStep9,deleteCounts:" + deleteCounts);
						if (deleteCounts > 0) {
							int customerWarnHistoryValue = zebraForewarn
									.getCustomerWarnHistoryValue()
									- deleteCounts;
							List<String> waybillNos = zebraSurfacebillDao
									.selectUploadSurceBillByCustomerCodeAndCounts(
											customerCode, deleteCounts);
							params = new HashMap<String, Object>();
							params.put("waybillNos", waybillNos);
							params.put("customerCode", customerCode);
							zebraSurfacebillDao.batchDelete(params);
							zebraForewarnDao.updateCustomerWarnHistoryValue(
									customerCode, customerWarnHistoryValue);
							logger.error("synStep10,deleteCounts:"
									+ deleteCounts);
						}

						responseXml = "<MailNoResponse><customerCode>"
								+ customerCode
								+ "</customerCode><sequence>"
								+ sequence
								+ "</sequence><success>true</success></MailNoResponse>";
						responseXml(responseXml);
						return;
					}

					// 批量更新请求中电子面单为下发状态
					waybills = zebraSurfacebillDao
							.selectZebraSurfacebillsByCustomerCode(
									customerCode, 0, 2);// 请求中的面单
					logger.error("synStep11,waybills:" + waybills.size());
					int rows1 = 0;
					List<String> waybillNos = new ArrayList<String>();
					for (ZebraSurfacebill zebraSurfacebill : waybills) {
						waybillNos.add(zebraSurfacebill.getWaybillNo());
					}
					// 请求序列为0,说明第一次请求完毕，但是第一次请求有问题
					if ("0".equals(sequence)) {
						if (!waybillNos.isEmpty()) {
							params = new HashMap<String, Object>();
							params.put("waybillNos", waybillNos);
							params.put("state", 0);
							rows1 = zebraSurfacebillDao
									.batchUpdateUseState(params);// 恢复未使用状态
						}
						logger.error("第一次请求有错误!, customerCode:" + customerCode);
						responseXml = "<MailNoResponse><success>false</success><message>第一次请求完毕，但是第一次请求有问题，请与管理员联系 QQ："
								+ ADMIN_QQ + "！</message></MailNoResponse>";
						responseXml(responseXml);
						return;
					} else {
						logger.error("第一次请求成功!, customerCode:" + customerCode
								+ ",sequence:" + sequence + ",面单数量:"
								+ waybillNos.size());
						// 验证请求序列号是否合法
						String validateSequence = zebraSurfacebillDao
								.validateSequence(customerCode, sequence);
						boolean isSuccess;
						if (StringUtils.isNotEmpty(validateSequence)) {
							isSuccess = true;
						} else {
							isSuccess = false;
						}
						if (!isSuccess) {
							// 商家获取数据失败，
							responseXml = "<MailNoResponse><success>false</success><message>请求序列号有错误, 请检查</message></MailNoResponse>";
							responseXml(responseXml);
							return;
						} else {
							logger.error("synStep12,waybills:"
									+ waybills.size());
							if (!waybills.isEmpty()) {
								// 更新请求中状态的电子面单的为已下发状态(is_use=2修改为is_use=1)
								params = new HashMap<String, Object>();
								params.put("waybillNos", waybillNos);
								params.put("state", 1);
								rows1 = zebraSurfacebillDao
										.batchUpdateUseState(params);// 1:已下发
								logger.error("synStep13,rows:" + rows1);

								/**
								 * 1:预警池没有完成初始化:<br>
								 * 第一次成功给仓配通某商家下发面单,填满缓冲池后， 设置为不是初始状态(即设置预警池填满)
								 * 2:预警池已经完成初始化：<br>
								 * 若预警值没有变化，走回传表;若预警值变大，不走回传表，只是更新预警历史值
								 * **/
								// 1:预警池没有完成初始化
								logger.error("synStep14,zebraForewarn.isInitState():"
										+ zebraForewarn.isInitState());
								if (zebraForewarn.isInitState()) {
									// 已经下发的面单数
									int usedCounts = zebraSurfacebillDao
											.selectUsedCountsByCustomerCode(customerCode);
									// 商家预警值
									int warnValue = zebraForewarn
											.getCustomerWarnValue();
									// 设置为不是初始状态(即预警池填满)
									if (zebraForewarn != null
											&& zebraForewarn.isInitState()
											&& usedCounts >= warnValue) {
										zebraForewarnDao
												.updateInitStateByCustomerCode(customerCode);
									}
									logger.error("synStep15,zebraForewarn.isInitState():"
											+ zebraForewarn.isInitState());
								}
								// 2:预警池已经完成初始化
								else {
									// 预警池变大，重新设置预警历史值
									if (zebraForewarn.getCustomerWarnValue() > zebraForewarn
											.getCustomerWarnHistoryValue()) {
										int customerWarnHistoryValue = zebraForewarn
												.getCustomerWarnHistoryValue()
												+ waybillNos.size();
										zebraForewarnDao
												.updateCustomerWarnHistoryValue(
														customerCode,
														customerWarnHistoryValue);
										logger.error("synStep16");
									}
									// 预警池预警值没有变化
									else {
										// 成功删除的回传电子面单(给商家下发多少条就删除多少条)
										int rows2 = 0;
										List<String> deleteWaybillNos = zebraSurfacebillDao
												.selectByCustomerCodeAndCounts(
														waybillNos.size(),
														customerCode);
										logger.error("synStep17,deleteWaybillNos:"
												+ deleteWaybillNos.size());
										if (!deleteWaybillNos.isEmpty()) {
											params = new HashMap<String, Object>();
											params.put("waybillNos", deleteWaybillNos);
											params.put("customerCode",
													customerCode);
											rows2 = zebraSurfacebillDao
													.batchDelete(params);
											logger.error("synStep18,rows2:"
													+ rows2);
										}
									}
								}

								// 电子面单告警检查
								checkForewarn(customerCode);
							}
							responseXml = "<MailNoResponse><customerCode>"
									+ customerCode
									+ "</customerCode><sequence>"
									+ sequence
									+ "</sequence><success>true</success></MailNoResponse>";
							responseXml(responseXml);
							return;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("同步单号error", e);
			responseXml = "<MailNoResponse><sequence>0</sequence><success>false</success><message>服务器内部错误，请与管理员联系 QQ："
					+ ADMIN_QQ + "！" + "</message></MailNoResponse>";
			responseXml(responseXml);
			return;
		}
	}

	/**
	 * 请求参数校验
	 * 
	 * @param params
	 * @return
	 * @throws IOException
	 */
	private boolean vaidateRequestParams(Map<String, Object> params,
			HttpServletRequest request) throws IOException {
		if (XmlSender.GET_REQUEST_METHOD.equals(request.getMethod())) {
			responseHtml(GET_METHOD_RESPONSE);
			return false;
		}

		String ip = null;
		if (request.getHeader("x-forwarded-for") == null) {
			ip = request.getRemoteAddr();
		} else {
			ip = request.getHeader("x-forwarded-for");
		}

		// 获取参数值
		// HttpServletRequest request = ServletActionContext.getRequest();
		String logisticsInterface = decode(
				request.getParameter(LOGISTICS_INTERFACE_PARAM), charset);
		String dataDigest = request.getParameter(DATA_DIGEST_PARAM);
		String clientID = request.getParameter(CLIENT_ID_PARAM);
		// 根据clientID从缓存中获取密钥
		String parternCode = Resource.getSecretId(clientID);

		if (logisticsInterface == null || dataDigest == null
				|| clientID == null) {
			String xml = "<MailNoResponse><success>false</success><message>请检查请求参数，请求参数不正确！</message></MailNoResponse>";
			responseXml(xml);
			return false;
		}

		/*
		 * boolean ipFlag = IpWhiteListProcessor.checkIp(clientID, ip); if
		 * (!ipFlag) { logger.error("非法用户访问！ip：" + ip + "  clientId:" +
		 * clientID); String xml =
		 * "<MailNoResponse><success>false</success><message>您好，此IP为非法IP，如有疑问请与管理员联系 QQ："
		 * + ADMIN_QQ + "！</message></MailNoResponse>"; responseXml(xml);
		 * return; }
		 */
		logger.error("logisticsInterface-->" + logisticsInterface);
		logger.error("dataDigest-->" + dataDigest);
		logger.error("clientID-->" + clientID);
		logger.error("parternCode-->" + parternCode);
		// logger.debug("ip:" + ip);

		params.put("logisticsInterface", logisticsInterface);
		params.put("dataDigest", dataDigest);
		params.put("clientID", clientID);
		params.put("parternCode", parternCode);
		// params.put("ip", ip);

		return true;
	}

	/**
	 * 验证数字签名
	 * 
	 * @param logisticsInterface
	 * @param parternCode
	 * @param dataDigest
	 * @return
	 */
	private static boolean validateData(String logisticsInterface,
			String parternCode, String dataDigest) {
		String newDataDigest = Md5Encryption.MD5Encode(logisticsInterface
				+ parternCode, charset);
		logger.debug(newDataDigest);
		logger.debug(dataDigest);
		System.out.println("newDataDigest:" + newDataDigest);
		System.out.println("dataDigest:" + dataDigest);
		if (dataDigest.equals(newDataDigest)) {
			return true;
		}
		return false;
	}

	/**
	 * @param arg
	 *            http请求参数
	 * @param charset
	 *            字符集
	 * @return decode后的参数
	 */
	private static String decode(String arg, String charset) {
		try {
			return java.net.URLDecoder.decode(arg, charset);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	private String toJson(Object obj) {
		String json = "密钥或者客户代码有问题!";

		ObjectMapper mapper = new ObjectMapper();
		StringWriter sw = new StringWriter();
		try {
			if (obj != null) {
				mapper.writeValue(sw, obj);
				json = sw.toString();
			}
		} catch (JsonGenerationException e) {
			logger.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return json;
	}

	private Object toObject(String json, Class clazz) {
		Object obj = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (StringUtils.isNotEmpty(json)) {
				obj = mapper.readValue(json, clazz);
			}
		} catch (JsonGenerationException e) {
			logger.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return obj;
	}

	private void response(String responseData, int type) {
		String contentType = "application/xml";
		if (type == 1) {// xml
			contentType = "application/xml";
		} else if (type == 2) {// json
			contentType = "application/json";
		} else if (type == 3) {// html
			contentType = "text/html";
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType(contentType + ";charset=UTF-8");// 解决中文乱码
		try {
			PrintWriter out = response.getWriter();
			out.println(responseData);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void responseJson(String json) {
		response(json, 2);
	}

	private void responseXml(String xml) {
		response(xml, 1);
	}

	private void responseHtml(String html) {
		response(html, 3);
	}

}
