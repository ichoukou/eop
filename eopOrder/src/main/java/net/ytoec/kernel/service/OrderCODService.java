package net.ytoec.kernel.service;

import net.ytoec.kernel.action.remote.xml.UpdateWaybillInfo;
import net.ytoec.kernel.dataobject.SendTask;

/**
 * 记录快捷COD的订单信息
 * @author mabo
 *
 */
public interface OrderCODService {

	//添加COD信息
	public String addCOD(UpdateWaybillInfo updateInfo,SendTask sendTask);
}
