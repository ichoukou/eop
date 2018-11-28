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
import javax.xml.bind.JAXBException;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.common.Constant;
import net.ytoec.kernel.common.PayEnumConstants;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.common.TechcenterEnum;
import net.ytoec.kernel.common.WebServiceUtils;
import net.ytoec.kernel.dao.ApiLogDao;
import net.ytoec.kernel.dao.SellerInfoDao;
import net.ytoec.kernel.dao.SyncLockDao;
import net.ytoec.kernel.dao.ZebraForewarnDao;
import net.ytoec.kernel.dao.ZebraParternDao;
import net.ytoec.kernel.dao.ZebraSequenceDao;
import net.ytoec.kernel.dao.ZebraSurfacebillDao;
import net.ytoec.kernel.dataobject.ApiLog;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.dataobject.MailNo;
import net.ytoec.kernel.dataobject.MailNoInfo;
import net.ytoec.kernel.dataobject.MailNoRequest;
import net.ytoec.kernel.dataobject.MailNoResponse;
import net.ytoec.kernel.dataobject.OrderInfoRequest;
import net.ytoec.kernel.dataobject.RequestInfo;
import net.ytoec.kernel.dataobject.ResponseInfo;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.dataobject.SyncLock;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.WaybillNo;
import net.ytoec.kernel.dataobject.ZebraForewarn;
import net.ytoec.kernel.dataobject.ZebraPartern;
import net.ytoec.kernel.dataobject.ZebraSequence;
import net.ytoec.kernel.dataobject.ZebraSurfacebill;
import net.ytoec.kernel.exception.BusinessException;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.SMSObjectService;
import net.ytoec.kernel.service.ZebraSurfacebillService;
import net.ytoec.kernel.util.DateUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sun.mail.util.ValidationTool;
import com.sun.mail.util.XmlJaxbMapper;
import com.yto.coresystem.module.matbranch.domain.MaterialDzDetail;

@Service
public class ZebraSurfacebillServiceImpl implements ZebraSurfacebillService {

	private static Logger logger = LoggerFactory
			.getLogger(ZebraSurfacebillServiceImpl.class);

	// 合并信息用Set集合去存放不会有重复记录
	Set<String> setMessage = null;

	// 定义存储XML订单信息的集合
	List<OrderInfoRequest> orderInfoRequestList = null;

	@Inject
	private ZebraForewarnDao<ZebraForewarn> zebraForewarnDao;

	@Value("${orderSyncJgUrl}")
	private String orderSyncJgUrl;

	@Value("${synWaybilIntervalTime}")
	private String synWaybilIntervalTime;
	
	@Value("${maxWayBillSize}")
	private int maxWayBillSize;
	
	@Value("${parternCode}")
	private String jgParentCode;
	
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
	
	@Inject
	private ApiLogDao<ApiLog> apiLogDao;
	
	@Inject
	private SyncLockDao syncLockDao;
	
	private static final String sdfString = "yyyy-MM-dd HH:mm:ss";
	private static final DateFormat sdf = new SimpleDateFormat(sdfString);

	/**
	 * @作者：罗典
	 * @时间：2013-09-25
	 * @描述：易通提供金刚系统用来获取金刚面单数据
	 * @参数：http用户请求消息
	 * @返回：响应消息 
	 * */
	@Override
	@Transactional
	public void receiveWaybill(HttpServletRequest request) throws Exception{
		// 默认响应消息为成功
		String responseContent = Constant.RESPONSE_SUCCESS;
		// 默认响应格式为xml格式
		String responseType = Constant.CONTENT_TYPE_XML;
		// 接口日志
		ApiLog apiLog = new ApiLog();
		apiLog.setLogType(Constant.LOG_TYPE_RECEIVEWAYBILL);
		// 记录当前时间
		Long startTime = System.currentTimeMillis();
		// 响应消息
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setSuccess(false);
		try{
			// method请求时直接返回success
			if (XmlSender.GET_REQUEST_METHOD.equals(request.getMethod())) {
				responseType = Constant.CONTENT_TYPE_TEXT;
			}else {
				String ip = request.getHeader("x-forwarded-for") ;
				if (ip == null) {
					ip = request.getRemoteAddr();
				}
				apiLog.setRequestIP(ip);
				// 参数非空校验
				String logisticsInterface = ValidationTool.validateString(
						request.getParameter(Constant.LOGISTICS_INTERFACE),
						Constant.LOGISTICS_INTERFACE);
				String dataDigest = ValidationTool.validateString(
						request.getParameter(Constant.DATA_DIGEST),Constant.DATA_DIGEST);
				ValidationTool.validateString(jgParentCode,"jgParentCode");
				// 数字签名校验
				validateDataDigest(logisticsInterface,	jgParentCode, dataDigest);
				// 接口请求参数xml-对象转换,及基本数据校验
				RequestInfo requestInfo = (RequestInfo) XmlJaxbMapper.readValue(
						logisticsInterface, RequestInfo.class);
				ValidationTool.validateObject(requestInfo, "requestInfo");
				ValidationTool.validateList(requestInfo.getMailNoList(), "requestInfo.getMailNoList()");
				apiLog.setRequestMsg(logisticsInterface+dataDigest+jgParentCode);
				
				// 批量保存面单数据(出错后单个面单地保存并记录出错原因)
				try{
					// 批量插入
					zebraSurfacebillDao.batchInsert(requestInfo.getMailNoList());
				}catch(DuplicateKeyException e){
					responseInfo.setFailCode(Constant.CODE_PART_EXCPETION);
					responseInfo.setFailReason(Constant.MSG_REPEATED_MAILNO);
					List<MailNoInfo> mailNoInfos = new ArrayList<MailNoInfo>();
					mailNoInfos.addAll(requestInfo.getMailNoList());
					for(MailNoInfo mailNoInfo : requestInfo.getMailNoList()){
						try{
							// 单条插入
							zebraSurfacebillDao.insertSurfaceBill(mailNoInfo);
						}catch(DuplicateKeyException ex){
							WaybillNo waybillNo = new WaybillNo();
							waybillNo.setWaybillNo(mailNoInfo.getWaybillNo());
							waybillNo.setReason(Constant.MSG_REPEATED_MAILNO);
							waybillNo.setResultCode(Constant.CODE_REPEATED_EXCPETION);
							responseInfo.getErrorWaybillNos().add(waybillNo);
							mailNoInfos.remove(mailNoInfo);
						}
					}
				}
				
				// 对客户预警信息进行处理
				List<String> customerCodelist = new ArrayList<String>();
				for(MailNoInfo mailNoInfo : requestInfo.getMailNoList()){
					if(!customerCodelist.contains(mailNoInfo.getCustomerCode())){
						customerCodelist.add(mailNoInfo.getCustomerCode());
					}
				}
				if(customerCodelist.size() > 0){
					List<ZebraForewarn> zebraForewarns = 
						zebraForewarnDao.queryZebraForewarn(customerCodelist);
					for(ZebraForewarn zebraForewarn : zebraForewarns){
						if(zebraForewarn.isSendMailState()||
							zebraForewarn.isSendPhoneState()||
							zebraForewarn.isWarnState()){
							// 商家的电子面单库存数量(is_use=0)
							int count = zebraSurfacebillDao.selectCounts(zebraForewarn.getCustomerCode());
							 if(zebraForewarn.getBranckWarnValue() <= count){
								 zebraForewarn.setWarnState(false);
								 zebraForewarn.setSendMailState(false);
								 zebraForewarn.setSendPhoneState(false);
								 zebraForewarnDao.updateZebraForewarn(zebraForewarn);
							 }
						}
					}
				}
				if(responseInfo.getErrorWaybillNos() == null || responseInfo.getErrorWaybillNos().size() <= 0){
					responseInfo.setSuccess(true);
				}
			}
		}catch(JAXBException e){
			// xml数据解析出错
			e.printStackTrace();
			apiLog.setException(true);
			apiLog.setExceptionMsg(e.getMessage());
			responseInfo.setFailReason(Constant.MSG_CONVERTION_FAIL);
			responseInfo.setFailCode(Constant.CODE_UNKOWN_EXCEPTION);
			throw e;
		}catch(BusinessException e){
			// 业务异常(数据校验等)
			e.printStackTrace();
			apiLog.setException(true);
			apiLog.setExceptionMsg(e.getLocalizedMessage());
			responseInfo.setFailReason(e.getLocalizedMessage());
			responseInfo.setFailCode(Constant.CODE_UNKOWN_EXCEPTION);
			throw e;
		}catch(Exception e){
			// 未知异常
			e.printStackTrace();
			apiLog.setException(true);
			apiLog.setExceptionMsg(e.getMessage());
			responseInfo.setFailReason(Constant.MSG_UNKONWN_EXCEPTION);
			responseInfo.setFailCode(Constant.CODE_UNKOWN_EXCEPTION);
			responseInfo.setErrorWaybillNos(null);
			throw e;
		}finally{
			if(!Constant.CONTENT_TYPE_TEXT.equals(responseType)){
				// 转换对象为xml格式字符串
				responseContent = XmlJaxbMapper.writeValue(responseInfo,ResponseInfo.QNAME);
			}
			// 记录当前时间
			Long endTime = System.currentTimeMillis();
			apiLog.setResponseMsg(responseContent);
			apiLog.setUsedtime(endTime-startTime);
			apiLogDao.insertApiLog(apiLog);
			// 返回消息至卖家
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType(responseType + ";charset=UTF-8");// 解决中文乱码
			
			PrintWriter out = response.getWriter();
			out.println(responseContent);
			out.flush();
			out.close();
		}
	
	}
	
	/**
	 * @作者：罗典
	 * @时间：2013-08-28
	 * @描述：易通提供给仓配通等第三方商家获取面单接口/处理结果反馈接口
	 * @参数：http用户请求消息
	 * @返回：响应消息 
	 * */
	@Override
	@Transactional
	public void synWaybill(HttpServletRequest request) throws Exception{
		// 默认响应消息为成功
		String responseInfo = Constant.RESPONSE_SUCCESS;
		// 默认响应格式为xml格式
		String responseType = Constant.CONTENT_TYPE_XML;
		// 接口日志
		ApiLog apiLog = new ApiLog();
		apiLog.setLogType(Constant.LOG_TYPE_SYNWAYBILL);
		// 记录当前时间
		Long startTime = System.currentTimeMillis();
		// 响应消息
		MailNoResponse mailNoResponse =new MailNoResponse();
		mailNoResponse.setSuccess(false);
		// 控制并发锁
		SyncLock lock = new SyncLock();
		lock.setMethod(Constant.LOG_TYPE_SYNWAYBILL);
		lock.setLock(true);
		// 是否需要解锁(默认为不需要)
		boolean isNeedUnlock = false;
		try{
			// method请求时直接返回success
			if (XmlSender.GET_REQUEST_METHOD.equals(request.getMethod())) {
				responseType = Constant.CONTENT_TYPE_TEXT;
			}else {
				String ip = request.getHeader("x-forwarded-for") ;
				if (ip == null) {
					ip = request.getRemoteAddr();
				}
				apiLog.setRequestIP(ip);
				// 参数非空校验
				String logisticsInterface = decode(ValidationTool.validateString(
						request.getParameter(Constant.LOGISTICS_INTERFACE),Constant.LOGISTICS_INTERFACE),Constant.CHARSET_UTF8);
				String dataDigest = ValidationTool.validateString(request.getParameter(Constant.DATA_DIGEST),Constant.DATA_DIGEST);
				String clientID = ValidationTool.validateString(request.getParameter(Constant.CLIENT_ID),Constant.CLIENT_ID);
				String parternCode = ValidationTool.validateString(Resource.getSecretId(clientID),"parternCode");
				// 数字签名校验
				validateDataDigest(logisticsInterface,	parternCode, dataDigest);
				// 接口请求参数xml-对象转换,及基本数据校验
				MailNoRequest mailNoRequest = (MailNoRequest) XmlJaxbMapper.readValue(logisticsInterface, MailNoRequest.class);
				ValidationTool.validateObject(mailNoRequest, "mailNoRequest");
				ValidationTool.validateString(mailNoRequest.getCustomerCode(), "mailNoRequest.getCustomerCode()");
				apiLog.setRequestMsg(logisticsInterface+dataDigest+clientID);
				
				// 控制并发
				lock.setBusinessId(mailNoRequest.getCustomerCode());
				if(!syncLockDao.isExistsSyncLock(lock)){
					syncLockDao.insertSyncLock(lock);
				}else{
					if(syncLockDao.updateByLockState(lock) <= 0){
						throw new BusinessException(Constant.MSG_FAST_REQUEST);
					}
				}
				isNeedUnlock = true;
				
				//判断是来拉取面单还是反馈状态, sequence为空则为拉取面单，反之则为反馈状态
				if(mailNoRequest.getSequence() == null || "".equals(mailNoRequest.getSequence())){
					// 查询预警值信息
					ZebraForewarn zebraForewarn = zebraForewarnDao.selectByCustomerCode(mailNoRequest.getCustomerCode());
					ValidationTool.validateObject(zebraForewarn, "zebraForewarn");
					// 需下发面单数默认为预警值与历史值间的差值
					int needMailCount = zebraForewarn.getCustomerWarnValue() - zebraForewarn.getCustomerWarnHistoryValue();
					
					// 预警值变小
					if(needMailCount < 0 ){
						// 删除差值数量的回传单表数据
						int deleteCount = zebraSurfacebillDao.deleteUploadBillByLimit(mailNoRequest.getCustomerCode(), Math.abs(needMailCount));
						// 修改预警历史值，使其与预警值保持一致
						if(deleteCount > 0){
							zebraForewarnDao.updateCustomerWarnHistoryValue(mailNoRequest.getCustomerCode(), 
									zebraForewarn.getCustomerWarnHistoryValue()-deleteCount);
						}
						throw new BusinessException(Constant.MSG_ENOUGH_WORNG);
					}
					// 预警值不变
					else if(needMailCount == 0){
						// 查询回传单数量
						needMailCount = zebraSurfacebillDao.selectCountsByCustomerCode(mailNoRequest.getCustomerCode());
						if(needMailCount == 0){
							throw new BusinessException(Constant.MSG_NOTENOUGH_ORDER);
						}
					}
					//不可超过预设值
					needMailCount = needMailCount > maxWayBillSize ? maxWayBillSize : needMailCount ;
					// 取出面单号集合
					List<String> waybills = zebraSurfacebillDao.querySurfaceBillListByLimit(mailNoRequest.getCustomerCode(), needMailCount);
					if(waybills == null || waybills.size() <= 0){
						throw new BusinessException(Constant.MSG_NOTENOUGH_MAIL);
					}
					// // 预警值变大
					if(zebraForewarn.getCustomerWarnValue() > zebraForewarn.getCustomerWarnHistoryValue()){
						// 修改预警历史值
						zebraForewarnDao.updateCustomerWarnHistoryValue(mailNoRequest.getCustomerCode(),
								zebraForewarn.getCustomerWarnHistoryValue()+waybills.size());
					}
					// 预警值不变
					else{
						// 删除差值数量的回传单表数据
						zebraSurfacebillDao.deleteUploadBillByLimit(mailNoRequest.getCustomerCode(), waybills.size());
					}
					
					// 新增一条sequence记录，用于标示某次请求生命周期
					ZebraSequence zebraSequence = new ZebraSequence();
					zebraSequence.setCustomerCode(mailNoRequest.getCustomerCode());
					zebraSequence.setParternCode(parternCode);
					zebraSequence.setCreateTime(new Date());
					zebraSequence.setState(Constant.SEQ_STATE_PROCESS);
					zebraSequence.setTotalCount(needMailCount);
					zebraSequenceDao.insertZebraSequence(zebraSequence);
					// 修改面单状态
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("waybillNos", waybills);
					map.put("state", 2);
					map.put("sequence", zebraSequence.getSequenceId());
					zebraSurfacebillDao.batchUpdateUseState(map);
					// 返回消息给卖家
					MailNo mailNo = new MailNo();
					mailNo.setMailNo(waybills);
					mailNoResponse.setMailNoList(mailNo);
					mailNoResponse.setQuantity(waybills.size());
					mailNoResponse.setSequence(zebraSequence.getSequenceId());
					apiLog.setBusinessId(zebraSequence.getSequenceId().toString());
					// 处理超时时，程序回滚(120秒)
					if(System.currentTimeMillis()-startTime > Constant.TIMEOUT_TIME){
						throw new BusinessException(Constant.MSG_TIMEOUT_WRONG);
					}
				}
				// 反馈状态
				else{
					// 需修改序列信息的状态
					int sequenceState = Constant.SEQ_STATE_SUCCESS;
					// 需修改面单号集合的状态
					int mailNoState = Constant.MAIL_STATE_USED;
					
					// 校验序列信息是否有效
					ZebraSequence sequence  = zebraSequenceDao.queryZebraSequence(mailNoRequest.getSequence());
					if(sequence == null){
						throw new BusinessException(Constant.MSG_SEQ_WRONG + mailNoRequest.getSequence());
					}
					apiLog.setBusinessId(mailNoRequest.getSequence());
					ValidationTool.validateObject(mailNoRequest.getSuccess(), "mailNoRequest.isSuccess()");
					// 上次请求失败
					if(! mailNoRequest.getSuccess()){
						// 补齐一定数量的回传单数据(备注： 引用之前的批量插入方法，有一定的可优化空间)
						List<Map<String,String>> list = new ArrayList<Map<String,String>>();
						for(int i = 0 ;i <sequence.getTotalCount();i++ ){
							// 自动生成一个编号作为面单号，防止唯一建
							String incrementNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
							Thread.sleep(10l);
							Map<String, String> map = new HashMap<String, String>();
							map.put("waybillNo", incrementNo);
							map.put("orderNo", incrementNo);
							map.put("customerCode", mailNoRequest.getCustomerCode());
							list.add(map);
						}
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("waybillNos", list);
						params.put("customerCode", mailNoRequest.getCustomerCode());
						zebraSurfacebillDao.batchInsert(params);
						sequenceState = Constant.SEQ_STATE_FAIL;
						mailNoState = Constant.MAIL_STATE_UNUSERD;
					}
					// 更新面单状态
					zebraSurfacebillDao.updateStateBySequence(mailNoRequest.getSequence(),mailNoState);
					// 更新序列状态
					zebraSequenceDao.updateStateById(mailNoRequest.getSequence(),sequenceState);
				}
				// 电子面单告警检查
//				checkForewarn( mailNoRequest.getCustomerCode());
				mailNoResponse.setSuccess(true);
			}
		}catch(JAXBException e){
			// xml数据解析出错
			e.printStackTrace();
			apiLog.setException(true);
			apiLog.setExceptionMsg(e.getMessage());
			mailNoResponse.setMessage(Constant.MSG_CONVERTION_FAIL);
			throw e;
		}catch(BusinessException e){
			// 业务异常(数据校验等)
			e.printStackTrace();
			apiLog.setException(true);
			apiLog.setExceptionMsg(e.getLocalizedMessage());
			mailNoResponse.setMessage(e.getLocalizedMessage());
			throw e;
		}catch(Exception e){
			// 未知异常
			e.printStackTrace();
			apiLog.setException(true);
			apiLog.setExceptionMsg(e.getMessage());
			mailNoResponse.setMailNoList(null);
			mailNoResponse.setQuantity(0);
			mailNoResponse.setSequence(null);
			mailNoResponse.setMessage(Constant.MSG_UNKONWN_EXCEPTION);
			throw e;
		}finally{
			try {
				if(!Constant.CONTENT_TYPE_TEXT.equals(responseType)){
					mailNoResponse.setSuccess(!apiLog.isException());
					// 转换对象为xml格式字符串
					responseInfo = XmlJaxbMapper.writeValue(mailNoResponse,MailNoResponse.QNAME);
					// 接口文档描述中无xml title消息，此处去除title消息
					responseInfo = responseInfo.replace(Constant.XML_TITLE, "");
				}
				// 记录当前时间
				Long endTime = System.currentTimeMillis();
				apiLog.setResponseMsg(responseInfo);
				apiLog.setUsedtime(endTime-startTime);
				apiLogDao.insertApiLog(apiLog);
				// 解除锁定
				if(isNeedUnlock){
					lock.setLock(false);
					syncLockDao.updateByLockState(lock);
				}
				// 返回消息至卖家
				HttpServletResponse response = ServletActionContext.getResponse();
				response.setContentType(responseType + ";charset=UTF-8");// 解决中文乱码
				
				PrintWriter out = response.getWriter();
				out.println(responseInfo);
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			
		}
	}
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
				// if ("K21000844".equals(zebraPartern.getCustomerCode())) {//
				// 测试
				syncWaybillNo(zebraPartern);
				// }
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
					waybill.setCustomerCode(customerCode);
					waybill.setVersionNo(ac.getVersionNo().toString());

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
	public int addBatch(List<Map<String, String>> waybillNos,
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
	 * @param zebraForewarn
	 *            商家预警
	 */
	private void checkForewarn(String customerCode) {
		ZebraForewarn zebraForewarn = zebraForewarnDao.selectByCustomerCode(customerCode);
		// 1:查询商家未下发状态的电子面单数量
		int noUseSum = zebraSurfacebillDao.selectCountsByState(customerCode, 0);
		// 2:根据customerCode(即:username)查询用户id(id)
		String siteId = sellerInfoDao
				.findSiteCodeByUserCodeFromUT(customerCode);
		int userId = Resource.getUserBySiteCode(siteId).getId();
		// 3:预警判断
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

				// 设置预警状态为预警(0:正常状态,1:预警状态)
				zebraForewarnDao.updateWarnStateByCustomerCodeAndWarnState(
						customerCode, 1);
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
	 * 提供仓配通/第三方商家获取面单接口
	 * 
	 * @throws Exception
	 */
//	@Override
	@Transactional
	public void synWaybillHistory(HttpServletRequest request) {

		// 响应xml串
		String responseXml = null;

		List<ZebraSurfacebill> waybills = new ArrayList<ZebraSurfacebill>();
		boolean isSuccess = false;

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

			/**
			 * 本次同步时间和最近一次状态为第一次请求中的同步请求的时间间隔<time分钟,<br>
			 * 不予处理，直接返回，提示请求过快!
			 */
			String lastSyningTime = zebraSequenceDao
					.selectLastSyningTime(customerCode);
			Date date = DateUtil.valueof(lastSyningTime, sdfString);
			if (date != null) {
				double intervalTime = DateUtil.minuteInterval(new Date(), date);
				if (intervalTime < Integer.valueOf(synWaybilIntervalTime)) {
					logger.error("请求过快,请稍后再请求!");
					responseXml = "<MailNoResponse><success>false</success><message>请求过快,请稍后再请求!</message></MailNoResponse>";
					responseXml(responseXml);
					return;
				}
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
				if (zebraForewarn == null) {
					logger.error("网点没有设置预警!");
					responseXml = "<MailNoResponse><success>false</success><message>网点没有设置预警，请让网点设置预警!</message></MailNoResponse>";
					responseXml(responseXml);
					return;
				}
				// 商家发送第一次请求，生成请求序列
				if (StringUtils.isEmpty(sequence)) {
					logger.error("synStep1,customerWarnValue:"
							+ zebraForewarn.getCustomerWarnValue()
							+ ",customerWarnHistoryValue:"
							+ zebraForewarn.getCustomerWarnHistoryValue());
					int newSequence = 0;
					// 查询仓配通回传电子面单表，得到需要要补仓配通的电子面单数量
					int counts = 0;
					// 预警池没有完成初始化
					if (zebraForewarn != null && zebraForewarn.isInitState()) {
						// 已经下发的面单数
						int usedCounts = zebraSurfacebillDao
								.selectUsedCountsByCustomerCode(customerCode);
						// 商家预警值
						int warnValue = zebraForewarn.getCustomerWarnValue();
						counts = warnValue - usedCounts >= maxWayBillSize ? maxWayBillSize
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
						counts = counts >= maxWayBillSize ? maxWayBillSize : counts;
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
						counts = counts >= maxWayBillSize ? maxWayBillSize : counts;
						logger.error("synStep5,counts:" + counts);
					}

					// 生成请求序列
					ZebraSequence zebraSequence = new ZebraSequence();
					zebraSequence.setCustomerCode(customerCode);
					zebraSequence.setParternCode(parternCode);
					zebraSequence.setCreateTime(new Date());
					zebraSequenceDao.insertZebraSequence(zebraSequence);
					newSequence = zebraSequence.getSequenceId();
					if (newSequence <= 0) {
						logger.error("生成序列错误!");
						responseXml = "<MailNoResponse><success>false</success><message>服务器生成序列错误，请与管理员联系 QQ："
								+ Constant.ADMIN_QQ + "！</message></MailNoResponse>";
						responseXml(responseXml);
						return;
					} else {
						// 预警池变小，不用下发面单
						if (zebraForewarn.getCustomerWarnValue() < zebraForewarn
								.getCustomerWarnHistoryValue()) {
							logger.error("synStep6,counts:" + counts);
							StringBuilder sb = new StringBuilder();
							sb.append("<MailNoResponse><success>true</success>");
							sb.append("<sequence>" + newSequence
									+ "</sequence>");
							sb.append("<quantity>0</quantity><mailNoList></mailNoList>");
							sb.append("<message>");
							sb.append("你当前可用面单数：");
							sb.append(zebraForewarn
									.getCustomerWarnHistoryValue());
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
						logger.error("synStep7,waybills.size():"
								+ waybills.size());

						// 设置面单同步请求序列表的同步状态为第一次请求中
						isSuccess = zebraSequenceDao.updateState(newSequence,
								customerCode, 1);// state=1:第一次请求中
						if (!isSuccess) {
							logger.error("设置面单同步状态为第一次请求中状态出错!");
							responseXml = "<MailNoResponse><success>false</success><message>设置面单同步状态为第一次请求中成功状态出错，请与管理员联系 QQ："
									+ Constant.ADMIN_QQ + "！</message></MailNoResponse>";
							responseXml(responseXml);
							return;
						}

						// 设置下发的电子面单的状态为某商家请求中，锁住这些电子面单，不让别的商家同步
						List<String> waybillNos = new ArrayList<String>();
						for (ZebraSurfacebill zebraSurfacebill : waybills) {
							waybillNos.add(zebraSurfacebill.getWaybillNo());
						}
						int rows = 0;
						if (!waybillNos.isEmpty()) {
							params = new HashMap<String, Object>();
							params.put("waybillNos", waybillNos);
							params.put("state", 2);
							params.put("sequence", newSequence);
							rows = zebraSurfacebillDao
									.batchUpdateUseState(params);// 请求中
							logger.error("synStep8,waybillNos.size():"
									+ waybillNos.size());
						}

						// 设置面单同步请求序列表的同步状态为第一次请求成功
						if (rows == waybillNos.size()) {
							// 设置面单同步状态为第一次请求成功
							isSuccess = zebraSequenceDao.updateState(
									newSequence, customerCode, 2);// state=2:第一次请求成功
							if (!isSuccess) {
								logger.error("设置面单同步状态为第一次请求成功状态出错!");
								responseXml = "<MailNoResponse><success>false</success><message>设置面单同步状态为第一次请求成功状态出错，请与管理员联系 QQ："
										+ Constant.ADMIN_QQ
										+ "！</message></MailNoResponse>";
								responseXml(responseXml);
								return;
							}
						}

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

						return;
					}
				}
				// 商家发送第二次请求，确认同步面单是否成功,此时更新面单状态; 删除面单回传表相应的面单、更新预警历史值
				else {
					if (!"0".equals(sequence)) {
						// 设置该请求序列的第二次请求时间
						isSuccess = zebraSequenceDao.updateSecondCreateTime(
								sequence, customerCode);
						if (!isSuccess) {
							logger.error("设置该请求序列的第二次请求时间出错!");
							responseXml = "<MailNoResponse><success>false</success><message>设置该请求序列的第二次请求时间出错，请与管理员联系 QQ："
									+ Constant.ADMIN_QQ + "！</message></MailNoResponse>";
							responseXml(responseXml);
							return;
						}
					}

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
							params.put("sequence", 0);
							rows1 = zebraSurfacebillDao
									.batchUpdateUseState(params);// 恢复未使用状态
						}
						logger.error("第一次请求有错误!, customerCode:" + customerCode);
						responseXml = "<MailNoResponse><success>false</success><message>第一次请求完毕，但是第一次请求有问题，请与管理员联系 QQ："
								+ Constant.ADMIN_QQ + "！</message></MailNoResponse>";
						responseXml(responseXml);
						return;
					} else {
						logger.error("第一次请求成功!, customerCode:" + customerCode
								+ ",sequence:" + sequence + ",面单数量:"
								+ waybillNos.size());
						// 验证请求序列号是否合法
						String validateSequence = zebraSurfacebillDao
								.validateSequence(customerCode, sequence);
						if (StringUtils.isEmpty(validateSequence)) {// 请求序列号不合法
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
								params.put("sequence", sequence);
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
											params.put("waybillNos",
													deleteWaybillNos);
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
					+ Constant.ADMIN_QQ + "！" + "</message></MailNoResponse>";
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
			responseHtml(Constant.RESPONSE_SUCCESS);
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
				request.getParameter(Constant.LOGISTICS_INTERFACE), Constant.CHARSET_UTF8);
		String dataDigest = request.getParameter(Constant.DATA_DIGEST);
		String clientID = request.getParameter(Constant.CLIENT_ID);
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
		 * + Constant.ADMIN_QQ + "！</message></MailNoResponse>"; responseXml(xml);
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
				+ parternCode, Constant.CHARSET_UTF8);
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
	public static void main(String[] args) {
		try{
			throw new BusinessException("------asdfafd");
		}catch(BusinessException e){
			System.out.println(e.getLocalizedMessage());
		}
	}
	
	/**
	 * @作者：罗典
	 * @时间：2013-08-29
	 * @描述：数字签名校验
	 * @参数：arg 需校验的字符串；argName 对象的名字，在提示异常信息时使用
	 * @返回：校验通过后的原值信息
	 * */
	public static boolean validateDataDigest(String logisticsInterface,String parternCode,String dataDigest) throws BusinessException{
		String newDataDigest = Md5Encryption.MD5Encode(logisticsInterface
				+ parternCode, Constant.CHARSET_UTF8);
		if (!dataDigest.equals(newDataDigest)) {
			throw new BusinessException(Constant.MSG_DATADIGEST_FAIL);
		}
		return false;
	}
}
