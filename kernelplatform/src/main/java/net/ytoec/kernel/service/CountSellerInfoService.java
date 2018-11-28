package net.ytoec.kernel.service;

import net.ytoec.kernel.dataobject.CountSellerInfo;

/**
 * 统计卖家信息service
 * 
 * @author huangtianfu
 * 
 */
public interface CountSellerInfoService {

	/**
	 * 根据手机号码查询统计卖家信息实体
	 * 
	 * @param phone
	 *            电话号码
	 * @return
	 */
	CountSellerInfo selectByPhone(String phone);

	/**
	 * 根据主键更新统计卖家信息实体
	 * 
	 * @param countSellerInfo
	 */
	void updateCountSellerInfoByKey(CountSellerInfo countSellerInfo);

	/**
	 * 添加统计卖家信息实体
	 * 
	 * @param countSellerInfo
	 */
	void addCountSellerInfo(CountSellerInfo countSellerInfo);

	/**
	 * 统计卖家手机购买次数timer
	 */
	void countSellerInfoTimer();
}
