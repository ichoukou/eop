package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.PayServiceDao;
import net.ytoec.kernel.dataobject.PayService;
import net.ytoec.kernel.mapper.PayServiceMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;



@Repository
@SuppressWarnings("all")
public class PayServiceDaoImpl<T extends PayService> implements PayServiceDao<T>{

	private static final Logger logger = LoggerFactory.getLogger(PayServiceDaoImpl.class);

	@Inject
	private PayServiceMapper<T> mapper;

	@Override
	public List<T> getNOpenserviceList(Map map) {
		 List<T> list=null;
	     try 
	     {
	    	 list=(List<T>) mapper.getNOpenserviceList(map);	
	    	 
	     } catch (Exception e) {
	        logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue(), e);	       
	     }
	     return list;
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

	@Override
	public List<T> getFreeServiceList() {
		List<T> list=null;
	     try {
	    	 list=(List<T>) mapper.getFreeServiceList();
	     } catch (Exception e) {
	        logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue(), e);	       
	     }
	     return list;
	}

	@Override
	public List<T> getNFreeserviceList() {
		return mapper.getNFreeserviceList();
	}

	@Override
	public List<T> getAllOpenServiceList() {
		// TODO Auto-generated method stub
		return mapper.getAllOpenServiceList();
	}
}
