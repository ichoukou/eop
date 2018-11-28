package net.ytoec.kernel.dao;

import net.ytoec.kernel.dataobject.ApiLog;

/**
 * @作者：罗典
 * @时间：2013-09-02
 * @描述：操作接口日志记录
 * */
public interface ApiLogDao<T> {
	/**
	 * @作者：罗典
	 * @时间：2013-09-02
	 * @描述：新增接口日志记录
	 * @参数：log 日志
	 * */
	public int insertApiLog(ApiLog log);
}
