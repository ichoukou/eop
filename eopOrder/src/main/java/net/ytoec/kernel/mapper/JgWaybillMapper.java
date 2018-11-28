/**
 * JgWaybillMapper.java
 * Wangyong
 * 2011-8-18 上午10:46:22
 */
package net.ytoec.kernel.mapper;

import java.util.List;

import net.ytoec.kernel.dataobject.JgWaybill;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 金刚运单接口信息数据映射
 * @author Wangyong
 * @2011-8-18
 * net.ytoec.kernel.mapper
 */
public interface JgWaybillMapper<T extends JgWaybill> extends BaseSqlMapper<T> {

	/**
	 * 查询所有金刚运单接口信息数据
	 * @return
	 */
	public List<T> getAllJgWaybill();
	
	void delJgWaybillFromJG(int limit);
	
	public Integer countJgwaybill();
}
