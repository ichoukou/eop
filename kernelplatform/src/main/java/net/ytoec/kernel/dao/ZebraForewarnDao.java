package net.ytoec.kernel.dao;

import java.util.List;

import net.ytoec.kernel.dataobject.ZebraForewarn;

import org.springframework.dao.DataAccessException;

/**
 * 预警设置dao
 * 
 * @author 11
 * 
 */
public interface ZebraForewarnDao<T> {
	/**
	 * @作者：罗典
	 * @时间：2013-09-26
	 * @描述：根据客户编码修改预警信息
	 * @参数：预警信息
	 * @返回：影响行数
	 * */
	public int updateZebraForewarn(ZebraForewarn zebraForewarn);
	
	/**
	 * @作者：罗典
	 * @时间：2013-09-26
	 * @描述：根据客户编码集合批量获取预警信息
	 * @参数：客户编码集合
	 * @返回：预警信息集合
	 * */
	public List<ZebraForewarn> queryZebraForewarn(List<String> customerList);

	void addZebraForewarn(T zebraForewarn) throws DataAccessException;

	void removeZebraForewarn(T zebraForewarn) throws DataAccessException;

	void editZebraForewarn(T zebraForewarn) throws DataAccessException;

	/**
	 * 根据商家编码查询电子面单预警信息
	 * 
	 * @param customerCode
	 * @return
	 */
	ZebraForewarn selectByCustomerCode(String customerCode);

	// 更新商家预警信息
	void updateForwarnByCustomerCode(ZebraForewarn zebraForewarn);

	/**
	 * 第一次给仓配通某商家下单面单后，预警状态设置为不是初始状态
	 * 
	 * @param customerCode
	 *            商家代码
	 */
	int updateInitStateByCustomerCode(String customerCode);

	/**
	 * 更新短信是否已经发送状态
	 * 
	 * @param customerCode
	 *            商家代码
	 * @param sendPhoneState
	 *            是否已经发送短信(0:否,1:是)
	 */
	int updateSendPhoneStateByCustomerCode(String customerCode,
			int sendPhoneState);

	/**
	 * 更新短信是否已经发送状态
	 * 
	 * @param customerCode
	 *            商家代码
	 * @param sendPhoneState
	 *            是否已经发送短信(0:否,1:是)
	 */
	int updateSendMailStateByCustomerCode(String customerCode, int sendMailState);

	void insertinsertForwarnByCustomerCode(ZebraForewarn zebraForewarn);

	/**
	 * 更新商家的预警状态
	 * 
	 * @param customerCode
	 *            商家代码
	 * @param warnState
	 *            是否预警状态(0:否,1:是)
	 */
	void updateWarnStateByCustomerCodeAndWarnState(String customerCode,
			int warnState);
 
	/**
	 * 更新预警历史值
	 * 
	 * @param params
	 */
	void updateCustomerWarnHistoryValue(String customerCode,
			int customerWarnHistoryValue);

}
