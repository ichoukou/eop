/**
 * 
 */
package net.ytoec.kernel.dao.impl;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.QuestionnaireDao;
import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.mapper.QuestionnaireMapper;
import net.ytoec.kernel.util.DateUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
/**
 * 问题单DAO层实现类
 * @author Wangyong
 * @2011-8-1
 * net.ytoec.kernel.dao.impl
 * @param <T>
 */
@Repository
public class QuestionnaireDaoImpl<T extends Questionnaire> implements QuestionnaireDao<T> {

	private static Logger logger = LoggerFactory.getLogger(QuestionnaireDaoImpl.class);
	@Inject
	private QuestionnaireMapper<Questionnaire> mapper;
	
	@Override
	public boolean addQuestionnaire(T questionnaire) {
		
		boolean flag = false;
		try{
			mapper.add(questionnaire);
			flag = true;
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean removeQuestionnaire(T questionnaire) {
		
		boolean flag = false;
		try{
			mapper.remove(questionnaire);
			flag = true;
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean editQuestionnaire(T questionnaire) {
		
		boolean flag = false;
		try{
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DATE, -7);
			String partitiondate=DateUtil.format(cal.getTime(), "yyyy-MM-dd");
			questionnaire.setOtherPartitionDate(partitiondate);
			mapper.edit(questionnaire);
			flag = true;
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public T getQuestionnaireById(Integer id) {
		
		T entity = null;
		try{
			Questionnaire question = new Questionnaire();
			question.setId(id);
			entity = (T)mapper.get(question);
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return entity;
	}

	@Override
	public boolean dealQuestionnaire(T questionnaire) {
		
		boolean flag = false;
		try{
			((QuestionnaireMapper)mapper).dealQuestionnaire(questionnaire);
			flag = true;
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public List<T> getQuestionnaireByBranchId(Map map) {
		
		List<T> list = null;
		try{
			list = ((QuestionnaireMapper)mapper).getQuestionnaireListByBranchId(map);
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public List<T> getAllQuestionnaire() {
		
		List<T> list = null;
		try{
			list = ((QuestionnaireMapper)mapper).getAllQuestionnaire();
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}


	@Override
	public List<T> getNoVipIdList(Map map) {
		
		List<T> list = null;
		try{
			list = ((QuestionnaireMapper)mapper).getNoVipIdListByMapSearch(map);
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public void updateVipId(T entity) {
		try {
			((QuestionnaireMapper) mapper).updateVipId(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
	}

	@Override
	public List<T> getQsnByMailNo(String mailNO) throws DataAccessException {
	    List<T> quesList = null;
		try{
			quesList = (List<T>) mapper.getQsnByMailNo(mailNO);
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return quesList;
	}

	@Override
	public T getQsnByIssueId(String issueId) {
		T entity = null;
		try{
			entity = (T) mapper.getQsnByIssueId(issueId);
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return entity;
	}
	
	@Override
	public List<T> queryQuestionnaireManageList(Map map){
		List<T> list = null;
		try{
			list = ((QuestionnaireMapper)mapper).queryQuestionnaireManageList(map);
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}
	
	@Override
	public int countQuestionnaireManageList(Map map){
		return mapper.countQuestionnaireManageList(map);
	}

    @Override
    public void batchUpdateQuestionnaire(List<Questionnaire> questionnaires) {
        // TODO Auto-generated method stub
        try {
            if (questionnaires ==null || questionnaires.isEmpty()) {
                return;
            }

            Integer result = 0;
            Questionnaire ques = new Questionnaire();
            for (int i = 0; i < questionnaires.size(); i++) {
                ques = questionnaires.get(i);
                if (StringUtils.isEmpty(ques.getIssueStatus())){
                	continue;
                    
                }
                result = mapper.updateQuestionnaireIssueStatus(ques);
            }
        } catch (Exception e) {
            logger.error("问题件更新失败", e);
        }
    }

    @Override
    public List<Questionnaire> getQuestionnaireByDealTime(Date dealTime, Integer limit) {
        // TODO Auto-generated method stub
        List<Questionnaire> list = null;
        if (limit == null || dealTime==null) {
            return Collections.EMPTY_LIST;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("dealTime", dealTime);
        map.put("limit", limit);

        try {
            list = mapper.getQuestionnaireByDealTime(map);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("根据dealtime查询失败");
        }

        return list;
    }

    @Override
    public List<Questionnaire> getQestionnaireById(Integer id, Integer limit) {
        // TODO Auto-generated method stub
        List<Questionnaire> list = null;
        if (limit == null ) {
            return Collections.EMPTY_LIST;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("limit", limit);

        try {
            list = mapper.getQuestionnaireById(map);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("根据id查询失败");
        }

        return list;
    }

    @Override
    public Integer updateIssueStatusByissueId(Questionnaire questionnaire) {
        Integer rows=0;
        if(questionnaire==null||StringUtils.isEmpty(questionnaire.getIssueId())){
            return rows;
        }
        rows=mapper.updateIssueStatusByissueId(questionnaire);
        return rows;
    }

	@Override
	public void updateBuyerNickToQuestionaire(Map<String, Object> params) {
			mapper.updateBuyerNickToQuestionaire(params);
	}

	@Override
	public Integer updateOrderStatusByMailNo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
		return mapper.updateOrderStatusByMailNo(map);
	}

	@Override
	public String wayQuestionnaireManageList(Map map) {
		
		String mailNo = null;
		try{
			mailNo = ((QuestionnaireMapper)mapper).wayQuestionnaireManageList(map);
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return mailNo;
		
	}

	@Override
	public List<Questionnaire> selectQuesByCustomerId(Map map) {
		List<Questionnaire> list = null;
		try{
			list = mapper.selectQuesByCustomerId(map);
		} catch(Exception e){
			logger.error("根据customerId查询问题件失败,customerId="+map.get("customerId"));
		}
		return list;
	}

	@Override
	public Integer updateQuesById(Map<String, Object> params) {
		
		return mapper.updateQuesById(params);
	}

	@Override
	public Integer updateIssueStatusById(Questionnaire questionnaire) {
		Integer rows=0;
        if(questionnaire==null||StringUtils.isEmpty(questionnaire.getIssueId())){
            return rows;
        }
        rows=mapper.updateIssueStatusById(questionnaire);
        return rows;
	}

}
