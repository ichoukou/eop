package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.ServiceHistory;

/**
 * 服务历史service
 * @author guoliang.wang
 *
 * @param <T>
 */
public interface ServiceHistoryService<T extends ServiceHistory>  extends BaseService<T>{

	/**
	 * 查询服务记录历史表列表
	 * @param Map   查询条件拼成的map
	 * @Pagination 分页信息
	 * @flag true:分页;   false:不分页
	 * @return List
	 */
	@SuppressWarnings("all")
	public List<T> getServiceHistoryList(Map map, Pagination pagination, boolean flag);
	
	/*
	 * 根据开通服务id、用户id来查询（自动续费时需要最新的一条记录）
	 */
	public List<T> getServiceHistoryListByDUserId(Map map);
}
