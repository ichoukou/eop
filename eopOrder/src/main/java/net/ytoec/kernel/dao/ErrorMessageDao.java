/**
 * 
 */
package net.ytoec.kernel.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

/**
 * @author Wangyong
 * 出错信息表数据访问接口
 */
public interface ErrorMessageDao<T> {
	
	/**
	 * 增加出错信息规范对象数据
	 * @param errMessage
	 * @return
	 * @throws DataAccessException
	 */
	public boolean addErrorMessage(T errMessage) throws DataAccessException;
	
	/**
	 * 删除出错信息规范对象数据
	 * @param errMessage
	 * @return
	 * @throws DataAccessException
	 */
	public boolean removeErrorMessage(T errMessage) throws DataAccessException;
	
	/**
	 * 更新出错信息规范对象数据
	 * @param errMessage
	 * @return
	 * @throws DataAccessException
	 */
	public boolean editErrorMessage(T errMessage) throws DataAccessException;
	
	/**
	 * 根据id号查询出错信息规范对象
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public T getErrorMessageById(Integer id) throws DataAccessException;
	
	/**
	 * 查询所有出错信息规范对象列表
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getAllErrorMessage() throws DataAccessException;

}
