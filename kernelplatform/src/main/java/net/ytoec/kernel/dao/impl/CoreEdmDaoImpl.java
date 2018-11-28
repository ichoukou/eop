package net.ytoec.kernel.dao.impl;


import net.ytoec.kernel.action.test.TestAction;
import net.ytoec.kernel.dao.CoreEdmDao;
import net.ytoec.kernel.dataobject.CoreEdm;
import net.ytoec.kernel.mapper.CoreEdmMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class CoreEdmDaoImpl<T extends CoreEdm> implements CoreEdmDao<T> {

	@Autowired
	private CoreEdmMapper<CoreEdm> mapper;
	
	private static Logger logger = LoggerFactory.getLogger(TestAction.class);
	@Override
	public boolean addCoreEdm(T entity) throws DataAccessException {
		// TODO Auto-generated method stub
		boolean flag = false;
		try {
			mapper.add(entity);
			flag = true;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Inserted into the database failed",e);
		}
		return flag;
	}

}
