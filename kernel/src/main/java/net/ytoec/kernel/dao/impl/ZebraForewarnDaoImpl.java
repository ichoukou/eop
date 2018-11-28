/**
 * 2012-4-17下午01:36:54
 * wangyong
 */
package net.ytoec.kernel.dao.impl;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dao.ZebraForewarnDao;
import net.ytoec.kernel.dataobject.ZebraForewarn;
import net.ytoec.kernel.mapper.ZebraForewarnMapper;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

/**
 * 预警设置dao实现类
 * 
 * @date 2013-5-20
 * @author huangtianfu
 */
@Repository
public class ZebraForewarnDaoImpl<T extends ZebraForewarn> implements
		ZebraForewarnDao<T> {

	@Inject
	private ZebraForewarnMapper<ZebraForewarn> mapper;

	@Override
	public void addZebraForewarn(T zebraForewarn) throws DataAccessException {
		mapper.add(zebraForewarn);
	}

	@Override
	public void removeZebraForewarn(T zebraForewarn) throws DataAccessException {

	}

	@Override
	public void editZebraForewarn(T zebraForewarn) throws DataAccessException {

	}

	@Override
	public ZebraForewarn selectByCustomerCode(String customerCode) {
		return mapper.selectByCustomerCode(customerCode);
	}

	@Override
	public void updateForwarnByCustomerCode(ZebraForewarn zebraForewarn) {
		mapper.updateForwarnByCustomerCode(zebraForewarn);

	}

	@Override
	public int updateInitStateByCustomerCode(String customerCode) {
		return mapper.updateInitStateByCustomerCode(customerCode);
	}

	@Override
	public int updateSendPhoneStateByCustomerCode(String customerCode,
			int sendPhoneState) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerCode", customerCode);
		params.put("sendPhoneState", sendPhoneState);
		return mapper.updateSendPhoneStateByCustomerCode(params);
	}

	@Override
	public int updateSendMailStateByCustomerCode(String customerCode,
			int sendMailState) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerCode", customerCode);
		params.put("sendMailState", sendMailState);
		return mapper.updateSendMailStateByCustomerCode(params);
	}

	@Override
	public void insertinsertForwarnByCustomerCode(ZebraForewarn zebraForewarn) {
		mapper.insertForwarnByCustomerCode(zebraForewarn);
	}

	@Override
	public void updateWarnStateByCustomerCodeAndWarnState(String customerCode,
			int warnState) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerCode", customerCode);
		params.put("warnState", warnState);
		mapper.updateWarnStateByCustomerCodeAndWarnState(params);
	}

	@Override
	public void updateCustomerWarnHistoryValue(String customerCode,
			int customerWarnHistoryValue) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customerCode", customerCode);
		params.put("customerWarnHistoryValue", customerWarnHistoryValue);
		mapper.updateCustomerWarnHistoryValue(params);
	}

}
