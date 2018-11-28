package net.ytoec.kernel.dao.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.QuestionaireRemarkDAO;
import net.ytoec.kernel.dataobject.QuestionaireRemark;
import net.ytoec.kernel.mapper.QuestionaireRemarkMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionaireRemarkDAOImpl<T extends QuestionaireRemark> implements QuestionaireRemarkDAO<T> {

    private static Logger logger = LoggerFactory.getLogger(QuestionaireExchangeDAOImpl.class);
    @Autowired
    private QuestionaireRemarkMapper<T> mapper;

    @SuppressWarnings("unchecked")
    @Override
    public List<T> getListByQuestionaireIdsAndReadStatus(Map<String, Object> map) {
        if (map.isEmpty()) {
            logger.error(LogInfoEnum.PARAM_EMPTY.getValue());
            return Collections.EMPTY_LIST;
        }
        return this.mapper.getListByQuestionaireIdsAndReadStatus(map);
    }

    @Override
    public boolean add(T entity) {
        boolean result = false;
        if (entity == null) {
            logger.error(LogInfoEnum.PARAM_EMPTY.getValue());
        }
        try {
            this.mapper.add(entity);
            result = true;
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
        }
        return result;
    }
    
    @Override
    public boolean edit(T entity) {
    	boolean result = false;
    	if (entity == null) {
    		logger.error(LogInfoEnum.PARAM_EMPTY.getValue());
    	}
    	try {
    		this.mapper.edit(entity);
    		result = true;
    	} catch (Exception e) {
    		logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
    	}
    	return result;
    }
    
    @Override
    public List<T> findByQuestionId(T entity) {
    	if (entity==null) {
            logger.error(LogInfoEnum.PARAM_EMPTY.getValue());
            return Collections.EMPTY_LIST;
        }
        return this.mapper.findByQuestionId(entity);
    }
}
