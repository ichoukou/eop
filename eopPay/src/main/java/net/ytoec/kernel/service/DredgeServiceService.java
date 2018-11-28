package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.DredgeService;

/**
 * 开通服务service
 * @author guoliang.wang
 * @param <T>
 */
public interface DredgeServiceService <T extends DredgeService>  extends BaseService<T>{

	/**
	 * 通过用户ID修改服务的 低于余额
	 * @param T（usrid,serviced）必须,sql 要设置好判断是否为空 ，为空的不修改
	 * @return boolean
	 */
	public boolean updateDredgeserviceBalanceById(T dredgeService);

	/**
	 * 查询服务列表
	 * 
	 * @param Map
	 * 查询条件拼成的map
	 * @Pagination 分页信息
	 * @flag 是否分页
	 * @return boolean
	 */
	@SuppressWarnings("all")
	public List<T> getDredgeserviceList(Map map, Pagination pagination,
			boolean flag);
	
	/**
	 * 查询服务列表总记录数
	 */
	public Integer getDredgeserviceListCount(Map map);
	
	 /**
	 *开通的服务列表
	 * 注：查询状态为1启用
	 * 定时器用到	
	 * @return List<T>
	 */
	public List<T> getOpenserviceTimeList();
	

	/**
	 *根据用户id修改余额
	 * @param T
	 * @return 
	 * 注：此方法不用
	 */
	public boolean updateBalanceByUserId(T dredgeService);



	/**
	 * 修改服务状态 0创建1启用2停用3到期 4 用户关闭
	 * （需修改用户的修改时间）
	 * @param T
	 * @return boolean
	 */
	public boolean updateFlagById(T dredgeService);


     /**
	 *开通的服务列表
	 * 注：查询状态为1启用
	 * @param userid 用户id
	 * @return List<T>
	 */
	public List<T> getOpenserviceList(Integer userId);
	
	  /**
	  * 服务列表	
	 * @return DredgeService
	 */
	@SuppressWarnings("all")
	public List<T> getServiceRecordByUserId(Map map);
	
	/**
	 * (注：余额充值调用此方法不足调用其他方法)
	 * 当点击付款的时候 余额充足 首先要查询出来已开通服务，看看这个服务是否已经存在 如果这个服务已经开通过 需要更新
	 * flag == 1	 
	 * 更新已开通服务表 插入交易明细表 插入服务 历史记录表 更新账户表 key dredgeService value dredgeService
	 * //已开通服务 key payment value payment //交易明细 key serviceHistory value
	 * serviceHistory //服务历史记录 key accountUser value accountUser //账户
	 * 
	 *flag == 2 
	 *当点击付款的时候 余额充足 当用户在服务管理页面 点击付款的时候 如果付款成功的话 需要 如果这个服务没有开通过 更新账户表
	 * 插入已开通服务表 插入历史记录表 插入交易明细表 key payment value payment //交易明细表 key
	 *serviceHistory value serviceHistory //历史记录表 key accountUser value
	 * accountUser //账户表 key dredgeService value dredgeService //已开通服务	 * 
	 * 余额不足调用其他方法
	 * @return boolean
	 */
	public boolean openService(Map<String, Object> map,String type);
	
	/**
	 * 插入提醒的时候
	 * @return
	 */
	public boolean insertBatch(List<DredgeService> list);
	
	/**
	 * 更新提醒标识 以及低于余额，短信不足条数
	 * 服务首页点击保存用到
	 */
	public boolean updateBatch(List<DredgeService> list);
	
	/**
	 * 判断用户是否开通了某项服务
	 * @param map(userId,serviceName)
	 * @return boolean
	 */
	public boolean getServiceByUserIdAndServiceName(Map map);
	
	/**
	 * 根据name得到已开通服务表
	 * @param userid
	 * @return
	 */
	public List<T> getOpenserviceListByName(Integer userid,List<Integer> list);
}
