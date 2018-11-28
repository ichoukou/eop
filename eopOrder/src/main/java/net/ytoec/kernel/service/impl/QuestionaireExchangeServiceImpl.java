/**
 * 
 */
package net.ytoec.kernel.service.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dao.QuestionaireExchangeDAO;
import net.ytoec.kernel.dataobject.QuestionaireExchange;
import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.QuestionaireExchangeService;
import net.ytoec.kernel.service.QuestionnaireService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 问题件信息反馈service业务实现
 * @author wangyong
 * @date 2012-1-30
 */
@Service
@Transactional
public class QuestionaireExchangeServiceImpl<T extends QuestionaireExchange> implements
		QuestionaireExchangeService<T> {
	
	@Inject
	private QuestionaireExchangeDAO<T> dao;
	@Inject
	private QuestionnaireService<Questionnaire> questionnaireService;

	@Override
	public List<T> getListByQuestionaireIds(List<Integer> questionnaireIds) {
		if(questionnaireIds!=null)
			return dao.getListByQuestionaireIds(questionnaireIds);
		return null;
	}

	@Override
	public boolean add(T entry) {
		return dao.add(entry);
	}
	
	@SuppressWarnings("unused")
	@Override
	public boolean sendExchange(User curUser, T entity) {
		boolean flag = dao.add(entity);
		/**
		 * 设置其对应的问题件状态未读：需要根据不同用户类型判断
		 */
		Questionnaire ques = questionnaireService.getQestionnaireById(entity.getQuestionaireId());
		boolean editResult = false;
		if(ques!=null && flag){
			//网点用户
			if("2".equals(curUser.getUserType()) || "21".equals(curUser.getUserType()) || "22".equals(curUser.getUserType()) || "23".equals(curUser.getUserType())){
				ques.setDealStatus("2");//设置已通知
				ques.setMjIsRead("0");//设置卖家未读
				ques.setWdIsRead("1");//设置网点已读
				//如果卖家状态不存在，则设置为未处理
				if(ques.getVipStatus()==null || ("").equals(ques.getVipStatus())){
					ques.setVipStatus("1");//设置卖家未处理
				}
			}else if("1".equals(curUser.getUserType()) || "11".equals(curUser.getUserType()) || "12".equals(curUser.getUserType()) || "13".equals(curUser.getUserType()) || "4".equals(curUser.getUserType()) || "41".equals(curUser.getUserType())){
				ques.setWdIsRead("0");//设置网点未读
				ques.setMjIsRead("1");//设置卖家已读
			}
			ques.setDealTime(new Date());
			ques.setPartitionDate(new Date());
			editResult = questionnaireService.editQuestionnaire(ques);
		}
		return flag&&editResult;
	}

}
