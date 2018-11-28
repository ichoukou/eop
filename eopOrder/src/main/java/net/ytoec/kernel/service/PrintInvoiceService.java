package net.ytoec.kernel.service;

import java.util.Map;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.dataobject.BigPen;
import net.ytoec.kernel.dataobject.PrintInvoice;

/**
 * 打印发货单
 * @author mabo
 *
 */
public interface PrintInvoiceService<T> {

	/**
	 * 增加一条发货单信息
	 * @param invoice
	 * @return
	 * @throws DataAccessException
	 */
	public boolean addInvoice(PrintInvoice invoice) throws DataAccessException;
	
	/**
	 * 根据发货单号查询发货单信息
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public PrintInvoice getInvoiceByDeliverNo(String deliverNo) throws DataAccessException;
	
	/**
	 * 根据城市名获取大头笔信息
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public BigPen getBigPenByCity(String city) throws DataAccessException;
	
}
