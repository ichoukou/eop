package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.dataobject.PosttempUser;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 
 * @author ChenRen
 * @date 2011-09-09
 */
public interface PosttempUserMapper<T extends PosttempUser> extends BaseSqlMapper<T> {

	/**
	 * 根据 VIP用户Id(Id主键) 获取运费模板用户关系数据
	 * 
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getPosttempUserByVipId(Map map);
	
	/**
	 * 根据 VIP用户Id(Id主键)集合 获取运费模板用户关系数据
	 * 
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getPosttempUserByVipIds(Map map);
	
	/**
	 * 根据 运费模板Id 获取运费模板用户关系数据
	 * 
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getPosttempUserByPostId(Integer id);

	/**
	 * 根据模板Id删除运费模板用户关系
	 * 
	 * @param postId
	 * @return	返回受影响的行数
	 */
	public Integer delPosttempUserByPostId(Integer postId);
	
	/**
	 * 根据 网点 获取运费模板用户关系数据
	 * 
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getPosttempUserByBranchId(Integer id) throws DataAccessException;

	/**
	 * 根据平台用户所对应的分仓的直客用户编码集合,批量查找运费模板用户关系
	 * 
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getPosttempUserByUserCodes(Map map);

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
