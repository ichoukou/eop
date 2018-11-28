package net.ytoec.kernel.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dao.SendTaskMailNoDao;
import net.ytoec.kernel.dataobject.SendTask;
import net.ytoec.kernel.mapper.NoticeMailNoMapper;

@Repository
public class SendTaskMailNoDaoImpl<T extends SendTask> implements SendTaskMailNoDao<T> {

	@Autowired
	private NoticeMailNoMapper<T> mapper;
	
	@Override
	public List<T> getMailNoListByLimit(Integer limit) {
		// TODO Auto-generated method stub
		List<T> list = new ArrayList<T>();
		list = mapper.getMailNoListByLimit(limit);
		return (List<T>)list;
	}

	@Override
	public Integer addMailNo(String mailNo, String clientId) {
		// TODO Auto-generated method stub
		StringBuilder xml = new StringBuilder();
		xml.append("<Request><mailNo>");
		xml.append(mailNo);
		xml.append("</mailNo></Request>");
		String url = Resource.getChannel(clientId);
		Map<String, String> map = new HashMap<String, String>();
		map.put("requestParams", xml.toString());
		map.put("requestURL", url);
		map.put("taskFlagId", getflagid(mailNo).toString());
		map.put("mailNo", mailNo);
		map.put("clientId",clientId);

		return mapper.addMailNo(map);
	}

	@Override
	public Integer removeMailNo(String mailNo, String clientId) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		map.put("mailNo", mailNo);
		map.put("clientId", clientId);
		
		return mapper.removeMailNo(map);
	}

	private static Integer getflagid(String arg) {
		String s = arg;
		// String s="klllll123456";
		s = s.substring(s.length() - 1);
		return Integer.parseInt(s);

	}
}
