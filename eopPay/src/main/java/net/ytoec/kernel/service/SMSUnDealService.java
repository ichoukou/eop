package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.dataobject.SMSConfigURL;

/**
 * 将未正确处理的短信统统转移到短信历史记录表ec_core_paysms_info
 * @author guolongchao
 * 20120919
 */
@SuppressWarnings("all")
public interface SMSUnDealService extends BaseService{

	/**
	 * 根据条件查询
	 * @param limit 查询的条数
	 * @param hours  小时数
	 */
	public void sendToHistoryInfo(int limit,int hours);
}
