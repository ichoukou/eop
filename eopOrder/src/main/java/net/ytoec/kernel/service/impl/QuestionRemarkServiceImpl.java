/**
 * 
 */
package net.ytoec.kernel.service.impl;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dao.QuestionaireRemarkDAO;
import net.ytoec.kernel.dataobject.QuestionaireRemark;
import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.service.QuestionRemarkService;
import net.ytoec.kernel.service.QuestionnaireService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 问题件备注service业务实现
 * @author wangyong
 * @date 2012-1-30
 */
@Service
@Transactional
public class QuestionRemarkServiceImpl<T extends QuestionaireRemark> implements
		QuestionRemarkService<T> {
	
	@Inject
	private QuestionaireRemarkDAO<T> dao;
	@Inject
	private QuestionnaireService<Questionnaire> questionnaireService;
	
	
	@Override
	public boolean addQuestionRemark(T entity) {
		return dao.add(entity);
	}

	/* (non-Javadoc)
	 * @see net.ytoec.kernel.service.QuestionRemarkService#findByQuestionId(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByQuestionId(Integer questionaireId) {
		if(questionaireId!=null){
			QuestionaireRemark remark = new QuestionaireRemark();
			remark.setQuestionaireId(questionaireId);
			return dao.findByQuestionId((T)remark);
		}
		return null;
	}
	
	@Override
	public boolean sendRemark(T entity, int sendFlag) {
		boolean flag = false;
		if(sendFlag==0)//0表示添加新备注
			flag = dao.add(entity);
		else//1表示修改备注
			flag = dao.edit(entity);
		Questionnaire ques = questionnaireService.getQestionnaireById(entity.getQuestionaireId());
		/**
		 * 设置对应的问题件读取状态改为已读
		 */
		if(ques!=null){
			ques.setMjIsRead("1");
		}
		boolean editFlag = questionnaireService.editQuestionnaire(ques);
		return flag&&editFlag;
	}

}
