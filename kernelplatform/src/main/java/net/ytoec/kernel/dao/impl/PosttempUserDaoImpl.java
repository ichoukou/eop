package net.ytoec.kernel.dao.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.PosttempUserDao;
import net.ytoec.kernel.dataobject.PosttempUser;
import net.ytoec.kernel.mapper.PosttempUserMapper;

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
@SuppressWarnings("unchecked")
public class PosttempUserDaoImpl<T extends PosttempUser> implements PosttempUserDao<T> {

	private static Logger logger = LoggerFactory.getLogger(ConfigCodeDaoImpl.class);
	@Inject
	private PosttempUserMapper<PosttempUser> mapper;

	@Override
	public boolean addPosttempUser(T entity) {
	
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
	public T getPosttempUserById(Integer id) {

		PosttempUser pt = new PosttempUser();
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
	public List<T> getPosttempUserByVipId(Map map) {
	

		List<T> list = null;

		try {
			list = (List<T>)this.mapper.getPosttempUserByVipId(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			return Collections.EMPTY_LIST;
		}

		return list;
	}
	
	@Override
	public List<T> getPosttempUserByVipIds(Map map) {
		List<T> list = null;
		try {
			list = (List<T>)this.mapper.getPosttempUserByVipIds(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			return Collections.EMPTY_LIST;
		}

		return list;
	}

	@Override
	public List<T> getPosttempUserByPostId(Integer id) {
	
		List<T> list = null;

		try {
			list = (List<T>)this.mapper.getPosttempUserByPostId(id);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return list;
	}

	@Override
	public boolean editPosttempUser(T entity) {
	
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
	public boolean delPosttempUser(T entity) {
	
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
	public boolean delPosttempUserByPostId(Integer postId) {
	
		boolean flag = false;

		try {
			((PosttempUserMapper<T>) mapper).delPosttempUserByPostId(postId);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}

		return flag;
	}

	@Override
	public List<T> getPosttempUserByBranchId(Integer id)
 {
		List<T> list = null;

		try {
			list = (List<T>)this.mapper.getPosttempUserByBranchId(id);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return list;
	}

	@Override
    public List<T> getPosttempUserByBranchIdMap(Map map)
 {
        List<T> list = null;

        try {
            list = (List<T>)this.mapper.getPosttempUserByBranchIdMap(map);
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
        }

        return list;
    }
	
	@Override
	public List<T>  getPosttempUserByUserCodes(Map map) {
		List<T> list = null;

		try {
			list = (List<T>)this.mapper.getPosttempUserByUserCodes(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			return Collections.EMPTY_LIST;
		}

		return list;
	}

	@Override
	public List<T> getPosttempUserByBranchIdAndVipId(Map map) {
		List<T> list = null;

		try {
			list = (List<T>)this.mapper.getPosttempUserByBranchIdAndVipId(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			return Collections.EMPTY_LIST;
		}

		return list;
	}

	@Override
	public List<T> getPosttempUserByContractVipIdMap(Map map) {
		List<T> list = null;

		try {
			list = (List<T>)this.mapper.getPosttempUserByContractVipIdMap(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			return Collections.EMPTY_LIST;
		}

		return list;
	}
	
	
}
