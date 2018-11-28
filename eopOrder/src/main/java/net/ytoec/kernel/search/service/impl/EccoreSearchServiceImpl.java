/**
 * 
 */
package net.ytoec.kernel.search.service.impl;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.common.StatusEnum;
import net.ytoec.kernel.dao.BuildSearchDao;
import net.ytoec.kernel.dao.BuildSearchStatusDao;
import net.ytoec.kernel.dao.BuildSearchStatusWeightIndexDao;
import net.ytoec.kernel.dao.ConfigCodeDao;
import net.ytoec.kernel.dao.OrderDao;
import net.ytoec.kernel.dataobject.BuildSearch;
import net.ytoec.kernel.dataobject.BuildSearchStatus;
import net.ytoec.kernel.dataobject.BuildSearchStatusWeightIndex;
import net.ytoec.kernel.dataobject.ConfigCode;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.search.dataobject.EccoreItem;
import net.ytoec.kernel.search.dto.EccoreSearchResultDTO;
import net.ytoec.kernel.search.dto.MailObjectDTO;
import net.ytoec.kernel.search.service.EccoreSearchService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.util.ConfigUtilSingle;
import net.ytoec.kernel.util.DateUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

/**
 * @author wangyong
 */
@Service
@Transactional
@SuppressWarnings("all")
public class EccoreSearchServiceImpl implements EccoreSearchService {

	protected final Logger logger = LoggerFactory
			.getLogger(EccoreSearchServiceImpl.class);

	@Inject
	private OrderDao<MailObjectDTO> orderDao;

	@Inject
	private ConfigCodeDao<ConfigCode> configCodeDao;

	@Inject
	private BuildSearchDao<BuildSearch> buildSearchDao;
	@Inject
	private BuildSearchStatusDao<BuildSearchStatus> buildSearchStatusDao;

	@Inject
	private BuildSearchStatusWeightIndexDao<BuildSearchStatusWeightIndex> buildSearchStatusWeightIndexDao;

	@Inject
	private OrderService orderService;// 4 Solr

	/**
	 * 超时件的传入参数
	 */
	private static final String[] COMMON_FIELDS_OVER_DATA = new String[] {
			"numStatus", "phone", "name", "mailNo" };

	SimpleDateFormat holdTimeFormat = new SimpleDateFormat("yyyy-MM-dd");

	public CommonsHttpSolrServer getSolrServer(String solrUrl)
			throws MalformedURLException {
		// 远程服务
		return new CommonsHttpSolrServer(solrUrl);
	}

	/**
	 * 删除不存在用户7天前的数据
	 * 
	 * @param solrServer
	 */
	@Override
	public void deleteBefore7Days(String solrUrl) {
		try {
			SolrServer solrServer = getSolrServer(solrUrl);
			int totalDays = 90;
			int before = 7;
			int maxDelNum = 1000;
			/**
			 * 循环最近3个月内且7天的solr里的数据
			 */
			for (int i = before; i <= totalDays; i++) {
				SolrQuery query = new SolrQuery();
				Date date = DateUtil.getDateBefore(new Date(), i);
				String dateString = DateUtil.format(date, "yyyy-MM-dd");
				Pagination<EccoreSearchResultDTO> searchPage = new Pagination<EccoreSearchResultDTO>(
						1, 1);
				Map<String, String> paramsMap = new HashMap<String, String>();
				paramsMap.put("startDate", dateString);
				paramsMap.put("endDate", dateString);
				searchPage.setParams(paramsMap);
				setQuery(query, searchPage);
				QueryResponse rsp = solrServer.query(query);
				searchPage.setTotalRecords(new Long(rsp.getResults()
						.getNumFound()).intValue());
				int totalRecords = searchPage.getTotalRecords();
				if (totalRecords > 0) {// 每次最多删除maxDelNum条
					Pagination<EccoreSearchResultDTO> tempPagination = new Pagination<EccoreSearchResultDTO>(
							1, maxDelNum);
					tempPagination.setTotalRecords(totalRecords);
					int totalPages = tempPagination.getTotalPages();
					for (int a = 1; a <= totalPages; a++) {
						tempPagination = new Pagination<EccoreSearchResultDTO>(
								a, maxDelNum);
						query.setStart((tempPagination.getCurrentPage() - 1)
								* tempPagination.getPageNum());
						query.setRows(tempPagination.getPageNum());
						QueryResponse rspTemp = solrServer.query(query);
						List<EccoreItem> searchResult = rspTemp
								.getBeans(EccoreItem.class);
						logger.info("start delete data of " + dateString
								+ " in solr.");
						List<String> ids = new ArrayList<String>();// 被删除数据的id
						for (EccoreItem ei : searchResult) {
							// 判断该数据的customerId对应的用户是否存在
							User u = Resource.getUserByCustomerId(ei
									.getCustomerId());
							if (u == null || u.getId() == null) {
								ids.add(ei.getId().toString());
							}
						}
						if (ids.size() > 0) {
							solrServer.deleteById(ids);
							solrServer.commit();
							logger.info("delete success in solr");
						}
					}
				}
			}

		} catch (Exception e) {
			logger.error("删除不存在用户7天前的数据", e);
		}
	}

	@Override
	public void buildEccoreData(String solrServer, int startId, int len) {

		try {
			CommonsHttpSolrServer server = getSolrServer(solrServer);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", startId);
			map.put("limit", len);
			List<MailObjectDTO> mailObjectDTOs = orderDao.bulidEccoreData(map);
			List<EccoreItem> result = new ArrayList<EccoreItem>();

			MailObjectDTO mailObjectDTO = new MailObjectDTO();
			String status = "0";

			String dateStr = "2011-11-19";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
			Date originalDate = simpleDateFormat.parse(dateStr);

			for (int j = 0; j < mailObjectDTOs.size(); j++) {
				mailObjectDTO = mailObjectDTOs.get(j);
				EccoreItem bean = new EccoreItem();
				bean.setId(mailObjectDTO.getId());
				status = mailObjectDTO.getStatus();
				if (StringUtils.equals(status, StatusEnum.CREATE.getValue()
						.toString())) {
					status = StatusEnum.CREATE.getName();
				}
				bean.setNumStatus(StatusEnum.valueOf(status).getValue());

				if (StringUtils.isEmpty(mailObjectDTO.getMailNo())) {
					bean.setMailNo("YTO" + mailObjectDTO.getId());
					bean.setIsDispaly(new Integer(1).shortValue());
				} else {
					bean.setMailNo(mailObjectDTO.getMailNo());
					bean.setIsDispaly(new Integer(0).shortValue());
				}

				bean.setNumCreateTime((int) getNumDay(mailObjectDTO
						.getPartitiondate()));
				bean.setCreateTime(mailObjectDTO.getCreateTime());
				bean.setAcceptTime(mailObjectDTO.getAcceptTime());
				bean.setCustomerId(mailObjectDTO.getCustomerId());
				bean.setName(mailObjectDTO.getName());

				bean.setPhone(new String[] { mailObjectDTO.getMobile(),
						mailObjectDTO.getPhone() });
				bean.setDisplayPhone(mailObjectDTO.getMobile());
				if (StringUtils.isEmpty(bean.getDisplayPhone())) {
					bean.setDisplayPhone(mailObjectDTO.getPhone());
				}
				bean.setNumProv(Integer.valueOf(mailObjectDTO.getNumProv()));

				bean.setNumCity(Resource.getCodeByName(mailObjectDTO.getCity()));

				bean.setNumDistrict(Resource.getCodeByName(mailObjectDTO
						.getDistrict()));
				bean.setAddress(mailObjectDTO.getAddress());
				bean.setWeight(mailObjectDTO.getWeight());
				bean.setOrderType(new Integer(mailObjectDTO.getOrderType())
						.shortValue());
				bean.setLineType(mailObjectDTO.getLineType().shortValue());
				bean.setTrimFreight(mailObjectDTO.getTrimFreight());
				if (mailObjectDTO.getFreightType() != null
						&& mailObjectDTO.getFreightType() == 0) {
					bean.setFreightType(new Integer(0).shortValue());
				} else {
					bean.setFreightType(new Integer(1).shortValue());
				}

				bean.setCityF(mailObjectDTO.getCityF());
				bean.setNumProvF(Resource.getCodeByName(mailObjectDTO
						.getProvF()));
				if (!StringUtils.isEmpty(mailObjectDTO.getHoldTime())) {
					bean.setHoldTime((int) getNumDay(holdTimeFormat
							.parse(mailObjectDTO.getHoldTime())));
				}
				bean.setMailNoAndCustomerId(bean.getMailNo()
						+ mailObjectDTO.getCustomerId());

				bean.setTxLogisticId(mailObjectDTO.getTxLogisticId());
				result.add(bean);

			}
			if (result.size() > 0) {
				server.addBeans(result);
				server.commit();
				result.clear();
			}
		} catch (Exception e) {
			logger.error("build error", e);
			logger.error("lastId=" + startId);
		}

	}

	@Override
	public void buildEccoreDataOnce(String solrServer, int startId, int len) {

		try {
			CommonsHttpSolrServer server = getSolrServer(solrServer);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", startId);
			map.put("limit", len);
			List<MailObjectDTO> mailObjectDTOs = orderDao.bulidEccoreData(map);
			List<EccoreItem> result = new ArrayList<EccoreItem>();

			MailObjectDTO mailObjectDTO = new MailObjectDTO();
			String status = "0";

			String dateStr = "2011-11-19";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
			Date originalDate = simpleDateFormat.parse(dateStr);

			for (int j = 0; j < mailObjectDTOs.size(); j++) {
				mailObjectDTO = mailObjectDTOs.get(j);
				EccoreItem bean = new EccoreItem();
				bean.setId(mailObjectDTO.getId());
				status = mailObjectDTO.getStatus();
				if (StringUtils.equals(status, StatusEnum.CREATE.getValue()
						.toString())) {
					status = StatusEnum.CREATE.getName();
				}
				bean.setNumStatus(StatusEnum.valueOf(status).getValue());

				if (StringUtils.isEmpty(mailObjectDTO.getMailNo())) {
					bean.setMailNo("YTO" + mailObjectDTO.getId());
					bean.setIsDispaly(new Integer(1).shortValue());
				} else {
					bean.setMailNo(mailObjectDTO.getMailNo());
					bean.setIsDispaly(new Integer(0).shortValue());
				}

				bean.setNumCreateTime((int) getNumDay(mailObjectDTO
						.getPartitiondate()));
				bean.setCreateTime(mailObjectDTO.getCreateTime());
				bean.setAcceptTime(mailObjectDTO.getAcceptTime());
				bean.setCustomerId(mailObjectDTO.getCustomerId());
				bean.setName(mailObjectDTO.getName());

				bean.setPhone(new String[] { mailObjectDTO.getMobile(),
						mailObjectDTO.getPhone() });
				bean.setDisplayPhone(mailObjectDTO.getMobile());
				if (StringUtils.isEmpty(bean.getDisplayPhone())) {
					bean.setDisplayPhone(mailObjectDTO.getPhone());
				}
				bean.setNumProv(Integer.valueOf(mailObjectDTO.getNumProv()));

				bean.setNumCity(Resource.getCodeByName(mailObjectDTO.getCity()));

				bean.setNumDistrict(Resource.getCodeByName(mailObjectDTO
						.getDistrict()));
				bean.setAddress(mailObjectDTO.getAddress());
				bean.setWeight(mailObjectDTO.getWeight());
				bean.setOrderType(new Integer(mailObjectDTO.getOrderType())
						.shortValue());
				bean.setLineType(mailObjectDTO.getLineType().shortValue());
				bean.setTrimFreight(mailObjectDTO.getTrimFreight());
				if (mailObjectDTO.getFreightType() == 0) {
					bean.setFreightType(new Integer(0).shortValue());
				} else {
					bean.setFreightType(new Integer(1).shortValue());
				}

				bean.setCityF(mailObjectDTO.getCityF());
				bean.setNumProvF(Resource.getCodeByName(mailObjectDTO
						.getProvF()));
				if (!StringUtils.isEmpty(mailObjectDTO.getHoldTime())) {
					bean.setHoldTime((int) getNumDay(holdTimeFormat
							.parse(mailObjectDTO.getHoldTime())));
				}

				bean.setMailNoAndCustomerId(bean.getMailNo()
						+ mailObjectDTO.getCustomerId());

				result.add(bean);

			}
			if (result.size() > 0) {
				server.addBeans(result);
				server.commit();
				result.clear();
			}
		} catch (Exception e) {
			logger.error("build error", e);
			logger.error("oncelastId=" + startId);
		}

	}

	// public static void main(String[] args) throws Exception {
	// int len = 100000;
	// for (int i = 1; i <= 18 * 30; i++) {
	// System.out.println(i);
	// long start = System.currentTimeMillis();
	// EccoreSearchServiceImpl eccoreSearchServiceImpl = new
	// EccoreSearchServiceImpl();
	// eccoreSearchServiceImpl.buildEccoreData("http://127.0.0.1:8083/solr/eccore/",
	// len * (i - 1), len);
	// System.out.println("第" + i + "次 build，bulid数据 " + len + "耗时：" +
	// (System.currentTimeMillis() - start));
	// }
	// EccoreSearchServiceImpl e = new EccoreSearchServiceImpl();
	// System.out.println(e.getNumDay("2012-10-16"));
	// }

	@Override
	public void searchEccoreData(String solrUrl,
			Pagination<EccoreSearchResultDTO> searchPage) throws Exception {
		Map<String, String> paramsMap = searchPage.getParams();
		if (!resetDate(searchPage.getParams())) {
			return;
		}

		String sortType = paramsMap.get("sortType");// desc or asc 默认desc

		SolrServer solrServer = getSolrServer(solrUrl);
		SolrQuery query = new SolrQuery();
		// 添加搜索条件
		setQuery(query, searchPage);
		// 添加过滤条件
		setFilterQueries(query, searchPage);

		// 排序
		if ("asc".equalsIgnoreCase(sortType)) {
			query.setSortField("id", ORDER.asc);
		} else {
			query.setSortField("id", ORDER.desc);
		}
		// 如果map里传了分页参数就分页
		// if(StringUtils.isNotEmpty(ObjectUtils.toString(searchPage.getPageNum())))
		// {
		// 分页
		query.setStart((searchPage.getCurrentPage() - 1)
				* searchPage.getPageNum());
		query.setRows(searchPage.getPageNum());
		// }
		// else {
		// query.setStart(0);
		// query.setRows(Integer.MAX_VALUE);
		// }
		// 搜索

		QueryResponse rsp = solrServer.query(query);
		logger.error("查询 Solr searchEccoreData 用时:" + rsp.getQTime());
		// 搜索结果处理
		searchPage.setTotalRecords(new Long(rsp.getResults().getNumFound())
				.intValue());
		List<EccoreItem> searchResult = rsp.getBeans(EccoreItem.class);
		List<EccoreSearchResultDTO> result = new ArrayList<EccoreSearchResultDTO>();
		// 转换搜索结果
		for (EccoreItem eccoreItem : searchResult) {
			EccoreSearchResultDTO eccoreSearchResultDTO = new EccoreSearchResultDTO();
			try {
				BeanUtils.copyProperties(eccoreItem, eccoreSearchResultDTO,
						new String[] { "numStatus", "phone", "displayPhone",
								"numCity", "numDistrict", "numCreateTime",
								"holdTime" });
			} catch (Exception e) {
				logger.error("" + e);
			}
			// 转换省市区
			eccoreSearchResultDTO.setStatus(StatusEnum.getStatusName(
					eccoreItem.getNumStatus()).getName());
			eccoreSearchResultDTO.setProv(Resource.getNameById(eccoreItem
					.getNumProv()));
			eccoreSearchResultDTO.setCity(Resource.getNameById(eccoreItem
					.getNumCity()));
			eccoreSearchResultDTO.setDistrict(Resource.getNameById(eccoreItem
					.getNumDistrict()));
			eccoreSearchResultDTO
					.setAddress(eccoreSearchResultDTO.getAddress());

			// 显示电话
			eccoreSearchResultDTO.setPhone(eccoreItem.getDisplayPhone());
			if (eccoreSearchResultDTO.getAcceptTime() != null
					&& !(eccoreSearchResultDTO.getAcceptTime().equals("")))
				eccoreSearchResultDTO.setAcceptTime(DateUtil
						.toLocalDate(eccoreSearchResultDTO.getAcceptTime()));
			if (eccoreSearchResultDTO.getCreateTime() != null
					&& !(eccoreSearchResultDTO.getCreateTime().equals("")))
				eccoreSearchResultDTO.setCreateTime(DateUtil
						.toLocalDate(eccoreSearchResultDTO.getCreateTime()));
			result.add(eccoreSearchResultDTO);
		}
		searchPage.setRecords(result);
	}

	@Override
	public void searchWayBillData(String solrUrl,
			Pagination<EccoreSearchResultDTO> searchPage) throws Exception {
		Map<String, String> paramsMap = searchPage.getParams();

		String sortType = paramsMap.get("sortType");// desc or asc 默认desc
		// String sortType ="desc";
		SolrServer solrServer = getSolrServer(solrUrl);
		SolrQuery query = new SolrQuery();
		// 添加搜索条件
		if (resetDate(searchPage.getParams())) { // 如果有时间条件，优先把时间做为搜索条件
			setQuery(query, searchPage);
		} else if (paramsMap.get("all") != null) { // 如果没有时间条件，则把运单号、买家姓名或手机号做为条件查询，因为all字段组合了这三个条件。
			query.setQuery("all:" + paramsMap.get("all").toString());
		} else { // 如果以两个条件都没有，则查询所有。
			query.setQuery("*:*");
		}
		// 添加过滤条件
		setFilterQueries(query, searchPage);

		// 排序
		if ("asc".equalsIgnoreCase(sortType)) {
			query.setSortField("id", ORDER.asc);
		} else {
			query.setSortField("id", ORDER.desc);
		}
		// 如果map里传了分页参数就分页
		// if(StringUtils.isNotEmpty(ObjectUtils.toString(searchPage.getPageNum())))
		// {
		// 分页
		query.setStart((searchPage.getCurrentPage() - 1)
				* searchPage.getPageNum());
		query.setRows(searchPage.getPageNum());
		// }
		// else {
		// query.setStart(0);
		// query.setRows(Integer.MAX_VALUE);
		// }
		// 搜索
		QueryResponse rsp = solrServer.query(query);
		// 搜索结果处理
		searchPage.setTotalRecords(new Long(rsp.getResults().getNumFound())
				.intValue());
		List<EccoreItem> searchResult = rsp.getBeans(EccoreItem.class);
		List<EccoreSearchResultDTO> result = new ArrayList<EccoreSearchResultDTO>();
		// 转换搜索结果
		for (EccoreItem eccoreItem : searchResult) {
			EccoreSearchResultDTO eccoreSearchResultDTO = new EccoreSearchResultDTO();
			BeanUtils.copyProperties(eccoreItem, eccoreSearchResultDTO,
					new String[] { "numStatus", "phone", "displayPhone",
							"numCity", "numDistrict", "numCreateTime",
							"holdTime" });

			String dateStr = "2011-11-19";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
			Date originalDate = simpleDateFormat.parse(dateStr);
			if (eccoreItem.getHoldTime() != null) {
				eccoreSearchResultDTO.setHoldTime(DateUtils.addDays(
						originalDate, eccoreItem.getHoldTime()));
			}

			// 转换省市区
			eccoreSearchResultDTO.setStatus(StatusEnum.getStatusName(
					eccoreItem.getNumStatus()).getName());
			eccoreSearchResultDTO.setProv(Resource.getNameById(eccoreItem
					.getNumProv()));
			eccoreSearchResultDTO.setCity(Resource.getNameById(eccoreItem
					.getNumCity()));
			eccoreSearchResultDTO.setDistrict(Resource.getNameById(eccoreItem
					.getNumDistrict()));
			eccoreSearchResultDTO
					.setAddress(eccoreSearchResultDTO.getAddress());

			// 显示电话
			eccoreSearchResultDTO.setPhone(eccoreItem.getDisplayPhone());

			/* 新增2012-6-27 begin */
			eccoreSearchResultDTO.setName(eccoreItem.getName());
			eccoreSearchResultDTO.setOrderType(eccoreItem.getOrderType());
			eccoreSearchResultDTO.setMailNo(eccoreItem.getMailNo());
			eccoreSearchResultDTO.setCityF(eccoreItem.getCityF());
			eccoreSearchResultDTO.setCreateTime(eccoreItem.getCreateTime());
			eccoreSearchResultDTO.setAcceptTime(eccoreItem.getAcceptTime());
			/* 新增2012-6-27 end */

			result.add(eccoreSearchResultDTO);
		}
		searchPage.setRecords(result);
	}

	// private void setQuery(SolrQuery query,
	// Pagination<EccoreSearchResultDTO> searchPage) throws ParseException {
	// Map<String, String> paramsMap = searchPage.getParams();
	// String startDate = paramsMap.get("startDate");// 2011-01-08
	// String endDate = paramsMap.get("endDate");// 2011-01-08
	// StringBuffer days = new StringBuffer();
	// long numStartDay = getNumDay(startDate);
	// long numEndDay = getNumDay(endDate);
	// days.append("numCreateTime:");
	// days.append("[").append(numStartDay);
	// days.append(" TO ").append(numEndDay);
	// days.append("]");
	// query.setQuery(days.toString());
	// }

	private void setQuery(SolrQuery query,
			Pagination<EccoreSearchResultDTO> searchPage) throws ParseException {
		Map<String, String> paramsMap = searchPage.getParams();
		String startDate = paramsMap.get("startDate");// 2011-01-08
		String endDate = paramsMap.get("endDate");// 2011-01-08
		StringBuffer days = new StringBuffer();
		long numStartDay = getNumDay(startDate);
		long numEndDay = getNumDay(endDate);
		for (long i = numStartDay; i <= numEndDay; i++) {
			if (i != numStartDay) {
				days.append(" OR ");
			}
			days.append("numCreateTime:").append(i);
		}
		query.setQuery(days.toString());
	}

	private static final String[] COMMON_FIELDS = new String[] { "mailNo",
			"numProv", "numCity", "numDistrict", "phone", "name", "numStatus",
			"orderType" };

	private void setFilterQueries(SolrQuery query,
			Pagination<EccoreSearchResultDTO> searchPage) {
		logger.error("Filter smylog:setFilterQueries==========start");
		Map<String, String> paramsMap = searchPage.getParams();
		String customerIDs = paramsMap.get("customerIDs");// 10213123,123123123
		// 增加filter
		List<String> fqList = new ArrayList();
		// 增加clientFilter
		if (StringUtils.isNotEmpty(customerIDs)) {
			StringBuffer customersSB = new StringBuffer();
			String[] customers = customerIDs.split("\\,");
			for (int i = 0; i < customers.length; i++) {
				if (i != 0) {
					customersSB.append(" OR ");
				}
				customersSB.append("customerId:").append(customers[i]);
			}
			fqList.add(customersSB.toString());
		}
		logger.error("Filter mylog:fqList.add(customersSB.toString())==>"
				+ fqList.toString());
		// 增加orderType filter
		String orderType = paramsMap.get("orderType");// 0,1,2
		if (StringUtils.isNotEmpty(orderType)) {
			StringBuffer orderTypeSB = new StringBuffer();
			String[] customers = orderType.split("\\,");
			for (int i = 0; i < customers.length; i++) {
				if (i != 0) {
					orderTypeSB.append(" OR ");
				}
				orderTypeSB.append("orderType:").append(customers[i]);
			}
			fqList.add(orderTypeSB.toString());
		}
		logger.error("Filter mylog:fqList.add(orderTypeSB.toString())==>"
				+ fqList.toString());
		// 增加其他Filter
		for (String field : COMMON_FIELDS) {
			if (orderType != null && orderType.indexOf(",") > -1
					&& field.equals("orderType"))
				continue;
			String value = paramsMap.get(field);
			if (StringUtils.isNotEmpty(value)) {
				fqList.add(field + ":" + value);
			}
		}
		if (paramsMap.get("isDispaly") != null
				&& !(paramsMap.get("isDispaly").toString().isEmpty())) {
			fqList.add("isDispaly" + ":"
					+ paramsMap.get("isDispaly").toString());
		}
		logger.error("Filter mylog:" + query.toString());
		logger.error("Filter mylog:query.setFilterQueries==>"
				+ fqList.toArray(new String[fqList.size()]));
		query.setFilterQueries(fqList.toArray(new String[fqList.size()]));
		logger.error("Filter mylog:setFilterQueries==========end");
	}

	private long getNumDay(String ymd) throws ParseException {
		if (StringUtils.isEmpty(ymd)) {

			return 0;
		}
		Date date = DateUtils.parseDate(ymd, new String[] { "yyyy-MM-dd" });
		return getNumDay(date);
	}

	private static long getNumDay(Date ymd) throws ParseException {
		if (ymd == null) {
			return 0;
		}
		Date originalDate = DateUtils.parseDate("2011-11-19",
				new String[] { "yyyy-MM-dd" });
		long result = (ymd.getTime() - originalDate.getTime())
				/ (1000 * 60 * 60 * 24);
		if (result < 0)
			result = 0;
		return result;
	}

	public long getNumDayChange(Date ymd) throws ParseException {
		if (ymd == null) {
			return 0;
		}
		// log.error("getNumDayChange=============>"+ymd);
		/*
		 * Date originalDate = DateUtils.parseDate("2011-11-19", new String[] {
		 * "yyyy-MM-dd" });
		 */
		Date originalDate = holdTimeFormat.parse("2011-11-19");
		// log.error("originalDate=============>"+originalDate);
		long result = (ymd.getTime() - originalDate.getTime())
				/ (1000 * 60 * 60 * 24);
		// log.error("result=============>"+result);
		if (result < 0)
			result = 0;
		return result;
	}

	public static void main(String[] main) throws ParseException {
		SimpleDateFormat holdTimeFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		int s = (int) getNumDay(holdTimeFormat2.parse("2013-09-07 16:54:37"));
		System.out.println(s);
	}

	@Override
	public Map<Short, Integer> searchEccoreDataGroupStatus(String solrUrl,
			Pagination<EccoreSearchResultDTO> searchPage) throws Exception {

		Map<Short, Integer> result = new HashMap<Short, Integer>();
		if (!resetDate(searchPage.getParams())) {
			return result;
		}
		SolrServer solrServer = getSolrServer(solrUrl);
		SolrQuery query = new SolrQuery();
		// 添加搜索条件
		setQuery(query, searchPage);
		// 添加过滤条件
		setFilterQueries(query, searchPage);
		query.addFacetField("numStatus");
		QueryResponse rsp = solrServer.query(query);
		logger.error("查询 Solr searchEccoreDataGroupStatus 用时:" + rsp.getQTime());
		List<Count> counts = rsp.getFacetField("numStatus").getValues();

		for (Count count : counts) {
			result.put(Short.valueOf(count.getName()),
					Long.valueOf(count.getCount()).intValue());
		}
		return result;
	}

	@Override
	public Map<Short, Integer> searchEccoreDataGroupOrderType(String solrUrl,
			Pagination<EccoreSearchResultDTO> searchPage) throws Exception {
		Map<Short, Integer> result = new HashMap<Short, Integer>();
		if (!resetDate(searchPage.getParams())) {
			return result;
		}
		SolrServer solrServer = getSolrServer(solrUrl);
		SolrQuery query = new SolrQuery();
		// 添加搜索条件
		setQuery(query, searchPage);
		// 添加过滤条件
		setFilterQueries(query, searchPage);
		query.addFacetField("orderType");
		QueryResponse rsp = solrServer.query(query);
		List<Count> counts = rsp.getFacetField("orderType").getValues();
		for (Count count : counts) {
			result.put(Short.valueOf(count.getName()),
					Long.valueOf(count.getCount()).intValue());
		}
		return result;
	}

	private CommonsHttpSolrServer getSolrServer() throws MalformedURLException {
		return new CommonsHttpSolrServer("http://yfb.ec.net.cn/isolr/eccore");
		// return new
		// CommonsHttpSolrServer("http://yfb.ec.net.cn/isolr/eccore1/");
		// return new
		// CommonsHttpSolrServer("http://127.0.0.1:8083/solr/eccore/");
	}

	public List<BuildSearch> getBuildData(int limit) {
		return buildSearchDao.getBuildSearchByLimit(limit);
	}

	public List<BuildSearchStatusWeightIndex> getUpdateBuildData(int limit,
			String buildFlag) {
		return buildSearchStatusWeightIndexDao.getResultByLimit(limit);
	}

	public List<BuildSearchStatusWeightIndex> getUpdateBuildData2(int limit,
			int type) {

		return buildSearchStatusWeightIndexDao.getResultByLimit2(limit, type);
	}

	@Override
	public void buildPartEccoreData(String solrUrl, int limit, String buildFlag) {
		try {
			long t1 = System.currentTimeMillis();
			logger.info("buildPartEccoreData:开始连接solr server:"
					+ System.currentTimeMillis());
			CommonsHttpSolrServer server = getSolrServer(solrUrl);
			logger.error("buildPartEccoreData:成功连接solr server:"
					+ System.currentTimeMillis());
			List<MailObjectDTO> mailObjectDTOs = orderDao
					.bulidPartEccoreData(limit); // wocao
			logger.error("buildPartEccoreData:orderDao.bulidPartEccoreData 查询完毕:"
					+ System.currentTimeMillis());
			List<EccoreItem> mailNoNull = new ArrayList<EccoreItem>();
			List<EccoreItem> mailNoChange = new ArrayList<EccoreItem>();
			List<EccoreItem> statusChange = new ArrayList<EccoreItem>();

			List<String> delItem = new ArrayList<String>();

			MailObjectDTO mailObjectDTO = new MailObjectDTO();
			String status = "0";

			String dateStr = "2011-11-19";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
			Date originalDate = simpleDateFormat.parse(dateStr);

			for (int j = 0; j < mailObjectDTOs.size(); j++) {
				mailObjectDTO = mailObjectDTOs.get(j);
				EccoreItem bean = new EccoreItem();
				bean.setId(mailObjectDTO.getId());
				status = mailObjectDTO.getStatus();
				if (StringUtils.equals(status, StatusEnum.CREATE.getValue()
						.toString())) {
					status = StatusEnum.CREATE.getName();
				}
				if (StatusEnum.isExitEnum(status) == false)
					continue;

				bean.setNumStatus(StatusEnum.valueOf(status).getValue());

				if (StringUtils.isEmpty(mailObjectDTO.getMailNo())) {
					// 运单号为空 solr不允许搜索此数据
					bean.setMailNo("YTO" + mailObjectDTO.getId());
					bean.setIsDispaly(new Integer(1).shortValue());
				} else {
					// 支持
					bean.setMailNo(mailObjectDTO.getMailNo());
					bean.setIsDispaly(new Integer(0).shortValue());
				}

				bean.setNumCreateTime((int) getNumDay(mailObjectDTO
						.getPartitiondate()));
				bean.setCreateTime(mailObjectDTO.getCreateTime());
				bean.setAcceptTime(mailObjectDTO.getAcceptTime());
				bean.setCustomerId(mailObjectDTO.getCustomerId());
				bean.setName(mailObjectDTO.getName());

				bean.setPhone(new String[] { mailObjectDTO.getMobile(),
						mailObjectDTO.getPhone() });

				bean.setDisplayPhone(mailObjectDTO.getMobile());
				if (StringUtils.isEmpty(bean.getDisplayPhone())) {
					bean.setDisplayPhone(mailObjectDTO.getPhone());
				}
				bean.setNumProv(Integer.valueOf(mailObjectDTO.getNumProv()));

				bean.setNumCity(Resource.getCodeByName(mailObjectDTO.getCity()));

				bean.setNumDistrict(Resource.getCodeByName(mailObjectDTO
						.getDistrict()));
				bean.setAddress(mailObjectDTO.getAddress());
				bean.setWeight(mailObjectDTO.getWeight());
				/**** Added by Johnson,2013-08-06 *****/
				if (mailObjectDTO.getOrderType() == null) {
					bean.setOrderType(new Integer(0).shortValue());
				} else {
					bean.setOrderType(new Integer(mailObjectDTO.getOrderType())
							.shortValue());
				}
				if (mailObjectDTO.getLineType() == null) { // 如果line_type为null,默认设置为线下订单:1
					bean.setLineType(new Integer(1).shortValue());
				} else {
					bean.setLineType(mailObjectDTO.getLineType().shortValue());
				}
				/**************************************/
				bean.setTrimFreight(mailObjectDTO.getTrimFreight());
				if (mailObjectDTO.getFreightType() != null) {
					bean.setFreightType(mailObjectDTO.getFreightType()
							.shortValue());
				} else {
					bean.setFreightType(new Integer(0).shortValue());
				}

				bean.setCityF(mailObjectDTO.getCityF());
				bean.setNumProvF(Resource.getCodeByName(mailObjectDTO
						.getProvF()));

				if (!StringUtils.isEmpty(mailObjectDTO.getHoldTime())) {
					bean.setHoldTime((int) getNumDay(holdTimeFormat
							.parse(mailObjectDTO.getHoldTime())));
				}

				bean.setMailNoAndCustomerId(bean.getMailNo()
						+ mailObjectDTO.getCustomerId());

				bean.setTxLogisticId(mailObjectDTO.getTxLogisticId());

				if (StringUtils.equals(mailObjectDTO.getBuildTask(), "1")) {
					// add mailNo
					mailNoNull.add(bean);
					delItem.add(bean.getMailNo()
							+ mailObjectDTO.getCustomerId());
				} else if (StringUtils
						.equals(mailObjectDTO.getBuildTask(), "2")) {
					// 更新mailNo
					mailNoChange.add(bean);
					delItem.add(mailObjectDTO.getOldMailNo()
							+ mailObjectDTO.getCustomerId());
				} else if (StringUtils
						.equals(mailObjectDTO.getBuildTask(), "3")) {
					// 删除订单
					delItem.add(bean.getMailNo()
							+ mailObjectDTO.getCustomerId());
				} else {
					// 新增订单
					statusChange.add(bean);
				}

			}
			logger.error("buildPartEccoreData:for 循环完毕:"
					+ System.currentTimeMillis());
			long t2 = System.currentTimeMillis();
			if (delItem.size() > 0) {
				server.deleteById(delItem);
				server.commit();
				logger.error("buildPartEccoreData:delItem:" + delItem.size()
						+ "用时：" + (System.currentTimeMillis() - t2));
				delItem.clear();
			}
			logger.error("buildPartEccoreData:delItem.commit:"
					+ System.currentTimeMillis());
			long t3 = System.currentTimeMillis();
			if (mailNoNull.size() > 0) {
				server.addBeans(mailNoNull);
				server.commit();
				logger.error("buildPartEccoreData:mailNonULL:"
						+ mailNoNull.size() + "用时："
						+ (System.currentTimeMillis() - t3));
				mailNoNull.clear();

			}
			logger.error("buildPartEccoreData:mailNoNull.commit:"
					+ System.currentTimeMillis());
			long t4 = System.currentTimeMillis();
			if (statusChange.size() > 0) {
				server.addBeans(statusChange);
				server.commit();
				logger.error("buildPartEccoreData:statusChange:"
						+ statusChange.size() + "用时："
						+ (System.currentTimeMillis() - t4));
				statusChange.clear();
			}
			logger.error("buildPartEccoreData:statusChange.commit:"
					+ System.currentTimeMillis());
			long t5 = System.currentTimeMillis();
			if (mailNoChange.size() > 0) {
				server.addBeans(mailNoChange);
				server.commit();
				logger.error("buildPartEccoreData:mailNoChange:"
						+ mailNoChange.size() + "用时："
						+ (System.currentTimeMillis() - t5));
				mailNoChange.clear();
			}
			logger.error("buildPartEccoreData:mailNoChange.commit:"
					+ System.currentTimeMillis());
			limit = mailObjectDTOs.size();
			if (limit > 0) {
				if ("1".equals(buildFlag)) {
					buildSearchDao.removeBuildSearchForAddByLimit(limit);
				}
				buildSearchDao.removeBuildSearchByLimit(limit);
			}
			logger.error("buildPartEccoreData:removeBuildSearchByLimit:"
					+ System.currentTimeMillis());
			mailObjectDTOs.clear();

		} catch (Exception e) {
			logger.error("build error", e);
		}

	}

	@Override
	public void newBuildPartEccoreData(String solrUrl,
			List<BuildSearch> buildSearchs, String buildFlag) {
		try {
			long t1 = System.currentTimeMillis();
			logger.info("buildPartEccoreData:开始连接solr server:"
					+ System.currentTimeMillis());
			CommonsHttpSolrServer server = getSolrServer(solrUrl);
			logger.error("buildPartEccoreData:成功连接solr server:"
					+ System.currentTimeMillis());
			/*
			 * List<BuildSearch> buildSearchs =
			 * buildSearchDao.getBuildSearchByLimit(limit); //wocao
			 */logger
					.error("buildPartEccoreData:orderDao.bulidPartEccoreData 查询完毕:"
							+ System.currentTimeMillis());
			List<EccoreItem> mailNoNull = new ArrayList<EccoreItem>();
			List<EccoreItem> mailNoChange = new ArrayList<EccoreItem>();
			List<EccoreItem> statusChange = new ArrayList<EccoreItem>();

			List<String> delItem = new ArrayList<String>();

			BuildSearch buildSearch = new BuildSearch();
			String status = "0";

			String dateStr = "2011-11-19";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
			Date originalDate = simpleDateFormat.parse(dateStr);
			int minId = 0, maxId = 0;
			for (int j = 0; j < buildSearchs.size(); j++) {
				buildSearch = buildSearchs.get(j);
				if (j == 0) {
					minId = buildSearch.getId();
					maxId = buildSearch.getId();
				} else {
					if (buildSearch.getId() > maxId) {
						maxId = buildSearch.getId();
					}
					if (buildSearch.getId() < minId) {
						minId = buildSearch.getId();
					}
				}
				EccoreItem bean = new EccoreItem();
				bean.setId(buildSearch.getOrderId());
				if (buildSearch.getStatus() != null)
					status = buildSearch.getStatus();
				if (StringUtils.equals(status, StatusEnum.CREATE.getValue()
						.toString())) {
					status = StatusEnum.CREATE.getName();
				}
				if (StatusEnum.isExitEnum(status) == false)
					continue;

				bean.setNumStatus(StatusEnum.valueOf(status).getValue());

				if (StringUtils.isEmpty(buildSearch.getMailNo())) {
					// 运单号为空 solr不允许搜索此数据
					bean.setMailNo("YTO" + buildSearch.getId());
					bean.setIsDispaly(new Integer(1).shortValue());
				} else {
					// 支持
					bean.setMailNo(buildSearch.getMailNo());
					bean.setIsDispaly(new Integer(0).shortValue());
				}

				bean.setNumCreateTime((int) getNumDay(buildSearch
						.getPartitiondate()));
				bean.setCreateTime(buildSearch.getCreateTime());
				bean.setAcceptTime(buildSearch.getAcceptTime());
				bean.setCustomerId(buildSearch.getCustomerId());
				bean.setName(buildSearch.getName());

				bean.setPhone(new String[] { buildSearch.getMobile(),
						buildSearch.getPhone() });

				bean.setDisplayPhone(buildSearch.getMobile());
				if (StringUtils.isEmpty(bean.getDisplayPhone())) {
					bean.setDisplayPhone(buildSearch.getPhone());
				}
				bean.setNumProv(Integer.valueOf(buildSearch.getNumProv()));

				bean.setNumCity(Resource.getCodeByName(buildSearch.getCity()));

				bean.setNumDistrict(Resource.getCodeByName(buildSearch
						.getDistrict()));
				bean.setAddress(buildSearch.getAddress());
				bean.setWeight(buildSearch.getWeight());
				/**** Added by Johnson,2013-08-06 *****/
				if (buildSearch.getOrderType() == 0) {
					bean.setOrderType(new Integer(0).shortValue());
				} else {
					bean.setOrderType(new Integer(buildSearch.getOrderType())
							.shortValue());
				}
				if (buildSearch.getLineType() == null) { // 如果line_type为null,默认设置为线下订单:1
					bean.setLineType(new Integer(1).shortValue());
				} else {
					bean.setLineType(new Integer(0).shortValue());
				}
				/**************************************/
				bean.setTrimFreight((float) buildSearch.getTrimFreight());
				if (buildSearch.getFreightType() != null) {
					bean.setFreightType(new Integer(1).shortValue());
				} else {
					bean.setFreightType(new Integer(0).shortValue());
				}

				bean.setCityF(buildSearch.getCityF());
				bean.setNumProvF(Resource.getCodeByName(buildSearch.getProvF()));

				if (!StringUtils.isEmpty(buildSearch.getHoldTime())) {
					// bean.setHoldTime(Integer.parseInt(buildSearch.getHoldTime()));
					String holdTime = buildSearch.getHoldTime().trim();
					logger.info("方法名：newBuildPartEccoreData;holdTime :"
							+ buildSearch.getHoldTime() + "orderID:"
							+ buildSearch.getOrderId());
					if (holdTime.length() == 19 || holdTime.length() == 10) {
						// holdTime = holdTime.substring(0,10);
						bean.setHoldTime((int) getNumDay(holdTimeFormat
								.parse(holdTime)));
					} else {
						bean.setHoldTime((int) getNumDay(holdTimeFormat
								.parse(holdTimeFormat.format(new Date()))));
					}
				}
				bean.setMailNoAndCustomerId(bean.getMailNo()
						+ buildSearch.getCustomerId());

				bean.setTxLogisticId(buildSearch.getTxLogisticId());

				if (StringUtils.equals(buildSearch.getBuildTask(), "1")) {
					// add mailNo
					mailNoNull.add(bean);
					delItem.add(bean.getMailNo() + buildSearch.getCustomerId());
				} else if (StringUtils.equals(buildSearch.getBuildTask(), "2")) {
					// 更新mailNo
					mailNoChange.add(bean);
					delItem.add(buildSearch.getOldMailNo()
							+ buildSearch.getCustomerId());
				} else if (StringUtils.equals(buildSearch.getBuildTask(), "3")) {
					// 删除订单
					delItem.add(bean.getMailNo() + buildSearch.getCustomerId());
				} else {
					// 新增订单
					statusChange.add(bean);
				}

			}
			logger.error("buildPartEccoreData:for 循环完毕:"
					+ System.currentTimeMillis());
			long t2 = System.currentTimeMillis();
			if (delItem.size() > 0) {
				UpdateResponse r = server.deleteById(delItem);
				server.commit();
				logger.error("buildPartEccoreData:delItem:" + delItem.size()
						+ "用时：" + (System.currentTimeMillis() - t2));
				delItem.clear();
			}
			logger.error("buildPartEccoreData:delItem.commit:"
					+ System.currentTimeMillis());
			long t3 = System.currentTimeMillis();
			if (mailNoNull.size() > 0) {
				UpdateResponse r = server.addBeans(mailNoNull);
				server.commit();
				logger.error("buildPartEccoreData:mailNonULL:"
						+ mailNoNull.size() + "用时："
						+ (System.currentTimeMillis() - t3));
				mailNoNull.clear();

			}
			logger.error("buildPartEccoreData:mailNoNull.commit:"
					+ System.currentTimeMillis());
			long t4 = System.currentTimeMillis();
			if (statusChange.size() > 0) {
				server.addBeans(statusChange);
				server.commit();
				logger.error("buildPartEccoreData:statusChange:"
						+ statusChange.size() + "用时："
						+ (System.currentTimeMillis() - t4));
				statusChange.clear();
			}
			logger.error("buildPartEccoreData:statusChange.commit:"
					+ System.currentTimeMillis());
			long t5 = System.currentTimeMillis();
			if (mailNoChange.size() > 0) {
				server.addBeans(mailNoChange);
				server.commit();
				logger.error("buildPartEccoreData:mailNoChange:"
						+ mailNoChange.size() + "用时："
						+ (System.currentTimeMillis() - t5));
				mailNoChange.clear();
			}
			logger.error("buildPartEccoreData:mailNoChange.commit:"
					+ System.currentTimeMillis());
			int limit = buildSearchs.size();
			logger.error("buildPartEccoreData:minid=" + minId + "maxId="
					+ maxId);
			if (limit > 0) {

				if ("1".equals(buildFlag)) {
					buildSearchDao.removeBuildSearchForAddById(minId, maxId);
				} else {
					buildSearchDao.removeBuildSearchById(minId, maxId);
				}
			}
			logger.error("buildPartEccoreData:removeBuildSearchByLimit:"
					+ System.currentTimeMillis());
			// buildSearchs.clear();

		} catch (Exception e) {
			logger.error("build error", e);
		}

	}

	private boolean resetDate(Map<String, String> paramsMap) throws Exception {
		String startDate = paramsMap.get("startDate");// 2011-01-08
		String endDate = paramsMap.get("endDate");// 2011-01-08
		if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
			return false;
		}
		Date originalDate = DateUtils.parseDate("2011-11-19",
				new String[] { "yyyy-MM-dd" });
		Date realStartDate = DateUtils.parseDate(startDate,
				new String[] { "yyyy-MM-dd" });
		Date realEndDate = DateUtils.parseDate(endDate,
				new String[] { "yyyy-MM-dd" });

		if (DateUtil.compareDay(realStartDate, originalDate)
				&& DateUtil.compareDay(realEndDate, originalDate)) {
			return false;
		} else if (DateUtil.compareDay(realStartDate, originalDate)) {
			paramsMap.put("startDate", "2011-11-19");
			return true;
		} else if (DateUtil.compareDay(realEndDate, originalDate)) {
			return false;
		}
		return true;
	}

	@Override
	public void delEccoreData(String solrUrl, Map<String, String> map)
			throws Exception {
		if (StringUtils.isEmpty(solrUrl) || map == null || map.isEmpty()) {
			return;
		}
		CommonsHttpSolrServer server = getSolrServer(solrUrl);
		String customerId = map.get("customerId");
		if (StringUtils.isEmpty(customerId)) {
			return;
		}
		server.deleteByQuery("customerId:" + customerId);
		server.commit();
	}

	@Override
	public void buildUpdatePartEccoreData(String solrUrl,
			List<BuildSearchStatusWeightIndex> buildSearchs, String buildFlag) {
		try {
			long t1 = System.currentTimeMillis();
			logger.info("buildUpdatePartEccoreData:开始连接solr server:"
					+ System.currentTimeMillis());
			CommonsHttpSolrServer server = getSolrServer(solrUrl);
			logger.info("buildUpdatePartEccoreData:成功连接solr server:"
					+ System.currentTimeMillis());
			/*
			 * List<BuildSearch> buildSearchs =
			 * buildSearchDao.getBuildSearchByLimit(limit); //wocao
			 */logger
					.error("buildUpdatePartEccoreData:orderDao.bulidPartEccoreData 查询完毕:"
							+ System.currentTimeMillis());
			List<EccoreItem> mailNoNull = new ArrayList<EccoreItem>();
			List<EccoreItem> mailNoChange = new ArrayList<EccoreItem>();
			List<EccoreItem> statusChange = new ArrayList<EccoreItem>();

			List<String> delItem = new ArrayList<String>();

			BuildSearchStatusWeightIndex buildSearch = new BuildSearchStatusWeightIndex();
			String status = "0";

			String dateStr = "2011-11-19";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
			Date originalDate = simpleDateFormat.parse(dateStr);
			int minId = 0, maxId = 0;
			for (int j = 0; j < buildSearchs.size(); j++) {
				buildSearch = buildSearchs.get(j);
				if (j == 0) {
					minId = buildSearch.getId();
					maxId = buildSearch.getId();
				} else {
					if (buildSearch.getId() > maxId) {
						maxId = buildSearch.getId();
					}
					if (buildSearch.getId() < minId) {
						minId = buildSearch.getId();
					}
				}
				EccoreItem bean = new EccoreItem();
				bean.setId(buildSearch.getOrderId());
				if (buildSearch.getStatus() != null)
					status = buildSearch.getStatus();
				if (StringUtils.equals(status, StatusEnum.CREATE.getValue()
						.toString())) {
					status = StatusEnum.CREATE.getName();
				}
				if (StatusEnum.isExitEnum(status) == false)
					continue;

				bean.setNumStatus(StatusEnum.valueOf(status).getValue());

				if (StringUtils.isEmpty(buildSearch.getMailNo())) {
					// 运单号为空 solr不允许搜索此数据
					bean.setMailNo("YTO" + buildSearch.getId());
					bean.setIsDispaly(new Integer(1).shortValue());
				} else {
					// 支持
					bean.setMailNo(buildSearch.getMailNo());
					bean.setIsDispaly(new Integer(0).shortValue());
				}

				bean.setNumCreateTime((int) getNumDay(buildSearch
						.getPartitiondate()));
				bean.setCreateTime(buildSearch.getCreateTime());
				bean.setAcceptTime(buildSearch.getAcceptTime());
				bean.setCustomerId(buildSearch.getCustomerId());
				bean.setName(buildSearch.getName());

				bean.setPhone(new String[] { buildSearch.getMobile(),
						buildSearch.getPhone() });

				bean.setDisplayPhone(buildSearch.getMobile());
				if (StringUtils.isEmpty(bean.getDisplayPhone())) {
					bean.setDisplayPhone(buildSearch.getPhone());
				}
				bean.setNumProv(Integer.valueOf(buildSearch.getNumProv()));

				bean.setNumCity(Resource.getCodeByName(buildSearch.getCity()));

				bean.setNumDistrict(Resource.getCodeByName(buildSearch
						.getDistrict()));
				bean.setAddress(buildSearch.getAddress());
				bean.setWeight(buildSearch.getWeight());
				/**** Added by Johnson,2013-08-06 *****/
				if (buildSearch.getOrderType() == 0) {
					bean.setOrderType(new Integer(0).shortValue());
				} else {
					bean.setOrderType(new Integer(buildSearch.getOrderType())
							.shortValue());
				}
				if (buildSearch.getLineType() == null) { // 如果line_type为null,默认设置为线下订单:1
					bean.setLineType(new Integer(1).shortValue());
				} else {
					bean.setLineType(new Integer(0).shortValue());
				}
				/**************************************/
				bean.setTrimFreight((float) buildSearch.getTrimFreight());
				if (buildSearch.getFreightType() != null) {
					bean.setFreightType(new Integer(1).shortValue());
				} else {
					bean.setFreightType(new Integer(0).shortValue());
				}

				bean.setCityF(buildSearch.getCityF());
				bean.setNumProvF(Resource.getCodeByName(buildSearch.getProvF()));

				if (!StringUtils.isEmpty(buildSearch.getHoldTime())) {
					// bean.setHoldTime(Integer.parseInt(buildSearch.getHoldTime()));
					String holdTime = buildSearch.getHoldTime().trim();
					logger.info("方法名：buildUpdatePartEccoreData; holdTime :"
							+ buildSearch.getHoldTime() + "orderID:"
							+ buildSearch.getOrderId());
					if (holdTime.length() == 19 || holdTime.length() == 10) {
						// log.error("holdTime=============>"+holdTime);
						// holdTime = holdTime.substring(0, 10);
						Date dateTemp = holdTimeFormat.parse(holdTime);
						// log.error("dateTemp=============>"+dateTemp);
						long dayTemp = getNumDay(dateTemp);
						// long dayTemp = getNumDayChange(dateTemp);
						// log.error("dayTemp=============>"+dayTemp);
						bean.setHoldTime((int) dayTemp);
					} else {
						bean.setHoldTime((int) getNumDay(holdTimeFormat
								.parse(holdTimeFormat.format(new Date()))));
					}

					// bean.setHoldTime(665);
				}
				bean.setMailNoAndCustomerId(bean.getMailNo()
						+ buildSearch.getCustomerId());

				bean.setTxLogisticId(buildSearch.getTxLogisticId());

				if (StringUtils.equals(buildSearch.getBuildTask(), "1")) {
					// add mailNo
					mailNoNull.add(bean);
					delItem.add(bean.getMailNo() + buildSearch.getCustomerId());
				} else if (StringUtils.equals(buildSearch.getBuildTask(), "2")) {
					// 更新mailNo
					mailNoChange.add(bean);
					delItem.add(buildSearch.getOldMailNo()
							+ buildSearch.getCustomerId());
				} else if (StringUtils.equals(buildSearch.getBuildTask(), "3")) {
					// 删除订单
					delItem.add(bean.getMailNo() + buildSearch.getCustomerId());
				} else {
					// 新增订单
					statusChange.add(bean);
				}

			}
			logger.error("buildUpdatePartEccoreData:for 循环完毕:"
					+ System.currentTimeMillis());
			long t2 = System.currentTimeMillis();
			if (delItem.size() > 0) {
				try {
					server.deleteById(delItem);
					server.commit();
				} catch (Exception e) {
					logger.error("delItem build error", e);
					throw e;

				}
				logger.error("mylog:buildUpdatePartEccoreData:delItem:"
						+ delItem.size() + "用时："
						+ (System.currentTimeMillis() - t2));
				delItem.clear();
			}
			logger.error("mylog:buildUpdatePartEccoreData:delItem.commit:"
					+ System.currentTimeMillis());
			long t3 = System.currentTimeMillis();
			if (mailNoNull.size() > 0) {
				try {
					server.addBeans(mailNoNull);
					server.commit();
				} catch (Exception e) {
					logger.error("mailNoNull build error", e);
					throw e;

				}
				logger.error("mylog:buildUpdatePartEccoreData:mailNonULL:"
						+ mailNoNull.size() + "用时："
						+ (System.currentTimeMillis() - t3));
				mailNoNull.clear();

			}
			logger.error("mylog:buildUpdatePartEccoreData:mailNoNull.commit:"
					+ System.currentTimeMillis());
			long t4 = System.currentTimeMillis();
			if (statusChange.size() > 0) {
				try {
					server.addBeans(statusChange);
					server.commit();
				} catch (Exception e) {
					logger.error("statusChange build error", e);
					throw e;

				}
				logger.error("mylog:buildUpdatePartEccoreData:statusChange:"
						+ statusChange.size() + "用时："
						+ (System.currentTimeMillis() - t4));
				statusChange.clear();
			}
			logger.error("mylog:buildUpdatePartEccoreData:statusChange.commit:"
					+ System.currentTimeMillis());
			long t5 = System.currentTimeMillis();
			if (mailNoChange.size() > 0) {
				try {
					server.addBeans(mailNoChange);
					server.commit();
				} catch (Exception e) {
					logger.error("mailNoChange build error", e);
					throw e;

				}
				logger.error("mylog:buildPartEccoreData:mailNoChange:"
						+ mailNoChange.size() + "用时："
						+ (System.currentTimeMillis() - t5));
				mailNoChange.clear();
			}
			logger.error("mylog:buildUpdatePartEccoreData:mailNoChange.commit:"
					+ System.currentTimeMillis());
			int limit = buildSearchs.size();
			logger.error(buildFlag + "mylog:buildUpdatePartEccoreData:minid="
					+ minId + "maxId=" + maxId);
			if (limit > 0) {
				if ("1".equals(buildFlag)) {
					long t = System.currentTimeMillis();
					buildSearchStatusWeightIndexDao.removeBuildSearchById(
							minId, maxId);
					logger.error("mylogt1:delete"
							+ (System.currentTimeMillis() - t));
				} else if ("2".equals(buildFlag)) {
					long t = System.currentTimeMillis();
					buildSearchStatusWeightIndexDao.removeBuildSearchById2(
							minId, maxId, 2);
					logger.error("mylogt2:delete"
							+ (System.currentTimeMillis() - t));
				} else if ("3".equals(buildFlag)) {
					long t = System.currentTimeMillis();
					buildSearchStatusWeightIndexDao.removeBuildSearchById2(
							minId, maxId, 3);
					logger.error("mylogt3:delete"
							+ (System.currentTimeMillis() - t));
				} else if ("4".equals(buildFlag)) {
					long t = System.currentTimeMillis();
					buildSearchStatusWeightIndexDao.removeBuildSearchById2(
							minId, maxId, 4);
					logger.error("mylogt4:delete"
							+ (System.currentTimeMillis() - t));
				} else if ("5".equals(buildFlag)) {
					long t = System.currentTimeMillis();
					buildSearchStatusWeightIndexDao.removeBuildSearchById2(
							minId, maxId, 5);
					logger.error("mylogt5:delete"
							+ (System.currentTimeMillis() - t));
				}

			}
			logger.error(buildFlag
					+ "mylog:buildUpdatePartEccoreData:removeBuildSearchByLimit:"
					+ buildSearchs.size());
			// buildSearchs.clear();

		} catch (Exception e) {
			logger.error("mylog:build error", e);
		}

	}

	@Override
	public void buildPartStatusWeightData(String solrUrl, Integer limit) {
		try {
			CommonsHttpSolrServer server = getSolrServer(solrUrl);

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
			String currentDate = simpleDateFormat.format(new Date());
			String endDate = simpleDateFormat
					.format(DateUtil.getDateBefore(30));
			List<MailObjectDTO> mailObjectDTOs = orderDao
					.bulidPartEccoreStatusData(limit, currentDate, endDate);
			// if (mailObjectDTOs.size() < limit) {
			// return;
			// }
			logger.error("EccoreSearchServiceImpl-----limit" + limit
					+ " mailObjectDTOs.size()=" + mailObjectDTOs.size());

			List<EccoreItem> mailNoNull = new ArrayList<EccoreItem>();
			List<EccoreItem> mailNoChange = new ArrayList<EccoreItem>();
			List<EccoreItem> statusChange = new ArrayList<EccoreItem>();

			List<String> delItem = new ArrayList<String>();

			MailObjectDTO mailObjectDTO = new MailObjectDTO();
			String status = "0";

			String dateStr = "2011-11-19";
			Date originalDate = simpleDateFormat.parse(dateStr);

			for (int j = 0; j < mailObjectDTOs.size(); j++) {
				mailObjectDTO = mailObjectDTOs.get(j);
				EccoreItem bean = new EccoreItem();
				bean.setId(mailObjectDTO.getId());
				status = mailObjectDTO.getStatus();
				if (StringUtils.equals(status, StatusEnum.CREATE.getValue()
						.toString())) {
					status = StatusEnum.CREATE.getName();
				}
				// if(status!=null&&status.indexOf("接单中")!=-1){
				// status = StatusEnum.ACCEPT.getName();
				// }
				if (StatusEnum.isExitEnum(status) == false)
					continue;

				bean.setNumStatus(StatusEnum.valueOf(status).getValue());

				if (StringUtils.isEmpty(mailObjectDTO.getMailNo())) {
					bean.setMailNo("YTO" + mailObjectDTO.getId());
					bean.setIsDispaly(new Integer(1).shortValue());
				} else {
					bean.setMailNo(mailObjectDTO.getMailNo());
					bean.setIsDispaly(new Integer(0).shortValue());
				}

				bean.setNumCreateTime((int) getNumDay(mailObjectDTO
						.getPartitiondate()));
				bean.setCreateTime(mailObjectDTO.getCreateTime());
				bean.setAcceptTime(mailObjectDTO.getAcceptTime());
				bean.setCustomerId(mailObjectDTO.getCustomerId());
				bean.setName(mailObjectDTO.getName());

				bean.setPhone(new String[] { mailObjectDTO.getMobile(),
						mailObjectDTO.getPhone() });
				bean.setDisplayPhone(mailObjectDTO.getMobile());
				if (StringUtils.isEmpty(bean.getDisplayPhone())) {
					bean.setDisplayPhone(mailObjectDTO.getPhone());
				}
				bean.setNumProv(Integer.valueOf(mailObjectDTO.getNumProv()));

				bean.setNumCity(Resource.getCodeByName(mailObjectDTO.getCity()));

				bean.setNumDistrict(Resource.getCodeByName(mailObjectDTO
						.getDistrict()));
				bean.setAddress(mailObjectDTO.getAddress());
				bean.setWeight(mailObjectDTO.getWeight());

				if (mailObjectDTO.getOrderType() != null) {
					bean.setOrderType(new Integer(mailObjectDTO.getOrderType())
							.shortValue());
				} else {
					// log.error("OrderType is null");
					bean.setOrderType((short) -1);
				}
				if (mailObjectDTO.getLineType() != null) {
					bean.setLineType(mailObjectDTO.getLineType().shortValue());
				} else {
					bean.setLineType((short) 1);
				}
				bean.setTrimFreight(mailObjectDTO.getTrimFreight());

				if (mailObjectDTO.getFreightType() != null) {
					bean.setFreightType(mailObjectDTO.getFreightType()
							.shortValue());
				} else {
					bean.setFreightType((short) 1);
				}

				bean.setCityF(mailObjectDTO.getCityF());
				bean.setNumProvF(Resource.getCodeByName(mailObjectDTO
						.getProvF()));
				if (!StringUtils.isEmpty(mailObjectDTO.getHoldTime().trim())) {
					bean.setHoldTime((int) getNumDay(holdTimeFormat
							.parse(mailObjectDTO.getHoldTime())));
				}
				bean.setMailNoAndCustomerId(bean.getMailNo()
						+ mailObjectDTO.getCustomerId());

				if (StringUtils.equals(mailObjectDTO.getBuildTask(), "1")) {
					mailNoNull.add(bean);
					delItem.add(bean.getMailNo()
							+ mailObjectDTO.getCustomerId());
				} else if (StringUtils
						.equals(mailObjectDTO.getBuildTask(), "2")) {
					mailNoChange.add(bean);
					delItem.add(mailObjectDTO.getOldMailNo()
							+ mailObjectDTO.getCustomerId());
				} else if (StringUtils
						.equals(mailObjectDTO.getBuildTask(), "3")) {
					delItem.add(bean.getMailNo()
							+ mailObjectDTO.getCustomerId());
				} else {
					statusChange.add(bean);
				}

			}
			logger.error("EccoreSearchServiceImpl bean set complete!");
			if (delItem.size() > 0) {
				server.deleteById(delItem);
				server.commit();
				logger.error("delItem:" + delItem.size());
				delItem.clear();
			}
			if (mailNoNull.size() > 0) {
				server.addBeans(mailNoNull);
				server.commit();
				logger.error("mailNonULL:" + mailNoNull.size());
				mailNoNull.clear();

			}
			if (statusChange.size() > 0) {
				server.addBeans(statusChange);
				server.commit();
				logger.error("statusChange:" + statusChange.size());
				statusChange.clear();
			}
			if (mailNoChange.size() > 0) {
				server.addBeans(mailNoChange);
				server.commit();
				logger.error("mailNoChange:" + mailNoChange.size());
				statusChange.clear();
			}
			limit = mailObjectDTOs.size();
			if (limit > 0) {
				buildSearchStatusDao.removeBuildSearchByLimit(limit);
			}
			mailObjectDTOs.clear();

		} catch (Exception e) {
			logger.error("build error", e);
		}

	}

	@Override
	public void getOrderEccoreSearchByOrderIds(String solrUrl,
			Pagination<EccoreSearchResultDTO> searchPage) throws Exception {
		Map<String, String> paramsMap = searchPage.getParams();
		SolrServer solrServer = getSolrServer(solrUrl);
		SolrQuery query = new SolrQuery();
		// 添加查询条件
		setQueryInOrderIds(query, searchPage);
		// 排序
		String sortType = paramsMap.get("sortType");
		if ("asc".equalsIgnoreCase(sortType)) {
			query.setSortField("id", ORDER.asc);
		} else {
			query.setSortField("id", ORDER.desc);
		}
		// 搜索
		QueryResponse rsp = solrServer.query(query);
		// 搜索结果处理
		searchPage.setTotalRecords(new Long(rsp.getResults().getNumFound())
				.intValue());
		List<EccoreItem> searchResult = rsp.getBeans(EccoreItem.class);
		List<EccoreSearchResultDTO> result = new ArrayList<EccoreSearchResultDTO>();
		// 转换搜索结果
		for (EccoreItem eccoreItem : searchResult) {
			EccoreSearchResultDTO eccoreSearchResultDTO = new EccoreSearchResultDTO();
			BeanUtils.copyProperties(eccoreItem, eccoreSearchResultDTO,
					new String[] { "numStatus", "phone", "displayPhone",
							"numCity", "numDistrict", "numCreateTime",
							"holdTime" });

			String dateStr = "2011-11-19";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
			Date originalDate = simpleDateFormat.parse(dateStr);
			if (eccoreItem.getHoldTime() != null) {
				eccoreSearchResultDTO.setHoldTime(DateUtils.addDays(
						originalDate, eccoreItem.getHoldTime()));
			}
			eccoreSearchResultDTO.setStatus(StatusEnum.getStatusName(
					eccoreItem.getNumStatus()).getName());
			eccoreSearchResultDTO.setProv(Resource.getNameById(eccoreItem
					.getNumProv()));
			eccoreSearchResultDTO.setCity(Resource.getNameById(eccoreItem
					.getNumCity()));
			eccoreSearchResultDTO.setDistrict(Resource.getNameById(eccoreItem
					.getNumDistrict()));
			eccoreSearchResultDTO
					.setAddress(eccoreSearchResultDTO.getAddress());
			// 显示电话
			eccoreSearchResultDTO.setPhone(eccoreItem.getDisplayPhone());
			result.add(eccoreSearchResultDTO);
		}
		searchPage.setRecords(result);
	}

	private void setQueryInOrderIds(SolrQuery query,
			Pagination<EccoreSearchResultDTO> searchPage) throws ParseException {
		Map<String, String> paramsMap = searchPage.getParams();
		String orderIds = paramsMap.get("orderIds");// 订单的id,以逗号“,”分隔
		if (StringUtils.isNotEmpty(orderIds)) {
			StringBuffer sbuffer = new StringBuffer();
			String[] orderArr = orderIds.split("\\,");
			for (int i = 0; i < orderArr.length; i++) {
				if (i != 0) {
					sbuffer.append(" OR ");
				}
				sbuffer.append("id:").append(orderArr[i]);
			}
			query.setQuery(sbuffer.toString());
		}
	}

	/**
	 * 添加订单id的数据作为查询条件
	 * 
	 * @param query
	 * @param searchPage
	 * @throws ParseException
	 */
	private void setFilterQueryInOrderIds(SolrQuery query,
			Pagination<EccoreSearchResultDTO> searchPage) throws ParseException {
		Map<String, String> paramsMap = searchPage.getParams();
		String orderIds = paramsMap.get("orderIds");// 订单的id,以逗号“,”分隔
		List<String> fqList = new ArrayList<String>();
		if (StringUtils.isNotEmpty(orderIds)) {
			StringBuffer sbuffer = new StringBuffer();
			String[] orderArr = orderIds.split("\\,");
			for (int i = 0; i < orderArr.length; i++) {
				if (i != 0) {
					sbuffer.append(" OR ");
				}
				sbuffer.append("id:").append(orderArr[i]);
			}
			fqList.add(sbuffer.toString());
		}
		query.setFilterQueries(fqList.toArray(new String[fqList.size()]));
	}

	@Override
	public Map<String, Integer> searchEccoreDataGroupCustomerId(String solrUrl,
			Pagination<EccoreSearchResultDTO> searchPage) throws Exception {
		Map<String, Integer> result = new HashMap<String, Integer>();
		if (!resetDate(searchPage.getParams())) {
			return result;
		}
		SolrServer solrServer = getSolrServer(solrUrl);
		SolrQuery query = new SolrQuery();
		// 添加搜索条件
		setQuery(query, searchPage);
		// 添加过滤条件
		setFilterQueries(query, searchPage);
		query.addFacetField("customerId");
		query.setFacetLimit(-1);
		QueryResponse rsp = solrServer.query(query);
		List<Count> counts = rsp.getFacetField("customerId").getValues();

		for (Count count : counts) {
			result.put(count.getName(), Long.valueOf(count.getCount())
					.intValue());
		}
		return result;
	}

	/**
	 * 查找超时件
	 */
	@Override
	public void searchOverTimeData(String solrUrl,
			Pagination<EccoreSearchResultDTO> searchPage) throws Exception {
		logger.info("searchOverTimerDateSolrUrl" + solrUrl);
		Map<String, String> paramsMap = searchPage.getParams();
		String startDateString = paramsMap.get("startDate"); // 下单时间 2012-06-20
		String endDateString = paramsMap.get("endDate"); // 下单时间

		if (!StringUtils.isBlank(startDateString)
				&& !StringUtils.isBlank(endDateString)) {
			// 获取搜索引擎
			SolrServer solrServer = getSolrServer(solrUrl);
			SolrQuery query = new SolrQuery();

			// 添加搜索条件
			setQueryOverData(query, searchPage);

			// 添加过滤条件
			setFilterQueriesOverData(query, searchPage);

			query.setStart((searchPage.getCurrentPage() - 1)
					* searchPage.getPageNum());
			query.setRows(searchPage.getPageNum());

			// 排序
			String sortType = paramsMap.get("sortType");
			if ("asc".equalsIgnoreCase(sortType)) {
				query.setSortField("numCreateTime", ORDER.asc);
			} else {
				query.setSortField("numCreateTime", ORDER.desc);
			}

			QueryResponse rsp = solrServer.query(query);

			// 搜索结果处理
			searchPage.setTotalRecords(new Long(rsp.getResults().getNumFound())
					.intValue());
			List<EccoreItem> searchResult = rsp.getBeans(EccoreItem.class);
			List<EccoreSearchResultDTO> result = new ArrayList<EccoreSearchResultDTO>();
			String dateStr = "2011-11-19";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
			Date originalDate = simpleDateFormat.parse(dateStr);
			// 转换搜索结果
			for (EccoreItem eccoreItem : searchResult) {
				EccoreSearchResultDTO eccoreSearchResultDTO = new EccoreSearchResultDTO();
				eccoreSearchResultDTO.setStatus(StatusEnum.getStatusName(
						eccoreItem.getNumStatus()).getName());
				eccoreSearchResultDTO.setName(eccoreItem.getName());
				eccoreSearchResultDTO.setPhone(eccoreItem.getDisplayPhone());
				eccoreSearchResultDTO.setOrderType(eccoreItem.getOrderType());
				eccoreSearchResultDTO.setMailNo(eccoreItem.getMailNo());
				eccoreSearchResultDTO.setAddress(eccoreItem.getAddress());

				eccoreSearchResultDTO.setProv(Resource.getNameById(eccoreItem
						.getNumProv()));
				eccoreSearchResultDTO.setCity(Resource.getNameById(eccoreItem
						.getNumCity()));
				eccoreSearchResultDTO.setDistrict(Resource
						.getNameById(eccoreItem.getNumDistrict()));

				eccoreSearchResultDTO.setCreateTime(eccoreItem.getCreateTime());
				eccoreSearchResultDTO.setAcceptTime(eccoreItem.getAcceptTime());
				if (eccoreItem.getHoldTime() != null) {
					eccoreSearchResultDTO.setHoldTime(DateUtils.addDays(
							originalDate, eccoreItem.getHoldTime()));
				}
				result.add(eccoreSearchResultDTO);

			}
			searchPage.setRecords(result);
		}
	}

	/**
	 * 查找揽收区间
	 * 
	 * @param query
	 * @param searchPage
	 * @throws ParseException
	 */
	// private void setQueryOverData(SolrQuery query,
	// Pagination<EccoreSearchResultDTO> searchPage) throws ParseException {
	// Map<String, String> paramsMap = searchPage.getParams();
	// String startDate = paramsMap.get("startDate");// 2011-01-08
	// String endDate = paramsMap.get("endDate");// 2011-01-08
	// StringBuffer days = new StringBuffer();
	// long numStartDay = getNumDay(startDate);
	// long numEndDay = getNumDay(endDate);
	// days.append("holdTime:");
	// days.append("[").append(numStartDay);
	// days.append(" TO ").append(numEndDay);
	// days.append("]");
	// query.setQuery(days.toString());
	// }

	private void setQueryOverData(SolrQuery query,
			Pagination<EccoreSearchResultDTO> searchPage) throws ParseException {
		Map<String, String> paramsMap = searchPage.getParams();
		String startDate = paramsMap.get("startDate");// 2011-01-08
		String endDate = paramsMap.get("endDate");// 2011-01-08
		StringBuffer days = new StringBuffer();
		long numStartDay = getNumDay(startDate);
		long numEndDay = getNumDay(endDate);
		for (long i = numStartDay; i <= numEndDay; i++) {
			if (i != numStartDay) {
				days.append(" OR ");
			}
			days.append("holdTime:").append(i);
		}
		query.setQuery(days.toString());
	}

	/**
	 * 超时件的过滤条件
	 * 
	 * @param query
	 * @param searchPage
	 * @throws ParseException
	 */
	private void setFilterQueriesOverData(SolrQuery query,
			Pagination<EccoreSearchResultDTO> searchPage) throws ParseException {
		Map<String, String> paramsMap = searchPage.getParams();
		String customerIDs = paramsMap.get("customerIDs");// 10213123,123123123
		// 增加filter
		List<String> fqList = new ArrayList();
		// 增加clientFilter
		if (StringUtils.isNotEmpty(customerIDs)) {
			StringBuffer customersSB = new StringBuffer();
			String[] customers = customerIDs.split("\\,");
			for (int i = 0; i < customers.length; i++) {
				if (i != 0) {
					customersSB.append(" OR ");
				}
				customersSB.append("customerId:").append(customers[i]);
			}
			fqList.add(customersSB.toString());
		}

		// 去除SIGNED 、 ACCEPT 、 UNACCEPT 、 CREATE 订单状态
		String statusList = paramsMap.get("statusList");
		if (StringUtils.isNotEmpty(statusList)) {
			StringBuffer statusString = new StringBuffer();
			String[] statusArray = statusList.split(",");
			for (int i = 0; i < statusArray.length; i++) {
				statusString.append(" -numStatus:")
						.append(StatusEnum.valueOf(statusArray[i]).getValue())
						.append(" ");
			}
			fqList.add(statusString.toString());
		}

		// 增加orderType filter
		String orderType = paramsMap.get("orderType");// 0,1,2
		if (StringUtils.isNotEmpty(orderType)) {
			StringBuffer orderTypeSB = new StringBuffer();
			String[] customers = orderType.split("\\,");
			for (int i = 0; i < customers.length; i++) {
				if (i != 0) {
					orderTypeSB.append(" OR ");
				}
				orderTypeSB.append("orderType:").append(customers[i]);
			}
			fqList.add(orderTypeSB.toString());
		}
		// 增加其他Filter
		for (String field : COMMON_FIELDS_OVER_DATA) {
			if (orderType != null && orderType.indexOf(",") > -1
					&& field.equals("orderType"))
				continue;
			String value = paramsMap.get(field);
			if (StringUtils.isNotEmpty(value)) {
				fqList.add(field + ":" + value);
			}
		}

		if (paramsMap.get("isDispaly") != null
				&& !(paramsMap.get("isDispaly").toString().isEmpty())) {
			fqList.add("isDispaly" + ":"
					+ paramsMap.get("isDispaly").toString());
		}

		String proAndDays = paramsMap.get("overTimeMap");
		String[] proAndDayArray = proAndDays.split(";");
		if (proAndDayArray != null && proAndDayArray.length > 0) {
			StringBuffer overTimeString = new StringBuffer();
			for (int i = 0; i < proAndDayArray.length; i++) {

				String proAndDay = proAndDayArray[i];
				String[] elements = proAndDay.split("\\,");
				if (elements != null && elements.length > 0) {
					// if (i != 0) {
					// overTimeString.append(" OR ");
					// }
					overTimeString.append("(numProv:")
							.append(Long.parseLong(elements[0]))
							.append(" AND ");
					for (int j = 0; j < Integer.parseInt(elements[1]); j++) {
						if (j != 0) {
							overTimeString.append(" AND ");
						}
						overTimeString.append("-holdTime:").append(
								(long) getNumDay(new Date()) - j);
					}
					overTimeString.append(") ");

				}
			}
			fqList.add(overTimeString.toString());
		}

		query.setFilterQueries(fqList.toArray(new String[fqList.size()]));
	}

	@Override
	public void searchOrderByMobile(String solrUrl,
			Pagination<EccoreSearchResultDTO> searchPage) throws Exception {
		Map<String, String> paramsMap = searchPage.getParams();
		String mobile = paramsMap.get("mobile");
		SolrServer solrServer = getSolrServer(solrUrl);
		SolrQuery query = new SolrQuery();
		if (StringUtils.isNotEmpty(mobile)) {
			query.setQuery("phone:" + mobile);
		}

		QueryResponse rsp = solrServer.query(query);

		// 搜索结果处理
		searchPage.setTotalRecords(new Long(rsp.getResults().getNumFound())
				.intValue());
		List<EccoreItem> searchResult = rsp.getBeans(EccoreItem.class);
		List<EccoreSearchResultDTO> result = new ArrayList<EccoreSearchResultDTO>();

		// 转换搜索结果
		for (EccoreItem eccoreItem : searchResult) {
			EccoreSearchResultDTO eccoreSearchResultDTO = new EccoreSearchResultDTO();
			eccoreSearchResultDTO.setId(eccoreItem.getId());
			eccoreSearchResultDTO.setTxLogisticId(eccoreItem.getTxLogisticId());
			result.add(eccoreSearchResultDTO);
		}
		searchPage.setRecords(result);
	}

	@Override
	public void searchOrderByMailNoAndCustomerId(String solrUrl,
			Pagination<EccoreSearchResultDTO> searchPage) throws Exception {

		Map<String, String> paramsMap = searchPage.getParams();
		String mailNoAndCustomerId = paramsMap.get("mailNoAndCustomerId");
		String numProv = paramsMap.get("numProv");
		String numCity = paramsMap.get("numCity");
		String numDistrict = paramsMap.get("numDistrict");

		SolrServer solrServer = getSolrServer(solrUrl);
		SolrQuery query = new SolrQuery();
		// 构造查询条件
		StringBuffer condition = new StringBuffer();
		if (StringUtils.isNotEmpty(mailNoAndCustomerId)) {
			condition.append(mailNoAndCustomerId);
		}
		if (StringUtils.isNotEmpty(numProv)) {
			query.setFilterQueries(" numProv:" + numProv);
		}
		if (StringUtils.isNotEmpty(numCity)) {
			query.setFilterQueries(" numCity:" + numCity);
		}
		if (StringUtils.isNotEmpty(numDistrict)) {
			query.setFilterQueries(" numDistrict:" + numDistrict);
		}
		query.setQuery(condition.toString());
		QueryResponse rsp = solrServer.query(query);

		// 搜索结果处理
		searchPage.setTotalRecords(new Long(rsp.getResults().getNumFound())
				.intValue());
		List<EccoreItem> searchResult = rsp.getBeans(EccoreItem.class);
		List<EccoreSearchResultDTO> result = new ArrayList<EccoreSearchResultDTO>();

		// 转换搜索结果
		for (EccoreItem eccoreItem : searchResult) {
			EccoreSearchResultDTO eccoreSearchResultDTO = new EccoreSearchResultDTO();
			eccoreSearchResultDTO.setStatus(StatusEnum.getStatusName(
					eccoreItem.getNumStatus()).getName());
			result.add(eccoreSearchResultDTO);
		}
		searchPage.setRecords(result);
	}

	@Override
	public void searchOrderByTimeMailNoCustomerId(String solrUrl,
			Pagination<EccoreSearchResultDTO> searchPage) throws Exception {
		SolrServer solrServer = getSolrServer(solrUrl);
		SolrQuery query = new SolrQuery();
		setQuery(query, searchPage);
		Map<String, String> paramsMap = searchPage.getParams();
		String customerIds = paramsMap.get("customerId");
		String numProv = paramsMap.get("numProv");
		String numCity = paramsMap.get("numCity");
		String numDistrict = paramsMap.get("numDistrict");
		if (StringUtils.isNotEmpty(customerIds)) {
			StringBuffer sb = new StringBuffer("");
			String[] customerIdArr = customerIds.split(",");
			for (int i = 0; i < customerIdArr.length; i++) {
				if (i < customerIdArr.length - 1)
					sb.append(" customerId:").append(customerIdArr[i])
							.append(" or ");
				else
					sb.append(" customerId:").append(customerIdArr[i]);
			}
			query.setFilterQueries(sb.toString());
		}
		if (StringUtils.isNotEmpty(numProv)) {
			query.setFilterQueries(" numProv:" + numProv);
		}
		if (StringUtils.isNotEmpty(numCity)) {
			query.setFilterQueries(" numCity:" + numCity);
		}
		if (StringUtils.isNotEmpty(numDistrict)) {
			query.setFilterQueries(" numDistrict:" + numDistrict);
		}
		QueryResponse rsp = solrServer.query(query);
		logger.error("查询    Solr searchOrderByTimeMailNoCustomerId 用时:"
				+ rsp.getQTime());
		searchPage.setTotalRecords(new Long(rsp.getResults().getNumFound())
				.intValue());
		List<EccoreItem> searchResult = rsp.getBeans(EccoreItem.class);
		List<EccoreSearchResultDTO> result = new ArrayList<EccoreSearchResultDTO>();
		for (EccoreItem eccoreItem : searchResult) {
			EccoreSearchResultDTO eccoreSearchResultDTO = new EccoreSearchResultDTO();
			eccoreSearchResultDTO.setStatus(StatusEnum.getStatusName(
					eccoreItem.getNumStatus()).getName());
			result.add(eccoreSearchResultDTO);
		}
		searchPage.setRecords(result);
	}

	@Override
	public void searchOrderByProvIdAndDate(String solrUrl,
			Pagination<EccoreSearchResultDTO> searchPage) throws Exception {
		SolrServer solrServer = getSolrServer(solrUrl);
		SolrQuery query = new SolrQuery();
		setQuery(query, searchPage);
		Map<String, String> paramsMap = searchPage.getParams();
		String numProv = paramsMap.get("numProv");
		String startDate = paramsMap.get("startDate");
		String endDate = paramsMap.get("endDate");
		if (StringUtils.isNotEmpty(numProv)) {
			query.setFilterQueries(" numProv:" + numProv);
		}
		if (StringUtils.isNotEmpty(startDate)) {
			query.setFilterQueries(" startDate:" + startDate);
		}
		if (StringUtils.isNotEmpty(endDate)) {
			query.setFilterQueries(" endDate:" + endDate);
		}
		QueryResponse rsp = solrServer.query(query);
		searchPage.setTotalRecords(new Long(rsp.getResults().getNumFound())
				.intValue());
		List<EccoreItem> searchResult = rsp.getBeans(EccoreItem.class);
		List<EccoreSearchResultDTO> result = new ArrayList<EccoreSearchResultDTO>();
		for (EccoreItem eccoreItem : searchResult) {
			EccoreSearchResultDTO eccoreSearchResultDTO = new EccoreSearchResultDTO();
			eccoreSearchResultDTO.setPhone(eccoreItem.getDisplayPhone());
			result.add(eccoreSearchResultDTO);
		}
		searchPage.setRecords(result);
	}

	@Override
	public void buildPartEccoreData(String solrUrl, MailObjectDTO mailObjectDTO) {

		if (mailObjectDTO == null || mailObjectDTO.getId() == 0) {
			return;
		}
		try {
			long t1 = System.currentTimeMillis();
			logger.error("buildPartEccoreData:开始连接solr server:"
					+ System.currentTimeMillis());
			CommonsHttpSolrServer server = getSolrServer(solrUrl);
			logger.error("buildPartEccoreData:成功连接solr server:"
					+ (System.currentTimeMillis() - t1));
			List<EccoreItem> mailNoNull = new ArrayList<EccoreItem>();
			List<EccoreItem> mailNoChange = new ArrayList<EccoreItem>();
			List<EccoreItem> statusChange = new ArrayList<EccoreItem>();

			List<String> delItem = new ArrayList<String>();

			String status = "0";
			String dateStr = "2011-11-19";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
			Date originalDate = simpleDateFormat.parse(dateStr);

			EccoreItem bean = new EccoreItem();
			bean.setId(mailObjectDTO.getId());
			status = mailObjectDTO.getStatus();
			if (StringUtils.equals(status, StatusEnum.CREATE.getValue()
					.toString())) {
				status = StatusEnum.CREATE.getName();
			}
			if (StatusEnum.isExitEnum(status) == false)
				return;

			bean.setNumStatus(StatusEnum.valueOf(status).getValue());

			if (StringUtils.isEmpty(mailObjectDTO.getMailNo())) {
				// 运单号为空 solr不允许搜索此数据
				bean.setMailNo("YTO" + mailObjectDTO.getId());
				bean.setIsDispaly(new Integer(1).shortValue());
			} else {
				// 支持
				bean.setMailNo(mailObjectDTO.getMailNo());
				bean.setIsDispaly(new Integer(0).shortValue());
			}

			bean.setNumCreateTime((int) getNumDay(mailObjectDTO
					.getPartitiondate()));
			bean.setCreateTime(mailObjectDTO.getCreateTime());
			bean.setAcceptTime(mailObjectDTO.getAcceptTime());
			bean.setCustomerId(mailObjectDTO.getCustomerId());
			bean.setName(mailObjectDTO.getName());

			bean.setPhone(new String[] { mailObjectDTO.getMobile(),
					mailObjectDTO.getPhone() });

			bean.setDisplayPhone(mailObjectDTO.getMobile());
			if (StringUtils.isEmpty(bean.getDisplayPhone())) {
				bean.setDisplayPhone(mailObjectDTO.getPhone());
			}
			bean.setNumProv(Integer.valueOf(mailObjectDTO.getNumProv()));

			bean.setNumCity(Resource.getCodeByName(mailObjectDTO.getCity()));

			bean.setNumDistrict(Resource.getCodeByName(mailObjectDTO
					.getDistrict()));
			bean.setAddress(mailObjectDTO.getAddress());
			bean.setWeight(mailObjectDTO.getWeight());
			/**** Added by Johnson,2013-08-06 *****/
			if (mailObjectDTO.getOrderType() == null) {
				bean.setOrderType(new Integer(0).shortValue());
			} else {
				bean.setOrderType(new Integer(mailObjectDTO.getOrderType())
						.shortValue());
			}
			if (mailObjectDTO.getLineType() == null) { // 如果line_type为null,默认设置为线下订单:1
				bean.setLineType(new Integer(1).shortValue());
			} else {
				bean.setLineType(mailObjectDTO.getLineType().shortValue());
			}
			/**************************************/
			bean.setTrimFreight(mailObjectDTO.getTrimFreight());
			if (mailObjectDTO.getFreightType() != null) {
				bean.setFreightType(mailObjectDTO.getFreightType().shortValue());
			} else {
				bean.setFreightType(new Integer(0).shortValue());
			}

			bean.setCityF(mailObjectDTO.getCityF());
			bean.setNumProvF(Resource.getCodeByName(mailObjectDTO.getProvF()));

			if (!StringUtils.isEmpty(mailObjectDTO.getHoldTime())) {
				bean.setHoldTime((int) getNumDay(holdTimeFormat
						.parse(mailObjectDTO.getHoldTime())));
			}

			bean.setMailNoAndCustomerId(bean.getMailNo()
					+ mailObjectDTO.getCustomerId());

			bean.setTxLogisticId(mailObjectDTO.getTxLogisticId());

			if (StringUtils.equals(mailObjectDTO.getBuildTask(), "1")) {
				// add mailNo
				mailNoNull.add(bean);
				delItem.add(bean.getMailNo() + mailObjectDTO.getCustomerId());
			} else if (StringUtils.equals(mailObjectDTO.getBuildTask(), "2")) {
				// 更新mailNo
				mailNoChange.add(bean);
				delItem.add(mailObjectDTO.getOldMailNo()
						+ mailObjectDTO.getCustomerId());
			} else if (StringUtils.equals(mailObjectDTO.getBuildTask(), "3")) {
				// 删除订单
				delItem.add(bean.getMailNo() + mailObjectDTO.getCustomerId());
			} else {
				// 新增订单
				statusChange.add(bean);
			}

			logger.error("buildPartEccoreData:for 循环完毕:"
					+ System.currentTimeMillis() + "," + mailObjectDTO.getId());
			if (delItem.size() > 0) {
				server.deleteById(delItem);
				server.commit();
				logger.error("buildPartEccoreData:delItem:" + delItem.size());
				delItem.clear();
			}
			logger.error("buildPartEccoreData:delItem.commit:"
					+ System.currentTimeMillis());
			if (mailNoNull.size() > 0) {
				server.addBeans(mailNoNull);
				server.commit();
				logger.error("buildPartEccoreData:mailNonULL:"
						+ mailNoNull.size());
				mailNoNull.clear();

			}
			logger.error("buildPartEccoreData:mailNoNull.commit:"
					+ System.currentTimeMillis());
			if (statusChange.size() > 0) {
				server.addBeans(statusChange);
				server.commit();
				logger.error("buildPartEccoreData:statusChange:"
						+ statusChange.size());
				statusChange.clear();
			}
			logger.error("buildPartEccoreData:statusChange.commit:"
					+ System.currentTimeMillis());
			if (mailNoChange.size() > 0) {
				server.addBeans(mailNoChange);
				server.commit();
				logger.error("buildPartEccoreData:mailNoChange:"
						+ mailNoChange.size());
				mailNoChange.clear();
			}

		} catch (Exception e) {
			logger.error("build error", e);
		}
	}

	@Override
	public List<BuildSearch> getBuildForAddData(int limit) {
		return buildSearchDao.getBuildSearchForAddByLimit(limit);
	}

	@Override
	public void writeOrderChangeInfo2SolrFromMq(List<String> msgs)
			throws Exception {
		// TODO 解析更新信息写入SOLR

		logger.error("into writeOrderChangeInfo2SolrFromMq: " + msgs.size());
		//long t0 = System.currentTimeMillis();
		String solrUrl = ConfigUtilSingle.getInstance().getSolrEccoreUrl();
		SolrServer solrServer;
		try {

			// --将报文解析成SOLR对象列表  
			logger.error("EccoreSearchService writeOrderChangeInfo2SolrFromMq 正在写入，本次处理条数："+msgs.size());
			//--将报文解析成SOLR对象列表
			List<EccoreItem> listSolr = new ArrayList<EccoreItem>();
			long s = System.currentTimeMillis();
			for(String jsonStr:msgs){
				
				//JSONObject jsonObject = JSONObject.fromObject(jsonStr);
				//System.out.println(jsonStr);
				BuildSearchStatusWeightIndex build = JSON.parseObject(jsonStr,BuildSearchStatusWeightIndex.class);
				EccoreItem bean = new EccoreItem();
				if(bean2EccoreItem(build,bean)){
					listSolr.add(bean);
				}
			}
			long e = System.currentTimeMillis();
			logger.error(" 解析"+msgs.size()+" 条消息耗时："+(e-s)+" ms");
			//--
			solrServer = getSolrServer(solrUrl);
			//--
			solrServer.addBeans(listSolr);
			//UpdateResponse res = solrServer.commit(false, false);
			//UpdateResponse res = solrServer.commit();
			solrServer.commit();
			long e1 = System.currentTimeMillis();
			logger.error("EccoreSearchService writeOrderChangeInfo2SolrFromMq 写入成功，本次处理条数："+msgs.size()+" 耗时："+(e1-e)+" ms");
		} catch (Exception e) {
			// TODO 记录错误日志
			logger.error(
					"EccoreSearchService writeOrderChangeInfo2SolrFromMq Error",
					e);
			throw e;
		}
	}

	/**
	 * 
	 * */
	private boolean bean2EccoreItem(BuildSearchStatusWeightIndex buildSearch,
			EccoreItem bean) throws Exception {
		try
		{
			String status = "0";
			bean.setId(buildSearch.getOrderId());
			if (buildSearch.getStatus() != null)
				status = buildSearch.getStatus();
			if (StringUtils.equals(status, StatusEnum.CREATE.getValue().toString())) {
				status = StatusEnum.CREATE.getName();
			}
			if (StatusEnum.isExitEnum(status) == false)
				return false;
			bean.setNumStatus(StatusEnum.valueOf(status).getValue());
			if (StringUtils.isEmpty(buildSearch.getMailNo())) {
				// 运单号为空 solr不允许搜索此数据
				bean.setMailNo("YTO" + buildSearch.getId());
				bean.setIsDispaly(new Integer(1).shortValue());
			} else {
				// 支持
				bean.setMailNo(buildSearch.getMailNo());
				bean.setIsDispaly(new Integer(0).shortValue());
			}
			bean.setNumCreateTime((int) getNumDay(buildSearch.getPartitiondate()));
			bean.setCreateTime(buildSearch.getCreateTime());
			bean.setAcceptTime(buildSearch.getAcceptTime());
			bean.setCustomerId(buildSearch.getCustomerId());
			bean.setName(buildSearch.getName());

			bean.setPhone(new String[] { buildSearch.getMobile()==null?"":buildSearch.getMobile(),
					buildSearch.getPhone()==null?"":buildSearch.getPhone() });

			bean.setDisplayPhone(buildSearch.getMobile()==null?"":buildSearch.getMobile());
			if (StringUtils.isEmpty(bean.getDisplayPhone())) {
				bean.setDisplayPhone(buildSearch.getPhone());
			}
			bean.setNumProv(Integer.valueOf(buildSearch.getNumProv()));
			bean.setNumCity(Resource.getCodeByName(buildSearch.getCity()));
			bean.setNumDistrict(Resource.getCodeByName(buildSearch.getDistrict()));
			bean.setAddress(buildSearch.getAddress());
			bean.setWeight(buildSearch.getWeight());
			if (buildSearch.getOrderType() == 0) {
				bean.setOrderType(new Integer(0).shortValue());
			} else {
				bean.setOrderType(new Integer(buildSearch.getOrderType())
						.shortValue());
			}
			if (buildSearch.getLineType() == null) { // 如果line_type为null,默认设置为线下订单:1
				bean.setLineType(new Integer(1).shortValue());
			} else {
				bean.setLineType(new Integer(0).shortValue());
			}
			bean.setTrimFreight((float) buildSearch.getTrimFreight());
			if (buildSearch.getFreightType() != null) {
				bean.setFreightType(new Integer(1).shortValue());
			} else {
				bean.setFreightType(new Integer(0).shortValue());
			}
			bean.setCityF(buildSearch.getCityF());
			bean.setNumProvF(Resource.getCodeByName(buildSearch.getProvF()));
			try {
				/*if (!StringUtils.isEmpty(buildSearch.getHoldTime())) {
					String holdTime = buildSearch.getHoldTime().trim();
					if (holdTime.length() == 19 || holdTime.length() == 10) {
						holdTime = holdTime.substring(0, 10);
						Date dateTemp = holdTimeFormat.parse(holdTime);
						long dayTemp = getNumDay(dateTemp);
						bean.setHoldTime((int) dayTemp);
					} else {
						bean.setHoldTime((int) getNumDay(holdTimeFormat
								.parse(holdTimeFormat.format(new Date()))));
					}
				}*/
				if (!StringUtils.isBlank(buildSearch.getHoldTime())){
					Date dateTemp = holdTimeFormat.parse(buildSearch.getHoldTime());
					long dayTemp = getNumDay(dateTemp);
					bean.setHoldTime((int) dayTemp);
				}else {
					bean.setHoldTime((int) getNumDay(holdTimeFormat
							.parse(holdTimeFormat.format(new Date()))));
				}
				
			} catch (Exception e) {
				logger.error("bean2EccoreItem holdTimeFormat parse Error HoldTime:"+buildSearch.getHoldTime());
				//bean.setHoldTime(new Integer(9999));
				bean.setHoldTime((int) getNumDay(holdTimeFormat
						.parse(holdTimeFormat.format(new Date()))));
			}
			bean.setMailNoAndCustomerId(bean.getMailNo()
					+ buildSearch.getCustomerId());

			bean.setTxLogisticId(buildSearch.getTxLogisticId());
			return true;
		}catch(Exception e){
			logger.error(
					"EccoreSearchService bean2EccoreItem Error",
					e);
		}
		return false;
	}
}
