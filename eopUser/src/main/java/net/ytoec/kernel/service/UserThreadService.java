package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.QueryUserCondition;
import net.ytoec.kernel.dataobject.UserThread;

/**
 * 从金刚系统同步过来的用户信息业务接口
 * 
 * @author wangyong
 */
public interface UserThreadService<T extends UserThread> {

	/**
	 * 新增一个用户
	 * 
	 * @param entity
	 * @return @
	 */
	public boolean addUser(T entity);

	/**
	 * 根据用户Id查找一条用户信息
	 * 
	 * @param id
	 * @return
	 */
	public T getUserById(Integer id);

	/**
	 * 获取所有用户信息
	 * 
	 * @return @
	 */
	public List<T> getAllUser();

	/**
	 * 修改用户信息
	 * 
	 * @param entity
	 * @return @
	 */
	public boolean editUser(T entity);

	/**
	 * 根据用户Id删除用户
	 * 
	 * @param id
	 * @return @
	 */
	public boolean delUserById(Integer id);

	/**
	 * 检查用户编码<br>
	 * 返回检查结果字符串. <br>
	 * &nbsp; 如果编码不存在, 返回"编码不存在![<code>user.getUserCode()</code>]"<br>
	 * &nbsp; 如果编码存在但是不唯一, 返回"编码不唯一![
	 * <code>userCode:user.getUserCode();size: list.getSize()]</code>"<br>
	 * &nbsp; 如果唯一存在并且已激活, 返回"编码已激活![<code>user.getUserCode()</code>]"<br>
	 * &nbsp; 如果 编码唯一存在并且未激活. 返回"TBA"; // TO BE ACTIVE<br>
	 * &nbsp; 否则返回"未知错误![<code>user.getUserCode()</code>]";
	 * 
	 * @author ChenRen/2011-11-01
	 * @param entity
	 * @return
	 */
	public abstract String checkUserCode(T entity);

	/**
	 * 根据用户编码查询用户数据
	 * 
	 * @author ChenRen/2011-11-01
	 * @param entity
	 * @return @
	 */
	public List<T> searchUsersByCode(T entity);
	
	/**
	 * 获取某个网点下已经激活的用户 
	 * @param entity
	 * @return
	 */
	public List<T> searchUsersBySite(String siteCode);
	
	/**
	 * 获取某个网点下已经激活或未激活的用户 
	 * @param entity
	 * @return
	 */
	public List<T> searchAllUsersBySite(String siteCode);

	//该接口已经被删除，被重载方法searchUsersBySiteCodeAndName(String switchEccount, String siteCode, String userCode, String userState, String userName, Pagination pagination, boolean flag)代替
	/**
	 * 获取某个网点下已经激活的用户 ,可按用户编码和用户名称进行查询
	 * @param siteCode 网点编码（不可少）
	 * @param userCode 用户编码（可选）
	 * @param userState 用户状态（可选）
	 * @param userName 用户名称（可选）
	 * @param pagination
	 * @param flag
	 * @return
	 */
	public List<T> searchUsersBySiteCodeAndName(String siteCode, String userCode, String userState, String userName, Pagination pagination, boolean flag);
	
	/**
	 * 获取某个网点下已经激活的用户 ,可按用户编码和用户名称进行查询
	 * @param switchEccount 电子对账
	 * @param siteCode 网点编码（不可少）
	 * @param userCode 用户编码（可选）
	 * @param userState 用户状态（可选）
	 * @param userName 用户名称（可选）
	 * @param pagination
	 * @param flag
	 * @return
	 */
	public List<T> searchUsersBySiteCodeAndName(String switchEccount, String siteCode, String userCode, String userState, String userName, Pagination pagination, boolean flag);
	
	//该接口已经被删除，被重载方法searchUsersBySiteCodeAndNameAndNotId(String switchEccount, List<Integer> ids,String siteCode, String userCode, String userState, String userName, Pagination pagination, boolean flag)代替
	/**
	 * 获取某个网点下已经激活的用户 ,可按用户编码和用户名称进行查询(并且去除包含承包区的客户)
	 * @param siteCode 网点编码（不可少）
	 * @param userCode 用户编码（可选）
	 * @param userState 用户状态（可选）
	 * @param userName 用户名称（可选）
	 * @param pagination
	 * @param flag
	 * @return
	 */
	public List<T> searchUsersBySiteCodeAndNameAndNotId(List<Integer> ids,String siteCode, String userCode, String userState, String userName, Pagination pagination, boolean flag);
	
	
	/**
	 * 获取某个网点下已经激活的用户 ,可按用户编码和用户名称进行查询(并且去除包含承包区的客户)
	 * @param switchEccount 电子对账
	 * @param siteCode 网点编码（不可少）
	 * @param userCode 用户编码（可选）
	 * @param userState 用户状态（可选）
	 * @param userName 用户名称（可选）
	 * @param pagination
	 * @param flag
	 * @return
	 */
	public List<T> searchUsersBySiteCodeAndNameAndNotId(String switchEccount, List<Integer> ids,String siteCode, String userCode, String userState, String userName, Pagination pagination, boolean flag);
	
	
	/**
	 * 根据用户id来获取承包区已经激活的用户
	 * @param id
	 * @return
	 */
	public T getByIdAndState(Integer id);
	
	//该接口已经被删除，被重载方法getContractUserList(String switchEccount, List<Integer> ids, Pagination pagination, String userState, boolean flag)代替
	/**
	 * 查询承包区账户已经激活的客户列表
	 * 
	 * */
	public List<T> getContractUserList(List<Integer> ids, Pagination pagination, String userState, boolean flag);
	
	
	/**
	 * 查询承包区账户已经激活的客户列表
	 * @param switchEccount 电子对账
	 * */
	public List<T> getContractUserList(String switchEccount, List<Integer> ids, Pagination pagination, String userState, boolean flag);
	
	/**
	 * 修改是否允许网点(承包区)打印功能
	 * 
	 * @param entity
	 * @return @
	 */
	public boolean updateUserThread(T entity);
	
	  /**
	 *  允许网点打印  是否选中  //根据分仓用户和卖家勾选面单打印
	 * @param param
	 * @return
	 * @throws 
	 */
	public  List<T> getisPrintFlag(T entity);
	
	/**
	 * 通过userState查数据
	 * @param userState
	 * @return
	 */
	public List<T> getByState(String userState);
	
	public String generateUserCode(String siteCode);

    public T generateUserThread(T entity);
  
    
    /**
	 * 通过QueryUserCondition查询数据（ isCanDownload:-1时可以查询全部数据）
	 * @param quc
	 * @return
	 * @throws DataAccessException
	 * @author wangpengfei
	 */
	public List<T> queryUserThread(QueryUserCondition quc) ;
	
	/**
	 * 通过QueryUserCondition查询数据总数（ isCanDownload:-1时可以查询全部数据）
	 * @param quc
	 * @return
	 * @author wangpengfei
	 */
	public int countUserThread(QueryUserCondition quc);
	
	/**
	 * 更新list中数据的isCanDownload值
	 * @param list
	 * @param isCanDownload
	 * @return
	 * @author wangpengfei
	 */
	public boolean updateIsCanDownloadByIds(List<String> list, int isCanDownload);
	
}
