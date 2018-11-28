package net.ytoec.kernel.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dao.WarnValueDao;
import net.ytoec.kernel.dataobject.WarnValue;
import net.ytoec.kernel.service.WarnValueService;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WarnValueServiceImpl<T extends WarnValue> implements WarnValueService<T> {

	private static Logger logger = Logger.getLogger(WarnValueServiceImpl.class);
	@Inject
	private WarnValueDao<T> dao;
    
	@Override
	public String addWarnValue(T entity) {
		String resultString = "";
		if(entity == null){
			logger.error(WarnValue.class.getName() + "对象参数信息为空!");
			resultString = "抱歉！系统繁忙，请稍后再试。";
			return resultString;
		}
		//先判断是否存在相同预警值
		T t = this.dao.searchWarnValueOneAdd(entity);
		if(t != null){
			logger.error("添加的预警值已存在!");
			resultString="抱歉！不能重复添加。";
			return resultString;
		}
		
		boolean b = false;
		try{
			this.dao.addWarnValue(entity);
			b = true;
			resultString = "";
		}catch(Exception e){
			logger.error("添加预警值异常!");
			resultString = "抱歉！系统繁忙，请稍后再试。";
		}
		
		return resultString;
	}

	@Override
	public boolean remove(T entity) {
		boolean b = false;
		try{
			this.dao.removeWarnValue(entity);
			b = true;
		}catch(Exception e){
			logger.error( "删除预警值异常!");
		}
		return b;
	}

	@Override
	public String edit(T entity) {
		if(entity == null){
			logger.error(WarnValue.class.getName() + "对象参数信息为空!");
			return "抱歉！系统繁忙，请稍后再试。";
		}
		
		//先判断是否存在相同预警值
		T t = this.dao.searchWarnValueOne(entity);
		if(t != null){
			logger.error("预警值已存在!无法修改");
			return "";
		}
		
		logger.info("预警值不存在，可以修改");
		boolean b = false;
		String resultString = "";
		try{
			this.dao.editWarnValue(entity);
			b = true;
			resultString = "";
		}catch(Exception e){
			logger.error( "修改预警值异常!");
			resultString = "抱歉！系统繁忙，请稍后再试。";
		}
		return resultString;
	}

	@Override
	public List<T> searchWarnValueBySellerId(T entity) throws Exception {
		
		logger.info("查询卖家:"+entity.getSellerId()+"下的所有目的地");
		List<T> list = null;
		try{
			list = this.dao.searchWarnValueMore(entity);
		}catch(Exception e){
			throw new Exception("查询卖家预警值异常");
		}
		return list;
	}

	@Override
	public boolean operatorWarnValue(Map<String, Object> map) {
		boolean result = true;
		List<T> paramInsertList = (List<T>) map.get("paramInsertList");
		List<T> paramUpdateList = (List<T>)map.get("paramUpdateList");
		List<T> paramDeleteList = (List<T>)map.get("paramDeleteList");
		//插入数据
		if(paramInsertList != null && paramInsertList.size() >0){
			for(T warnValue : paramInsertList){
				result = this.dao.addWarnValue(warnValue);	
			}
		}
		if(paramUpdateList != null && paramUpdateList.size()> 0){
			for(T warnValue : paramUpdateList){
				result = this.dao.editWarnValue(warnValue);
			}
		}
		if(paramDeleteList != null && paramDeleteList.size() >0){
			for(T warnValue : paramDeleteList){
				result = this.dao.removeWarnValue(warnValue);
			}
		}
		return result;
	}	


}
