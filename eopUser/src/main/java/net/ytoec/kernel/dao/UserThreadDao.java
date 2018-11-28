package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.QueryUserCondition;
import net.ytoec.kernel.dataobject.UserThread;

import org.springframework.dao.DataAccessException;

/**
 * 从金刚系统同步过来的用户信息操作接口
 * 
 * @param <T>
 * @author wangyong 2011-10-31
 */
public interface UserThreadDao<T extends UserThread> {

	public boolean addUser(T entity) throws DataAccessException;

	public T getUserById(Integer id) throws DataAccessException;

	/**
	 * 查找所有用户. <br>
	 * 
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getAllUser() throws DataAccessException;

	public boolean editUser(T entity) throws DataAccessException;

	public boolean delUser(T entity) throws DataAccessException;

	/**
	 * 根据用户编码查找用户
	 * 
	 * @author ChenRen/2011-11-01
	 * @param entity
	 * @return
	 */
	public abstract List<T> searchUsersByCode(T entity) throws DataAccessException;
	
	/**
	 * 获取某个网点下已经激活的用户 
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> searchUsersBySite(T entity) throws DataAccessException;
	
	/**
	 * 获取某个网点下已经激活或未激活的用户 
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> searchAllUsersBySite(T entity) throws DataAccessException;
	
	/**
	 * 获取某个网点下已经激活的用户 ,可按用户编码和用户名称进行查询
	 * @param entity
	 * @return
	 */
	public List<T> searchUsersBySiteCodeAndName(Map map) throws DataAccessException;
	
	/**
	 * 根据用模板id查询用户列表
	 * 
	 * @param postId
	 * @return
	 */
	public List<T> getUserThreadByPostId(Integer postId) throws DataAccessException;

	/**
	 * 根据用户id来获取承包区已经激活的用户
	 * @param id
	 * @return
	 */
	public T getByIdAndState(Integer id) throws DataAccessException;
	
	/**
	 * 查询承包区账户已经激活的客户列表
	 * @param ids
	 * @return
	 */
	public List<T> getContractUserList(Map map) throws DataAccessException;
	/**
	 * 修改是否允许网点(承包区)打印功能
	 * 
	 * @param entity
	 * @return @
	 */
	public boolean updateUserThread(T entity) throws DataAccessException;
	
	 /**
	 *  允许网点打印  是否选中  //根据分仓用户和卖家勾选面单打印
	 * @param param
	 * @return
	 * @throws 
	 */
	public  List<T> getisPrintFlag(T entity)throws DataAccessException;
	
	/**
	 * 通过userState查数据
	 * @param userState
	 * @return
	 */
	public List<T> getByState(String userState) throws DataAccessException;
	

	/**
	 * 通过QueryUserCondition查询数据
	 * @param quc
	 * @return
	 * @throws DataAccessException
	 * @author wangpengfei
	 */
	public List<T> queryUserThread(QueryUserCondition quc) throws DataAccessException;
	
	/**
	 * 通过QueryUserCondition查询数据总数
	 * @param quc
	 * @return
	 * @throws DataAccessException
	 * @author wangpengfei
	 */
	public int countUserThread(QueryUserCondition quc) throws DataAccessException;
	
	/**
	 * 更新list中数据的isCanDownload值
	 * @param list
	 * @param isCanDownload
	 * @return
	 * @throws DataAccessException
	 * @author wangpengfei
	 */
	public boolean updateIsCanDownloadByIds(Map map) throws DataAccessException;
	
}
