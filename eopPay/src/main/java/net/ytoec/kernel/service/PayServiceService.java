package net.ytoec.kernel.service;

import java.util.List;

import net.ytoec.kernel.dataobject.PayService;

/**
 * 服务service
 * @author guoliang.wang
 *
 * @param <T>
 */
public interface PayServiceService <T extends PayService>  extends BaseService<T>{

	  // 服务管理界面需要查询服务接口来判断哪些服务没有开通。
	/**
	 * 未开通的服务列表
	 * 第一步 调用开通服务表，查看有哪些开通的服务（调用dao）
	 * @param userid 用户id
	 * arr为null,说明此用户还没有开通任何服务
	 * @return List<T>
	 */
	public List<T> getNOpenserviceList(Integer userid);
	
	/**
	 * 查询所有的收费的服务(短信除外)
	 * @return
	 */
	public List<T> getNFreeserviceList();
	
	/**
	 * 查询所有的已经开通的服务列表
	 * @return
	 */
	public List<T> getAllOpenServiceList();

}
