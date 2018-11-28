package net.ytoec.kernel.dao.impl;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.QuestionnaireTagDao;
import net.ytoec.kernel.dataobject.QuestionnaireTag;
import net.ytoec.kernel.mapper.QuestionnaireTagMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 问题件标签DAO接口实现
 * @author wangjianzhong
 * @2012-8-2
 * @param <T>
 */

@Service
@Transactional
public class QuestionnaireTagDaoImpl<T extends QuestionnaireTag> implements QuestionnaireTagDao<T> {

	private static Logger logger = LoggerFactory.getLogger(QuestionnaireTagDaoImpl.class);
	
	@Inject
	private QuestionnaireTagMapper<QuestionnaireTag> mapper; 
	
	@Override
	public boolean addQuestionnaireTag(T entity) {	
		boolean flag = true;
		try{
			mapper.addQuestionnaireTag(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}
	
	@Override
	public Integer getTagCountInQuestionnaire(Integer id) {
		Integer count = null;
		try{
			count = mapper.getTagCountInQuestionnaire(id);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return count;
	}

	@Override
	public boolean removeQuestionnaireTag(Integer id) {
		boolean flag = true;
		try{
			mapper.removeQuestionnaireTag(id);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean editQuestionnaireTag(T entity) {
		boolean flag = true;
		try{
			mapper.editQuestionnaireTag(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public T getQestionnaireTag(Integer id) {
		T entity = null;
		try{
			List<T> entitys = (List<T>) mapper.getQestionnaireTag(id);
			entity = entitys.get(0);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return entity;
	}

	@Override
	public List<T> getQestionnaireTagsByUserThreadId(Integer userThreadId) {
		List<T> entitys = null;
		try{
			entitys = (List<T>) mapper.getQestionnaireTagsByUserThreadId(userThreadId);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return entitys;
	}

	@Override
	public Integer getQestionnaireTagsCountByUserThreadId(Integer userThreadId) {
		Integer count = null;
		try{
			count = mapper.getQestionnaireTagsCountByUserThreadId(userThreadId);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return count;
	}

	@Override
	public List<T> getQestionnaireTags(T entity) {
		List<T> entitys = null;
		try{
			entitys = (List<T>) mapper.getQestionnaireTags(entity);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return entitys;
	}

	@Override
	public Integer getQestionnaireTagMaxPos(Integer userThreadId) {
		Integer max = null;
		try{
			max = mapper.getQestionnaireTagMaxPos(userThreadId);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return max;
	}

	/*@Override
	public Integer getTagCountInQestionnaire(Map params) {
		Integer count = null;
		try{
			count = mapper.getTagCountInQestionnaire(params);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return count;
	}*/
	
}
