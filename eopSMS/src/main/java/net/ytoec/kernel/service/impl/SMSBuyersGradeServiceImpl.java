package net.ytoec.kernel.service.impl;

import javax.inject.Inject;

import net.ytoec.kernel.dao.SMSBuyersGradeDao;
import net.ytoec.kernel.dataobject.SMSBuyersGrade;
import net.ytoec.kernel.service.SMSBuyersGradeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@SuppressWarnings("all")
public class SMSBuyersGradeServiceImpl implements SMSBuyersGradeService {

	private static final Logger logger = LoggerFactory.getLogger(SMSBuyersGradeServiceImpl.class);
	
	@Inject
	private SMSBuyersGradeDao sMSBuyersGradeDao;
	
	@Override
	public boolean addSMSBuyersGrade(SMSBuyersGrade grade) {
		
		if(grade == null){
			logger.error("##添加买家等级时传入参数为空！");
			return false;
		}
		return sMSBuyersGradeDao.addSMSBuyersGrade(grade);
	}

	@Override
	public boolean delSMSBuyersGradeById(Integer id) {

		if("".equals(id) || id == null){
			logger.error("##删除会员等级时传入参数为空！");
			return false;
		}
		return sMSBuyersGradeDao.delSMSBuyersGradeById(id);
	}

	@Override
	public boolean editSMSBuyersGrade(SMSBuyersGrade grade) {
		
		if(grade == null){
			logger.error("##修改会员等级时传入参数为空！");
			return false;
		}
		return sMSBuyersGradeDao.editSMSBuyersGrade(grade);
	}

	@Override
	public SMSBuyersGrade getSMSBuyersGradeById(Integer id) throws Exception {

		if(id == null || "".equals(id)){
			throw new Exception("##根据ID查询会员等级时传参为空");
		}
		
		return (SMSBuyersGrade)sMSBuyersGradeDao.getSMSBuyersGradeById(id);
	}
	
}
