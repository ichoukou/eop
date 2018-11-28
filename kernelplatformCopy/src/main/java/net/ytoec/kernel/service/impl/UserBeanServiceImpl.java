/**
 * 
 */
package net.ytoec.kernel.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dao.UserThreadContractDao;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserBean;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.dataobject.UserThreadContract;
import net.ytoec.kernel.service.UserBeanService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.service.UserThreadService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 我的客户业务处理
 * @author wangyong
 * @2012-01-05
 */
@Service
@Transactional
public class UserBeanServiceImpl<T extends UserBean> implements UserBeanService<T> {

	@Inject
	private UserService<User> userService;
	
	@Inject
	private UserThreadService<UserThread> userThreadService;
	@Inject
	private UserThreadContractDao<UserThreadContract> userThreadContractDao;
	
	private static Logger logger=LoggerFactory.getLogger(UserBeanServiceImpl.class);
	
	public List<T> getUserList(String switchEccount,String siteCode, String userCode, String userName, String userState, Pagination pagination, boolean flag){
		if(siteCode==null || ("").equals(siteCode.trim())){
			return null;
		}
		List<UserThread> uThreadList = userThreadService.searchUsersBySiteCodeAndName(switchEccount,siteCode, userCode, userState, userName, pagination, flag);
		if(uThreadList!=null && uThreadList.size()>0){
			List<UserBean> userBeanList = new ArrayList<UserBean>();
			for(UserThread ut:uThreadList){
				UserBean userBean = new UserBean();
				userBean.setUserThreadId(ut.getId());
				userBean.setUserCode(ut.getUserCode());
				userBean.setUserName((StringUtils.isBlank(ut.getUserName()) || StringUtils.equals(ut.getUserName(), "null")) ? "":ut.getUserName() );
				userBean.setSwitchEccount(ut.getSwitchEccount());
				List<User> userList = userService.searchUsersByCodeTypeState(ut.getUserCode(), "1", userState);
				if(userList!=null && userList.size()>0){
					Set<String> loginName = new HashSet<String>();
					for(User u:userList){
						if(u.getUserName()!=null && !(u.getUserName().equals("")))
							loginName.add(u.getUserName());
						else
							loginName.add("暂无账号名称");
					}
					userBean.setLoginName(loginName);
					userBean.setTelephone(userList.get(0).getMobilePhone());
					userBean.setPhone(userList.get(0).getTelePhone());
					String address="";
					if(userList.get(0).getAddressProvince()!=null)
						address+=userList.get(0).getAddressProvince();
					if(userList.get(0).getAddressCity()!=null)
						address+=userList.get(0).getAddressCity();
					if(userList.get(0).getAddressDistrict()!=null)
						address+=userList.get(0).getAddressDistrict();
					if(userList.get(0).getAddressStreet()!=null)
						address+=userList.get(0).getAddressStreet();
					userBean.setAddress(address);
				}
				
//				if("1".equals(userState)){//已经绑定的直客才能对"管理运费模板"可操作
					List<UserThreadContract> utcs = userThreadContractDao.searchContractByConractAreaId(ut.getId(),"2");
					if(utcs != null && utcs.size() != 0){
						userBean.setIsContractUserFlg("isContractUser");//表示直客被分配给了承包区
					}
//				}else{//未绑定的直客不能对"管理运费模板"操作
//					userBean.setIsContractUserFlg("isContractUser");
//				}
				
				userBeanList.add(userBean);
			}
			return (List<T>)userBeanList;
		}else{
			return null;
		}
	}
	
	@Override
	public int countUserList(String switchEccount,String siteCode, String userCode, String userName, String userState, Pagination pagination, boolean flag){
		if(siteCode==null || ("").equals(siteCode.trim())){
			return 0;
		}
		List<UserThread> uThreadList = userThreadService.searchUsersBySiteCodeAndName(switchEccount,siteCode, userCode, userState, userName, pagination, false);
		if(uThreadList!=null && uThreadList.size()>0){
			int size = uThreadList.size();
			return size;
		}else
			return 0;
	}

	@Override
	public List<T> getContractUserList(String switchEccount, List<Integer> ids, Pagination pagination,String userState, boolean flag) {
		List<UserThread> uThreadList = userThreadService.getContractUserList(switchEccount,ids, pagination,userState,flag);
		if(uThreadList!=null && uThreadList.size()>0){
			List<UserBean> userBeanList = new ArrayList<UserBean>();
			for(UserThread ut:uThreadList){
				UserBean userBean = new UserBean();
				userBean.setUserThreadId(ut.getId());
				userBean.setUserCode(ut.getUserCode());
				userBean.setUserName(ut.getUserName());
				userBean.setSwitchEccount(ut.getSwitchEccount());
				List<User> userList = userService.searchUsersByCodeTypeState(ut.getUserCode(), "1", userState);
				if(userList!=null && userList.size()>0){
					Set<String> loginName = new HashSet<String>();
					for(User u:userList){
						if(u.getUserName()!=null && !(u.getUserName().equals("")))
							loginName.add(u.getUserName());
						else
							loginName.add("暂无账号名称");
					}
					userBean.setLoginName(loginName);
					userBean.setTelephone(userList.get(0).getMobilePhone());
					userBean.setPhone(userList.get(0).getTelePhone());
					String address="";
					if(userList.get(0).getAddressProvince()!=null)
						address+=userList.get(0).getAddressProvince();
					if(userList.get(0).getAddressCity()!=null)
						address+=userList.get(0).getAddressCity();
					if(userList.get(0).getAddressDistrict()!=null)
						address+=userList.get(0).getAddressDistrict();
					if(userList.get(0).getAddressStreet()!=null)
						address+=userList.get(0).getAddressStreet();
					userBean.setAddress(address);
				}
				/**add by yuyuezhong 2012-08-30 start*/
//				if("1".equals(userState)){//已经绑定的直客才能对"管理运费模板"可操作
//					List<UserThreadContract> utcs = userThreadContractDao.searchContractByConractAreaId(ut.getId(),"2");
//					if(utcs != null && utcs.size() != 0){
//						userBean.setIsContractUserFlg("isContractUser");
//					}
//				}else{//未绑定的直客不能对"管理运费模板"操作
//					userBean.setIsContractUserFlg("isContractUser");
//				}
				/**add by yuyuezhong 2012-08-30 end*/
				userBeanList.add(userBean);
			}
			return (List<T>)userBeanList;
		}else{
			return null;
		}
	}

	@Override
	public int countContractUserList(String switchEccount,List<Integer> ids, Pagination pagination,
			String userState, boolean flag) {
		List<UserThread> uThreadList = userThreadService.getContractUserList(switchEccount, ids, pagination, userState, false);
		if(uThreadList!=null && uThreadList.size()>0){
			int size = uThreadList.size();
			return size;
		}else
			return 0;
	}
}
