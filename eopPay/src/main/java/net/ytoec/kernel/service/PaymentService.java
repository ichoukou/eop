package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.AccountUser;
import net.ytoec.kernel.dataobject.DredgeService;
import net.ytoec.kernel.dataobject.Payment;
import net.ytoec.kernel.dataobject.ServiceHistory;


/**
 * 支付service
 * @author guoliang.wang
 * @param <T>
 */
public interface PaymentService  <T extends Payment>  extends BaseService<T>{

	/**
	 * 查询交易（支付）明细表列表总记录数
	 * @param Map  查询条件拼成的map       
	 */	
	public Integer getPaymentListCount(Map map);

	/**
	 * 查询交易（支付）明细表列表
	 * @param Map  查询条件拼成的map       
	 * @Pagination 分页信息
	 * @flag 是否分页
	 * @return List
	 */
	@SuppressWarnings("all")
	public  List<T> getPaymentList(Map map, Pagination pagination, boolean flag);

	/**
	 * 通过id修改remark标记
	 * @param T
	 */
	public boolean updateRemarkById(T Payment);

	/**
	 * 通过id修改del_flag
	 * @param T
	 */
	public boolean updateDeleteFlagById(T Payment);

	/**注：充值 与 付款 (余额不足情况) 调用此方法[支付宝的账号信息传过来]
	 * 充值(:map里面需传payment对象的值过来)付款(：map里面要传三个对象过来)
	 * 对订单进行账户充值 第一步调用add方法生成一条订单
	 *  第二步调支付宝接口
	 * (充值成功后支付宝返回后) 第三步修改账户余额
	 * @param map 里面存三个对象([payment 、 dredgeService、serviceHistory]alipayConfigMessage)
	 * 对象：map.put("payment",Payment) map.put("dredgeService",DredgeService)
	 * map.put("alipayConfigMessage",AlipayConfigMessage) AlipayConfigMessage是支付宝账号的配置信息
	 * Payment 对象的值(userId、auserId、dealMoney、dealType、dealName)这些值必须传
	 * dredgeService、 尽量多传值
	 * serviceHistory(全部值[id可能除外])
	 * @return 生成请求URL
	 */
	public String dealPayment(Map<String,Object> map);

	
	/**
	 * 查询交易（支付）根据用户id
	 * 未付款的订单(超过7天删除) 
	 */
	@SuppressWarnings("all")
	public  List<T> getPaymentListByUserId(Map map);
	
	/**
	 * 自动续费的步骤：1）修改账户表-ec_core_accountUser 2）修改开通充值服务表-ec_core_dredgeService
	 * 3）修改交易（支付）明细表-ec_core_payment 插入一条记录
	 * 4)插入服务记录历史表-ec_core_serviceHistory
	 */
	public Boolean autoPayment(ServiceHistory serviceHistory,DredgeService dredgeService);
	
	
	/**
	 * 自动续费
	 *批量修改交易状态为：等待付款（交易（支付）明细表-ec_core_payment）
	 */
	@SuppressWarnings("all")
	public boolean updateBatchByDealStatus(Map map);
	
	/**
	 * 交易提醒 余额不足提醒
	 * 公用方法
	 */
	public boolean sendSMSByUserId(Integer userId,String serviceName,DredgeService dredgeService, String isSend);
	
	/**
	 * 注：支付返回 调用易通
	 * params 的参数包括：
	 * trade_no（支付宝交易号）
	 * out_trade_no（获取订单号）说明此订单号 与交易（支付）明细表 的id 对应
	 * total_fee（获取总金额）
	 * subject（商品名称、订单名称）
	 * body（商品描述、订单备注、描述）
	 * buyer_email（买家支付宝账号）
	 * trade_status（交易状态）
	 */
	public boolean alipayReturn(Map<String,String> params);
	
	/**
	 * 注：支付返回 调用易通(异步)
	 * params 的参数包括：
	 * trade_no（支付宝交易号）
	 * out_trade_no（获取订单号）说明此订单号 与交易（支付）明细表 的id 对应
	 * total_fee（获取总金额）
	 * subject（商品名称、订单名称）
	 * body（商品描述、订单备注、描述）
	 * buyer_email（买家支付宝账号）
	 * trade_status（交易状态）
	 */
	public boolean alipayNotify(Map<String,String> params);
	
	/**
	 * 根据id降序排列
	 * @param map(userId,dealStatus,delFlag,dealName)
	 * @return
	 */
	public List<T> getList(Map map); 
	
	/**
	 * 根据id 更新dealMoney
	 * @param payment
	 * @return
	 */
	public boolean updateDealMoneyById(Payment payment);
	
	/***
	 *  首先就是关闭这个订单，旧订单做flag=5逻辑删除
	 *  然后就是重新付款
	 *  这个时候需要更新账户表
	 *  插入交易明细表（一条新订单）
	 *  更新服务历史记录表(主要是更新交易明细id payId)
	 *  然后把订单传入到支付宝
	 *  作用：处理支付宝交易金额不一致的情况
	 *  
	 *  
	 *  例如：同一条订单第一次提交到支付宝的付款金额是50，
	 *        但是在交易明细支付页面第二次提交时的金额是30，
	 *        主要是由于在第一次提交订单之后又对账户进行在线充值20所导致的
	 */
	public Map<String, Object> add_Alipay(Payment payment,ServiceHistory serviceHistory,DredgeService dredgeService,AccountUser accountUser,Integer createrId);
	
	/**
	 * 判断短信余额是否充足
	 * @param userId
	 * @return
	 */
	public Double sendMessageByUserId(Integer userId);
}
