/**
 * 
 */
package net.ytoec.kernel.service;

import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.common.Pagination;

/**
 * 我的客户业务处理接口
 * @author wangyong
 * @2012-01-05
 */
public interface UserBeanService<T> {

	/**
	 * 首先根据网点编码从userThread表中查找该编码下的直客信息。<br>
	 * 再根据直客信息中查找的用户编码从user表中查找用户信息。
	 * @param siteCode 网点编码不为空
	 * @param userCode 可选查询条件（查找userThread 表）
	 * @param userState 激活状态
	 * @param userName 可选查询条件（查找userThread 表）
	 * @return
	 */
	public List<T> getUserList(String switchEccount,String siteCode, String userCode, String userName, String userState, Pagination pagination, boolean flag);
	
	/**
	 * 统计总数
	 * @param siteCode
	 * @param userCode
	 * @param userName
	 * @param userState 激活状态
	 * @param pagination
	 * @param flag
	 * @return
	 */
	public int countUserList(String switchEccount,String siteCode, String userCode, String userName, String userState, Pagination pagination, boolean flag);

	/**
	 * 查询承包区账户已经激活的客户列表
	 * @param
	 * 
	 * @return
	 */
	public List<T> getContractUserList(String switchEccount, List<Integer> ids, Pagination pagination,String userState, boolean flag);

	/**
	 * 统计总数
	 * @param ids
	 * @param pagination
	 * @param userState
	 * @param flag
	 * @return
	 */
	public int countContractUserList(String switchEccount,List<Integer> ids, Pagination pagination,String userState, boolean flag);
}
