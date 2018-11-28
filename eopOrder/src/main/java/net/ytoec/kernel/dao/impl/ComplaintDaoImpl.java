/**
 * net.ytoec.kernel.dao.impl
 * ComplaintDaoImpl.java
 * 2012-7-10下午03:00:23
 * @author wangyong
 */
package net.ytoec.kernel.dao.impl;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dao.ComplaintDao;
import net.ytoec.kernel.dataobject.Complaint;
import net.ytoec.kernel.mapper.ComplaintMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author wangyong 2012-7-10
 */
@Repository
public class ComplaintDaoImpl<T extends Complaint> implements ComplaintDao<T> {

	private static Logger logger = LoggerFactory
			.getLogger(ComplaintDaoImpl.class);

	@Inject
	private ComplaintMapper<Complaint> mapper;

	@Override
	public boolean addComplaint(T entity) throws DataAccessException {
		boolean flag = false;
		try {
			mapper.add(entity);
			flag = true;
		} catch (Exception e) {
			flag = false;
			logger.error("新增投诉数据出错，" + e);
		}
		return flag;
	}

	@Override
	public List<T> getListByMailNo(String mailNo) throws DataAccessException {
		List<T> list = null;
		try {
			list = (List<T>) mapper.getByMailNo(mailNo);
		} catch (Exception e) {
			logger.error("根据运单号" + mailNo + "查询投诉报错" + e);
		}
		return list;
	}

}
