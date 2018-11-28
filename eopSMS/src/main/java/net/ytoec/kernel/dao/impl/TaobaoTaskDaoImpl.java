package net.ytoec.kernel.dao.impl;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dao.TaobaoTaskDao;
import net.ytoec.kernel.dataobject.TaobaoTask;
import net.ytoec.kernel.mapper.TaobaoTaskMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * 淘宝更新订单任务dao层实现类
 * @author
 *
 * @param <T>
 */
@Repository
public class TaobaoTaskDaoImpl<T extends TaobaoTask> implements TaobaoTaskDao<T> {

	private static final Logger logger=LoggerFactory.getLogger(TaobaoTaskDaoImpl.class);
	
	@Inject
	private TaobaoTaskMapper<T> mapper;

	@Override
	public T getTaobaoTaskByTaskId(Integer taskId) {
		return mapper.getTaobaoTaskByTaskId(taskId);
	}

	@Override
	public void addTaobaoTask(T taobaoTask) {
		mapper.addTaobaoTask(taobaoTask);
	}

	@Override
	public void delTaobaoTaskById(Integer id) {
		mapper.delTaobaoTaskById(id);
	}

	@Override
	public void updateTaobaoTask(T taobaoTask) {
		try {
			mapper.updateTaobaoTask(taobaoTask);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public List<T> getTaobaoTaskByUserId(String flag, Integer userId) {
	    if (flag==null&&userId==null) {
            return Collections.EMPTY_LIST;
        }
		if (userId == null) {
			return mapper.getTaobaoTaskByFlag(flag);
		}
		return mapper.getTaobaoTaskByUserId(userId);
	}
}
