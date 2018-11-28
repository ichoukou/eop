package net.ytoec.kernel.dao;

/**
 * 会员等级dao层
 * @author 
 *
 * @param <T>
 */
public interface SMSBuyersGradeDao<T> {

	/**
	 * 根据用户ID获取会员划分等级
	 * @param userId
	 * @return
	 */
	public T getSMSBuyersGradeByUserId(Integer userId);
	
	/**
	 * 添加会员等级
	 * @param grade
	 * @return
	 */
	public boolean addSMSBuyersGrade(T grade);

	/**
	 * 根据id删除会员等级
	 * @param id
	 * @return
	 */
	public boolean delSMSBuyersGradeById(Integer id);

	/**
	 * 编辑会员等级
	 * @param grade
	 * @return
	 */
	public boolean editSMSBuyersGrade(T grade);

	/**
	 * 根据ID查询会员等级
	 * @param id
	 * @return
	 */
	public Object getSMSBuyersGradeById(Integer id);

}
