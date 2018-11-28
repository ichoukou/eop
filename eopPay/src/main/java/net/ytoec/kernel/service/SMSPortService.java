package net.ytoec.kernel.service;

import java.util.Map;

import com.techcenter.protocol.standard12.Standard_Submit;

import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dto.SMSMessage;


/**
 * 短信接口service
 * @author guoliang.wang
 * @param <T>
 */
public interface SMSPortService {
	
	/**
	 * 提供短信是否开启接口：
	 * 返回Boolean（查询开通服务表是否有记录） 
	 * @param userId
	 * 注：userId 为主账号id(分仓账号可以使用平台)
	 * @return boolean
	 */
	public boolean supplyStatusOpenUp(Integer userId);
	
	/**
	 * 提供短信是否开启接口：
	 * 返回Boolean（查询开通服务表是否有记录） 
	 * @param userId
	 * 注：userId 为主账号id(分仓账号可以使用平台)
	 * @return boolean
	 */
	public boolean SMSStatusOpenUp (Integer userId);
	/**
	 * 根据userId 查询主账号
	 * @param userId
	 * @return
	 */
	public User getUserById(Integer userId);
	/**
	 * 是否启用
	 * @param userId
	 * @return
	 */
	public boolean sourceStatusOpenUp(int userId);
	
	public boolean parentStatusOpenUp(int userId);
	
	/**
	 * 提供短信相关的信息接口：
	 * 可用短信数量、开始时间、结束时间 返回一个小的对象（查询开通的服务表记录）
	  * 注：userId 为主账号id
	 * @return SMSMessage(private Integer smsUsecount;private java.util.Date beginDate;private java.util.Date endDate;)
	 */
	public SMSMessage supplySMSMessage(Integer userId);
	
	
	/**
	 * 提供短信相关的信息接口：
	 * @param Map 的值：（Integer）userId、(Integer) SMSnum,(double) prince,(String) name,String softwareSerialNo,String  key
	 * name 名称:短信体验套餐\短信初级短信套餐\短信中级套餐\短信高级套餐\ 其它
	  * 第一步：修改账号表余额，可用余额 。第二步：修改开通服务表 短信数量。第三步：插入支付表
	 * 第四步：插入服务历史记录表
	 * @return Integer(0 成功、1失败、2用户不存在、3 余额不足4 代表请稍后重试（记录已经被更改） 5、其它 )
	 *注：userId  用户id
	 */
	public Integer supplyRecharge(Map map);
	
	
	/**
	 * 将短信信息保存到ec_core_paySMS表中,等待发送
	 * 必填项：
	 *     1.userId         (与EC_CORE_USER的id(主账号)保持一致)
	 *     2.destMobile     (要发送的目标手机)
	 *     3.messageContent (短信发送内容)
	 *     4.sequenceID     (唯一)
	 *     5.smsType        5短信模块 [其它模块待定]
	 *  非必输项：
	 *      2. sendMobile   (发送的手机号)
	 * @param obj
	 * @return boolean
	 * false:失败
	 * true:成功
	 * 注：(提供一个action方法来接收短信的发送状态参考SMSSendtoOtherServiceImpl类的)
	 */
	public boolean saveSMSInfo(SMSObject obj,int user_id);
	
	/**
	 * 将短信信息保存到ec_core_paySMS表中,等待发送
	 * 必填项：
	 *     1.userId         (与EC_CORE_USER的id(主账号)保持一致)
	 *     2.destMobile     (要发送的目标手机)
	 *     3.messageContent (短信发送内容)
	 *     4.sequenceID     (唯一)
	 *     5.smsType        5短信模块 [其它模块待定]
	 *  非必输项：
	 *      2. sendMobile   (发送的手机号)
	 * @param obj
	 * @return boolean
	 * false:失败
	 * true:成功
	 * 注：(提供一个action方法来接收短信的发送状态参考SMSSendtoOtherServiceImpl类的)
	 */
	public boolean saveSMSInfo(SMSObject obj);
	
	/**
	 * 判断短信条数是否充足
	 * @param userId 主账号id
	 * @return
	 */
	public Integer smsLessForRemaind(Integer userId);
	/**
	 * 余额不足提醒
	 */
	public boolean sendSMSForMoney(Integer userId,String serviceName);

}
