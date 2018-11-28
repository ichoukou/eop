package net.ytoec.kernel.dao.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.QuestionaireExchangeDAO;
import net.ytoec.kernel.dataobject.QuestionaireExchange;
import net.ytoec.kernel.mapper.QuestionaireExchangeMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionaireExchangeDAOImpl<T extends QuestionaireExchange> implements QuestionaireExchangeDAO<T> {

    private static Logger logger = LoggerFactory.getLogger(QuestionaireExchangeDAOImpl.class);
    @Autowired
    private QuestionaireExchangeMapper<T> mapper;

    @SuppressWarnings("unchecked")
    @Override
    public List<T> getListByQuestionaireIds(List<Integer> questionnaireIds) {
        if (questionnaireIds == null || questionnaireIds.isEmpty()) {
            logger.error(LogInfoEnum.PARAM_EMPTY.getValue());
            return Collections.EMPTY_LIST;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("questionnaireIds", questionnaireIds);
        return this.mapper.getListByQuestionaireIds(map);
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

}
