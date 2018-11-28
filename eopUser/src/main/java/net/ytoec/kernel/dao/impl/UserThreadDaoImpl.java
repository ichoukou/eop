package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.UserThreadDao;
import net.ytoec.kernel.dataobject.QueryUserCondition;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.mapper.UserThreadMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

/**
 * 从金刚系统同步过来的用户信息操作Dao
 * 
 * @param <T>
 * @author wangyong 2011-10-31
 */
@Repository
public class UserThreadDaoImpl<T extends UserThread> implements
		UserThreadDao<T> {

	private static final Logger logger = LoggerFactory.getLogger(UserThreadDaoImpl.class);
	
	@Inject
	private UserThreadMapper<UserThread> mapper;

	@Override
	public boolean addUser(T entity) {
		boolean flag = false;
		try {
			mapper.add(entity);
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
		UserThread user = new UserThread();
		user.setId(id);
		T entity = null;
		try {
			entity = (T) mapper.get(user);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getAllUser() {
		List<T> list = null;
		try {
			list = (List<T>) mapper.getAllUser();
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public boolean editUser(T entity) {
		boolean flag = false;
		try {
			mapper.edit(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}

		return flag;
	}

	@Override
	public boolean delUser(T entity) {
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
	public List<T> searchUsersByCode(T entity) {

		List<T> list = null;

		try {
			list = (List<T>) mapper.searchUsersByCode(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> searchUsersBySite(T entity) throws DataAccessException {
		List<T> list = null;
		try {
			list = (List<T>) mapper.searchUsersBySite(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> searchAllUsersBySite(T entity) throws DataAccessException {
		List<T> list = null;
		try {
			list = (List<T>) mapper.searchAllUsersBySite(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<T> searchUsersBySiteCodeAndName(Map map) {
		List<T> list = null;
		try {
			list = (List<T>) mapper.searchUsersBySiteCodeAndName(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getUserThreadByPostId(Integer postId) {
		List<T> list = null;
		try {
			list = (List<T>) mapper.getUserThreadByPostId(postId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getByIdAndState(Integer id) {
		UserThread user = new UserThread();
		user.setId(id);
		T entity = null;
		try {
			entity = (T) mapper.getByIdAndState(user);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getContractUserList(Map map) throws DataAccessException {
		List<T> list = null;
		try {
			list = (List<T>) mapper.getContractUserList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}
	
	@Override
	public boolean updateUserThread(T entity) {
		boolean flag = false;
		try {
			mapper.updateUserThread(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}

		return flag;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> getisPrintFlag(T entity) {
		List<T> list = null;
		try {
			list = (List<T>) mapper.getisPrintFlag(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public List<T> getByState(String userState) throws DataAccessException {
		try {
			return (List<T>)mapper.getByState(userState);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			return null;
		}
	}
	
	@Override
	public List<T> queryUserThread(QueryUserCondition quc) throws DataAccessException {
		try {
			return (List<T>)mapper.getUserThreadList(quc);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			return null;
		}
	}

	@Override
	public int countUserThread(QueryUserCondition quc) throws DataAccessException {
		try {
			return (int)mapper.getCountUserThread(quc);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			return 0;
		}
	}

	@Override
	public boolean updateIsCanDownloadByIds(Map map)throws DataAccessException {
		try {
			mapper.updateIsCanDownloadByIds(map);
			return true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			return false;
		}
	}
}
