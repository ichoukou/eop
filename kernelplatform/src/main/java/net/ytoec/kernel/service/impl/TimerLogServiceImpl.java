package net.ytoec.kernel.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dao.TimerLogDao;
import net.ytoec.kernel.dataobject.TimerLog;
import net.ytoec.kernel.service.TimerLogService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.oscache.util.StringUtil;

/**
 * timer日志
 * @author L
 * @param <T>
 */
@Service
@Transactional
public class TimerLogServiceImpl<T> implements TimerLogService<TimerLog>{

	private static Logger logger = LoggerFactory.getLogger(TimerLogServiceImpl.class);
	
	@Inject
	private TimerLogDao<T> dao;

	@Override
	public boolean addTimerLog(TimerLog bean) {
		try
		{
			return dao.addTimerLog(bean);
		}catch(Exception e){
			logger.error("插入Timer日志出错",e);
		}
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TimerLog> get(Map<String, String> map,Pagination pagination) {
		
		List<TimerLog> list=(List<TimerLog>) new ArrayList<T>();
		Map<String,Object> maps=new HashMap<String,Object>();
		maps.put("startTime", map.get("startTime"));
		maps.put("endTime", map.get("endTime"));
		if(StringUtil.isEmpty(map.get("operate"))){
			maps.put("operate",null);
		}else{
			maps.put("operate",Integer.parseInt(map.get("operate")));
		}
			maps.put("timerNO",map.get("timerNO"));
		if(StringUtil.isEmpty(map.get("isError"))){
			maps.put("isError",null);
		}else{
			maps.put("isError",Integer.parseInt(map.get("isError")));
		}
		
		maps.put("startIndex", pagination.getStartIndex());
		maps.put("pageNum", pagination.getPageNum());
		try{
			list=(List<TimerLog>) dao.get(maps);
		}catch(Exception e){
			logger.error("Timer日志出错",e);
		}
		return list;
	}

	@Override
	public int countNum(Map<String, String> map, Pagination pagination) {
		Map<String,Object> maps=new HashMap<String,Object>();
		maps.put("startTime", map.get("startTime"));
		maps.put("endTime", map.get("endTime"));
		if(StringUtil.isEmpty(map.get("operate"))){
			maps.put("operate",null);
		}else{
			maps.put("operate",Integer.parseInt(map.get("operate")));
		}
			maps.put("timerNO",map.get("timerNO"));
		if(StringUtil.isEmpty(map.get("isError"))){
			maps.put("isError",null);
		}else{
			maps.put("isError",Integer.parseInt(map.get("isError")));
		}
		String result="";
		int num=0;
		try{
			result=dao.countNum(maps);
		}catch(Exception e){
			logger.error("Timer日志出错",e);
		}
		if(StringUtils.isEmpty(result)){
			num=0;
		}else{
			num=Integer.parseInt(result);
		}
		return num;
	}

	@Override
	public int count(Map<String, String> map, Pagination pagination) {
		Integer num=0;
		Map<String,Object> maps=new HashMap<String,Object>();
		maps.put("startTime", map.get("startTime"));
		maps.put("endTime", map.get("endTime"));
		if(StringUtil.isEmpty(map.get("operate"))){
			maps.put("operate",null);
		}else{
			maps.put("operate",Integer.parseInt(map.get("operate")));
		}
			maps.put("timerNO",map.get("timerNO"));
		if(StringUtil.isEmpty(map.get("isError"))){
			maps.put("isError",null);
		}else{
			maps.put("isError",Integer.parseInt(map.get("isError")));
		}
		try{
			num=dao.count(maps);
		}catch(Exception e){
			logger.error("Timer日志出错",e);
		}
		return num;
	}

	

}
