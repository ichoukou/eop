package net.ytoec.kernel.dao;

import java.util.List;

/**
 * 通知仓配平台面单号dao
 * @author mabo
 *
 */
public interface SendTaskMailNoDao<T> {

	/**
	 * 获取发送对象集合
	 * @param limit
	 * @return
	 */
	public List<T> getMailNoListByLimit(Integer limit);
	
	/**
	 * 插入运单号
	 * @param mailNo
	 * @return
	 */
	public Integer addMailNo(String mailNo, String clientId);
	
	/**
	 * 删除发送成功的运单号
	 * @param mailNo
	 * @param clientId
	 * @return
	 */
	public Integer removeMailNo(String mailNo,String clientId);
}
