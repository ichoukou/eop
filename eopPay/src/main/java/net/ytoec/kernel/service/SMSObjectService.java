package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.techcenter.protocol.standard12.Standard_Submit;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.SMSObject;

/**
 * 短信表实体类Service
 * @author guolongchao
 * 20120808
 */
@SuppressWarnings("all")
public interface SMSObjectService<T extends SMSObject> extends BaseService<T>{
	
	/**
	 * 根据查询条件进行分页查询数据
	 * @param map
	 * @param pagination
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getList(Map map,Pagination<T> pagination) throws DataAccessException;
	
	/**
	 * 根据查询条件获取总的记录数
	 * @param map
	 * @return Integer
	 */
	public Integer getRecordsCount(Map map);
	
	/**
	 * 根据查询条件查询符合条件的记录，用作发送短信
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getList(Map map) throws DataAccessException;
	
	/**
	 * 通过mysql的limit函数删除记录
	 * @param limit
	 * @throws DataAccessException
	 */
	public boolean removeByLimit(int limit) throws DataAccessException;
	
	/**
	 * 根据查询条件查询符合条件的记录，用作查询短信发送状态
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getStatusList(Map map) throws DataAccessException;
	
	
	public void addSendSMS(Standard_Submit ssm,SMSObject sMSObject);
}
