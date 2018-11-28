package net.ytoec.kernel.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dao.UserDao;
import net.ytoec.kernel.dao.UserThreadContractDao;
import net.ytoec.kernel.dao.UserThreadDao;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.dataobject.UserThreadContract;
import net.ytoec.kernel.service.UserThreadContractService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 新增子账户操作
 * @author yuyuezhong
 */
@Service
@Transactional
public class UserThreadContractServiceImpl<T extends UserThreadContract> implements UserThreadContractService<T> {

	private static final Logger logger=LoggerFactory.getLogger(UserThreadContractServiceImpl.class);
	
	@Inject
	private UserThreadContractDao<T> dao;
	
	@Inject
    private UserDao<T>          userDao;
	
	@Inject
	private UserThreadDao<UserThread>     userThreadDao;

	@Override
	public boolean insertContract(T entity) {
		boolean flag = true;
		flag = dao.insertContract(entity);
		
		return flag;
	}

	@Override
	public List<T> searchContractsBysiteId(String siteId,String accountType) {
		UserThreadContract utc = new UserThreadContract();
		utc.setSiteId(Integer.parseInt(siteId));
		utc.setAccountType(accountType);
		return dao.searchContractsBysiteId(utc);
	}

	@Override
	public boolean deleteContractByConractAreaId(String addUserName, String conractAreaId,String site) {
		boolean flag = true;
		/**如果删除网点下的直客时，同时删除承包区子账号的直客；
		 * 如果删除承包区子账号的直客，那么网点下的直客不删除
		 */
		UserThreadContract utc = new UserThreadContract();
		utc.setConractAreaId(Integer.parseInt(conractAreaId));
		utc.setAddUserName(addUserName);
		flag = dao.deleteContractByConractAreaId(utc);
		
		//根据userThread表里的主键来查询直客表数据
		UserThread ut = userThreadDao.getUserById(Integer.parseInt(conractAreaId));
		List<String> userCodes = new ArrayList<String>();
		userCodes.add(ut.getUserCode());
		
		Map map = new HashMap();
    	map.put("userCodes", userCodes);
    	map.put("userName", site);
		userDao.updateSiteByUserCode(map);
		return flag;
	}

	@Override
	public List<T> getContractersByUserNameAndType(String username, String accountType) {
		
		UserThreadContract utc = new UserThreadContract();
		utc.setAddUserName(username);
		utc.setAccountType(accountType);
		return dao.getContractersByUserNameAndType(utc);
	}

	@Override
	public boolean updateContractByAddUserName(UserThreadContract entity) {
		boolean flag = true;
		try {
			dao.updateContractByAddUserName(entity);
			
			//已经更新的客户账号类型的用户列表
			UserThreadContract entityTemp = new UserThreadContract();
			entityTemp.setAddUserName(entity.getAddUserName());
			entityTemp.setAccountType(null);
			List<UserThreadContract> contractsList = (List<UserThreadContract>) dao.getContractersByUserNameAndType(entityTemp);
			
			//根据userThread表里的主键来查询直客表数据
			List<String> userCodes = new ArrayList<String>();
			if(contractsList != null && contractsList.size() > 0){
				for(UserThreadContract utc :contractsList){
					UserThread ut = userThreadDao.getUserById(utc.getConractAreaId());
					userCodes.add(ut.getUserCode());
				}
			}
			
			Map map = new HashMap();
			if("2".equals(entity.getAccountType())){//更新承包区客户是卖家绑定的客户的时候 ，卖家用户的site值为当前子账号名
				map.put("userCodes", userCodes);
		    	map.put("userName", entity.getAddUserName());
			}else{                                  //更新承包区客户被取消，之前更新的site值恢复为原来网点值
				map.put("userCodes", userCodes);
		    	map.put("userName", entity.getSite());
			}
			userDao.updateSiteByUserCode(map);
			
		}catch(Exception e){
			logger.error("update has error");
			flag = false;
		}
		return flag;
	}

	@Override
	public List<T> searchContractByConractAreaId(Integer id, String accountType) {
		return dao.searchContractByConractAreaId(id, accountType);
	}

	@Override
	public List<T> searchContractByConractAreaIdAndUserName(Integer id,
			String userName) {
		UserThreadContract utc = new UserThreadContract();
		utc.setConractAreaId(id);
		utc.setAddUserName(userName);
		return dao.searchContractByConractAreaIdAndUserName(utc);
	}

}
