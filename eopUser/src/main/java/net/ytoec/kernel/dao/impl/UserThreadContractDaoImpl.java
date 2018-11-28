package net.ytoec.kernel.dao.impl;


import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.UserThreadContractDao;
import net.ytoec.kernel.dataobject.UserThreadContract;
import net.ytoec.kernel.mapper.UserThreadContractMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class UserThreadContractDaoImpl<T extends UserThreadContract> implements UserThreadContractDao<T> {

	private static final Logger logger=LoggerFactory.getLogger(UserThreadContractDaoImpl.class);
	
	@Inject
	private UserThreadContractMapper<UserThreadContract> mapper;

	@Override
	public boolean insertContract(T entity) throws DataAccessException {
		boolean flag = false;
		try {
			mapper.insertContract(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> searchContractsBysiteId(UserThreadContract entity)
			throws DataAccessException {
		List<T> list = null;
		try {
			list = (List<T>) mapper.searchContractsBysiteId(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public boolean deleteContractByConractAreaId(UserThreadContract entity)
			throws DataAccessException {
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
	
	@Override
	public boolean deleteContractByAddUsername(UserThreadContract entity)
			throws DataAccessException {
		boolean flag = false;
		try {
			mapper.removeByAddUsername(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getContractersByUserNameAndType(UserThreadContract entity)
			throws DataAccessException {
		List<T> list = null;
		try {
			list = (List<T>) mapper.getContractersByUserNameAndType(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> searchContractByConractAreaId(Integer id,String accountType)
			throws DataAccessException {
		UserThreadContract utc = new UserThreadContract();
		utc.setConractAreaId(id);
		utc.setAccountType(accountType);
		List<T> list = null;
		try {
			list = (List<T>) mapper.searchContractByConractAreaId(utc);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean updateContractByAddUserName(UserThreadContract entity)
			throws DataAccessException {
		boolean flag = true;
		try {
			mapper.updateContractByAddUserName(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public List<T> searchContractByConractAreaIdAndUserName(UserThreadContract entity) throws DataAccessException {
		List<T> list = null;
		try {
			list = (List<T>) mapper.searchContractByConractAreaIdAndUserName(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}
	
	
}
