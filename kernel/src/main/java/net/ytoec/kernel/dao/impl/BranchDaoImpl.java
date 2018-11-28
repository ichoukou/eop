/**
 * 
 */
package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.BranchDao;
import net.ytoec.kernel.dataobject.Branch;
import net.ytoec.kernel.mapper.BranchMapper;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

/**
 * @author wangyong 2012-02-12
 */
@Repository
public class BranchDaoImpl<T extends Branch> implements BranchDao<T> {

	private Logger logger = Logger.getLogger(BranchDaoImpl.class);
	@Inject
	private BranchMapper<Branch> mapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ytoec.kernel.dao.BranchDao#getAllSearchData(java.util.Map)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<T> getAllSearchData(Map map) {
		return (List<T>) mapper.getAllSearchData(map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ytoec.kernel.dao.BranchDao#countAllSearchData()
	 */
	@Override
	public int countAllSearchData() {
		return mapper.countAllSearchData();
	}

	@Override
	public boolean addBranch(T entity) {
		boolean flag = true;
		try {
			mapper.addBranch(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean edit(T entity) {
		boolean flag = true;

		try {
			mapper.edit(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public Branch get(Integer id) {
		Branch branch = null;
		try {
			branch = mapper.get(id);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return branch;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByCompanyName(T entity) {
		// TODO Auto-generated method stub
		List<T> list = null;
		try {
			list = (List<T>) mapper.findByCompanyName(entity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByManagerPhone(T entity) {
		// TODO Auto-generated method stub
		List<T> list = null;
		try {
			list = (List<T>) mapper.findByManagerPhone(entity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByServicePhone(T entity) {
		// TODO Auto-generated method stub
		List<T> list = null;
		try {
			list = (List<T>) mapper.findByServicePhone(entity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByQuestionPhone(T entity) {
		// TODO Auto-generated method stub
		List<T> list = null;
		try {
			list = (List<T>) mapper.findByQuestionPhone(entity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByBranchCode(int branchCode) {
		// TODO Auto-generated method stub
		List<T> list = null;
		try {
			list = (List<T>)mapper.findByBranchCode(branchCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public Branch findByUserId(Integer userId) {
		// TODO Auto-generated method stub
		Branch branch = null;
		try {
			branch = mapper.get(userId);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return branch;
	}



}
