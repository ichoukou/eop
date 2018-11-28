/**
 * Feb 21, 201211:46:30 AM
 * wangyong
 */
package net.ytoec.kernel.dao.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dao.MailTendencyDao;
import net.ytoec.kernel.dataobject.MailTendency;
import net.ytoec.kernel.mapper.MailTendencyMapper;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

/**
 * @author wangyong
 * Feb 21, 2012
 */
@Repository
@SuppressWarnings("all")
public class MailTendencyDaoImpl<T extends MailTendency> implements MailTendencyDao<T> {
	
	private Logger logger=Logger.getLogger(MailTendencyDaoImpl.class);
	
	@Inject
	private MailTendencyMapper<MailTendency> mapper;

	@Override
	public boolean addMailTendency(T entity) {
		boolean flag = false;
		try{
			mapper.add(entity);
			flag = true;
		}catch(Exception e){
			flag = false;
			logger.error("添加走势数据错误");
		}
		return flag;
	}

	@Override
	public List<T> countBranchTendency(Map map) {
		return (List<T>)mapper.countBranchTendency(map);
	}
	
	@Override
	public List<T> countPingTaiTendency(Map map) {
		return (List<T>)mapper.countPingTaiTendency(map);
	}

	
	@Override
	public boolean removeMailTendency(T entity) {
		boolean flag = false;
		try{
			mapper.remove(entity);
			flag = true;
		}catch(Exception e){
			flag = false;
			logger.error("删除走势数据错误");
		}
		return flag;
	}
	
	@Override
	public List<T> getRepeatSiteList(String siteId){
		return (List<T>)mapper.getRepeatSiteList(siteId);
	}

}
