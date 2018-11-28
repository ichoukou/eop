package net.ytoec.kernel.service;

import java.util.List;

import net.ytoec.kernel.common.Pagination;

/**
 * 渠道信息的业务接口
 * 
 * @author ChenRen
 * @date 2011-7-19
 */
public interface ChannelService<T> {

	/**
	 * 新增一条渠道信息数据
	 * 
	 * @param entity
	 * @return @
	 */
	boolean addChannel(T entity);

	/**
	 * 根据clientId查询结果集. clientId应是唯一, 如果存在多个结果集,则按创建时间排序取最近的记录.
	 * 
	 * @param clientId
	 * @return
	 */
	T getChannelByClientId(String clientId);

	/**
	 * 根据Id得到一条数据
	 * 
	 * @param id
	 * @return @
	 */
	T getChannelById(Integer id);

	/**
	 * 得到所有渠道信息数据
	 * 
	 * @return @
	 */
	List<T> getAllChannel(Pagination pagination, boolean flag);

	/**
	 * 修改一个订单
	 * 
	 * @param entity
	 * @return @
	 */
	boolean editChannel(T entity);

	/**
	 * 删除一个订单
	 * 
	 * @param entity
	 * @return @
	 */
	boolean removeChannel(T entity);
	
}