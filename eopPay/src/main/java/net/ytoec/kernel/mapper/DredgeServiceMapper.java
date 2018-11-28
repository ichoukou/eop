package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;
import net.ytoec.kernel.dataobject.DredgeService;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * Mapper
 * @author guoliang.wang
 * @param <T>
 */
public interface DredgeServiceMapper<T extends DredgeService> extends BaseSqlMapper<T> {

	/**
	 * 通过用户ID修改服务的 低于余额
	 * @param T（id,usrid,serviced,balance）必须,sql 要设置好判断是否为空 ，为空的不修改
	 */
	public void updateDredgeserviceBalanceById(T dredgeService);

	/**
	 * 查询服务列表
	 * @param Map
	 * @return List
	 */
	public List<T> getDredgeserviceList(Map map);
	
	/**
	 * 查询服务列表总记录数
	 */
	public Integer getDredgeserviceListCount(Map map);

	/**
	 *根据用户id修改余额
	 * @param T
	 * @return 
	 */
	public void updateBalanceByUserId(T dredgeService);

	/**
	 * 修改服务状态 0创建1启用2停用3到期 4 用户关闭
	 * （需修改用户的修改时间）
	 * @param T
	 * @return boolean
	 */
	public void updateFlagById(T dredgeService);


    /**
	 * 开通的服务列表
	 * 注：查询状态为1启用
	 * @param userid 用户id
	 * @return List<T>
	 */
	public List<T> getOpenserviceList(Map map);
	
	/**
	  * 服务列表	
      * @return DredgeService
	  */
	public List<T> getServiceRecordByUserId(Map map);

	/**
	 * 短信调用，修改短信相关的信息
	 * 
	 * @return boolean
	 */
	public Integer updateDredgeserviceBySMS(T Dredgeservice);

	/**
	 * 批量添加
	 * @param list
	 */
	public void insertBatch(List<T> list);
	
	 /**
	 *开通的服务列表
	 * 注：查询状态为1启用
	 * 定时器用到	
	 * @return List<T>
	 */
	public List<T> getOpenserviceTimeList();
	
	/**
	 * 更新提醒标识 以及低于余额，短信不足条数
	 * 服务首页点击保存用到,批量更新
	 */
	public boolean updateBatch(List<DredgeService> list);
	
	
	/**
	 *根据id,serviceId,flag(主要用于支付、付款操作)
	 * 
	 * @return boolean
	 */
	public boolean updateDredgeserviceUseAlipay(T Dredgeservice);

	/**
	 * 判断用户是否开通了某项服务
	 * @param map(userId,serviceName)
	 * @return Integer
	 */
	public Integer getServiceByUserIdAndServiceName(Map map);
	
	/**
	 * 根据名称
	 * @param list
	 * @return
	 */
	public List<T> getOpenserviceListByName(List<DredgeService> list);
}
