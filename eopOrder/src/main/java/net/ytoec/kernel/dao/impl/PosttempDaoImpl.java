package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.PosttempDao;
import net.ytoec.kernel.dataobject.Posttemp;
import net.ytoec.kernel.mapper.PosttempMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
/**
 * 运费模板
 * 
 * @author ChenRen
 * @date 2011-09-09
 * 
 * @param <T>
 */
@Repository
//@SuppressWarnings("all")
public class PosttempDaoImpl<T extends Posttemp> implements PosttempDao<T> {

	private static Logger logger = LoggerFactory.getLogger(PosttempDaoImpl.class);
	@Inject
	private PosttempMapper<Posttemp> mapper;

	@Override
	public boolean addPosttemp(T entity) {

		boolean flag = false;

		try {
			mapper.add(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}

		return flag;
	}

	@Override
	public T getPosttempById(Integer id) {

		
		Posttemp pt = new Posttemp();
		pt.setId(id);
		T entity = null;

		try {
			entity = (T) mapper.get(pt);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return entity;
	}

	@Override
	public boolean editPosttemp(T entity) {

		boolean flag = false;

		try {
			mapper.edit(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}

		return flag;
	}

	@Override
	public boolean delPosttemp(T entity) {

		boolean flag = false;

		try {
			mapper.remove(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}

		return flag;
	}

	@Override
	public List<T> getPosttempByBranchId(Map map) {

		List<T> list = null;

		try {
			list = (List<T>) this.mapper.getPosttempByBranchId(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return list;
	}

	@Override
	public List<T> getPosttempByType(String type) {

		List<T> list = null;

		try {
			list = (List<T>) mapper.getPosttempByType(type);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return list;
	}

}
