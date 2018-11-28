/**
 * UserThreadContractMapper.java
 * 2012-05-08 下午03:07:45
 * yuyuezhong
 */
package net.ytoec.kernel.mapper;

import java.util.List;

import net.ytoec.kernel.dataobject.UserThreadContract;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;


public interface UserThreadContractMapper<T extends UserThreadContract> extends
		BaseSqlMapper<T> {
	/**
	 * 承包区客户选择，新增子账户
	 * @param map
	 * @return
	 * */
	public void insertContract(T entity);
	
	/**
	 * 查询已经被承包的客户
	 * */
	public List<T> searchContractsBysiteId(T entity);
	
	/**
	 * 更新当前子账号的客户(包括承包区，客服，财务)
	 **/
	public void updateContractByAddUserName(T entity);
	
	/**
	 * 删除承包的客户
	 * */
	public void remove(T entity);
	
	/**
	 * 批量删除分配的客户
	 * */
	public void removeByAddUsername(T entity);

	/**
	 * 承包区客户账号登陆相关的用户列表 
	 */
	public List<T> getContractersByUserNameAndType(T entity);
	
	/**
	 * 查询指定承包的客户
	 * */
	public List<T> searchContractByConractAreaId(T entity);
	
	/**根据用户名和客户ID查询*/
	public List<T> searchContractByConractAreaIdAndUserName(T entity);
}

