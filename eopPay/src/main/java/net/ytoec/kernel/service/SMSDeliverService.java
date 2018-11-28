package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.SMSObject;

/**
 *已处理的短信表-ec_core_deliverSMS
 *Service
 * @author guolongchao
 * 20120808
 */
@SuppressWarnings("all")
public interface SMSDeliverService<T extends SMSObject> extends BaseService<T>{
	
	/**
	 * 根据查询条件进行查询数据
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getList(Map map) throws DataAccessException;
	
	/**
	 * 通过mysql的limit函数删除记录
	 * @param limit
	 */
	public boolean removeByLimit(int limit) throws DataAccessException;

	
	//public T getRecordBysequenceID(String smsType,Integer sequenceID);

	
	/**
	 * 根据id删除list集合
	 * @param list
	 * @throws DataAccessException
	 */
	public boolean removeByIDs(List<T> list) throws DataAccessException;

}
