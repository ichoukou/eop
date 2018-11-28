/**
 * UserThreadMapper.java
 * 2011-10-31 下午01:50:45
 * wangyong
 */
package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.UserCustom;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

import org.springframework.dao.DataAccessException;


public interface UserCustomMapper<T extends UserCustom> extends
	   BaseSqlMapper<T> {
	    
	   public List<T> searchUsers(T entity);
	   
	    /**
	     * 根据用户名，删除custom表中的用户信息
	     * @param userName
	     * @return
	     */
	    public Integer delUserCustom(String userName);
	    
	    /**
	     * 个性化删除：entity里有userName、bindedUserName、type、relationalQuery属性值
	     * @param entity
	     * @throws DataAccessException
	     */
	    public void individuationDelete(T entity) throws DataAccessException;
}
