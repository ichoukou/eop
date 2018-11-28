/**
 * BranchMapper.java
 * 2011 2012-02-14
 * wangyong
 */
package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.Branch;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;


/**
 * @author wangyong
 * TODO
 */
public interface BranchMapper<T extends Branch> extends BaseSqlMapper<T> {

    @SuppressWarnings("rawtypes")
	public List<T> getAllSearchData(Map map);
    
    public int countAllSearchData();
    
    /**
     * 当查询网点无结果的时候，新增网点
     * @param entity
     */
    public void addBranch(T entity);
    
    /**
     * 网点纠错
     * @param entity
     */
    public void edit(T entity);
    
    public Branch get(Integer id);
    
    public List<T> findByCompanyName(T entity);
    
    public List<T> findByManagerPhone(T entity);
    
    public List<T> findByServicePhone(T entity);
    
    public List<T> findByQuestionPhone(T entity);
    
    public List<T> findByBranchCode(int branchCode);
    
    public Branch findByUserId(Integer id);
}
