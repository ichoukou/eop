package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.ZebraPartern;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 密钥Mapper
 * 
 * @author huangtianfu
 * 
 * @param <T>
 */
public interface ZebraParternMapper<T extends ZebraPartern> extends
		BaseSqlMapper<T> {

	void insert(ZebraPartern zebraPartern);

	void updateByCustomerCode(ZebraPartern zebraPartern);

	void deleteByCustomerCode(String CustomerCode);

	void insertSelective(ZebraPartern zebraPartern);

	// 生成密钥
	void updateByPrimaryKeySelective(ZebraPartern zebraPartern);

	void deleteByPrimaryKey(ZebraPartern zebraPartern);

	String selectParternCodeByCustomerCode(String customerCode);

	String findParternByUserCode(String userCode);

	List<T> selectPageList(Map<String, Object> params);

	Integer selectTotal(Map<String, Object> map);

}
