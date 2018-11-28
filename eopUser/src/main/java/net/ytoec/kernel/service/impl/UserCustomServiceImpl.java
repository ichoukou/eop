package net.ytoec.kernel.service.impl;


import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dao.UserCustomDao;
import net.ytoec.kernel.dataobject.UserCustom;
import net.ytoec.kernel.service.UserCustomService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户业务接口实现类
 * 
 * @author ChenRen
 * @date 2011-7-26
 */
@Service
@Transactional
public class UserCustomServiceImpl<T extends UserCustom> implements UserCustomService<T> {
	@Inject
	private UserCustomDao<T> dao;
	
	@Override
	public boolean add(T entity) {
		boolean rs=false;
		try{
			rs=dao.add(entity);
		}catch(DuplicateKeyException e){
			rs=true;
		}catch(Exception e){
			rs=false;
		}
		return rs;
	}

	@Override
	public boolean edit(T entity) {
		return dao.edit(entity);
	}

	@Override
	public boolean remove(T entity) {
		return dao.remove(entity);
	}

	@Override
	public T get(T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> searchUserCustom(T userCustom) {
		return dao.searchUserCustom(userCustom);
	}

	@Override
	public boolean individuationDelete(T userCustom) {
		return dao.individuationDelete(userCustom);
	}

}
