package net.ytoec.kernel.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dao.StatsMessageDao;
import net.ytoec.kernel.dataobject.StatsMessage;
import net.ytoec.kernel.service.StatsMessageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * 接口实现类.
 * 
 * @param <T>
 *            StatsMessage
 */
@Service
@Transactional
public class StatsMessageServiceImpl<T extends StatsMessage> implements
		StatsMessageService<T> {
	private static Logger logger = LoggerFactory.getLogger(StatsMessageServiceImpl.class);
	@Autowired
	private StatsMessageDao<T> dao;

	@Override
	public boolean addStatsMessage(T task) {

		try {
			this.dao.addStatsMessage(task);
		} catch (DataAccessException dae) {
			return false;
		}
		return true;
	}


	@Override
	public List<T> getStatsMessageList(Map map, Pagination pagination,
			boolean flag) {
		// TODO Auto-generated method stub
		if (flag) {
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
		}
		return (List<T>) dao.getStatsMessageList(map);
	}


	@Override
	public StatsMessage getStatsMessageListbySR(String sr, Pagination pagination,
			boolean flag) {
		// TODO Auto-generated method stub
		Map map=new HashMap();
		map.put("statsReason", sr);
        List<T> statsMessages = getStatsMessageList(map, pagination, flag);
        if (statsMessages == null || statsMessages.isEmpty()) {
            return null;
        }
        return statsMessages.get(0);
	}


	@Override
	public StatsMessage getStatsMessageListbySR(String sr) {
		// TODO Auto-generated method stub
		Pagination pagination=new Pagination(1, 10);
		return getStatsMessageListbySR(sr, pagination, false);
	}


	@Override
	public boolean editStatsMessage(T statsMessage) {
		// TODO Auto-generated method stub
		try {
			this.dao.editStatsMessage(statsMessage);
		} catch (DataAccessException dae) {
			return false;
		}
		return true;
	}

}
