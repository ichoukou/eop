/**
 * AttentionMailMapper.java
 * 2011 2011-12-13 下午02:48:55
 * wangyong
 */
package net.ytoec.kernel.mapper;

import java.util.List;

import net.ytoec.kernel.dataobject.OrderPlaceSenderTemp;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

public interface OrderPlaceSenderTempMapper extends BaseSqlMapper{

	public List<OrderPlaceSenderTemp> getByUserId(Integer id);

	public void addTemp(OrderPlaceSenderTemp temp);

	public void eidtTemp(OrderPlaceSenderTemp temp);

  
}
