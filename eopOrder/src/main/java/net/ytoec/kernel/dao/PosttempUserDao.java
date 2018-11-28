package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

/**
 * 运费模板用户关系
 * 
 * @author ChenRen
 * @date 2011-09-09
 * 
 * @param <T>
 */
public interface PosttempUserDao<T> {
	
	/**
	 * 创建运费模板用户关系
	 * 
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	public boolean addPosttempUser(T entity) throws DataAccessException;

	/**
	 * 根据Id读取单个运费模板用户关系
	 * 
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public T getPosttempUserById(Integer id) throws DataAccessException;
	
	/**
	 * 根据运费模板Id读取单个运费模板用户关系
	 * 
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getPosttempUserByPostId(Integer id) throws DataAccessException;

	/**
	 * 根据 VIP用户Id(Id主键) 获取运费模板用户关系
	 * 
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getPosttempUserByVipId(Map map) throws DataAccessException;
	
	/**
	 * 根据VIP用户ID集合,批量查找运费模板用户关系
	 */
	public List<T> getPosttempUserByVipIds(Map map) throws DataAccessException;
	
	
	/**
	 * 根据平台用户所对应的分仓的直客用户编码集合,批量查找运费模板用户关系
	 *	 
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getPosttempUserByUserCodes(Map map) throws DataAccessException;
	
	
	/**
	 * 根据 网点 获取运费模板用户关系数据
	 * 
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getPosttempUserByBranchId(Integer id) throws DataAccessException;
	/**
	 * 更新运费模板用户关系
	 * 
	 * @return
	 */
	public boolean editPosttempUser(T entity) throws DataAccessException;

	/**
	 * 删除运费模板用户关系
	 * 
	 * @param id
	 * @return
	 */
	public boolean delPosttempUser(T entity) throws DataAccessException;
	
	/**
	 * 根据模板Id删除运费模板用户关系
	 * 
	 * @param postId
	 * @return
	 */
	public boolean delPosttempUserByPostId(Integer postId) throws DataAccessException;

	
	/**
	 * 根据网点编号分页查询用户模板关系表
	 * @param map
	 * @return
	 */
    public List<T> getPosttempUserByBranchIdMap(Map map);
    
    public List<T> getPosttempUserByBranchIdAndVipId(Map map);

    /**
     * 子账号登陆时获取运费模板
     * @param map
     * @return
     */
    public List<T> getPosttempUserByContractVipIdMap(Map map);
}
