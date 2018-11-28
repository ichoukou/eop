package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.Channel;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 
 * @author ChenRen
 * @date 2011-7-19
 */
public interface ChannelMapper<T extends Channel> extends BaseSqlMapper<T> {
	
	/**
	 * 查找所有渠道信息(分页信息在map集合中)
	 * @param map
	 * @return
	 */
	public List<T> getAllChannel(Map map);
	
	public T getChannelByClientId(String clientId);

}