package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.ZebraPartern;

/**
 * 密钥管理dao
 * 
 * @author tfhuang
 * 
 * @param <T>
 */
public interface ZebraParternDao<T> {

	List<T> selectPageList(Map<String, Object> params);

	Integer selectTotal(Map<String, Object> map);

	void insertSelective(ZebraPartern zebraPartern);

	void updateByPrimaryKeySelective(ZebraPartern zebraPartern);

	void deleteByPrimaryKeySelective(ZebraPartern zebraPartern);

	String selectParternCodeByCustomerCode(String customerCode);

	void updateByCustomerCode(ZebraPartern zebraPartern);

	void deleteByCustomerCode(ZebraPartern zebraPartern);
	
}
