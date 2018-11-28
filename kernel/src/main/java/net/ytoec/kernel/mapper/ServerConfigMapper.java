package net.ytoec.kernel.mapper;

import net.ytoec.kernel.dataobject.ServerConfig;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * <b>
 * 
 * @author huaiwen
 * @package net.ytoec.kernel
 * @project kernel
 * @email huhuaiwen@gmail.com
 * @version 1.0
 */
public interface ServerConfigMapper<T extends ServerConfig> extends
		BaseSqlMapper<T> {

	public T getConfigByServerId(Integer serverId);
}