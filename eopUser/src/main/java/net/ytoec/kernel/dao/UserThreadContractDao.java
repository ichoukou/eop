package net.ytoec.kernel.dao;


import java.util.List;

import net.ytoec.kernel.dataobject.UserThreadContract;
import org.springframework.dao.DataAccessException;

public interface UserThreadContractDao<T extends UserThreadContract> {

	/**
	 * 新增子账户操作
	 * */
	public boolean insertContract(T entity) throws DataAccessException;
	
	/**
	 * 查询已经被承包的客户
	 * */
	public List<T> searchContractsBysiteId(UserThreadContract entity) throws DataAccessException;
	
	/**
	 * 更新当前子账号的客户(包括承包区，客服，财务)
	 **/
	public boolean updateContractByAddUserName(UserThreadContract entity) throws DataAccessException;
	
	/**
	 * 删除承包的客户
	 * */
	public boolean deleteContractByConractAreaId(UserThreadContract entity) throws DataAccessException;
	
	/**
	 * 批量删除承包的客户
	 * */
	public boolean deleteContractByAddUsername(UserThreadContract entity) throws DataAccessException;
	
	/**
	 * 承包区客户账号登陆相关的用户列表 
	 */
	public List<T> getContractersByUserNameAndType(UserThreadContract entity) throws DataAccessException;
	
	/**
	 * 查询指定承包的客户
	 * */
	public List<T> searchContractByConractAreaId(Integer id, String accountType) throws DataAccessException;
	
	/**根据用户名和客户ID查询*/
	public List<T> searchContractByConractAreaIdAndUserName(UserThreadContract entity) throws DataAccessException;
}