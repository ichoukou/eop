/**
 * net.ytoec.kernel.service
 * UserRelationService.java
 * 2012-9-25下午05:24:54
 * @author wangyong
 */
package net.ytoec.kernel.service;

import java.util.List;

import net.ytoec.kernel.dataobject.User;

import org.springframework.dao.DataAccessException;

/**
 * @author wangyong
 * 2012-9-25
 */
public interface UserRelationService<T> extends BaseService<T> {
	
	/**
	 * 根据主键获取对象
	 * @param id
	 * @return
	 */
	public T getById(Integer id);
	
	/**
	 * 根据用户id获取该用户所关联的用户 :关联关系是双向关联的:所以在sql中userId既作为user_id的参数也作为related_user_id的参数
	 * @param userId
	 * @return
	 */
	public List<T> searchByUserId(int userId);
	
	/**
	 * 查找两个用户是否存在关联关系：entity对象里面必须要有userId和relatedUserId属性的值，分别表示两个关联账户的id
	 * @param userIdOne
	 * @param userIdTwo
	 * @return
	 */
	public List<T> searchRelation(Integer userIdOne, Integer userIdTwo);

	/**
	 * 取消关联关系:userOne、userTwo对象里面必须要有userId和relatedUserId属性的值，分别表示将要取消的两个关联账户的id<br/>
	 * 取消关联之后需要删除个性化配置中相关数据
	 * @param userOne
	 * @param userTwo
	 * @return
	 */
	public boolean cancelRelation(User userOne, User userTwo);
}
