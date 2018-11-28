package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.ZebraPartern;

/**
 * 密钥管理service
 * 
 * @data 2013-2-20
 * @author huangtianfu
 */
public interface ZebraParternService<T> {

	void editByCustomerCode(ZebraPartern zebraPartern);

	void insertSelective(ZebraPartern zebraPartern);

	void zebraParternTimer();

	void deleteByCustomerCode(ZebraPartern zebraPartern);

	// 更新密钥
	void updateParternByCustomerCode(ZebraPartern zebraPartern);

	Pagination<ZebraPartern> findPageList(Pagination<ZebraPartern> pagination,
			Map<String, Object> params);

	/**
	 * 根据商户代码查询密钥
	 * 
	 * @param customerCode
	 * @return
	 */
	String selectParternCodeByCustomerCode(String customerCode);

	/**
	 * 定时上传密钥信息
	 */
	void parternTimer();
}