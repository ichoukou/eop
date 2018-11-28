/**
 * BranchDao.java
 * 2012-02-12
 * wangyong
 */
package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.Branch;

/**
 * 
 * @author wangyong TODO
 */
public interface BranchDao<T> {

	@SuppressWarnings("rawtypes")
	public List<T> getAllSearchData(Map map);

	public int countAllSearchData();

	/**
	 * 当查询网点无结果的时候，新增网点
	 * 
	 * @param entity
	 */
	public boolean addBranch(T entity);

	/**
	 * 网点纠错
	 * 
	 * @param entity
	 * @return
	 */
	public boolean edit(T entity);

	public Branch get(Integer id);

	public List<T> findByCompanyName(T entity);

	public List<T> findByManagerPhone(T entity);

	public List<T> findByServicePhone(T entity);

	public List<T> findByQuestionPhone(T entity);
	
	public List<T> findByBranchCode(int branchCode);
	
	public Branch findByUserId(Integer userId);
}
