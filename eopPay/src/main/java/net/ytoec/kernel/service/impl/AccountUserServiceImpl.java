package net.ytoec.kernel.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.CommonUtil;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.PayEnumConstants;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.AccountUserDao;
import net.ytoec.kernel.dao.DredgeServiceDao;
import net.ytoec.kernel.dao.PayServiceDao;
import net.ytoec.kernel.dataobject.AccountUser;
import net.ytoec.kernel.dataobject.DredgeService;
import net.ytoec.kernel.dataobject.PayService;
import net.ytoec.kernel.service.AccountUserService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 账户表的service
 * @author guoliang.wang 
 */
@Service
@Transactional
public class AccountUserServiceImpl<T extends AccountUser> implements
		AccountUserService<T> {

	private static final Logger logger = LoggerFactory
			.getLogger(AccountUserServiceImpl.class);

	@Inject
	private AccountUserDao<AccountUser> accountUserDao;

	@Inject
	private DredgeServiceDao<DredgeService> dredgeServiceDao;

	@Inject
	private PayServiceDao<PayService> payServiceDao;
	
	

	@Override
	@SuppressWarnings("all")
	public T getAccountUserByUserId(Integer userId) throws DataAccessException {

		return (T) accountUserDao.getAccountUserByUserId(userId);
	}

	/**
	 * 通过用户ID修改取账户的手机号码 
	 * @param T（userid, cell_phone）必须
	 * @return boolean
	 */
	@Override
	public boolean updateAccountUserMobileByUserId(T accountUser)
			throws DataAccessException {
		if(accountUser.getUserId() ==null||
				accountUser.getCellPhone()==null){
			return false;
		}
		return accountUserDao.updateAccountUserMobileByUserId(accountUser);
	}

	 /**
	 * 给手机发送验证码
	 * 作用1：发送一条短信到手机
	 * 作用2：修改用户表 记下phoneCode
	 * @param
	 * @return boolean
	 */
	@Override
	@SuppressWarnings("all")
	public boolean updateSendPhoneCode(Integer userId, String cellPhone,
			String phoneCode) throws DataAccessException {
		boolean flag = true;
		//1)判断手机号码是否符合规则
		  if((BooleanUtils.isFalse(CommonUtil.isMobilePhone(cellPhone)))||
				  (userId==null)){
			  return false;
		  }
		
		try {
			
			if (phoneCode == null || phoneCode.trim().length() == 0) {
				logger.error(LogInfoEnum.PARAM_EMPTY.getValue());
				return false;
			}
			//1.1)如果用户不存在,需增加记录
			AccountUser accountUser=accountUserDao.getAccountUserByUserId(userId);
			if(accountUser==null){
			    accountUser=new AccountUser();
				accountUser.setUserId(userId);
				addUserAndDService((T)accountUser);
			}			
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId", userId);
			//param.put("cellPhone", cellPhone);--不需要修改手机号码
			param.put("phoneCode", phoneCode);
			//3) 更新验证码
			flag = accountUserDao.updateSendPhoneCode(param);
		} catch (Exception e) {
			logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue(), e);
		}
		return flag;
	}

	/**
	 * 通过用户ID修改账户的 总消费,总充值、余额 
	 * 注：充值(总充值增加、余额增加)、买服务调用（余额减少、总消费增加） 
	 * @param map里面放的三个值是
	 * (bool,userid,money) Boolean
	 *true代表充值,调用updateAccountUserBalanceAddByUserId（）方法，
	 *false代表买服务，调用updateAccountUserBalanceLowerByUserId（）方法
	 *(调用dao不同的方法)
	 * @return boolean
	 */
	@Override
	@Deprecated
	public boolean updateAccountUserBalanceByUserId(Map<String, Object> param)
			throws DataAccessException {
		Boolean bool = false;
		if (param.get("bool") != null) {
			bool = Boolean.parseBoolean(param.get("bool").toString());
			if (BooleanUtils.isFalse(bool)) {
				return accountUserDao
						.updateAccountUserBalanceLowerByUserId(param);
			} else {
				return accountUserDao
						.updateAccountUserBalanceAddByUserId(param);
			}
		}
		return false;

	}

	@Override
	@SuppressWarnings("all")
	public List<T> getAccountUserList(Map map, Pagination pagination,
			boolean flag) {
		List<T> list = null;
		if (flag) {
			map.put("startIndex", pagination.getStartIndex()); // 设置分页参数
			map.put("pageNum", pagination.getPageNum());
		} else {
			// 去掉分页参数
			if (map.containsKey("startIndex"))
				map.remove("startIndex");
			if (map.containsKey("pageNum"))
				map.remove("pageNum");
		}
		list = (List<T>) accountUserDao.getAccountUserList(map);
		return list;
	}

	@Override
	public boolean add(T entity) {
		return accountUserDao.add(entity);
	}

	@Override
	public boolean edit(T entity) {
		return accountUserDao.edit(entity);
	}

	@Override
	public boolean remove(T entity) {
		return accountUserDao.remove(entity);
	}

	@Override
	@SuppressWarnings("all")
	public T get(T entity) {
		return (T) accountUserDao.get(entity);
	}

	@Override
	public Double getSumUseBalance(Map map) {
		return accountUserDao.getSumUseBalance(map);
	}

	@Override
	public Double getSumAllConsume(Map map) {
		return accountUserDao.getSumAllConsume(map);
	}

	@Override
	public Boolean addUserAndDService(T accountUser) {
		boolean res = true;

		// 1)创建账户
		if (accountUser == null || accountUser.getUserId() == null) {
			logger.info("用户ID：" + LogInfoEnum.PARAM_EMPTY.getValue());
			return false;
		}
		res = accountUserDao.add(accountUser);

		// 2)插入免费的服务

		List<PayService> pay_list = payServiceDao.getFreeServiceList();// 查询免费的服务
		if (CollectionUtils.isEmpty(pay_list)) {
			logger.info("没有免费的服务");
		} else {
			List<DredgeService> dredgeFreeServices = new ArrayList<DredgeService>();
			for (PayService payService : pay_list) {
				DredgeService dredgeService = new DredgeService();
				dredgeService.setRemark(payService.getName());
				dredgeService.setFlag(PayEnumConstants.PAY_FLAG.ISFLAG.value);
				dredgeService.setServiceId(payService.getId());
				dredgeService.setUserId(accountUser.getUserId());
				if(1==payService.getId()){
					//2.1)余额不足提醒
					dredgeService.setBalance(5.0);
				}else if(4==payService.getId()){
					//短信不足提醒
					dredgeService.setSmsLess(50);
				}
				dredgeFreeServices.add(dredgeService);
			}
			res = dredgeServiceDao.insertBatch(dredgeFreeServices);
		}

		return res;
	}

	@Override
	public List<T> getAccountUserListUseTime(Map map) {
		return (List<T>) accountUserDao.getAccountUserListUseTime(map);
	}

	/*
	 * 修改可用余额[在付款跳到支付宝冻结相应的可用余额、定时器退回可用余额、在关闭时]
	 */
	@Override
	public boolean updateUseBalanceByUserId(Map map) {
		return accountUserDao.updateUseBalanceByUserId(map);
	}

	@Override
	public boolean updateByAccountUser(T accountUser) {
		return accountUserDao.updateByAccountUser(accountUser);
	}
   
	/**
	 * 取消手机号绑定
	 */
	@Override
	public boolean cancelMobileBindByUserId(T accountUser) {
		
		return accountUserDao.cancelMobileBindByUserId(accountUser);
	}

	@Override
	public Integer getAccountUserListCount(Map map) {
		// TODO Auto-generated method stub
		return accountUserDao.getAccountUserListCount(map);
	}

}
