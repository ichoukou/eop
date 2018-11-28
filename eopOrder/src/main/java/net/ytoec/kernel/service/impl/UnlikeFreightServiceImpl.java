package net.ytoec.kernel.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dao.UnlikeFreightDao;
import net.ytoec.kernel.dataobject.UnlikeFreight;
import net.ytoec.kernel.service.UnlikeFreightService;

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
 *            UnlikeFreight
 */
@Service
@Transactional
public class UnlikeFreightServiceImpl<T extends UnlikeFreight> implements
		UnlikeFreightService<T> {
	private static Logger logger = LoggerFactory.getLogger(UnlikeFreightServiceImpl.class);
	@Autowired
	private UnlikeFreightDao<T> dao;

	@Override
	public boolean addUnlikeFreight(T task) {
		boolean flag = false;
		try {
			flag = this.dao.addUnlikeFreight(task);
		} catch (DataAccessException dae) {
			flag = false;
		}
		return flag;
	}


	@Override
	public List<T> getUnlikeFreightList(Map map, Pagination pagination,
			boolean flag) {
		// TODO Auto-generated method stub
		if (flag) {
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
		}
		return (List<T>) dao.getUnlikeFreightList(map);
	}


	@Override
	public UnlikeFreight getUnlikeFreightListbySR(String sr, Pagination pagination,
			boolean flag) {
		// TODO Auto-generated method stub
		Map map=new HashMap();
		map.put("mailNo", sr);
		return getUnlikeFreightList(map, pagination, flag).get(0);
	}


	@Override
	public UnlikeFreight getUnlikeFreightListbySR(String sr) {
		// TODO Auto-generated method stub
		Pagination pagination=new Pagination(1, 10);
		return getUnlikeFreightListbySR(sr, pagination, false);
	}


	@Override
	public boolean editUnlikeFreight(T unlikeFreight) {
		// TODO Auto-generated method stub
		try {
			this.dao.editUnlikeFreight(unlikeFreight);
		} catch (DataAccessException dae) {
			return false;
		}
		return true;
	}


	@Override
	public boolean deleteUnlikeFreight(T unlikeFreight) {
		// TODO Auto-generated method stub
		try {
			this.dao.removeUnlikeFreight(unlikeFreight);
		} catch (DataAccessException dae) {
			return false;
		}
		return true;
	}


	@Override
	public List<T> getUnlikeFreightListbymailNo(String sr, Pagination pagination,
			boolean flag) {
		// TODO Auto-generated method stub
		Map map=new HashMap();
		map.put("mailNo", sr);
		return getUnlikeFreightList(map, pagination, flag);
	}
	@Override
	public UnlikeFreight getUnlikeFreightByMailNo(String mailNo) {
		UnlikeFreight unlikeFreight = null;
		try {
			 unlikeFreight=this.dao.getUnlikeFreightByMailNo(mailNo);
		} catch (DataAccessException dae) {
			return unlikeFreight;
		}
		return unlikeFreight;
	}
	

	

	@Override
	public UnlikeFreight getUnlikeFreightById(int id) {
		UnlikeFreight unlikeFreight = null;
		// TODO Auto-generated method stub
		try {
			 unlikeFreight=this.dao.getUnlikeFreightById(id);
		} catch (DataAccessException dae) {
			return unlikeFreight;
		}
		return unlikeFreight;
	}

}
