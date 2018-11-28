package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

/**
 * 账户实体dao
 * @author guoliang.wang
 *
 * @param <T>
 */
public interface ServiceHistoryDao<T> extends BaseDao<T>{

	/**
	 * 查询服务记录历史表列表
	 * @param Map
	 * @return List
	 */
	public List<T> getServiceHistoryList(Map map);
	
	/*
	 * 根据开通服务id、用户id来查询（自动续费时需要最新的一条记录）
	 */
	public List<T> getServiceHistoryListByDUserId(Map map);
}