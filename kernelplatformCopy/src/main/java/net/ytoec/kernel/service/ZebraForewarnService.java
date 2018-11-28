package net.ytoec.kernel.service;

import net.ytoec.kernel.dataobject.ZebraForewarn;

/**
 * 预警设置管理service
 * 
 * @data 2013-2-20
 * @author huangtianfu
 */
public interface ZebraForewarnService<T> {

	ZebraForewarn selectByCustomerCode(String customerCode);

	void updateForwarnByCustomerCode(ZebraForewarn customerCode);

	/**
	 * 第一次给仓配通某商家下单面单后，预警状态设置为不是初始状态
	 * 
	 * @param customerCode
	 *            商家代码
	 */
	int updateInitStateByCustomerCode(String customerCode);

	void insertForwarnByCustomerCode(ZebraForewarn zebraForewarn);

	/**
	 * 更新预警历史值
	 * 
	 * @param customerCode
	 * @param customerWarnHistoryValue
	 */
	void updateCustomerWarnHistoryValue(String customerCode,
			int customerWarnHistoryValue);
}