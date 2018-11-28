package net.ytoec.kernel.service;

import net.ytoec.kernel.dataobject.SMSBuyersGrade;

/**
 *  会员等级service
 * @author 
 *
 */
public interface SMSBuyersGradeService {
	
	/**
	 * 添加等级
	 * @param grade
	 * @return
	 */
	public boolean addSMSBuyersGrade(SMSBuyersGrade smsBuyersGrade);
	
	/**
	 * 根据ID删除等级
	 * @param id
	 * @return
	 */
	public boolean delSMSBuyersGradeById(Integer buyersId);
	
	/**
	 * 根据ID修改等级
	 * @param grade
	 * @return
	 */
	public boolean editSMSBuyersGrade(SMSBuyersGrade smsBuyersGrade);
	
	/**
	 * 根据ID查询等级对象
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public SMSBuyersGrade getSMSBuyersGradeById(Integer buyersId) throws Exception;
}
