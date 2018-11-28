package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.ServiceHistory;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

public interface ServiceHistoryMapper<T extends ServiceHistory> extends BaseSqlMapper<T> {

	/**
	 * 查询服务记录历史表列表
	 * 
	 * @param Map
	 *            查询条件拼成的map
	 * @Pagination 分页信息
	 * @flag 是否分页
	 * @return boolean
	 */
	public List<T> getServiceHistoryList(Map map);
	
	/*
	 * 根据开通服务id、用户id来查询（自动续费时需要最新的一条记录）
	 */
	public List<T> getServiceHistoryListByDUserId(Map map);
}
