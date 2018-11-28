package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.PaymentDao;
import net.ytoec.kernel.dataobject.Payment;
import net.ytoec.kernel.mapper.PaymentMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("all")
public class PaymentDaoImpl<T extends Payment> implements PaymentDao<T> {
	
	private static final Logger logger = LoggerFactory.getLogger(PaymentDaoImpl.class);

	@Inject
	private PaymentMapper<T> mapper;
	
	@Override
	public List<T> getPaymentList(Map map) {
		List<T> list=null;
		try{
			list = mapper.getPaymentList(map);
		}
		catch(Exception e){
			logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue(), e);	 
		}
		return list;
	}

	@Override
	public boolean updateRemarkById(T payment) {
		boolean flag=true;
		try{
			mapper.updateRemarkById(payment);
		}
		catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);	
			flag=false;
		}
		return flag;
	}

	@Override
	public boolean updateDeleteFlagById(T payment) {
		boolean flag=true;
		try{
			mapper.updateDeleteFlagById(payment);
		}
		catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);	
			flag=false;
		}
		return flag;
	}

	@Override
	public boolean add(T entity) {
		boolean flag=true;
		try{
			mapper.add(entity);
		}
		catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);	
			flag=false;
		}
		return flag;
	}

	@Override
	public boolean edit(T entity) {
		boolean flag=true;
		try{
			mapper.edit(entity);
		}
		catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);	
			flag=false;
		}
		return flag;
	}

	@Override
	public boolean remove(T entity) {
		boolean flag=true;
		try{
			mapper.remove(entity);
		}
		catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);	
			flag=false;
		}
		return flag;
	}

	@Override
	public T get(T entity) {
		try{
			return mapper.get(entity);
		}
		catch(Exception e){
			logger.error(LogInfoEnum.DATA_ACCESS_EXCEPTION.getValue(), e);	
		}
		return null;
	}

	@Override
	public List<T> getPaymentListByUserId(Map map) {
	
		return mapper.getPaymentListByUserId(map);
	}

	@Override
	public boolean updateBatchByDealStatus(Map map) {
		boolean flag=true;
		try{
			mapper.updateBatchByDealStatus(map);
		}
		catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);	
			flag=false;
		}
		return flag;
	
	}

	@Override
	public List<T> getList(Map map) {
		// TODO Auto-generated method stub
		return mapper.getList(map);
	}

	@Override
	public boolean updateDealMoneyById(T Payment) {
		boolean flag = true;
		try {
			mapper.updateDealMoneyById(Payment);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(),e);
			flag = false;
		}
		
		return flag;
	}

	@Override
	public T getPaymentById(Map map) {
		return mapper.getPaymentById(map);
	}

	@Override
	public Integer getPaymentListCount(Map map) {
		// TODO Auto-generated method stub
		return mapper.getPaymentListCount(map);
	}

}
