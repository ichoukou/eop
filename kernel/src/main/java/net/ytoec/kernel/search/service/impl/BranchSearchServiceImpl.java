/**
 * 
 */
package net.ytoec.kernel.search.service.impl;

import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dao.BranchDao;
import net.ytoec.kernel.dataobject.Branch;
import net.ytoec.kernel.search.dataobject.BranchItem;
import net.ytoec.kernel.search.service.BranchSearchService;
import net.ytoec.kernel.util.IKAnalyzerSingle;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wangyong
 * 
 */
@Service
@Transactional
@SuppressWarnings("all")
public class BranchSearchServiceImpl<T extends Branch> implements
		BranchSearchService<T> {

	@Inject
	private BranchDao<T> branchDao;

	protected final Logger log = LoggerFactory
			.getLogger(BranchSearchServiceImpl.class);

	public CommonsHttpSolrServer getSolrServer(String solrUrl)
			throws MalformedURLException {
		// 远程服务
		return new CommonsHttpSolrServer(solrUrl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.ytoec.kernel.search.service.BranchSearchService#commitBranchData()
	 */
	@Override
	public void commitBranchData(String solrServer)
			throws MalformedURLException {
		CommonsHttpSolrServer server = getSolrServer(solrServer);
		Map<String, Integer> map = new HashMap<String, Integer>();
		int startIndex = 0;
		int pageNum = 1000;
		map.put("startIndex", startIndex);
		map.put("pageNum", pageNum);
		List<Branch> branchList = (List<Branch>) branchDao
				.getAllSearchData(map);
		int i = 0;

		while (branchList != null && branchList.size() > 0) {
			try {
				List<BranchItem> beans = new ArrayList<BranchItem>();
				for (Branch branch : branchList) {
					BranchItem branchItem = new BranchItem();
					if (branch.getBranchCode() == null) {
						branch.setBranchCode(0);
					}
					BeanUtils.copyProperties(branchItem, branch);
					// branchItem.setProvice(branch.getProvice()
					// + branch.getCity() + branch.getCounty()
					// + branch.getCompanyName());
					beans.add(branchItem);
				}
				if (server != null) {

					server.addBeans(beans);
					server.commit();
				}
			} catch (Exception e) {
				log.error("startIndex:" + map.get("startIndex") + " pageNum"
						+ map.get("pageNum"));
				log.error("定时查询网点信息出错：数据执行到" + map.get("pageNum") * i + "行出错",
						e);
			}
			i++;
			map.put("startIndex", map.get("pageNum") * i);
			branchList = (List<Branch>) branchDao.getAllSearchData(map);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.ytoec.kernel.search.service.BranchSearchService#searchBranchData(
	 * net.ytoec.kernel.common.Pagination)
	 */
	@Override
	public Pagination searchBranchData(Pagination pageParams,
			String solrServerUrl) {
		SolrServer solrServer;
		try {
			solrServer = getSolrServer(solrServerUrl);
			SolrQuery query = new SolrQuery();

			// 启动高亮设置
			query.setHighlight(true)
					.setHighlightSimplePre("<font color=\'red\'>")// 设置开头
					.setHighlightSimplePost("</font>"); // 设置结尾
			// 提取关键字
			String searchKey = (String) pageParams.getParams().get("searchKey");

			// 设置高亮的哪些区域
			query.setParam(
					"hl.fl",
					"provice,city,companyName,managerName,managerPhone,servicePhone,questionPhone,sendScope,unSendScope,sendTimeLimit");

			if (StringUtils.isEmpty(searchKey)) {
				pageParams.setRecords(null);
				pageParams.setTotalRecords(0);
				return pageParams;
			}
			TokenStream tokenStream = IKAnalyzerSingle.getInstance()
					.getIkAnalyzer()
					.tokenStream("provice", new StringReader(searchKey));
			Token token = null;
			StringBuffer stringBuffer = new StringBuffer();
			while ((token = tokenStream.next()) != null) {
				stringBuffer.append(token.term() + " ");

			}

			query.setQuery("all:" + stringBuffer.toString());
			// System.out.println("============================== "+stringBuffer.toString());
			query.addSortField("score", SolrQuery.ORDER.desc);
			// query.addSortField("createTime", SolrQuery.ORDER.desc);
			query.setStart(pageParams.getStartIndex());
			query.setRows(pageParams.getPageNum());
			QueryResponse rsp = solrServer.query(query);
			// 定义map接收各个字段中关键字高亮后的数据文本
			Map<String, Map<String, List<String>>> map = rsp.getHighlighting();
			// 得到查询结果中的数据集
			List<BranchItem> branchItemList = rsp.getBeans(BranchItem.class);
			List<Branch> branchList = null;
			if (branchItemList != null) {
				branchList = new ArrayList<Branch>();
				for (BranchItem bi : branchItemList) {
					Branch branch = new Branch();
					// System.out.println(bi.getId());
					if (map.get(bi.getId().toString()) != null) {
						if (map.get(bi.getId().toString()).get("provice") != null) {
							List<String> ls = map.get(bi.getId().toString())
									.get("provice");
							String strProvice = ls.get(0);
							//如果当前字段数据中包含查询关键，把该字段中的关键字内容替换成带有高亮显示的数据
							if (bi.getProvice().contains(searchKey)) {
								bi.setProvice(strProvice);
								// System.out.println(bi.getProvice());
							}
						}
						if (map.get(bi.getId().toString()).get("city") != null) {
							List<String> ls = map.get(bi.getId().toString())
									.get("city");
							String strCity = ls.get(0);
							if (bi.getCity().contains(searchKey)) {
								bi.setCity(strCity);
							}
						}
						if (map.get(bi.getId().toString()).get("companyName") != null) {
							List<String> ls = map.get(bi.getId().toString())
									.get("companyName");
							String strCompanyName = ls.get(0);
							if (bi.getCompanyName().contains(searchKey)) {
								bi.setCompanyName(strCompanyName);
							}
						}
						if (map.get(bi.getId().toString()).get("managerName") != null) {
							List<String> ls = map.get(bi.getId().toString())
									.get("managerName");
							String strManagerName = ls.get(0);
							if (bi.getManagerName().contains(searchKey)) {
								bi.setManagerName(strManagerName);
							}
						}
						if (map.get(bi.getId().toString()).get("managerPhone") != null) {
							List<String> ls = map.get(bi.getId().toString())
									.get("managerPhone");
							String strManagerPhone = ls.get(0);
							if (bi.getManagerPhone().contains(searchKey)) {
								bi.setManagerPhone(strManagerPhone);
							}
						}
						if (map.get(bi.getId().toString()).get("servicePhone") != null) {
							List<String> ls = map.get(bi.getId().toString())
									.get("servicePhone");
							String strServicePhone = ls.get(0);
							if (bi.getServicePhone().contains(searchKey)) {
								bi.setServicePhone(strServicePhone);
							}
						}
						if (map.get(bi.getId().toString()).get("questionPhone") != null) {
							List<String> ls = map.get(bi.getId().toString())
									.get("questionPhone");
							String strQuestionPhone = ls.get(0);
							if (bi.getQuestionPhone().contains(searchKey)) {
								bi.setQuestionPhone(strQuestionPhone);
							}
						}
						if (map.get(bi.getId().toString()).get("sendScope") != null) {
							List<String> ls = map.get(bi.getId().toString())
									.get("sendScope");
							String strSendScope = ls.get(0);
							if (bi.getSendScope().contains(searchKey)) {
								bi.setSendScope(strSendScope);
							}
						}
						if (map.get(bi.getId().toString()).get("unSendScope") != null) {
							List<String> ls = map.get(bi.getId().toString())
									.get("unSendScope");
							String strUnSendScope = ls.get(0);
							if (bi.getUnSendScope().contains(searchKey)) {
								bi.setUnSendScope(strUnSendScope);
							}
						}
						if (map.get(bi.getId().toString()).get("sendTimeLimit") != null) {
							List<String> ls = map.get(bi.getId().toString())
									.get("sendTimeLimit");
							String strSendTimeLimit = ls.get(0);
							if (bi.getSendTimeLimit().contains(searchKey)) {
								bi.setSendTimeLimit(strSendTimeLimit);
							}
						}
						// System.out.println(bi.getProvice());
						// System.out.println(map.get(bi.getId().toString()).get("provice"));
						// System.out.println(map.get(bi.getId().toString()).get("city"));
						// System.out.println(map.get(bi.getId().toString()).get("companyName"));
						// System.out.println(map.get(bi.getId().toString()).get("managerName"));
						// System.out.println(map.get(bi.getId().toString()).get("managerPhone"));
						// System.out.println(map.get(bi.getId().toString()).get("servicePhone"));
						// System.out.println(map.get(bi.getId().toString()).get("questionPhone"));
						// System.out.println(map.get(bi.getId().toString()).get("sendScope"));
						// System.out.println(map.get(bi.getId().toString()).get("unSendScope"));
						// System.out.println(map.get(bi.getId().toString()).get("sendTimeLimit"));
					}
					BeanUtils.copyProperties(branch, bi);
					branchList.add(branch);
				}
			}

			pageParams.setRecords(branchList);
			// pageParams.setRecords(branchItemList);
			pageParams.setTotalRecords(Integer.parseInt(String.valueOf(rsp
					.getResults().getNumFound())));
			return pageParams;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("search error", e);
			pageParams.setRecords(null);
			pageParams.setTotalRecords(0);
			return pageParams;
		}

	}

	@Override
	public boolean addBranch(T entity) {
		// TODO Auto-generated method stub
		boolean flag = false;
		if (entity == null) {
			log.error("entity is empty");
		} else {
			branchDao.addBranch(entity);
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean edit(T entity) {
		// TODO Auto-generated method stub
		if (entity == null) {
			log.error(Branch.class.getName() + "对象参数信息为空！");
			return false;
		}
		return branchDao.edit(entity);
	}

	@Override
	public Branch get(Integer id) {
		// TODO Auto-generated method stub
		if (id == 0 || ("").equals(id.toString())) {
			log.error(Branch.class.getName() + "查询网点信息异常！");
		}
		return branchDao.get(id);
	}

	@Override
	public boolean findByCompanyName(T entity) {
		// TODO Auto-generated method stub
		if (entity == null) {
			log.error("entity is empty!");
			return false;
		}
		// 在Dao里查询数据集合，如果集合大于0返回false
		List<T> list = branchDao.findByCompanyName(entity);
		return list.size() > 0 ? false : true;
	}

	@Override
	public boolean findByManagerPhone(T entity) {
		// TODO Auto-generated method stub
		if (entity == null) {
			log.error("entity is empty!");
			return false;
		}
		// 在Dao里查询数据集合，如果集合大于0返回false
		List<T> list = branchDao.findByManagerPhone(entity);
		return list.size() > 0 ? false : true;
	}

	@Override
	public boolean findByServicePhone(T entity) {
		// TODO Auto-generated method stub
		if (entity == null) {
			log.error("entity is empty!");
			return false;
		}
		// 在Dao里查询数据集合，如果集合大于0返回false
		List<T> list = branchDao.findByServicePhone(entity);
		return list.size() > 0 ? false : true;
	}

	@Override
	public boolean findByQuestionPhone(T entity) {
		// TODO Auto-generated method stub
		if (entity == null) {
			log.error("entity is empty!");
			return false;
		}
		// 在Dao里查询数据集合，如果集合大于0返回false
		List<T> list = branchDao.findByQuestionPhone(entity);
		return list.size() > 0 ? false : true;
	}

	@Override
	public boolean findByBranchCode(int branchCode) {
		// TODO Auto-generated method stub
		if (String.valueOf(branchCode) == null) {
			log.error("branchCode is empty!");
			return false;
		}
		List<T> list = branchDao.findByBranchCode(branchCode);
		return list.size() > 0 ? true : false;
	}
	
	@Override
	public Branch findBranchByBranchCode(int branchCode) {
		// TODO Auto-generated method stub
		if (String.valueOf(branchCode) == null) {
			log.error("branchCode is empty!");
			return null;
		}
		List<T> list = branchDao.findByBranchCode(branchCode);
		return list.size() > 0 ? list.get(0) : null;
	}

	@Override
	public Branch findByUserId(Integer userId) {
		// TODO Auto-generated method stub
		if (userId == 0 || ("").equals(userId.toString())) {
			log.error(Branch.class.getName() + "查询网点信息异常！");
		}
		List<T> list = branchDao.findByBranchCode(userId);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

}
