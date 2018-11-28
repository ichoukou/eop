package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dao.SMSOtherWaitDao;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.mapper.SMSOtherWaitMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

/**
 * 待发送短信表（供其他模块使用，不受定时器发送时间的限制）
 */
@Repository
public class SMSOtherWaitDaoImpl<T extends SMSObject> implements
		SMSOtherWaitDao<T> {

	@Inject
	private SMSOtherWaitMapper<T> mapper;

	private static final Logger logger = LoggerFactory.getLogger(SMSOtherWaitDaoImpl.class);

	@Override
	public boolean insertBatchToSMS() {
		// TODO Auto-generated method stub
		boolean flag = true;
		try {
			mapper.insertBatchToSMS();
		} catch (Exception e) {
			logger.error("手机号由ec_core_phone_temp导入到ec_core_sms_wait批量导入时出错...");
			flag = false;
		}
		return flag;

	}

	@Override
	public boolean deleteByStatus(String status) {
		// TODO Auto-generated method stub
		boolean flag = true;
		try {
			mapper.deleteByStatus(status);
		} catch (Exception e) {
			logger.error("批量删除ec_core_sms_wait,status=" + status + "时出错...");
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean editBatch(Map map) {
		// TODO Auto-generated method stub
		boolean flag = true;
		try {
			mapper.editBatch(map);
		} catch (Exception e) {
			logger.error("批量更新ec_core_sms_wait的手机内容时出错...");
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean add(T entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean edit(T entity) {
		// TODO Auto-generated method stub
		boolean flag = true;
		try {
			mapper.edit(entity);
		} catch (Exception e) {
			logger.error("更新ec_core_sms_wait时出错...id=" + entity.getId());
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean remove(T entity) {
		// TODO Auto-generated method stub
		boolean flag = true;
		try {
			mapper.remove(entity);
		} catch (Exception e) {
			logger.error("删除ec_core_sms_wait时出错...id=" + entity.getId());
			flag = false;
		}
		return flag;
	}

	@Override
	public T get(T entity) {
		// TODO Auto-generated method stub
		return mapper.get(entity);
	}

	@Override
	public List<T> getList(Map map) throws DataAccessException {
		// TODO Auto-generated method stub
		return (List<T>) mapper.getList(map);
	}

	@Override
	public int insertPhone(SMSObject smsObject) {
		return mapper.insertPhone(smsObject);
	}

	@Override
	public SMSObject searchMaxId() {
		SMSObject SMS = new SMSObject();
		try {
			SMS = mapper.searchMaxId();
		} catch (Exception e) {
			logger.error("查询ec_core_sms_wait的最大id时出错了...");
		}
		return SMS;
	}

	@Override
	public boolean updatesequenceID(SMSObject smsObject) {
		boolean flag = true;
		try {
			mapper.updatesequenceID(smsObject);
		} catch (Exception e) {
			logger.error("更新ec_core_sms_wait序列时出错...");
			flag = false;
		}
		return flag;
	}
}
