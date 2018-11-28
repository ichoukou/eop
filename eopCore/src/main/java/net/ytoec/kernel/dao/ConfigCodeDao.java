package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.ConfigCode;

import org.springframework.dao.DataAccessException;

/**
 * 配置项表的数据库操作
 * 
 * @author ChenRen
 * @date 2011-08-10
 */
public interface ConfigCodeDao<T extends ConfigCode> {
	
	/**
	 * 新增一条配置项信息数据
	 * 
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public boolean addConfig(T config) throws DataAccessException;

	/**
	 * 根据Id得到一条配置项信息数据
	 * 
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public T getConfigById(Integer id) throws DataAccessException;

	/**
	 * 获取所有配置项数据
	 * 
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getAllConfig(Map map) throws DataAccessException;
	
	/**
	 * 根据配置项层级查询配置信息类表<br>
	 * 分页查询实现
	 * @param map map中包含confLevel、startIndex、pageNum键值对
	 * @return
	 */
	public List<T> getConfigByLevel(Map map) throws DataAccessException;

	/**
	 * 修改配置项信息
	 * 
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public boolean editConfig(T config) throws DataAccessException;

	/**
	 * 删除一条配置项信息
	 * 
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public boolean removeConfig(T config) throws DataAccessException;
	
	/**
	 * 根据pid(父配置项的Id)值查询所有子配置项<br>
	 * 
	 * @param pid
	 * @return
	 */
	public List<T> getConfByPid(Integer pid) throws DataAccessException;

	/**
	 * 根据key值查询一个配置项<br>
	 * key在数据库中唯一
	 * 
	 * @param confKey
	 * @return
	 */
	public T getConfByKey(String confKey) throws DataAccessException;

	/**
	 * 根据key值查询一个配置项<br>
	 * key在数据库中唯一
	 * 
	 * @param map
	 *            map中存放的是key/type; sql会根据key&&type查找唯一匹配数据
	 *   map中要传入confType和confKey两个参数
	 * @return
	 */
	public abstract List<T> getConfByKeyAndType(Map<String, String> map)
			throws DataAccessException;

	/**
	 * 根据type值查询配置项<br>
	 * 
	 * @param type
	 * @return
	 * @throws DataAccessException
	 */
	public abstract List<T> getConfByType(String type)
			throws DataAccessException;

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
    
    /**
     * 根据Key值获取对应时间
     * @param key
     * @return
     * @author liuchunyan
     */
    public  List<ConfigCode> getJobMonitorTime1(String key);

}