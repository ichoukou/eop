package com.ytoec.cms.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.Pagination;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.ytoec.cms.bean.Column;
import com.ytoec.cms.dao.ColumnDao;
import com.ytoec.cms.mapper.ColumnMapper;

@Repository
public class ColumnDaoImpl<T extends Column> implements ColumnDao<T> {
	
	private static Logger log=LoggerFactory.getLogger(ColumnDaoImpl.class);
	@Inject
	private ColumnMapper<T> columnMapper;
	
	@Override
	public boolean add(T object){
		// TODO Auto-generated method stub
		try {
			if(object != null && StringUtils.isEmpty(object.getColumnCode())){
				log.error("添加栏目时出错：栏目Code字段不能为空！");
				return false;
			}
			T obj = retrive(object.getColumnCode());
			if(obj != null){
				log.error("添加栏目时出错：栏目Code为“"+object.getColumnCode()+"”的栏目已经存在，请修改code后再添加！");
				return false;
			}
			columnMapper.add(object);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("添加栏目时出错："+e);
			return false;
		}
		return true;
	}

	@Override
	public T get(T object){
		// TODO Auto-generated method stub
		return columnMapper.get(object);
	}

	@Override
	public boolean edit(T object){
		// TODO Auto-generated method stub
		try {
			columnMapper.edit(object);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("修改栏目时出错："+e);
			return false;
		}
		return true;
	}
	
	/**
	*物理删除(数据将从数据库中删除)
	*/
	@Override
	public boolean remove(T object){
		// TODO Auto-generated method stub
		try {
			columnMapper.remove(object);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("删除栏目时出错："+e);
			return false;
		}
		return true;
	}

	@Override
	public List<T> getAll(Map<String, Object> param){
		// TODO Auto-generated method stub
		return columnMapper.getAll(param);
	}

	@Override
	public Pagination<T> getPageList(Pagination<T> page,Map<String,Object> params){
		// TODO Auto-generated method stub
		if (page != null && page.getPageNum() != null) {
			int curPage = page.getCurrentPage() < 1 ? 1 : page.getCurrentPage();
			params.put("start",(curPage-1)*page.getPageNum());
			params.put("limit", page.getPageNum());
			int total = columnMapper.getTotal(params);
			page.setTotalRecords(total);
			List<T> dateList = (List<T>) columnMapper.getPageList(params);
			page.setRecords(dateList);
		}
		return page;
	}

	@Override
	public T retrive(String columnCode){
		// TODO Auto-generated method stub
		return columnMapper.retrive(columnCode);
	}

	@Override
	public boolean delete(Integer columnId){
		// TODO Auto-generated method stub
		try {
			columnMapper.delete(columnId);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			log.error("删除栏目时出错："+e);
			return false;
		}
		return true;
	}
}