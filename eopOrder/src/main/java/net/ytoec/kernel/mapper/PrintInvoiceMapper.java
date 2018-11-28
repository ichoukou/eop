package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.dataobject.BigPen;
import net.ytoec.kernel.dataobject.PrintInvoice;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

public interface PrintInvoiceMapper<T extends PrintInvoice> extends BaseSqlMapper<T> {

	/**
	 * 增加一条发货单信息
	 * @param invoice
	 * @return
	 * @throws DataAccessException
	 */
	public Integer addInvoice(PrintInvoice invoice) throws DataAccessException;
	
	/**
	 * 根据发货单号查询发货单信息
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public PrintInvoice getInvoiceByDeliverNo(Map<String, String> map) throws DataAccessException;
	
	/**
	 * 根据城市名获取大头笔信息
	 * @param map
	 * @return
	 * @throws DataAccessException
	 */
	public BigPen getBigPenByCity(Map<String, String> map) throws DataAccessException;
	
	/**
	 * 获取运单号为0的信息(发货单信息默认为0)
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
