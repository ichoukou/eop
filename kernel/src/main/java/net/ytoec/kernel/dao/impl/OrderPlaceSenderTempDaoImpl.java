package net.ytoec.kernel.dao.impl;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.OrderPlaceSenderTempDao;
import net.ytoec.kernel.dataobject.OrderPlaceSenderTemp;
import net.ytoec.kernel.mapper.OrderPlaceSenderTempMapper;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class OrderPlaceSenderTempDaoImpl implements OrderPlaceSenderTempDao {

	private Logger logger=Logger.getLogger(OrderPlaceSenderTempDaoImpl.class);
	
	@Inject
	private OrderPlaceSenderTempMapper mapper;

	@Override
	public List<OrderPlaceSenderTemp> getByUserId(Integer id) {
		List<OrderPlaceSenderTemp> senders = null;
		try{
			senders = mapper.getByUserId(id);
		}catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return senders;
	}

	@Override
	public boolean addTemp(OrderPlaceSenderTemp temp) {
		boolean bool = false;
		try{
			mapper.addTemp(temp);
			bool = true;
		}catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return bool;
	}

	@Override
	public boolean eidtTemp(OrderPlaceSenderTemp temp) {
		boolean bool = false;
		try{
			mapper.eidtTemp(temp);
			bool = true;
		}catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return bool;
	}
	
	
}
