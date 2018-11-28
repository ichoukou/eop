package net.ytoec.kernel.service.impl;


import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dao.OrderPlaceSenderTempDao;
import net.ytoec.kernel.dataobject.OrderPlaceSenderTemp;
import net.ytoec.kernel.service.OrderPlaceSenderTempService;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderPlaceSenderTempServiceImpl implements
		OrderPlaceSenderTempService {

	private static Logger logger = Logger.getLogger(OrderPlaceSenderTempServiceImpl.class);
	
	@Inject
	private OrderPlaceSenderTempDao dao;

	@Override
	public List<OrderPlaceSenderTemp> getByUserId(Integer id) {
		if(id == null){
			logger.error("##根据用户ID查询卖家发货信息传参数为空！");
			return null;
		}
		
		return dao.getByUserId(id);
	}

	@Override
	public void addTemp(OrderPlaceSenderTemp temp) {
		if(temp == null){
			logger.error("##保存发件人信息临时表传参为空！");
		}
		boolean bool = dao.addTemp(temp);
		logger.info("保存发件人信息临时表成功与否："+bool);
		
	}

	@Override
	public void eidtTemp(OrderPlaceSenderTemp temp,String tips) {
		if(temp == null){
			if("1".equals(tips))logger.error("##修改发件人临时表手机号码传参为空！");
			if("2".equals(tips))logger.error("##修改发件人临时表姓名传参为空！");
			if("3".equals(tips))logger.error("##修改发件人临时表地址传参为空！");
		}
		
		boolean bool = dao.eidtTemp(temp);
		
		if("1".equals(tips))logger.info("修改发件人信息临时表手机号码成功与否："+bool);
		if("2".equals(tips))logger.info("修改发件人信息临时表姓名成功与否："+bool);
		if("3".equals(tips))logger.info("修改发件人信息临时表地址成功与否："+bool);
	}
	
	
}
