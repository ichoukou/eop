/**
 * AttentionMailService.java
 * 2011 2011-12-13 下午05:52:38
 * wangyong
 */
package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;


/**
 * 
 * @author wangyong
 * TODO
 */
public interface AttentionMailService<T> {

    public boolean add(String mailNo, String destination, String buyerName, String buyerPhone, String acceptTime, String status, String sendTime, String customerId);
    
    public boolean add(T entity);
    
    public boolean remove(Integer id);
    
    public boolean edit(T entity);
    
    public T get(Integer id);
    
    /**
     * 获取是否关注，因为系统只保留5天内用户关注的运单，所以只查询条件时前提5天内的运单
     * @param mailNo
     * @return
     */
    public List<T> searchByMailNo(String mailNo);
    
    public List<T> searchByCustomerId(String customerId);
    
    /**
     * 分页查找
     * @param bindedId TODO
     * @param map
     * @return
     */
    public List<T> searchPaginationList(String startTime, String endTime, String mailNo, String buyerName, String buyerPhone, Integer orderBy, Pagination pagination, List bindedId);
    
    public int countPaginationList(String startTime, String endTime, String mailNo, String buyerName, String buyerPhone, List bindedId);
    
    /**
     * 通过运单号和customerId查询
     * @param mailNo
     * @param customerId
     * @return
     */
    public List<T> searchByMailNoAndCustomerId(String mailNo, String customerId);
    
    /**
     * 通过运单号、customerId集合及时间查询
     * @param mailNo
     * @param customerId
     * @param startTime
     * @param endTime
     * @return
     */
    public List<T> searchByMailNoAndCustomerIdsAndTime(String mailNo, List<String> customerId, String startTime, String endTime);
}
