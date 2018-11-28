package net.ytoec.kernel.search.service;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;

public interface JgOrderCommandService<T> {

	/**
	 * 批量获取金刚同步遗漏订单
	 * @param n
	 * @return
	 */
    public List<T> getJGOrderByLimit(Integer n);
    /**
	 * 分页批量获取金刚同步遗漏订单
	 * @param n
	 * @return
	 */
    public List<T> getJGOrder(Map<String, String> map,Pagination pagination);
    /**
	 * 统计总数量
	 * @param n
	 * @return
	 */
    public int count(Map<String, String> map,Pagination pagination);

    /**
     * 根据ID批量删除订单
     * @param n
     * @return
     */
    
    public Boolean removeJGOrderById(Integer minId,Integer maxId);
    
    /**
     * 添加一条记录
     * @param bean
     * @return
     */
    public boolean addJGOrder(T bean);

}
