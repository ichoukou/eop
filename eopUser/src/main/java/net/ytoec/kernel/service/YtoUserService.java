package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.User;

import org.springframework.dao.DataAccessException;

/**
 * 用户业务接口
 * 
 * @author ChenRen
 * @date 2011-7-26
 */
public interface YtoUserService<T extends User> extends BaseService<T> {

    /**
     * 新增一个用户
     * 
     * @param entity
     * @return @
     */
    public abstract boolean addUser(T entity);
    
    public abstract boolean addDeptUser(T entity);

    /**
     * 根据用户Id查找一条用户信息
     * 
     * @param id
     * @return
     */
    public abstract T getUserById(Integer id);

    
    /**
     * 根据clientId查询用户 
     * @param clientId
     * @return
     */
	public T getUserByClientId(String clientId);

    /**
     * 根据vipId获取网点用户
     */
    public abstract T getSiteByVipId(Integer id);

    /**
     * 修改用户信息
     * 
     * @param entity
     * @return @
     */
    public abstract boolean editUser(T entity);
    
    public abstract boolean modifyUser(T entity);

    /**
     * 跟新用户密码
     * 
     * @param entity
     * @return @
     */
    public boolean updateUser(T entity);

    /**
     * 根据用户Id删除用户
     * 
     * @param id
     * @return @
     */
    public abstract boolean delUserById(Integer id);
    
    /**
     * 根据用户名删除用户
     * @param userName
     * @return
     */
    public boolean delUserByUserName(String userName);

    /**
     * 查询用户. 现在只根据用户帐号显示值(userNameText)模糊查询 <br>
     * 要指定VIP用户的所属网点.
     * 
     * @param entity
     * @param pagination
     * @param flag true表示分页查询, false表示查询所有
     * @return
     */
    public List<T> searchUsers(Map<String, Object> params, Pagination<T> pagination, boolean flag);
    
    public List<T> getAllUserMap(Map<String, Object> params, Pagination<T> pagination, boolean flag); 
    
    /**
     * 统计用户表总数
     */
    public int searchTotal(Map<String, Object> params);
    
    /**
     * 查询用户. 现在只根据用户帐号显示值(userNameText)模糊查询 <br>
     * 要指定VIP用户的所属网点.
     * 
     * @param entity
     * @param pagination
     * @param flag true表示分页查询, false表示查询所有
     * @return
     */
    public List<T> searchUsers(T entity, Pagination<T> pagination, boolean flag);

    /**
     * 取所有用户
     * 
     * @return
     */
    public List<T> getAllUser();
	
	/**
	 * 验证密码
	 * @param entity
	 * @return
	 */
	public boolean checkPassword(T entity);

    /**
     * 查询网点的用户，以分配模板<br>
     * 如果该用户分配过模板，用户将不能被选择，标识属性field003的值为false<br>
     * 
     * @param entity
     * @param pagination
     * @param flag true表示分页查询, false表示查询所有
     * @return public List<T> listPosttempUser(T entity, Pagination<T> pagination,boolean flag);
     */

    /**
     * 检查用户帐号的唯一性<br>
     * 存在返回false；不存在返回true
     * 
     * @param entity
     * @return
     */
    public abstract boolean checkName(T entity);

    /**
     * 修改密码
     * 
     * @param entity
     * @param newPwd
     * @return
     */
    public abstract boolean edit_pwd(T entity, String newPwd);

    /**
     * 根据网点用户id查询该网点下的用户列表
     * 
     * @param site
     * @return
     */
    public List<T> searchUsersBySite(String site);

    /**
     * 根据网点编码和用户类型查询用户
     * 
     * @param site
     * @param userType
     * @return
     */
    public List<T> searchUsersBySiteAndUserType(String site, String userType);

    /**
     * 根据用户账户查询用户
     * 
     * @param userName
     * @return @
     */
    public T searchUsersByUserName(String userName);

    /**
     * 根据淘宝帐号(<b>完全匹配</b>)查找对应数据
     * 
     * @param shopAccount
     * @return
     */
    public T searchUsersByShopAccount(String shopAccount);

    /**
     * 根据用户账户查询用户
     * 
     * @param userName
     * @return @
     */
    public T searchUsersByLoginName(String loginName);

    /**
     * 根据用户名和密码验证用户信息
     * 
     * @param userName
     * @param password
     * @return @
     */
    public T searchUserByUserNameAndPass(String userName, String password);

    /**
     * 根据用户编码查找用户
     * 
     * @param entity
     * @return
     */
    public abstract List<T> searchUsersByCode(T entity);
    
    public abstract List<T> searchUsersByDeptCode(T entity);

    /**
     * 根据用户淘宝帐号查找用户<br>
     * 用户的淘宝帐号对应字段{@link User#getShopAccount()}
     * 
     * @param entity
     * @return
     */
    public abstract List<T> searchUsersByTaoBao(String taobao);

    /**
     * 检查用户编码<br>
     * 返回检查结果字符串. <br>
     * &nbsp; 如果编码不存在, 返回"编码不存在![<code>user.getUserCode()</code>]"<br>
     * &nbsp; 如果编码存在但是不唯一, 返回"编码不唯一![ <code>userCode:user.getUserCode();size: list.getSize()]</code>"<br>
     * &nbsp; 如果唯一存在并且已激活, 返回"编码已激活![<code>user.getUserCode()</code>]"<br>
     * &nbsp; 如果 编码唯一存在并且未激活. 返回"TBA"; // TO BE ACTIVE<br>
     * &nbsp; 否则返回"未知错误![<code>user.getUserCode()</code>]";
     * 
     * @param entity
     * @return
     */
    public abstract String checkUserCode(T entity);

    /**
     * 激活用户<br>
     * 激活的时候会在服务器端继续进行用户编码和用户帐号唯一性验证
     * 
     * @param entity
     * @return @
     */
    public abstract boolean activateUser(T entity);

    /**
     * 网点用户完善信息。
     * 
     * @param entity
     * @return @
     */
    public boolean completeSite(T entity);

    /**
     * 通过用户类型查询用户列表
     * 
     * @param userType
     * @return @
     */
    public List<T> getUserListByUserType(String userType);

    /**
     * 申请用户编码。向管理员发送一条消息，通知管理员向消息中指定的邮箱发送用户编码。
     * 
     * @param inactiveUser 发件人信息，必须包含id和username
     * @param linkway 联系方式
     * @param content 消息内容 @ public String applyForCode(User inactiveUser, String content, String linkway);
     */

    /**
     * 根据用户来源获取用户列表
     * 
     * @param userSource 用户来源
     * @return
     */
    public List<T> searchUsersByUserSource(String userSource);

    /**
     * 网点编码验证<br>
     * 
     * @param site
     * @return 返回验证提示消息。<br>
     * 验证通过返回null或""；验证失败的提示消息会显示在页面
     */
    public abstract String checkSite(T entity);

    /**
     * 绑定淘宝用户
     * 
     * @param user
     * @return
     */
    @Deprecated
    public abstract boolean bindTaoBaoUser(T entity);

    /**
     * 绑定淘宝用户; 第一步 完善基本的用户信息
     * 
     * @param user
     * @return
     */
    public abstract boolean bindTaoBaoUserStep1(T entity);

    /**
     * 绑定淘宝用户; 第二步
     * 
     * @param user
     * @return
     */
    public abstract boolean bindTaoBaoUserStep2(T entity);

    /**
     * 登录判断：登录时判断用户名或者用户编码与密码是否匹配。
     * 
     * @param userName
     * @param password
     * @return
     */
    public T loginUserCheck(String userName, String password);

    /**
     * 修改网点编码
     * 
     * @param entity
     * @return @
     */
    public abstract boolean editSite(T entity);

    /**
     * 绑定/取消绑定 淘宝 帐号
     * 
     * @param entity
     * @param flag 操作标识(1=绑定;0=取消绑定)
     * @return @
     */
    public abstract boolean bindTaoBaoAccount(T entity, String flag);

    /**
     * 根据父Id查找子项
     * 
     * @param pid
     * @param pagination 
     * @return
     * @throws DataAccessException
     */
    @SuppressWarnings("rawtypes")
	public Map getUserByParentId(Integer pid, Pagination<User> pagination) throws DataAccessException;
    public List<T> getUserByParentId(Integer pid) throws DataAccessException;

    /**
     * 根据customerId查找用户<br>
     * 如果该用户帐号是子账号就查询父对象
     * 
     * @param customerId
     * @return
     */
    public T getUserByCustomerId(String customerId);

    /**
     * 绑定/取消绑定子账号操作<br>
     * 
     * @param entity
     * @param flag 操作标识(1=绑定;0=取消绑定)
     */
    public abstract boolean bindSubAccount(T entity, String flag);

    /**
     * 根据用户编码、用户类型和用户状态查找用户
     * 
     * @param entity
     * @return
     */
    public List<T> searchUsersByCodeTypeState(String userCode, String userType, String userState);

    /**
     * 绑定关联帐号
     * 
     * @param targetAccount 目标帐号，即即将为它绑定关联帐号
     * @param associationAccount 关联帐号
     * @return
     */
    public boolean bindedAssociationAccount(T targetAccount, T associationAccount);

    /**
     * 根据customerids 查找user
     * 
     * @param customerIds
     * @return
     */
    List<T> getUsersByCustomerIds(String customerIds);

    /**
     * 取消绑定关系
     * 
     * @param user
     * @return
     */
    public boolean cancelAssociationAccount(T user);

    /**
     * 修改用户编码<br>
     * 1.把userthread中原用户修改成TBA状态<br>
     * 2.修改新用户为激活状态，修改时间戳<br>
     * 3.把user表中的用户修改成新的编码、名称、网点
     * 
     * @param oldUserCode
     * @param newUserCode
     * @return
     */
	public abstract boolean editUserCode(String oldUserCode, String newUserCode);

	/**
	 * 解除绑定<bR>
	 * <pre>
	 * 逻辑处理：
	 * 把该帐号、其子账号改成TBA状态
	 * 关联帐号不修改
	 * 
	 * 2012-03-27/ChenRen
	 * 增加一个逻辑处理: 在user表中根据userCode查询用户. 
	 * 如果返回结果集为0, 则认为直客没有被绑定. 把直客改成TBA状态
	 * 这样避免#电子对账 等地方使用直客的时候出现直客下面没有用户的情况
	 * </pre>
	 * @param entity
	 * @return
	 */
	public abstract boolean unbindUserCode(T entity);
	
	/**
	 * 获取平台用户下的分仓和入驻企业用户,用户页面上选择下拉列表框功能
	 * @param curUser 当前平台用户
	 * @param type 1表示只获取分仓，2获取入驻企业，0获取分仓和入驻企业
	 * @return
	 */
    public List<T> pingTaiSelect(User curUser, Integer type);
    
    /**
	 * 获取平台用户下的分仓和入驻企业用户,用户页面上选择下拉列表框功能
	 * @param curUser 当前平台用户
	 * @param type 1表示只获取分仓，2获取入驻企业，0获取分仓和入驻企业
	 * @param status 用户状态
	 * @return
	 */
    public List<T> pingTaiSelect(User curUser, Integer type,String status);
    
    /**
	 * 获取平台用户下的分仓和入驻企业用户并分页
	 * @param curUser 当前平台用户
	 * @param type 1表示只获取分仓，2获取入驻企业，0获取分仓和入驻企业
	 * @return
	 */
    public Map<String, Object> pingTaiSelect(User curUser, Integer type, Pagination<User> pagination) throws DataAccessException;
    /**
     * 根据网点编码初始化网点
     * @param userName
     * @return
     */
    public boolean initBranch(String userName);
}
