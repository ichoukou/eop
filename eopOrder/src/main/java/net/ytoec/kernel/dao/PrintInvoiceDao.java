package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.dataobject.BigPen;
import net.ytoec.kernel.dataobject.PrintInvoice;

/**
 * 打印发货单
 * @author mabo
 *
 */
public interface PrintInvoiceDao<T> {

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
	
	/**
	 * 获取运单号为0的发货单信息
	 * @return
	 * @throws DataAccessException
	 */
	public List<PrintInvoice> getInvoiceByNullMail(Map<String, Object> map) throws DataAccessException;
	
	/**
	 * PrintInvoice invoice
	 * @param invoice
	 * @return
	 * @throws DataAccessException
	 */
	public Integer fixUncompletedMailNo(PrintInvoice invoice) throws DataAccessException;
}
