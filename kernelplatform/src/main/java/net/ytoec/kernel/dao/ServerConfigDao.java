package net.ytoec.kernel.dao;

public interface ServerConfigDao<T> {

	public T getConfigByServerId(Integer serverId);
}
