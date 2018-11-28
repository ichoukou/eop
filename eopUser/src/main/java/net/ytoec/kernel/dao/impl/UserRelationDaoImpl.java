/**
 * net.ytoec.kernel.dao.impl
 * UserRelationDaoImpl.java
 * 2012-9-25下午04:55:22
 * @author wangyong
 */
package net.ytoec.kernel.dao.impl;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dao.UserRelationDao;
import net.ytoec.kernel.dataobject.UserRelation;
import net.ytoec.kernel.mapper.UserRelationMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

/**
 * @author wangyong
 * 2012-9-25
 */
@Repository
public class UserRelationDaoImpl<T extends UserRelation> implements UserRelationDao<T> {

	private static final Logger logger = LoggerFactory.getLogger(UserRelationDaoImpl.class);
	
	@Inject
	private UserRelationMapper<UserRelation> mapper;
	
	@Override
	public boolean add(T entity) {
		boolean flag = false;
		try{
			mapper.add(entity);
			flag = true;
		} catch(Exception e){
			logger.error("add userRelation error:"+e);
		}
		return flag;
	}

	@Override
	public boolean edit(T entity) {
		boolean flag = false;
		try{
			mapper.edit(entity);
			flag = true;
		} catch(Exception e){
			logger.error("edit userRelation error:"+e);
		}
		return flag;
	}

	@Override
	public boolean remove(T entity) {
		boolean flag = false;
		try{
			mapper.remove(entity);
			flag = true;
		} catch(Exception e){
			logger.error("remove userRelation error:"+e);
		}
		return flag;
	}

	@Override
	public T get(T entity) {
		return (T)mapper.get(entity);
	}

	@Override
	public List<T> searchByUserId(int userId) throws DataAccessException {
		if(userId<=0){
			logger.error("根据用户id获取该用户所关联的用户时用户id不合法");
		} else{
			return (List<T>)mapper.searchByUserId(userId);
		}
		return null;
	}

	@Override
	public List<T> searchRelation(T entity) throws DataAccessException {
		//entity对象里面必须要有userId和relatedUserId属性的值
		if(entity.getUserId()!=null && entity.getRelatedUserId()!=null) {
			return (List<T>)mapper.searchRelation(entity);
		}
		return null;
	}

	@Override
	public boolean cancelRelation(T entity) throws DataAccessException {
		boolean flag = false;
		//取消关联关系:entity对象里面必须要有userId和relatedUserId属性的值
		if(entity.getUserId()!=null && entity.getRelatedUserId()!=null) {
			try{
				mapper.cancelRelation(entity);
				flag = true;
			} catch(Exception e){
				logger.error("取消关联关系:"+e);
			}
		}
		return flag;
	}

}
