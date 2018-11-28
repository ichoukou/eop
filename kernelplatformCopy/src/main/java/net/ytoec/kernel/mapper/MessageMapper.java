/**
 * 2012-4-17上午11:21:43
 * wangyong
 */
package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.dataobject.Message;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 消息管理映射器
 * @author wangyong
 * 2012-4-17
 */
public interface MessageMapper<T extends Message> extends BaseSqlMapper<T> {

//	/**
//	 * 添加消息，并返回该消息的主键id
//	 * @param entity
//	 * @return
//	 */
//	public int addMessage(T entity);
	
	/**
	 * 消息有回复后需要更新时间
	 * @param entity
	 * @throws DataAccessException
	 */
	public void updateMessage(T entity) throws DataAccessException;
	
	/**
	 * 卖家获取消息列表<br>
	 * @param map中键的含义："classify"(required):消息分类（0：所有消息、1：管理员、2：系统消息、3：其他消息）；"userId"(required)：当前卖家用户Id;<br>
	 * 其他键值根据classify值不同而不同，具体参见MessageMapper.xml里<select id="getSellerMessageListByMap">的注释
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<T> getSellerMessageListByMap(Map map);
	
	
	/**
	 * 卖家：根据消息状态、收件人、发件人统计消息列表数目
	 * @param map中键的含义："classify"(required):消息分类（0：所有消息、1：管理员、2：系统消息、3：其他消息）；"userId"(required)：当前卖家用户Id;<br>
	 * 其他键值根据classify值不同而不同，具体参见MessageMapper.xml里<select id="countSellerMessageListByMap">的注释
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public int countSellerMessageListByMap(Map map);
	
	/**
	 * 网点：根据消息状态、收件人、发件人获取消息列表（支持分页）<br>
	 * @param map中键的含义："classify"(required):消息分类（0：所有消息、1：管理员、2：系统消息、3：其他消息）；"userId"(required)：当前网点用户Id;<br>
	 * 其他键值根据classify值不同而不同，具体参见MessageMapper.xml里<select id="getSiteMessageListByMap">的注释
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<T> getSiteMessageListByMap(Map map);
	
	
	/**
	 * 网点：根据消息状态、收件人、发件人统计消息列表
	 * @param map中键的含义："classify"(required):消息分类（0：所有消息、1：管理员、2：系统消息、3：其他消息）；"userId"(required)：当前网点用户Id;<br>
	 * 其他键值根据classify值不同而不同，具体参见MessageMapper.xml里<select id="countSiteMessageListByMap">的注释
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public int countSiteMessageListByMap(Map map);
	
	/**
	 * 管理员：根据消息状态、收件人、发件人获取消息列表（支持分页）
	 * @param map中键的含义："classify"(required):消息分类（0：所有消息、1：管理员、2：网点、3：卖家）；"userId"(required)：当前管理员用户Id;<br>
	 * 其他键值根据classify值不同而不同，具体参见MessageMapper.xml里<select id="getSiteMessageListByMap">的注释
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<T> getAdminMessageListByMap(Map map);
	
	
	/**
	 * 管理员：根据消息状态、收件人、发件人统计消息列表
	 * @param map中键的含义："classify"(required):消息分类（0：所有消息、1：管理员、2：网点、3：卖家）；"userId"(required)：当前管理员用户Id;<br>
	 * 其他键值根据classify值不同而不同，具体参见MessageMapper.xml里<select id="countSiteMessageListByMap">的注释
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public int countAdminMessageListByMap(Map map);
	
	/**
	 * 平台用户：根据消息状态、收件人、发件人统计消息数目:平台用户按“发件箱”、“分仓用户”分类，只获取分仓所发信息（支持分页） ;
	 * @param map中键的含义："classify"(required):消息分类（0：发件箱、1：分仓用户）；"userId"(required):平台用户id和其所有分仓用户及子账号的id;<br>
	 * 其他键值根据classify值不同而不同，具体参见MessageMapper.xml里<select id="getSiteMessageListByMap">的注释
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<T> getPlatMessageListByMap(Map map);
	
	
	/**
	 * 平台用户：根据消息状态、收件人、发件人统计消息数目:平台用户按“发件箱”、“分仓用户”分类，只获取分仓所发信息
	 * @param map中键的含义："classify"(required):消息分类（0：发件箱、1：分仓用户）；"userId"(required):平台用户id和其所有分仓用户及子账号的id;<br>
	 * 其他键值根据classify值不同而不同，具体参见MessageMapper.xml里<select id="countSiteMessageListByMap">的注释
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public int countPlatMessageListByMap(Map map);

	
}
