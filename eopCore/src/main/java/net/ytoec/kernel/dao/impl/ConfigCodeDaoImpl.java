package net.ytoec.kernel.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.ConfigCodeDao;
import net.ytoec.kernel.dataobject.ConfigCode;
import net.ytoec.kernel.mapper.ConfigCodeMapper;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * 配置项表的数据库操作
 * 
 * @author ChenRen
 * @date 2011-08-10
 */
@Repository
public class ConfigCodeDaoImpl<T extends ConfigCode> implements
		ConfigCodeDao<T> {

	private static Logger logger=LoggerFactory.getLogger(ConfigCodeDaoImpl.class);
	@Inject
	private ConfigCodeMapper<ConfigCode> mapper;

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getConfByKeyAndType(Map<String, String> map) {

		List<T> list = null;

		try {
			list = (List<T>) this.mapper.getConfByKeyAndType(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return list;
	}

	@Override
	public List<T> getConfByType(String type) {

		List<T> list = null;

		try {
			list = (List<T>) this.mapper.getConfByType(type);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return list;
	}

	@Override
	public List<T> getConfByPid(Integer pid) {

		List<T> list = null;

		try {
			list = (List<T>) this.mapper.getConfByPid(pid);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getConfByKey(String confKey) {

		T entity = null;
		try {
			entity = (T) mapper.getConfByKey(confKey);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return entity;
	}

	@Override
	public boolean addConfig(T config) {

		boolean flag = false;
		try {
			mapper.add(config);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public T getConfigById(Integer id) {

		ConfigCode config = new ConfigCode();
		T entity = null;
		try {
			config.setId(id);
			entity = (T) mapper.get(config);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return entity;
	}

	@Override
	public List<T> getAllConfig(Map map) {

		List<T> list = null;
		try {
			list = (List<T>) this.mapper.getAllConfig(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public List<T> getConfigByLevel(Map map) {

		List<T> list = null;
		try {
			list = (List<T>) this.mapper.getConfigByLevel(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public boolean editConfig(T config) {

		boolean flag = false;
		try {
			mapper.edit(config);
			flag = true;
		} catch (Exception e) {
			logger.error("ConfigCodeDaoImpl editConfig "+LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean removeConfig(T config) {

		boolean flag = false;
		try {
			mapper.remove(config);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean updateConfByKey(String key, String value) {
		if (StringUtils.isEmpty(key)) {
			return false;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("confKey", key);
		map.put("confValue", value);
		Integer rows = mapper.updateConfByKey(map);
		if (rows == null || rows == 0) {
			return false;
		}
		return true;
	}

	@Override
	public String getJobMonitorTime(String key) {
		// TODO Auto-generated method stub
		String time = null;
		try {
			time = mapper.getJobMonitorTime(key);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("查询数据库失败！", e);
		}
		return time;
	}
	
	@Override
	public  List<ConfigCode> getJobMonitorTime1(String key) {
		 List<ConfigCode> times = new ArrayList<ConfigCode>();
		try {
			times = mapper.getJobMonitorTime1(key);
		} catch (Exception e) {
			logger.error("查询数据库失败！", e);
		}
		return times;
	}

}
