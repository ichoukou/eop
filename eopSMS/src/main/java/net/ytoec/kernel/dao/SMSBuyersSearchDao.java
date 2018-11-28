package net.ytoec.kernel.dao;

import java.util.List;

/**
 * 搜索器dao层
 * @author
 *
 */
public interface SMSBuyersSearchDao<T> {

	/**
	 * 获取当前用户的搜索器
	 * @param userId    当前用户ID
	 * @return
	 */
	public List<T> getSMSBuyersSearchByUserId(Integer userId);
	
	/**
	 * search中id为空时保存搜索器否则编辑,返回当前用户的搜索器列表
	 * @param search
	 * @return
	 */
	public List<T> saveSMSBuyersSearch(T search, Integer userId);
	
	/**
	 * 添加搜索器
	 * @param search
	 * @return
	 */
	public boolean addSmsBuyersSearch(T search);

	/**
	 * 删除搜索器
	 * @param id
	 * @return
	 */
	public boolean delSmsBuyersSearch(Integer id);

	/**
	 * 更新搜索器
	 * @param search
	 * @return
	 */
	public boolean updateSmsBuyersSearch(T search);

	/**
	 * 根据ID获取搜索器
	 * @param id
	 * @return
	 */
	public T getSmsBuyersSearchById(Integer id);

}
