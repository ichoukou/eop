package net.ytoec.kernel.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.ytoec.kernel.dao.SMSBuyersSearchDao;
import net.ytoec.kernel.dataobject.SMSBuyersSearch;
import net.ytoec.kernel.service.SMSBuyersSearchService;

@Service
@Transactional
@SuppressWarnings("all")
public class SMSBuyersSearchServiceImpl<T extends SMSBuyersSearch> implements SMSBuyersSearchService {
    
	private static final Logger logger = LoggerFactory.getLogger(SMSBuyersSearchServiceImpl.class);
	   
	@Inject
	private SMSBuyersSearchDao sMSBuyersSearchdao;
	
	@Override
	public List<SMSBuyersSearch> getSMSBuyersSearchByUserId(Integer userId) {
		if (userId == null) {
			logger.error("getSMSBuyersSearchByUserId方法中userId参数为null！");
			return new ArrayList<SMSBuyersSearch>();
		}
		return sMSBuyersSearchdao.getSMSBuyersSearchByUserId(userId);
	}

	@Override
	public List<SMSBuyersSearch> saveSMSBuyersSearch(SMSBuyersSearch search) {
		try {
			if (search != null) {
				return sMSBuyersSearchdao.saveSMSBuyersSearch(search, search.getCreateUserId());
			} else {
				logger.error("saveSMSBuyersSearch方法中search参数为null！");
			}
			return new ArrayList<SMSBuyersSearch>();
		} catch (Exception e) {
			logger.error("saveSMSBuyersSearch方法执行出现异常！", e);
			return new ArrayList<SMSBuyersSearch>();
		}
	}
	
	@Override
	public boolean addSmsBuyersSearch(SMSBuyersSearch search) {
		
		if(search == null){
			logger.error("##添加搜索条件时传入参数为空！");
			return false;
		}
		return sMSBuyersSearchdao.addSmsBuyersSearch(search);
	}

	@Override
	public boolean delSmsBuyersSearch(Integer id) {

		if("".equals(id) || id == null){
			logger.error("##删除搜索条件时传入参数为空！");
			return false;
		}
		return sMSBuyersSearchdao.delSmsBuyersSearch(id);
	}

	@Override
	public boolean editSmsBuyersSearch(SMSBuyersSearch search) {
		if(search == null){
			logger.error("##修改搜索条件时传入参数为空！");
			return false;
		}
		return sMSBuyersSearchdao.updateSmsBuyersSearch(search);
	}

	@Override
	public SMSBuyersSearch getSmsBuyersSearchById(Integer searchId) throws Exception {
		
		if(searchId == null){
			throw new Exception("##根据ID查询搜索条件时传入参数为空！");
		}
		return (SMSBuyersSearch) sMSBuyersSearchdao.getSmsBuyersSearchById(searchId);
	}
}
