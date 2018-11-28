/**
 * AttentionMailDaoImpl.java
 * 2011 2011-12-13 下午05:42:37
 * wangyong
 */
package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.AttentionMailDao;
import net.ytoec.kernel.dataobject.AttentionMail;
import net.ytoec.kernel.mapper.AttentionMailMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


/**
 * 
 * @author wangyong
 * TODO
 */
@Repository
@SuppressWarnings("all")
public class AttentionMailDaoImpl<T extends AttentionMail> implements AttentionMailDao<T> {

    private static Logger logger = LoggerFactory.getLogger(OrderDaoImpl.class);
    
    @Inject
    private AttentionMailMapper<AttentionMail> mapper; 
    
    @Override
    public int countPaginationList(Map map) {
        return mapper.countPaginationList(map);
    }

    @Override
    public List<T> searchByCustomerId(String customerId) {
        return (List<T>)mapper.searchByCustomerId(customerId);
    }

    @Override
    public List<T> searchByMailNo(String mailNo) {
        return (List<T>)mapper.searchByMailNo(mailNo);
    }

    @Override
    public List<T> searchPaginationList(Map map) {
        return (List<T>)mapper.searchPaginationList(map);
    }

    @Override
    public boolean add(T entity) {
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
    public boolean edit(T entity) {
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
    public T get(Integer id) {
        AttentionMail am = new AttentionMail();
        am.setId(id);
        T entity = null;
        try {
            entity =  (T)mapper.get(am);
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
        }
        return entity;
    }

    @Override
    public boolean remove(Integer id) {
        boolean flag = false;
        AttentionMail am = new AttentionMail();
        am.setId(id);
        try {
            mapper.remove(am);
            flag = true;
        } catch (Exception e) {
            flag = false;
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
        }
        return flag;
    }

	@Override
	public List<T> searchByMailNoAndCustomerId(Map map) {
		return (List<T>)mapper.searchByMailNoAndCustomerId(map);
	}
	
	@Override
	public int searchByMailNoAndCustomerIdsAndTime(Map map) {
		int listsize=0;
		 try {
			 listsize=mapper.searchWithCustomerIdMailnoTime(map);
	        } catch (Exception e) {
	        	listsize=0; 
	            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
	        }
		return listsize;
	}
	
	

}
