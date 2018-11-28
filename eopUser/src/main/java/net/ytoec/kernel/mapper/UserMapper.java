package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

import org.springframework.dao.DataAccessException;

/**
 * 
 * @author ChenRen
 * @date 2011-7-25
 */
public interface UserMapper<T extends User> extends BaseSqlMapper<T> {


	/**
	 * 根据shopName获取用户卖家主帐号ID
	 * @param shopName
	 * @return 	by wusha
	 */
	public T getUserByShopName(String shopName);
	
	
	/**
	 * 根据shopName获取用户卖家主帐号ID
	 * @param shopName  [数据库应该唯一]
	 * @return 	by   
	 */
	public List<T> getUsersByShopName(String shopName);
	/**
	 * 搜索用户, 目前根据用户帐号显示名称
	 * 
	 * @param map
	 * @return
	 */
	public List<T> searchUsers(Map<String,Object> map);
	
	public List<T> getAllUserMap(Map<String, Object> map);
	
	public void modify(T entity);
	
	public void addDeptUser(T entity);
		
	/**
	 * 搜索用户, 目前根据用户帐号显示名称
	 * 
	 * @param map
	 * @return
	 */
	public List<T> getAllUser();

	/**
	 * 根据用户帐号(<b>模糊查询</b>)查找对应数据
	 * 
	 * @param userAccount
	 * @return
	 */
	public List<T> searchUsersByLikeName(T entity);

	/**
	 * 根据用户帐号(<b>完全匹配</b>)查找对应数据
	 * 
	 * @param entity
	 * @return
	 */
	public List<T> searchUsersByUserName(T entity);

	/**
	 * 根据淘宝帐号(<b>完全匹配</b>)查找对应数据
	 * 
	 * @param entity
	 * @return
	 */
	public List<T> searchUsersByShopAccount(T entity);

	/**
	 * 根据用户淘宝帐号查找用户<br>
	 * 用户的淘宝帐号对应字段{@link User#getShopAccount()}
	 * 
	 * @param entity
	 * @return
	 */
	public List<T> searchUsersByTaoBao(String taobao)
			throws DataAccessException;

	/**
	 * 根据用户帐号和用户密码完全匹配查询数据<br>
	 * 
	 * @param userAccount
	 * @return
	 */
	public List<T> searchUserByNameAndPwd(T entity);

	/**
	 * 根据网点编码查询该网点下的vip用户
	 * 
	 * @param entity
	 * @return
	 */
	public List<T> searchUsersBySite(T entity);
	
	/**
	 * 根据clientId查询用户
	 * 
	 * @param entity
	 * @return
	 */
	public T getUserByClientId(String clientId);

	/**
	 * 根据网点编码和用户类型查询用户
	 * 
	 * @param entity
	 * @return
	 */
	public List<T> searchUsersBySiteAndUserType(T entity);

	/**
	 * 根据用户编码查找用户
	 * 
	 * @param entity
	 * @return
	 */
	public List<T> searchUsersByCode(T entity);
	
	public List<T> searchUsersByDeptCode(T entity);

	/**
	 * 根据用户类型查询用户列表
	 * 
	 * @param entity
	 * @return
	 */
	public List<T> searchUsersByUserType(T entity);
	
	/**
	 * 根据用户编码、用户类型和用户状态查找用户
	 * @param entity
	 * @return
	 */
	public List<T> searchUsersByCodeTypeState(T entity);

	/**
	 * 根据用模板id查询Vip用户列表
	 * 
	 * @param postId
	 * @return
	 */
	public List<T> getVipUserByPostId(Integer postId);

	/**
	 * 根据用户来源获取用户列表
	 * 
	 * @param userSource
	 *            用户来源
	 * @return
	 */
	public List<T> searchUsersByUserSource(String userSource);

	/**
	 * 登录判断：登录时判断用户名或者用户编码与密码是否匹配。
	 * 
	 * @param entity
	 * @return
	 */
	public List<T> loginUserCheck(T entity);

	/**
	 * 根据vipId获取网点用户
	 */
	public List<T> getSiteByVipId(Integer id) throws DataAccessException;
	
	/**
	 * 根据父Id查找子项
	 * 
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getUserByParentId(Map map) throws DataAccessException;

	public T getUserByCustomerId(String customerId);
	
    public List<T> getUsersByCustomerIds(Map<String, List<String>> map);
    
    /**
     * 根据用户来源获取用户。map中存放userSource、userState和type(1表示只获取分仓，2获取入驻企业，0获取分仓和入驻企业)。
     * userSource必选：分仓和入驻企业的该字段是平台用户的主键id
     * @param map
     * @return
     */
    public List<T> searchDepotHosting(Map map);
    
    
    /**
     * 根据 remark 取得user
     * @param map
     * @return
     */
    public List<T> getUsersByRemark(Map<String, String> map);
    
    /**
     * 找出用户表中userCode重复的网点数据 :其他不符合逻辑的方法勿用
     * @return
     */
    public List<T> getRepeatSiteList();
    
    /**
     * 根据用户编码和用户类型获取用户列表
     * @param entity
     * @return
     */
    public List<T> searchUsersByCodeAndType(T entity);
    
    /**
     * 根据承包区客户的选择来更新卖家的site
     * 
     * @param map
     */
    public void updateSiteByUserCode(Map map);
    
    
    /**
     * 根据分仓用户和卖家勾选面单打印
     * 
     * @param map
     */
    public void updateUserPrint(Map map);
    
    /**
	 * 允许网点打印  是否选中  //根据分仓用户和卖家勾选面单打印
	 * 
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getisPrintFlag(Map map) throws DataAccessException;
    
    
    /**
     * 修改打印导航标志
     * 
     * @param map
     */
    public void updateUserPrintNav(Map map);    
    
    
	
	/**
	 * 根据user name 查询用户
	 * 
	 * @param entity
	 * @return
	 */
	public T getUserByUserName(String userName);
	
	/**
	 * 本方法的作用是：
	 * 把user表 customer_id不为空，但是user_name 和shopaccount为空的账号删除
	 * @return
	 */
	public List<User> searchUserByTBKeyAndUserNameAndShopAccount();
	

    /**
     * 根据user 状态查询用户
     * 
     * @param map
     * @return
     */
    public List<T> getUserByUserStateAndType(Map map);
    
    
    /**
	 * 根据shopUserName查找用户(服务管理模块用到)
	 * pararms :shopUserName
	 * @return List
	 */
	public List<T> getUserByShopNameorUserName(String shopUserName);
	
	/**
	 * 根据mobilePhone查找用户(服务管理模块用到)
	 * pararms :mobilePhone
	 * @return List
	 */
	public List<T> getUserByMobilePhone(String mobilePhone);
	
	/**
	 * 通过用户类型查询用户列表(服务管理模块用到)
	 * @param Map map
	 * @return List
	 */
	public List<T> getUserListByUserTypes(Map map);
    
    /**
     * 根据网点编码初始化网点
     * @param userName
     * @return
     */
    public Integer initBranch(String userName);
    
    /**
     * 根据 usercode 查到最后登陆的用户
     * @param userCode
     * @return
     */
    public T getUserByUserCode(String userCode);

    /**
	 * 通过商家代码查询网点的email
	 * 
	 * @param userCode
	 */
	public String searchSiteMailByUserCode(String userCode);
    
    
    /**
     * 创建网点用户之后修改 TaobaoEncodeKey=id
     * 
     * @param map{
     *  	id : 主键id
     *  	TaobaoEncodeKey : taobao_encode_key
     *  }
     *  @return
     *}
     */
    public void updateTaobaoEncodeKeyById(Map map);  
    
    /**
     * 统计用户表总数
     */
    public int searchTotal(Map<String, Object> map);
}
