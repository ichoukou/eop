/**
 * AttentionMailDao.java
 * 2011 2011-12-13 下午05:40:58
 * wangyong
 */
package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;


/**
 * 
 * @author wangyong
 * TODO
 */
public interface AttentionMailDao<T> {
    
    public boolean add(T entity);
    
    public boolean remove(Integer id);
    
    public boolean edit(T entity);
    
    public T get(Integer id);
    
    public List<T> searchByMailNo(String mailNo);
    
    public List<T> searchByCustomerId(String customerId);
    
    /**
     * 分页查找
     * @param map
     * @return
     */
    public List<T> searchPaginationList(Map map);
    
    public int countPaginationList(Map map);
    
    /**
     * 通过运单号和customerId查询，map中存放mailNo和customerId
     * @param map
     * @return
     */
    public List<T> searchByMailNoAndCustomerId(Map map);
    
    /**
     * 通过运单号或customerId集合或者时间查询
     * @param map
     * @return
     */
    public int searchByMailNoAndCustomerIdsAndTime(Map map);

}
