package net.ytoec.kernel.service;

import java.util.List;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.ConfigCode;

/**
 * 配置项表的业务操作
 * 
 * @author ChenRen
 * @date 2011-08-10
 */
public interface ConfigCodeService<T> {

	/**
	 * 根据key值查询一个配置项<br>
	 * key/type 在数据库中联合唯一
	 * 
	 * @param key
	 * @param type
	 * @return
	 */
	public abstract T getConfByKeyAndType(String key, String type)
;
	
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
	 * 新增一条配置项信息数据
	 * 
	 * @param entity
	 * @param levelId
	 * @return @
	 */
	public boolean addConfig(T entity, Integer levelId);

	/**
	 * 根据Id得到一条配置项数据
	 * 
	 * @param id
	 * @return @
	 */
	public T getConfigById(Integer id);

	/**
	 * 得到所有配置项信息数据
	 * 
	 * @return @
	 */
	public List<T> getAllConfig(Pagination pagination, boolean flag);
	
	/**
	 * 根据配置项层级查询配置信息类表<br>
	 * 分页查询实现
	 * @param map map中包含confLevel、startIndex、pageNum键值对
	 * @return
	 */
	public List<T> getConfigByLevel(Pagination pagination, String confLevel,
			boolean flag);

	/**
	 * 修改一个配置项
	 * 
	 * @param entity
	 * @return @
	 */
	public boolean editConfig(T entity);

	/**
	 * 删除一个配置项
	 * 
	 * @param entity
	 * @return @
	 */
	public boolean removeConfig(T entity);

    /**
     * @param key
     * @return
     */
    public boolean updateConfByKey(String key, String value);
    
    /**
     * 根据Key值获取对应时间
     * @param key
     * @return
     */
    public String getJobMonitorTime(String key);

    public List<ConfigCode> getJobMonitorTime1(String key);
    
}