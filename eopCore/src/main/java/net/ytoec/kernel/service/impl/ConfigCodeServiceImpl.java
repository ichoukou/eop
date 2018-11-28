package net.ytoec.kernel.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dao.ConfigCodeDao;
import net.ytoec.kernel.dataobject.ConfigCode;
import net.ytoec.kernel.service.ConfigCodeService;
import net.ytoec.kernel.service.MemcacheService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 配置项表的业务操作
 * 
 * @author ChenRen
 * @date 2011-08-10
 */
@Service
@Transactional
public class ConfigCodeServiceImpl<T extends ConfigCode> implements
		ConfigCodeService<T> {

	private static Logger logger = LoggerFactory
			.getLogger(ConfigCodeServiceImpl.class);

	@Autowired
	private MemcacheService<ConfigCode> memcacheService;

	@Inject
	private ConfigCodeDao<T> dao;

	@Override
	public T getConfByKeyAndType(String key, String type) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("confKey", key);
		map.put("confType", type);

		List<T> list = dao.getConfByKeyAndType(map);

		if (list.size() != 1) {
			logger.error("key&&type没有匹配的唯一数据对象!/r/n" + "参数信息：key[" + key
					+ "]type[" + type + "]" + "数据大小：" + list.size());
		}

		return list.get(0);
	}

	@Override
	public List<T> getConfByPid(Integer pid) {
		List<T> list = null;
		if (pid != null) {
			list = dao.getConfByPid(pid);
		}
		return list;
	}

	@Override
	public T getConfByKey(String confKey) {
		if (confKey != null) {
			return dao.getConfByKey(confKey);
		}
		return null;
	}

	@Override
	public boolean addConfig(T entity, Integer levelId) {
		if (entity == null) {

			logger.error("entity is empity");
			return false;
		}
		entity.setConfLevel(levelId);// 设置该配置项为第levelId级配置目录
		memcacheService.add(entity.getConfKey(), entity);
		return dao.addConfig(entity);
	}

	@Override
	public T getConfigById(Integer id) {
		return dao.getConfigById(id);
	}

	@Override
	public List<T> getAllConfig(Pagination pagination, boolean flag) {
		Map map = new HashMap();
		if (flag) {
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
		}
		return dao.getAllConfig(map);
	}

	@Override
	public List<T> getConfigByLevel(Pagination pagination, String confLevel,
			boolean flag) {
		Map map = new HashMap();
		if (flag) {

			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
		}
		if (confLevel != null && !confLevel.trim().equals("")) {
			map.put("confLevel", confLevel);
		}
		return dao.getConfigByLevel(map);
	}

	@Override
	public boolean editConfig(T entity) {
		if (entity == null) {
			logger.error("editConfig entity is empity");
			return false;
		}
		try
		{
			memcacheService.add(entity.getConfKey(), entity);
		}catch(Exception e){
			logger.error(" ConfigCodeServiceImpl editConfig 写入缓存失败 原因：",e);
		}
		return dao.editConfig(entity);
	}

	@Override
	public boolean removeConfig(T entity) {
		if (entity == null) {

			logger.error("entity is empity");
			return false;
		}
		memcacheService.delete(entity.getConfKey());
		return dao.removeConfig(entity);
	}

	@Override
	public boolean updateConfByKey(String key, String value) {

		return dao.updateConfByKey(key, value);
	}

	@Override
	public String getJobMonitorTime(String key) {
		return dao.getJobMonitorTime(key);
	}

	@Override
	public List<ConfigCode> getJobMonitorTime1(String key) {

		List<ConfigCode> list = new ArrayList<ConfigCode>();
		list = dao.getJobMonitorTime1(key);
		return list;
	}

}
