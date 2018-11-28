package net.ytoec.kernel.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.PrintInvoiceDao;
import net.ytoec.kernel.dataobject.BigPen;
import net.ytoec.kernel.dataobject.PrintInvoice;
import net.ytoec.kernel.mapper.PrintInvoiceMapper;
import net.ytoec.kernel.service.impl.OrderServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

/**
 * 发货单实现dao
 * 
 * @author mabo
 * 
 * @param <T>
 */
@Repository
public class PrintInvoiceDaoImpl<T extends PrintInvoice> implements
		PrintInvoiceDao<T> {

	@Inject
	private PrintInvoiceMapper<PrintInvoice> mapper;

	private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Override
	public boolean addInvoice(PrintInvoice invoice) throws DataAccessException {
		// TODO Auto-generated method stub
		boolean flag = false;
		int i = mapper.addInvoice(invoice);
		if (i > 0) {
			flag = true;
		}

		return flag;
	}

	@Override
	public PrintInvoice getInvoiceByDeliverNo(String deliverNo) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		map.put("deliverNo", deliverNo);
		PrintInvoice invoice = null;
		try {
			invoice = mapper.getInvoiceByDeliverNo(map);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return invoice;
	}

	@Override
	public BigPen getBigPenByCity(String city) throws DataAccessException {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		map.put("areaName", city);
		BigPen bigPen = null;
		try {
			bigPen = mapper.getBigPenByCity(map);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return bigPen;
	}

	@Override
	public List<PrintInvoice> getInvoiceByNullMail(Map<String, Object> map) throws DataAccessException {
		// TODO Auto-generated method stub
		return mapper.getInvoiceByNullMail(map);
	}

	@Override
	public Integer fixUncompletedMailNo(PrintInvoice invoice) throws DataAccessException {
		// TODO Auto-generated method stub
		return mapper.fixUncompletedMailNo(invoice);
	}

}
