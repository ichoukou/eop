package net.ytoec.kernel.dao;

import java.util.List;

import net.ytoec.kernel.dataobject.OrderPlaceSenderTemp;

public interface OrderPlaceSenderTempDao {

	/**
	 * 根据用户ID查询卖家发货人信息
	 * @param id
	 * @return
	 */
	public List<OrderPlaceSenderTemp> getByUserId(Integer id);

	/**
	 * 保存发件人信息临时表
	 * @param temp
	 * @return
	 */
	public boolean addTemp(OrderPlaceSenderTemp temp);

	/**
	 * 修改手机号码
	 * @param temp
	 * @return
	 */
	public boolean eidtTemp(OrderPlaceSenderTemp temp);

}
