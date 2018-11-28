package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.PayService;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * Mapper
 * @author guoliang.wang
 *
 * @param <T>
 */
public interface PayServiceMapper<T extends PayService> extends BaseSqlMapper<T> {

	  // 服务管理界面需要查询服务接口来判断哪些服务没有开通。
	/**
	 * 未开通的服务列表
	 * 
	 * @param userid 用户id,arr  为已经开通服务的id集合，
	 * arr为null,说明此用户还没有开通任何服务
	 * Map (List<Integer> arr)
	 * @return List<T>
	 */
	public List<T> getNOpenserviceList(Map map);
	
	/**
	 * 查询免费的服务列表
	 * @return
	 */
	public List<T> getFreeServiceList();
	
	/**
	 * 查询所有的收费的服务(短信除外)
	 * @return
	 */
	public List<T> getNFreeserviceList();
	
	/**
	 * 查询所有的已经开通的服务列表(免费服务除外)
	 * @return
	 */
	public List<T> getAllOpenServiceList();
}
