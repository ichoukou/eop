package net.ytoec.kernel.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.JgOrderCommandDao;
import net.ytoec.kernel.dto.JGOrderDTO;
import net.ytoec.kernel.mapper.JgOrderCommandMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class JgOrderCommandDaoImpl<T extends JGOrderDTO> implements JgOrderCommandDao<T> {

	private static Logger logger = LoggerFactory.getLogger(JgOrderCommandDaoImpl.class);
	@Inject
	private JgOrderCommandMapper<T> mapper;

	@Override
	public Boolean removeJGOrderById(Map<String ,Integer> mapId) {
		
		boolean flag = false;
		try {
		 mapper.removeJGOrderById(mapId);
		 logger.error("remove from JgOrderCommand"+"最小id"+mapId.get("minId")+"最大id"+mapId.get("maxId"));
		 flag = true;
		} catch (Exception e) {
			logger.error("金刚订单同步遗漏订单删除失败", e);
			flag = false;
		}
		return flag;

	}

	@Override
	public List<T> getJGOrderByLimit(Integer limit)
			throws DataAccessException {
		 List<T> result = null;
	        try {
	            result = this.mapper.getJGOrderByLimit(limit);
	        } catch (Exception e) {
	        	logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
	        }
	        return result;
	}

	@Override
	public Boolean addJGOrder(T bean) {
		boolean flag = false;

		try {
			mapper.add(bean);
			flag = true;
		} catch (DataAccessException e) {
			logger.error("金刚订单同步遗漏订单创建失败", e);
		}

		return flag;
	}

	@Override
	public List<T> getJGOrder(Map<String, Object> map)
			throws DataAccessException {
		List<T> list=new ArrayList<T>();
		try {
			list=mapper.getJGOrder(map);
		} catch (DataAccessException e) {
			logger.error("查询金刚订单同步遗漏订单失败", e);
		}
		return list;
	}

	@Override
	public int count(Map<String, Object> map) {
		int count=0;
		try {
			count=mapper.count(map);
		} catch (DataAccessException e) {
			logger.error("查询金刚订单同步遗漏订单失败", e);
		}
		return count;
	}

}
