package net.ytoec.kernel.service.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dao.ServiceHistoryDao;
import net.ytoec.kernel.dataobject.ServiceHistory;
import net.ytoec.kernel.service.ServiceHistoryService;

import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务历史service实现类
 * @author guolongchao
 * @date 2012-07-03
 */
@Service
@Transactional
@SuppressWarnings("all")
public class ServiceHistoryServiceImpl<T extends ServiceHistory> implements
		ServiceHistoryService<T> {

	private static final Logger logger = LoggerFactory
			.getLogger(ServiceHistoryServiceImpl.class);

	@Inject
	private ServiceHistoryDao<ServiceHistory> serviceHistoryDao;

	@Override
	public List<T> getServiceHistoryList(Map map, Pagination pagination,
			boolean flag) {
		List<ServiceHistory> list = null;
		if (BooleanUtils.isFalse(flag)) {//ture表示分页
			map.put("startIndex", pagination.getStartIndex()); // 设置分页参数
			map.put("pageNum", pagination.getPageNum());			
		} else {
			// 去掉分页参数
			if (map.containsKey("startIndex"))
				map.remove("startIndex");
			if (map.containsKey("pageNum"))
				map.remove("pageNum");			
		}
		list = serviceHistoryDao.getServiceHistoryList(map);
		return (List<T>) list;
	}

	/*
	 * 根据开通服务id、用户id来查询（自动续费时需要最新的一条记录）
	 */
	@Override
	public List<T> getServiceHistoryListByDUserId(Map map) {
		return (List<T>) serviceHistoryDao.getServiceHistoryListByDUserId(map);
	}

	@Override
	public boolean add(T entity) {

		return serviceHistoryDao.add(entity);
	}

	@Override
	public T get(T entity) {
		return (T) serviceHistoryDao.get(entity);
	}

	@Override
	public boolean edit(T entity) {
		return serviceHistoryDao.edit(entity);
	}

	@Override
	public boolean remove(T entity) {
		return serviceHistoryDao.remove(entity);
	}

}
