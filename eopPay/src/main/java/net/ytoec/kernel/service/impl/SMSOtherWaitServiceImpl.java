package net.ytoec.kernel.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.TechcenterEnum;
import net.ytoec.kernel.dao.SMSOtherWaitDao;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.service.SMSOtherWaitService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("sMSOtherWaitServiceImpl")
@Transactional
@SuppressWarnings("all")
public class SMSOtherWaitServiceImpl<T extends SMSObject> implements SMSOtherWaitService<T> {

	@Inject
	private SMSOtherWaitDao<SMSObject> sMSOtherWaitDao;

	private static final Logger logger = LoggerFactory.getLogger(SMSOtherWaitServiceImpl.class);

	@Override
	public boolean add(T entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean edit(T entity) {
		// TODO Auto-generated method stub
		return sMSOtherWaitDao.edit(entity);
	}

	@Override
	public boolean remove(T entity) {
		// TODO Auto-generated method stub
		return sMSOtherWaitDao.remove(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(T entity) {
		// TODO Auto-generated method stub
		return (T) sMSOtherWaitDao.get(entity);
	}

	
	/**
	 * 批量编辑ec_core_sms_wait表中待发送短信的手机内容
	 * @param messageContent
	 * @param smsType
	 */
	@Override
	public boolean updateMessageContent(String smsType) {
		// TODO Auto-generated method stub		
//		if(messageContent==null||messageContent.trim().length()<=0)
//		{
//			logger.error("待发送手机内容不能为null");
//			return false;
//		}
		boolean flag = true;
		//1. 删除ec_core_sms_wait表中所有status==8待发送短信
//		flag = sMSOtherWaitDao.deleteByStatus(TechcenterEnum.TECHCENTERFLAG.WAIT.getValue());
//		if(flag==false) {
//			logger.error("删除ec_core_sms_wait表中所有status==8待发送短信失败了");
//			return false;
//		}
		//2. 将临时表ec_core_phone_temp中的数据去除重复的手机号后导入待发送短信表ec_core_sms_wait 
	    //flag = sMSOtherWaitDao.insertBatchToSMS();
		//直接解析文本文件  插入数据库
		
//		if(flag==false) {
//			logger.error("将临时表ec_core_phone_temp中的数据去除重复的手机号后导入待发送短信表ec_core_sms_wait失败了");
//			return false;
//		}
		//计算实际发送的短信数
//		int count = messageContent.trim().length();
//		if(count<=70) {
//			count = 1;
//		}else{
//			count = count/67+1;
//		} 
		//3. 更新短信内容
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("smsType", smsType);//模块的类型   
//		map.put("pkTotal", count);
//		map.put("messageContent", messageContent);
		map.put("status", TechcenterEnum.TECHCENTERFLAG.WAIT.getValue());
		
		flag = sMSOtherWaitDao.editBatch(map);
		if(flag==false) {
			logger.error("更新短信内容待发送短信表ec_core_sms_wait失败了");
			return false;
		}
		return flag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getList(Map map) throws DataAccessException {
		// TODO Auto-generated method stub
		return (List<T>) sMSOtherWaitDao.getList(map);
	}

	@Override
	public int insertPhone(SMSObject smsObject) {
		return sMSOtherWaitDao.insertPhone(smsObject);
	}

	@Override
	public boolean updatesequenceID(SMSObject smsObject) {
		return sMSOtherWaitDao.updatesequenceID(smsObject);
	}
}
