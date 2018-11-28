/**
 * UserThreadMapper.java
 * 2011-10-31 下午01:50:45
 * wangyong
 */
package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.QueryUserCondition;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

import org.springframework.dao.DataAccessException;

/**
 * 从金刚系统同步过来的用户信息映射。
 * 
 * @author wangyong
 * 
 */
public interface UserThreadMapper<T extends UserThread> extends
		BaseSqlMapper<T> {

	public List<T> getAllUser() throws DataAccessException;

	/**
	 * 根据用户编码查找用户
	 * 
	 * @param entity
	 * @return
	 */
	public List<T> searchUsersByCode(T entity);
	
	/**
	 * 获取某个网点下已经激活的用户 
	 * @param entity
	 * @return
	 */
	public List<T> searchUsersBySite(T entity);
	
	/**
	 * 获取某个网点下已经激活或未激活的用户 
	 * @param entity
	 * @return
	 */
	public List<T> searchAllUsersBySite(T entity);
	
	/**
	 * 获取某个网点下已经激活的用户 ,可按用户编码和用户名称进行查询
	 * @param entity
	 * @return
	 */
	public List<T> searchUsersBySiteCodeAndName(Map map);
	
	
	/**
	 * 根据用模板id查询直接客列表
	 * 
	 * @param postId
	 * @return
	 */
	public List<T> getUserThreadByPostId(Integer postId);

	
	/**
	 * 根据用户id来获取承包区已经激活的用户
	 * @param id
	 * @return
	 */
	public T getByIdAndState(T entity);
	
	/**
	 * 查询承包区账户已经激活的客户列表
	 * @param ids
	 * @return
	 */
	public List<T> getContractUserList(Map map);
	
	
	
	/**
	 * 获取某个网点下已经激活的用户 
	 * @param entity
	 * @return
	 */
	public void updateUserThread(T entity);
	
	/**
	 * 允许网点打印  是否选中  //根据分仓用户和卖家勾选面单打印
	 * @param entity
	 * @return
	 */
	public List<T> getisPrintFlag(T entity);
	
	/**
	 * 通过userState查数据
	 * @param userState
	 * @return
	 */
	public List<T> getByState(String userState);
	
	/**
	 * 通过QueryUserCondition查数据
	 * @param queryUserCondition
	 * @return
	 */
	public List<T> getUserThreadList(QueryUserCondition queryUserCondition);
	
	/**
	 * 通过QueryUserCondition查数据总数
	 * @param queryUserCondition
	 * @return
	 */
	public int getCountUserThread(QueryUserCondition queryUserCondition);
	
	/**
	 * 通过map中的ids更新记录的isCanDownload的值
	 * @param map
	 */
	public void updateIsCanDownloadByIds(Map map);
}
