package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.OrderTemp;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * @author wuguiqiang
 * 
 * @date 2012-6-11
 * */
@SuppressWarnings("all")
public interface OrderTempMapper<T extends OrderTemp> extends BaseSqlMapper<T> {

	public List<T> getAllOrderTemp();

	public List<T> getOrderTempByMailNoRemark(T entity);

	public List<T> getOrderTempListByRemark(String remark);

	public List<T> getOrderTempListByRemarkPage(Map map);

	public List<T> getOrderTempLogisticIdRemark(T entity);

	public int countOrderTempListByMap(Map map);

	public int addOrderTempList(List<T> list);

}