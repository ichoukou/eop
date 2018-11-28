package net.ytoec.kernel.search.service;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.BuildSearch;
import net.ytoec.kernel.dataobject.BuildSearchStatusWeightIndex;
import net.ytoec.kernel.search.dto.EccoreSearchResultDTO;
import net.ytoec.kernel.search.dto.MailObjectDTO;

public interface EccoreSearchService {

	/**
	 * 删除不存在用户7天前的数据
	 * 
	 * @param solrUrl
	 */
	public void deleteBefore7Days(String solrUrl);

	/**
	 * build 数据到solr
	 * 
	 * @param solrServer
	 * @param startId
	 *            开始同步的orderId
	 * @param length
	 *            要同步的数据量大小
	 * @throws Exception
	 */
	public void buildEccoreData(String solrServer, int startId, int length);

	/**
	 * @param solrUrl
	 * @param idTypeMap
	 * @param limit
	 */
	public void buildPartEccoreData(String solrUrl, int limit ,String buildFlag);
	/**
	 * @param solrUrl
	 * @param idTypeMap
	 * @param limit
	 */
	public void newBuildPartEccoreData(String solrUrl, List<BuildSearch> list,String buildFlag);

	/**
	 * map中的参数： startDate：2011-01-08 endDate: 2011-01-08 customerIDs:
	 * 10123123,123,123,123 mailNo: 1243576676565 numProv: 360000 numCity:
	 * 361100 numDistrict: 361122 phone: 13588747777 name: 张三 numStatus: 8
	 * orderType: 1,2,3 sortType: desc or asc 默认desc
	 */
	public void searchEccoreData(String solrUrl,
			Pagination<EccoreSearchResultDTO> searchPage) throws Exception;

	/**
	 * 搜索条件参数和方法 searchEccoreData 一致，返回的是按照status分组数目。如果没有则为0
	 * 
	 * @param searchPage
	 * @return
	 */
	public Map<Short, Integer> searchEccoreDataGroupStatus(String solrUrl,
			Pagination<EccoreSearchResultDTO> searchPage) throws Exception;

	/**
	 * 搜索条件参数和方法 searchEccoreData 一致，返回的是按照orderTyle数目。如果没有则为0
	 * 
	 * @param searchPage
	 * @return
	 */
	public Map<Short, Integer> searchEccoreDataGroupOrderType(String solrUrl,
			Pagination<EccoreSearchResultDTO> searchPage) throws Exception;

	public void buildEccoreDataOnce(String solrServer, int startId, int len);

	/**
	 * @param solrUrl
	 * @param idTypeMap
	 * @param limit
	 */
	public void delEccoreData(String solrUrl, Map<String, String> map)
			throws Exception;

	/**
	 * 智能查件搜索数据接口
	 * 
	 * @param solrUrl
	 * @param searchPage
	 * @throws Exception
	 */
	public void searchWayBillData(String solrUrl,
			Pagination<EccoreSearchResultDTO> searchPage) throws Exception;

	/**
	 * 更新状态和重量到solr
	 * 
	 * @param solrUrl
	 * @param idTypeMap
	 * @param limit
	 */
	public void buildPartStatusWeightData(String solrUrl, Integer limit);

	/**
	 * 通过订单id集合查询solr中订单列表
	 * 
	 * @param solrUrl
	 * @param searchPage
	 * @throws Exception
	 */
	public void getOrderEccoreSearchByOrderIds(String solrUrl,
			Pagination<EccoreSearchResultDTO> searchPage) throws Exception;

	/**
	 * 搜索条件参数和方法 searchEccoreData 一致，返回的是按照status分组数目。如果没有则为0
	 * 
	 * @param searchPage
	 * @return
	 */
	public Map<String, Integer> searchEccoreDataGroupCustomerId(String solrUrl,
			Pagination<EccoreSearchResultDTO> searchPage) throws Exception;

	/**
	 * 查找超时件
	 * 
	 * map中的参数： startDate：2011-01-08 endDate: 2011-01-08 customerIDs:
	 * 10123123,123,123,123 mailNo: 1243576676565 overTimeMap: 省的code，value为预警值
	 * ，如（330000(code),5(value);330001(code),6(value)); name: 张三 phone:
	 * 13588747777 numStatus: 8 orderType: 1,2,3 sortType: desc or asc 默认desc
	 * statusList: LIST<String>
	 * 
	 */
	public void searchOverTimeData(String solrUrl,
			Pagination<EccoreSearchResultDTO> searchPage) throws Exception;

	/**
	 * 根据手机、固话查询订单
	 * 
	 * @param map中的参数
	 *            ： mobile : 18801807218 telphone : 0913-3583456
	 */
	public void searchOrderByMobile(String solrUrl,
			Pagination<EccoreSearchResultDTO> searchPage) throws Exception;

	/**
	 * 根据mailNo、customerId联合主键和地区参数获取order
	 * 
	 * @param mailNoAndCustomerId
	 *            numProv: 360000 numCity: 361100 numDistrict: 361122
	 */
	public void searchOrderByMailNoAndCustomerId(String solrUrl,
			Pagination<EccoreSearchResultDTO> searchPage) throws Exception;

	/**
	 * 时间作为查询条件，运单号、customerId、区域作为过滤条件<br>
	 * 注意：customerId里面是以","相隔的多个customerId的字符串
	 * 
	 * @param solrUrl
	 * @param searchPage
	 * @throws Exception
	 */
	public void searchOrderByTimeMailNoCustomerId(String solrUrl,
			Pagination<EccoreSearchResultDTO> searchPage) throws Exception;

	/**
	 * 根据省份id和日期查询卖家订单
	 * 
	 * @param solrUrl
	 * @param searchPage
	 * @throws Exception
	 */
	void searchOrderByProvIdAndDate(String solrUrl,
			Pagination<EccoreSearchResultDTO> searchPage) throws Exception;

	public List<BuildSearch> getBuildData(int limit);
	
	public List<BuildSearch> getBuildForAddData(int limit);

	/**
	 * 单条写入对象
	 * 
	 * @param solrUrl
	 * @param idTypeMap
	 * @param limit
	 */
	public void buildPartEccoreData(String solrUrl, MailObjectDTO mailObjectDTO);
	
	public void buildUpdatePartEccoreData(String solrUrl,
			List<BuildSearchStatusWeightIndex> buildSearchs,String buildFlag) ;
	public List<BuildSearchStatusWeightIndex> getUpdateBuildData(int limit,String table);
	public List<BuildSearchStatusWeightIndex> getUpdateBuildData2(int limit,int type);
	
	/**
	 * 从MQ里面读取订单状态和重量批量更新到 solr
	 * @param msgs 更新列表
	 * */
	public void writeOrderChangeInfo2SolrFromMq(List<String> msgs) throws Exception;
}
