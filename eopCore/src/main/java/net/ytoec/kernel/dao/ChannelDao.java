package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.Channel;

import org.springframework.dao.DataAccessException;

/**
 * 渠道信息表 Dao接口
 * 
 * @author ChenRen
 * @date 2011-7-19
 */
public interface ChannelDao<T extends Channel> {

	/**
	 * 新增一条渠道信息数据
	 * 
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	boolean addChannel(T entity) throws DataAccessException;

	/**
	 * 根据Id得到一条渠道信息数据
	 * 
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	T getChannelById(Integer id) throws DataAccessException;

	/**
	 * 获取所有数据
	 * 
	 * @return
	 * @throws DataAccessException
	 */
	List<T> getAllChannel(Map map) throws DataAccessException;

	/**
	 * 修改渠道信息
	 * 
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	boolean editChannel(T entity) throws DataAccessException;

	/**
	 * 删除一条渠道信息
	 * 
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	boolean removeChannel(T entity) throws DataAccessException;
	
	
	/**
	 * 根据clientId得到一条渠道信息数据
	 * 
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	T getChannelByClientId(String clientId) throws DataAccessException;
}