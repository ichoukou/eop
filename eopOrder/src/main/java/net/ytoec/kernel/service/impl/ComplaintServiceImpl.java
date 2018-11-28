/**
 * net.ytoec.kernel.service.impl
 * ComplaintServiceImpl.java
 * 2012-7-10下午03:06:17
 * @author wangyong
 */
package net.ytoec.kernel.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dao.ComplaintDao;
import net.ytoec.kernel.dataobject.Complaint;
import net.ytoec.kernel.dataobject.QuestionaireExchange;
import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.ComplaintService;
import net.ytoec.kernel.service.QuestionaireExchangeService;
import net.ytoec.kernel.service.QuestionnaireService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.util.DateUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wangyong
 * 2012-7-10
 */
@Service
@Transactional
public class ComplaintServiceImpl<T extends Complaint> implements ComplaintService<T> {
	
	private static Logger logger = LoggerFactory.getLogger(ComplaintServiceImpl.class);
	
	
	@Inject
	private ComplaintDao<Complaint> dao;
	
	@Inject
	private QuestionnaireService<Questionnaire> questionnaireService;
	
	@Inject
	private UserService<User> userService;
	
	@Inject
	private QuestionaireExchangeService<QuestionaireExchange> questionaireExchangeService;

	@Override
	public boolean addComplaint(T entity) {
		return dao.addComplaint(entity);
	}

	@Override
	public String complaintQuesition(Integer[] questionIds, String complaintContent, User curUser) {
		String resultString = "";
		if(curUser.getUserType().equals("1") || curUser.getUserType().equals("11") || curUser.getUserType().equals("12") || curUser.getUserType().equals("13")){
			if(questionIds!=null && questionIds.length>0){
				if(StringUtils.isNotEmpty(complaintContent)){
					StringBuffer errorMailNo = new StringBuffer("");
					StringBuffer repeatMailNo = new StringBuffer("");
					for(Integer questionId : questionIds){
						Questionnaire question = questionnaireService.getQestionnaireById(questionId);
						if(question!=null){
							/**
							 * 首先判断该问题件是否已经投诉
							 */
							List<T> listComplaint = this.getListByMailNo(question.getMailNO());
							if(listComplaint!=null && listComplaint.size()>0){
								repeatMailNo.append(question.getMailNO()).append(",");
							}else{
								Complaint complaint = new Complaint();
								complaint.setComplaintContent(complaintContent);
								complaint.setComplaintUser(curUser.getUserNameText());
								complaint.setMailNo(question.getMailNO());
								complaint.setSiteUserCode(question.getBranchId());
								complaint.setSiteUserName(question.getBranckText());
								/**
								 * 获取沟通记录
								 */
								List<Integer> questionnaireIds = new ArrayList<Integer>();
								questionnaireIds.add(question.getId());
								List<QuestionaireExchange> questionaireExchangeList = questionaireExchangeService.getListByQuestionaireIds(questionnaireIds);
								if(!questionaireExchangeList.isEmpty()){
									StringBuffer bs = new StringBuffer("");
									Date lastTimeForUser = new Date();
									Date lastTimeForSite = new Date();
									for(QuestionaireExchange qExchange : questionaireExchangeList){
										if(qExchange.getUserId().equals(curUser.getId())){
											if(DateUtil.compareDay(qExchange.getCreateTime(), lastTimeForUser)){
												lastTimeForUser = qExchange.getCreateTime();
											}
										}else{
											if(DateUtil.compareDay(qExchange.getCreateTime(), lastTimeForSite)){
												lastTimeForSite = qExchange.getCreateTime();
											}
										}
										bs.append(qExchange.getOperatorName()).append(",").append(qExchange.getCreateTime()).append(",").append(qExchange.getMsgContent()).append(";");
									}
									complaint.setExchangeContent(bs.toString());
									complaint.setLastTimeForSite(lastTimeForSite);
									complaint.setLastTimeForUser(lastTimeForUser);
								}
								if(this.addComplaint((T)complaint)){
								}else{
									errorMailNo.append(question.getMailNO()).append(",");
								}
							}
						}
					}
					if(!(errorMailNo.toString().equals("")) && (repeatMailNo.toString().equals(""))){
						resultString = "系统繁忙，请稍后再试！";
					} else if(!(repeatMailNo.toString().equals(""))){
						resultString = "对不起，问题件"+repeatMailNo.toString()+"已投诉！";
					} else {
						resultString = "您的投诉信息已提交管理员，感谢支持！";
					}
				}else{
					resultString = "没有投诉内容!";
				}
			}else{
				resultString = "没有问题件投诉!";
			}
		}else{
			logger.error("当前用户不是卖家，没有问题件投诉。");
			resultString = "您不是卖家，无法投诉问题件!";
		}
		return resultString;
	}

	@Override
	public List<T> getListByMailNo(String mailNo) {
		if(StringUtils.isNotEmpty(mailNo)){
			return (List<T>)dao.getListByMailNo(mailNo);
		}
		return null;
	}

}
