package net.ytoec.kernel.dao.impl;

import javax.inject.Inject;

import net.ytoec.kernel.dao.CountSellerInfoDao;
import net.ytoec.kernel.dataobject.CountSellerInfo;
import net.ytoec.kernel.mapper.CountSellerInfoMapper;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CountSellerInfoDaoImpl<T extends CountSellerInfo> implements
		CountSellerInfoDao<T> {

	@Inject
	private CountSellerInfoMapper<CountSellerInfo> countSellerInfoMapper;

	@Override
	public CountSellerInfo selectByPhone(String phone)
			throws DataAccessException {
		return countSellerInfoMapper.selectByPhone(phone);
	}

	@Transactional
	@Override
	public void updateCountSellerInfoByKey(CountSellerInfo countSellerInfo) {
		countSellerInfoMapper.updateCountSellerInfoByKey(countSellerInfo);
	}

	@Transactional
	@Override
	public void insertCountSellerInfo(CountSellerInfo countSellerInfo) {
		countSellerInfoMapper.insertCountSellerInfo(countSellerInfo);
	}

}
