package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.StandardPosttempDao;
import net.ytoec.kernel.dataobject.StandardPosttemp;
import net.ytoec.kernel.mapper.StandardPosttempMapper;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 
 * 数据访问实现类.
 * 
 * @param <T>
 *            StandardPosttemp
 */

@Repository
public class StandardPosttempDaoImpl<T extends StandardPosttemp> implements StandardPosttempDao<T> {

	private Logger logger = Logger.getLogger(StandardPosttempDaoImpl.class);
	@Autowired
	private StandardPosttempMapper<T> mapper;

	@Override
	public void addStandardPosttemp(T standardPosttemp) {
		try {
			this.mapper.add(standardPosttemp);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
	}

	@Override
	public void editStandardPosttemp(T standardPosttemp) {
		try {
			this.mapper.edit(standardPosttemp);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue());
		}
	}

	@Override
	public List<T> getAll() {

		List<T> list = null;
		try {
			list = (List<T>) mapper.getAllStandardPosttemp();
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue());
		}
		return list;
	}

	@Override
	public T getStandardPosttempById(Integer standardPosttempId) {
		try {
			StandardPosttemp st = new StandardPosttemp();
			st.setId(standardPosttempId);
			return this.mapper.get((T) st);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue());
		}
		return null;
	}

	@Override
	public void removeStandardPosttemp(T standardPosttemp) {
		try {
			this.mapper.remove(standardPosttemp);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue());
		}
	}

	@Override
	public List<T> getStandardPosttempListBySourceId(Map map) {
		List<T> result = (List<T>) this.mapper.getStandardPosttempListBySourceId(map);
		return result;
	}

	@Override
	public List<T> getStandardPosttempByProv(Map map) {

		List<T> list = null;
		try {
			list = (List<T>) mapper.getStandardPosttempByProv(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue());
		}
		return list;
	}

}
