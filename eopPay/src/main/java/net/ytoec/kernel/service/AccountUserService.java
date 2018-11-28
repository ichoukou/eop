package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.AccountUser;


/**
 * 账户实体service
 * @author guoliang.wang
 *
 * @param <T>
 */
public interface AccountUserService<T extends AccountUser>  extends BaseService<T>{

	/**
	 * 通过用户ID取账户信息
	 * 注：服务管理界面用到，当返回的对象为null时，界面提示去充值（或者购买服务）
	 * 查询手机验证码 也用这个方法
	 * @param userId 
	 * @return T
	 */
	public T getAccountUserByUserId(Integer userId) throws DataAccessException;

	/**
	 * 通过用户ID修改取账户的手机号码 
	 * @param T（userid, cell_phone）必须
	 * @return boolean
	 */
	public boolean updateAccountUserMobileByUserId(T accountUser) throws DataAccessException;
	
    
    /**
	 * 给手机发送验证码
	 * 作用1：发送一条短信到手机
	 * 作用2：修改用户表 记下phoneCode
	 * @param
	 * @return boolean
	 */
	public boolean updateSendPhoneCode(Integer userId,String phone,String phoneCode) throws DataAccessException;
	
	
	

	
	/**
	 * 通过用户ID修改账户的    总消费,总充值、余额
	 * 注：充值(总充值增加、余额增加)、买服务调用（余额减少、总消费增加）
	 * @param map里面放的三个值是(bool,userid,money)
      * Boolean true代表充值,调用updateAccountUserBalanceAddByUserId（）方法，
      * false代表买服务，调用updateAccountUserBalanceLowerByUserId（）方法 (调用dao不同的方法)
	 * @return boolean
	 */
	public boolean updateAccountUserBalanceByUserId(Map<String, Object> param) throws DataAccessException;


	/**
	 * 查询账户列表总记录数
	 * @param Map 查询条件拼成的map
	 */
	public Integer getAccountUserListCount(Map map);
	
	/**
	 * 查询账户列表
	 * 
	 * @param Map 查询条件拼成的map
	 * @Pagination 分页信息
	 * @flag 是否分页
	 * @return boolean
	 */
	public List<T> getAccountUserList(Map map, Pagination pagination,
			boolean flag);
	
	/**
	 * 获取总的可以余额
	 */
	public Double getSumUseBalance(Map map);
	
	/**
	 * 获取总消费
	 */
	public Double getSumAllConsume(Map map);
	
	
	/**
	 * 账户不存在时，调用
	 * 创建账户并插入4条 免费的服务
	 */
	public Boolean addUserAndDService(T accountUser);
	
	 /**
	 * 查询账户列表[给定时器使用,修改可使用余额,使可用余额跟余额相同]
	 * @param Map 
	 * @return List
	 */
	public List<T> getAccountUserListUseTime(Map map);
	
	/*
	 * 修改可用余额[在付款跳到支付宝冻结相应的可用余额、定时器退回可用余额、在关闭时]
	 */
    public boolean updateUseBalanceByUserId(Map map);
    
    
    public boolean updateByAccountUser(T accountUser);
    
    /**
     * 手机号取消绑定
     * @param accountUser
     * @return
     */
    public boolean cancelMobileBindByUserId(T  accountUser);

}
