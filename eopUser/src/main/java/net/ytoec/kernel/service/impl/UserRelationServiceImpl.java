/**
 * net.ytoec.kernel.service.impl
 * UserRelationServiceImpl.java
 * 2012-9-26上午09:57:29
 * @author wangyong
 */
package net.ytoec.kernel.service.impl;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dao.UserRelationDao;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserCustom;
import net.ytoec.kernel.dataobject.UserRelation;
import net.ytoec.kernel.service.UserCustomService;
import net.ytoec.kernel.service.UserRelationService;
import net.ytoec.kernel.service.UserService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户关联关系业务实现
 * @author wangyong
 * 2012-9-26
 */
@Service
@Transactional
public class UserRelationServiceImpl<T extends UserRelation> implements UserRelationService<T> {
	
	private static final Logger logger = LoggerFactory.getLogger(UserRelationServiceImpl.class);
	
	@Inject
	private UserRelationDao<UserRelation> userRelationDao;
	
	@Inject
	private UserService<User> userService;
	
	@Inject
	private UserCustomService<UserCustom> userCustomService;

	@Override
	public boolean add(T entity) {
		return userRelationDao.add(entity);
	}

	@Override
	public boolean edit(T entity) {
		if(entity!=null)
			return userRelationDao.edit(entity);
		return false;
	}

	@Override
	public boolean remove(T entity) {
		//不提供根据id删除数据的方法，删除这里的数据只能采用cancelRelation()方法
		return false;
	}

	@Override
	public T get(T entity) {
		if(entity!=null)
			return (T)userRelationDao.get(entity);
		return null;
	}

	@Override
	public T getById(Integer id) {
		if(id!=null) {
			UserRelation ur = new UserRelation();
			ur.setId(id);
			return get((T)ur);
		}
		return null;
	}

	@Override
	public List<T> searchByUserId(int userId) {
		return (List<T>)userRelationDao.searchByUserId(userId);
	}

	@Override
	public List<T> searchRelation(Integer userIdOne, Integer userIdTwo) {
		if(userIdOne!=null && userIdTwo!=null) {
			UserRelation ur = new UserRelation();
			ur.setUserId(userIdOne);
			ur.setRelatedUserId(userIdTwo);
			return (List<T>)userRelationDao.searchRelation(ur);
		}
		return null;
	}

	@Override
	public boolean cancelRelation(User userOne, User userTwo) {
		if(userOne!=null && userTwo!=null) {
			UserRelation ur = new UserRelation();
			ur.setUserId(userOne.getId());
			ur.setRelatedUserId(userTwo.getId());
			boolean result = userRelationDao.cancelRelation(ur);
			if(result) {
				/**
				 *	删除个性化配置中userOne对userTwo及userTwo对userOne的数据
				*/
				//第一步：删除userOne及其子账号对userTwo的店铺关联
				UserCustom ucOne=new UserCustom();
				ucOne.setUserName(userOne.getUserName());
				ucOne.setCustomerId(userTwo.getTaobaoEncodeKey());
				ucOne.setType("1");
		        userCustomService.remove(ucOne);
		        List<User> userOneChildren = userService.getUserByParentId(userOne.getId());
		        for(User uChildren : userOneChildren) {
		        	if(StringUtils.isNotEmpty(uChildren.getUserName())) {
		        		UserCustom ucOneChildren=new UserCustom();
		        		ucOneChildren.setUserName(uChildren.getUserName());
		        		ucOneChildren.setCustomerId(userTwo.getTaobaoEncodeKey());
		        		ucOneChildren.setType("1");
			            userCustomService.remove(ucOneChildren);
		        	}
		        }
		        //第二步：删除userTwo及其子账号对userOne的店铺关联
		        UserCustom ucTwo=new UserCustom();
		        ucTwo.setUserName(userTwo.getUserName());
		        ucTwo.setCustomerId(userOne.getTaobaoEncodeKey());
		        ucTwo.setType("1");
		        userCustomService.remove(ucTwo);
		        List<User> userTwoChildren = userService.getUserByParentId(userTwo.getId());
		        for(User uChildren : userTwoChildren) {
		        	if(StringUtils.isNotEmpty(uChildren.getUserName())) {
		        		UserCustom ucTwoChildren=new UserCustom();
		        		ucTwoChildren.setUserName(uChildren.getUserName());
		        		ucTwoChildren.setCustomerId(userOne.getTaobaoEncodeKey());
		        		ucTwoChildren.setType("1");
			            userCustomService.remove(ucTwoChildren);
		        	}
		        }
		        return true;
			}
		}
		return false;
	}

}
