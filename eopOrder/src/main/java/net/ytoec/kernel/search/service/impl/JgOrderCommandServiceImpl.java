package net.ytoec.kernel.search.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dao.JgOrderCommandDao;
import net.ytoec.kernel.dto.JGOrderDTO;
import net.ytoec.kernel.search.service.JgOrderCommandService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.oscache.util.StringUtil;

@Service
@Transactional
@SuppressWarnings("all")
public class JgOrderCommandServiceImpl<T extends JGOrderDTO> implements JgOrderCommandService<T> {
	private static Logger logger = LoggerFactory.getLogger(JgOrderCommandServiceImpl.class);

    @Inject
    private JgOrderCommandDao<T> dao;

	@Override
	public List<T> getJGOrderByLimit(Integer n) {
		 return this.dao.getJGOrderByLimit(n);
	}
	@Override
	public Boolean removeJGOrderById(Integer minId,Integer maxId) {
			Map<String,Integer> map=new HashMap<String,Integer>();
			map.put("minId", minId);
			map.put("maxId", maxId);
	        return dao.removeJGOrderById(map);
	}

	@Override
	public boolean addJGOrder(T bean) {
		return dao.addJGOrder(bean);
	}

	@Override
	public List<T> getJGOrder(Map<String, String> map,Pagination pagination) {
		List<JGOrderDTO> list=(List<JGOrderDTO>) new ArrayList<T>();
		Map<String,Object> maps=new HashMap<String,Object>();
		maps.put("startTime", map.get("startTime"));
		maps.put("endTime", map.get("endTime"));
		if(StringUtil.isEmpty(map.get("isOffline"))){
			maps.put("isOffline",null);
		}else{
			maps.put("isOffline",Integer.parseInt(map.get("isOffline")));
		}
		if(StringUtil.isEmpty(map.get("orderType"))){
			maps.put("orderType",null);
		}else{
			maps.put("orderType",Integer.parseInt(map.get("orderType")));
		}
		if(StringUtil.isEmpty(map.get("commandType"))){
			maps.put("commandType",null);
		}else{
			maps.put("commandType",Integer.parseInt(map.get("commandType")));
		}
		
		maps.put("startIndex", pagination.getStartIndex());
		maps.put("pageNum", pagination.getPageNum());
		try{
			list=(List<JGOrderDTO>) dao.getJGOrder(maps);
		}catch(Exception e){
			logger.error("Timer日志出错",e);
		}
		return (List<T>) list;
	}
	@Override
	public int count(Map<String, String> map, Pagination pagination) {
		int count=0;
		Map<String,Object> maps=new HashMap<String,Object>();
		maps.put("startTime", map.get("startTime"));
		maps.put("endTime", map.get("endTime"));
		if(StringUtil.isEmpty(map.get("isOffline"))){
			maps.put("isOffline",null);
		}else{
			maps.put("isOffline",Integer.parseInt(map.get("isOffline")));
		}
		if(StringUtil.isEmpty(map.get("orderType"))){
			maps.put("orderType",null);
		}else{
			maps.put("orderType",Integer.parseInt(map.get("orderType")));
		}
		if(StringUtil.isEmpty(map.get("commandType"))){
			maps.put("commandType",null);
		}else{
			maps.put("commandType",Integer.parseInt(map.get("commandType")));
		}
		try{
			count=dao.count(maps);
		}catch(Exception e){
			logger.error("Timer日志出错",e);
		}
		return count;
	}

}
