/**
 * 2012-4-17下午01:36:54
 * wangyong
 */
package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dao.ZebraParternDao;
import net.ytoec.kernel.dataobject.ZebraPartern;
import net.ytoec.kernel.mapper.ZebraParternMapper;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

/**
 * 预警设置dao实现类
 * 
 * @date 2013-5-20
 * @author huangtianfu
 */
@Repository
public class ZebraParternDaoImpl<T extends ZebraPartern> implements
		ZebraParternDao<T> {

	private Logger logger = Logger.getLogger(ZebraParternDaoImpl.class);

	@Inject
	private ZebraParternMapper<T> zebraParternMapper;

	@Override
	public List<T> selectPageList(Map<String, Object> params) {
		return zebraParternMapper.selectPageList(params);
	}

	@Override
	public Integer selectTotal(Map<String, Object> map) {
		return zebraParternMapper.selectTotal(map);
	}

	@Override
	public void insertSelective(ZebraPartern zebraPartern) {
		ZebraPartern zp = new ZebraPartern();
		zp.setCustomerCode(zebraPartern.getCustomerCode());
		zp.setParternCode(zebraPartern.getParternCode());
		zp.setUpdateTime(zebraPartern.getUpdateTime());
		zebraParternMapper.insert(zp);

	}

	@Override
	public void updateByCustomerCode(ZebraPartern zebraPartern) {
		ZebraPartern zp = new ZebraPartern();
		zp.setCustomerCode(zebraPartern.getCustomerCode());
		zp.setParternCode(zebraPartern.getParternCode());
		zp.setUpdateTime(zebraPartern.getUpdateTime());
		System.out.println(zp.getCustomerCode() + zp.getParternCode()
				+ zp.getUpdateTime() + "-----------------------------------");
		zebraParternMapper.updateByCustomerCode(zebraPartern);

	}

	@Override
	public void deleteByCustomerCode(ZebraPartern zebraPartern) {
		ZebraPartern zp = new ZebraPartern();
		zp.setCustomerCode(zebraPartern.getCustomerCode());
		zebraParternMapper.deleteByCustomerCode(zp.getCustomerCode());

	}

	@Override
	public String selectParternCodeByCustomerCode(String customerCode) {
		return zebraParternMapper.selectParternCodeByCustomerCode(customerCode);
	}

	@Override
	public void updateByPrimaryKeySelective(ZebraPartern zebraPartern) {
		zebraParternMapper.updateByPrimaryKeySelective(zebraPartern);
	}

	@Override
	public void deleteByPrimaryKeySelective(ZebraPartern zebraPartern) {
		zebraParternMapper.deleteByPrimaryKey(zebraPartern);
	}
}
