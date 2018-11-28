package net.ytoec.kernel.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.UserDao;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.mapper.UserMapper;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

/**
 * 用户数据库操作
 * 
 * @author ChenRen
 * @date 2011-7-25
 */
@Repository
// @SuppressWarnings("all")
public class UserDaoImpl<T extends User> implements UserDao<T> {

	private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
	
	@Inject
	private UserMapper<User> mapper;

	public boolean add(T entity) {
		boolean flag = true;
		try {
			String userType = entity.getUserType();
			if ("11".equals(userType) || "12".equals(userType)
					|| "13".equals(userType)) {
				// 子账号不复制这些字段值;
				entity.setTaobaoEncodeKey("");
				entity.setUserCode("");
				entity.setShopAccount("");
				// entity.setShopName("");
			}
			mapper.add(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}

		return flag;
	}

	public boolean addDeptUser(T entity) {
		boolean flag = true;
		try {
			String userType = entity.getUserType();
			if ("11".equals(userType) || "12".equals(userType)
					|| "13".equals(userType)) {
				// 子账号不复制这些字段值;
				entity.setTaobaoEncodeKey("");
				entity.setUserCode("");
				entity.setShopAccount("");
				// entity.setShopName("");
			}
			mapper.addDeptUser(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}

		return flag;
	}

	@SuppressWarnings("unchecked")
	public T get(T entity) {
		if (entity == null || entity.getId() == null) {
			return null;
		}
		try {
			entity = (T) mapper.get(entity);
			if(entity==null) {
				return null;
			}
			String userType = entity.getUserType();
			if ("11".equals(userType) || "12".equals(userType)
					|| "13".equals(userType)) {
				T parent = this.getUserById(entity.getParentId());
				entity.setTaobaoEncodeKey(parent.getTaobaoEncodeKey());
				entity.setUserCode(parent.getUserCode());
				entity.setShopAccount(parent.getShopAccount());
				// entity.setShopName(parent.getShopName());
			}
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return entity;
	}

	public boolean edit(T entity) {

		boolean flag = false;

		try {
			String userType = entity.getUserType();
			if ("11".equals(userType) || "12".equals(userType)
					|| "13".equals(userType)) {
				// 子账号不复制这些字段值;
				entity.setTaobaoEncodeKey("");
				entity.setUserCode("");
				entity.setShopAccount("");
				// entity.setShopName("");
			}
			mapper.edit(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}

		return flag;
	}

	public boolean modifyUser(T entity) {
		boolean flag = false;
		try {
			mapper.modify(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	public boolean delUser(T entity) {
		return remove(entity);
	}

	@SuppressWarnings("unchecked")
	public List<T> searchUsers(Map<String, Object> map) {

		List<T> list = null;

		try {
			list = (List<T>) mapper.searchUsers(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<T> getAllUserMap(Map<String, Object> map) {

		List<T> list = null;

		try {
			list = (List<T>) mapper.getAllUserMap(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<T> searchUsersByLikeName(T entity) {

		List<T> list = null;

		try {
			// 模糊查询
			list = (List<T>) mapper.searchUsersByLikeName(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<T> searchUsersByName(T entity) {

		List<T> list = null;

		try {
			// 完全匹配查询
			list = (List<T>) mapper.searchUsersByUserName(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<T> searchUsersByShopAccount(T entity) {

		List<T> list = null;
		try {
			// 完全匹配查询
			list = (List<T>) mapper.searchUsersByShopAccount(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<T> searchUserByNameAndPwd(T entity) {

		List<T> list = null;

		try {
			list = (List<T>) mapper.searchUserByNameAndPwd(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<T> searchUsersBySite(T entity) {

		List<T> list = null;

		try {
			list = (List<T>) mapper.searchUsersBySite(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<T> searchUsersBySiteAndUserType(T entity) {
		List<T> list = null;
		try {
			list = (List<T>) mapper.searchUsersBySiteAndUserType(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> searchUsersByCodeTypeState(T entity) {
		List<T> list = null;
		try {
			list = (List<T>) mapper.searchUsersByCodeTypeState(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public T searchUsersByUserName(T entity) {
		User user = new User();
		try {
			List<User> users = mapper.searchUsersByUserName(entity);
			if (users == null || users.isEmpty()) {
				return (T) user;
			}
			user = users.get(0);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return (T) user;
	}

	@SuppressWarnings("unchecked")
	public T searchUsersByLoginName(T entity) {
		User user = new User();
		try {
			if (mapper.searchUsersByUserName(entity).size() > 0)
				user = mapper.searchUsersByUserName(entity).get(0);
			else
				user = null;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return (T) user;
	}

	@SuppressWarnings("unchecked")
	public List<T> searchUsersByCode(T entity) {
		List<T> list = null;
		try {
			list = (List<T>) mapper.searchUsersByCode(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			return Collections.EMPTY_LIST;
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<T> searchUsersByDeptCode(T entity) {
		List<T> list = null;
		try {
			list = (List<T>) mapper.searchUsersByDeptCode(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<T> searchUsersByUserType(T entity) {
		List<T> list = null;
		try {
		    list = (List<T>) mapper.searchUsersByUserType(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<T> getVipUserByPostId(Integer postId) {
		List<T> list = null;
		try {
			list = (List<T>) mapper.getVipUserByPostId(postId);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<T> searchUsersByTaoBao(String taobao) {
		List<T> list = null;
		try {
			list = (List<T>) mapper.searchUsersByTaoBao(taobao);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<T> searchUsersByUserSource(String userSource) {
		List<T> list = null;
		try {
			list = (List<T>) mapper.searchUsersByUserSource(userSource);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<T> loginUserCheck(T entity) {

		List<T> list = null;
		if (entity == null || StringUtils.isEmpty(entity.getUserName())
				|| StringUtils.isEmpty(entity.getUserPassword())) {
			return list;
		}
		try {
			list = (List<T>) mapper.loginUserCheck(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<T> getSiteByVipId(Integer id) {
		List<T> list = null;
		try {
			list = (List<T>) mapper.getSiteByVipId(id);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return list;
	}

	@Override
	public boolean remove(T entity) {
		boolean flag = false;
		try {
			mapper.remove(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}

		return flag;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T getUserById(Integer id) {
		// TODO Auto-generated method stub
		User u = new User();
		u.setId(id);
		return get((T) u);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getAllUser() {
		// TODO Auto-generated method stub
		List<User> list = mapper.getAllUser();
		if (list == null || list.size() == 0) {
			return new ArrayList<T>();
		}
		return (List<T>) list;
	}

	@Override
	public List<T> getUserByParentId(Integer pid) throws DataAccessException {
		Map map = new HashMap();
		map.put("parentId", pid);
		return getUserByParentId(map);
	}

	@Override
	public List<T> getUserByParentId(Map map) throws DataAccessException {
		List<T> list = null;
		try {
			list = (List<T>) mapper.getUserByParentId(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			return Collections.EMPTY_LIST;
		}

		return list;
	}

	@Override
	public T getUserByCustomerId(String customerId) {
		T entity = (T) new User();
		try {
			entity = (T) mapper.getUserByCustomerId(customerId);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return entity;
	}

	@Override
	public List<T> getUsersByCustomerIds(List<String> customerIds) {

		if (customerIds == null || customerIds.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		map.put("customerIds", customerIds);
		List<T> users = (List<T>) mapper.getUsersByCustomerIds(map);
		return users;
	}

	@Override
	public T getUserByClientId(String clientId) {
		// TODO Auto-generated method stub
		return (T) mapper.getUserByClientId(clientId);
	}

	@Override
	public List<T> searchDepotHosting(Map map) {
		return (List<T>) mapper.searchDepotHosting(map);
	}

	@Override
	public List<T> getUsersByRemark(Map<String, String> map) {
		if (map == null || map.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		return (List<T>) mapper.getUsersByRemark(map);
	}

	@Override
	public List<T> getRepeatSiteList() {
		return (List<T>) mapper.getRepeatSiteList();
	}

	@Override
	public List<T> searchUsersByCodeAndType(T entity) {
		return (List<T>) mapper.searchUsersByCodeAndType(entity);
	}

	@Override
	public boolean updateSiteByUserCode(Map map) {
		boolean flag = false;
		try {
			mapper.updateSiteByUserCode(map);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean updateUserPrint(Map map) {
		boolean flag = false;
		try {
			mapper.updateUserPrint(map);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public List<T> getisPrintFlag(Map map) {

		return (List<T>) mapper.getisPrintFlag(map);
	}

	@Override
	public boolean updateUserPrintNav(Map map) {
		boolean flag = false;
		try {
			mapper.updateUserPrintNav(map);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public T getUserByUserName(String userName) {
		// TODO Auto-generated method stub
		return (T) mapper.getUserByUserName(userName);
	}

	@Override
	public List<User> searchUserByTBKeyAndUserNameAndShopAccount() {
		// TODO Auto-generated method stub
		List list = null;
		try {
			list = mapper.searchUserByTBKeyAndUserNameAndShopAccount();
		} catch (Exception e) {
			// TODO: handle exception
		}

		return list;
	}

	@Override
	public List<T> getUserByUserStateAndType(Map<String, String> map) {

		return (List<T>) mapper.getUserByUserStateAndType(map);
	}

	@Override
	public T getUserByShopName(String shopName) {
		return (T) mapper.getUserByShopName(shopName);
	}
	
	@Override
	public T getUserByShopNameOnly(String shopName) {
		List<T> list = null;
		try {
			list = (List<T>)mapper.getUsersByShopName(shopName);
			if(list!=null&&list.size()>0) {
				return list.get(0);
			}
		}catch(Exception e) {
			logger.error("getUserByShopNameOnly:E-"+e.getMessage());
		}
		return null;
	}
	
	@Override
	public List<T> getUsersByShopName(String shopName) {
		List<T> list = null;
		try {
			list = (List<T>)mapper.getUsersByShopName(shopName);
		}catch(Exception e) {
			logger.error("getUsersByShopName:E-"+e.getMessage());
		}
		return list;
	}

	/**
	 * 根据shopNameorUserName查找用户(服务管理模块用到) pararms :shopUserName
	 * 
	 * @return List
	 */
	@Override
	public List<T> getUserByShopNameorUserName(String shopUserName) {
		// TODO Auto-generated method stub
		return (List<T>) mapper.getUserByShopNameorUserName(shopUserName);
	}

	/**
	 * 根据mobilePhone查找用户(服务管理模块用到) pararms :mobilePhone
	 * 
	 * @return List
	 */
	@Override
	public List<T> getUserByMobilePhone(String mobilePhone) {
		// TODO Auto-generated method stub
		return (List<T>) mapper.getUserByMobilePhone(mobilePhone);
	}

	/**
	 * 通过用户类型查询用户列表(服务管理模块用到)
	 * 
	 * @param List
	 *            <String> userTypes
	 * @return List
	 */
	public List<T> getUserListByUserTypes(List<String> userTypes) {
		if(userTypes==null||userTypes.size()<=0)
			return null;
		
		Map<Object,Object> map=new HashMap<Object,Object>();		
		List<String> userTypeList=new ArrayList<String>();
		String userType=null;
		
		//网点，承包区,卖家,业务账号在这里要进行特殊处理
		Iterator<String>  it=userTypes.iterator();
		while(it.hasNext())
		{
			userType=it.next();
			
			if(userType.indexOf("网点")>=0){
				map.put("site", "2;网点");
				continue;
			}
			if(userType.indexOf("承包区")>=0){
				map.put("contractorAreas", "2;承包区");
				continue;
			}
			
			if(userType.indexOf("卖家")>=0){
				map.put("seller", "1;卖家");
				continue;
			}
			if(userType.indexOf("业务账号")>=0){
				map.put("businessAccount", "1;业务账号");
				continue;
			}
			
			if(userType.indexOf("formName")>=0){
				map.put("formName", userType.substring(userType.indexOf(':')+1));
				continue;
			}
			
			userTypeList.add(userType);
		}
		if(userTypeList.size()>0)
		    map.put("userTypeList", userTypeList);
		
		return (List<T>) mapper.getUserListByUserTypes(map);
	}

	@Override
	public boolean initBranch(String userName) {
		// TODO Auto-generated method stub
		Integer i = mapper.initBranch(userName);

		if (i > 0) {
			return true;
		}
		return false;
	}

	@Override
	public T getUserByUserCode(String userCode) {
		
		return (T) mapper.getUserByUserCode(userCode);
	}

	@Override
	public boolean updateTaobaoEncodeKeyById(Map map) {
		boolean flag = false;
		try {
			mapper.updateTaobaoEncodeKeyById(map);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.UPDATE_TAOBAOENCODEKEY_BY_ID.getValue(), e);
			flag = false;
		}

		return flag;
	}

	@Override
	public String searchSiteMailByUserCode(String userCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int searchTotal(Map<String, Object> map) {
		int total=0;
		try {
			total=mapper.searchTotal(map);
		} catch (Exception e) {
			logger.error("统计用户表出错", e);
		}
		return total;
	}
}
