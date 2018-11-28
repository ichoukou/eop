/**
 * 
 */
package net.ytoec.kernel.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.Constants;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.QuestionaireExchange;
import net.ytoec.kernel.dataobject.QuestionaireRemark;
import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.dataobject.QuestionnaireDeal;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.dto.DtoQuestion;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.QuestionDtoService;
import net.ytoec.kernel.service.QuestionRemarkService;
import net.ytoec.kernel.service.QuestionaireExchangeService;
import net.ytoec.kernel.service.QuestionnaireService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.service.UserThreadService;
import net.ytoec.kernel.util.ConfigUtilSingle;
import net.ytoec.kernel.util.DateUtil;
import net.ytoec.kernel.util.JDBCUtilSingle;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 问题单service接口实现
 * 
 * @author Wangyong
 * @2012-1-31 net.ytoec.kernel.service.impl
 */
@Service
@Transactional
public class QuestionDtoServiceImpl<T extends DtoQuestion> implements
		QuestionDtoService<T> {

	private static Logger logger = LoggerFactory.getLogger(QuestionDtoServiceImpl.class);

	@Inject
	private QuestionnaireService<Questionnaire> questionnaireService;
	@Inject
	private OrderService<Order> orderService;
	@Inject
	private UserService<User> userService;
	@Inject
	private QuestionaireExchangeService<QuestionaireExchange> questionaireExchangeService;
	@Inject
	private QuestionRemarkService<QuestionaireRemark> questionaireRemarkService;
	@Inject
	private UserThreadService<UserThread> userThreadService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.ytoec.kernel.service.QuestionDtoService#siteGetQuestionList(java.
	 * lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.Integer, net.ytoec.kernel.common.Pagination)
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	@Override
	public List<T> siteGetQuestionList(User siteUser, String startTime,
			String endTime, String userCode, String mailNO, String issueStatus,
			List<String> feedbackInfo, String dealStatus, String wdIsRead,
			Integer sortType, String contactWay, String buyerName,
			Integer isShowSigned, Pagination pagination) {
		/**
		 * 判断是否显示已签收问题件；默认只显示未签收问题件
		 */
		String orderStatus = null;
		if (isShowSigned != null) {
			if (isShowSigned.equals(0))// 只显示未签收的问题件；如果要同时显示已签收的话设置orderStatus==null就行
				orderStatus = "0";
		}
		long begin = System.currentTimeMillis();
		// 首先获取问题件列表
		List<Questionnaire> questionList = questionnaireService
				.siteQueryQuestionnaireManageList(siteUser, startTime, endTime,
						userCode, mailNO, issueStatus, feedbackInfo,
						dealStatus, wdIsRead, sortType, contactWay, buyerName,
						orderStatus, pagination);

		long end1 = System.currentTimeMillis();
		logger.error("siteQueryQuestionnaireManageList方法运行时间： "
				+ (end1 - begin) + "毫秒");

		// 获取问题件的issueIds，用于一次性从金刚数据库中获取相应的处理信息,减少链接次数提高效率；
		int count = questionList.size();
		String issueIds = "";
		if (count > 0) {
			for (int i = 0; i < count; i++) {
				issueIds += " '" + questionList.get(0).getIssueId() + "',";
			}
			issueIds = issueIds.substring(0, issueIds.length() - 1);
		}

		long begin2 = System.currentTimeMillis();
		Map<String, ArrayList<QuestionnaireDeal>> dealMaps = getQsnDealByIssueids(issueIds);
		long end2 = System.currentTimeMillis();
		logger.error("getQsnDealByIssueids方法运行时间： " + (end2 - begin2) + "毫秒");

		long begin3 = System.currentTimeMillis();
		long end3 = System.currentTimeMillis();
		logger.error("UpdateDotCache方法运行时间： " + (end3 - begin3) + "毫秒");
		/**
		 * 循环问题件列表，并根据问题件主键获取反馈信息；根据custormerId获取卖家信息；
		 * 根据金刚状态获取网点沟通记录。(为了减少系统响应时间，网点沟通记录在页面上点击时再请求) 将属性及对象封装在DtoQuestion对象里.
		 */
		if (questionList != null && questionList.size() > 0) {
			List<DtoQuestion> resultList = new ArrayList<DtoQuestion>();
			for (Questionnaire ques : questionList) {
				long begin4 = System.currentTimeMillis();
				DtoQuestion dtoQues = new DtoQuestion();
				dtoQues.setId(ques.getId());
				dtoQues.setMailNO(ques.getMailNO());
				dtoQues.setDealStatus(ques.getDealStatus());
				dtoQues.setBranchId(ques.getBranchId());
				dtoQues.setWdIsRead(ques.getWdIsRead());
				if (ques.getCustomerId() != null) {
					dtoQues.setIsElseCustomer(0);// 不属于其他客户问题件
					dtoQues.setCustomerId(ques.getCustomerId());
				} else {
					dtoQues.setIsElseCustomer(1);// 属于其他客户问题件
				}
				dtoQues.setFeedbackInfo(ques.getFeedbackInfo());
				dtoQues.setIssueId(ques.getIssueId());
				dtoQues.setIssueDesc(ques.getIssueDesc());// 此为网点界面上显示的问题描述
				dtoQues.setIssueStatus(ques.getIssueStatus());
				dtoQues.setIssueCreateTime(ques.getIssueCreateTime());
				dtoQues.setIssueCreateUserText(ques.getIssueCreateUserText());
				dtoQues.setBranckText(ques.getBranckText());
				dtoQues.setRecBranckText(ques.getRecBranckText());
				dtoQues.setReportBranckText(ques.getReportBranckText());
				dtoQues.setOrderStatus(ques.getOrderStatus());
				/**
				 * 根据上报网点编码获取网点
				 */

				if (ques.getReportBranckCode() != null
						&& !ques.getReportBranckCode().equals("")) {
					User user = Resource.getUserBySiteCode(ques
							.getReportBranckCode());
					if (user != null)
						dtoQues.setReportBranckContact(this.setContact(
								user.getMobilePhone(),
								user.getTelCode() + user.getTelePhone()));
				}
				/**
				 * 设置图片地址
				 */
				if (ques.getIMG1() != null && !(ques.getIMG1().equals("")))
					dtoQues.setIMG1(Constants.JINGANGPHOTO_ADDRESS
							+ ques.getIMG1());
				if (ques.getIMG2() != null && !(ques.getIMG2().equals("")))
					dtoQues.setIMG2(Constants.JINGANGPHOTO_ADDRESS
							+ ques.getIMG2());
				if (ques.getIMG3() != null && !(ques.getIMG3().equals("")))
					dtoQues.setIMG3(Constants.JINGANGPHOTO_ADDRESS
							+ ques.getIMG3());
				if (ques.getIMG4() != null && !(ques.getIMG4().equals("")))
					dtoQues.setIMG4(Constants.JINGANGPHOTO_ADDRESS
							+ ques.getIMG4());
				// 设置卖家信息
				User user = userService.getUserByCustomerId(ques
						.getCustomerId());
				if (user != null) {
					/**
					 * 姓名取直客的信息，根据客户编码取直客
					 */
					UserThread ut = new UserThread();
					ut.setUserCode(user.getUserCode());
					List<UserThread> utList = userThreadService
							.searchUsersByCode(ut);
					if (utList != null && utList.size() > 0) {
						user.setUserName(utList.get(0).getUserName());
					} else {
						user.setMobilePhone("");
						user.setUserName("");
					}
					dtoQues.setCustomer(user);
				}
				// 设置反馈信息
				List<Integer> questionnaireIds = new ArrayList<Integer>();
				questionnaireIds.add(ques.getId());
				List<QuestionaireExchange> questionaireExchangeList = questionaireExchangeService
						.getListByQuestionaireIds(questionnaireIds);
				if (questionaireExchangeList != null
						&& questionaireExchangeList.size() > 0) {
					dtoQues.setQuestionaireExchangeList(questionaireExchangeList);
					/**
					 * 拼接网点和卖家交互信息:为了防止在页面上截取的时候<em></em>这些标签被截断，所以在后台进行字符串的截取
					 */
					StringBuffer questionaireExchangeString = new StringBuffer();
					for (QuestionaireExchange que : questionaireExchangeList) {
						// 获取处理人电话信息
						User operationUser = userService.getUserById(que
								.getUserId());
						String contact = "";
						if (operationUser != null) {
							contact = StringUtils.isNotEmpty(operationUser
									.getTelePhone()) ? operationUser
									.getTelePhone() : operationUser
									.getMobilePhone();
							contact = StringUtils.isNotEmpty(contact) ? contact
									: "";
						}
						questionaireExchangeString
								.append(que.getMsgContent())
								.append("<em class='btdes'" + ">处理人：")
								.append(que.getOperatorName())
								.append("  " + contact)
								.append("<br />")
								.append(DateUtil.toSeconds(que.getCreateTime()))
								.append("</em>");
					}
					dtoQues.setQuestionaireExchangeString(questionaireExchangeString
							.toString());
				}
				// 设置沟通记录
				List<QuestionnaireDeal> quesDealList = dealMaps.get(ques
						.getIssueId());
				if (quesDealList != null) {
					dtoQues.setQuesDealList(quesDealList);
					StringBuffer quesDealString = new StringBuffer();
					for (QuestionnaireDeal que : quesDealList) {
						if (dealStatus.equals("1")) {
							quesDealString
									.append(que.getDealContent())
									.append("<em>")
									.append(que.getDealUserText())
									.append(DateUtil.toSeconds(que
											.getDealTime())).append("</em>");
						} else if (dealStatus.equals("3")) {
							quesDealString
									.append(que.getDealContent())
									.append("<div class=\"btdes\">")
									.append(que.getDealBranckText())
									.append("<br />")
									.append(DateUtil.toSeconds(que
											.getDealTime())).append("</div>");
						}
					}
					dtoQues.setQuesDealString(quesDealString.toString());
				}
				resultList.add(dtoQues);
				long end4 = System.currentTimeMillis();
				logger.error("循环一次运行时间： " + (end4 - begin4) + "毫秒");

			}
			return (List<T>) resultList;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.ytoec.kernel.service.QuestionDtoService#siteCountQuestionList(java
	 * .lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.Integer)
	 */
	@Override
	public int siteCountQuestionList(User siteUser, String startTime,
			String endTime, String userCode, String mailNO, String issueStatus,
			List<String> feedbackInfo, String dealStatus, String wdIsRead,
			String contactWay, String buyerName, Integer isShowSigned) {
		/**
		 * 判断是否显示已签收问题件；默认只显示未签收问题件
		 */
		String orderStatus = null;
		if (isShowSigned != null) {
			if (isShowSigned.equals(0))// 只显示未签收的问题件；如果要同时显示已签收的话设置orderStatus==null就行
				orderStatus = "0";
		}
		int result = questionnaireService.siteCountQuestionnaireManageList(
				siteUser, startTime, endTime, userCode, mailNO, issueStatus,
				feedbackInfo, dealStatus, wdIsRead, contactWay, buyerName,
				orderStatus);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.ytoec.kernel.service.QuestionDtoService#vipGetQuestionList(java.lang
	 * .String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.Integer, net.ytoec.kernel.common.Pagination)
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	@Override
	public List<T> vipGetQuestionList(List userCode, String customerId,
			String startTime, String endTime, String mailNO,
			List<String> feedbackInfo, String vipStatus, String mjIsRead,
			Integer sortType, String contactWay, String buyerName,
			Integer isShowSigned, Integer tagId, Pagination pagination) {
		/**
		 * 判断是否显示已签收问题件；默认只显示未签收问题件
		 */
		String orderStatus = null;
		if (isShowSigned != null) {
			if (isShowSigned.equals(0))// 只显示未签收的问题件；如果要同时显示已签收的话设置orderStatus==null就行
				orderStatus = "0";
		}
		// 首先获取问题件列表
		List<Questionnaire> questionList = questionnaireService
				.vipQueryQuestionnaireManageList(userCode, customerId,
						startTime, endTime, mailNO, feedbackInfo, vipStatus,
						mjIsRead, sortType, contactWay, buyerName, orderStatus,
						tagId, pagination);
		/**
		 * 循环问题件列表，并根据问题件主键获取反馈信息和备注信息；根据mailNO获取买家信息； 将属性及对象封装在DtoQuestion对象里.
		 */
		if (questionList != null && questionList.size() > 0) {
			List<DtoQuestion> resultList = new ArrayList<DtoQuestion>();
			for (Questionnaire ques : questionList) {
				DtoQuestion dtoQues = new DtoQuestion();
				dtoQues.setId(ques.getId());
				dtoQues.setMailNO(ques.getMailNO());
				dtoQues.setVipStatus(ques.getVipStatus());
				dtoQues.setBranchId(ques.getBranchId());
				dtoQues.setMjIsRead(ques.getMjIsRead());
				dtoQues.setCustomerId(ques.getCustomerId());
				dtoQues.setFeedbackInfo(ques.getFeedbackInfo());
				dtoQues.setIssueStatus(ques.getIssueStatus());
				dtoQues.setIssueCreateTime(ques.getIssueCreateTime());

				dtoQues.setIssueCreateUserText(ques.getIssueCreateUserText());
				dtoQues.setBranckText(ques.getBranckText());
				dtoQues.setRecBranckText(ques.getRecBranckText());
				dtoQues.setReportBranckText(ques.getReportBranckText());
				dtoQues.setOrderStatus(ques.getOrderStatus());
				dtoQues.setTaobaoLoginName(ques.getTaobaoLoginName());// 设置会员名
				/**
				 * 根据上报网点编码获取网点
				 */

				if (ques.getReportBranckCode() != null
						&& !ques.getReportBranckCode().equals("")) {
					User user = Resource.getUserBySiteCode(ques
							.getReportBranckCode());
					if (user != null)
						dtoQues.setReportBranckContact(this.setContact(
								user.getMobilePhone(),
								user.getTelCode() + user.getTelePhone()));
				}
				/**
				 * 设置图片地址
				 */
				if (ques.getIMG1() != null && !(ques.getIMG1().equals("")))
					dtoQues.setIMG1(Constants.JINGANGPHOTO_ADDRESS
							+ ques.getIMG1());
				if (ques.getIMG2() != null && !(ques.getIMG2().equals("")))
					dtoQues.setIMG2(Constants.JINGANGPHOTO_ADDRESS
							+ ques.getIMG2());
				if (ques.getIMG3() != null && !(ques.getIMG3().equals("")))
					dtoQues.setIMG3(Constants.JINGANGPHOTO_ADDRESS
							+ ques.getIMG3());
				if (ques.getIMG4() != null && !(ques.getIMG4().equals("")))
					dtoQues.setIMG4(Constants.JINGANGPHOTO_ADDRESS
							+ ques.getIMG4());
				// 设置备注信息
				List<QuestionaireRemark> questionaireBackupList = questionaireRemarkService
						.findByQuestionId(ques.getId());
				if (questionaireBackupList != null
						&& questionaireBackupList.size() > 0) {
					dtoQues.setQuestionaireRemark(questionaireBackupList.get(0));
				}
				// 设置反馈信息
				List<Integer> questionnaireIds = new ArrayList<Integer>();
				questionnaireIds.add(ques.getId());
				List<QuestionaireExchange> questionaireExchangeList = questionaireExchangeService
						.getListByQuestionaireIds(questionnaireIds);
				if (questionaireExchangeList != null
						&& questionaireExchangeList.size() > 0) {
					dtoQues.setQuestionaireExchangeList(questionaireExchangeList);
					// 反馈信息的第一条记录显示在卖家的问题描述中：因查询时按时间降序查询的，所以在显示时第一条反馈就是列表中最后一条记录
					dtoQues.setMjQuesDesc((questionaireExchangeList
							.get(questionaireExchangeList.size() - 1))
							.getMsgContent());
					/**
					 * 设置上报网点信息 卖家问题件上报网点是卖家对应处理的网点、时间和联系方式
					 */
					dtoQues.setSiteReportTime((questionaireExchangeList
							.get(questionaireExchangeList.size() - 1))
							.getCreateTime());
					dtoQues.setSiteName((questionaireExchangeList
							.get(questionaireExchangeList.size() - 1))
							.getOperatorName());
					User user = userService
							.getUserById(questionaireExchangeList.get(
									questionaireExchangeList.size() - 1)
									.getUserId());
					if (user != null && user.getMobilePhone() != null
							&& !user.getMobilePhone().equals(""))
						dtoQues.setRelationShip(user.getMobilePhone());
					else if (user != null && user.getTelePhone() != null)
						dtoQues.setRelationShip(user.getTelePhone());

					/**
					 * 拼接网点和卖家交互信息
					 */
					StringBuffer questionaireExchangeString = new StringBuffer();
					for (QuestionaireExchange que : questionaireExchangeList) {
						// 获取处理人电话信息
						User operationUser = userService.getUserById(que
								.getUserId());
						String contact = "";
						if (operationUser != null) {
							contact = StringUtils.isNotEmpty(operationUser
									.getTelePhone()) ? operationUser
									.getTelePhone() : operationUser
									.getMobilePhone();
							contact = StringUtils.isNotEmpty(contact) ? contact
									: "";
						}
						questionaireExchangeString
								.append(que.getMsgContent())
								.append("<em class='btdes'" + ">处理人：")
								.append(que.getOperatorName())
								.append("  " + contact)
								.append("<br />")
								.append(DateUtil.toSeconds(que.getCreateTime()))
								.append("</em>");
					}
					dtoQues.setQuestionaireExchangeString(questionaireExchangeString
							.toString());
				}
				// 设置买家信息
				if (ques.getBuyerName() != null)
					dtoQues.setBuyerName(ques.getBuyerName());
				if (ques.getBuyerMobile() != null
						&& !("").equals(ques.getBuyerMobile()))
					dtoQues.setContactWay(ques.getBuyerMobile());
				else if (ques.getBuyerPhone() != null)
					dtoQues.setContactWay(ques.getBuyerPhone());

				// 设置店铺
				User user = userService.getUserByCustomerId(ques
						.getCustomerId());
				dtoQues.setShopName(user.getShopName());
				resultList.add(dtoQues);
			}
			return (List<T>) resultList;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.ytoec.kernel.service.QuestionDtoService#vipCountQuestionList(java
	 * .lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.Integer)
	 */
	@Override
	public int vipCountQuestionList(List userCode, String customerId,
			String startTime, String endTime, String mailNO,
			List<String> feedbackInfo, String vipStatus, String mjIsRead,
			String contactWay, String buyerName, Integer isShowSigned,
			Integer tagId) {
		/**
		 * 判断是否显示已签收问题件；默认只显示未签收问题件
		 */
		String orderStatus = null;
		if (isShowSigned != null) {
			if (isShowSigned.equals(0))// 只显示未签收的问题件；如果要同时显示已签收的话设置orderStatus==null就行
				orderStatus = "0";
		}
		int result = questionnaireService.vipCountQuestionnaireManageList(
				userCode, customerId, startTime, endTime, mailNO, feedbackInfo,
				vipStatus, mjIsRead, contactWay, buyerName, orderStatus, tagId);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.ytoec.kernel.service.QuestionDtoService#getQuesDealList(java.lang
	 * .String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T getQuesDealList(String issueId) {
		List<QuestionnaireDeal> quesDealList = getQsnDealByIssueid(issueId);
		if (quesDealList != null) {
			DtoQuestion dtoQues = new DtoQuestion();
			dtoQues.setQuesDealList(quesDealList);
			return (T) dtoQues;
		}
		return null;
	}

	@Override
	public int noneReadQuestion(User curUser, String readStatus)
			throws ParseException {
		int resultNum = 0;
		/**
		 * 默认当前时间取10天内。
		 */
		String starttime = dateArithmetic(new Date(), 9);
		String endtime = dateArithmetic(new Date(), 0);
		if ("1".equals(curUser.getUserType())
				|| "11".equals(curUser.getUserType())
				|| "12".equals(curUser.getUserType())
				|| "13".equals(curUser.getUserType())) {
			List<String> bindedId = Resource.getBindedCustomerIdList(curUser);
			resultNum = questionnaireService.vipCountQuestionnaireManageList(
					bindedId, curUser.getTaobaoEncodeKey(), starttime,
					stringToDate(endtime, -1), null, null, null, readStatus,
					null, null, "0", null);
		} else if ("2".equals(curUser.getUserType())
				|| "21".equals(curUser.getUserType())
				|| "22".equals(curUser.getUserType())
				|| "23".equals(curUser.getUserType())) {
			resultNum = questionnaireService.siteCountQuestionnaireManageList(
					curUser, starttime, stringToDate(endtime, -1), null, null,
					null, null, "2", readStatus, null, null, "0");
		}
		return resultNum;
	}

	@Override
	public int noneHandleQuestion(User curUser, String stauts)
			throws ParseException {
		int resultNum = 0;
		/**
		 * 默认当前时间取10天月。
		 */
		String starttime = dateArithmetic(new Date(), 9);
		String endtime = dateArithmetic(new Date(), 0);
		if ("1".equals(curUser.getUserType())
				|| "11".equals(curUser.getUserType())
				|| "12".equals(curUser.getUserType())
				|| "13".equals(curUser.getUserType())) {
			List<String> bindedId = Resource.getBindedCustomerIdList(curUser);
			resultNum = questionnaireService.vipCountQuestionnaireManageList(
					bindedId, curUser.getTaobaoEncodeKey(), starttime,
					stringToDate(endtime, -1), null, null, stauts, null, null,
					null, "0", null);
		} else if ("2".equals(curUser.getUserType())
				|| "21".equals(curUser.getUserType())
				|| "22".equals(curUser.getUserType())
				|| "23".equals(curUser.getUserType())) {
			resultNum = questionnaireService.siteCountQuestionnaireManageList(
					curUser, starttime, stringToDate(endtime, -1), null, null,
					null, null, stauts, null, null, null, "0");
		}
		return resultNum;
	}

	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;

	/**
	 * 根据{@link Questionnaire#getIssueId()}去金刚的表中查询问题件的处理内容
	 * 
	 * @param issueId
	 *            {@link Questionnaire#getIssueId()} 金刚同步过来的问题件的id
	 * @return List<QuestionnaireDeal>
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private List<QuestionnaireDeal> getQsnDealByIssueid(String issueId) {
		List<QuestionnaireDeal> list = new ArrayList<QuestionnaireDeal>();
		if (StringUtils.isEmpty(ConfigUtilSingle.getInstance()
				.getQuestionnaireIssuedeal())) {
			logger.error("问题件处理内容表表名为空! 请检查配置文件是否配置了[questionnaire.table.issuedeal]!");
			return (List<QuestionnaireDeal>) Collections.EMPTY_LIST;
		}
		try {
			if (conn == null || conn.isClosed()) {
				conn = JDBCUtilSingle.getInstance().getConnection();
			}
			ps = conn.prepareStatement("select * from "
					+ ConfigUtilSingle.getInstance()
							.getQuestionnaireIssuedeal() + " where issue_id='"
					+ issueId + "'" + " order by create_time desc");
			// ps = conn.prepareStatement("select * from " +
			// "YTEXP.t_exp_waybill_issue_deal" +
			// " where issue_id='"+issueId+"'");
			rs = ps.executeQuery();
			while (rs.next()) {
				try {
					QuestionnaireDeal qsnDeal = new QuestionnaireDeal();
					qsnDeal.setDealContent(rs.getString("deal_content")); // 处理内容
					qsnDeal.setDealUserText(rs.getString("modify_user_name")); // 处理人
					qsnDeal.setDealTime(rs.getTimestamp("modify_time")); // 处理时间
					list.add(qsnDeal);
				} catch (Exception e) {
					logger.error(
							"查询问题件的处理内容出错! 参数信息[issue_id:" + issueId + "]", e);
				}
			}
		} catch (SQLException e) {
			list = (List<QuestionnaireDeal>) Collections.EMPTY_LIST;
			logger.error("数据库连接出错!", e);
		}
		/*
		 * 在主方法for循环执行完之后在关闭, 这样就不要创建多个连接对象
		 */
		finally {
			JDBCUtilSingle.free(rs, ps, conn);
		}
		return list;
	}

	/**
	 * 批量查询金刚的表中问题件的处理内容
	 * 
	 * @param issueIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, ArrayList<QuestionnaireDeal>> getQsnDealByIssueids(
			String issueIds) {
		Map<String, ArrayList<QuestionnaireDeal>> dealMaps = new HashMap<String, ArrayList<QuestionnaireDeal>>();
		if (StringUtils.isEmpty(issueIds)) {
			logger.error("查询issueIds为空!");
			return (Map<String, ArrayList<QuestionnaireDeal>>) Collections.EMPTY_MAP;
		}
		if (StringUtils.isEmpty(ConfigUtilSingle.getInstance()
				.getQuestionnaireIssuedeal())) {
			logger.error("问题件处理内容表表名为空! 请检查配置文件是否配置了[questionnaire.table.issuedeal]!");
			return (Map<String, ArrayList<QuestionnaireDeal>>) Collections.EMPTY_MAP;
		}
		try {
			if (conn == null || conn.isClosed()) {
				long begin = System.currentTimeMillis();
				conn = JDBCUtilSingle.getInstance().getConnection();
				long end = System.currentTimeMillis();
				logger.error("创建金刚链接方法运行时间： " + (end - begin) + "毫秒");
			}
			ps = conn.prepareStatement("select * from "
					+ ConfigUtilSingle.getInstance()
							.getQuestionnaireIssuedeal()
					+ " where issue_id in (" + issueIds + ")"
					+ " order by create_time desc");
			logger.error("金刚查询sql： "
					+ "select * from "
					+ ConfigUtilSingle.getInstance()
							.getQuestionnaireIssuedeal()
					+ " where issue_id in (" + issueIds + ")"
					+ " order by create_time desc");

			long begin1 = System.currentTimeMillis();
			rs = ps.executeQuery();
			while (rs.next()) {
				try {
					String issue_id = rs.getString("issue_id");

					QuestionnaireDeal qsnDeal = new QuestionnaireDeal();
					qsnDeal.setDealContent(rs.getString("deal_content")); // 处理内容
					qsnDeal.setDealUserText(rs.getString("modify_user_name")); // 处理人
					qsnDeal.setDealTime(rs.getTimestamp("modify_time")); // 处理时间
					if (dealMaps.containsKey(dealMaps)) {
						dealMaps.get(dealMaps).add(qsnDeal);
					} else {
						ArrayList<QuestionnaireDeal> list = new ArrayList<QuestionnaireDeal>();
						list.add(qsnDeal);
						dealMaps.put(issue_id, list);
					}
				} catch (Exception e) {
					logger.error("查询问题件的处理内容出错! 参数信息[issue_ids:" + issueIds
							+ "]", e);
				}
			}
			long end1 = System.currentTimeMillis();
			logger.error("整个金刚查询花费： " + (end1 - begin1) + "毫秒");
		} catch (SQLException e) {
			dealMaps = (Map<String, ArrayList<QuestionnaireDeal>>) Collections.EMPTY_MAP;
			logger.error("数据库连接出错!", e);
		}
		/*
		 * 在主方法for循环执行完之后在关闭, 这样就不要创建多个连接对象
		 */
		finally {
			long begin1 = System.currentTimeMillis();
			JDBCUtilSingle.free(rs, ps, conn);
			long end1 = System.currentTimeMillis();
			logger.error("释放金刚链接花费： " + (end1 - begin1) + "毫秒");
		}
		return dealMaps;
	}

	private static String dateArithmetic(Date originalDate, int countDay) {
		SimpleDateFormat sdm = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(originalDate);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		// 在当前日期上加-countDay天数
		cal.add(Calendar.DAY_OF_MONTH, -countDay);
		return sdm.format(cal.getTime());
	}

	private static String stringToDate(String str, int days)
			throws ParseException {
		SimpleDateFormat sdm = new SimpleDateFormat("yyyy-MM-dd");
		String result = null;
		if (str != null && !str.equals("")) {
			Date next = sdm.parse(str);
			result = dateArithmetic(next, days);
		}
		return result;
	}

	/**
	 * 有固话显示固话，没有则显示手机
	 * 
	 * @param mobile
	 * @param phone
	 * @return
	 */
	private String setContact(String mobile, String phone) {
		if (phone != null && !("").equals(phone))
			return phone;
		else if (mobile != null && !("").equals(mobile))
			return mobile;
		else
			return "";
	}

}
