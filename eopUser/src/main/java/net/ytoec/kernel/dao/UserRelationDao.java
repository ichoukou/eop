/**
 * net.ytoec.kernel.dao
 * UserRelationDao.java
 * 2012-9-25下午04:53:48
 * @author wangyong
 */
package net.ytoec.kernel.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

/**
 * 
 * @author wangyong
 * 2012-9-25
 */
public interface UserRelationDao<T> extends BaseDao<T> {
	
	/**
	 * 根据用户id获取该用户所关联的用户 :关联关系是双向关联的:所以在sql中userId既作为user_id的参数也作为related_user_id的参数
	 * @param userId
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> searchByUserId(int userId) throws DataAccessException;
	
	/**
	 * 查找两个用户是否存在关联关系：entity对象里面必须要有userId和relatedUserId属性的值，分别表示两个关联账户的id
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> searchRelation(T entity) throws DataAccessException;

	/**
	 * 取消关联关系:entity对象里面必须要有userId和relatedUserId属性的值，分别表示将要取消的两个关联账户的id
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public boolean cancelRelation(T entity) throws DataAccessException;
}
