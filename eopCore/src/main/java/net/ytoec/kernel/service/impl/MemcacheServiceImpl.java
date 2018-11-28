package net.ytoec.kernel.service.impl;

import java.util.Map;
import java.util.Map.Entry;

import net.ytoec.kernel.service.MemcacheService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whalin.MemCached.MemCachedClient;

@Service
public class MemcacheServiceImpl<T extends Object> implements
		MemcacheService<T> {

	@Autowired
	private MemCachedClient memCachedClient;

	private static Logger logger = LoggerFactory
			.getLogger(MemcacheServiceImpl.class);

	@Override
	public boolean add(String key, Object object) {
		if (StringUtils.isEmpty(key)) {
			logger.error("key is empty");
			return false;
		}
		return memCachedClient.set(key, object);
	}

	@Override
	public T get(String key) {
		if (StringUtils.isEmpty(key)) {
			logger.error("key is empty");
			return null;
		}
		return (T) memCachedClient.get(key);

	}

	@Override
	public boolean delete(String key) {
		if (StringUtils.isEmpty(key)) {
			logger.error("key is empty");
			return false;
		}
		return memCachedClient.delete(key);
	}

	@Override
	public boolean replace(String key, Object object) {
		if (StringUtils.isEmpty(key)) {
			logger.error("key is empty");
			return false;
		}
		return memCachedClient.replace(key, object);
	}

	@Override
	public void printStatus() {
		Map<String, Map<String, String>> map = memCachedClient.stats();
		if (map.isEmpty()) {
			logger.error("***********memcache stats:empty");
		}
		for (Entry<String, Map<String, String>> enry : map.entrySet()) {

			for (Entry<String, String> inerenry : enry.getValue().entrySet()) {
				logger.error("***********memcache stats:" + enry.getKey() + ","
						+ inerenry.getKey() + "," + inerenry.getValue());
			}
		}

	}

}
