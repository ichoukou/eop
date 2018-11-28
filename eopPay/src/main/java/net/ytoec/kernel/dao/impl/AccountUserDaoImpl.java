package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.AccountUserDao;
import net.ytoec.kernel.dataobject.AccountUser;
import net.ytoec.kernel.mapper.AccountUserMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;


@Repository
@SuppressWarnings("all")
public class AccountUserDaoImpl<T extends AccountUser> implements AccountUserDao<T> {

	private static final Logger logger = LoggerFactory.getLogger(AccountUserDaoImpl.class);

	@Inject
	private AccountUserMapper<T> mapper;
	
	
	@Override
	public T getAccountUserByUserId(Integer userId) throws DataAccessException {
		 try 
        {        	
        	return  mapper.getAccountUserByUserId(userId);  
        	
        } catch (Exception e) {
            logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue(), e);
        }
		return null;
	}

	@Override
	public boolean updateAccountUserMobileByUserId(T accountUser)
			throws DataAccessException {
		boolean flag = true;
        try {
        	 mapper.updateAccountUserMobileByUserId(accountUser);
           
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
            flag = false;
        }
        return flag;		
	}

	@Override
	public boolean updateSendPhoneCode(Map<String, Object> param)
			throws DataAccessException {
		boolean flag = true;
        try {
        	 //修改用户表 下cellPhone,phoneCode
        	 mapper.updateSendPhoneCode(param);
           
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
            flag = false;
        }
        return flag;
	}

	@Override
	public boolean updateAccountUserBalanceAddByUserId(Map<String, Object> param)
			throws DataAccessException {
		int i= mapper.updateAccountUserBalanceAddByUserId(param);
		if(i==1){
			return true;
		}		
        return false;
	}

	@Override
	public boolean updateAccountUserBalanceLowerByUserId(
			Map<String, Object> param) throws DataAccessException {
		int i= mapper.updateAccountUserBalanceLowerByUserId(param);
		if(i==1){
			return true;
		}		
        return false;
	}

	@Override
	public List<T> getAccountUserList(Map map) {
		List<T> list=null;
		 try {        	 
			 list = mapper.getAccountUserList(map);
           
        } catch (Exception e) {
            logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue(), e);            
        }
		return list;
	}

	@Override
	public boolean add(T entity) {
		boolean flag = true;
        try {        	 
        	 mapper.add(entity);
           
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
            flag = false;
        }
        return flag;
	}

	@Override
	public boolean edit(T entity) {
		boolean flag = true;
        try {        	 
        	 mapper.edit(entity);
           
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
            flag = false;
        }
        return flag;
	}

	@Override
	public boolean remove(T entity) {
		boolean flag = true;
        try {        	 
        	 mapper.remove(entity);
           
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
            flag = false;
        }
        return flag;
	}

	@Override
	public T get(T entity) {
		T t= null;
        try {        	 
        	t=mapper.get(entity);
           
        } catch (Exception e) {
            logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue(), e);
        }
        return t;
	}

	@Override
	public Double getSumUseBalance(Map map) {
		Double res=0.0;
		 try {        	 
			 res=mapper.getSumUseBalance(map);
        } catch (Exception e) {
            logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue(), e);            
        }
		return res;
	}

	@Override
	public Double getSumAllConsume(Map map) {
		Double res=0.0;
		 try {        	 
			 res = mapper.getSumAllConsume(map);
        } catch (Exception e) {
            logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue(), e);            
        }
		return res;
	}

	/*
	 * 修改可用余额[在付款跳到支付宝的时候调用]
	 */
	@Override
	public boolean updateUseBalanceByUserId(Map map)throws DataAccessException{		
		int i=mapper.updateUseBalanceByUserId(map);
		if(i==1){
			return true;
		}		
        return false;
	}

	/**
	 * 查询账户列表[给定时器使用,修改可使用余额,使可用余额跟余额相同]
	 * @param Map 
	 * @return List
	 */
	@Override
	public List<T> getAccountUserListUseTime(Map map) {
		return mapper.getAccountUserListUseTime(map);
	}

	@Override
	public boolean updateByAccountUser(T accountUser) {
		boolean flag = true;
        try {        	 
        	 mapper.updateByAccountUser(accountUser);
           
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
            flag = false;
        }
        return flag;
	}
    /**
     * 取消手机号绑定
     */
	@Override
	public boolean cancelMobileBindByUserId(T accountUser) {
		boolean flag = true;
		try {
		   mapper.cancelMobileBindByUserId(accountUser);
		  
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(),e);
			flag = false;
		}
		return flag;
	}

	@Override
	public Integer getAccountUserListCount(Map map) {
		// TODO Auto-generated method stub
		return mapper.getAccountUserListCount(map);
	}

}
