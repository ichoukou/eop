/**
 * MonitorServiceImpl.java
 * Wangyong
 * 2011-8-9 上午09:53:53
 */
package net.ytoec.kernel.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.remote.TaoBaoOrderAction;
import net.ytoec.kernel.action.remote.xml.PassMessageQueryOrder;
import net.ytoec.kernel.action.remote.xml.QueryOrder;
import net.ytoec.kernel.action.remote.xml.StepInfo;
import net.ytoec.kernel.common.OrderTypeEnum;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.StatusEnum;
import net.ytoec.kernel.dataobject.AttentionMail;
import net.ytoec.kernel.dataobject.MonitorBean;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dto.PassIssueDTO;
import net.ytoec.kernel.search.dto.EccoreSearchResultDTO;
import net.ytoec.kernel.search.service.EccoreSearchService;
import net.ytoec.kernel.service.AttentionMailService;
import net.ytoec.kernel.service.MonitorService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.QuestionnaireService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.util.ConfigUtilSingle;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Wangyong
 * @2011-8-9 net.ytoec.kernel.service.impl
 */
@Service
@Transactional
@SuppressWarnings("all")
public class MonitorServiceImpl<T extends MonitorBean> implements
		MonitorService<T> {

	private static Logger logger = LoggerFactory.getLogger(MonitorServiceImpl.class);
	@Inject
	private QuestionnaireService<Questionnaire> questionnaireService;

	@Inject
	private OrderService<Order> orderService;

	@Inject
	private AttentionMailService<AttentionMail> attenService;
	@Inject
	private UserService<User> userService;

	@Inject
	private EccoreSearchService eccoreSearchService;

	@Override
	public List<T> getOrderMonitor(String customerId, Integer status,
			String startTime, String endTime, Integer numProv, Integer numCity,
			Integer numDistrict, String mailNO, String buyerName,
			String buyerPhone, Integer orderBy, Pagination pagination,
			boolean flag, List bindedId, String mailNoOrderType) {
		List<T> monitorList = new ArrayList<T>();
		pagination = buildSearchPage(status, startTime, endTime, numProv,
				numCity, numDistrict, mailNO, buyerName, buyerPhone, orderBy,
				pagination, bindedId, mailNoOrderType);
		try {
			if (pagination != null) {
				eccoreSearchService.searchEccoreData(ConfigUtilSingle
						.getInstance().getSolrEccoreUrl(), pagination);
				List<EccoreSearchResultDTO> eccoreSearchList = pagination
						.getRecords();
				if (eccoreSearchList != null && eccoreSearchList.size() > 0) {
					for (EccoreSearchResultDTO eccore : eccoreSearchList) {
						MonitorBean monitor = new MonitorBean();
						if (eccore.getId() != null)
							monitor.setId(eccore.getId());
						if (eccore.getMailNo() != null) {
							monitor.setMailNO(eccore.getMailNo());
							/**
							 * 判断是否被关注
							 */
							List<AttentionMail> list = attenService
									.searchByMailNoAndCustomerId(
											eccore.getMailNo(), customerId);
							if (list != null && list.size() > 0)
								monitor.setIsAttention(1);
						}
						if (eccore.getStatus() != null)
							monitor.setStatus(eccore.getStatus());
						if (eccore.getLineType() != null)
							monitor.setLineType(eccore.getLineType().toString());
						if (eccore.getName() != null)
							monitor.setUserName(eccore.getName());
						if (eccore.getPhone() != null
								&& eccore.getPhone() != "")
							monitor.setPhone(eccore.getPhone());
						if (eccore.getAddress() != null)
							monitor.setDestination(eccore.getProv()
									+ eccore.getCity() + eccore.getDistrict()
									+ eccore.getAddress());
						if (eccore.getCreateTime() != null) {
							monitor.setSenderTime(eccore.getCreateTime());
							monitor.setArriveTime(dateArithmetic(
									eccore.getCreateTime(), 5));// 设置预计时间为创建时间之后的五天
							// 判断创建时间7天内寄件是否超时
							if ((dateArithmetic(eccore.getCreateTime(), 7)
									.getTime()) > (new Date().getTime()))
								monitor.setDateOut(1);// 未超时
							else
								monitor.setDateOut(2);// 已超时
						}
						if (eccore.getAcceptTime() != null) {
							monitor.setAcceptTime(eccore.getAcceptTime());
						}
						/**
						 * 设置店铺名称
						 */
						if (eccore.getCustomerId() != null) {
							User u = userService.getUserByCustomerId(eccore
									.getCustomerId());
							monitor.setShopName(u.getShopName());
						}

						/**
						 * 在走件列表和派送中需要显示当条运单的最近一条物流信息
						 */
						if (status.equals(2) || status.equals(3)||status.equals(5)) {
							TaoBaoOrderAction op = new TaoBaoOrderAction();
							String[] logisticsId = { eccore.getMailNo() };
							try {
								List<QueryOrder> logisticList = op
										.ordersSearch(logisticsId);// 运单物流信息
								if (logisticList != null
										&& logisticList.size() > 0) {
									QueryOrder qo = logisticList.get(0);
									if (qo.getSteps() != null
											&& qo.getSteps().size() > 0) {
										StepInfo lastStep = qo.getSteps().get(
												qo.getSteps().size() - 1);
										monitor.setStepInfo(lastStep);
									}
								}
							} catch (Exception e) {
							}
						}
						monitorList.add((T) monitor);
					}
					return monitorList;
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return new ArrayList();
	}

	@Override
	public int countOrderMonitorList(boolean isAll, Integer curUserId,
			Integer status, String startTime, String endTime, String mailNO,
			String buyerName, String buyerPhone, List bindedId,
			Integer numProv, Integer numCity, Integer numDistrict,
			Pagination pagination, String mailNoOrderType) {
		int result = 0;
		if (isAll) {
			/**
			 * 平台用户在查询所有属于自己的业务账号的时候根据主键curUserId获取所有仓库及入驻企业
			 */
			List<String> custormerIdList = new ArrayList<String>();
			User curPlatformUser = userService.getUserById(curUserId);// 获取当前平台用户
			List<User> userList = userService.pingTaiSelect(curPlatformUser, 0);// 获取所有仓库及入驻企业
			if (userList != null && userList.size() > 0) {
				for (User user : userList) {
					if (user.getTaobaoEncodeKey() != null)
						custormerIdList.add(user.getTaobaoEncodeKey());
				}
			}
			pagination = buildGroupSearchPage(startTime, endTime, numProv,
					numCity, numDistrict, mailNO, buyerName, buyerPhone, 0,
					pagination, custormerIdList, mailNoOrderType);
		} else {
			pagination = buildGroupSearchPage(startTime, endTime, numProv,
					numCity, numDistrict, mailNO, buyerName, buyerPhone, 0,
					pagination, bindedId, mailNoOrderType);
		}
		try {
			if (pagination != null) {
				Map<Short, Integer> map = eccoreSearchService
						.searchEccoreDataGroupStatus(ConfigUtilSingle
								.getInstance().getSolrEccoreUrl(), pagination);
				if (status == 1) {
					if (map.get(StatusEnum.SIGNED.getValue()) != null)
						result = map.get(StatusEnum.SIGNED.getValue());
				} else if (status == 2) {
					if (map.get(StatusEnum.SENT_SCAN.getValue()) != null)
						result = map.get(StatusEnum.SENT_SCAN.getValue());
				} else if (status == 3) {
					if (map.get(StatusEnum.ACCEPT.getValue()) != null)
						result += map.get(StatusEnum.ACCEPT.getValue());
					if (map.get(StatusEnum.UNACCEPT.getValue()) != null)
						result += map.get(StatusEnum.UNACCEPT.getValue());
					if (map.get(StatusEnum.GOT.getValue()) != null)
						result += map.get(StatusEnum.GOT.getValue());
					if (map.get(StatusEnum.NOT_SEND.getValue()) != null)
						result += map.get(StatusEnum.NOT_SEND.getValue());
					if (map.get(StatusEnum.FAILED.getValue()) != null)
						result += map.get(StatusEnum.FAILED.getValue());
					if (map.get(StatusEnum.UNKNOW.getValue()) != null)
						result += map.get(StatusEnum.UNKNOW.getValue());
				} else if (status == 4) {
					map = eccoreSearchService.searchEccoreDataGroupOrderType(
							ConfigUtilSingle.getInstance().getSolrEccoreUrl(),
							pagination);
					if (map.get(OrderTypeEnum.MRB.getValue()) != null)
						result = map.get(OrderTypeEnum.MRB.getValue());
				} else if (status == 5) {
					if (map.get(StatusEnum.CREATE.getValue()) != null)
						result += map.get(StatusEnum.CREATE.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<T> getMerchantMonitorOrderList(String customerId,
			Integer status, String startTime, String endTime, Integer numProv,
			Integer numCity, Integer numDistrict, Integer vipId, String mailNO,
			String buyerName, String buyerPhone, Integer orderBy,
			Pagination pagination, boolean flag, String mailNoOrderType) {
		List<T> monitorList = new ArrayList<T>();
		/**
		 * 平台用户在查询所有属于自己的业务账号和入驻企业的时候根据vipId获取
		 */
		List<String> bindedId = new ArrayList<String>();
		User curPlatformUser = userService.getUserById(vipId);// 获取当前平台用户
		List<User> userList = userService.pingTaiSelect(curPlatformUser, 0);// 获取业务账号和入驻企业
		if (userList != null && userList.size() > 0) {
			for (User user : userList) {
				if (user.getTaobaoEncodeKey() != null)
					bindedId.add(user.getTaobaoEncodeKey());
			}
		}
		pagination = buildSearchPage(status, startTime, endTime, numProv,
				numCity, numDistrict, mailNO, buyerName, buyerPhone, orderBy,
				pagination, bindedId, mailNoOrderType);
		try {
			if (pagination != null) {
				eccoreSearchService.searchEccoreData(ConfigUtilSingle
						.getInstance().getSolrEccoreUrl(), pagination);
				List<EccoreSearchResultDTO> eccoreSearchList = pagination
						.getRecords();
				if (eccoreSearchList != null && eccoreSearchList.size() > 0) {
					for (EccoreSearchResultDTO eccore : eccoreSearchList) {
						MonitorBean monitor = new MonitorBean();
						if (eccore.getId() != null)
							monitor.setId(eccore.getId());
						if (eccore.getMailNo() != null) {
							monitor.setMailNO(eccore.getMailNo());
							/**
							 * 判断是否被关注
							 */
							List<AttentionMail> list = attenService
									.searchByMailNoAndCustomerId(
											eccore.getMailNo(), customerId);
							if (list != null && list.size() > 0)
								monitor.setIsAttention(1);

						}
						if (eccore.getStatus() != null)
							monitor.setStatus(eccore.getStatus());
						if (eccore.getLineType() != null)
							monitor.setLineType(eccore.getLineType().toString());
						if (eccore.getName() != null)
							monitor.setUserName(eccore.getName());
						if (eccore.getPhone() != null
								&& eccore.getPhone() != "")
							monitor.setPhone(eccore.getPhone());
						if (eccore.getAddress() != null)
							monitor.setDestination(eccore.getProv()
									+ eccore.getCity() + eccore.getDistrict()
									+ eccore.getAddress());
						if (eccore.getCreateTime() != null) {
							monitor.setSenderTime(eccore.getCreateTime());
							monitor.setArriveTime(dateArithmetic(
									eccore.getCreateTime(), 5));// 设置预计时间为创建时间之后的五天
							// 判断创建时间7天内寄件是否超时
							if ((dateArithmetic(eccore.getCreateTime(), 7)
									.getTime()) > (new Date().getTime()))
								monitor.setDateOut(1);// 未超时
							else
								monitor.setDateOut(2);// 已超时
						}
						if (eccore.getAcceptTime() != null) {
							monitor.setAcceptTime(eccore.getAcceptTime());
						}
						if (eccore.getCustomerId() != null
								&& !"".equals(eccore.getCustomerId())) {
							User u = userService.getUserByCustomerId(eccore
									.getCustomerId());
							if (u != null)
								monitor.setShopName(u.getUserName());
						}
						/**
						 * 在走件列表和派送中需要显示当条运单的最近一条物流信息
						 */
						if (status.equals(2) || status.equals(3)||status.equals(5)) {
							TaoBaoOrderAction op = new TaoBaoOrderAction();
							String[] logisticsId = { eccore.getMailNo() };
							try {
								List<QueryOrder> logisticList = op
										.ordersSearch(logisticsId);// 运单物流信息
								if (logisticList != null
										&& logisticList.size() > 0) {
									QueryOrder qo = logisticList.get(0);
									if (qo.getSteps() != null
											&& qo.getSteps().size() > 0) {
										StepInfo lastStep = qo.getSteps().get(
												qo.getSteps().size() - 1);
										monitor.setStepInfo(lastStep);
									}
								}
							} catch (Exception e) {
							}
						}
						monitorList.add((T) monitor);
					}
					return monitorList;
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return new ArrayList();
	}

	/**
	 * 日期运算方法:在原来日期基础上加天数
	 * 
	 * @param originalDate
	 *            原来的日期
	 * @param countDay
	 *            在原来日期基础上加的天数
	 * @return 返回增加count天数后的日期
	 */
	private static Date dateArithmetic(Date originalDate, int countDay) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(originalDate);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		// 在当前日期上加countDay天数
		cal.add(Calendar.DAY_OF_MONTH, countDay);
		return cal.getTime();
	}

	/**
	 * 在原始日期originalDate上减去countDay天数
	 * 
	 * @param originalDate
	 * @param countDay
	 * @return
	 */
	private static String dateArithmeticMinus(Date originalDate, int countDay) {
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
			result = dateArithmeticMinus(next, days);
		}
		return result;
	}

	/**
	 * 获取关注运单 (先在数据库中获取符合除地址之外的运单，然后通过查出运单的customId和mailNo在搜索引擎查询然后过滤掉与地址不相符的运单)
	 * @param isPingtai
	 * @param customerId
	 * @param startTime
	 * @param endTime
	 * @param numProv
	 * @param numCity
	 * @param numDistrict
	 * @param mailNO
	 * @param buyerName
	 * @param buyerPhone
	 * @param orderBy
	 * @param pagination
	 * @param bindedId
	 * @return
	 */
	public List<T> getAttentionList(boolean isPingtai, String customerId, String startTime, String endTime, Integer numProv, Integer numCity, Integer numDistrict,
			String mailNO, String buyerName, String buyerPhone,
			Integer orderBy, Pagination pagination, List bindedId) {
		// 从数据库中获取没有过滤掉地址条件的关注运单
		if(isPingtai){
			if(!bindedId.isEmpty())
				bindedId.clear();
			bindedId.add(customerId);
		}
		List<AttentionMail> amList = attenService.searchPaginationList(startTime, endTime, mailNO, buyerName, buyerPhone, orderBy, pagination, bindedId);
		// 最终符合条件的运单
		List<T> monitorList = new ArrayList<T>();
		
		if (amList != null && amList.size() > 0) {
			for (AttentionMail attentionMail : amList) {
				MonitorBean monitor = new MonitorBean();
				monitor.setId(attentionMail.getId());
				monitor.setMailNO(attentionMail.getMailNo());
				monitor.setLineType(attentionMail.getLineType());
				monitor.setUserName(attentionMail.getBuyerName());
				monitor.setPhone(attentionMail.getBuyerPhone());
				monitor.setDestination(attentionMail.getDestination());
				monitor.setSenderTime(attentionMail.getSendTime());
				
				/*	// 关注运单是否超时
					monitor.setDateOut(1);  // 1 : 未超时,  2 : 已超时
				 */			
				
				// 预计到达时间
				monitor.setArriveTime(dateArithmetic(attentionMail.getSendTime(), 5)); // 发货时间五天之后
				monitor.setAcceptTime(attentionMail.getAcceptTime());
				
				/**
				 *  根据搜索引擎判断地址条件是否满足
				 */
		       Pagination<EccoreSearchResultDTO> searchPage = new Pagination<EccoreSearchResultDTO>(1, 15);
               Map<String, String> searchParams = new HashMap<String, String>();
               StringBuffer customerIds = new StringBuffer();
               customerIds.append("mailNoAndCustomerId:"+attentionMail.getMailNo() + attentionMail.getCustomerId());
               searchParams.put("mailNoAndCustomerId", customerIds.toString());
               if (numProv != null) {
            	   searchParams.put("numProv", numProv + "");
               }
               if (numCity != null) {
            	   searchParams.put("numCity", numCity + "");
               }
               if (numDistrict != null) {
            	   searchParams.put("numDistrict", numDistrict + "");
               }
               searchPage.setParams(searchParams);
               try {
				eccoreSearchService.searchOrderByMailNoAndCustomerId(ConfigUtilSingle.getInstance().getSolrEccoreUrl(), searchPage);
				} catch (Exception e1) {
					logger.error("关注订单~~~~~~搜索引擎查询订单时出错", e1);
				}
               List<EccoreSearchResultDTO> eccoreSearchResultDTOs = searchPage.getRecords();
               if (eccoreSearchResultDTOs != null && eccoreSearchResultDTOs.size() > 0) {
            	   monitor.setStatus(eccoreSearchResultDTOs.get(0).getStatus());
               }
				/**
				 * 显示最近一条物流信息
				 */
				TaoBaoOrderAction op = new TaoBaoOrderAction();
				String[] logisticsId = { attentionMail.getMailNo() };
				try {
					List<QueryOrder> logisticList = op
							.ordersSearch(logisticsId);// 运单物流信息
					if (logisticList != null && logisticList.size() > 0) {
						QueryOrder qo = logisticList.get(0);
						if (qo.getSteps() != null && qo.getSteps().size() > 0) {
							StepInfo lastStep = qo.getSteps().get(
									qo.getSteps().size() - 1);
							monitor.setStepInfo(lastStep);
						}
					}
				} catch (Exception e) {
				}
				if (eccoreSearchResultDTOs != null && eccoreSearchResultDTOs.size() > 0) {
					if (monitorList.size() <= 50) {
						monitorList.add((T) monitor); 
					} else {
						return monitorList;
					}
				}
			}
		}
		return monitorList;
	}

	/**
	 * 列表build search条件
	 * 
	 * @param status
	 *            1(成功订单)、2(正在派送)、3(走件中)、4(退货单)、5(接单中)
	 * @param startTime
	 *            对应solr startDate
	 * @param endTime
	 *            对应solr endDate
	 * @param numProv
	 *            省id
	 * @param numCity
	 *            市id
	 * @param numDistrict
	 *            县区id
	 * @param mailNO
	 *            对应solr mailNo
	 * @param buyerName
	 *            对应solr name
	 * @param buyerPhone
	 *            对应solr phone
	 * @param orderBy
	 *            按时间排序方式 ：1表示升序，2表示降序 对应solr sortType desc or asc 默认desc
	 * @param pagination
	 * @param bindedId
	 * @return
	 */
	private Pagination<EccoreSearchResultDTO> buildSearchPage(Integer status,
			String startTime, String endTime, Integer numProv, Integer numCity,
			Integer numDistrict, String mailNO, String buyerName,
			String buyerPhone, Integer orderBy, Pagination pagination,
			List bindedId, String mailNoOrderType) {
		/**
		 * 订单状态和时间及客户bindedId非空
		 */
		if (status == null || startTime == null || endTime == null
				|| startTime.equals("") || endTime.equals(""))
			return null;
		if (bindedId == null || bindedId.size() == 0)
			return null;
		Map<String, String> map = new HashMap<String, String>();
		if (status != null) {
			if (status.equals(1)) {
				map.put("numStatus", StatusEnum.SIGNED.getValue().toString());
			} else if (status.equals(2)) {
				map.put("numStatus", StatusEnum.SENT_SCAN.getValue().toString());
			} else if (status.equals(3)) {
				/**
				 * 走件中订单状态包括ACCEPT、UNACCEPT、GOT、NOT_SEND、FAILED、UNKNOW
				 * 将这些条件的值作为一个条件以or相隔开，再作为条件传给solr引擎
				 */
				String relationStr = " OR numStatus:";
				StringBuffer sb = new StringBuffer();
				sb.append(StatusEnum.ACCEPT.getValue());
				sb.append(relationStr);
				sb.append(StatusEnum.UNACCEPT.getValue());
				sb.append(relationStr);
				sb.append(StatusEnum.GOT.getValue());
				sb.append(relationStr);
				sb.append(StatusEnum.NOT_SEND.getValue());
				sb.append(relationStr);
				sb.append(StatusEnum.FAILED.getValue());
				sb.append(relationStr);
				sb.append(StatusEnum.UNKNOW.getValue());
				map.put("numStatus", sb.toString());
			} else if (status.equals(4)) {
				map.put("orderType",
						Integer.valueOf(OrderTypeEnum.MRB.getValue())
								.toString());
			} else if (status.equals(5)) {
				map.put("numStatus", StatusEnum.CREATE.getValue().toString());
			}
		}
		if (startTime != null && !startTime.equals("")) {
			map.put("startDate", startTime);
		}
		if (endTime != null && !endTime.equals("")) {
			map.put("endDate", endTime);
		}
		if (bindedId != null && bindedId.size() > 0) {
			StringBuffer customerIDs = new StringBuffer();
			for (int i = 0; i < bindedId.size(); i++) {
				if (i != 0)
					customerIDs.append(",");
				customerIDs.append(bindedId.get(i));
			}
			map.put("customerIDs", customerIDs.toString());
		}
		if (numProv != null)
			map.put("numProv", numProv.toString());
		if (numCity != null)
			map.put("numCity", numCity.toString());
		if (numDistrict != null)
			map.put("numDistrict", numDistrict.toString());
		if (mailNO != null && !(mailNO.trim()).equals(""))
			map.put("mailNo", mailNO);
		if (buyerName != null && !(buyerName.trim()).equals(""))
			map.put("name", buyerName);
		if (buyerPhone != null && !(buyerPhone.trim()).equals(""))
			map.put("phone", buyerPhone);
		if (mailNoOrderType != null && !(mailNoOrderType.trim()).equals("")) {
			map.put("orderType", mailNoOrderType);
		}
		if (orderBy == 1) {
			map.put("sortType", "asc");
		} else
			map.put("sortType", "desc");
		pagination.setParams(map);
		return pagination;
	}

	/**
	 * 统计build search条件，不需要传递状态值
	 * 
	 * @param startTime
	 *            对应solr startDate
	 * @param endTime
	 *            对应solr endDate
	 * @param numProv
	 *            省id
	 * @param numCity
	 *            市id
	 * @param numDistrict
	 *            县区id
	 * @param mailNO
	 *            对应solr mailNo
	 * @param buyerName
	 *            对应solr name
	 * @param buyerPhone
	 *            对应solr phone
	 * @param orderBy
	 *            按时间排序方式 ：1表示升序，2表示降序 对应solr sortType desc or asc 默认desc
	 * @param pagination
	 * @param bindedId
	 * @return
	 */
	private Pagination<EccoreSearchResultDTO> buildGroupSearchPage(
			String startTime, String endTime, Integer numProv, Integer numCity,
			Integer numDistrict, String mailNO, String buyerName,
			String buyerPhone, Integer orderBy, Pagination pagination,
			List bindedId, String mailNoOrderType) {
		/**
		 * 订单状态和时间及客户bindedId非空
		 */
		if (startTime == null || endTime == null || startTime.equals("")
				|| endTime.equals(""))
			return null;
		if (bindedId == null || bindedId.size() == 0)
			return null;
		Map<String, String> map = new HashMap<String, String>();
		if (startTime != null && !startTime.equals("")) {
			map.put("startDate", startTime);
		}
		if (endTime != null && !endTime.equals("")) {
			map.put("endDate", endTime);
		}
		if (bindedId != null && bindedId.size() > 0) {
			StringBuffer customerIDs = new StringBuffer();
			for (int i = 0; i < bindedId.size(); i++) {
				if (i != 0)
					customerIDs.append(",");
				customerIDs.append(bindedId.get(i));
			}
			map.put("customerIDs", customerIDs.toString());
		}
		if (numProv != null)
			map.put("numProv", numProv.toString());
		if (numCity != null)
			map.put("numCity", numCity.toString());
		if (numDistrict != null)
			map.put("numDistrict", numDistrict.toString());
		if (mailNO != null && !(mailNO.trim()).equals(""))
			map.put("mailNo", mailNO);
		if (buyerName != null && !(buyerName.trim()).equals(""))
			map.put("name", buyerName);
		if (buyerPhone != null && !(buyerPhone.trim()).equals(""))
			map.put("phone", buyerPhone);
		if (mailNoOrderType != null && !(mailNoOrderType.trim()).equals("")) {
			map.put("orderType", mailNoOrderType);
		}
		if (orderBy == 1) {
			map.put("sortType", "asc");
		} else
			map.put("sortType", "desc");
		pagination.setParams(map);
		return pagination;
	}

	@Override
    public Map<String, Integer> countOrderMonitorListNew(boolean isAll, Integer curUserId, String startTime, String endTime, String mailNO,
            String buyerName, String buyerPhone, List bindedId,
            Integer numProv, Integer numCity, Integer numDistrict,
            Pagination pagination, String mailNoOrderType) {
      
        Map<String, Integer> resultMap=new HashMap<String, Integer>();
        for (int i = 1; i < 6; i++) {
            resultMap.put(String.valueOf(i), 0);
        }
        if (isAll) {
            /**
             * 平台用户在查询所有属于自己的业务账号的时候根据主键curUserId获取所有仓库
             */
            List<String> custormerIdList = new ArrayList<String>();
            User curPlatformUser = userService.getUserById(curUserId);// 获取当前平台用户
            List<User> userList = userService.pingTaiSelect(curPlatformUser, 1);// 获取所有仓库
            if (userList != null && userList.size() > 0) {
                for (User user : userList) {
                    if (user.getTaobaoEncodeKey() != null)
                        custormerIdList.add(user.getTaobaoEncodeKey());
                }
            }
            pagination = buildGroupSearchPage(startTime, endTime, numProv,
                    numCity, numDistrict, mailNO, buyerName, buyerPhone, 0,
                    pagination, custormerIdList, mailNoOrderType);
        } else {
            pagination = buildGroupSearchPage(startTime, endTime, numProv,
                    numCity, numDistrict, mailNO, buyerName, buyerPhone, 0,
                    pagination, bindedId, mailNoOrderType);
        }
        try {
            if (pagination != null) {
                Map<Short, Integer> map = eccoreSearchService
                        .searchEccoreDataGroupStatus(ConfigUtilSingle
                                .getInstance().getSolrEccoreUrl(), pagination);
                    if (map.get(StatusEnum.SIGNED.getValue()) != null)
                    	resultMap.put("1", map.get(StatusEnum.SIGNED.getValue()));
                    if (map.get(StatusEnum.SENT_SCAN.getValue()) != null)
                    	resultMap.put("2", map.get(StatusEnum.SENT_SCAN.getValue()));
                    int result = 0;
                    if (map.get(StatusEnum.ACCEPT.getValue()) != null)
                        result += map.get(StatusEnum.ACCEPT.getValue());
                    if (map.get(StatusEnum.UNACCEPT.getValue()) != null)
                        result += map.get(StatusEnum.UNACCEPT.getValue());
                    if (map.get(StatusEnum.GOT.getValue()) != null)
                        result += map.get(StatusEnum.GOT.getValue());
                    if (map.get(StatusEnum.NOT_SEND.getValue()) != null)
                        result += map.get(StatusEnum.NOT_SEND.getValue());
                    if (map.get(StatusEnum.FAILED.getValue()) != null)
                        result += map.get(StatusEnum.FAILED.getValue());
                    if (map.get(StatusEnum.UNKNOW.getValue()) != null)
                        result += map.get(StatusEnum.UNKNOW.getValue());
                    
                    resultMap.put("3", result);
                    
                    if (map.get(StatusEnum.CREATE.getValue()) != null)
                        resultMap.put("5", map.get(StatusEnum.CREATE.getValue()));
                  
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

	/**
	 * @param userClassify user用户分类：1卖家、2网点、3平台
	 * @param userId 网点或者平台用户id
	 * @param status
	 * @param startTime
	 * @param endTime
	 * @param mailNO
	 * @param buyerName
	 * @param buyerPhone
	 * @param orderBy
	 * @param pagination
	 * @param bindedId
	 * @param numProv
	 * @param numCity
	 * @param numDistrict
	 * @param mailNoOrderType
	 * @return
	 */
	private List<T> searchSolr(int userClassify, Integer userId, Integer status, String startTime, String endTime, String mailNO,
			String buyerName, String buyerPhone, Integer orderBy,
			Pagination pagination, List<String> bindedId, Integer numProv,
			Integer numCity, Integer numDistrict, String mailNoOrderType) {
		List<T> monitorList = new ArrayList<T>();
		pagination = buildSearchPage(status, startTime, endTime, numProv,
				numCity, numDistrict, mailNO, buyerName, buyerPhone, orderBy,
				pagination, bindedId, mailNoOrderType);
		try{
			if (pagination != null) {
				eccoreSearchService.searchEccoreData(ConfigUtilSingle
						.getInstance().getSolrEccoreUrl(), pagination);
				List<EccoreSearchResultDTO> eccoreSearchList = pagination.getRecords();
				HashMap<String,MonitorBean> monitorDTOMap =new HashMap<String,MonitorBean>();
				if (eccoreSearchList != null && eccoreSearchList.size() > 0) {
					for (EccoreSearchResultDTO eccore : eccoreSearchList) {
						MonitorBean monitor = new MonitorBean();
						if (eccore.getId() != null)
							monitor.setId(eccore.getId());
						if (eccore.getMailNo() != null) {
							monitor.setMailNO(eccore.getMailNo());
							/**
							 * 判断是否被关注:只需网点和卖家判断。平台用户没有关注功能
							 */
							if(userClassify==1) {//卖家
								int listsize = attenService.searchByMailNoAndCustomerIdsAndTime(eccore.getMailNo(), bindedId, startTime, endTime);
								if (listsize > 0)
									monitor.setIsAttention(1);
							} else if(userClassify==2 && userId!=null) {//网点
								List<String> customerIds = new ArrayList<String>();
								customerIds.add(userId.toString());
								 int listsize = attenService.searchByMailNoAndCustomerIdsAndTime(eccore.getMailNo(), customerIds, startTime, endTime);
								if (listsize > 0)
									monitor.setIsAttention(1);
							}
						}
						if (eccore.getStatus() != null)
							monitor.setStatus(eccore.getStatus());
						if (eccore.getLineType() != null)
							monitor.setLineType(eccore.getLineType().toString());
						if (eccore.getName() != null)
							monitor.setUserName(eccore.getName());
						if (eccore.getPhone() != null
								&& eccore.getPhone() != "")
							monitor.setPhone(eccore.getPhone());
						if (eccore.getAddress() != null)
							monitor.setDestination(eccore.getProv()
									+ eccore.getCity() + eccore.getDistrict()
									+ eccore.getAddress());
						if (eccore.getCreateTime() != null) {
							monitor.setSenderTime(eccore.getCreateTime());
						}
						if (eccore.getAcceptTime() != null) {
							monitor.setAcceptTime(eccore.getAcceptTime());
						}
						/**
						 * 设置店铺名称
						 */
						if (eccore.getCustomerId() != null) {
							User u = userService.getUserByCustomerId(eccore.getCustomerId());
							monitor.setShopName(u.getShopName());
						}

						/**
						 * 在走件列表和派送中需要显示当条运单的最近一条物流信息
						 */
//						if (status.equals(2) || status.equals(3) || status.equals(5)) {
//							TaoBaoOrderAction op = new TaoBaoOrderAction();
//							String[] logisticsId = { eccore.getMailNo() };
//							try {
//								List<QueryOrder> logisticList = op
//										.ordersSearch(logisticsId);// 运单物流信息
//								if (logisticList != null
//										&& logisticList.size() > 0) {
//									QueryOrder qo = logisticList.get(0);
//									if (qo.getSteps() != null
//											&& qo.getSteps().size() > 0) {
//										StepInfo lastStep = qo.getSteps().get(
//												qo.getSteps().size() - 1);
//										monitor.setStepInfo(lastStep);
//									}
//								}
//							} catch (Exception e) {
//							}
//						}
						monitorList.add((T) monitor);
						monitorDTOMap.put(monitor.getMailNO(), monitor);
					}
//					return monitorList;
					return ordersSearchList(monitorDTOMap,(List<MonitorBean>) monitorList);
				}
			}
		} catch(Exception e) {
			logger.error("网点查询运单监控出错："+e);
		}
		return new ArrayList();
	}
	/**
	 * 查金刚获取当前位置
	 */
	public List<T> ordersSearchList(HashMap<String,MonitorBean> MonitorDTOMap,
								List<MonitorBean> MonitorDTOList){
		long startJgTime=System.currentTimeMillis();
		TaoBaoOrderAction op = new TaoBaoOrderAction();
		List<MonitorBean> monitorDTO=new ArrayList<MonitorBean>();
		StepInfo lastStep = null;
		List<String> mailNoList=new ArrayList<String>();
		if(MonitorDTOList !=null && MonitorDTOList.size()>0){
			String[] logisticsId=new String[MonitorDTOList.size()];
			for(int passIssue=0;passIssue<MonitorDTOList.size();passIssue++){
				logisticsId[passIssue] = new String(MonitorDTOList.get(passIssue).getMailNO());
			}
			try {
				List<PassMessageQueryOrder> logisticList = op.ManageOrdersSearch(logisticsId);
				if (logisticList != null && logisticList.size() > 0) {
					for(PassMessageQueryOrder qo:logisticList){
						//获取已经签收的订单运单号
						if(qo.getOrderStatus() != null && qo.getOrderStatus().equals("SIGNED")){
							mailNoList.add(qo.getMailNo());
						}
						if (qo.getSteps() != null && qo.getSteps().size() > 0) {
							lastStep = qo.getSteps().get(qo.getSteps().size() - 1);
							MonitorDTOMap.get(qo.getMailNo()).setStepInfo(lastStep);//当前位置
							logger.info(lastStep + "########################" + lastStep.getAcceptAddress());
						}
					}
				}
			} catch (Exception e1) {
				logger.error("##引擎中查询出超时mailNo查询金刚获取当前位置异常.异常信息：",e1);
			}
			
			//过滤已经签收的单子
			for(MonitorBean dto:MonitorDTOList){
				if(!mailNoList.contains(dto.getMailNO())){
					monitorDTO.add(MonitorDTOMap.get(dto.getMailNO()));
				}
			}
		
//		//将map转换成list后返回
//		for(MonitorBean dto:MonitorDTOList){
//			monitorDTO.add(MonitorDTOMap.get(dto.getMailNO()));
//		}
		}
		long endJgTime=System.currentTimeMillis();
		long jgNeedTime=endJgTime-startJgTime;
		logger.error("查询金刚接口 ordersSearchList 解析组装对象用时 毫秒数:"+jgNeedTime);
		return (List<T>) monitorDTO;
	}
	/**
	 * 运单监控中我的关注数据获取方法
	 * @param userClassify user用户分类：1卖家、2网点、3平台
	 * @param userId 网点或者平台用户id
	 * @param startTime
	 * @param endTime
	 * @param numProv
	 * @param numCity
	 * @param numDistrict
	 * @param mailNo
	 * @param buyerName
	 * @param buyerPhone
	 * @param orderBy
	 * @param pagination
	 * @param bindedId 当用户分类为卖家是需要这个字段
	 * @return
	 */
	private List<T> getAttentionList(int userClassify, Integer userId, String startTime, 
			String endTime, Integer numProv, Integer numCity, Integer numDistrict,
			String mailNo,String buyerName, String buyerPhone,
			Integer orderBy, Pagination pagination, List<String> bindedId) {
		List<T> monitorList = new ArrayList<T>();
		if(userClassify==1) {//卖家我的关注
			if(bindedId.isEmpty()) {
				return monitorList;
			}
			List<AttentionMail> amList = attenService.searchPaginationList(startTime, endTime, mailNo, buyerName, buyerPhone, orderBy, pagination, bindedId);
			if(amList != null && amList.size() > 0) {
				for (AttentionMail attentionMail : amList) {
					MonitorBean monitor = new MonitorBean();
					monitor.setId(attentionMail.getId());
					monitor.setMailNO(attentionMail.getMailNo());
					monitor.setLineType(attentionMail.getLineType());
					monitor.setUserName(attentionMail.getBuyerName());
					monitor.setPhone(attentionMail.getBuyerPhone());
					monitor.setDestination(attentionMail.getDestination());
					monitor.setSenderTime(attentionMail.getSendTime());
					/**
					 *  根据搜索引擎判断地址条件是否满足或者订单是否还在搜索引擎
					 */
			       Pagination<EccoreSearchResultDTO> searchPage = new Pagination<EccoreSearchResultDTO>(1, 15);
	               Map<String, String> searchParams = new HashMap<String, String>();
	               searchParams.put("startDate", startTime);
	               searchParams.put("endDate", endTime);
	               StringBuffer customerIds = new StringBuffer();
	               for(int i=0; i<bindedId.size(); i++) {
	            	   if(i < bindedId.size()-1) 
	            		   customerIds.append(bindedId.get(i)).append(",");
	            	   else
	            		   customerIds.append(bindedId.get(i));
	               }
	               if(customerIds!=null && StringUtils.isNotEmpty(customerIds.toString())) {
	            	   searchParams.put("customerId", customerIds.toString());
	               }
	               if(numProv != null) {
	            	   searchParams.put("numProv", numProv.toString());
	               }
	               if(numCity != null) {
	            	   searchParams.put("numCity", numCity.toString());
	               }
	               if(numDistrict != null) {
	            	   searchParams.put("numDistrict", numDistrict.toString());
	               }
	               searchPage.setParams(searchParams);
	               try{
	            	   eccoreSearchService.searchOrderByTimeMailNoCustomerId(ConfigUtilSingle.getInstance().getSolrEccoreUrl(), searchPage);
	               } catch (Exception e1) {
	            	   logger.error("关注订单~~~~~~搜索引擎查询订单时出错", e1);
	               }
	               List<EccoreSearchResultDTO> eccoreSearchResultDTOs = searchPage.getRecords();
	               if(eccoreSearchResultDTOs != null && eccoreSearchResultDTOs.size() > 0) {
            		   monitor.setStatus(eccoreSearchResultDTOs.get(0).getStatus());
            		   /**
    	                * 显示最近一条物流信息
    					*/
    	               TaoBaoOrderAction op = new TaoBaoOrderAction();
    	               String[] logisticsId = {attentionMail.getMailNo()};
    	               try {
    	            	   List<QueryOrder> logisticList = op.ordersSearch(logisticsId);// 运单物流信息
    	            	   if (logisticList != null && logisticList.size() > 0) {
    	            		   QueryOrder qo = logisticList.get(0);
    	            		   if (qo.getSteps() != null && qo.getSteps().size() > 0) {
    	            			   StepInfo lastStep = qo.getSteps().get(qo.getSteps().size() - 1);
    	            			   monitor.setStepInfo(lastStep);
    	            		   }
    	            	   }
    	               }catch (Exception e) {
    	               }
            		   monitorList.add((T) monitor); 
	               }
				}
			}
		} else if(userClassify==2) {//网点查询我的关注
			//网点查询我的关注是按网点用户id来查询；在查询搜索引擎时需要查询出该网点所有客户的customerId集合作为其中的一个条件
			if(userId!=null) {
				List<String> userIdList = new ArrayList<String>();
				userIdList.add(userId.toString());
				List<AttentionMail> amList = attenService.searchPaginationList(startTime, endTime, mailNo, buyerName, buyerPhone, orderBy, pagination, userIdList);
				if(amList != null && amList.size() > 0) {
					for(AttentionMail attentionMail : amList) {
						MonitorBean monitor = new MonitorBean();
						monitor.setId(attentionMail.getId());
						monitor.setMailNO(attentionMail.getMailNo());
						monitor.setLineType(attentionMail.getLineType());
						monitor.setUserName(attentionMail.getBuyerName());
						monitor.setPhone(attentionMail.getBuyerPhone());
						monitor.setDestination(attentionMail.getDestination());
						monitor.setSenderTime(attentionMail.getSendTime());
						/**
						 *  根据搜索引擎判断地址条件是否满足或者订单是否还在搜索引擎
						 */
				        Pagination<EccoreSearchResultDTO> searchPage = new Pagination<EccoreSearchResultDTO>(1, 15);
		                Map<String, String> searchParams = new HashMap<String, String>();
		                searchParams.put("startDate", startTime);
		                searchParams.put("endDate", endTime);
		                StringBuffer customerIds = new StringBuffer();
		                List<String> customerIdList = new ArrayList<String>();
						User user = userService.getUserById(userId);
						List<User> userList = userService.searchUsersBySiteAndUserType(user.getSite(), "1");
		                if(userList!=null) {
		                	for(int i=0; i<userList.size(); i++) {
		                		if(StringUtils.isNotEmpty(userList.get(i).getTaobaoEncodeKey())) {
		                			if(i < userList.size()-1) 
		                				customerIds.append(userList.get(i).getTaobaoEncodeKey()).append(",");
		                			else
		                				customerIds.append(userList.get(i).getTaobaoEncodeKey());
		                		}
			                }
			                if(customerIds!=null && StringUtils.isNotEmpty(customerIds.toString())) {
			            	    searchParams.put("customerId", customerIds.toString());
			                }
		                }
		                if(numProv != null) {
		            	    searchParams.put("numProv", numProv.toString());
		                }
		                if(numCity != null) {
		            	    searchParams.put("numCity", numCity.toString());
		                }
		                if(numDistrict != null) {
		            	    searchParams.put("numDistrict", numDistrict.toString());
		                }
		                searchPage.setParams(searchParams);
		                try{
		            	    eccoreSearchService.searchOrderByTimeMailNoCustomerId(ConfigUtilSingle.getInstance().getSolrEccoreUrl(), searchPage);
		                } catch (Exception e1) {
		            	    logger.error("关注订单~~~~~~搜索引擎查询订单时出错", e1);
		                }
		                List<EccoreSearchResultDTO> eccoreSearchResultDTOs = searchPage.getRecords();
		                if(eccoreSearchResultDTOs != null && eccoreSearchResultDTOs.size() > 0) {
	            		    monitor.setStatus(eccoreSearchResultDTOs.get(0).getStatus());
	            		    /**
	    	                 * 显示最近一条物流信息
	    					 */
	    	                TaoBaoOrderAction op = new TaoBaoOrderAction();
	    	                String[] logisticsId = {attentionMail.getMailNo()};
	    	                try {
	    	            	    List<QueryOrder> logisticList = op.ordersSearch(logisticsId);// 运单物流信息
	    	            	    if (logisticList != null && logisticList.size() > 0) {
	    	            		    QueryOrder qo = logisticList.get(0);
	    	            		    if (qo.getSteps() != null && qo.getSteps().size() > 0) {
	    	            			    StepInfo lastStep = qo.getSteps().get(qo.getSteps().size() - 1);
	    	            			    monitor.setStepInfo(lastStep);
	    	            		    }
	    	            	    }
	    	                }catch (Exception e) {
	    	                }
	            		    monitorList.add((T) monitor); 
		                }
					}
				}
			}
		} else if(userClassify==3){//平台用户暂不处理
		}
		return monitorList;
	}
	
	@Override
	public List<T> getSiteMonitorList(Integer tabFlag, Integer status,
			Integer userId, String startTime, String endTime, String mailNo,
			String buyerName, String buyerPhone, Integer orderBy,
			Pagination pagination, List<String> bindedId, Integer numProv,
			Integer numCity, Integer numDistrict, String mailNoOrderType) {
		if(tabFlag == null) {
			return new ArrayList();
		} else if(tabFlag.intValue() == 1) { //网点查询订单
			return searchSolr(2,userId, status, startTime, endTime, mailNo, buyerName, buyerPhone, orderBy, pagination, bindedId, numProv, numCity, numDistrict, mailNoOrderType);
		} else if(tabFlag.intValue() == 2) {//网点关注运单(关注订单只显示最多50条)
			return getAttentionList(2, userId, startTime, endTime, numProv, numCity, numDistrict, mailNo, buyerName, buyerPhone, 2, pagination, bindedId);
		} else{
			return new ArrayList();
		}
	}

	@Override
	public int countSiteMonitorAttentionList(Integer userId, String startTime,
			String endTime, String mailNO, String buyerName, String buyerPhone,
			Integer numProv, Integer numCity, Integer numDistrict) {
		List<T> monitorList = new ArrayList<T>();
		//网点查询我的关注是按网点用户id来查询；在查询搜索引擎时需要查询出该网点所有客户的customerId集合作为其中的一个条件
		List<String> userIdList = new ArrayList<String>();
		userIdList.add(userId.toString());
		Pagination pagination = new Pagination(1,50);
		List<AttentionMail> amList = attenService.searchPaginationList(startTime, endTime, mailNO, buyerName, buyerPhone, 2, pagination, userIdList);
		if(amList != null && amList.size() > 0) {
			for(AttentionMail attentionMail : amList) {
				MonitorBean monitor = new MonitorBean();
				/**
				 *  根据搜索引擎判断地址条件是否满足或者订单是否还在搜索引擎
				 */
		        Pagination<EccoreSearchResultDTO> searchPage = new Pagination<EccoreSearchResultDTO>(1, 15);
                Map<String, String> searchParams = new HashMap<String, String>();
                searchParams.put("startDate", startTime);
                searchParams.put("endDate", endTime);
                StringBuffer customerIds = new StringBuffer();
                List<String> customerIdList = new ArrayList<String>();
				User user = userService.getUserById(userId);
				List<User> userList = userService.searchUsersBySiteAndUserType(user.getSite(), "1");
                if(userList!=null) {
                	for(int i=0; i<userList.size(); i++) {
                		if(StringUtils.isNotEmpty(userList.get(i).getTaobaoEncodeKey())) {
                			if(i < userList.size()-1) 
                				customerIds.append(userList.get(i).getTaobaoEncodeKey()).append(",");
                			else
                				customerIds.append(userList.get(i).getTaobaoEncodeKey());
                		}
	                }
	                if(customerIds!=null && StringUtils.isNotEmpty(customerIds.toString())) {
	            	    searchParams.put("customerId", customerIds.toString());
	                }
                }
                if(numProv != null) {
            	    searchParams.put("numProv", numProv.toString());
                }
                if(numCity != null) {
            	    searchParams.put("numCity", numCity.toString());
                }
                if(numDistrict != null) {
            	    searchParams.put("numDistrict", numDistrict.toString());
                }
                searchPage.setParams(searchParams);
                try{
            	    eccoreSearchService.searchOrderByTimeMailNoCustomerId(ConfigUtilSingle.getInstance().getSolrEccoreUrl(), searchPage);
                } catch (Exception e1) {
            	    logger.error("关注订单~~~~~~搜索引擎查询订单时出错", e1);
                }
                List<EccoreSearchResultDTO> eccoreSearchResultDTOs = searchPage.getRecords();
                if(eccoreSearchResultDTOs != null && eccoreSearchResultDTOs.size() > 0) {
        			monitor.setStatus(eccoreSearchResultDTOs.get(0).getStatus());
        		    monitorList.add((T) monitor); 
                }
			}
		}
		return monitorList.size();
	}

	@Override
	public List<T> getSellerMonitorList(Integer tabFlag, Integer status,
			String customerId, String startTime, String endTime, String mailNO,
			String buyerName, String buyerPhone, Integer orderBy,
			Pagination pagination, List<String> bindedId, Integer numProv,
			Integer numCity, Integer numDistrict, String mailNoOrderType) {
		if(tabFlag == null) {
			return new ArrayList();
		} else if(tabFlag == 1) { //卖家查询订单
			return searchSolr(1,null, status, startTime, endTime, mailNO, buyerName, buyerPhone, orderBy, pagination, bindedId, numProv, numCity, numDistrict, mailNoOrderType);
		} else if(tabFlag == 2) {//卖家关注运单
			return getAttentionList(1, null, startTime, endTime, numProv, numCity, numDistrict, mailNoOrderType, buyerName, buyerPhone, 2, pagination, bindedId);
		} else{
			return new ArrayList();
		}
	}

	@Override
	public int countSellerMonitorAttentionList(String startTime,
			String endTime, String mailNO, String buyerName, String buyerPhone,
			Integer numProv, Integer numCity, Integer numDistrict, List<String> bindedId) {
		List<T> monitorList = new ArrayList<T>();
		if(bindedId.isEmpty()) {
			return 0;
		}
		Pagination pagination = new Pagination(1,50);
		List<AttentionMail> amList = attenService.searchPaginationList(startTime, endTime, mailNO, buyerName, buyerPhone, 2, pagination, bindedId);
		if(amList != null && amList.size() > 0) {
			for (AttentionMail attentionMail : amList) {
				MonitorBean monitor = new MonitorBean();
				/**
				 *  根据搜索引擎判断地址条件是否满足或者订单是否还在搜索引擎
				 */
		       Pagination<EccoreSearchResultDTO> searchPage = new Pagination<EccoreSearchResultDTO>(1, 15);
               Map<String, String> searchParams = new HashMap<String, String>();
               searchParams.put("startDate", startTime);
               searchParams.put("endDate", endTime);
               StringBuffer customerIds = new StringBuffer();
               for(int i=0; i<bindedId.size(); i++) {
            	   if(i < bindedId.size()-1) 
            		   customerIds.append(bindedId.get(i)).append(",");
            	   else
            		   customerIds.append(bindedId.get(i));
               }
               if(customerIds!=null && StringUtils.isNotEmpty(customerIds.toString())) {
            	   searchParams.put("customerId", customerIds.toString());
               }
               if(numProv != null) {
            	   searchParams.put("numProv", numProv.toString());
               }
               if(numCity != null) {
            	   searchParams.put("numCity", numCity.toString());
               }
               if(numDistrict != null) {
            	   searchParams.put("numDistrict", numDistrict.toString());
               }
               searchPage.setParams(searchParams);
               try{
            	   eccoreSearchService.searchOrderByTimeMailNoCustomerId(ConfigUtilSingle.getInstance().getSolrEccoreUrl(), searchPage);
               } catch (Exception e1) {
            	   logger.error("关注订单~~~~~~搜索引擎查询订单时出错", e1);
               }
               List<EccoreSearchResultDTO> eccoreSearchResultDTOs = searchPage.getRecords();
               if(eccoreSearchResultDTOs != null && eccoreSearchResultDTOs.size() > 0) {
        		   monitorList.add((T) monitor); 
               }
			}
		}
		return monitorList.size();
	}

	@Override
	public List<T> getPlatMonitorList(boolean isAll, Integer tabFlag,
			Integer status, Integer userId, String startTime, String endTime,
			String mailNO, String buyerName, String buyerPhone,
			Integer orderBy, Pagination pagination, Integer numProv,
			Integer numCity, Integer numDistrict, String mailNoOrderType) {
		List<T> monitorList = new ArrayList<T>();
		List<String> bindedId = new ArrayList<String>();
		if(isAll) {//平台用户查看所有业务账号
			User curPlatformUser = userService.getUserById(userId);// 获取当前平台用户
			List<User> userList = userService.pingTaiSelect(curPlatformUser, 1);// 获取业务账号
			if (userList != null && userList.size() > 0) {
				for (User user : userList) {
					if (user.getTaobaoEncodeKey() != null)
						bindedId.add(user.getTaobaoEncodeKey());
				}
			}
		} else {//单个查看某个业务账号
			User curFCUser = userService.getUserById(userId);// 获取当前所选业务账号
			if(StringUtils.isNotEmpty(curFCUser.getTaobaoEncodeKey()))
				bindedId.add(curFCUser.getTaobaoEncodeKey());
		}
		if(bindedId.isEmpty())
			return monitorList;
		if(tabFlag == null) {
			return new ArrayList<T>();
		} else if(tabFlag == 1) {
			return searchSolr(3, userId, status, startTime, endTime, mailNO, buyerName, buyerPhone, orderBy, pagination, bindedId, numProv, numCity, numDistrict, mailNoOrderType);
		}
		return monitorList;
	}

	
}
