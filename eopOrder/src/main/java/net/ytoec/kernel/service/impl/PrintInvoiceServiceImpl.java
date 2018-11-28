package net.ytoec.kernel.service.impl;

import javax.inject.Inject;

import net.ytoec.kernel.dao.PrintInvoiceDao;
import net.ytoec.kernel.dataobject.BigPen;
import net.ytoec.kernel.dataobject.PrintInvoice;
import net.ytoec.kernel.service.PrintInvoiceService;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PrintInvoiceServiceImpl<T extends PrintInvoice> implements
		PrintInvoiceService<T> {

	@Inject
	private PrintInvoiceDao<PrintInvoice> printInvoiceDao;

	@Override
	public boolean addInvoice(PrintInvoice invoice) throws DataAccessException {
		return printInvoiceDao.addInvoice(invoice);
	}

	@Override
	public PrintInvoice getInvoiceByDeliverNo(String deliverNo)
			throws DataAccessException {
		return printInvoiceDao.getInvoiceByDeliverNo(deliverNo);
	}

	@Override
	public BigPen getBigPenByCity(String city) throws DataAccessException {
		return printInvoiceDao.getBigPenByCity(city);
	}
}
