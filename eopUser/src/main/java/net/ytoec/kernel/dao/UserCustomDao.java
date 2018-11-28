package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.User;

import org.springframework.dao.DataAccessException;


public interface UserCustomDao<T> extends BaseDao<T>{

	public List<T> searchUserCustom(T entity);
    
    /**
     * 根据用户名，删除custom表中的用户信息
     * @param userName
     * @return
     */
    public boolean delUserCustom(String userName);
    
    /**
     * 个性化删除:userCustom里有userName、bindedUserName、type、relationalQuery属性值
     * @return
     */
    public boolean individuationDelete(T userCustom);
}