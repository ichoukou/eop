package net.ytoec.kernel.dao.impl;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.UserCustomDao;
import net.ytoec.kernel.dataobject.UserCustom;
import net.ytoec.kernel.mapper.UserCustomMapper;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
@Repository
public class UserCustomDaoImpl<T extends UserCustom> implements UserCustomDao<T> {
    
	private static final Logger  logger = LoggerFactory.getLogger(UserCustomDaoImpl.class);
    
	@Inject
    private UserCustomMapper<UserCustom> mapper;
	
	@Override
	public List<T> searchUserCustom(T entity) {
		return (List<T>)mapper.searchUsers(entity);
	}

	@Override
	public boolean add(T entity) {
        boolean flag = true;
        try {
            mapper.add(entity);
        } catch(DuplicateKeyException e){
        	flag = true;
        }catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
            flag = false;
        }
        return flag;
    }
	
	@Override
	public boolean edit(T entity) {
		try {
			mapper.edit(entity);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean remove(T entity) {
		try {
			mapper.remove(entity);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public T get(T entity) {
		return null;
	}
	
	@Override
	public boolean delUserCustom(String userName) {
		Integer i = mapper.delUserCustom(userName);
		if (i>0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean individuationDelete(T userCustom) {
		try{
			if(StringUtils.isNotEmpty(userCustom.getUserName()) && StringUtils.isNotEmpty(userCustom.getBindedUserName()) && StringUtils.isNotEmpty(userCustom.getType()) && StringUtils.isNotEmpty(userCustom.getRelationalQuery())) {
				mapper.individuationDelete(userCustom);
				return true;
			}
			return false;
		} catch(Exception e) {
			return false;
		}
	}

}
