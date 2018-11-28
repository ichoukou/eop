package net.ytoec.kernel.dao;

import net.ytoec.kernel.dataobject.ZebraSequence;

/**
 * 获取电子面单的请求序列dao
 * 
 * @author huangtianfu
 * 
 */
public interface ZebraSequenceDao<T> {
	/**
	 * @作者：罗典
	 * @时间：2013-08-30
	 * @描述：判断商家时间段内是否某状态的序列,值为null时不筛选。
	 * @参数：customerCode 商家编码，state：序列状态，timeInterval：时间区间(分钟)
	 * */
	public Boolean isExistSequence(String customerCode, int state,
			int timeInterval);

	/**
	 * @作者：罗典
	 * @时间：2013-09-01
	 * @描述：根据序列号查询有效的序列信息
	 * @参数：sequenceId序列号
	 * */
	public ZebraSequence queryZebraSequence(String sequenceId) ;

	/**
	 * @作者：罗典
	 * @时间：2013-09-01
	 * @描述：根据序列号修改序列信息
	 * @参数：sequenceId序列号,state 状态
	 * */
	public int updateStateById(String sequenceId,int state);
	
	
	int insertZebraSequence(ZebraSequence zebraSequence);

	/**
	 * 更新同步状态
	 * 
	 * @param newSequence
	 *            请求序列
	 * @param customerCode
	 *            商家代码
	 * @param state
	 *            同步状态(0:默认第一次次请求失败，1:第一次请求成功，2:第二次请求成功)
	 * @return
	 */
	boolean updateState(int newSequence, String customerCode, int state);

	/**
	 * 查询商家最近一次状态为第一次请求中的同步时间
	 * 
	 * @param customerCode
	 *            商家代码
	 * @return
	 */
	String selectLastSyningTime(String customerCode);

	/**
	 * 设置该请求序列的第二次请求时间
	 * 
	 * @param sequence
	 *            请求序列
	 * @param customerCode
	 *            商家代码
	 * @return
	 */
	boolean updateSecondCreateTime(String sequence, String customerCode);

}
