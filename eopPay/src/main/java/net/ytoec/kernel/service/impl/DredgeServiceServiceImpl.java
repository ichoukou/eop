package net.ytoec.kernel.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.PayEnumConstants;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.AccountUserDao;
import net.ytoec.kernel.dao.DredgeServiceDao;
import net.ytoec.kernel.dao.LogsDao;
import net.ytoec.kernel.dao.PayServiceDao;
import net.ytoec.kernel.dao.PaymentDao;
import net.ytoec.kernel.dao.ServiceHistoryDao;
import net.ytoec.kernel.dataobject.AccountUser;
import net.ytoec.kernel.dataobject.DredgeService;
import net.ytoec.kernel.dataobject.Logs;
import net.ytoec.kernel.dataobject.PayService;
import net.ytoec.kernel.dataobject.Payment;
import net.ytoec.kernel.dataobject.ServiceHistory;
import net.ytoec.kernel.service.DredgeServiceService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@SuppressWarnings("all")
public class DredgeServiceServiceImpl<T extends DredgeService> implements
		DredgeServiceService<T> {

	private static final Logger logger = LoggerFactory
			.getLogger(DredgeServiceServiceImpl.class);

	@Inject
	private DredgeServiceDao<DredgeService> dredgeServiceDao;

	@Inject
	private PaymentDao<Payment> paymentDao;

	@Inject
	private ServiceHistoryDao<ServiceHistory> serviceHistoryDao;

	@Inject
	private AccountUserDao<AccountUser> accountUserDao;

	@Inject
	private PayServiceDao<PayService> payServiceDao;

	/*
	 * 日志
	 */
	@Inject
	private LogsDao<Logs> logsDao;

	@Override
	public boolean updateDredgeserviceBalanceById(T dredgeService) {

		return dredgeServiceDao.updateDredgeserviceBalanceById(dredgeService);

	}

	@Override
	public List<T> getDredgeserviceList(Map map, Pagination pagination,
			boolean flag) {

		List<T> list = null;
		if (flag) {
			map.put("startIndex", pagination.getStartIndex()); // 设置分页参数
			map.put("pageNum", pagination.getPageNum());
		} else {
			if (map.containsKey("startIndex"))// 去掉分页参数
				map.remove("startIndex");
			if (map.containsKey("pageNum"))
				map.remove("pageNum");
		}
		list = (List<T>) dredgeServiceDao.getDredgeserviceList(map);
		return list;
	}

	@Override
	@Deprecated
	public boolean updateBalanceByUserId(T dredgeService) {
		return dredgeServiceDao.updateBalanceByUserId(dredgeService);
	}

	@Override
	public boolean updateFlagById(T dredgeService) {
		return dredgeServiceDao.updateFlagById(dredgeService);
	}

	@Override
	public List<T> getOpenserviceList(Integer userId) {
		//免费的服务
		List<PayService> freeServiceList=payServiceDao.getFreeServiceList();
          Map freemap = new HashMap();		
          freemap.put("userId", userId);
		if(CollectionUtils.isNotEmpty(freeServiceList)){
			List<Integer> serviceIdList=new ArrayList<Integer>();//存放已经开通服务的id
			for(PayService payService:freeServiceList){
				serviceIdList.add(payService.getId());
			}
			freemap.put("serviceIdList", serviceIdList);
		}
		return (List<T>) dredgeServiceDao.getOpenserviceList(freemap);
	}

	@Override
	public List<T> getServiceRecordByUserId(Map map) {
		return (List<T>) dredgeServiceDao.getServiceRecordByUserId(map);

	}

	@Override
	public boolean add(T entity) {
		return dredgeServiceDao.add(entity);
	}

	@Override
	public boolean edit(T entity) {
		return dredgeServiceDao.edit(entity);
	}

	@Override
	public boolean remove(T entity) {
		return dredgeServiceDao.remove(entity);
	}

	@Override
	public T get(T entity) {
		return (T) dredgeServiceDao.get(entity);
	}

	/**
	 * (注：余额充值调用此方法不足调用其他方法)
	 * 当点击付款的时候 余额充足 首先要查询出来已开通服务，看看这个服务是否已经存在 如果这个服务已经开通过 需要更新
	 * flag == 1	 
	 * 更新已开通服务表 插入交易明细表 插入服务 历史记录表 更新账户表 key dredgeService value dredgeService
	 * //已开通服务 key payment value payment //交易明细 key serviceHistory value
	 * serviceHistory //服务历史记录 key accountUser value accountUser //账户
	 * 
	 *flag == 2 
	 *当点击付款的时候 余额充足 当用户在服务管理页面 点击付款的时候 如果付款成功的话 需要 如果这个服务没有开通过 更新账户表
	 * 插入已开通服务表 插入历史记录表 插入交易明细表 key payment value payment //交易明细表 key
	 *serviceHistory value serviceHistory //历史记录表 key accountUser value
	 * accountUser //账户表 key dredgeService value dredgeService //已开通服务	 * 
	 * 余额不足调用其他方法
	 * 
	 * flag == 4 
	 * 开通短信服务时，需要插入一条交易明细，更新已开通服务，或者插入已开通服务表
	 * @return boolean
	 */
	@Override
	public boolean openService(Map<String, Object> map, String type) {
        boolean bool=true;
		if (!map.containsKey("dredgeService") || !map.containsKey("payment")
				|| !map.containsKey("serviceHistory")
				|| !map.containsKey("accountUser")) {
			logger.error(LogInfoEnum.PARAM_EMPTY.getValue());
			return false;
		}
		//===========================begin======================
		DredgeService dredgeService= (DredgeService) map.get("dredgeService");
		Payment payment= (Payment) map.get("payment");
		ServiceHistory serviceHistory= (ServiceHistory) map.get("serviceHistory");
		AccountUser accountUser= (AccountUser) map.get("accountUser");
		AccountUser accountParentUser = (AccountUser) map.get("accountParentUser");
		//===========================end=========================
		if (StringUtils.equals("1", type)) {
			//1.1)说明已开通服务已经存在记录,则需要更新记录
			if (map.get("dredgeService") != null){
				try{
					dredgeServiceDao.edit(dredgeService);
				}catch(Exception e){
					bool=false;
					logger.error("余额充值付款,修改已开通服务表报错");
				}
			}
			//1.2)插入交易明细表
			if (map.get("payment") != null){
				try{
					paymentDao.add(payment);
				}catch(Exception e){
					bool=false;
					logger.error("余额充值付款,插入交易明细表报错");
				}
			}	
			//1.3)插入服务历史记录表
			if (map.get("serviceHistory") != null){
				try{
					serviceHistory.setDserviceId(dredgeService.getId());
					serviceHistory.setPayId(payment.getId());
					serviceHistoryDao.add(serviceHistory);
				}catch(Exception e){
					bool=false;
					logger.error("余额充值付款,插入服务历史记录表报错");
				}	
			}	
			//1.4)修改服务历史记录表
			if (map.get("accountUser") != null){
				try{
					accountUserDao.edit(accountUser);
				}catch(Exception e){
					bool=false;
					logger.error("余额充值付款,修改账户表报错");
				}	
			}	
			//1.5)修改父账号的金额
			if(map.get("accountParentUser") != null){
				try{
					accountUserDao.edit(accountParentUser);
					
					Logs logs=new Logs();
					logs.setOperName(payment.getDealName());
					logs.setOperType(PayEnumConstants.OPERTYPE.OTHER.getValue());
					logs.setUserId(accountParentUser.getUserId());
					logs.setRemark(accountUser.getUserId()+" 用到了平台用户 的钱买服务");
					logsDao.add(logs);
					logger.error("短信调用,修改用户账户不成功!");
				}catch(Exception e){
					bool=false;
					logger.error("余额充值付款,修改账户表报错");
				}
			}
		} else if (StringUtils.equals("2", type)) {
			//1.1)插入开通服务表
			if (map.get("dredgeService") != null){
				try{
					dredgeServiceDao.add(dredgeService);
				}catch(Exception e){
					bool=false;
					logger.error("余额充值付款,插入开通服务表报错");
				}
			}
			//1.2)插入交易明细表	
			if (map.get("payment") != null){
				try{
					paymentDao.add(payment);
				}catch(Exception e){
					bool=false;
					logger.error("余额充值付款,插入交易明细表报错");
				}		
			}	
			//1.3)插入服务历史记录表	
			if (map.get("serviceHistory") != null){
				try{
					serviceHistory.setDserviceId(dredgeService.getId());
					serviceHistory.setPayId(payment.getId());
					serviceHistoryDao.add(serviceHistory);
				}catch(Exception e){
					bool=false;
					logger.error("余额充值付款,插入交易明细表报错");
				}	
			}
			//1.4)修改账户表
			if (map.get("accountUser") != null){
				try{
					accountUserDao.edit(accountUser);
				}catch(Exception e){
					bool=false;
					logger.error("余额充值付款,修改账户表报错");
				}
			
			}	
			//1.5)修改父账号的金额
			if(map.get("accountParentUser") != null){
				try{
					accountUserDao.edit(accountParentUser);
					Logs logs=new Logs();
					logs.setOperName(payment.getDealName());
					logs.setOperType(PayEnumConstants.OPERTYPE.OTHER.getValue());
					logs.setUserId(accountParentUser.getUserId());
					logs.setRemark(accountUser.getUserId()+" 用到了平台用户 的钱买服务");
					logsDao.add(logs);
					logger.error("短信调用,修改用户账户不成功!");
					
				}catch(Exception e){
					bool=false;
					logger.error("余额充值付款,修改账户表报错");
				}
			}
		} if (StringUtils.equals("3", type)) {
			//1.1)更新已开通服务表
			if (map.get("dredgeService") != null){
				try{
					dredgeServiceDao.edit(dredgeService);
				}catch(Exception e){
					bool=false;
					logger.error("余额充值付款,插入开通服务表报错");
				}
			}
			//1.2) 更新交易明细表
			if (map.get("payment") != null){
				try{
					paymentDao.edit(payment);
				}catch(Exception e){
					bool=false;
					logger.error("余额充值付款,插入交易明细表报错");
				}		
			}	
			//1.3)更新服务历史记录表	
			if (map.get("serviceHistory") != null){
				try{
					serviceHistory.setDserviceId(dredgeService.getId());
					serviceHistory.setPayId(payment.getId());
					serviceHistoryDao.edit(serviceHistory);
				}catch(Exception e){
					bool=false;
					logger.error("余额充值付款,更新服务历史记录表报错");
				}	
			}
			//1.4)修改账户表
			if (map.get("accountUser") != null){
				try{
					accountUserDao.edit(accountUser);
				}catch(Exception e){
					bool=false;
					logger.error("余额充值付款,修改账户表报错");
				}
			
		}
			//1.5)修改父账号的金额
			if(map.get("accountParentUser") != null){
				try{
					accountUserDao.edit(accountParentUser);
					
					Logs logs=new Logs();
					logs.setOperName(payment.getDealName());
					logs.setOperType(PayEnumConstants.OPERTYPE.OTHER.getValue());
					logs.setUserId(accountParentUser.getUserId());
					logs.setRemark(accountUser.getUserId()+" 用到了平台用户 的钱买服务");
					logsDao.add(logs);
					logger.error("短信调用,修改用户账户不成功!");
				}catch(Exception e){
					bool=false;
					logger.error("余额充值付款,修改账户表报错");
				}
			}
		}else if (StringUtils.equals("4", type)) {
			if(map.get("payment") != null){
				try{
					paymentDao.add(payment);
				}catch(Exception e){
					bool=false;
					logger.error("开通短信、插入交易明细,插入交易明细表报错");
				}		
			}
			if(map.get("dredgeService") != null){
				try{
					dredgeServiceDao.add(dredgeService);
				}catch(Exception e){
					bool=false;
					logger.error("开通短信服务,插入开通服务表报错");
				}
			}
			if(map.get("serviceHistory") != null){
				try{
					serviceHistory.setDserviceId(dredgeService.getId());
					serviceHistory.setPayId(payment.getId());
					serviceHistoryDao.add(serviceHistory);
				}catch(Exception e){
					bool=false;
					logger.error("开通短信服务,插入开通服务表报错");
				}

			}
		}else if (StringUtils.equals("5", type)) {
			if(map.get("payment") != null){
				try{
					paymentDao.add(payment);
				}catch(Exception e){
					bool=false;
					logger.error("开通短信、插入交易明细,插入交易明细表报错");
				}		
			}
			if(map.get("dredgeService") != null){
				try{
					dredgeServiceDao.edit(dredgeService);
				}catch(Exception e){
					bool=false;
					logger.error("开通短信服务,插入开通服务表报错");
				}
			}
			if(map.get("serviceHistory") != null){
				try{
					serviceHistory.setDserviceId(dredgeService.getId());
					serviceHistory.setPayId(payment.getId());
					serviceHistoryDao.add(serviceHistory);
				}catch(Exception e){
					bool=false;
					logger.error("开通短信服务,插入开通服务表报错");
				}

			}
		}
		return bool;
	}

	@Override
	public boolean insertBatch(List<DredgeService> list) {
		return dredgeServiceDao.insertBatch(list);
	}

	/**
	 * 更新提醒标识 以及低于余额，短信不足条数 服务首页点击保存用到,批量更新
	 */

	@Override
	public boolean updateBatch(List<DredgeService> list) {
		if (CollectionUtils.isEmpty(list)) {
			return false;
		}
		
		AccountUser accountUser = accountUserDao.getAccountUserByUserId(list
				.get(0).getUserId());// 得到账户信息

		// 1)创建账户
		if (accountUser == null || accountUser.getUserId() == null) {
			AccountUser createAccountUser = new AccountUser();
			createAccountUser.setUserId(list.get(0).getUserId());
			accountUserDao.add(createAccountUser);
			logger.info("用户ID：" + LogInfoEnum.PARAM_EMPTY.getValue());
			// 2)插入免费的服务

			List<PayService> pay_list = payServiceDao.getFreeServiceList();// 查询免费的服务
			if (CollectionUtils.isEmpty(pay_list)) {
				logger.info("没有免费的服务");
			} else {
				List<DredgeService> dredgeFreeServices = new ArrayList<DredgeService>();
				for (PayService payService : pay_list) {
					DredgeService dredgeService = new DredgeService();
					dredgeService.setRemark(payService.getName());
					dredgeService.setFlag(PayEnumConstants.SERVICEFLAG.ENABLED.getValue());
					dredgeService.setServiceId(payService.getId());
					dredgeService.setUserId(accountUser.getUserId());
					dredgeFreeServices.add(dredgeService);
				}
				dredgeServiceDao.insertBatch(dredgeFreeServices);
			}
			// 4)记下日志
			Logs logs = new Logs();
			logs.setOperName("服务插入账号表");
			logs.setOperType(PayEnumConstants.OPERTYPE.OTHER.getValue());
			logs.setUserId(list.get(0).getUserId());
			logs.setRemark("服务管理首页点击修改时,账户没有存在、新增账号");
			logsDao.add(logs);
		}

		// 后期改为批量更新
		if (CollectionUtils.isNotEmpty(list)) {
			for (DredgeService dredgeService : list) {
				dredgeServiceDao.updateDredgeserviceBalanceById(dredgeService);
			}
		}
		return true;
		// return dredgeServiceDao.updateBatch(list);
	}

	/**
	 * 开通的服务列表 注：查询状态为1启用 定时器用到
	 * 
	 * @return List<T>
	 */
	@Override
	public List<T> getOpenserviceTimeList() {
		return (List<T>) dredgeServiceDao.getOpenserviceTimeList();
	}
	
	/**
	 * 判断用户是否开通了某项服务
	 * @param map(userId,serviceName)
	 * @return Integer
	 */
	public boolean getServiceByUserIdAndServiceName(Map map)
	{
		return dredgeServiceDao.getServiceByUserIdAndServiceName(map);
	}

	@Override
	public Integer getDredgeserviceListCount(Map map) {
		// TODO Auto-generated method stub
		return dredgeServiceDao.getDredgeserviceListCount(map);
	}

	@Override
	public List<T> getOpenserviceListByName(Integer userid,List<Integer> list) {
		List<PayService> freeServiceList=payServiceDao.getFreeServiceList();
        Map freemap = new HashMap();		
        freemap.put("userId", userid);
		if(CollectionUtils.isNotEmpty(freeServiceList)){
			List<Integer> serviceIdList=new ArrayList<Integer>();//存放已经开通服务的id
			for(PayService payService:freeServiceList){
				serviceIdList.add(payService.getId());
			}
			for(Integer serviceId : list){
				serviceIdList.add(serviceId);
			}
			freemap.put("serviceIdList", serviceIdList);
		}
		return (List<T>) dredgeServiceDao.getOpenserviceList(freemap);
	}

}
