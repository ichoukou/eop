package net.ytoec.kernel.service;

import java.util.List;

import net.ytoec.kernel.dataobject.OrderPlaceSenderTemp;

public interface OrderPlaceSenderTempService {

	/**
	 * 根据用户ID查询卖家发货人信息
	 * @param id
	 * @return
	 */
	public List<OrderPlaceSenderTemp> getByUserId(Integer id);

	/**
	 * 保存
	 */
	public void addTemp(OrderPlaceSenderTemp temp);

	/**
	 * 修改
	 * @param temp
	 */
	public void eidtTemp(OrderPlaceSenderTemp temp,String tips);

}
