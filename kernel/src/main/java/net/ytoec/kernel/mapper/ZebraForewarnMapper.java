package net.ytoec.kernel.mapper;

import java.util.Map;

import net.ytoec.kernel.dataobject.ZebraForewarn;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 预警设置Mapper
 * 
 * @author huangtianfu
 * 
 * @param <T>
 */
public interface ZebraForewarnMapper<T extends ZebraForewarn> extends
		BaseSqlMapper<T> {

	ZebraForewarn selectByCustomerCode(String customerCode);

	int selectUserIdByUsername(String customerCode);

	// 更新商家预警信息
	void updateForwarnByCustomerCode(ZebraForewarn zebraForewarn);

	int updateInitStateByCustomerCode(String customerCode);

	int updateSendPhoneStateByCustomerCode(Map<String, Object> params);
	
	int updateSendMailStateByCustomerCode(Map<String, Object> params);
	
	int updateMailPhoneStateByCustomerCode(Map<String, Object> params);

	void insertForwarnByCustomerCode(ZebraForewarn zebraForewarn);

	void updateWarnStateByCustomerCodeAndWarnState(Map<String, Object> params);

	void updateCustomerWarnHistoryValue(Map<String, Object> params);

}