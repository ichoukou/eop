/**
 * 2012-5-15上午09:50:08
 * wangyong
 */
package net.ytoec.kernel.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dao.TempOrderDao;
import net.ytoec.kernel.dataobject.TempOrder;
import net.ytoec.kernel.search.service.EccoreSearchService;
import net.ytoec.kernel.service.TempOrderService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 临时订单的业务接口。该表主要是用户电子对账中异常订单、调整订单、退货订单的业务逻辑处理
 * @author wangyong
 * 2012-5-15
 */
@Service
@Transactional
public class TempOrderServiceImpl<T extends TempOrder> implements TempOrderService<T>{

	@Inject
	private TempOrderDao<T> tempOrderDao;
	
	@Inject
	private EccoreSearchService eccoreSearchService;
	
	@Override
	public boolean add(T entity) {
		if(entity!=null)
			return tempOrderDao.add(entity);
		return false;
	}

	@Override
	public boolean remove(Integer id) {
		if(id!=null)
			return tempOrderDao.remove(id);
		return false;
	}

	@Override
	public List<T> getByOrderId(String orderId) {
		return tempOrderDao.getByOrderId(orderId);
	}

	@Override
	public List<T> getTempListByMap(Map map) {
		if(map.get("tempKey")!=null && !(map.get("tempKey").equals("")))
			return tempOrderDao.getTempListByMap(map);
		return null;
	}

	@Override
	public int countTempListByMap(Map map) {
		if(map.get("tempKey")!=null && !(map.get("tempKey").equals("")))
			return tempOrderDao.countTempListByMap(map);
		return 0;
	}

	@Override
	public List<T> getTempList(String tempKey, Integer tempType, Pagination pagination) {
		Map map = new HashMap();
		if(tempKey!=null && !tempKey.isEmpty())
			map.put("tempKey", tempKey.trim());
		else
			return null;
		if(tempType!=null)
			map.put("tempType", tempType);
		if(pagination.getPageNum()!=null && pagination.getStartIndex()!=null){
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
		}
		return getTempListByMap(map);
	}

	@Override
	public int countTempList(String tempKey, Integer tempType) {
		Map map = new HashMap();
		if(tempKey!=null && !tempKey.isEmpty())
			map.put("tempKey", tempKey.trim());
		else
			return 0;
		if(tempType!=null)
			map.put("tempType", tempType);
		return countTempListByMap(map);
	}

}
