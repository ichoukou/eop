package net.ytoec.kernel.dao.impl;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dao.OrderDaoVersion;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.mapper.OrderVersionMapper;
import net.ytoec.kernel.search.dto.MailObjectDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * @author ChenRen
 * @date 2011-7-20
 */
@Repository
public class OrderVersionDaoImpl<T extends Order> implements OrderDaoVersion<T> {

	private static Logger logger = LoggerFactory.getLogger(OrderVersionDaoImpl.class);
	@Inject
	private OrderVersionMapper<T> mapper;

	@Override
	public List<MailObjectDTO> bulidPartEccoreStatusData(Integer limit, String currentDate, String endDate) {
		if (limit == null) {
			return Collections.EMPTY_LIST;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limit", limit);
		map.put("currentDate", currentDate);
		map.put("endDate", endDate);

		return mapper.bulidPartEccoreStatusData(map);
	}
}
