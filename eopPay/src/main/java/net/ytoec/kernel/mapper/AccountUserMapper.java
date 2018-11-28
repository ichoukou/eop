package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.dataobject.AccountUser;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 账户Mapper
 * @author guoliang.wang
 *
 * @param <T>
 */
public interface AccountUserMapper<T extends AccountUser> extends BaseSqlMapper<T> {

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
	 * @param T（userId, cellPhone）必须
	 */
	public void updateAccountUserMobileByUserId(T accountUser) throws DataAccessException;
	
    /**
	 * 通过用户ID修改用户表 记下cellPhone,phoneCode
	 * @param Map中的key:(String userId,String cellPhone,String phoneCode)
	 */
	public void updateSendPhoneCode(Map<String, Object> param) throws DataAccessException;
	
	/**
	 * 注：充值(总充值增加、余额增加)
	 * @param map里面放的两个值是(userId,money)
	 */
	public Integer updateAccountUserBalanceAddByUserId(Map<String, Object> param) throws DataAccessException;
	
	/**
	 * 注：买服务调用（余额减少、总消费增加）
	 * @param map里面放的两个值是(userId,money)
	 */
	public Integer updateAccountUserBalanceLowerByUserId(Map<String, Object> param) throws DataAccessException;

	/**
	 * 查询账户列表
	 * @param Map 
	 * @return List
	 */
	public List<T> getAccountUserList(Map map);
	
	/**
	 * 查询账户列表总记录数
	 * @param Map 查询条件拼成的map
	 */
	public Integer getAccountUserListCount(Map map);
	
	/**
	 * 获取总的可以余额
	 */
	public Double getSumUseBalance(Map map);
	
	/**
	 * 获取总消费
	 */
	public Double getSumAllConsume(Map map);
	/*
	 * 修改可用余额[在付款跳到支付宝的时候调用]
	 */
    public Integer updateUseBalanceByUserId(Map map);
    
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
	public void cancelMobileBindByUserId(T accountUser) throws DataAccessException;
}
