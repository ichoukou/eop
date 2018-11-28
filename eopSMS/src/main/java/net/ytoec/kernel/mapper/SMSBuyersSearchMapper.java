package net.ytoec.kernel.mapper;

import java.util.List;

import net.ytoec.kernel.dataobject.SMSBuyersSearch;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

public interface SMSBuyersSearchMapper<T extends SMSBuyersSearch> extends BaseSqlMapper<T> {

	/**
	 * 获取当前用户的搜索器
	 * @param userId    当前用户ID
	 * @return
	 */
	public List<T> getSMSBuyersSearchByUserId(Integer userId);
	
	/**
	 * 获取当前用户搜索器的个数
	 * @param userId
	 * @return
	 */
	public Integer getSearchCountByUserId(Integer userId);
	
	/**
	 * 添加搜索条件记录
	 * @param search
	 */
	public void addSmsBuyersSearch(T search);

	/**
	 * 根据ID删除搜索记录
	 * @param search
	 */
	public void delSmsBuyersSearch(Integer id);

	/**
	 * 根据ID修改搜索器
	 * @param search
	 */
	public void editSmsBuyersSearch(T search);

	/**
	 * 根据ID查询搜索器
	 * @param search
	 * @return 
	 */
	public T getSmsBuyersSearchById(Integer id);
	
}
