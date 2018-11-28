package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.TempOrder;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 临时非正常订单表数据映射
 * @author wangyong
 * 2012-5-14
 * net.ytoec.kernel.mapper
 */
public interface TempOrderMapper<T extends TempOrder> extends BaseSqlMapper<T> {

	/**
	 * 根据订单号查询
	 * @param orderId
	 * @return
	 */
	public List<T> getByOrderId(String orderId);
	
	 /**
	  * 查询不同temp_key下不同订单列表 tempKey必选项
	  * @param map
	  * @return
	  */
	public List<T> getTempListByMap(Map map);
	
	/**
	 * 统计不同temp_key下不同订单列表 tempKey必选项
	 * @param map
	 * @return
	 */
	public int countTempListByMap(Map map);
	
}
