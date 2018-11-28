/**
 * 
 */
package net.ytoec.kernel.service.impl;

import javax.inject.Inject;

import net.ytoec.kernel.dao.ZebraForewarnDao;
import net.ytoec.kernel.dataobject.ZebraForewarn;
import net.ytoec.kernel.service.ZebraForewarnService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ZebraForewarnServiceImpl<T extends ZebraForewarn> implements
		ZebraForewarnService<T> {

	@Inject
	private ZebraForewarnDao<ZebraForewarn> zebraForewarnDao;

	@Override
	public ZebraForewarn selectByCustomerCode(String customerCode) {
		return zebraForewarnDao.selectByCustomerCode(customerCode);
	}

	@Override
	public void updateForwarnByCustomerCode(ZebraForewarn zebraForewarn) {
		zebraForewarnDao.updateForwarnByCustomerCode(zebraForewarn);

	}

	@Override
	public int updateInitStateByCustomerCode(String customerCode) {
		return zebraForewarnDao.updateInitStateByCustomerCode(customerCode);
	}

	@Override
	public void insertForwarnByCustomerCode(ZebraForewarn zebraForewarn) {
		zebraForewarnDao.insertinsertForwarnByCustomerCode(zebraForewarn);
	}

	@Override
	public void updateCustomerWarnHistoryValue(String customerCode,
			int customerWarnHistoryValue) {
		zebraForewarnDao.updateCustomerWarnHistoryValue(customerCode,
				customerWarnHistoryValue);
	}

}
