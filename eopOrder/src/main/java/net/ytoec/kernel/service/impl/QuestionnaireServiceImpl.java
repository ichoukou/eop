/**
 * 
 */
package net.ytoec.kernel.service.impl;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.DocumentReader;
import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.action.remote.process.ProcessorUtils;
import net.ytoec.kernel.common.Constants;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.OrderDao;
import net.ytoec.kernel.dao.QuestionnaireDao;
import net.ytoec.kernel.dao.SendTaskDao;
import net.ytoec.kernel.dao.TraderInfoDao;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.QuestionaireExchange;
import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.dataobject.QuestionnaireDeal;
import net.ytoec.kernel.dataobject.QuestionnaireTag;
import net.ytoec.kernel.dataobject.SendTask;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.dataobject.UserThreadContract;
import net.ytoec.kernel.dto.BranchMailNoDTO;
import net.ytoec.kernel.dto.DtoBranch;
import net.ytoec.kernel.dto.QuestionnaireResultsUpdateDTO;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.QuestionaireExchangeService;
import net.ytoec.kernel.service.QuestionnaireService;
import net.ytoec.kernel.service.QuestionnaireTagService;
import net.ytoec.kernel.service.TraderInfoService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.service.UserThreadContractService;
import net.ytoec.kernel.service.UserThreadService;
import net.ytoec.kernel.service.helper.QuestionnaireCompensateHelper;
import net.ytoec.kernel.util.ConfigUtilSingle;
import net.ytoec.kernel.util.DateUtil;
import net.ytoec.kernel.util.JDBCUtilSingle;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 问题业务接口实现类
 * 
 * @author Wangyong
 * @2011-8-1 net.ytoec.kernel.service.impl
 */
@Service
@Transactional
public class QuestionnaireServiceImpl<T extends Questionnaire> implements
		QuestionnaireService<T> {

	private static Logger logger = LoggerFactory.getLogger(QuestionnaireServiceImpl.class);
	@Inject
	private QuestionnaireDao<T> dao;
	@Inject
	private OrderDao<Order> orderDao;
	@Inject
	private TraderInfoDao<TraderInfo> traderInfoDao;
	@Inject
	private UserService<User> userService;
	@Inject
	private OrderService<Order> orderService;
	@Inject
	private UserThreadService<UserThread> userThreadService;
	@Inject
	private UserThreadContractService<UserThreadContract> userThreadContractService;
	@Inject
	private QuestionaireExchangeService<QuestionaireExchange> questionaireExchangeService;
	@Inject
	private TraderInfoService<TraderInfo> traderInfoService;
	@Inject
	private QuestionnaireTagService<QuestionnaireTag> questionnaireTagService;

	@Autowired
	private SendTaskDao<SendTask> sendTaskDao;

	/** 问题id */
	private static final String ISSUE_ID = "id";
	/** 问题描述 */
	private static final String ISSUE_DESC = "issue_desc";
	/** 问题状态 */
	private static final String ISSUE_STATUS = "status";
	/** 问题类型 */
	private static final String ISSUE_TYPE = "issue_type";
	/** 揽收网点Id */
	private static final String BRANCK_ID = "source_org_code";
	/** 上报人 */
	private static final String ISSUE_CREATE_USER_TEXT = "create_user_name";
	/** 上报时间 */
	private static final String ISSUE_CREATE_TIME = "create_time";
	/** 接收网点 */
	private static final String REC_BRANCK_TEXT = "rec_org_name";
	/** 上报网点编码 */
	private static final String REPORT_BRANCK_CODE = "org_code";
	private static final String MAIL_NO = "waybill_no";
	/** 运单类型 */
	private static final String MAIL_TYPE = "exp_type";
	/** 图片地址 */
	private static final String IMG1 = "IMG1";
	/** 图片地址 */
	private static final String IMG2 = "IMG2";
	/** 图片地址 */
	private static final String IMG3 = "IMG3";
	/** 图片地址 */
	private static final String IMG4 = "IMG4";
	private static final String PHYSICAL_NAME = "PHYSICAL_NAME";

	private DocumentReader documentReader = new DocumentReader();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.ytoec.kernel.service.QuestionnaireService#addQuestionnaire(java.lang
	 * .Object)
	 */
	@Override
	public boolean addQuestionnaire(T questionnaire) {
		if (questionnaire == null) {
			logger.error("questionnaire is empity");
			return false;
		}
		return dao.addQuestionnaire(questionnaire);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.ytoec.kernel.service.QuestionnaireService#editQuestionnaire(java.
	 * lang.Object)
	 */
	@Override
	public boolean editQuestionnaire(T questionnaire) {
		if (questionnaire == null) {
			logger.error("questionnaire is empity");
			return false;
		}
		return dao.editQuestionnaire(questionnaire);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.ytoec.kernel.service.QuestionnaireService#removeQuestionnaire(java
	 * .lang.Object)
	 */
	@Override
	public boolean removeQuestionnaire(T questionnaire) {
		if (questionnaire == null) {
			logger.error("questionnaire is empity");
			return false;
		}
		return dao.removeQuestionnaire(questionnaire);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.ytoec.kernel.service.QuestionnaireService#getQestionnaireById(java
	 * .lang.Integer)
	 */
	@Override
	public T getQestionnaireById(Integer id) {
		return dao.getQuestionnaireById(id);
	}

	@Override
	public boolean dealQuestionnaire(Integer id, String feedbackInfo,
			Integer dealUserId) {
		if (id != null && id > 0) {
			T questionnaire = this.getQestionnaireById(id);
			questionnaire.setDealInfo(feedbackInfo);
			questionnaire.setFeedbackInfo(feedbackInfo);
			questionnaire.setDealStatus("已处理");
			questionnaire.setDealUserId(dealUserId);
			return dao.dealQuestionnaire(questionnaire);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.ytoec.kernel.service.QuestionnaireService#getQuestionnaireByBranchId
	 * (java.lang.Integer)
	 */
	@Override
	public List<T> getQuestionnaireByBranchId(Map map) {
		return dao.getQuestionnaireByBranchId(map);
	}

	@Override
	public List<T> getNoVipIdList(Date startTime, Date endTime) {
		Map map = new HashMap();
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		return dao.getNoVipIdList(map);
	}

	@Override
	public void updateVipId(T entity) {
		dao.updateVipId(entity);
	}

	private static String tableIssuedel = ConfigUtilSingle.getInstance()
			.getQuestionnaireIssuedeal();
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
	@SuppressWarnings("unchecked")
	private List<QuestionnaireDeal> getQsnDealByIssueid(String issueId) {

		List<QuestionnaireDeal> list = new ArrayList<QuestionnaireDeal>();
		if (StringUtils.isEmpty(tableIssuedel)) {
			logger.error("问题件处理内容表表名为空! 请检查配置文件是否配置了[questionnaire.table.issuedeal]!");
			return (List<QuestionnaireDeal>) Collections.EMPTY_LIST;
		}

		try {
			if (conn == null || conn.isClosed()) {
				conn = JDBCUtilSingle.getInstance().getConnection();
			}

			ps = conn.prepareStatement("select * from " + tableIssuedel
					+ " where issue_id='" + issueId + "'");
			rs = ps.executeQuery();
			while (rs.next()) {
				try {
					QuestionnaireDeal qsnDeal = new QuestionnaireDeal();
					qsnDeal.setDealContent(rs.getString("deal_content")); // 处理内容
					// qsnDeal.setDealBranckText(userService.getBranckNameByCode(rs.getString("org_code"))
					// ); // 处理网点
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
		 * 在主方法for循环执行完之后在关闭, 这样就不要创建多个连接对象 finally { JDBCUtilSingle.free(rs,
		 * ps, conn); }
		 */
		return list;
	}

	@Override
	public void syncQuestionnaire(T qsn, String nextStarttime) {
		// 为保持更新问题件和缓存的事物一致，都在一个service方法里操作
		try {
			//long t1 = System.currentTimeMillis();
			T entity = dao.getQsnByIssueId(qsn.getIssueId());
			//logger.error("根据问题件Id查询问题件耗时："+(System.currentTimeMillis()-t1));
			String customerId = null;
			// 新增
			if (entity == null) {
				Order order = orderService.getOrderByMailNo(qsn.getMailNO());
				/**
				 * 问题件同步入库同时，插入买家信息入库
				 */
				if (order != null && order.getId() != null) {
					/* 调整成直接根据OrderId和tradeType取交易信息
					List<TraderInfo> traderInfoList = traderInfoService
							.getTraderInfoByOrderId(order.getId());
					if (traderInfoList != null && !traderInfoList.isEmpty()) {
						for (TraderInfo traderInfo : traderInfoList) {
							if (traderInfo.getTradeType().equals("1")) {
								qsn.setBuyerName(traderInfo.getName());
								qsn.setBuyerMobile(traderInfo.getMobile());
								qsn.setBuyerPhone(traderInfo.getPhone());
								break;
							}
						}
					}*/
					List<TraderInfo> traderInfoList = traderInfoService.getTraderInfo4Question(order.getId(), "1");
					if(traderInfoList!=null && traderInfoList.size()>0){
						TraderInfo traderInfo = traderInfoList.get(0);
						qsn.setBuyerName(traderInfo.getName());
						qsn.setBuyerMobile(traderInfo.getMobile());
						qsn.setBuyerPhone(traderInfo.getPhone());
					}
					/**
					 * 插入订单状态
					 */
					if (StringUtils.isNotEmpty(order.getStatus())) {
						if (order.getStatus().equals("SIGNED"))
							qsn.setOrderStatus("1");// 设置已签收
						else
							qsn.setOrderStatus("0");// 设置未签收
					}
					/**
					 * 设置会员名
					 */
					if (StringUtils.isNotEmpty(order.getVersion())) {
						qsn.setTaobaoLoginName(order.getVersion());
					}
					customerId = order.getCustomerId();
					if (!StringUtils.isEmpty(customerId)
							&& Resource.getUserByCustomerId(customerId) != null) {
						qsn.setCustomerId(order.getCustomerId());
					}
					//--
					if (StringUtils.isEmpty(order.getCustomerId())) {
						logger.error("问题件更新. 订单不存在或订单的customerId为空! "
								+ "参数信息[运单号(mailNo):" + qsn.getMailNO() + "]");
					}
					qsn.setSenderTime(order.getCreateTime());
					if (!StringUtils.isEmpty(order.getCustomerId())
							&& Resource.getUserByCustomerId(order.getCustomerId()) != null) {
						qsn.setCustomerId(order.getCustomerId());
					}
					
					//logger.error("问题件更新="+qsn.getMailNO());
					long t3 = System.currentTimeMillis();
					this.addQuestionnaire((T) qsn);
					long t4 = System.currentTimeMillis();
					logger.error("插入问题件表耗时："+(t4-t3));
					// 调用自动通知客户
					try
					{
						if (!StringUtils.isEmpty(order.getCustomerId())
								&& Resource.getUserByCustomerId(order.getCustomerId()) != null) {
							this.autoNotifyQuestion(qsn);
						}
					}catch(Exception e){
						logger.error("同步问题件时 调用自动通知客户功能出错  MailNo:"+qsn.getMailNO()+" 原因：",e);
					}
					long t5 = System.currentTimeMillis();
					logger.error("同步问题件时 调用自动通知客户功能耗时："+(t5-t4));
				}
			}
			// 更新字段
			else {
				//logger.error("问题件更新updateIssueStatusById");
				entity.setMailType(qsn.getMailType());
				entity.setMailNO(qsn.getMailNO());
				entity.setFeedbackInfo(qsn.getFeedbackInfo());
				entity.setIssueDesc(qsn.getIssueDesc());
				entity.setIssueStatus(qsn.getIssueStatus());
				// dao.updateIssueStatusByissueId(entity);
				long t3 = System.currentTimeMillis();
				dao.updateIssueStatusById(entity);
				logger.error("更新问题件表耗时："+(System.currentTimeMillis()-t3));
			}

			// 更新缓存
			// Resource.updateQsnConfig(Resource.QSN_NEXTSTARTTIME,
			// nextStarttime);
		} catch (Exception e) {
			logger.error("syncQuestionnaire 更新问题件失败!", e);
		}
	}

	@Override
	public TraderInfo getCustomerInfo(String mailNo) {
		List<Order> list = orderDao.getOrderByMailNo(mailNo);
		if (list.size() == 0) {
			logger.info("问题件#查看客户/买家信息 操作! 没有根据运单号查到对应的订单! 参数信息[运单号:" + mailNo
					+ "]");
			return null;
		}

		List<TraderInfo> listTrader = traderInfoDao.getTraderInfoByOrderId(list
				.get(0).getId());
		if (listTrader.size() == 0) {
			logger.info("问题件#查看客户/买家信息 操作! 没有根据运单id查到对应的交易人信息! 参数信息[运单Id:"
					+ list.get(0).getId() + "]");
			return null;
		}

		for (TraderInfo traderInfo : listTrader) {
			// 收件人
			if ("1".equals(traderInfo.getTradeType())) {
				return traderInfo;
			}
		}

		return null;
	}

	/**
	 * 根据用户的ID取当前用户的customerId和子账号的customerId
	 * 
	 * @param vipId
	 * @return
	 */
	private List<String> getCustomerIds(String vipId) {
		List<String> list = new ArrayList<String>();
		User user = userService.getUserById(Integer.parseInt(vipId));
		if (user != null) {
			list.add(user.getTaobaoEncodeKey());

			List<User> subUserList = userService
					.getUserByParentId(user.getId());
			for (User subUser : subUserList) {
				String customerId = subUser.getTaobaoEncodeKey();
				// 过滤空值
				if (!StringUtils.isEmpty(customerId)) {
					list.add(customerId);
				}
			}
		}

		return list;
	}

	/**
	 * 查找同一用户编码下已激活的卖家
	 * 
	 * @param userCode
	 * @return
	 */
	private List<String> getCustomerIdByUserCode(String userCode) {
		List<String> list = new ArrayList<String>();
		List<User> userList = userService.searchUsersByCodeTypeState(userCode,
				"1", "1");// 查找同一用户编码下已激活的买家
		if (userList != null && userList.size() > 0) {
			for (User user : userList) {
				if (user.getTaobaoEncodeKey() != null
						&& !(user.getTaobaoEncodeKey().equals("")))
					list.add(user.getTaobaoEncodeKey());
			}
		} else {
			return null;
		}
		if (list.size() > 0)
			return list;
		else
			return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<T> siteQueryQuestionnaireManageList(User siteUser,
			String startTime, String endTime, String userCode, String mailNO,
			String issueStatus, List<String> feedbackInfo, String dealStatus,
			String wdIsRead, Integer sortType, String contactWay,
			String buyerName, String orderStatus, Pagination pagination) {
		if (siteUser.getSite() == null || siteUser.getSite().equals("")) // 在网点编码不存在的情况返回null
			return null;
		Map map = new HashMap();
		String branchId = siteUser.getSite();
		if (siteUser.getUserType().equals("2")
				&& siteUser.getParentId() != null) {// 承包区类型
			branchId = userService.getUserById(siteUser.getParentId())
					.getSite();
		} else if (siteUser.getUserType().equals("21")
				|| siteUser.getUserType().equals("22")
				|| siteUser.getUserType().equals("23")) {
			// 如果是子账号的话还要判断是否是承包区的子账号。
			User u = userService.getUserById(siteUser.getParentId());
			if (u.getParentId() != null)
				branchId = userService.getUserById(u.getParentId()).getSite();
		}
		map.put("branchId", branchId);
		// 默认按DESC排序:（注：前端sortType取值范围为1(升序)、2(降序)两种）
		if (sortType == null)
			map.put("sortType", 2);
		else
			map.put("sortType", sortType);
		if (dealStatus != null) {// 网点处理状态:未通知（1），已通知（2）,其他（3）
			map.put("dealStatus", dealStatus);
		}
		map.put("startIndex", pagination.getStartIndex());
		map.put("pageNum", pagination.getPageNum());
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (startTime != null && !startTime.equals(""))
				map.put("startTime", simpleDate.parse(startTime));
			if (endTime != null && !endTime.equals(""))
				map.put("endTime", simpleDate.parse(endTime));
		} catch (ParseException e) {
			logger.error(LogInfoEnum.PARAM_INVALID.getValue(), e);
		}
		if (userCode != null && !userCode.equals("")) {
			if (userCode.equals("0")) {// 这里新增其他客户筛选，其他客户userCode值为0，（其他客户表示customerId为null的情况）
				map.put("customerId", new ArrayList<String>());
			} else {
				List<String> result = getCustomerIdByUserCode(userCode);
				if (result != null) {
					map.put("customerId", getCustomerIdByUserCode(userCode));
				} else {
					return null;
				}
			}
		}
		if (mailNO != null && mailNO.trim().length() > 0) {
			map.put("mailNO", mailNO.trim());
		}
		if (issueStatus != null && !(issueStatus.trim().equals(""))) {
			map.put("issueStatus", issueStatus.trim());
		}
		if (feedbackInfo != null && feedbackInfo.size() > 0) {
			map.put("feedbackInfo", feedbackInfo);
		}
		if (wdIsRead != null && wdIsRead.trim().length() > 0) {// 网点未读（0），网点已读（1）
			map.put("wdIsRead", wdIsRead);
		}
		if (contactWay != null && !("").equals(contactWay.trim()))
			map.put("contactWay", contactWay.trim());
		if (buyerName != null && !("").equals(buyerName.trim()))
			map.put("buyerName", buyerName.trim());
		if (orderStatus != null) {
			map.put("orderStatus", orderStatus);
		}
		return dao.queryQuestionnaireManageList(map);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int siteCountQuestionnaireManageList(User siteUser,
			String startTime, String endTime, String userCode, String mailNO,
			String issueStatus, List<String> feedbackInfo, String dealStatus,
			String wdIsRead, String contactWay, String buyerName,
			String orderStatus) {
		if (siteUser.getSite() == null || siteUser.getSite().equals("")) // 在网点编码不存在的情况返回0
			return 0;
		Map map = new HashMap();
		String branchId = siteUser.getSite();
		if (siteUser.getUserType().equals("2")
				&& siteUser.getParentId() != null) {
			branchId = userService.getUserById(siteUser.getParentId())
					.getSite();
		} else if (siteUser.getUserType().equals("21")
				|| siteUser.getUserType().equals("22")
				|| siteUser.getUserType().equals("23")) {
			// 如果是子账号的话还要判断是否是承包区的子账号。
			User u = userService.getUserById(siteUser.getParentId());
			if (u.getParentId() != null)
				branchId = userService.getUserById(u.getParentId()).getSite();
		}
		map.put("branchId", branchId);
		if (dealStatus != null) {// 默认显示未通知问题件:网点处理状态:默认未通知（1），已通知（2）,其他（3）
			map.put("dealStatus", dealStatus);
		}
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (startTime != null && !startTime.equals(""))
				map.put("startTime", simpleDate.parse(startTime));
			if (endTime != null && !endTime.equals(""))
				map.put("endTime", simpleDate.parse(endTime));
		} catch (ParseException e) {
			logger.error(LogInfoEnum.PARAM_INVALID.getValue(), e);
		}
		if (userCode != null && !userCode.equals("")) {
			if (userCode.equals("0")) {// 这里新增其他客户筛选，其他客户userCode值为0，（其他客户表示customerId为null的情况）
				map.put("customerId", new ArrayList<String>());
			} else {
				List<String> result = getCustomerIdByUserCode(userCode);
				if (result != null) {
					map.put("customerId", getCustomerIdByUserCode(userCode));
				} else {
					return 0;
				}
			}
		}
		if (mailNO != null && mailNO.trim().length() > 0)
			map.put("mailNO", mailNO.trim());
		if (issueStatus != null && !(issueStatus.trim().equals(""))) {
			map.put("issueStatus", issueStatus.trim());
		}
		if (feedbackInfo != null && feedbackInfo.size() > 0) {
			map.put("feedbackInfo", feedbackInfo);
		}
		if (wdIsRead != null && wdIsRead.trim().length() > 0) {// 网点未读（0），网点已读（1）
			map.put("wdIsRead", wdIsRead);
		}
		if (contactWay != null && !("").equals(contactWay.trim()))
			map.put("contactWay", contactWay.trim());
		if (buyerName != null && !("").equals(buyerName.trim()))
			map.put("buyerName", buyerName.trim());
		if (orderStatus != null) {
			map.put("orderStatus", orderStatus);
		}
		return dao.countQuestionnaireManageList(map);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<T> vipQueryQuestionnaireManageList(List bindedId,
			String customerId, String startTime, String endTime, String mailNO,
			List<String> feedbackInfo, String vipStatus, String mjIsRead,
			Integer sortType, String contactWay, String buyerName,
			String orderStatus, Integer tagId, Pagination pagination) {
		Map map = new HashMap();
		// 在使用关联账号查询所有该用户编码下的customerId时使用这个条件
		if (bindedId != null && bindedId.size() > 0) {
			map.put("customerId", bindedId);
		} else if (customerId != null && !customerId.equals("")) {// 在单个customerId查询时使用这个条件
			List<String> list = new ArrayList<String>();
			list.add(customerId);
			map.put("customerId", list);
		} else
			// 在用户编码和customerId都为null时返回空
			return null;
		if (pagination != null) {
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
		}
		map.put("dealStatus", "2");// 卖家只关注网点已通知状态下的问题件
		// 默认按DESC排序:（注：前端sortType取值范围为1(升序)、2(降序)两种）
		if (sortType == null)
			map.put("sortType", 2);
		else {
			map.put("sortType", sortType);
		}
		if (vipStatus != null) {// 卖家未处理（1），卖家处理中（2），卖家已处理（3）（注：卖家只关注网点已通知状态下的问题件）
			map.put("vipStatus", vipStatus);
		}
		if (tagId != null && tagId > 0 && vipStatus.equals("2")) {// 标签查询只在处理中
			map.put("tagId", tagId);
		}
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (startTime != null && !startTime.equals(""))
				map.put("startTime", simpleDate.parse(startTime));
			if (endTime != null && !endTime.equals(""))
				map.put("endTime", simpleDate.parse(endTime));
		} catch (ParseException e) {
			logger.error(LogInfoEnum.PARAM_INVALID.getValue(), e);
		}
		if (mailNO != null && mailNO.trim().length() > 0)
			map.put("mailNO", mailNO.trim());
		if (mjIsRead != null && mjIsRead.trim().length() > 0) {
			map.put("mjIsRead", mjIsRead);
		}
		if (contactWay != null && !("").equals(contactWay.trim()))
			map.put("contactWay", contactWay.trim());
		if (buyerName != null && !("").equals(buyerName.trim()))
			map.put("buyerName", buyerName.trim());
		if (feedbackInfo != null && feedbackInfo.size() > 0) {
			map.put("feedbackInfo", feedbackInfo);
		}
		if (orderStatus != null) {
			map.put("orderStatus", orderStatus);
		}
		return dao.queryQuestionnaireManageList(map);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int vipCountQuestionnaireManageList(List bindedId,
			String customerId, String startTime, String endTime, String mailNO,
			List<String> feedbackInfo, String vipStatus, String mjIsRead,
			String contactWay, String buyerName, String orderStatus,
			Integer tagId) {
		Map map = new HashMap();
		// 在使用关联账号查询所有该用户编码下的customerId时使用这个条件
		if (bindedId != null && bindedId.size() > 0) {
			map.put("customerId", bindedId);
		} else if (customerId != null && !customerId.equals("")) {// 在单个customerId查询时使用这个条件
			List<String> list = new ArrayList<String>();
			list.add(customerId);
			map.put("customerId", list);
		} else
			// 在用户编码和customerId都为null时返回空
			return 0;
		map.put("dealStatus", "2");// 卖家只关注网点已通知状态下的问题件
		if (vipStatus != null) {// 卖家未处理（1），卖家处理中（2），卖家已处理（注：卖家只关注网点已通知状态下的问题件）
			map.put("vipStatus", vipStatus);
		}
		if (tagId != null && tagId > 0 && vipStatus.equals("2")) {// 标签查询只在处理中
			map.put("tagId", tagId);
		}
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (startTime != null && !startTime.equals(""))
				map.put("startTime", simpleDate.parse(startTime));
			if (endTime != null && !endTime.equals(""))
				map.put("endTime", simpleDate.parse(endTime));
		} catch (ParseException e) {
			logger.error(LogInfoEnum.PARAM_INVALID.getValue(), e);
		}
		if (mailNO != null && mailNO.trim().length() > 0)
			map.put("mailNO", mailNO.trim());
		if (mjIsRead != null && mjIsRead.trim().length() > 0) {
			map.put("mjIsRead", mjIsRead);
		}
		if (contactWay != null && !("").equals(contactWay.trim()))
			map.put("contactWay", contactWay.trim());
		if (buyerName != null && !("").equals(buyerName.trim()))
			map.put("buyerName", buyerName.trim());
		if (feedbackInfo != null && feedbackInfo.size() > 0) {
			map.put("feedbackInfo", feedbackInfo);
		}
		if (orderStatus != null) {
			map.put("orderStatus", orderStatus);
		}
		return dao.countQuestionnaireManageList(map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean moveQuestion(User curUser, Integer questionId,
			Integer quesStatus, Integer tagId) {
		boolean flag = false;
		Questionnaire ques = dao.getQuestionnaireById(questionId);
		if ("2".equals(curUser.getUserType())
				|| "2".equals(curUser.getUserType())
				|| "22".equals(curUser.getUserType())
				|| "23".equals(curUser.getUserType())) {
			ques.setDealStatus(quesStatus.toString());
		}
		if ("1".equals(curUser.getUserType())
				|| "11".equals(curUser.getUserType())
				|| "12".equals(curUser.getUserType())
				|| "13".equals(curUser.getUserType())) {
			ques.setVipStatus(quesStatus.toString());
			if (tagId != null)
				ques.setTagId(tagId);
		}
		ques.setDealTime(new Date());
		ques.setPartitionDate(new Date());
		flag = dao.editQuestionnaire((T) ques);
		return flag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean markQuestion(User curUser, Integer questionId,
			String readStatus) {
		boolean flag = false;
		Questionnaire ques = dao.getQuestionnaireById(questionId);
		if ("1".equals(curUser.getUserType())
				|| "11".equals(curUser.getUserType())
				|| "12".equals(curUser.getUserType())
				|| "13".equals(curUser.getUserType())) {
			ques.setMjIsRead(readStatus);
		} else if ("2".equals(curUser.getUserType())
				|| "21".equals(curUser.getUserType())
				|| "22".equals(curUser.getUserType())
				|| "23".equals(curUser.getUserType())) {
			ques.setWdIsRead(readStatus);
		}
		flag = dao.editQuestionnaire((T) ques);
		return flag;
	}

	@Override
	public void getQuestionnaireFromJIinGang(ResultSet rs, String strEndtime)
			throws SQLException {
		String branchId = "";
		while (rs.next()) {
			branchId = rs.getString(BRANCK_ID);
			if (branchId != null) {

				DtoBranch branch = Resource.getDtoBranchByCode(branchId);
				if (branch == null) {
					logger.error("branchId：" + branchId + ",DtoBranch is null ");
					continue;
				}
				// 网点未激活, 跳过更新
				if (!"1".equals(branch.getStatus())) {
					logger.info("问题件同步. 揽件网点不存在或者账号 状态不是激活状态, 跳过更新!"
							+ " 参数信息[运单号:" + rs.getString(MAIL_NO) + ";"
							+ " 网点编码:" + branchId + ";" + " 网点状态:"
							+ branch.getStatus() + "]");
					continue;
				}

				Questionnaire qsn = new Questionnaire();

				qsn.setBranchId(branchId);
				qsn.setBranckText(branch.getText());
				// 设置问题件处理时间为当前时间，网点在查询的时候按这个字段查询。处理后更新次字段。
				// 上述逻辑修正：余建江说 同步过来的处理时间就写 上报时间 2012-05-10
				qsn.setDealTime(rs.getTimestamp(ISSUE_CREATE_TIME));
				// 设置分区时间
				qsn.setPartitionDate(rs.getDate(ISSUE_CREATE_TIME));
				qsn.setMailType(rs.getString(MAIL_TYPE)); // 运单状态
				qsn.setMailNO(rs.getString(MAIL_NO));
				qsn.setFeedbackInfo(rs.getString(ISSUE_TYPE));
				qsn.setIssueId(rs.getString(ISSUE_ID));
				qsn.setIssueDesc(rs.getString(ISSUE_DESC));
				qsn.setIssueStatus(rs.getString(ISSUE_STATUS));
				qsn.setDealStatus("1"); // 未通知
				qsn.setIssueCreateUserText(rs.getString(ISSUE_CREATE_USER_TEXT));
				qsn.setIssueCreateTime(rs.getTimestamp(ISSUE_CREATE_TIME));
				qsn.setRecBranckText(rs.getString(REC_BRANCK_TEXT));
				qsn.setReportBranckCode(rs.getString(REPORT_BRANCK_CODE)); // 上报网点code
				qsn.setReportBranckText(Resource.getDtoBranchByCode(
						rs.getString(REPORT_BRANCK_CODE)).getText());// 上报网点名称

				// 同步问题件图片:需要从金刚库中查找PHYSICAL_NAME
				String tablename = ConfigUtilSingle.getInstance()
						.getQuestionnaireFile();
				if (StringUtils.isNotEmpty(rs.getString(IMG1))) {
					Connection con = null;
					PreparedStatement ps = null;
					ResultSet result = null;
					con = JDBCUtilSingle.getInstance().getConnection();
					try {
						String sql = "select * from " + tablename
								+ " where ID=" + rs.getString(IMG1);
						ps = con.prepareStatement(sql);
						result = ps.executeQuery();
						if (!result.next()) {
							return;
						} else {
							while (result.next()) {
								String value = result.getString(PHYSICAL_NAME);
								if (value.contains("/yto"))
									value = value.replace("/yto", "");
								qsn.setIMG1(value);
								break;
							}
						}
					} catch (Exception e) {
						logger.error("查找问题件图片出错：" + e);
					} finally {
						JDBCUtilSingle.free(result, ps, con);
					}
				}
				if (StringUtils.isNotEmpty(rs.getString(IMG2))) {
					Connection con = null;
					PreparedStatement ps = null;
					ResultSet result = null;
					con = JDBCUtilSingle.getInstance().getConnection();
					try {
						String sql = "select * from " + tablename
								+ " where ID=" + rs.getString(IMG2);
						ps = con.prepareStatement(sql);
						result = ps.executeQuery();
						if (!result.next()) {
							return;
						} else {
							while (result.next()) {
								String value = result.getString(PHYSICAL_NAME);
								if (value.contains("/yto"))
									value = value.replace("/yto", "");
								qsn.setIMG2(value);
								break;
							}
						}
					} catch (Exception e) {
						logger.error("查找问题件图片出错：" + e);
					} finally {
						JDBCUtilSingle.free(result, ps, con);
					}
				}
				if (StringUtils.isNotEmpty(rs.getString(IMG3))) {
					Connection con = null;
					PreparedStatement ps = null;
					ResultSet result = null;
					con = JDBCUtilSingle.getInstance().getConnection();
					try {
						String sql = "select * from " + tablename
								+ " where ID=" + rs.getString(IMG3);
						ps = con.prepareStatement(sql);
						result = ps.executeQuery();
						if (!result.next()) {
							return;
						} else {
							while (result.next()) {
								String value = result.getString(PHYSICAL_NAME);
								if (value.contains("/yto"))
									value = value.replace("/yto", "");
								qsn.setIMG3(value);
								break;
							}
						}
					} catch (Exception e) {
						logger.error("查找问题件图片出错：" + e);
					} finally {
						JDBCUtilSingle.free(result, ps, con);
					}
				}
				if (StringUtils.isNotEmpty(rs.getString(IMG4))) {
					Connection con = null;
					PreparedStatement ps = null;
					ResultSet result = null;
					con = JDBCUtilSingle.getInstance().getConnection();
					try {
						String sql = "select * from " + tablename
								+ " where ID=" + rs.getString(IMG4);
						ps = con.prepareStatement(sql);
						result = ps.executeQuery();
						if (!result.next()) {
							return;
						} else {
							while (result.next()) {
								String value = result.getString(PHYSICAL_NAME);
								if (value.contains("/yto"))
									value = value.replace("/yto", "");
								qsn.setIMG4(value);
								break;
							}
						}
					} catch (Exception e) {
						logger.error("查找问题件图片出错：" + e);
					} finally {
						JDBCUtilSingle.free(result, ps, con);
					}
				}

				syncQuestionnaire((T) qsn, strEndtime);
			}
		}
		Resource.updateQsnConfig(Resource.QSN_NEXTSTARTTIME, strEndtime);
		logger.info("-------------------问题件同步执行完毕----------------------");

	}

	@Override
	public List<T> getQestionnaireByMailNo(String mailNo) {
		return dao.getQsnByMailNo(mailNo);
	}

	@Override
	public void getQuestionnaireFromJIinGang(List<Questionnaire> list)
			throws SQLException {
		// TODO Auto-generated method stub
		for (int i = 0; i < list.size(); i++) {
			syncQuestionnaire((T) list.get(i), null);
		}

		logger.info("-------------------问题件同步执行完毕----------------------");

	}

	@Override
	public List<Questionnaire> getQuestionnaireByDealTime(Date dealTime,
			Integer limit) throws SQLException {
		return dao.getQuestionnaireByDealTime(dealTime, limit);
	}

	@Override
	public List<Questionnaire> getQestionnaireById(Integer id, Integer limit) {
		return dao.getQestionnaireById(id, limit);
	}

	@Override
	public void updateQuestionnaire(List<Questionnaire> questionnairesList) {
		if (questionnairesList == null || questionnairesList.isEmpty()) {
			logger.error("questionnairesList is empty");
			return;
		}
		Map<String, String> quesMap = new HashMap<String, String>();
		Questionnaire questionnaire = new Questionnaire();
		StringBuffer quesIdBuffer = new StringBuffer();
		for (int i = 0; i < questionnairesList.size(); i++) {
			questionnaire = questionnairesList.get(i);
			quesMap.put(questionnaire.getIssueId(),
					questionnaire.getIssueStatus());
			if (i == (questionnairesList.size() - 1)) {
				quesIdBuffer.append("'" + questionnaire.getIssueId() + "'");
			} else {
				quesIdBuffer.append("'" + questionnaire.getIssueId() + "',");
			}
		}

		QuestionnaireCompensateHelper.getQuestionnairFromJingang(
				quesIdBuffer.toString(), quesMap);

		String quesId = "";
		for (int i = 0; i < questionnairesList.size(); i++) {
			Questionnaire tmpQuestionnaire = new Questionnaire();
			tmpQuestionnaire = questionnairesList.get(i);
			quesId = tmpQuestionnaire.getIssueId();
			if (quesMap.containsKey(quesId)) {
				tmpQuestionnaire.setIssueStatus(quesMap.get(quesId));
			}
		}
		dao.batchUpdateQuestionnaire(questionnairesList);
		Resource.questionnaireMap.put("pageId",
				questionnairesList.get(questionnairesList.size() - 1).getId());
		logger.error("last update qsn id :"
				+ questionnairesList.get(questionnairesList.size() - 1).getId());

	}

	@Override
	public QuestionnaireResultsUpdateDTO updateQuestionnaireResults(
			QuestionnaireResultsUpdateDTO ques, boolean isSys) {
		if (ques == null) {
			logger.error("参数对象为空！");
			return null;
		}
		if (StringUtils.isEmpty(ques.getIssueId())
				|| StringUtils.isEmpty(ques.getDealContent())
				|| StringUtils.isEmpty(ques.getStatus())
				|| StringUtils.isEmpty(ques.getCreateOrgCode())
				|| StringUtils.isEmpty(ques.getCreateUserCode())) {

			logger.error("参数对象的非空字段出现了空值。" + ques.getIssueId() + ","
					+ ques.getDealContent() + "," + ques.getStatus() + ","
					+ ques.getCreateOrgCode() + "," + ques.getCreateUserCode());
			return null;
		}
		logger.error("--------------组装xml------------------");

		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("<IssueUpdateRequest>");
		sBuffer.append("<issueId>");
		sBuffer.append(ques.getIssueId());
		sBuffer.append("</issueId>");
		sBuffer.append("<dealContent>");
		sBuffer.append(ques.getDealContent());
		sBuffer.append("</dealContent>");
		sBuffer.append("<status>");
		sBuffer.append(ques.getStatus());
		sBuffer.append("</status>");
		sBuffer.append("<createOrgCode>");
		sBuffer.append(ques.getCreateOrgCode());
		sBuffer.append("</createOrgCode>");
		sBuffer.append("<createUserName>");
		sBuffer.append(ques.getCreateUserName());
		sBuffer.append("</createUserName>");
		sBuffer.append("<createUserCode>");
		sBuffer.append(ques.getCreateUserCode());
		sBuffer.append("</createUserCode>");
		// if (!StringUtils.isEmpty(ques.getIssueCreateTime())) {
		sBuffer.append("<issueCreateTime>");
		// sBuffer.append(ques.getIssueCreateTime());
		// sBuffer.append("2012-05-28 15:40:26.000");
		sBuffer.append("</issueCreateTime>");
		// }
		sBuffer.append("</IssueUpdateRequest>");

		String dslogisticsInterface = sBuffer.toString();
		String dsdataDigest = "";
		logger.error("xml原始明文：" + dslogisticsInterface);
		// 获得utf-8 密文
		try {
			dsdataDigest = java.net.URLEncoder.encode(
					Md5Encryption.MD5Encode(dslogisticsInterface
							+ ConfigUtilSingle.getInstance().getPARTERID_HR()),
					"UTF-8");
			logger.error("xml密文：" + dsdataDigest);
		} catch (UnsupportedEncodingException e1) {
			logger.error("加密并且转码失败！", e1);
		}
		// 获得明文
		try {
			dslogisticsInterface = java.net.URLEncoder.encode(
					dslogisticsInterface, "UTF-8");
			logger.error("xml解密后明文：" + dslogisticsInterface);
		} catch (UnsupportedEncodingException e) {
			logger.error("xml转码出现异常", e);
		}
		String xmlString = "logistics_interface=" + dslogisticsInterface + "&"
				+ "data_digest=" + dsdataDigest;
		logger.error("RMI发送参数：" + xmlString);
		QuestionnaireResultsUpdateDTO resultsUpdateDTO = new QuestionnaireResultsUpdateDTO();
		if (isSys == true) {
			XmlSender xmlSender = new XmlSender();
			xmlSender.setUrlString(Resource.getChannel("UPDATE.QUES"));
			xmlSender.setRequestMethod(XmlSender.POST_REQUEST_METHOD);
			xmlSender.setRequestParams(xmlString);
			String xmlResult = xmlSender.send();
			logger.error("发送后：" + xmlResult);
			// String xmlResult =
			// "<IssueUpdateResponse><issueId>8a81432134119c10013428077862000e</issueId><success>true</success></IssueUpdateResponse>";
			try {
				resultsUpdateDTO = this.xmlResultParse(xmlResult);
				if (!("true").equals(resultsUpdateDTO.getSuccess()))
					logger.info("问题件发送上报网点：" + resultsUpdateDTO.getReason());
			} catch (Exception e) {
				logger.error("返回xml转换QuestionnaireresultsupdateDTO对象失败！");
			}
			logger.error("reason:" + resultsUpdateDTO.getReason());
			logger.error("issueId:" + resultsUpdateDTO.getIssueId());
			return resultsUpdateDTO;
		}

		// 构建sendTask对象
		SendTask sendTask = new SendTask();
		sendTask.setOrderId(0);
		sendTask.setClientId("");
		sendTask.setRequestURL(Resource.getChannel("UPDATEQUES"));
		sendTask.setRequestParams(xmlString);
		sendTask.setTaskFlagId(0);
		sendTask.setTaskFlag(String.valueOf(0));

		try {
			logger.error("开始插入数据库");
			sendTaskDao.addSendTask(sendTask);
			logger.error("插入send_task表成功！");
		} catch (Exception e) {
			logger.error("插入数据库失败!", e);

		}

		return resultsUpdateDTO;
	}

	@Override
	public List<BranchMailNoDTO> searchOrgCodeByMailNo(String[] mailNo) {
		List<BranchMailNoDTO> listDtos = new ArrayList<BranchMailNoDTO>();
		if (mailNo == null || mailNo.length == 0) {
			logger.error("运单号数量必须大于0");
			return null;
		}
		if (mailNo.length > 10) {
			logger.error("运单号不能那个大于10");
			return null;
		}

		logger.debug("--------------构建xml查询------------------");

		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("<BatchOrgQueryRequest>");
		sBuffer.append("<mailNos>");
		for (int i = 0; i < mailNo.length; i++) {
			sBuffer.append("<mailNo>");
			sBuffer.append(mailNo[i]);
			sBuffer.append("</mailNo>");
		}
		sBuffer.append("</mailNos>");
		sBuffer.append("</BatchOrgQueryRequest>");

		String dslogisticsInterface = sBuffer.toString();
		String dsdataDigest = "";

		logger.error("构建xml：" + dslogisticsInterface);
		// 获得utf-8 密文
		try {
			dsdataDigest = java.net.URLEncoder.encode(
					Md5Encryption.MD5Encode(dslogisticsInterface
							+ ConfigUtilSingle.getInstance().getPARTERID_HR()),
					"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			logger.error("加密并且转码失败！");
		}
		// 获得明文
		try {
			dslogisticsInterface = java.net.URLEncoder.encode(
					dslogisticsInterface, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("xml转码出现异常");
		}
		// 组装成sendtask中的requestParams参数
		String xmlString = "logistics_interface=" + dslogisticsInterface + "&"
				+ "data_digest=" + dsdataDigest;

		// 向金刚发送Http请求 查询面单信息

		String url = Resource.getChannel("SELECT.MAILNO");
		// logger.error("url:"+url);

		XmlSender xmlSender = new XmlSender();
		xmlSender.setUrlString(url);
		xmlSender.setRequestMethod(XmlSender.POST_REQUEST_METHOD);
		xmlSender.setRequestParams(xmlString);
		String xmlResult = xmlSender.send();

		logger.error(xmlResult);
		// String xmlResult =
		// "<BatchOrgQueryResponse><success>true</success><orgInfos><orgInfo><mailNo>268123456789</mailNo><orgCode>100003</orgCode></orgInfo><orgInfo><mailNo>268123450000</mailNo><orgCode></orgCode><failed>无此运单号</failed></orgInfo></orgInfos></BatchOrgQueryResponse>";
		// String xmlResult
		// ="<BatchOrgQueryResponse><success>false</success><reason>数据签名不匹配 </reason></BatchOrgQueryResponse>";
		// 查询成功！
		if (xmlResult.contains("<success>true</success>")) {
			logger.error("解析xml");
			listDtos = this.xmlParse(xmlResult);
		}
		if (xmlResult.contains("<success>false</success>")) {
			logger.error("查询失败！");
			String[] strings = xmlResult.split("<reason>");
			if (strings.length > 1) {
				String str = strings[1].split("</reason>")[0];
				logger.error("失败原因:" + str);
			}
			return Collections.EMPTY_LIST;
		}
		return listDtos;
	}

	// 解析Questionnarie结果对象
	public QuestionnaireResultsUpdateDTO xmlResultParse(String xmlResult) {
		logger.info("要解析的字符串" + xmlResult);

		QuestionnaireResultsUpdateDTO result = new QuestionnaireResultsUpdateDTO();
		if (StringUtils.isEmpty(xmlResult)) {
			logger.error("要解析的字符串是空值" + xmlResult);
			return result;
		}
		InputStream inputStream = ProcessorUtils.getInputStream(xmlResult);
		Document document = this.documentReader.getDocument(inputStream);
		Element root = document.getDocumentElement();

		NodeList nodeList = root.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if ("issueId".equals(node.getNodeName())) {
				Element issueIdElement = (Element) node;
				if (issueIdElement.getFirstChild() != null) {
					result.setIssueId(issueIdElement.getFirstChild()
							.getNodeValue().trim());
				}
			}
			if ("success".equals(node.getNodeName())) {
				Element successElement = (Element) node;
				if (successElement.getFirstChild() != null) {
					result.setSuccess(successElement.getFirstChild()
							.getNodeValue().trim());
				}
			}
			if ("reason".equals(node.getNodeName())) {
				Element reasonElement = (Element) node;
				if (reasonElement.getFirstChild() != null) {
					result.setReason(reasonElement.getFirstChild()
							.getNodeValue().trim());
				}
			}
		}

		return result;
	}

	// 解析使用mailNo查询的结果
	public List<BranchMailNoDTO> xmlParse(String xmlResult) {
		List<BranchMailNoDTO> listDtos = new ArrayList<BranchMailNoDTO>();
		InputStream inputStream = ProcessorUtils.getInputStream(xmlResult);
		Document document = this.documentReader.getDocument(inputStream);
		Element root = document.getDocumentElement();

		NodeList nodeList = root.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if ("orgInfos".equals(node.getNodeName())) {
				Element orgInfosEle = (Element) node;
				NodeList orginfoNodeList = orgInfosEle.getChildNodes();
				for (int j = 0; j < orginfoNodeList.getLength(); j++) {
					BranchMailNoDTO branchDTO = new BranchMailNoDTO();
					Node orginfoNode = orginfoNodeList.item(j);

					if (orginfoNode != null
							&& "orgInfo".equals(orginfoNode.getNodeName())) {
						Element orgInfoEle = (Element) orginfoNode;
						NodeList mapList = orgInfoEle.getChildNodes();
						for (int k = 0; k < mapList.getLength(); k++) {
							Node mapNode = mapList.item(k);
							if ("mailNo".equals(mapNode.getNodeName())) {
								Element mailNoElement = (Element) mapNode;
								if (mailNoElement.getFirstChild() != null) {
									branchDTO.setMailNo(mailNoElement
											.getFirstChild().getNodeValue()
											.trim());
								}
							}
							if ("orgCode".equals(mapNode.getNodeName())) {
								Element mailNoElement = (Element) mapNode;
								if (mailNoElement.getFirstChild() != null) {
									branchDTO.setOrgCode(mailNoElement
											.getFirstChild().getNodeValue()
											.trim());
									if (branchDTO.getOrgCode() == null) {
										branchDTO.setFailed("");
									}
								}
							}
							if ("failed".equals(mapNode.getNodeName())) {
								Element mailNoElement = (Element) mapNode;
								if (mailNoElement.getFirstChild() != null) {
									branchDTO.setFailed(mailNoElement
											.getFirstChild().getNodeValue()
											.trim());
								}
							}

						}

					}
					listDtos.add(branchDTO);// 将查询出来的对象放入集合中
				}

			}
		}
		return listDtos;
	}

	/**
	 * 自动通知客户。对于部分的问题件类型，请参考Constants.AUTO_NOTIFIER的定义
	 * 
	 * Questionnaire 不是已经传过来了？ 为什么还要 再查一遍？sendExchange 方法里面又查了一遍
	 * id 拿不到？ 可否换其他唯一性的，比如 IssueId
	 * 
	 * @param ques
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean autoNotifyQuestion(Questionnaire ques) {
		boolean flag = false;
		// 获取问题件
		T entity = dao.getQsnByIssueId(ques.getIssueId());
		String siteCode = entity.getBranchId();
		if (siteCode == null || siteCode.isEmpty()) {
			logger.error("自动通知客户时问题件" + entity.getId() + "中的网点编码为空");
			return flag;
		}
		User siteUser = Resource.getUserBySiteCode(siteCode);
		if (siteUser == null) {
			logger.error("自动通知客户时问题件中的网点编码对应的用户不存在");
			return flag;
		}

		// YITONG-2142 揽件网点自己上报的问题件无需推送给卖家。
		String branchId = entity.getBranchId(); // 揽收网点
		String reportBranckCode = entity.getReportBranckCode(); // 上报网点
		if (StringUtils.equals(branchId, reportBranckCode)) {
			logger.error("揽收网点为问题件上报网点时，无需推送给卖家！");
			return flag;
		}

		// 首先判断该用户是否开启了自动通知功能，如果开启了则自动发送任意问题件；否则只发送部分类型的问题件
		//User user = Resource.getUserByCustomerId(ques.getCustomerId());
		User user = Resource.getUserByCustomerId(ques.getCustomerId());
		if (user != null && Resource.getAutoNotifyUser(user.getUserName())) {// 表示网点开启了问题件自动通知客户功能,则自动通知所有类型的问题件
			QuestionaireExchange questionaireExchange = new QuestionaireExchange();
			questionaireExchange.setMsgContent(entity.getIssueDesc());
			questionaireExchange.setOperatorName(StringUtils
					.isNotBlank(siteUser.getUserNameText()) ? siteUser
					.getUserNameText() : siteUser.getUserName());
			questionaireExchange.setQuestionaireId(entity.getId());
			questionaireExchange.setUserId(siteUser.getId());
			flag = questionaireExchangeService.sendExchange(siteUser,
					questionaireExchange);
			if (!flag) {
				logger.error("自动通知客户时问题件" + entity.getId() + "失败");
			}
		} else {// 表示没有开启自动通知客户功能
			/**
			 * 判断该问题件类型是否属于自动通知范畴
			 */
			if (entity.getFeedbackInfo() != null
					&& !("").equals(entity.getFeedbackInfo())) {
				if (ArrayUtils.contains(Constants.AUTO_NOTIFIER,
						entity.getFeedbackInfo())) {
					QuestionaireExchange questionaireExchange = new QuestionaireExchange();
					questionaireExchange.setMsgContent(entity.getIssueDesc());
					questionaireExchange.setOperatorName(StringUtils
							.isNotBlank(siteUser.getUserNameText()) ? siteUser
							.getUserNameText() : siteUser.getUserName());
					questionaireExchange.setQuestionaireId(entity.getId());
					questionaireExchange.setUserId(siteUser.getId());
					flag = questionaireExchangeService.sendExchange(siteUser,
							questionaireExchange);
					if (!flag) {
						logger.error("自动通知客户时问题件" + entity.getId() + "失败");
					}
				}
			}
		}
		return flag;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<T> siteQueryQuestionnaire(User siteUser, String mailNO) {
		if (siteUser.getSite() == null || siteUser.getSite().equals("")) // 在网点编码不存在的情况返回null
			return null;
		Map map = new HashMap();
		String branchId = siteUser.getSite();
		if (siteUser.getUserType().equals("2")
				&& siteUser.getParentId() != null)
			branchId = userService.getUserById(siteUser.getParentId())
					.getSite();
		if (siteUser.getUserType().equals("21")
				|| siteUser.getUserType().equals("22")
				|| siteUser.getUserType().equals("23")) {
			// 如果是子账号的话还要判断是否是承包区的子账号。
			User u = userService.getUserById(siteUser.getParentId());
			if (u.getParentId() != null)
				branchId = userService.getUserById(u.getParentId()).getSite();
		}
		map.put("branchId", branchId);
		// 默认按DESC排序:（注：前端sortType取值范围为1(升序)、2(降序)两种）
		map.put("sortType", 2);
		/**
		 * 网点查询所有已激活卖家的用户customerId.此时根据网点编码获取其已激活卖家。
		 * 此处分两种情况：一种是网点查询多有激活的卖家的用户customerId,包括承包出去的客户；第二种是承包区只查询属于自己承包的客户。
		 */
		List<UserThreadContract> contractList = userThreadContractService
				.getContractersByUserNameAndType(siteUser.getUserName(),
						siteUser.getUserType());
		List<UserThread> utList = new ArrayList<UserThread>();
		if (contractList.size() == 0) {
			if (siteUser.getParentId() != null) { // 承包区，客服，财务没有分配客户的子账号
				utList = null;
			} else { // 网点账号
				utList = userThreadService
						.searchUsersBySite(siteUser.getSite());
			}
		} else {
			for (int i = 0; i < contractList.size(); i++) {
				UserThread ut = userThreadService.getByIdAndState(contractList
						.get(i).getConractAreaId());
				if (ut != null) {
					utList.add(ut);
				}
			}
		}
		if (utList != null && utList.size() > 0) {
			List<String> customerIdList = new ArrayList<String>();
			for (UserThread ut : utList) {
				List<String> resultList = this.getCustomerIdByUserCode(ut
						.getUserCode());
				if (resultList != null)
					customerIdList.addAll(resultList);
			}
			map.put("customerId", customerIdList);
		} else {
			return null;
		}
		if (mailNO != null && mailNO.trim().length() > 0)
			map.put("mailNO", mailNO.trim());
		return dao.queryQuestionnaireManageList(map);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<T> vipQueryQuestionnaire(User vipUser, String mailNO) {
		Map map = new HashMap();
		// 在使用关联账号查询所有该用户编码下的customerId时使用这个条件
		List<String> bindedId = Resource.getBindedCustomerIdList(vipUser);
		if (bindedId != null && bindedId.size() > 0) {
			map.put("customerId", bindedId);
		} else
			// 在bindedId为null时返回空
			return null;
		map.put("dealStatus", "2");// 卖家只关注网点已通知状态下的问题件
		// 默认按DESC排序
		map.put("sortType", 2);
		if (mailNO != null && mailNO.trim().length() > 0)
			map.put("mailNO", mailNO.trim());
		return dao.queryQuestionnaireManageList(map);
	}

	@Override
	public boolean moveQuestion(User curUser, Integer[] questionIds,
			Integer tagId) {
		boolean flag = true;
		/**
		 * 首先判断当前tagId对应的标签是不是“已处理”，是的话将问题件移动到已处理状态下
		 */
		QuestionnaireTag questionnaireTag = questionnaireTagService
				.getQestionnaireTag(tagId);
		if (questionnaireTag != null
				&& ("已处理").equals(questionnaireTag.getTagName())) {
			for (Integer id : questionIds) {
				moveQuestion(curUser, id, 3, tagId);
			}
		} else {
			for (Integer questionId : questionIds) {
				Questionnaire questionnaire = getQestionnaireById(questionId);
				questionnaire.setTagId(tagId);
				questionnaire.setVipStatus("2");
				questionnaire.setMjIsRead("1");
				questionnaire.setDealTime(new Date());
				questionnaire.setPartitionDate(new Date());
				try {
					dao.editQuestionnaire((T) questionnaire);
				} catch (Exception e) {
					logger.error(e.toString());
					flag = false;
					return flag;
				}
			}
		}
		return flag;
	}

	@Override
	public String export(List<String> bindedId, String starttime,
			String endtime, String mailNO, List<String> questionType,
			String vipStatus, String contactWay, String buyerName,
			Integer isShowSigned, Integer tagId) {
		StringBuffer exportString = new StringBuffer();
		if (vipStatus.equals("1") || vipStatus.equals("3")) {// 新问题件和已处理问题件格式相同
			exportString.append(Constants.EXPORTFORMATONE);
		} else {// 处理中问题件
			exportString.append(Constants.EXPORTFORMATTWO);
		}
		List<Questionnaire> questionList = (List<Questionnaire>) vipQueryQuestionnaireManageList(
				bindedId, null, starttime, endtime, mailNO, questionType,
				vipStatus, null, null, contactWay, buyerName,
				isShowSigned.toString(), tagId, null);
		if (questionList == null || questionList.size() == 0) {
			return exportString.toString();
		}
		for (Questionnaire questionnaire : questionList) {
			exportString.append("\r\n");
			// "旺旺号, 运单号, 问题件上报时间, 问题件类型, 问题件描述, 买家姓名, 买家电话, 上报网点, 店铺名称"
			exportString
					.append(StringUtils.defaultIfEmpty(
							questionnaire.getTaobaoLoginName(), ""))
					.append(",")
					.append(StringUtils.defaultIfEmpty(
							questionnaire.getMailNO(), "")).append(",");
			if (vipStatus.equals("2")) {// 处理中问题件格式
				QuestionnaireTag tag = questionnaireTagService
						.getQestionnaireTag(questionnaire.getTagId());
				if (tag != null) {
					exportString.append(
							StringUtils.defaultIfEmpty(tag.getTagName(), ""))
							.append(",");
				} else {
					exportString.append("").append(",");
				}
			}
			exportString.append(
					StringUtils.defaultIfEmpty(DateUtil.toSeconds(questionnaire
							.getIssueCreateTime()), "")).append(",");
			exportString.append(
					StringUtils.defaultIfEmpty(
							getQuestionTypeString(questionnaire
									.getFeedbackInfo()), "")).append(",");
			// 获取问题件描述
			List<Integer> questionnaireIds = new ArrayList<Integer>();
			questionnaireIds.add(questionnaire.getId());
			List<QuestionaireExchange> questionaireExchangeList = questionaireExchangeService
					.getListByQuestionaireIds(questionnaireIds);
			if (questionaireExchangeList != null
					&& questionaireExchangeList.size() > 0) {
				// 反馈信息的第一条记录显示在卖家的问题描述中：因查询时按时间降序查询的，所以在显示时第一条反馈就是列表中最后一条记录
				String content = questionaireExchangeList.get(
						questionaireExchangeList.size() - 1).getMsgContent();
				if (StringUtils.isNotEmpty(content)) {
					content = content.replaceAll(",", "，");
					content = content.replaceAll("\t", " ");
					content = content.replaceAll("\n", " ");
					exportString.append(content).append(",");
				} else {
					exportString.append("").append(",");
				}
			} else {
				exportString.append("").append(",");
			}
			exportString
					.append(StringUtils.defaultIfEmpty(
							questionnaire.getBuyerName(), "")).append(",");
			if (StringUtils.isNotEmpty(questionnaire.getBuyerMobile())) {
				exportString.append(questionnaire.getBuyerMobile()).append(",");
			} else {
				exportString.append(
						StringUtils.defaultIfEmpty(
								questionnaire.getBuyerPhone(), "")).append(",");
			}
			exportString.append(
					StringUtils.defaultIfEmpty(
							questionnaire.getReportBranckText(), "")).append(
					",");
			// 设置店铺
			User user = userService.getUserByCustomerId(questionnaire
					.getCustomerId());
			exportString.append(StringUtils.defaultIfEmpty(user.getShopName(),
					""));
		}
		return exportString.toString();
	}

	private String getQuestionTypeString(String questionType) {
		Map<String, String> dealMap = new HashMap<String, String>();
		dealMap.put("PR11", "快件到我公司已破损");
		dealMap.put("PR100", "有进无出");
		dealMap.put("PR110", "有出无进");
		dealMap.put("PR120", "有单无货");
		dealMap.put("PR130", "有货无单");
		dealMap.put("PR210", "收件客户拒收");
		dealMap.put("PR211", "地址不详电话联系不上");
		dealMap.put("PR212", "收件客户已离职");
		dealMap.put("PR213", "收件客户要求改地址");
		dealMap.put("PR214", "地址不详且电话为传真或无人接听");
		dealMap.put("PR215", "地址不详电话与收件客户本人不符");
		dealMap.put("PR216", "地址不详无收件人的电话");
		dealMap.put("PR220", "错发");
		dealMap.put("PR230", "延误");
		dealMap.put("PR240", "遗失");
		dealMap.put("PR250", "违禁品");
		dealMap.put("PR260", "快件污染");
		dealMap.put("PR50", "到付费");
		dealMap.put("PR60", "代收款");
		dealMap.put("PR270", "其它原因");
		dealMap.put("PR001", "无头件上报");
		dealMap.put("PR002", "面单填写不规范");
		dealMap.put("R70", "节假日客户休息");
		dealMap.put("PR10", "破损");
		dealMap.put("PR20", "超区");
		dealMap.put("PR30", "内件短少");
		dealMap.put("PR40", "超重");
		dealMap.put("PR140", "签收失败（自动上报）");
		if (StringUtils.isNotEmpty(questionType)) {
			return dealMap.get(questionType);
		} else {
			return "";
		}
	}

	@Override
	public String siteWayQuestionnaire(User siteUser, String mailNO) {

		if (siteUser.getSite() == null || siteUser.getSite().equals("")) // 在网点编码不存在的情况返回null
			return null;
		Map map = new HashMap();
		String branchId = siteUser.getSite();
		if (siteUser.getUserType().equals("2")
				&& siteUser.getParentId() != null) {// 承包区类型
			branchId = userService.getUserById(siteUser.getParentId())
					.getSite();
		} else if (siteUser.getUserType().equals("21")
				|| siteUser.getUserType().equals("22")
				|| siteUser.getUserType().equals("23")) {
			// 如果是子账号的话还要判断是否是承包区的子账号。
			User u = userService.getUserById(siteUser.getParentId());
			if (u.getParentId() != null)
				branchId = userService.getUserById(u.getParentId()).getSite();
		}
		map.put("branchId", branchId);
		if (mailNO != null && mailNO.trim().length() > 0)
			map.put("mailNO", mailNO.trim());
		return dao.wayQuestionnaireManageList(map);
	}

	@Override
	public String vipWayQuestionnaire(User vipUser, String mailNO) {

		Map map = new HashMap();
		// 在使用关联账号查询所有该用户编码下的customerId时使用这个条件
		List<String> bindedId = Resource.getBindedCustomerIdList(vipUser);
		if (bindedId != null && bindedId.size() > 0) {
			map.put("customerId", bindedId);
		} else
			// 在bindedId为null时返回空
			return null;
		map.put("dealStatus", "2");// 卖家只关注网点已通知状态下的问题件
		if (mailNO != null && mailNO.trim().length() > 0)
			map.put("mailNO", mailNO.trim());
		return dao.wayQuestionnaireManageList(map);

	}
}
