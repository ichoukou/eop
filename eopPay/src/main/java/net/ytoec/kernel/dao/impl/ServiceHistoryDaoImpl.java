package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.ServiceHistoryDao;
import net.ytoec.kernel.dataobject.ServiceHistory;
import net.ytoec.kernel.mapper.ServiceHistoryMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * 服务历史ServiceHistoryDao实现类
 * @author guolongchao
 * @date 2012-07-03
 */
@Repository
@SuppressWarnings("all")
public class ServiceHistoryDaoImpl <T extends ServiceHistory> implements ServiceHistoryDao<T>{

	private static final Logger logger = LoggerFactory.getLogger(ServiceHistoryDaoImpl.class);

	@Inject
	private ServiceHistoryMapper<ServiceHistory> mapper;
	
	@Override
	public List<T> getServiceHistoryList(Map map) {
		  if( map.get("userId")==null){
			  return null;
		  }
		 List<T> list=null;
	     try 
	     {
	    	 list=(List<T>) mapper.getServiceHistoryList(map);	
	    	 
	     } catch (Exception e) {
	        logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue(), e);	       
	     }
	     return list;
	}
	@Override
	public List<T> getServiceHistoryListByDUserId(Map map) {
		  if( map.get("userId")==null){
			  return null;
		  }
	     return (List<T>) mapper.getServiceHistoryListByDUserId(map);
	}
	
	
	@Override
	public boolean add(T entity) {
		boolean flag = true;
        try {        	 
        	 mapper.add(entity);
           
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
            flag = false;
        }
        return flag;
	}

	@Override
	public boolean edit(T entity) {

		boolean flag = true;
        try {        	 
        	 mapper.edit(entity);
           
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
            flag = false;
        }
        return flag;
	}

	@Override
	public boolean remove(T entity) {

		boolean flag = true;
        try {        	 
        	 mapper.remove(entity);
           
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
            flag = false;
        }
        return flag;
	}

	@Override
	public T get(T entity) {
	   try 
	   {        	
	       return  (T) mapper.get(entity);
	        	
	   } catch (Exception e) {
	       logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue(), e);
	   }
	   return null;
	}



	
}
