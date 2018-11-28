package net.ytoec.kernel.dao;

import net.ytoec.kernel.dataobject.CountSellerInfo;

import org.springframework.dao.DataAccessException;

/**
 * 统计卖家信息dao
 * 
 * @author huangtianfu
 * 
 */
public interface CountSellerInfoDao<T> {

	/**
	 * 根据电话号码查询统计卖家信息实体
	 * 
	 * @param phone
	 * @return
	 * @throws DataAccessException
	 */
	CountSellerInfo selectByPhone(String phone) throws DataAccessException;

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
	void insertCountSellerInfo(CountSellerInfo countSellerInfo);

}
