package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.Postinfo;
import net.ytoec.kernel.dataobject.Posttemp;
import net.ytoec.kernel.dataobject.PosttempUser;
import net.ytoec.kernel.dataobject.User;

/**
 * 运费模板
 * 
 * @author ChenRen
 * @date 2011-09-09
 * 
 * @param <T>
 */
public interface PosttempService<T> {

	/**
	 * 创建运费模板
	 * 
	 * @param entity
	 * @param vipIds
	 *            运费模板关联的 VIP用户的id主键字符串; 多个用";"连接
	 * @return @
	 */
	public boolean addPosttemp(T entity, String vipIds);

	/**
	 * 根据Id读取单个运费模板
	 * 
	 * @param id
	 * @return @
	 */
	public T getPosttempById(Integer id);
	
	/**
	 * 运费模板编辑
	 * 
	 * @param utype
	 * @param id
	 * 
	 * @param entity
	 * @return @
	 */
	public T toPosttempEdit(T entity, Integer id, String utype);

	/**
	 * 更新运费模板
	 * 
	 * @return
	 */
	public boolean editPosttemp(T entity);
	
	/**
	 * 更新运费模板
	 * 
	 * @return
	 */
	public boolean editPosttemp(User currentUser,T entity);

	/**
	 * 删除运费模板
	 * 
	 * @param id
	 * @return
	 */
	public boolean delPosttemp(T entity);

	/**
	 * 根据用户id查询用户的运费模板
	 * 
	 * @param userId
	 *            用户Id主键
	 * @param pagination
	 * @param userType
	 *            1(VIP用户)、2(网店用户) 3(渠道信息员(管理员)、4(大商家)
	 * @return @
	 */
	public Map getPosttempByUserId(Integer userId, Pagination<T> pagination,
			String userType);
	
	/**
	 * 根据用户id查询用户的运费模板
	 * 
	 * @param userId
	 *            用户Id主键
	 * @param userType
	 *            1(VIP用户)、2(网店用户) 3(渠道信息员(管理员)、4(大商家)
	 * @return @
	 */
	public List<T> getPosttempByUserId(Integer userId, String userType);
	
	/**
	 * 根据VipId取用户的官方标准模板
	 * 
	 * @param vipId
	 * @return @
	 */
	public T getSysPosttempByVipId(Integer uid);
	
	/**
	 * 根据VipId取用户的官方标准模板
	 * 
	 * @param vipId
	 * @return @
	 */
	public T getSysPosttempBySiteId(Integer uid);

	public T viewPTByVip(String vipId);
	public T toPosttempView2(String usercode);
	public T getPosttempByVipId(String vipId);

	public Postinfo queryPostinfoByProv(User cuser, int destId);
	
	public void clearPostForRepeatUser(Integer compareId,Integer siteId);
	
	public List<PosttempUser> getPosttempUserByBranchIdAndVipId(Integer branchId, Integer vipId);
}
