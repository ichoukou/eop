package net.ytoec.kernel.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dao.CountSellerInfoDao;
import net.ytoec.kernel.dataobject.CountSellerInfo;
import net.ytoec.kernel.search.dto.EccoreSearchResultDTO;
import net.ytoec.kernel.search.service.EccoreSearchService;
import net.ytoec.kernel.service.CountSellerInfoService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CountSellerInfoServiceImpl implements CountSellerInfoService {

	private static Logger logger = LoggerFactory.getLogger(CountSellerInfoServiceImpl.class);

	@Autowired
	private CountSellerInfoDao<CountSellerInfo> countSellerInfoDao;

	@Autowired
	private EccoreSearchService eccoreSearchService;

	@Value("${solr.eccore.url}")
	private String solrUrl;

	@Value("${solr.countSellerInfo.startTime}")
	private String startTime;

	@Value("${solr.countSellerInfo.endTime}")
	private String endTime;

	private String currentPagekey = "key_countSellerInfo";

	private static final int pageSize = 10000;

	@Override
	public CountSellerInfo selectByPhone(String phone) {
		return countSellerInfoDao.selectByPhone(phone);
	}

	@Transactional
	@Override
	public void updateCountSellerInfoByKey(CountSellerInfo countSellerInfo) {
		countSellerInfoDao.updateCountSellerInfoByKey(countSellerInfo);
	}

	@Transactional
	@Override
	public void addCountSellerInfo(CountSellerInfo countSellerInfo) {
		countSellerInfoDao.insertCountSellerInfo(countSellerInfo);
	}

	@Override
	public void countSellerInfoTimer() {
		try {
			logger.error("CountSellerInfoFromSolrTimer start...");

			Pagination<EccoreSearchResultDTO> searchPage = new Pagination<EccoreSearchResultDTO>(
					1, pageSize);
			Map<String, String> searchParams = new HashMap<String, String>();
			List<EccoreSearchResultDTO> records = new ArrayList<EccoreSearchResultDTO>();
			CountSellerInfo countSellerInfo = null;
			Date date = new Date();
			int currentPage = 1;
			String cachePage = Resource.get(currentPagekey);
			if (StringUtils.isNotEmpty(cachePage)) {
				currentPage = Integer.parseInt(cachePage);
			}
			while (true) {// 循环分页查询
				searchPage.setCurrentPage(currentPage);
				searchParams.put("startDate", startTime);
				searchParams.put("endDate", endTime);
				searchParams.put("numProv", "310000");// 上海：numProv=310000

				searchPage.setParams(searchParams);
				eccoreSearchService.searchEccoreData(solrUrl,
						searchPage);

				// 退出循环
				if (searchPage.getRecords().isEmpty()) {
					break;
				}

				// 入库
				records.addAll(searchPage.getRecords());
				for (EccoreSearchResultDTO dto : records) {
					if (StringUtils.isNotEmpty(dto.getPhone())) {
						countSellerInfo = countSellerInfoDao.selectByPhone(dto
								.getPhone());
						if (countSellerInfo != null
								&& StringUtils.isNotEmpty(countSellerInfo
										.getPhone())) {// 数据库存在号码，更新购买次数
							countSellerInfo.setBuyNum(countSellerInfo
									.getBuyNum() + 1);
							countSellerInfo.setUpdateTime(date);
							countSellerInfoDao
									.updateCountSellerInfoByKey(countSellerInfo);
						} else {// 数据库不存在电话号码，插入记录并设置购买次数为1
							countSellerInfo = new CountSellerInfo();
							countSellerInfo.setPhone(dto.getPhone());
							countSellerInfo.setBuyNum(1);
							countSellerInfo.setCreateTime(date);
							countSellerInfo.setUpdateTime(date);
							countSellerInfoDao
									.insertCountSellerInfo(countSellerInfo);
						}
					}
				}
				records.clear();
				// 保存分页号
				currentPage++;
				Resource.put(currentPagekey, currentPage + "");
			}
		} catch (Exception e) {
			logger.error("count error", e);
		}
	}

}
