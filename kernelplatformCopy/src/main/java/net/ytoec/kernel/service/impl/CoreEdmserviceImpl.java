package net.ytoec.kernel.service.impl;



import net.ytoec.kernel.action.test.TestAction;
import net.ytoec.kernel.dao.CoreEdmDao;
import net.ytoec.kernel.dataobject.CoreEdm;
import net.ytoec.kernel.service.CoreEdmService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CoreEdmserviceImpl<T extends CoreEdm> implements CoreEdmService<T> {

	@Autowired
	private CoreEdmDao<T> coreEdmDao;
	
	private static Logger logger = LoggerFactory.getLogger(TestAction.class);
	@Override
	public boolean addCoreEdm(T entity) {
		// TODO Auto-generated method stub
		boolean flag = false;
		try {
			flag = coreEdmDao.addCoreEdm(entity);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("create fail",e);
		}
		return flag;
	}

}
