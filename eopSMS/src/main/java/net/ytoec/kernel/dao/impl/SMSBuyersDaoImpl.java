package net.ytoec.kernel.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.SMSBuyersDao;
import net.ytoec.kernel.dataobject.SMSBuyers;
import net.ytoec.kernel.mapper.SMSBuyersMapper;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

/**
 * 会员管理dao层实现类
 * @author 
 *
 * @param <T>
 */
@Repository
public class SMSBuyersDaoImpl<T extends SMSBuyers> implements SMSBuyersDao<T> {

	private static final Logger logger = LoggerFactory.getLogger(SMSBuyersDaoImpl.class);
	
	@Inject
	private SMSBuyersMapper<T> mapper;
	
	@Override
	public Boolean updateInfoByTaobao(Double tradeAmount, Date theLastTradeTime, Integer userId) {
		boolean flag = false;
		try {
			if (userId == null) {
				logger.error("userId为空！！");
				return false;
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("tradeAmount", tradeAmount);
			params.put("theLastTradeTime", theLastTradeTime);
			params.put("userId", userId);
			flag = mapper.updateInfoByTaobao(params) > 0 ? true : false;
		} catch(Exception e){
			logger.error("从淘宝获取订单下来时需要更新会员的信息 时出错", e);
		}
		return flag;
	}
	
	@Override
	public boolean addSMSBuyers(T buyers) {

		boolean flag = false;
		try{
			mapper.addSMSBuyers(buyers);
			flag = true;
		} catch(Exception e){
			
			flag = false;
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public boolean delSMSBuyersById(Integer id) {
		
		boolean flag = false;
		try{
			mapper.delSMSBuyersById(id);
			flag = true;
		} catch(Exception e){
			
			flag = false;
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public boolean editSMSBuyers(T buyers) {
		boolean flag = false;
		try{
			mapper.editSMSBuyers(buyers);
			flag = true;
		} catch(Exception e){
			
			flag = false;
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public T getSMSBuyersById(Integer id) {
		T buyers = null;
		try{
			buyers = mapper.getSMSBuyersById(id);
		} catch(Exception e){
			
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return buyers;
	}

	@Override
	public boolean delBatchSMSBuyers(List<Integer> idLists) {
		boolean flag = false;
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("idLists", idLists);
			flag = mapper.delBatchSMSBuyers(params) > 0 ? true : false;
		} catch(Exception e){
			logger.error("从淘宝获取订单下来时需要更新会员的信息 时出错", e);
		}
		return flag;
	}

	@Override
	public List<SMSBuyers> getSMSBuyersByPamams(Map<String, Object> params) {
		List<SMSBuyers> smsBuyersList = null;
		 if (StringUtils.isNotEmpty((String)params.get("receiverName"))){
			smsBuyersList = mapper.getSMSBuyersByPamams(params);
			if (smsBuyersList == null || smsBuyersList.size() == 0) {
				params.put("buyerAccount", (String)params.get("receiverName"));
				params.put("receiverName", "");
				smsBuyersList = mapper.getSMSBuyersByPamams(params);
			}
		} else if (StringUtils.isNotEmpty((String)params.get("buyerAccount"))){
			smsBuyersList = mapper.getSMSBuyersByPamams(params);
			if (smsBuyersList == null || smsBuyersList.size() == 0) {
				params.put("receiverName", (String)params.get("buyerAccount"));
				params.put("buyerAccount", "");
				smsBuyersList = mapper.getSMSBuyersByPamams(params);
			}
		} else {
			smsBuyersList = mapper.getSMSBuyersByPamams(params);
		}
		return smsBuyersList;
	}


	@Override
	public Integer getSMSBuyersCountByPamams(Map<String, Object> params) {
		Integer count = 0;
		 if (StringUtils.isNotEmpty((String)params.get("receiverName"))){
			 count = mapper.getSMSBuyersCountByPamams(params);
			if (count == null || count == 0) {
				params.put("buyerAccount", (String)params.get("receiverName"));
				params.put("receiverName", "");
				count = mapper.getSMSBuyersCountByPamams(params);
			}
		} else if (StringUtils.isNotEmpty((String)params.get("buyerAccount"))){
			count = mapper.getSMSBuyersCountByPamams(params);
			if (count == null || count == 0) {
				params.put("receiverName", (String)params.get("buyerAccount"));
				params.put("buyerAccount", "");
				count = mapper.getSMSBuyersCountByPamams(params);
			}
		} else {
			count = mapper.getSMSBuyersCountByPamams(params);
		}
		return count;
	}
	
	@Override
	public boolean addSMSBuyersList(List<T> list) throws DataAccessException {
		boolean flag = false;
		try {
			for(T s:list){
				mapper.add(s);
				flag = true;
			}
		}catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public SMSBuyers getSMSBuyersByBuyerAccount(String buyerAccount, Integer userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("buyerAccount", buyerAccount);
		params.put("userId", userId);
		return mapper.getSMSBuyersByBuyerAccount(params);
	}

	@Override
	public Integer getCountByUserGrade(Map<String, Object> params) {
		return mapper.getCountByUserGrade(params);
	}

	/**
	 * 验证旺旺唯一性
	 */
	public SMSBuyers checkWW(String buyerAccount) {
		return mapper.checkWW(buyerAccount);
	}
}
