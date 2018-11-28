/**
 * JgWaybillServiceImpl.java
 * Wangyong
 * 2011-8-18 下午01:30:38
 */
package net.ytoec.kernel.service.impl;

import javax.inject.Inject;

import net.ytoec.kernel.dao.JgWaybillUpdateDao;
import net.ytoec.kernel.dataobject.JgWaybillUpdate;
import net.ytoec.kernel.service.JgWaybillUpdateService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Wangyong
 * @2011-8-18
 * net.ytoec.kernel.service.impl
 */
@Service
@Transactional
public class JgWaybillUpdateServiceImpl<T extends JgWaybillUpdate> implements JgWaybillUpdateService<T> {
	
	private static Logger logger = LoggerFactory.getLogger(JgWaybillUpdateServiceImpl.class);
	@Inject
	private JgWaybillUpdateDao<T> dao;

	@Override
	public boolean addJgWaybillUpdate(T jgWaybillUpdate) {
		if (jgWaybillUpdate == null) {
			logger.error("jgWaybillUpdate is empity");
			return false;
		}
		return dao.addJgWaybillUpdate(jgWaybillUpdate);
	}

}
