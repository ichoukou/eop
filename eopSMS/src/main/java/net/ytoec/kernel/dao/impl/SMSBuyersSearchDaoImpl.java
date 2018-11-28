package net.ytoec.kernel.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.SMSBuyersSearchDao;
import net.ytoec.kernel.dataobject.SMSBuyersSearch;
import net.ytoec.kernel.mapper.SMSBuyersSearchMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * 搜索器dao层实现类
 * @author
 *
 * @param <T>
 */
@Repository
public class SMSBuyersSearchDaoImpl<T extends SMSBuyersSearch> implements SMSBuyersSearchDao<T> {

	private static final Logger logger=LoggerFactory.getLogger(SMSBuyersSearchDaoImpl.class);
	
	@Inject
	private SMSBuyersSearchMapper<T> mapper;
	
	
	@Override
	public List<T> getSMSBuyersSearchByUserId(Integer userId) {
		try{
			if (userId == null) {
				return new ArrayList<T>();
			}
			return mapper.getSMSBuyersSearchByUserId(userId);
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			return new ArrayList<T>();
		}
	}

	@Override
	public List<T> saveSMSBuyersSearch(T search, Integer userId) {
		try {
			//  查看搜索器个数
			Integer count = mapper.getSearchCountByUserId(userId);
			if (count < 5) {
				mapper.addSmsBuyersSearch(search);
			} else {
				List<T> tempList = mapper.getSMSBuyersSearchByUserId(userId);
				if (tempList != null && tempList.size() > 0) {
					SMSBuyersSearch temp = tempList.get(tempList.size() - 1);
					search.setId(temp.getId());
					mapper.editSmsBuyersSearch(search);
				}
			}
			return mapper.getSMSBuyersSearchByUserId(userId);
		} catch (Exception e) {
			logger.error("搜索器saveSMSBuyersSearch方法出现异常", e);
			return new ArrayList<T>();
		}
		
	}
	
	@Override
	public boolean addSmsBuyersSearch(T search) {
		
		boolean flag = false;
		try{
			mapper.addSmsBuyersSearch(search);
			flag = true;
		} catch(Exception e){
			
			flag = false;
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public boolean delSmsBuyersSearch(Integer id) {
		boolean flag = false;
		try{
			mapper.delSmsBuyersSearch(id);
			flag = true;
		} catch(Exception e){
			
			flag = false;
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public boolean updateSmsBuyersSearch(T search) {
		boolean flag = false;
		try{
			mapper.editSmsBuyersSearch(search);
			flag = true;
		} catch(Exception e){
			
			flag = false;
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return flag;
	}

	@Override
	public T getSmsBuyersSearchById(Integer searchId) {

		T smsBuyersSearch = null;
		try{
			smsBuyersSearch = mapper.getSmsBuyersSearchById(searchId);
		} catch(Exception e){
			
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		
		return smsBuyersSearch;
	}
}
