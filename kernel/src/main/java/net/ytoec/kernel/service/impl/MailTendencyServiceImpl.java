/**
 * Feb 22, 20124:01:24 PM
 * wangyong
 */
package net.ytoec.kernel.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dao.MailTendencyDao;
import net.ytoec.kernel.dataobject.MailTendency;
import net.ytoec.kernel.service.MailTendencyService;
import net.ytoec.kernel.util.DateUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wangyong
 * Feb 21, 2012
 */
@Service
@Transactional
@SuppressWarnings("all")
public class MailTendencyServiceImpl<T extends MailTendency> implements MailTendencyService<T> {

	@Inject
	private MailTendencyDao<MailTendency> mailTendecyDao;
	
	@Override
	public boolean addMailTendency(T entity) {
		return mailTendecyDao.addMailTendency(entity);
	}
	
	@Override
	public int countMailNoNumBySiteAndTime(Integer siteId, String someDay){
		int count = 0;
		Map map = new HashMap();
		if(siteId==null || siteId==0)
			return 0;
		if(someDay==null || "".equals("someDay"))
			return 0;
		map.put("siteId", siteId);
		map.put("someDay", someDay);
		
		List<MailTendency> mailTendencyList = mailTendecyDao.countBranchTendency(map);
		if(mailTendencyList!=null && mailTendencyList.size()>0){
			for(MailTendency mt : mailTendencyList){
				if(mt.getMailNoNum()!=null)
					count += mt.getMailNoNum();
			}
		}
		return count;
	}

	
	@Override
	public List<Map<String, Integer>> singleSeries(Integer userId, String startDate,String endDate,Integer timeLimit) {
		List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
		if(timeLimit>0){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			GregorianCalendar gc = new GregorianCalendar();
			while(timeLimit>0){
				Map<String, Integer> map = new HashMap<String, Integer>();
				gc.setTime(DateUtil.valueOfStandard(endDate));
				gc.add(Calendar.DAY_OF_MONTH, -timeLimit);
				int year = gc.get(Calendar.YEAR);
				int month = gc.get(Calendar.MONTH);
				int day = gc.get(Calendar.DAY_OF_MONTH);
				String curDate = sdf.format(gc.getTime());
				int mailNum = this.countMailNoNumBySiteAndTime(userId, curDate);
				map.put("year", year);
				map.put("month", month+1);
				map.put("day", day);
				map.put("mailNum", mailNum);
				list.add(map);
				timeLimit--;
			}
		}
		return list;
	}
	
	

	@Override
	public List<Map<String, Integer>> createTimeSeries(Integer siteId, int timeLimit) {
		List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
		if(timeLimit>0){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			GregorianCalendar gc = new GregorianCalendar();
			while(timeLimit>0){
				Map<String, Integer> map = new HashMap<String, Integer>();
				gc.setTime(new Date());
				gc.add(Calendar.DAY_OF_MONTH, -timeLimit);
				int year = gc.get(Calendar.YEAR);
				int month = gc.get(Calendar.MONTH);
				int day = gc.get(Calendar.DAY_OF_MONTH);
				String curDate = sdf.format(gc.getTime());
				int mailNum = this.countMailNoNumBySiteAndTime(siteId, curDate);
				map.put("year", year);
				map.put("month", month+1);
				map.put("day", day);
				map.put("mailNum", mailNum);
				list.add(map);
				timeLimit--;
			}
		}
		return list;
	}
	
	@Override
	public List<Map<String, Integer>> pingTaiSeries(List<Integer>  userId, String startDate,String endDate,Integer timeLimit) {
		List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
		if(timeLimit>0){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			GregorianCalendar gc = new GregorianCalendar();
			Date standardDate = DateUtil.valueOfStandard(endDate);
			while(timeLimit>0){
				
				Map<String, Integer> map = new HashMap<String, Integer>();
				gc.setTime(standardDate);
				int year = gc.get(Calendar.YEAR);
				int month = gc.get(Calendar.MONTH);
				int day = gc.get(Calendar.DAY_OF_MONTH);
				String curDate = sdf.format(gc.getTime());
	
				int mailNum = 0;
				for(Integer id : userId){
					System.out.println("id=="+ id);
					mailNum += this.countPingTaiTendency(id, curDate);
				}
				
				map.put("year", year);
				map.put("month", month+1);
				map.put("day", day);
				map.put("mailNum", mailNum);
				list.add(map);
				timeLimit--;
				standardDate = DateUtil.add(standardDate, Calendar.DATE, -1);
			}
		}
		return list;
	}
	

	
	
	@Override
	public List<T> getMailTendencyBySiteAndTime(Integer siteId, String someDay) {
		Map map = new HashMap();
		if(siteId==null || siteId==0)
			return null;
		if(someDay==null || "".equals("someDay"))
			return null;
		map.put("siteId", siteId);
		map.put("someDay", someDay);
		List<MailTendency> mailTendencyList = mailTendecyDao.countBranchTendency(map);
		return (List<T>)mailTendencyList;
	}
	
	@Override
	public List<T> getMailTendencyBySiteUserTime(Integer siteId, Integer userId, String someDay){
		Map map = new HashMap();
		if(siteId==null || siteId==0)
			return null;
		if(userId==null || userId==0)
			return null;
		if(someDay==null || "".equals("someDay"))
			return null;
		map.put("siteId", siteId);
		map.put("userId", userId);
		map.put("someDay", someDay);
		List<MailTendency> mailTendencyList = mailTendecyDao.countBranchTendency(map);
		return (List<T>)mailTendencyList;
	}
	
	

	@Override
	public boolean removeMailTendecy(T entity) {
		return mailTendecyDao.removeMailTendency(entity);
	}

	@Override
	public int countPingTaiTendency(Integer userId, String someDay) {
		int count = 0;
		Map map = new HashMap();
		if(userId==null || userId==0)
			return 0;
		if(someDay==null || "".equals("someDay"))
			return 0;
		map.put("userId", userId);
		map.put("ssomeDay", someDay+" 00:00:00");
		map.put("esomeDay", someDay+" 23:59:59");
		List<MailTendency> mailTendencyList = mailTendecyDao.countPingTaiTendency(map);
		if(mailTendencyList!=null && mailTendencyList.size()>0){
			System.out.println(userId+"=="+ mailTendencyList.get(0).getMailNoNum());
			count = mailTendencyList.get(0).getMailNoNum();
		}
		return count;
	}
	
	@Override
	public List<T> getRepeatSiteList(String siteId){
		if(siteId!=null)
			return (List<T>)mailTendecyDao.getRepeatSiteList(siteId);
		else
			return null;
	}

}
