package net.ytoec.kernel.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dao.StandardPosttempDao;
import net.ytoec.kernel.dataobject.StandardPosttemp;
import net.ytoec.kernel.service.StandardPosttempService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * 标准模版接口实现类.
 * 
 * @param <T>
 *            StandardPosttemp
 */
@Service
@Transactional
@SuppressWarnings("unchecked")
public class StandardPosttempServiceImpl<T extends StandardPosttemp> implements
		StandardPosttempService<T> {
	private static Logger logger = LoggerFactory.getLogger(StandardPosttempServiceImpl.class);
	@Autowired
	private StandardPosttempDao<T> dao;

	@Override
	public boolean addStandardPosttemp(T task) {

		try {
			this.dao.addStandardPosttemp(task);
		} catch (DataAccessException dae) {
			return false;
		}
		return true;
	}

	@Override
	public List<T> getStandardPosttempListBySourceId(int sourceId,
			Pagination pagination, boolean flag) {
		Map map = new HashMap();
		map.put("sourceId", sourceId);
		if (flag) {
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
		}
		
		List<T> spList =  dao.getStandardPosttempListBySourceId(map);
		
		// 设置始发地和目的地的显示值
		int size = spList.size();
		for (int i = 0; i < size; i++) {
			spList.get(i).setSrcText(Resource.getNameById(spList.get(i).getSourceId() ) );
			spList.get(i).setDestText(Resource.getNameById(spList.get(i).getDestId() ) );
		}
		
		return spList;
	}

	@Override
	public List<T> getStandardPosttempList(Map map, Pagination pagination,
			boolean flag) {

		if (flag) {
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
		}
		return (List<T>) dao.getStandardPosttempListBySourceId(map);
	}

	@Override
	public T getStandardPosttempByProv(int sourceId, int destId)
 {
		Map map = new HashMap();
		map.put("sourceId", sourceId);
		map.put("destId", destId);

		List<T> list = dao.getStandardPosttempByProv(map);
		if(list.size() == 0) {
			logger.error("数据异常! 没有对应的数据! 参数信息[sourceId:" + sourceId
					+ ";destId:" + destId + "]");
		}
		if(list.size() > 1) {
			logger.error("数据异常! 存在多个相同数据! 参数信息[sourceId:" + sourceId
					+ ";destId:" + destId + "]");
		}
		return list.get(0);
	}

}
