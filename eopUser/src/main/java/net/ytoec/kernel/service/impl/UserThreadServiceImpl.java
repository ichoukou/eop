package net.ytoec.kernel.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dao.UserThreadDao;
import net.ytoec.kernel.dataobject.QueryUserCondition;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.service.UserThreadService;
import net.ytoec.kernel.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 从金刚系统同步过来的用户信息业务实现
 * @author wangyong
 */
@Service
@Transactional
public class UserThreadServiceImpl<T extends UserThread> implements UserThreadService<T> {

	private static final Logger logger=LoggerFactory.getLogger(UserThreadServiceImpl.class);

	// VIP用户激活判断编码状态的常量字段
	public static final String CHECK_USERCODE_NOTEXIST 	= "编码不存在!";
	public static final String CHECK_USERCODE_NOTUNIQUEN 	= "编码不唯一!";
	public static final String CHECK_USERCODE_ACTIVED 		= "编码已激活!";
	public static final String CHECK_USERCODE_TOBEACTIVE 	= "TBA";	// 待激活
	public static final String CHECK_USERCODE_UNKNOWERROE  = "未知错误!";
	
	@Inject
	private UserThreadDao<T> dao;

	@Override
	public boolean addUser(T entity) {
		if (entity == null) {
			logger.error("entity is empity");
			return false;
		}
		return dao.addUser(entity);
	}

	@Override
	public T getUserById(Integer id) {
		return dao.getUserById(id);
	}

	@Override
	public List<T> getAllUser() {
		return dao.getAllUser();
	}

	@Override
	public boolean editUser(T entity) {
		if (entity == null) {
			logger.error("entity is empity");
			return false;
		}
		return dao.editUser(entity);
	}
	
	@Override
	public boolean delUserById(Integer id) {
		T entity = dao.getUserById(id);
		if (entity == null) {
			logger.error("entity is empity");
			return false;
		}
		return dao.delUser(entity);
	}

	@Override
	public String checkUserCode(T entity) {
		List<T> list = searchUsersByCode(entity);
		String msg = "false";
		
		if(list != null) {
			if(list.size() > 0) {
				msg = "true";
			}
		}
		/*
		int size = list.size();
		// 编码不存在
		if(size == 0) {
			msg = CHECK_USERCODE_NOTEXIST + "<br> [userCode: " + entity.getUserCode() + "]";
		}
		// 编码不唯一
		else if(size > 1) {
			msg = CHECK_USERCODE_NOTUNIQUEN + "<br> [userCode: " + entity.getUserCode()
				+ "; size: " + size + "]";
		}
		else if(size == 1) {
			T e = list.get(0);
			// 编码已激活
			if(!"TBA".equalsIgnoreCase(e.getUserState())) {
				msg = CHECK_USERCODE_ACTIVED + "<br> [userCode: " + entity.getUserCode() + "]";
			}
			// 编码未激活
			if("TBA".equalsIgnoreCase(e.getUserState())) {
				msg = CHECK_USERCODE_TOBEACTIVE;
			}
		}
		// 未知错误
		else {
			msg = CHECK_USERCODE_UNKNOWERROE + "<br> [userCode: " + entity.getUserCode() + "]";;
		}
		*/
		return msg;
	} // checkUserCode
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> searchUsersByCode(T entity) {
		if (entity == null) {
			logger.error("entity is empity");
			return Collections.EMPTY_LIST;
		}
		if (StringUtils.isEmpty(entity.getUserCode() ) ) {
			logger.error("UserCode is empity");
			return Collections.EMPTY_LIST;
		}
		
		return dao.searchUsersByCode(entity);
	}
	
	@SuppressWarnings("unchecked")
    @Override
	public List<T> searchUsersBySite(String siteCode) {
		if (siteCode == null) {
			logger.error("siteCode is empity");
			return Collections.EMPTY_LIST;
		}
		if (StringUtils.isEmpty(siteCode)) {
			logger.error("UserCode is empity");
			return Collections.EMPTY_LIST;
		}
		UserThread ut = new UserThread();
		ut.setSiteCode(siteCode);
		return dao.searchUsersBySite((T)ut);
	}
	
	@SuppressWarnings("unchecked")
    @Override
	public List<T> searchAllUsersBySite(String siteCode) {
		if (siteCode == null) {
			logger.error("siteCode is empity");
			return Collections.EMPTY_LIST;
		}
		if (StringUtils.isEmpty(siteCode)) {
			logger.error("UserCode is empity");
			return Collections.EMPTY_LIST;
		}
		UserThread ut = new UserThread();
		ut.setSiteCode(siteCode);
		return dao.searchAllUsersBySite((T)ut);
	}

	/**该方法被删除，已经被重载方法searchUsersBySiteCodeAndName(
	 * 					String switchEccount, String siteCode, String userCode, String userState, 
	 * 					String userName, Pagination pagination, boolean flag)代替*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<T> searchUsersBySiteCodeAndName(String siteCode, String userCode, String userState, String userName, Pagination pagination, boolean flag){
		if (siteCode == null || siteCode.trim().equals("")) {
			return null;
		}
		Map map = new HashMap();
		map.put("switchEccount", null);
		if(flag){
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
		}
		map.put("siteCode", siteCode);
		if(userState!=null && !(userState.trim().equals("")))
			map.put("userState", userState);
		if(userCode!=null && !(userCode.trim().equals("")))
			map.put("userCode", userCode.trim());
		if(userName!=null && !(userName.trim().equals("")))
			map.put("userName", userName.trim());
			map.put("ids",null);
		return dao.searchUsersBySiteCodeAndName(map);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<T> searchUsersBySiteCodeAndName(String switchEccount, String siteCode, String userCode, String userState, String userName, Pagination pagination, boolean flag){
		if (siteCode == null || siteCode.trim().equals("")) {
			return null;
		}
		Map map = new HashMap();
		map.put("switchEccount", switchEccount);
		if(flag){
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
		}
		map.put("siteCode", siteCode);
		if(userState!=null && !(userState.trim().equals("")))
			map.put("userState", userState);
		if(userCode!=null && !(userCode.trim().equals("")))
			map.put("userCode", userCode.trim());
		if(userName!=null && !(userName.trim().equals("")))
			map.put("userName", userName.trim());
			map.put("ids",null);
		return dao.searchUsersBySiteCodeAndName(map);
	}
	
	/**该方法被删除，已经被重载方法searchUsersBySiteCodeAndNameAndNotId(String switchEccount, List<Integer> ids,
			String siteCode, String userCode, String userState,
			String userName, Pagination pagination, boolean flag)代替*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<T> searchUsersBySiteCodeAndNameAndNotId(List<Integer> ids,
			String siteCode, String userCode, String userState,
			String userName, Pagination pagination, boolean flag) {
		if (siteCode == null || siteCode.trim().equals("")) {
			return null;
		}
		Map map = new HashMap();
		map.put("switchEccount", null);
		if(flag){
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
		}
		map.put("siteCode", siteCode);
		if(userState!=null && !(userState.trim().equals("")))
			map.put("userState", userState);
		if(userCode!=null && !(userCode.trim().equals("")))
			map.put("userCode", userCode.trim());
		if(userName!=null && !(userName.trim().equals("")))
			map.put("userName", userName.trim());
		if(ids != null && ids.size() != 0){
			map.put("ids", ids);
		}
		return dao.searchUsersBySiteCodeAndName(map);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<T> searchUsersBySiteCodeAndNameAndNotId(String switchEccount, List<Integer> ids,
			String siteCode, String userCode, String userState,
			String userName, Pagination pagination, boolean flag) {
		if (siteCode == null || siteCode.trim().equals("")) {
			return null;
		}
		Map map = new HashMap();
		map.put("switchEccount", switchEccount);
		if(flag){
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
		}
		map.put("siteCode", siteCode);
		if(userState!=null && !(userState.trim().equals("")))
			map.put("userState", userState);
		if(userCode!=null && !(userCode.trim().equals("")))
			map.put("userCode", userCode.trim());
		if(userName!=null && !(userName.trim().equals("")))
			map.put("userName", userName.trim());
		if(ids != null && ids.size() != 0){
			map.put("ids", ids);
		}
		return dao.searchUsersBySiteCodeAndName(map);
	}

	@Override
	public T getByIdAndState(Integer id) {
		return dao.getByIdAndState(id);
	}
	
	//该方法已经被删除，被重载方法getContractUserList(String switchEccount, List<Integer> ids, Pagination pagination,String userState,boolean flag)代替
	@SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
	public List<T> getContractUserList(List<Integer> ids, Pagination pagination,String userState,boolean flag) {
		Map map = new HashMap();
		if(flag){
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
		}
		map.put("switchEccount", null);
		map.put("ids", ids);
		if(userState!=null && !(userState.trim().equals(""))){
			map.put("userState", userState);
		}
		return dao.getContractUserList(map);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
	public List<T> getContractUserList(String switchEccount, List<Integer> ids, Pagination pagination,String userState,boolean flag) {
		Map map = new HashMap();
		if(flag){
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
		}
		map.put("switchEccount", switchEccount);
		map.put("ids", ids);
		if(userState!=null && !(userState.trim().equals(""))){
			map.put("userState", userState);
		}
		return dao.getContractUserList(map);
	}

	public boolean updateUserThread(T entity) {
		if (entity == null) {
			logger.error("entity is empity");
			return false;
		}
		return dao.updateUserThread(entity);
	}
	public  List<T>  getisPrintFlag(T entity) {
		
		return dao.getisPrintFlag(entity);
	}
	
	@Override
	public List<T> getByState(String userState){
		if(userState!=null){
			return dao.getByState(userState);
		}else
			return null;
	}

    @Override
    public String generateUserCode(String siteCode) {
        T entity = generater(siteCode);
        return entity.getUserCode();
    }
	

    @Override
    public T generateUserThread(T entity) {
        if(StringUtils.isEmpty(entity.getSiteCode())){
            return null;
        }
        T tempT = generater(entity.getSiteCode());
        if(StringUtils.isNotEmpty(entity.getUserName())){
            tempT.setUserName(entity.getUserName());
        }
        dao.editUser(tempT);
        return tempT;
    }
    
    @SuppressWarnings("unchecked")
    private T generater(String siteCode) {
        T entity = (T) new UserThread();
        int randNum = 0;
        randNum = StringUtil.NextInt(1000000, 9999999);
        String tempUserCode = "YTO"+randNum;
        
        entity.setUserCode(tempUserCode);
        
        entity.setSiteCode(siteCode);
        entity.setUserState("TBA");
        entity.setUserName("客户"+randNum);
        entity.setCreateTime(new Date());
        entity.setSwitchEccount("0");
        dao.addUser(entity);
        entity.setUserCode("YT"+entity.getId());
        entity.setUserName("客户"+entity.getId());
        dao.editUser(entity);
        return entity;
    } 
    
    @Override
	public List<T> queryUserThread(QueryUserCondition quc) {
		if(quc!=null){
			return dao.queryUserThread(quc);
		}else
			return new ArrayList<T>();
	}

	@Override
	public int countUserThread(QueryUserCondition quc) {
		if(quc!=null){
			return dao.countUserThread(quc);
		}else
			return 0;
	}

	@Override
	public boolean updateIsCanDownloadByIds(List<String> list,int isCanDownload) {
		if(list!=null&& list.size()>0){
			Map map = new HashMap();
			map.put("ids", list);
			map.put("isCanDownload", isCanDownload);
			dao.updateIsCanDownloadByIds(map);
			return true;
		}else
			return false;
	} 
}
