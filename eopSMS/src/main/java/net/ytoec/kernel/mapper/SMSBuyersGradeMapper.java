package net.ytoec.kernel.mapper;

import net.ytoec.kernel.dataobject.SMSBuyersGrade;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 会员等级Mapper
 * @author 
 *
 */
public interface SMSBuyersGradeMapper<T extends SMSBuyersGrade> extends BaseSqlMapper<T> {

	/**
	 * 根据用户ID获取会员划分等级
	 * @param userId
	 * @return
	 */
	public T getSMSBuyersGradeByUserId(Integer userId);
	
	/**
	 * 添加会员等级
	 * @param grade
	 */
	public void addSMSBuyersGrade(T smsBuyersGrade);

	/**
	 * 根据ID删除会员等级
	 * @param id
	 */
	public void delSMSBuyersGradeById(Integer buyerId);

	/**
	 * 编辑会员等级
	 * @param grade
	 */
	public void editSMSBuyersGrade(T smsBuyersGrade);

	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	public T getSMSBuyersGradeById(Integer buyerId);

}
