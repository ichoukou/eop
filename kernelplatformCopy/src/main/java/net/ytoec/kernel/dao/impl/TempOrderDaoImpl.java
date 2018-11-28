package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dao.TempOrderDao;
import net.ytoec.kernel.dataobject.TempOrder;
import net.ytoec.kernel.mapper.TempOrderMapper;

import org.springframework.stereotype.Repository;

/**
 * 订单临时表的dao处理
 * @author wangyong
 * 2012-5-14
 * @param <T>
 */
@Repository
public class TempOrderDaoImpl<T extends TempOrder> implements TempOrderDao<T> {

	@Inject
	private TempOrderMapper<T> mapper;
	
	@Override
	public boolean add(T entity) {
		boolean flag = false;
		try{
			mapper.add(entity);
			flag = true;
		}catch(Exception e){
			flag = false;
		}
		return flag;
	}
	
	@Override
	public boolean remove(Integer id) {
		boolean flag = false;
		try{
			TempOrder entity = new TempOrder();
			entity.setId(id);
			mapper.remove((T)entity);
			flag = true;
		}catch(Exception e){
			flag = false;
		}
		return flag;
	}
	
	@Override
	public List<T> getByOrderId(String orderId) {
		return mapper.getByOrderId(orderId);
	}

	@Override
	public List<T> getTempListByMap(Map map) {
		return mapper.getTempListByMap(map);
	}

	@Override
	public int countTempListByMap(Map map) {
		return mapper.countTempListByMap(map);
	}

}
