package net.ytoec.kernel.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dao.DredgeServiceDao;
import net.ytoec.kernel.dao.PayServiceDao;
import net.ytoec.kernel.dataobject.DredgeService;
import net.ytoec.kernel.dataobject.PayService;
import net.ytoec.kernel.service.PayServiceService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务表的service
 * @author guoliang.wang 
 */
@Service
@Transactional
@SuppressWarnings("all")
public class PayServiceServiceImpl<T extends PayService> implements PayServiceService<T> {
	private static final Logger logger = LoggerFactory.getLogger(PayServiceServiceImpl.class);

	@Inject
	private PayServiceDao<PayService> payServiceDao;
	
	@Inject
	private DredgeServiceDao<DredgeService> dredgeServiceDao;
	
	@Override
	public List<T> getNOpenserviceList(Integer userid) {
		
		//免费的服务
		List<PayService> freeServiceList=payServiceDao.getFreeServiceList();
          Map freemap = new HashMap();		
          freemap.put("userId", userid);
		if(CollectionUtils.isNotEmpty(freeServiceList)){
			List<Integer> serviceIdList=new ArrayList<Integer>();//存放已经开通服务的id
			for(PayService payService:freeServiceList){
				serviceIdList.add(payService.getId());
			}
			freemap.put("serviceIdList", serviceIdList);
		}
		//查询以及开通的服务
		List<DredgeService> dredgeServiceList= dredgeServiceDao.getOpenserviceList(freemap);
		Map map = new HashMap();
		map.put("userId", userid);
		if(CollectionUtils.isNotEmpty(dredgeServiceList)){
			List<Integer> strList=new ArrayList<Integer>();//存放已经开通服务的id
			for(DredgeService dredgeService:dredgeServiceList){
				strList.add(dredgeService.getServiceId());
			}
			map.put("dServiceIdList", strList);
		}
	
	
		return (List<T>) payServiceDao.getNOpenserviceList(map);
	}

	@Override
	public boolean add(T entity) {
		return payServiceDao.add(entity);
	}

	@Override
	public boolean edit(T entity) {
		boolean bool=false;
		bool = payServiceDao.edit(entity);
		   if(BooleanUtils.isFalse(bool)){
			   logger.error("修改服务表记录失败");
		   }
		return bool;
	}

	@Override
	public boolean remove(T entity) {
		return payServiceDao.remove(entity);
	}

	@Override
	@SuppressWarnings("all")
	public T get(T entity) {
		return (T) payServiceDao.get(entity);
	}

	/**
	 * 查询所有的收费的服务(短信除外)
	 * @return
	 */
	@Override
	@SuppressWarnings("all")
	public List<T> getNFreeserviceList() {
		return (List<T>) payServiceDao.getNFreeserviceList();
	}

	@Override
	public List<T> getAllOpenServiceList() {
		// TODO Auto-generated method stub
		return (List<T>) payServiceDao.getAllOpenServiceList();
	}

}
