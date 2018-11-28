package net.ytoec.kernel.dao.impl;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.SMSBuyersGradeDao;
import net.ytoec.kernel.dataobject.SMSBuyersGrade;
import net.ytoec.kernel.mapper.SMSBuyersGradeMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * 会员等级dao层实现类
 * @author
 *
 * @param <T>
 */
@Repository
public class SMSBuyersGradeDaoImpl<T extends SMSBuyersGrade> implements SMSBuyersGradeDao<T> {

	private static final Logger logger=LoggerFactory.getLogger(SMSBuyersGradeDaoImpl.class);
	
	@Inject
	private SMSBuyersGradeMapper<T> mapper;
	
	@Override
	public boolean addSMSBuyersGrade(T grade) {

		boolean flag = false;
		try{
			mapper.addSMSBuyersGrade(grade);
			flag = true;
		} catch(Exception e){
			
			flag = false;
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public boolean delSMSBuyersGradeById(Integer id) {
		
		boolean flag = false;
		try{
			mapper.delSMSBuyersGradeById(id);
			flag = true;
		} catch(Exception e){
			
			flag = false;
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public boolean editSMSBuyersGrade(T grade) {

		boolean flag = false;
		try{
			mapper.editSMSBuyersGrade(grade);
			flag = true;
		} catch(Exception e){
			
			flag = false;
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public T getSMSBuyersGradeById(Integer id) {

		T grade = null;
		try{
			grade = mapper.getSMSBuyersGradeById(id);
		} catch(Exception e){
			
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return grade;
	}

	@Override
	public T getSMSBuyersGradeByUserId(Integer userId) {
		return mapper.getSMSBuyersGradeByUserId(userId);
	}

}
