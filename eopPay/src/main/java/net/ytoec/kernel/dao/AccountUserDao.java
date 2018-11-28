package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

/**
 * 账户Dao接口
 * @author guoliang.wang
 * @param <T>
 */
public interface AccountUserDao<T> extends BaseDao<T>{

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
	 * @param T (userId, cellPhone)必须
	 * @return boolean
	 */
	public boolean updateAccountUserMobileByUserId(T accountUser) throws DataAccessException;
	
    
    /**
	 * 给手机发送验证码
	 * 作用1：发送一条短信到手机
	 * 作用2：修改用户表 记下phoneCode
	 * @param Map中的key: (String userId,String cellPhone,String phoneCode)
	 * @return boolean
	 */
	public boolean updateSendPhoneCode(Map<String, Object> param) throws DataAccessException;
	
	/**
	 * 注：充值(总充值增加、余额增加)
	 * @param map里面放的两个值是(userId,money)
	 */
	public boolean updateAccountUserBalanceAddByUserId(Map<String, Object> param) throws DataAccessException;
	
	/**
	 * 注：买服务调用（余额减少、总消费增加）
	 * @param map里面放的两个值是(userId,money)
	 */
	public boolean updateAccountUserBalanceLowerByUserId(Map<String, Object> param) throws DataAccessException;

	/**
	 * 查询账户列表总记录数
	 * @param Map 查询条件拼成的map
	 */
	public Integer getAccountUserListCount(Map map);
	
	/**
	 * 查询账户列表
	 * @param Map 
	 * @return List
	 */
	public List<T> getAccountUserList(Map map);
	
	/**
	 * 获取总的可以余额
	 */
	public Double getSumUseBalance(Map map);
	
	/**
	 * 获取总消费
	 */
	public Double getSumAllConsume(Map map);
	
	/*
	 * 修改可用余额[在付款跳到支付宝冻结相应的可用余额、定时器退回可用余额、在关闭时]
	 */
    public boolean updateUseBalanceByUserId(Map map)throws DataAccessException;
    
    /**
	 * 查询账户列表[给定时器使用,修改可使用余额,使可用余额跟余额相同]
	 * @param Map 
	 * @return List
	 */
	public List<T> getAccountUserListUseTime(Map map);
	
	public boolean updateByAccountUser(T accountUser);
	
	/**
	 * 取消手机号绑定
	 * @param accountUser
	 * @return
	 */
	public boolean cancelMobileBindByUserId(T accountUser);
}
