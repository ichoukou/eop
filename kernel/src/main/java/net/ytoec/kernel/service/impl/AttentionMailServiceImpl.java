/**
 * AttentionMailServiceImpl.java
 * 2011 2011-12-13 下午05:55:24
 * wangyong
 */
package net.ytoec.kernel.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dao.AttentionMailDao;
import net.ytoec.kernel.dataobject.AttentionMail;
import net.ytoec.kernel.service.AttentionMailService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * @author wangyong
 * TODO
 */
@Service
@Transactional
@SuppressWarnings("unchecked")
public class AttentionMailServiceImpl<T extends AttentionMail> implements AttentionMailService<T> {

    @Inject
    private AttentionMailDao<AttentionMail> dao;
    
    @Override
    public boolean add(String mailNo, String destination, String buyerName, String buyerPhone, String acceptTime, String status, String sendTime, String customerId) {
        AttentionMail am = new AttentionMail();
        am.setMailNo(mailNo);
        am.setDestination(destination);
        am.setBuyerName(buyerName);
        am.setBuyerPhone(buyerPhone);
        try {
            am.setAcceptTime(strToDate(acceptTime,"yyyy-MM-dd HH:mm:ss"));
            am.setSendTime(strToDate(sendTime,"yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
        }
        am.setStatus(status);
        am.setCustomerId(customerId);
        return dao.add(am);
    }
    
    @Override
    public boolean add(T entity){
        return dao.add(entity);
    }

    @Override
    public boolean edit(T entity) {
        
        return false;
    }

    @Override
    public T get(Integer id) {
        return (T)dao.get(id);
    }

    @Override
    public boolean remove(Integer id) {
        return dao.remove(id);
    }

    @Override
    public List<T> searchByCustomerId(String customerId) {
        return (List<T>)dao.searchByCustomerId(customerId);
    }

    @Override
    public List<T> searchByMailNo(String mailNo) {
        return (List<T>)dao.searchByMailNo(mailNo);
    }

    @Override
    public List<T> searchPaginationList(String startTime, String endTime, String mailNo, String buyerName, String buyerPhone, Integer orderBy, Pagination pagination, List bindedId) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (bindedId != null && bindedId.size() > 0) {
        	map.put("customerId", bindedId);
        } else {
        	return null;
        }
        if (startTime != null && endTime != null && !startTime.equals("") && !endTime.equals("")){
        	try {
        		endTime = stringToDate(endTime, -1);
        		map.put("sendStartTime", startTime);
                map.put("sendEndTime", endTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
        }
        if (buyerName != null && !(buyerName.trim()).equals("")) {
            map.put("buyerName", buyerName.trim());
        }
        if (buyerPhone != null && !(buyerPhone.trim()).equals("")) {
            map.put("buyerPhone", buyerPhone.trim());
        }
        if (mailNo!= null && !(mailNo.trim()).equals("")){
            map.put("mailNo", mailNo.trim());
        }
        if (orderBy != null) {
            map.put("orderBy", orderBy);
        }
        if (pagination.getStartIndex() != null && pagination.getPageNum() != null) {
            map.put("startIndex", pagination.getStartIndex());
            map.put("pageNum", pagination.getPageNum());
        }
        return (List<T>)dao.searchPaginationList(map);
    }
    
    @Override
    public int countPaginationList(String startTime, String endTime, String mailNo, String buyerName, String buyerPhone, List bindedId) {
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        Map map = new HashMap();
        if(startTime!=null && endTime!=null && !startTime.equals("") && !endTime.equals("")){
            try {
            	endTime = stringToDate(endTime, -1);
                map.put("sendStartTime", simpleDate.parse(startTime));
                map.put("sendEndTime", simpleDate.parse(endTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(buyerName != null && !(buyerName.trim()).equals(""))
            map.put("buyerName", buyerName.trim());
        if(buyerPhone != null && !(buyerPhone.trim()).equals(""))
            map.put("buyerPhone", buyerPhone.trim());
        if(mailNo!=null&&!(mailNo.trim()).equals("")){
            map.put("mailNo",mailNo.trim());
        }
        if(bindedId!=null&&bindedId.size()>0){
        	 map.put("customerId", bindedId);
        }
        if(map.get("customerId")==null){
            return 0;
        }
        return dao.countPaginationList(map);
    }
    
    @Override
    public List<T> searchByMailNoAndCustomerId(String mailNo, String customerId) {
    	if(mailNo!=null && customerId!=null && !mailNo.equals("") && !customerId.equals("")){
    		Map map = new HashMap();
    		map.put("mailNo", mailNo);
    		map.put("customerId", customerId);
    		List<AttentionMail> list = dao.searchByMailNoAndCustomerId(map);
    		return (List<T>)list;
    	}else
    		return null;
    }
    
    @Override
    public List<T> searchByMailNoAndCustomerIdsAndTime(String mailNo, List<String> customerId, String startTime, String endTime) {
    	Map map = new HashMap();
    	SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        if(startTime!=null && endTime!=null && !startTime.equals("") && !endTime.equals("")){
            try {
            	endTime = stringToDate(endTime, -1);
        		map.put("sendStartTime", startTime);
                map.put("sendEndTime", endTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
        	return null;
        }
    	if(StringUtils.isNotEmpty(mailNo)) {
    		map.put("mailNo", mailNo);
    	} else {
        	return null;
        }
    	if(!customerId.isEmpty()){
    		map.put("customerId", customerId);
    	} else {
        	return null;
        }
		List<AttentionMail> list = dao.searchByMailNoAndCustomerIdsAndTime(map);
		return (List<T>)list;
    }
    
    private static Date strToDate(String str, String format) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(str);
    }
    
    private static String stringToDate(String str, int days)
		throws ParseException {
		SimpleDateFormat sdm = new SimpleDateFormat("yyyy-MM-dd");
		String result = null;
		if (str != null && !str.equals("")) {
			Date next = sdm.parse(str);
			result = dateArithmeticMinus(next, days);
		}
		return result;
    }
    
    /**
	 * 在原始日期originalDate上减去countDay天数
	 * 
	 * @param originalDate
	 * @param countDay
	 * @return
	 */
	private static String dateArithmeticMinus(Date originalDate, int countDay) {
		SimpleDateFormat sdm = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(originalDate);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		// 在当前日期上加-countDay天数
		cal.add(Calendar.DAY_OF_MONTH, -countDay);
		return sdm.format(cal.getTime());
	}

}
