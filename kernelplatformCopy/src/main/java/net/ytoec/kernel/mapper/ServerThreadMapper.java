/**
 * ServerThreadMapper.java
 * Wangyong
 * 2011-9-7 上午10:46:22
 */
package net.ytoec.kernel.mapper;

import java.util.List;

import net.ytoec.kernel.dataobject.ServerThread;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 多线程服务数据映射
 * @author Wangyong
 * @2011-9-7
 * net.ytoec.kernel.mapper
 */
public interface ServerThreadMapper<T extends ServerThread> extends BaseSqlMapper<T> {

	/**
	 * 查询所有多线程服务数据，并获取最近的一条数据
	 * @return
	 */
	public List<T> getLastServerThread();
}
