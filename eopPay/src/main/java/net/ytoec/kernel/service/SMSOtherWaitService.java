package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.dataobject.SMSObject;

/**
 * 待发送短信表（供其他代理商（如：本草堂）使用，不受定时器发送时间的限制）
 * @author guolongchao
 * @param <T>
 */
public interface SMSOtherWaitService<T extends SMSObject> extends BaseService<T> {
	
	/**
	 * 批量编辑ec_core_sms_wait表中待发送短信的手机内容
	 * @param messageContent
	 * @param smsType
	 * true:成功；false：失败
	 */
	//public boolean updateMessageContent(String messageContent,String smsType);
	public boolean updateMessageContent(String smsType);
	
	/**
	 * 根据查询条件查询符合条件的记录
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getList(Map map) throws DataAccessException;
	/**
	 * 向待发送短信表插入记录
	 * @param SMSObject
	 * @return SMSObject
	 * 
	 */
	public int insertPhone(SMSObject smsObject); 
	/**
	 * 更新待发送短信表的sequence
	 * @param SMSObject
	 * @return SMSObject
	 * 
	 */
	public boolean updatesequenceID(SMSObject smsObject);
}
