/**
 * AttentionMailMapper.java
 * 2011 2011-12-13 下午02:48:55
 * wangyong
 */
package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.AttentionMail;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;


/**
 * 关注中运单对象映射接口
 * @author wangyong
 * TODO
 */
public interface AttentionMailMapper<T extends AttentionMail> extends BaseSqlMapper<T> {

    public List<T> searchByMailNo(String mailNo);
    
    public List<T> searchByCustomerId(String customerId);
    
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
    public int searchWithCustomerIdMailnoTime(Map map);
    
    /**
     * 分页查找
     * @param map
     * @return
     */
    public List<T> searchPaginationList(Map map);
    
    public int countPaginationList(Map map);

}
