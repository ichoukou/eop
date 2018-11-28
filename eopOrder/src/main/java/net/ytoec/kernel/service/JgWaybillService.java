package net.ytoec.kernel.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

public interface JgWaybillService<T> {
	
	public boolean addJgWaybills(List<T> jgWaybill);
	
	public boolean sendPrintOrders(List<T> jgWaybill);
	
	public boolean delJgWaybillFromJG(int limit);
	
	public Integer countJgwaybill(); 
	
}
