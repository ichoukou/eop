package net.ytoec.kernel.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.ytoec.kernel.dao.SendTaskMailNoDao;
import net.ytoec.kernel.dataobject.SendTask;
import net.ytoec.kernel.service.SendTaskMailNoService;
/**
 * 
 * @author mabo
 *
 * @param <T>
 */

@Service
@Transactional
public class SendTaskMailNoServiceImpl<T extends SendTask> implements SendTaskMailNoService<T> {
	
	@Autowired
	private SendTaskMailNoDao<T> sendTaskMailNoDao;
	
	@Override
	public List<T> getMailNoListByLimit(Integer limit) {
		// TODO Auto-generated method stub

		return sendTaskMailNoDao.getMailNoListByLimit(limit);
	}

	@Override
	public Integer removeSendTask(String mailNo, String clientId) {
		// TODO Auto-generated method stub
		return sendTaskMailNoDao.removeMailNo(mailNo, clientId);
	}

	@Override
	public Integer addMailNo(String mailNo, String clientId) {
		// TODO Auto-generated method stub
		return sendTaskMailNoDao.addMailNo(mailNo, clientId);
	}

}
