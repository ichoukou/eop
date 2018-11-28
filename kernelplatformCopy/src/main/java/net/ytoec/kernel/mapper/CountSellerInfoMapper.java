package net.ytoec.kernel.mapper;

import net.ytoec.kernel.dataobject.CountSellerInfo;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 统计卖家信息实体Mapper
 * 
 * @author huangtianfu
 * 
 */
public interface CountSellerInfoMapper<T extends CountSellerInfo> extends
		BaseSqlMapper<T> {

	/**
	 * 根据电话号码查询统计卖家信息实体
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
	void insertCountSellerInfo(CountSellerInfo countSellerInfo);
}
