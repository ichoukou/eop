package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.ConfigCode;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

import org.springframework.dao.DataAccessException;

/**
 * 
 * @author ChenRen
 * @date 2011-08-10
 */
public interface ConfigCodeMapper<T extends ConfigCode> extends
		BaseSqlMapper<T> {
	
	/**
	 * 根据pid值查询一个配置项<br>
	 * key在数据库中唯一
	 * 
	 * @param pid
	 * @return
	 */
	public List<T> getConfByPid(Integer pid);

	/**
	 * 根据key值查询一个配置项<br>
	 * key在数据库中唯一
	 * 
	 * @param confKey
	 * @return
	 */
	public T getConfByKey(String confKey);
	
	/**
	 * 查询所有配置信息列表
	 * @param confKey
	 * @return
	 */
	public List<T> getAllConfig(Map map);
	
	/**
	 * 根据配置项层级查询配置信息类表<br>
	 * 分页查询实现
	 * @param map map中包含confLevel、startIndex、pageNum键值对
	 * @return
	 */
	public List<T> getConfigByLevel(Map map);
	
	/**
	 * 根据key值查询一个配置项<br>
	 * key在数据库中唯一
	 * 
	 * @param key
	 * @return
	 */
	public List<T> getConfByKeyAndType(Map<String, String> map);

	/**
	 * 根据type值查询配置项<br>
	 * 
	 * @param type
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getConfByType(String type);

    public Integer updateConfByKey(Map<String, String> map);
    
    /**
     * 根据Key值获取对应时间
     * @param key
     * @return
     */
    public String getJobMonitorTime(String key);
    
    /**
     * 根据Key值获取对应时间
     * @param key
     * @return
     * @author liuchunyan
     */
    public List<ConfigCode> getJobMonitorTime1(String key);


}
