package net.ytoec.kernel.search.service;

import java.io.IOException;
import java.net.MalformedURLException;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.Branch;

import org.apache.solr.client.solrj.SolrServerException;

/**
 * @author wangyong
 * @2012-02-14
 */
public interface BranchSearchService<T> {

	/**
	 * 每天定时查询数据
	 * 
	 * @throws MalformedURLException
	 */
	public void commitBranchData(String solrServer)
			throws MalformedURLException;

	/**
	 * 查询网点信息
	 * 
	 * @param pageParams
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public Pagination searchBranchData(Pagination pageParams, String solrServer)
			throws MalformedURLException, SolrServerException, IOException;

	/**
	 * 当查询网点无结果的时候，新增网点
	 * 
	 * @param entity
	 */
	public boolean addBranch(T entity);
	
	/**
	 * 网点纠错
	 * @param entity
	 * @return
	 */
	public boolean edit(T entity);
	
	public Branch get(Integer id);
	
	public boolean findByCompanyName(T entity);

	public boolean findByManagerPhone(T entity);

	public boolean findByServicePhone(T entity);

	public boolean findByQuestionPhone(T entity);
	
	public boolean findByBranchCode(int branchCode);
	
	public Branch findByUserId(Integer userId);
	
	public Branch findBranchByBranchCode(int branchCode);
}
