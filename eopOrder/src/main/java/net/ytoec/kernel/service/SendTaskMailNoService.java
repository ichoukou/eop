package net.ytoec.kernel.service;

import java.util.List;

/**
 * 通知仓配平台面单号
 * @author mabo
 *
 */
public interface SendTaskMailNoService<T> {

	/**
	 * 获取发送对象集合
	 * @param limit
	 * @return
	 */
	public List<T> getMailNoListByLimit(Integer limit);
	
	/**
	 * 删除发送成功的运单号
	 * @param mailNo
	 * @param clientId
	 * @return
	 */
	public Integer removeSendTask(String mailNo,String clientId);
	
	/**
	 * 插入运单号
	 * @param mailNo
	 * @return
	 */
	public Integer addMailNo(String mailNo, String clientId);
}
