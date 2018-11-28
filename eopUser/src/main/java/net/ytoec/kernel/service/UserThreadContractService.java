package net.ytoec.kernel.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.dataobject.UserThreadContract;


public interface UserThreadContractService<T extends UserThreadContract> {

	/**
	 * 承包区客户的选择，新增子账户
	 * @param entiry
	 * @return
	 * */
	public boolean insertContract(T entity);
	
	/**
	 * 查询已经被承包的客户
	 * */
	public List<T> searchContractsBysiteId(String siteId,String accountType);
	
	/**
	 * 更新当前子账号的客户(包括承包区，客服，财务)
	 **/
	public boolean updateContractByAddUserName(UserThreadContract entity);
	
	/**
	 * 删除承包的客户
	 * */
	public boolean deleteContractByConractAreaId(String addUserName,String conractAreaId,String site);
	
	/**
	 * 承包区客户账号登陆相关的用户列表 
	 */
	public List<T> getContractersByUserNameAndType(String username,String accountType);
	
	public List<T> searchContractByConractAreaId(Integer id,String accountType);
	
	/**根据用户名和客户ID查询*/
	public List<T> searchContractByConractAreaIdAndUserName(Integer id,String userName);
}