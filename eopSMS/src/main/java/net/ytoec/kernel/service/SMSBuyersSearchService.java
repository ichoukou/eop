package net.ytoec.kernel.service;

import java.util.List;

import net.ytoec.kernel.dataobject.SMSBuyersSearch;

/**
 *  搜索器service
 */
public interface SMSBuyersSearchService {
	
	/**
	 * 获取当前用户的搜索器
	 * @param userId    当前用户ID
	 * @return
	 */
	public List<SMSBuyersSearch> getSMSBuyersSearchByUserId(Integer userId);
	
	/**
	 * search中id为空时保存搜索器否则编辑,返回当前用户的搜索器列表
	 * @param search
	 * @return
	 */
	public List<SMSBuyersSearch> saveSMSBuyersSearch(SMSBuyersSearch search);
	
	/**
	 * 保存搜索条件
	 * @param search
	 * @return
	 */
	public boolean addSmsBuyersSearch(SMSBuyersSearch search);
	
	/**
	 * 根据ID删除搜索条件
	 * @param search
	 * @return
	 */
	public boolean delSmsBuyersSearch(Integer id);
	
	/**
	 * 根据ID修改搜索器
	 * @param search
	 * @return
	 */
	public boolean editSmsBuyersSearch(SMSBuyersSearch search);
	
	/**
	 * 根据ID查询一个搜索器
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public SMSBuyersSearch getSmsBuyersSearchById(Integer searchId) throws Exception;
}
